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













### Circuit Breaker

### Rate Limiter

### Time Limiter

### Bulkhead

### Cache

### Fallback















