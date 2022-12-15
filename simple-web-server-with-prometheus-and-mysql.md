```java
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.NoSuchElementException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

public class SimpleWebServerWithProm {

	private static final CompositeMeterRegistry compositeMeterRegistry = new CompositeMeterRegistry(Clock.SYSTEM);
	private static final MySqlConnector mysqlConnector = new MySqlConnector(compositeMeterRegistry);
	private static final ScheduledExecutorService metricExecutor = Executors.newScheduledThreadPool(1);
	private static final ScheduledExecutorService insertExecutor = Executors.newScheduledThreadPool(1);

	public static void main(String[] args) throws IOException {

		initializeMetrics();

		HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
		server.createContext("/health", new MetricsController());
		server.start();
		System.out.println("Server is ready to receive requests");
		metricExecutor.scheduleAtFixedRate(mysqlConnector::metrics, 0, 5, TimeUnit.SECONDS);
		insertExecutor.scheduleAtFixedRate(mysqlConnector::insert, 0, 10, TimeUnit.MILLISECONDS);
	}

	private static void initializeMetrics() {

		registerJvmMetrics();
		final var meterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
		compositeMeterRegistry.add(meterRegistry);
	}
	private static void registerJvmMetrics() {

		new ClassLoaderMetrics().bindTo(compositeMeterRegistry);
		new JvmMemoryMetrics().bindTo(compositeMeterRegistry);
		new JvmGcMetrics().bindTo(compositeMeterRegistry); //NOSONAR
		new ProcessorMetrics().bindTo(compositeMeterRegistry);
		new JvmThreadMetrics().bindTo(compositeMeterRegistry);
	}
	private static class MetricsController implements HttpHandler {

		public void handle(HttpExchange exchange) throws IOException {

			try (OutputStream responseBody = exchange.getResponseBody()) {
				System.out.println("Request received");

				String payload = getMeterRegistry(PrometheusMeterRegistry.class).scrape();

				exchange.sendResponseHeaders(200, payload.length());
				responseBody.write(payload.getBytes());
			}
		}
	}

	private static <T extends MeterRegistry> T getMeterRegistry(Class<T> meterRegistryType) {

		return compositeMeterRegistry.getRegistries().stream()
				.filter(meterRegistry -> meterRegistryType.isAssignableFrom(meterRegistryType))
				.map(meterRegistryType::cast)
				.findFirst()
				.orElseThrow(() -> new NoSuchElementException("Meter registry not found"));
	}
}
```

```java

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import javax.sql.DataSource;

import com.mysql.cj.jdbc.MysqlDataSource;

import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.MeterRegistry;

/**
 * docker run --name test-metrics -e MYSQL_ROOT_PASSWORD=password -d -p 3307:3306 mysql:5.7.33
 *
 * ```
 * CREATE  TABLE `test`.`belekas` (
 *
 *   `ID` VARCHAR(64) NOT NULL ,
 *   `NAME` VARCHAR(128) NOT NULL ,
 *   `CREATIONDATE` DATETIME NULL ,
 *   `LASTMODIFIEDDATE` DATETIME NULL ,
 *
 *   PRIMARY KEY (`ID`)
 * ) ENGINE=INNODB DEFAULT CHARSET=UTF8;
 * ```
 *
 *
 *
 * docker exec -it test-metrics /bin/bash
 */

public class MySqlConnector {

	private final DataSource dataSource;
	private final MeterRegistry meterRegistry;

	private final AtomicLong dataLengthIndexLengthBytesSizeGauge = new AtomicLong(0);
	private final AtomicLong innodbFileSizeGauge = new AtomicLong(0);

	private static final String INSERT = "INSERT INTO test.belekas (ID, NAME, CREATIONDATE, LASTMODIFIEDDATE) VALUES (?, ?, ?, ?)";

	private static final String STRATEGY_ONE = """
 			SELECT (DATA_LENGTH + INDEX_LENGTH) AS TABLE_SIZE FROM information_schema.TABLES WHERE table_name = "belekas";""";

	private static final String STRATEGY_TWO = """
			SELECT FILE_SIZE FROM INFORMATION_SCHEMA.INNODB_SYS_TABLESPACES WHERE name="test/belekas";""";

	public MySqlConnector(MeterRegistry meterRegistry) {

		final var mysqlDataSource = new MysqlDataSource();
		mysqlDataSource.setServerName("localhost");
		mysqlDataSource.setPortNumber(3307);
		mysqlDataSource.setUser("root");
		mysqlDataSource.setPassword("password");

		this.dataSource = mysqlDataSource;
		this.meterRegistry = meterRegistry;

		meterRegistry.gauge("table_size", List.of(new ImmutableTag("strategy", "data_length_plus_index_length_bytes_size")),
				dataLengthIndexLengthBytesSizeGauge, AtomicLong::doubleValue);
		meterRegistry.gauge("table_size", List.of(new ImmutableTag("strategy", "innodb_file_size")),
				innodbFileSizeGauge, AtomicLong::doubleValue);
	}

	public void metrics() {

		try (var connection = dataSource.getConnection();
			 final var statement = connection.createStatement()) {

			final var resultSet = statement.executeQuery(STRATEGY_ONE);
			processStrategyOne(resultSet);

			final var resultSet1 = statement.executeQuery(STRATEGY_TWO);
			processStrategyTwo(resultSet1);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void processStrategyOne(ResultSet resultSet) throws SQLException {
		while (resultSet.next()) {
//			final var dataLength = resultSet.getLong("DATA_LENGTH");
//			final var indexLength = resultSet.getLong("INDEX_LENGTH");
			final var tableSize = resultSet.getLong("TABLE_SIZE");
			dataLengthIndexLengthBytesSizeGauge.set(tableSize);

//			System.out.println("Data: %d Index: %d".formatted(dataLength, indexLength));
			System.out.println("Table size: %d".formatted(tableSize));
		}
	}

	private void processStrategyTwo(ResultSet resultSet) throws SQLException {
		while (resultSet.next()) {
			final var fileSize = resultSet.getLong("FILE_SIZE");
			innodbFileSizeGauge.set(fileSize);

			System.out.println("File: %d".formatted(fileSize));
		}
	}

	public void insert() {

		try (var connection = dataSource.getConnection()) {

			final var preparedStatement = connection.prepareStatement(INSERT);
			preparedStatement.setString(1, UUID.randomUUID().toString());
			preparedStatement.setString(2, UUID.randomUUID().toString());
			preparedStatement.setTimestamp(3, randomDate());
			preparedStatement.setTimestamp(4, Timestamp.from(Instant.now()));
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static Timestamp randomDate() {

		final var year = new Random().nextInt(2020, 2023);
		return Timestamp.valueOf(LocalDateTime.of(year, 1, 1, 0, 0));
	}
}
```
