# Guide to resilience4j

[Resilience4j Github](https://github.com/resilience4j/resilience4j)

[Documentation](https://resilience4j.readme.io/docs/getting-started)

## Server

```
public class Server {

    private static int EXCEPTION_TIMER = 0;

    public static void main(String[] args) throws IOException {
        var server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.setExecutor(Executors.newSingleThreadExecutor()); //For easier scenarios (like timeouts) setup.
        server.createContext("/", new HelloController());
        server.start();
        System.out.println("Server is ready to receive requests");
    }

    private static class HelloController implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("Request received");
            EXCEPTION_TIMER++;
            if (EXCEPTION_TIMER % 3 == 0) {
                sleepSeconds();
                respond(exchange, 200, "Hello");
            } else if (EXCEPTION_TIMER % 2 == 0) {
                respond(exchange, 404, "Not found");
            } else {
                respond(exchange, 200, "Hello");
            }
        }

        private void sleepSeconds() {
            try {
                System.out.println("Sleeping");
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void respond(HttpExchange exchange, int statusCode, String body) throws IOException {
            try (OutputStream responseBody = exchange.getResponseBody()) {
                System.out.println("Responding with " + statusCode);
                exchange.getResponseHeaders().set("Content-Type", "text/plain");
                exchange.sendResponseHeaders(statusCode, body.length());
                responseBody.write(body.getBytes(StandardCharsets.UTF_8));
            }
        }
    }
}
```

## No resilience4j client

```
public class ResilientClient {

    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    public static void main(String[] args) throws IOException, InterruptedException {
        callServer();
        callServer();
        callServer();
    }

    private static void callServer() throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder(URI.create("http://localhost:8080"))
                .POST(HttpRequest.BodyPublishers.ofString("hello"))
                .build();

        var response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.printf("Status code: %d%n", response.statusCode());
        System.out.printf("Response body: %s%n", response.body());
        System.out.printf("Response: %s%n", response);
    }
}
```

## Core resilience4j modules

### Retry

#### Tracking exponentialBackoff policy

```
public class ResilientClient {

    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static Instant now;

    public static void main(String[] args) throws Throwable {
        var retryConfig = RetryConfig.<HttpResponse<String>>custom()
                .maxAttempts(5)
                .intervalFunction(IntervalFunction.ofExponentialBackoff(Duration.ofSeconds(1L)))
                .build();

        var retryRegistry = RetryRegistry.of(retryConfig);
        var retry = retryRegistry.retry("test");

        retry.getEventPublisher()
                .onEvent(event -> {
                    System.out.println("Event");
                    if (now == null) {
                        now = Instant.now();
                    } else {
                        System.out.println(Duration.between(now, Instant.now()).toMillis());
                        now = Instant.now();
                    }
                });

        retry.executeCheckedSupplier(ResilientClient::callServer);
    }

    private static HttpResponse<String> callServer() throws IOException, InterruptedException {
        System.out.println("Calling");
        var request = HttpRequest.newBuilder(URI.create("http://localhost:8080"))
                .POST(HttpRequest.BodyPublishers.ofString("hello"))
                .timeout(Duration.ofSeconds(5L))
                .build();

        var response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.printf("Status code: %d%n", response.statusCode());
        System.out.printf("Response body: %s%n", response.body());
        System.out.printf("Response: %s%n", response);
        return response;
    }
}
```

You should get something like this:
```
Calling
Event
Calling
Event
1018
Calling
Event
1515
Calling
Event
2256
Calling
Event
3383
```

Because backoff policy is exponential first retry is 1 sec, next one is 1 * 1.5 = 1.5 sec, next one is 1.5 * 1.5 = 2.25 sec etc.

#### failAfterMaxAttempts

```
    public static void main(String[] args) throws Throwable {
        var retryConfig = RetryConfig.<String>custom()
                .maxAttempts(3)
                .intervalFunction(IntervalFunction.ofExponentialBackoff(Duration.ofSeconds(1L)))
                .failAfterMaxAttempts(true)
                .retryExceptions(RetryableException.class)
                .retryOnResult(result -> result.equals("Hello"))
                .build();

        var retryRegistry = RetryRegistry.of(retryConfig);
        var retry = retryRegistry.retry("test");

        retry.getEventPublisher().onEvent(System.out::println);

        retry.executeSupplier(TestResilience4J::print);
    }

    private static String print() {
        return "Hello";
    }
```

If ``failAfterMaxAttempts`` is true, it will throw ``MaxRetriesExceededException: Retry 'test' has exhausted all attempts (3)``. However, if ``failAfterMaxAttempts`` is false, then it will just silently end retrying ant continue with the remaining instructions.

#### ``waitDuration`` and ExponentialBackoff

Initially I wanted to configure ``Retry`` to have ``initialDuration`` of 1 second and have exponential backoff policy. Naturally, RetryConfig look like so:
```
    var retryConfig = RetryConfig.custom()
            .waitDuration(Duration.ofSeconds(1))
            .maxAttempts(3)
            .intervalFunction(IntervalFunction.ofExponentialBackoff())
            .build();
```

However, this threw exception:
```
java.lang.IllegalStateException: The intervalFunction was configured twice which could result in an undesired state. Please use either intervalFunction or intervalBiFunction.
```

The exception didn't help to identify what was the problem. As it turns out, ``waitDuration`` registers ``intervalBiFunction``:

```
    public RetryConfig.Builder<T> waitDuration(Duration waitDuration) {
        if (waitDuration.toMillis() >= 0) {
            this.intervalBiFunction = (attempt, either) -> waitDuration.toMillis();
        } else {
            throw new IllegalArgumentException(
                    "waitDuration must be a positive value");
        }
        return this;
    }
```

Hence, the proper way to register waitDuration with exponentialBackoff is:
```
    var retryConfig = RetryConfig.custom()
            .maxAttempts(3)
            .intervalFunction(IntervalFunction.ofExponentialBackoff(Duration.ofSeconds(1)))
            .build();

```

#### ``intervalFunction``

> An IntervalFunction which can be used to calculate the wait interval. The input parameter of the function is the number of attempts (attempt), the output parameter the wait interval in milliseconds. The attempt parameter starts at 1 and increases with every further attempt.
 
#### ``intervalBiFunction``


> An ``IntervalBiFunction`` which can be used to calculate the wait interval. The input parameters of the bifunction is the number of attempts (attempt) and either result or exception, the output parameter is the wait interval in milliseconds. The attempt parameter starts at 1 and increases with every further attempt.


```
    public static void main(String[] args) throws Throwable {
        var retryConfig = RetryConfig.custom()
                .maxAttempts(3)
                .intervalBiFunction((retryCount, error) -> {
                            System.out.println(retryCount.longValue());
                            System.out.println(error);
                            return 1L;
                        }
                )
                .build();

        var retryRegistry = RetryRegistry.of(retryConfig);
        var retry = retryRegistry.retry("test");

        retry.getEventPublisher().onEvent(System.out::println);
        retry.executeSupplier(TestResilience4J::print);
    }

    private static String print() {
        throw new RuntimeException();
    }
```

Result:
```
1
Left(java.lang.RuntimeException)
2021-04-02T15:07:29.689925200+03:00: Retry 'test', waiting PT0.001S until attempt '1'. Last attempt failed with exception 'java.lang.RuntimeException'.
2
Left(java.lang.RuntimeException)
2021-04-02T15:07:29.730105500+03:00: Retry 'test', waiting PT0.001S until attempt '2'. Last attempt failed with exception 'java.lang.RuntimeException'.
2021-04-02T15:07:29.734763400+03:00: Retry 'test' recorded a failed retry attempt. Number of retry attempts: '3'. Giving up. Last exception was: 'java.lang.RuntimeException'.
```

Which leads to very customazable wait times. Maybe for one type of error we should wait longer.

#### ``writableStackTraceEnabled``

> When set to false, {@link Exception#getStackTrace()} returns a zero length array. This may be used to reduce log spam when the Retry has exceeded the maximum nbr of attempts, and flag {@link #failAfterMaxAttempts} has been enabled. The thrown {@link MaxRetriesExceededException} will then have no stacktrace.
 
```
    public static void main(String[] args) throws Throwable {
        var retryConfig = RetryConfig.<String>custom()
                .maxAttempts(3)
                .failAfterMaxAttempts(true)
                .writableStackTraceEnabled(false)
                .retryOnResult(s -> s.equals("Hello"))
                .build();

        var retryRegistry = RetryRegistry.of(retryConfig);
        var retry = retryRegistry.retry("test");

        retry.getEventPublisher().onEvent(System.out::println);
        retry.executeSupplier(TestResilience4J::print);
    }

    private static String print() {
        return "Hello";
    }

```

```
Exception in thread "main" io.github.resilience4j.retry.MaxRetriesExceededException: Retry 'test' has exhausted all attempts (3)
```
vs when ``writableStackTraceEnabled`` is true:
```
Exception in thread "main" io.github.resilience4j.retry.MaxRetriesExceededException: Retry 'test' has exhausted all attempts (3)
	at io.github.resilience4j.retry.MaxRetriesExceededException.createMaxRetriesExceededException(MaxRetriesExceededException.java:27)
	at io.github.resilience4j.retry.internal.RetryImpl$ContextImpl.onComplete(RetryImpl.java:174)
	at io.github.resilience4j.retry.Retry.lambda$decorateSupplier$2(Retry.java:216)
	at io.github.resilience4j.retry.Retry.executeSupplier(Retry.java:430)
	at resilience.TestResilience4J.main(TestResilience4J.java:23)
```

#### ``retryExceptions``

If retryException does not catch exception that the method throws, it will just pass it through to the caller:
```
    public static void main(String[] args) throws Throwable {
        var retryConfig = RetryConfig.<String>custom()
                .retryExceptions(RuntimeException.class)
                .build();

        var retryRegistry = RetryRegistry.of(retryConfig);
        var retry = retryRegistry.retry("test");

        retry.getEventPublisher().onEvent(System.out::println);
        retry.executeCheckedSupplier(TestResilience4J::print);
    }

    private static String print() throws Exception {
        throw new Exception();
    }
```

```
2021-04-02T15:18:26.503276700+03:00: Retry 'test' recorded an error which has been ignored: 'java.lang.Exception'.
Exception in thread "main" java.lang.Exception
	at resilience.TestResilience4J.print(TestResilience4J.java:24)
	at io.github.resilience4j.retry.Retry.lambda$decorateCheckedSupplier$3f69f149$1(Retry.java:137)
	at io.github.resilience4j.retry.Retry.executeCheckedSupplier(Retry.java:419)
	at resilience.TestResilience4J.main(TestResilience4J.java:20)
```

If however, ``retryExceptions(Exception.class)``, then obviously it will retry:
```
2021-04-02T15:23:00.273836400+03:00: Retry 'test', waiting PT0.5S until attempt '1'. Last attempt failed with exception 'java.lang.Exception'.
2021-04-02T15:23:00.823600100+03:00: Retry 'test', waiting PT0.5S until attempt '2'. Last attempt failed with exception 'java.lang.Exception'.
2021-04-02T15:23:01.338639100+03:00: Retry 'test' recorded a failed retry attempt. Number of retry attempts: '3'. Giving up. Last exception was: 'java.lang.Exception'.
Exception in thread "main" java.lang.Exception
	at resilience.TestResilience4J.print(TestResilience4J.java:24)
	at io.github.resilience4j.retry.Retry.lambda$decorateCheckedSupplier$3f69f149$1(Retry.java:137)
	at io.github.resilience4j.retry.Retry.executeCheckedSupplier(Retry.java:419)
	at resilience.TestResilience4J.main(TestResilience4J.java:20)

Process finished with exit code 1
```

### Circuit Breaker

#### Simple setup

#### Failure rate and slow call rate thresholds

The state of the CircuitBreaker changes from CLOSED to OPEN when the failure rate is equal or greater than a configurable threshold. For example when more than 50% of the recorded calls have failed. By default all exceptions count as a failure.
The CircuitBreaker also changes from CLOSED to OPEN when the percentage of slow calls is equal or greater than a configurable threshold. For example when more than 50% of the recorded calls took longer than 5 seconds.

If 20 concurrent threads ask for the permission to execute a function and the state of the CircuitBreaker is closed, all threads are allowed to invoke the function. Even if the sliding window size is 15. The sliding window does not mean that only 15 calls are allowed to run concurrently. If you want to restrict the number of concurrent threads, please use a Bulkhead. You can combine a Bulkhead and a CircuitBreaker.

































### Rate Limiter

### Time Limiter

### Bulkhead

### Cache

### Fallback
