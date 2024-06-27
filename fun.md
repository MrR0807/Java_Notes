# Production code in one my workplaces

```
public class EventStats {

	private final Integer readParallelism;
	private final Integer writeParallelism;
	private final Integer messagesPerMinute;
	private final Integer messageSize;

	public EventStats() {

		this.readParallelism = PARTITIONS_NUMBER;
		this.writeParallelism = WRITE_PARALLELISM;
		this.messagesPerMinute = MESSAGES_PER_MINUTE;
		this.messageSize = MESSAGE_SIZE;
	}

	public Integer getReadParallelism() {

		return readParallelism;
	}

	public Integer getWriteParallelism() {

		return writeParallelism;
	}

	public Integer getMessagesPerMinute() {

		return messagesPerMinute;
	}

	public Integer getMessageSize() {

		return messageSize;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		EventDefaultStatistic that = (EventStats) o;

		return Objects.equals(readParallelism, that.readParallelism) &&
				Objects.equals(writeParallelism, that.writeParallelism) &&
				Objects.equals(messagesPerMinute, that.messagesPerMinute) &&
				Objects.equals(messageSize, that.messageSize);
	}

	@Override
	public String toString() {

		return "EventStats{" +
				"readParallelism=" + readParallelism +
				", writeParallelism=" + writeParallelism +
				", messagesPerMinute=" + messagesPerMinute +
				", messageSize=" + messageSize +
				'}';
	}
}
```


```
@TestOwner(teams = {Team.NOTIFICATIONS})
public class EventStatsTest {

	@Test
	public void test() {

		// execute
		final EventStats defaultStatistic = new EventStats();

		// verify
		assertEquals(PARTITIONS_NUMBER, defaultStatistic.getReadParallelism());
		assertEquals(Integer.valueOf(WRITE_PARALLELISM), defaultStatistic.getWriteParallelism());
		assertEquals(Integer.valueOf(MESSAGES_PER_MINUTE), defaultStatistic.getMessagesPerMinute());
		assertEquals(Integer.valueOf(MESSAGE_SIZE), defaultStatistic.getMessageSize());
	}
}
```

```
public final class EventStatsConfig {

	public static final Integer WRITE_PARALLELISM = 1;

	public static final Integer MESSAGES_PER_MINUTE = 0;

	public static final Integer MESSAGE_SIZE = 1;

	public static final Integer PARTITIONS_NUMBER = 3;
}
```
