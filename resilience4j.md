# Guide to resilience4j

[Resilience4j Github](https://github.com/resilience4j/resilience4j)

[Documentation](https://resilience4j.readme.io/docs/getting-started)

## Why resilience4j

Resilience4j is a lightweight, easy-to-use fault tolerance library inspired by Netflix Hystrix, but designed for Java 8 and functional programming. **Lightweight**, because the library only uses Vavr, which does not have any other external library dependencies. Netflix Hystrix, in contrast, has a compile dependency to Archaius which has many more external library dependencies such as Guava and Apache Commons Configuration.

Resilience4j provides higher-order functions (decorators) to enhance any functional interface, lambda expression or method reference with a Circuit Breaker, Rate Limiter, Retry or Bulkhead. You can stack more than one decorator on any functional interface, lambda expression or method reference. The advantage is that you have the choice to select the decorators you need and nothing else.

Netflix Hystrix is currently in maintenance mode and states the following:
> Hystrix is no longer in active development, and is currently in maintenance mode.

> Hystrix (at version 1.5.18) is stable enough to meet the needs of Netflix for our existing applications. <...> For the cases where something like Hystrix makes sense, we intend to continue using Hystrix for existing applications, and to leverage open and active projects like resilience4j for new internal projects. We are beginning to recommend others do the same.


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

[Retry Documentation](https://resilience4j.readme.io/docs/retry)

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
                .retryExceptions(RuntimeException.class)
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

```
2021-04-30T08:33:00.339668+03:00: Retry 'test', waiting PT1S until attempt '1'. Last attempt failed with exception 'null'.
2021-04-30T08:33:01.366464400+03:00: Retry 'test', waiting PT1.5S until attempt '2'. Last attempt failed with exception 'null'.
2021-04-30T08:33:02.877139400+03:00: Retry 'test' recorded a failed retry attempt. Number of retry attempts: '3'. Giving up. Last exception was: 'io.github.resilience4j.retry.MaxRetriesExceeded: max retries is reached out for the result predicate check'.
2021-04-30T08:33:02.877139400+03:00: Retry 'test' recorded a failed retry attempt. Number of retry attempts: '4'. Giving up. Last exception was: 'io.github.resilience4j.retry.MaxRetriesExceededException: Retry 'test' has exhausted all attempts (3)'.
Exception in thread "main" io.github.resilience4j.retry.MaxRetriesExceededException: Retry 'test' has exhausted all attempts (3)
at io.github.resilience4j.retry.MaxRetriesExceededException.createMaxRetriesExceededException(MaxRetriesExceededException.java:27)
at io.github.resilience4j.retry.internal.RetryImpl$ContextImpl.onComplete(RetryImpl.java:174)
at io.github.resilience4j.retry.Retry.lambda$decorateCheckedSupplier$3f69f149$1(Retry.java:140)
at io.github.resilience4j.retry.Retry.executeCheckedSupplier(Retry.java:419)
at resilience.Retry.main(Retry.java:25)
```

If ``failAfterMaxAttempts`` is true, it will throw ``MaxRetriesExceededException: Retry 'test' has exhausted all attempts (3)``. However, if ``failAfterMaxAttempts`` is false, then it will just silently end retrying ant continue with the remaining instructions. If however, supplier function itself returns an exception, it will be propogated instead of throwing ``MaxRetriesExceededException``:
```
    public static void main(String[] args) throws Throwable {
        var retryConfig = RetryConfig.<String>custom()
                                     .maxAttempts(3)
                                     .intervalFunction(IntervalFunction.ofExponentialBackoff(Duration.ofSeconds(1L)))
                                     .failAfterMaxAttempts(true)
                                     .retryExceptions(RuntimeException.class)
                                     .retryOnResult(result -> result.equals("Hello"))
                                     .build();

        var retryRegistry = RetryRegistry.of(retryConfig);
        var retry = retryRegistry.retry("test");

        retry.getEventPublisher().onEvent(System.out::println);

        retry.executeCheckedSupplier(Retry::exception);
    }
    
    private static String exception() {
        throw new RuntimeException("");
    }
```

```
2021-04-30T07:52:53.520931300+03:00: Retry 'test', waiting PT1S until attempt '1'. Last attempt failed with exception 'java.lang.RuntimeException: '.
2021-04-30T07:52:54.583049800+03:00: Retry 'test', waiting PT1.5S until attempt '2'. Last attempt failed with exception 'java.lang.RuntimeException: '.
2021-04-30T07:52:56.089247200+03:00: Retry 'test' recorded a failed retry attempt. Number of retry attempts: '3'. Giving up. Last exception was: 'java.lang.RuntimeException:'.
Exception in thread "main" java.lang.RuntimeException:
at resilience.Retry.exception(Retry.java:33)
at io.github.resilience4j.retry.Retry.lambda$decorateCheckedSupplier$3f69f149$1(Retry.java:137)
at io.github.resilience4j.retry.Retry.executeCheckedSupplier(Retry.java:419)
at resilience.Retry.main(Retry.java:25)
```

#### ``waitDuration`` and ExponentialBackoff

Initially I wanted to configure ``Retry`` to have ``initialDuration`` of 1 second and have exponential backoff policy. Naturally, RetryConfig looked like so:
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

[Circuit Breaker Documentation](https://resilience4j.readme.io/docs/circuitbreaker)

#### Failure rate and slow call rate thresholds

The state of the CircuitBreaker changes from CLOSED to OPEN when the failure rate is equal or greater than a configurable threshold. For example when more than 50% of the recorded calls have failed. By default all exceptions count as a failure.
The CircuitBreaker also changes from CLOSED to OPEN when the percentage of slow calls is equal or greater than a configurable threshold. For example when more than 50% of the recorded calls took longer than 5 seconds.

If 20 concurrent threads ask for the permission to execute a function and the state of the CircuitBreaker is closed, all threads are allowed to invoke the function. Even if the sliding window size is 15. The sliding window does not mean that only 15 calls are allowed to run concurrently. If you want to restrict the number of concurrent threads, please use a Bulkhead. You can combine a Bulkhead and a CircuitBreaker.

#### Simple demo

```
public class TestResilience4J {

    public static void main(String[] args) throws Throwable {
        var circuitBreaker = buildCircuitBreaker();

        circuitBreaker.executeSupplier(TestResilience4J::throwException);
        circuitBreaker.executeSupplier(TestResilience4J::throwException);
    }

    private static CircuitBreaker buildCircuitBreaker() {
        var configs = CircuitBreakerConfig.custom()
                .minimumNumberOfCalls(2)
                .waitDurationInOpenState(Duration.ofSeconds(1L))
                .build();

        var registry = CircuitBreakerRegistry.of(configs);
        var circuitBreaker = registry.circuitBreaker("test");
        circuitBreaker.getEventPublisher().onEvent(System.out::println);

        return circuitBreaker;
    }

    private static int throwException() {
        throw new RuntimeException("Hello");
    }
}
```

Explanation of ``CircuitBreakerConfig`` configuration: 
* ``minimumNumberOfCalls`` - Configures the minimum number of calls which are required (per sliding window period) before the CircuitBreaker can calculate the error rate or slow call rate. For example, if minimumNumberOfCalls is 10, then at least 10 calls must be recorded, before the failure rate can be calculated. If only 9 calls have been recorded the CircuitBreaker will not transition to open even if all 9 calls have failed. Default value is 100, hence it has to be lowered for demo.
* ``waitDurationInOpenState`` - The time that the CircuitBreaker should wait before transitioning from open to half-open. Default value is 60 seconds, hence it has to be  lowered for demo.

Starting application results in:
```
18:42:19.069 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - CircuitBreaker 'test' recorded an exception as failure:
java.lang.RuntimeException: Hello
at resilience.TestResilience4J.throwException(TestResilience4J.java:33)
at io.github.resilience4j.circuitbreaker.CircuitBreaker.lambda$decorateSupplier$4(CircuitBreaker.java:197)
at io.github.resilience4j.circuitbreaker.CircuitBreaker.executeSupplier(CircuitBreaker.java:700)
at resilience.TestResilience4J.main(TestResilience4J.java:15)
2021-04-06T18:42:19.084745900+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 0 ms
18:42:19.100 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event ERROR published: 2021-04-06T18:42:19.084745900+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 0 ms
Exception in thread "main" java.lang.RuntimeException: Hello
at resilience.TestResilience4J.throwException(TestResilience4J.java:33)
at io.github.resilience4j.circuitbreaker.CircuitBreaker.lambda$decorateSupplier$4(CircuitBreaker.java:197)
at io.github.resilience4j.circuitbreaker.CircuitBreaker.executeSupplier(CircuitBreaker.java:700)
at resilience.TestResilience4J.main(TestResilience4J.java:15)
```

In other words, it just throws up on first ``executeSupplier`` and does not execute the second one. Hence, I need to engulf into ``try/catch``:
```
public class TestResilience4J {

    public static void main(String[] args) throws Throwable {
        var circuitBreaker = buildCircuitBreaker();

        circuitBreakerThrowsException(circuitBreaker);
        circuitBreakerThrowsException(circuitBreaker);
    }

    private static CircuitBreaker buildCircuitBreaker() {
        var configs = CircuitBreakerConfig.custom()
                .minimumNumberOfCalls(2)
                .waitDurationInOpenState(Duration.ofSeconds(1L))
                .build();

        var registry = CircuitBreakerRegistry.of(configs);
        var circuitBreaker = registry.circuitBreaker("test");
        circuitBreaker.getEventPublisher().onEvent(System.out::println);

        return circuitBreaker;
    }

    private static void circuitBreakerThrowsException(CircuitBreaker circuitBreaker) {
        try {
            circuitBreaker.executeSupplier(() -> {
                throw new RuntimeException("Hello");
            });
        } catch (Exception e) {
            //Do something
        }
    }
}
```

Now this, generates the following:
```
18:48:29.962 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - CircuitBreaker 'test' recorded an exception as failure:
java.lang.RuntimeException: Hello
at resilience.TestResilience4J.lambda$circuitBreakerThrowsException$0(TestResilience4J.java:34)
at io.github.resilience4j.circuitbreaker.CircuitBreaker.lambda$decorateSupplier$4(CircuitBreaker.java:197)
at io.github.resilience4j.circuitbreaker.CircuitBreaker.executeSupplier(CircuitBreaker.java:700)
at resilience.TestResilience4J.circuitBreakerThrowsException(TestResilience4J.java:33)
at resilience.TestResilience4J.main(TestResilience4J.java:14)
2021-04-06T18:48:29.978172400+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 0 ms
18:48:29.984 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event ERROR published: 2021-04-06T18:48:29.978172400+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 0 ms
18:48:29.984 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - CircuitBreaker 'test' recorded an exception as failure:
java.lang.RuntimeException: Hello
at resilience.TestResilience4J.lambda$circuitBreakerThrowsException$0(TestResilience4J.java:34)
at io.github.resilience4j.circuitbreaker.CircuitBreaker.lambda$decorateSupplier$4(CircuitBreaker.java:197)
at io.github.resilience4j.circuitbreaker.CircuitBreaker.executeSupplier(CircuitBreaker.java:700)
at resilience.TestResilience4J.circuitBreakerThrowsException(TestResilience4J.java:33)
at resilience.TestResilience4J.main(TestResilience4J.java:15)
2021-04-06T18:48:29.984689200+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 0 ms
18:48:29.984 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event ERROR published: 2021-04-06T18:48:29.984689200+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 0 ms
2021-04-06T18:48:29.984689200+03:00: CircuitBreaker 'test' exceeded failure rate threshold. Current failure rate: 100.0
18:48:29.984 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event FAILURE_RATE_EXCEEDED published: 2021-04-06T18:48:29.984689200+03:00: CircuitBreaker 'test' exceeded failure rate threshold. Current failure rate: 100.0
2021-04-06T18:48:30.000331700+03:00: CircuitBreaker 'test' changed state from CLOSED to OPEN
18:48:30.000 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event STATE_TRANSITION published: 2021-04-06T18:48:30.000331700+03:00: CircuitBreaker 'test' changed state from CLOSED to OPEN
```

I'll remove the exception details and just leave the interesting part:
```
18:48:29.962 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - CircuitBreaker 'test' recorded an exception as failure:
18:48:29.984 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event ERROR published: 2021-04-06T18:48:29.978172400+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 0 ms
18:48:29.984 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - CircuitBreaker 'test' recorded an exception as failure:
18:48:29.984 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event ERROR published: 2021-04-06T18:48:29.984689200+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 0 ms
2021-04-06T18:48:29.984689200+03:00: CircuitBreaker 'test' exceeded failure rate threshold. Current failure rate: 100.0
18:48:29.984 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event FAILURE_RATE_EXCEEDED published: 2021-04-06T18:48:29.984689200+03:00: CircuitBreaker 'test' exceeded failure rate threshold. Current failure rate: 100.0
2021-04-06T18:48:30.000331700+03:00: CircuitBreaker 'test' changed state from CLOSED to OPEN
18:48:30.000 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event STATE_TRANSITION published: 2021-04-06T18:48:30.000331700+03:00: CircuitBreaker 'test' changed state from CLOSED to OPEN
```

Because it's configured ``minimumNumberOfCalls(2)``, ``CircuitBraker`` only needs two calls to evaluate whether CircuitBraker is open or closed. In this case, 2 calls were made, 2 calls resulted in error, hence the state transitions to OPEN.

After waiting configured amount of time in ``waitDurationInOpenState(Duration.ofSeconds(1L))`` configuration, it will try to move from open to half-open, which means it will allow some calls to pass through to check, whether the downstream calls are responding. Firstly, let's not wait and try to do additional calls after state is open:
```
...
    public static void main(String[] args) throws Throwable {
        var circuitBreaker = buildCircuitBreaker();

        circuitBreakerThrowsException(circuitBreaker);
        circuitBreakerThrowsException(circuitBreaker);
        circuitBreakerThrowsException(circuitBreaker);
        circuitBreakerThrowsException(circuitBreaker);
    }
...
```

The result is:
```
18:57:40.859 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - CircuitBreaker 'test' recorded an exception as failure:
2021-04-06T18:57:40.859564400+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 0 ms
18:57:40.875 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event ERROR published: 2021-04-06T18:57:40.859564400+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 0 ms
18:57:40.875 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - CircuitBreaker 'test' recorded an exception as failure:
2021-04-06T18:57:40.875190900+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 0 ms
18:57:40.875 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event ERROR published: 2021-04-06T18:57:40.875190900+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 0 ms
2021-04-06T18:57:40.875190900+03:00: CircuitBreaker 'test' exceeded failure rate threshold. Current failure rate: 100.0
18:57:40.875 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event FAILURE_RATE_EXCEEDED published: 2021-04-06T18:57:40.875190900+03:00: CircuitBreaker 'test' exceeded failure rate threshold. Current failure rate: 100.0
2021-04-06T18:57:40.903214100+03:00: CircuitBreaker 'test' changed state from CLOSED to OPEN
18:57:40.903 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event STATE_TRANSITION published: 2021-04-06T18:57:40.903214100+03:00: CircuitBreaker 'test' changed state from CLOSED to OPEN
2021-04-06T18:57:40.903214100+03:00: CircuitBreaker 'test' recorded a call which was not permitted.
18:57:40.903 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event NOT_PERMITTED published: 2021-04-06T18:57:40.903214100+03:00: CircuitBreaker 'test' recorded a call which was not permitted.
2021-04-06T18:57:40.903214100+03:00: CircuitBreaker 'test' recorded a call which was not permitted.
18:57:40.903 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event NOT_PERMITTED published: 2021-04-06T18:57:40.903214100+03:00: CircuitBreaker 'test' recorded a call which was not permitted.
```

Once the ``CircuitBraker`` moved to open state, it does not permit additional calls and throws ``CallNotPermittedException`` which can be caught and handled accordingly (most likely, switching to default method):
```
...
    private static void circuitBreakerThrowsException(CircuitBreaker circuitBreaker) {
        try {
            circuitBreaker.executeSupplier(() -> {
                throw new RuntimeException("Hello");
            });
        } catch (CallNotPermittedException e) {
            System.out.println("Call is not permited do something else");
        } catch (Exception e) {
            //Do something
        }
    }
...
```

Now if I sleep for at least a second before doing additional two calls, those calls will be permited to execute and we'll be in a half-open state which will end up in open, because subsequential calls still produce errors:

```
    public static void main(String[] args) throws Throwable {
        var circuitBreaker = buildCircuitBreaker();

        circuitBreakerThrowsException(circuitBreaker);
        circuitBreakerThrowsException(circuitBreaker);
        System.out.println("-".repeat(10) + "Sleep" + "-".repeat(10));
        sleepSeconds(2);
        circuitBreakerThrowsException(circuitBreaker);
        circuitBreakerThrowsException(circuitBreaker);
    }
    
    private static void sleepSeconds(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
```

```
19:09:32.058 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - CircuitBreaker 'test' recorded an exception as failure:
2021-04-06T19:09:32.068796400+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 0 ms
19:09:32.079 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event ERROR published: 2021-04-06T19:09:32.068796400+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 0 ms
19:09:32.089 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - CircuitBreaker 'test' recorded an exception as failure:
2021-04-06T19:09:32.089135+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 0 ms
19:09:32.089 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event ERROR published: 2021-04-06T19:09:32.089135+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 0 ms
2021-04-06T19:09:32.089135+03:00: CircuitBreaker 'test' exceeded failure rate threshold. Current failure rate: 100.0
19:09:32.109 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event FAILURE_RATE_EXCEEDED published: 2021-04-06T19:09:32.089135+03:00: CircuitBreaker 'test' exceeded failure rate threshold. Current failure rate: 100.0
2021-04-06T19:09:32.129618800+03:00: CircuitBreaker 'test' changed state from CLOSED to OPEN
19:09:32.129 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event STATE_TRANSITION published: 2021-04-06T19:09:32.129618800+03:00: CircuitBreaker 'test' changed state from CLOSED to OPEN
----------Sleep----------
2021-04-06T19:09:34.165330500+03:00: CircuitBreaker 'test' changed state from OPEN to HALF_OPEN
19:09:34.165 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event STATE_TRANSITION published: 2021-04-06T19:09:34.165330500+03:00: CircuitBreaker 'test' changed state from OPEN to HALF_OPEN
19:09:34.165 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - CircuitBreaker 'test' recorded an exception as failure:
2021-04-06T19:09:34.165330500+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 0 ms
19:09:34.165 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event ERROR published: 2021-04-06T19:09:34.165330500+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 0 ms
19:09:34.165 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - CircuitBreaker 'test' recorded an exception as failure:
2021-04-06T19:09:34.165330500+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 0 ms
19:09:34.165 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event ERROR published: 2021-04-06T19:09:34.165330500+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 0 ms
2021-04-06T19:09:34.165330500+03:00: CircuitBreaker 'test' changed state from HALF_OPEN to OPEN
19:09:34.165 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event STATE_TRANSITION published: 2021-04-06T19:09:34.165330500+03:00: CircuitBreaker 'test' changed state from HALF_OPEN to OPEN
```

During third call, the state moves from open to half_open, because application gave time for transition to happen. Of course, third and fourth results in exception, hence the state moves back from half_open to open. Do have in mind that if I were to increase ``.minimumNumberOfCalls(2)`` to 3, then it would have a completely different outcome. The third call would produce the state transition and last call would not be permited, because the sleeper is in the wrong step.
```
19:33:18.478 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - CircuitBreaker 'test' recorded an exception as failure:
2021-04-06T19:33:18.478999900+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 0 ms
19:33:18.501 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event ERROR published: 2021-04-06T19:33:18.478999900+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 0 ms
19:33:18.501 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - CircuitBreaker 'test' recorded an exception as failure:
2021-04-06T19:33:18.501145100+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 0 ms
19:33:18.501 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event ERROR published: 2021-04-06T19:33:18.501145100+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 0 ms
----------Sleep----------
19:33:20.530 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - CircuitBreaker 'test' recorded an exception as failure:
2021-04-06T19:33:20.530941600+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 0 ms
19:33:20.530 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event ERROR published: 2021-04-06T19:33:20.530941600+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 0 ms
2021-04-06T19:33:20.530941600+03:00: CircuitBreaker 'test' exceeded failure rate threshold. Current failure rate: 100.0
19:33:20.530 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event FAILURE_RATE_EXCEEDED published: 2021-04-06T19:33:20.530941600+03:00: CircuitBreaker 'test' exceeded failure rate threshold. Current failure rate: 100.0
2021-04-06T19:33:20.546574200+03:00: CircuitBreaker 'test' changed state from CLOSED to OPEN
19:33:20.546 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event STATE_TRANSITION published: 2021-04-06T19:33:20.546574200+03:00: CircuitBreaker 'test' changed state from CLOSED to OPEN
2021-04-06T19:33:20.546574200+03:00: CircuitBreaker 'test' recorded a call which was not permitted.
19:33:20.546 [main] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event NOT_PERMITTED published: 2021-04-06T19:33:20.546574200+03:00: CircuitBreaker 'test' recorded a call which was not permitted.
```

#### ``permittedNumberOfCallsInHalfOpenState``

Configures the number of permitted calls when the CircuitBreaker is half open. Default is 10.

Looking at source code:
```
    @Override
    public boolean tryAcquirePermission() {
        if (permittedNumberOfCalls.getAndUpdate(current -> current == 0 ? current : --current)
                > 0) {
            return true;
        }
        circuitBreakerMetrics.onCallNotPermitted();
        return false;
    }
    ...
    @Override
    public void acquirePermission() {
        if (!tryAcquirePermission()) {
            throw CallNotPermittedException
                    .createCallNotPermittedException(CircuitBreakerStateMachine.this);
        }
    }
    ...
    @Override
    public void acquirePermission() {
        try {
            stateReference.get().acquirePermission();
        } catch (Exception e) {
            publishCallNotPermittedEvent();
            throw e;
        }
    }
    ...
    static <T> Supplier<T> decorateSupplier(CircuitBreaker circuitBreaker, Supplier<T> supplier) {
        return () -> {
            circuitBreaker.acquirePermission();
            final long start = circuitBreaker.getCurrentTimestamp();
            try {
                T result = supplier.get();
                long duration = circuitBreaker.getCurrentTimestamp() - start;
                circuitBreaker.onResult(duration, circuitBreaker.getTimestampUnit(), result);
                return result;
            } catch (Exception exception) {
                // Do not handle java.lang.Error
                long duration = circuitBreaker.getCurrentTimestamp() - start;
                circuitBreaker.onError(duration, circuitBreaker.getTimestampUnit(), exception);
                throw exception;
            }
        };
    }
```

In other words, before doing anything, circuitBreaker tries to acquire permission, which is directly tied to how many permitted number of calls can be made.

To trigger this property, one has to do a little more setup.

```
public class TestResilience4J {

    public static void main(String[] args) throws Throwable {
        var circuitBreaker = buildCircuitBreaker();
        var executorService = Executors.newFixedThreadPool(4);

        circuitBreakerThrowsException(circuitBreaker);
        circuitBreakerThrowsException(circuitBreaker);
        System.out.println("-".repeat(10) + "Sleep" + "-".repeat(10));
        sleepSeconds(2);

        executorService.submit(() -> circuitBreakerThrowsException(circuitBreaker));
        sleepMilliseconds(100);
        executorService.submit(() -> circuitBreakerThrowsException(circuitBreaker));
        sleepMilliseconds(100);
        executorService.submit(() -> circuitBreakerThrowsException(circuitBreaker));
        sleepMilliseconds(100);
        executorService.submit(() -> circuitBreakerThrowsException(circuitBreaker));

        executorService.shutdown();
    }

    private static CircuitBreaker buildCircuitBreaker() {
        var configs = CircuitBreakerConfig.custom()
                .minimumNumberOfCalls(2)
                .waitDurationInOpenState(Duration.ofSeconds(1L))
                .permittedNumberOfCallsInHalfOpenState(1)
                .build();

        var registry = CircuitBreakerRegistry.of(configs);
        var circuitBreaker = registry.circuitBreaker("test");
        circuitBreaker.getEventPublisher().onEvent(System.out::println);

        return circuitBreaker;
    }

    private static void circuitBreakerThrowsException(CircuitBreaker circuitBreaker) {
        try {
            circuitBreaker.executeSupplier(() -> { sleepSeconds(4); throw new RuntimeException("Hello"); });
        } catch (CallNotPermittedException e) {
            System.out.println("Call is not permited do something else");
        } catch (Exception e) {
            //Do something
        }
    }

    private static void sleepSeconds(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void sleepMilliseconds(long milliseconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

Several points:
* Added ``sleepSeconds(4)`` to ``circuitBreakerThrowsException`` method, otherwise the time executing a function would be too short to capture that several calls are in active state and more shouldn't be allowed;
* ``sleepMilliseconds(100);`` between calls, otherwise, it seems all threads are allowed to execute;
* ``permittedNumberOfCallsInHalfOpenState(1)`` to ``buildCircuitBreaker``;
* Add ``Executors.newFixedThreadPool(4);`` to simulate several concurrent actions.

If I don't add ``sleepMilliseconds(100);``:
```
2021-04-30T10:34:22.863785400+03:00: CircuitBreaker 'test' changed state from OPEN to HALF_OPEN
10:34:22.863 [pool-1-thread-1] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event STATE_TRANSITION published: 2021-04-30T10:34:22.863785400+03:00: CircuitBreaker 'test' changed state from OPEN to HALF_OPEN
10:34:26.869 [pool-1-thread-1] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - CircuitBreaker 'test' recorded an exception as failure:
java.lang.RuntimeException: Hello
10:34:26.869 [pool-1-thread-2] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - CircuitBreaker 'test' recorded an exception as failure:
java.lang.RuntimeException: Hello
10:34:26.869 [pool-1-thread-4] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - CircuitBreaker 'test' recorded an exception as failure:
java.lang.RuntimeException: Hello
2021-04-30T10:34:26.871812400+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 4000 ms
10:34:26.869 [pool-1-thread-3] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - CircuitBreaker 'test' recorded an exception as failure:
java.lang.RuntimeException: Hello
2021-04-30T10:34:26.871812400+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 4002 ms
2021-04-30T10:34:26.871812400+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 4001 ms
10:34:26.871 [pool-1-thread-1] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event ERROR published: 2021-04-30T10:34:26.871812400+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 4000 ms
2021-04-30T10:34:26.871812400+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 4001 ms
10:34:26.872 [pool-1-thread-2] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event ERROR published: 2021-04-30T10:34:26.871812400+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 4002 ms
10:34:26.872 [pool-1-thread-4] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event ERROR published: 2021-04-30T10:34:26.871812400+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 4001 ms
2021-04-30T10:34:26.872480100+03:00: CircuitBreaker 'test' changed state from HALF_OPEN to OPEN
10:34:26.872 [pool-1-thread-3] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event ERROR published: 2021-04-30T10:34:26.871812400+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 4001 ms
10:34:26.872 [pool-1-thread-1] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event STATE_TRANSITION published: 2021-04-30T10:34:26.872480100+03:00: CircuitBreaker 'test' changed state from HALF_OPEN to OPEN
```

Notice that all threads ``[pool-1-thread-1]`` - ``[pool-1-thread-4]`` are allowed to be executed, even though, it should be gatekept. With added delay:
```
2021-04-30T10:22:30.216441900+03:00: CircuitBreaker 'test' changed state from OPEN to HALF_OPEN
10:22:30.216 [pool-1-thread-1] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event STATE_TRANSITION published: 2021-04-30T10:22:30.216441900+03:00: CircuitBreaker 'test' changed state from OPEN to HALF_OPEN
2021-04-30T10:22:30.433458100+03:00: CircuitBreaker 'test' recorded a call which was not permitted.
10:22:30.433 [pool-1-thread-3] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event NOT_PERMITTED published: 2021-04-30T10:22:30.433458100+03:00: CircuitBreaker 'test' recorded a call which was not permitted.
Call is not permited do something else
2021-04-30T10:22:30.548722200+03:00: CircuitBreaker 'test' recorded a call which was not permitted.
10:22:30.548 [pool-1-thread-4] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event NOT_PERMITTED published: 2021-04-30T10:22:30.548722200+03:00: CircuitBreaker 'test' recorded a call which was not permitted.
Call is not permited do something else
10:22:34.231 [pool-1-thread-1] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - CircuitBreaker 'test' recorded an exception as failure:
java.lang.RuntimeException: Hello
2021-04-30T10:22:34.231172800+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 4002 ms
10:22:34.231 [pool-1-thread-1] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event ERROR published: 2021-04-30T10:22:34.231172800+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 4002 ms
2021-04-30T10:22:34.231172800+03:00: CircuitBreaker 'test' changed state from HALF_OPEN to OPEN
10:22:34.231 [pool-1-thread-1] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event STATE_TRANSITION published: 2021-04-30T10:22:34.231172800+03:00: CircuitBreaker 'test' changed state from HALF_OPEN to OPEN
10:22:34.327 [pool-1-thread-2] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - CircuitBreaker 'test' recorded an exception as failure:
java.lang.RuntimeException: Hello
2021-04-30T10:22:34.327201600+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 4003 ms
10:22:34.327 [pool-1-thread-2] DEBUG io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine - Event ERROR published: 2021-04-30T10:22:34.327201600+03:00: CircuitBreaker 'test' recorded an error: 'java.lang.RuntimeException: Hello'. Elapsed time: 4003 ms
```

Now thread-1 is permitted, while thread-3 and thread-4 are gatekept and CircuitBrake throws ``NOT_PERMITTED``. What's happening with thread-2 - I have no idea.





























### Rate Limiter

### Time Limiter

### Bulkhead

### Cache

### Fallback
