public class SimpleWebServerWithProm {

	private static final CompositeMeterRegistry compositeMeterRegistry = new CompositeMeterRegistry(Clock.SYSTEM);

	public static void main(String[] args) throws IOException {

		initializeMetrics();

		HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
		server.createContext("/health", new MetricsController());
		server.start();
		System.out.println("Server is ready to receive requests");
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
