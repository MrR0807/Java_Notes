###Logging

You can log request and responses by specifying -Djdk.httpclient.HttpClient.log=requests on the Java command line.

Additional note:

The jdk.httpclient.HttpClient.log property is an implementation specific property whose value is a comma separated list which can be configured on the Java command line for diagnosis/debugging purposes with the following values:

-Djava.net.HttpClient.log=
       errors,requests,headers,
       frames[:control:data:window:all],content,ssl,trace,channel


https://stackoverflow.com/questions/53215038/how-to-log-request-response-using-java-net-http-httpclient




















http://hg.openjdk.java.net/jdk/jdk/file/2cac7d48db4c/test/jdk/java/net/httpclient/offline/OfflineTesting.java




public class OfflineTesting {


    private static HttpClient getClient() {

        // be sure to return the appropriate client when testing

        //return HttpClient.newHttpClient();

        return FixedResponseHttpClient.createClientFrom(

                HttpClient.newBuilder(),

                200,

                headersOf("Server", "nginx",

                          "Content-Type", "text/html"),

                "A response message");

    }


    @Test

    public void testResponseAsString() {

        HttpClient client = getClient();


        HttpRequest request = HttpRequest.newBuilder()

                .uri(URI.create("http://openjdk.java.net/"))

                .build();


        client.sendAsync(request, BodyHandlers.ofString())

                .thenAccept(response -> {

                    System.out.println("response: " + response);

                    assertEquals(response.statusCode(), 200);

                    assertTrue(response.headers().firstValue("Server").isPresent());

                    assertEquals(response.body(), "A response message"); } )

                .join();

    }


    @Test

    public void testResponseAsByteArray() {

        HttpClient client = getClient();


        HttpRequest request = HttpRequest.newBuilder()

                .uri(URI.create("http://openjdk.java.net/"))

                .build();


        client.sendAsync(request, BodyHandlers.ofByteArray())

                .thenAccept(response -> {

                    System.out.println("response: " + response);

                    assertEquals(response.statusCode(), 200);

                    assertTrue(response.headers().firstValue("Content-Type").isPresent());

                    assertEquals(response.body(), "A response message".getBytes(UTF_8)); } )

                .join();

    }


    @Test

    public void testFileNotFound() {

        //HttpClient client = HttpClient.newHttpClient();

        HttpClient client = FixedResponseHttpClient.createClientFrom(

                HttpClient.newBuilder(),

                404,

                headersOf("Connection",  "keep-alive",

                          "Content-Length", "162",

                          "Content-Type", "text/html",

                          "Date", "Mon, 15 Jan 2018 15:01:16 GMT",

                          "Server", "nginx"),

                "<html>\n" +

                "<head><title>404 Not Found</title></head>\n" +

                "<body bgcolor=\"white\">\n" +

                "<center><h1>404 Not Found</h1></center>\n" +

                "<hr><center>nginx</center>\n" +

                "</body>\n" +

                "</html>");


        HttpRequest request = HttpRequest.newBuilder()

                .uri(URI.create("http://openjdk.java.net/notFound"))

                .build();


        client.sendAsync(request, BodyHandlers.ofString())

                .thenAccept(response -> {

                    assertEquals(response.statusCode(), 404);

                    response.headers().firstValue("Content-Type")

                            .ifPresentOrElse(type -> assertEquals(type, "text/html"),

                                             () -> fail("Content-Type not present"));

                    assertTrue(response.body().contains("404 Not Found")); } )

                .join();

    }


    @Test

    public void testEcho() {

        HttpClient client = FixedResponseHttpClient.createEchoClient(

                HttpClient.newBuilder(),

                200,

                headersOf("Connection",  "keep-alive"));


        HttpRequest request = HttpRequest.newBuilder()

                .uri(URI.create("http://openjdk.java.net/echo"))

                .POST(BodyPublishers.ofString("Hello World"))

                .build();


        client.sendAsync(request, BodyHandlers.ofString())

                .thenAccept(response -> {

                    System.out.println("response: " + response);

                    assertEquals(response.statusCode(), 200);

                    assertEquals(response.body(), "Hello World"); } )

                .join();

    }


    @Test

    public void testEchoBlocking() throws IOException, InterruptedException {

        HttpClient client = FixedResponseHttpClient.createEchoClient(

                HttpClient.newBuilder(),

                200,

                headersOf("Connection",  "keep-alive"));


        HttpRequest request = HttpRequest.newBuilder()

                .uri(URI.create("http://openjdk.java.net/echo"))

                .POST(BodyPublishers.ofString("Hello chegar!!"))

                .build();


        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        System.out.println("response: " + response);

        assertEquals(response.statusCode(), 200);

        assertEquals(response.body(), "Hello chegar!!");

    }


    // ---


    public static IllegalArgumentException newIAE(String message, Object... args) {

        return new IllegalArgumentException(format(message, args));

    }


    static final BiPredicate<String,String> ACCEPT_ALL = (x, y) -> true;


    static HttpHeaders headersOf(String... params) {

        Map<String,List<String>> map = new HashMap<>();

        requireNonNull(params);

        if (params.length == 0 || params.length % 2 != 0) {

            throw newIAE("wrong number, %d, of parameters", params.length);

        }

        for (int i = 0; i < params.length; i += 2) {

            String name  = params[i];

            String value = params[i + 1];

            map.put(name, List.of(value));

        }

        return HttpHeaders.of(map, ACCEPT_ALL);

    }

}



----




By default, basic authentication with the proxy is disabled when tunneling through an authenticating proxy since java 8u111.

You can re-enable it by specifying -Djdk.http.auth.tunneling.disabledSchemes="" on the java command line.

See the jdk 8u111 release notes

--------------------


Author: https://golb.hplar.ch/2019/01/java-11-http-client.html


A closer look at the Java 11 HTTP Client

Published: January 23, 2019  •  java

In this blog post we are going to take a look at the HTTP client library that has been introduced in Java 11 (September 2018). It's one of the larger new feature we've got with Java 11. To be exact, the library was already part of Java 9 but only as an incubation module. This is a pattern we maybe see more of in the future because of the shorter 6 months release cycles of Java. It's a way for the Java platform developers to release new features early and collect feedback from the developer community and when ready, release it officially, as they did with the HTTP client library.

Up to Java version 11, the only built-in way to work with HTTP is URLConnection (available since Java 1.0). But it's not the easiest API to work with and does not support the newer HTTP/2 protocol. Because of that most projects added an external HTTP client library, like Apache HTTP Client and OkHttp to their projects. I think there is still room for these libraries on Java 11, because as you will see later, the new Java 11 HTTP client misses a few convenient functions like: URI builder, multipart form data, form data and compression support. Especially the missing compression support surprised me a little bit coming from OkHttp where this functionality is transparently handled by the library. But these missing features are no deal breakers and can be implemented on top of the new HTTP client library which speaks for the flexibility and configurability of the library. You find examples of these features in the demo applications below.

In the following article I show you multiple examples with the library. Most example speak with a local Spring Boot server. You find the code for this application on GitHub:
https://github.com/ralscha/blog2019/tree/master/java11httpclient/server

All examples use HTTP/2 and TLS, but you can disable it in application.properties. Only the HTTP/2 Server Push demo requires HTTP/2 and TLS, all the other examples also work with HTTP/1.1 and cleartext HTTP.

I utilized mkcert to install a private CA and to create the TLS certificate (see my previous blog post). Make sure that you set the environment variable JAVA_HOME correctly before running mkcert --install, so mkcert is able to add the root CA to the Java installation. This is important for the client part of the demos because they have to trust this private root CA.
Overview

The Java 11 HTTP client is part of the Java SE platform and comprises the following classes and interfaces that all reside in the java.net.http package (module: java.net.http).

This is not a complete list of all available classes and interfaces. Visit the JavaDoc to see a complete overview: https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/package-summary.html
java.net.HttpClient

To send requests, you first have to create an HttpClient with a builder style API. You can configure per-client settings when building the client.

Here an example that shows all available settings.

    var client = HttpClient.newBuilder()
            .authenticator(Authenticator.getDefault())
            .connectTimeout(Duration.ofSeconds(30))
            .cookieHandler(CookieHandler.getDefault())
            .executor(Executors.newFixedThreadPool(2))
            .followRedirects(Redirect.NEVER)
            .priority(1) //HTTP/2 priority
            .proxy(ProxySelector.getDefault())
            .sslContext(SSLContext.getDefault())
            .version(Version.HTTP_2)
            .sslParameters(new SSLParameters())
            .build();

Client.java

Once created, an HttpClient instance is immutable, thus automatically thread-safe, and you can send multiple requests with it.

By default, the client tries to open a HTTP/2 connection, if the server answers with HTTP/1.1 the client automatically falls back to this version. If you know in advance that the server only speaks HTTP/1.1, you may create the client with version(Version.HTTP_1_1).

connectTimeout() determines how long the client waits until a connection can be established. If the connection can't be established, the client throws a HttpConnectTimeoutException exception.

executor() sets the executor to be used for asynchronous and dependent tasks. If you don't specify an executor, a default executor is created for each newly built HttpClient. The default executor uses a thread pool.

See the examples below for more information about followRedirects(), authenticator() and cookieHandler().

If you are okay with the default settings, you can build the client with newHttpClient().

    client = HttpClient.newHttpClient();
    // equivalent
    client = HttpClient.newBuilder().build();

Client.java

The default settings include:

    prefer HTTP/2
    no connection timeout
    redirection policy of NEVER
    no cookie handler
    no authenticator
    default thread pool executor
    default proxy selector
    default SSL context


java.net.HttpRequest

Similar to clients, you build HttpRequest instances with a builder. You have to set the URI, request method and optionally specify the body, timeout and headers.

var request = HttpRequest.newBuilder()
        .uri(URI.create("https://localhost:8443/headers"))
        .timeout(Duration.ofMinutes(2))
        .header("Content-Type", "application/json")
        .POST(BodyPublishers.ofString("the body"))
        .build();

HttpRequest instances are immutable and can be sent multiple times.

The request URI can be specified with uri() or as argument of newBuilder(). There is no difference in functionality.

	  var request1 = HttpRequest.newBuilder(URI.create("https://localhost:8443/headers"))
            .build();

	   var request2 = HttpRequest.newBuilder()
	        .uri(URI.create("https://localhost:8443/headers"))
	        .build();

timeout() sets a timeout only for this request. If the client does not receive a response in the specified amount of time, he throws an HttpTimeoutException exception, and sendAsync() completes exceptionally with this exception. If you don't set a timeout, the client waits forever.

The client supports all HTTP methods, but the request builder only contains these predefined methods: GET(), POST(), DELETE() and PUT(). To create a request with a different HTTP method you need to call method().

Example of a HEAD request:

  var request = HttpRequest.newBuilder(URI.create("https://localhost:8443/headers"))
        .method("HEAD", BodyPublishers.noBody())
        .build();

The special BodyPublishers.noBody() can be used where no request body is required.

You may copy the builder if you need to create multiple similar requests

    var client = HttpClient.newHttpClient();

    var builder = HttpRequest.newBuilder()
                  .GET()
                  .uri(URI.create("https://localhost:8443/headers"));

    var request1 = builder.copy().setHeader("X-Counter", "1").build();
    var request2 = builder.copy().setHeader("X-Counter", "2").build();

Get.java

java.net.HttpResponse

HttpResponse is an interface and not created directly. Implementations of this interface are returned by the client when sending a request.

A few of the provided methods in the response interface.
Return type 	Method 	Description
T 	body() 	returns the response body
HttpHeaders 	headers() 	returns the response headers
HttpRequest 	request() 	returns the HttpRequest corresponding to this response
int 	statusCode() 	returns the HTTP status code
URI 	uri() 	returns the URI that the response was received from
HttpClient.Version 	version() 	returns the HTTP protocol

See the JavaDoc for a complete overview: https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpResponse.html

The request and response bodies are exposed as reactive streams (asynchronous streams of data with non-blocking back pressure). They use the new reactive stream support that has been introduced in Java 9 (Flow)

HttpRequest.BodyPublisher

Subtype of Flow.Publisher

A BodyPublisher is used when you send a request with a request body. The BodyPublisher converts objects into a flow of byte buffers suitable for sending as a body.

HttpRequest.BodyPublishers

Implementations of BodyPublisher that implement various useful publishers, such as publishing the request body from a String, or from a file.

Here a few examples

BodyPublishers::ofString
BodyPublishers::ofFile
BodyPublishers::ofByteArray
BodyPublishers::ofInputStream


HttpResponse.BodyHandler

BodyHandlers are responsible for converting the bytes from a response into higher-level Java types, like String or Path. This interface allows inspection of the status code and headers before the actual response body is received

HttpResponse.BodyHandlers

Factory class that provides BodyHandler implementation for handling common response body types such as files, Strings and bytes. These implementations do not examine the status code. They typically return a HttpResponse.BodySubscribers with the same name

A few examples

BodyHandlers::ofByteArray
BodyHandlers::ofFile
BodyHandlers::ofString
BodyHandlers::ofInputStream


WebSocket

The Java 11 HTTP client not only supports HTTP, it also includes a WebSocket client. The last demo application in this blog post shows you an example with WebSocket.
Sending requests

Requests can be sent either synchronously or asynchronously.
Synchronously

send() blocks the calling thread until the response is available.

HttpResponse<String> response = client.send(request, BodyHandlers.ofString());


Asynchronously

sendAsync() immediately returns with a CompletableFuture that completes with a HttpResponse when it becomes available.

CompletableFuture<String> future = client.sendAsync(request, BodyHandlers.ofString())
         .thenApply(response -> {
             System.out.println(response.statusCode());
             return response;
         })
         .thenApply(HttpResponse::body)
         .thenAccept(System.out::println)

Asynchronous requests are handled by an executor service that the HTTP client automatically manages, or use a custom executor when configured with executor()

In the following examples I switch between the two styles. Both styles provide the same functionality, it depends on the overall architecture of your application how you want to send the requests. You may, without any problem, use both styles in one application.
GET

GET is maybe the most widely used HTTP method and this fact is reflected in the HttpClient design. When you build a request, you may omit GET(). The HttpClient assumes it's a GET request by default.

    var client = HttpClient.newHttpClient();
    var request = HttpRequest.newBuilder()
                  .GET()
                  .uri(URI.create("https://localhost:8443/helloworld"))
                  .timeout(Duration.ofSeconds(15))
                  .build();

    try {
      HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
      printResponse(response);
    }
    catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }

Get.java

BodyHandlers.ofString() is a factory creating a body handler that handles the bytes in the response and converts them to a String, thus the generic type of HttpResponse in this example is String.

Same request sent asynchronously

    var client = HttpClient.newHttpClient();
    var request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("https://localhost:8443/helloworld"))
                    .build();

    CompletableFuture<HttpResponse<String>> future = client.sendAsync(request,
        BodyHandlers.ofString());

    return future.thenApply(response -> {
      printResponse(response);
      return response;
    }).thenApply(HttpResponse::body)
      .exceptionally(e -> "Error: " + e.getMessage())
      .thenAccept(System.out::println);
  }

Get.java

With the asynchronous API an application can send multiple requests concurrently.

    var client = HttpClient.newHttpClient();

    List<HttpRequest> requests = paths.stream()
        .map(path -> "https://localhost:8443" + path)
        .map(URI::create)
        .map(HttpRequest::newBuilder)
        .map(requestBuilder -> requestBuilder.build())        
        .collect(Collectors.toList());
        
       
    CompletableFuture<?>[] responses = requests.stream()
        .map(request -> client.sendAsync(request, BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .exceptionally(e -> "Error: " + e.getMessage())
            .thenAccept(System.out::println))
        .toArray(CompletableFuture<?>[]::new);

Get.java

JSON

There is no special support for JSON built-in. It is handled like any other String message.

Because there is no JSON parser built into the Java platform, I added Yasson to this project.

		<dependency>
			<groupId>org.eclipse</groupId>
			<artifactId>yasson</artifactId>
			<version>1.0.3</version>
		</dependency>
		<dependency>
			<groupId>org.glassfish</groupId>
			<artifactId>javax.json</artifactId>
			<version>1.1.4</version>
		</dependency>

Eclipse Yasson is an official reference implementation of JSON Binding (JSR-367).

An application can handle a JSON response in different ways. Either convert the response into a String and then use the JSON parser to convert it into an object or create a custom BodyHandler that does the conversion. In this example you see the latter approach in action.

BodyHandler is an interface and you need to implement the apply() method. In this example the JSON parser (jsonb) takes the bytes from the response and converts them into the target type.

public class JsonBodyHandler<T> implements HttpResponse.BodyHandler<T> {
  private final Jsonb jsonb;
  private final Class<T> type;

  public static <T> JsonBodyHandler<T> jsonBodyHandler(final Class<T> type) {
    return jsonBodyHandler(JsonbBuilder.create(), type);
  }

  public static <T> JsonBodyHandler<T> jsonBodyHandler(final Jsonb jsonb,
      final Class<T> type) {
    return new JsonBodyHandler<>(jsonb, type);
  }

  private JsonBodyHandler(Jsonb jsonb, Class<T> type) {
    this.jsonb = jsonb;
    this.type = type;
  }

  @Override
  public HttpResponse.BodySubscriber<T> apply(
      final HttpResponse.ResponseInfo responseInfo) {
    return BodySubscribers.mapping(BodySubscribers.ofByteArray(),
        byteArray -> this.jsonb.fromJson(new ByteArrayInputStream(byteArray), this.type));
  }
}

JsonBodyHandler.java

In the main application you can now use this BodyHandler to very conveniently convert a JSON response into a POJO.

    Jsonb jsonb = JsonbBuilder.create();
    
    var client = HttpClient.newHttpClient();
    var request = HttpRequest.newBuilder(URI.create("https://localhost:8443/user"))
                    .build();

    HttpResponse<User> response = client.send(request,
          JsonBodyHandler.jsonBodyHandler(jsonb, User.class));

JsonDemo.java
POST

In this section we are going to look at some examples with POST. Unlike GET, a POST always requires a body, therefore you have to pass an instance of BodyPublisher to the POST() method. If for some reasons you don't want to send any body you can call the special BodyPublishers.noBody factory method.
Text

This example sends the String this is a text to the server. BodyPublishers.ofString creates a BodyPublisher that takes a String and converts it into bytes for the request body. This BodyPublisher converts the given String with the UTF-8 character set. There is a second ofString() method available that takes the character set as second argument and uses that for the conversion.

    var client = HttpClient.newHttpClient();
    var request = HttpRequest.newBuilder()
                    .POST(BodyPublishers.ofString("this is a text"))
                    .uri(URI.create("https://localhost:8443/uppercase"))
                    .header("Conent-Type", "text/plain")
                    .build();

    return client.sendAsync(request, BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .exceptionally(e -> "Error: " + e.getMessage())
                    .thenAccept(System.out::println);

Post.java

JSON

Like for fetching JSON, there is no special built-in support for posting JSON. You simply convert an object to a JSON string and then treat it like any String POST request. Make sure that you specify the correct Content-Type header if the back end depends on this information to work correctly.

    Jsonb jsonb = JsonbBuilder.create();
    var client = HttpClient.newHttpClient();

    User user = new User(2, "Mr. Client");
    var request = HttpRequest.newBuilder()
                    .POST(BodyPublishers.ofString(jsonb.toJson(user)))
                    .uri(URI.create("https://localhost:8443/saveUser"))
                    .header("Content-Type", "application/json")
                    .build();

    HttpResponse<Void> response = client.send(request, BodyHandlers.discarding());

JsonDemo.java

The /saveUser endpoint does not return anything thus I use a special BodyHandler that discards the body.

Formdata (x-www-form-urlencoded)

There is no built-in support to send a POST request with x-www-form-urlencoded, but it's not that complicated to implement it. When you send a x-www-form-urlencoded POST the keys and values are encoded in key-value tuples separated by & with = between the key and the value. Non-alphanumeric characters in keys and values must be properly encoded.

A simple utility method takes a Map of key/value pairs and converts them into the proper form for a x-www-form-urlencoded POST request.

  public static BodyPublisher ofFormData(Map<Object, Object> data) {
    var builder = new StringBuilder();
    for (Map.Entry<Object, Object> entry : data.entrySet()) {
      if (builder.length() > 0) {
        builder.append("&");
      }
      builder
          .append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
      builder.append("=");
      builder
          .append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
    }
    return BodyPublishers.ofString(builder.toString());
  }

Post.java

And here the code that sends the request to the server. It calls the method above to convert the parameters into a proper body and sets the Content-Type header.

    var client = HttpClient.newHttpClient();

    Map<Object, Object> data = new HashMap<>();
    data.put("id", 1);
    data.put("name", "a name");
    data.put("ts", System.currentTimeMillis());

    var request = HttpRequest.newBuilder()
                    .POST(ofFormData(data))
                    .uri(URI.create("https://localhost:8443/formdata"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .build();

    return client.sendAsync(request, BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .exceptionally(e -> "Error: " + e.getMessage())
                    .thenAccept(System.out::println);

Post.java
Compression

As mentioned at the beginning, the Java 11 HTTP client does not handle compressed responses nor does it send the Accept-Encoding request header to request compressed responses by default.

If we know that the server is able to send back compressed resources, we can request them by adding the Accept-Encoding header. In this example we only want a compressed response if it's in the gzip format.

    var client = HttpClient.newHttpClient();
    var request = HttpRequest.newBuilder()
                    .GET()
                    .header("Accept-Encoding", "gzip")
                    .uri(URI.create("https://localhost:8443/indexWithoutPush"))
                    .build();

Get.java

The server can disregard this header and send back an uncompressed response or he complies and sends back gzip compressed resources. In our application we have to handle both cases, unless your are absolutely certain that a server always sends back compressed resources.

To check if a resource is compressed the application reads the Content-Encoding response header. If this header is present and contains the value gzip, the application uses the built-in GZIPInputStream to decompress the response body. Otherwise, the resource is uncompressed and no special handling is needed.

    HttpResponse<InputStream> response = client.send(request, BodyHandlers.ofInputStream());

    String encoding = response.headers().firstValue("Content-Encoding").orElse("");
    if (encoding.equals("gzip")) {
      System.out.println("gzip compressed");
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      try (InputStream is = new GZIPInputStream(response.body()); var autoCloseOs = os) {
        is.transferTo(autoCloseOs);
      }
      System.out.println(new String(os.toByteArray(), StandardCharsets.UTF_8));
    }
    else {
      System.out.println("not compressed");
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      try (var is = response.body(); var autoCloseOs = os) {
        is.transferTo(autoCloseOs);
      }
      System.out.println(new String(os.toByteArray(), StandardCharsets.UTF_8));
    }

Get.java
Query Parameters

Parameters are key/value pairs that are added to the URI. For example https://duckduckgo.com/?q=java+11 sends a query parameter q with the value java+11 to the server. Notice that you also need to properly encode these parameters. The built-in method URLEncoder.encode helps you with this conversion.

As mentioned at the beginning, there is no built-in URI builder that provides an easy way to create URIs programmatically.

The only built-in way is by concatenating Strings and encode them with URLEncoder. This is not very convenient when you work with many parameters. Fortunately there is a lightweight, zero dependency free library available, urlbuilder, that includes a URI builder. You add it with the following coordinates to your project

		<dependency>
		   <groupId>io.mikael</groupId>
		   <artifactId>urlbuilder</artifactId>
		   <version>2.0.9</version>
		</dependency>

Usage is straightforward. Every part of an URI can be specified separately and the values are automatically encoded if required.

    URI uri = UrlBuilder.empty()
                        .withScheme("https")
                        .withHost("localhost")
                        .withPort(8443)
                        .withPath("helloworld")
                        .addParameter("query", "value")
                        .toUri();
    request = HttpRequest.newBuilder(uri).build();

Get.java

An alternative is the org.springframework.web.util.UriComponentsBuilder class from the Spring framework. If your application already depends on Spring you can use this class and don't need to add any additional library.

	URI uri = org.springframework.web.util.UriComponentsBuilder.newInstance()
			         .scheme("https")
			         .host("localhost")
			         .port(8443)
			         .path("helloworld")
			         .queryParam("query", "value")
			         .build()
			         .toUri();

Headers

Headers are an important part of the HTTP protocol, they transfer metadata about requests and responses.
Request

The request builder provides three methods for adding headers: header, headers and setHeader.

header adds one header to the request. You can add the same header key multiple times and they will be all sent to the server. If you need to add many headers you may find headers convenient. You add multiple headers by passing the keys and values as arguments to the method and always alternate between key and value.

    var client = HttpClient.newHttpClient();
    var request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("https://localhost:8443/headers"))
                    .header("X-Auth", "authtoken")
                    .headers("X-Custom1", "value1", "X-Custom2", "value2")
                    .setHeader("X-Auth", "overwrite authtoken")
                    .build();

Get.java

headers also allows you to add multiple values to one key. You just have to supply the same key name with each new value.

.headers("X-MyHeader", "one", "X-MyHeader", "two", "X-MyHeader", "three")
// equivalent to
.header("X-MyHeader", "one").header("X-MyHeader", "two").header("X-MyHeader", "three")

setHeader adds one key/value pair to the request headers and it overwrites previously set header with the same name. In the example above the setHeader() call overwrites the header from the header() call.

Response

The response provides the method headers() to access the response headers. This method returns an instance of HttpHeaders, a read-only view of the headers.

allValues() returns all values for a given header as unmodifiable List. The method always returns a List and may be empty if there is no header with the give name in the response.

firstValue() and firstValueAsLong() return an Optional containing the first value of a given header. If the header is not present the Optional is empty. firstValueAsLong() in addition to retrieving the header it also converts the value to a Long. This method throws a NumberFormatException if a value is present but does not parse as Long.

      HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

      for (String value : response.headers().allValues("X-Custom-Header")) {
        System.out.println(value);
      }

      String firstValue = response.headers().firstValue("X-Custom-Header").orElse("");
      long time = response.headers().firstValueAsLong("X-Time").orElse(-1L);

Get.java
Cookies

Cookies are a way to add state to HTTP, which by design is a stateless protocol. But with Cookies the server is able to associate multiple requests to the same session.

Cookies are, from a technical standpoint, just HTTP headers: Cookie (request) and Set-Cookie (response). But they are treated in a special way by the browsers. When a server wants to set a cookie, he adds the Set-Cookie header to the response. The client reads the value of this header and sends it in the Cookie header with each consecutive request back to the server.

The Java 11 HttpClient has built-in cookie support but it's disabled by default. To enable it you use the following code.

    CookieHandler.setDefault(new CookieManager());

    var client = HttpClient.newBuilder()
                  .cookieHandler(CookieHandler.getDefault())
                  .build();

    //OR
    /*
    var client = HttpClient.newBuilder()
                  .cookieHandler(new CookieManager())
                  .build();
    */

Cookie.java

The default constructor creates a cookie manager that stores all cookies in RAM. You can change this behavior by instantiating the manager with the other constructor (CookieManager​(CookieStore store, CookiePolicy cookiePolicy)) and specify an implementation of the CookieStore interface.

With the CookieManager in place there is no special request or response handling necessary. Everything is handled by the HTTP client transparently. The /setCookie endpoint sets a cookie and the client sends this cookie together with the next request to secondCookieRequest.

    var request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("https://localhost:8443/setCookie"))
                    .build();

    HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
    System.out.println(response.statusCode());
    System.out.println(response.headers().firstValue("set-cookie"));

    request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://localhost:8443/secondCookieRequest"))
                .build();

    response = client.send(request, BodyHandlers.ofString());

Cookie.java
Redirect

Redirects are a signal from the server to the client that a resource has been moved to another location. Browsers automatically send another request to the new location when they receive a redirect response (301, 302, 303, 307, 308).

For the following examples I wrote a server endpoint /redirect that returns 308 PERMANENT_REDIRECT and a new location /helloworld.
NEVER

By default, the redirect policy is set to Redirect.NEVER which tells the HttpClient not to follow any redirects. In this mode the application is responsible for handling redirect responses. The following request sends back a status code of 308. The application could now read the new location from the Location response header, maybe update the URI stored somewhere in a database and issue another GET request to the new location.

    var client = HttpClient.newHttpClient();
    var request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("https://localhost:8443/redirect"))
                    .build();

    HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
    int sc = response.statusCode();
    String newLocation = response.headers().firstValue("Location").orElse(null);

RedirectDemo.java

NORMAL

With the Redirect.NORMAL mode the HTTP client automatically follows redirects, except from HTTPS to HTTP. The client automatically sends another request if he receives a redirect response.

An application can check if a redirect occurred by comparing the URI stored in the response object (response.uri()) with the request URI.

In this example the response URI is https://localhost:8443/helloworld and because it is different from the request URI the application knows that a redirect occurred.

    var client = HttpClient.newBuilder().followRedirects(Redirect.NORMAL).build();
    var request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("https://localhost:8443/redirect"))
                    .build();
    
    HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

    int sc = response.statusCode();
    String body = response.body();
    URI uri = response.uri();

RedirectDemo.java

ALWAYS

Redirect.ALWAYS behaves like NORMAL but also redirects from HTTPS URLs to HTTP URLs.
Basic Authentication

Basic Authentication is a simple way to protect resources on the server. If a client accesses such resources without any authentication, the server sends back a status code of 401. The client then re-sends the request with an authentication header attached to it.

See the MDN documentation for a more in depth look at Basic Authentication:
https://developer.mozilla.org/en-US/docs/Web/HTTP/Authentication

This is all handled transparently by the Java 11 HTTP client when an Authenticator instance is configured. You can do this in two ways.

    var client = HttpClient.newBuilder()
                  .authenticator(new BasicAuthenticator("user", "password"))
                  .build();

    /* OR
    Authenticator.setDefault(new BasicAuthenticator("user", "password"));
    var client = HttpClient.newBuilder()
                    .authenticator(Authenticator.getDefault())
                    .build();
    */

Basic.java

The application then requests the protected resource like any other unprotected resource.

    var request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("https://localhost:8443/secret"))
                    .build();

    HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

Basic.java
Files

HTTP is not limited to just sending and receiving text bodies. It is also capable of transferring binary data (image, audio, video, ...). The following example downloads a file and then uploads it to Virus Total. A service that analyses files to detect malware. The service provides a web interface, where users upload files via a browser, and an HTTP API that an application can access with any HTTP client.

Download

Downloading a file is very straightforward. Send a GET request and then handle the bytes in the response according to your use case. In this example we utilize a BodyHandler that automatically saves the bytes from the response into a file on the local filesystem.

    var url = "https://www.7-zip.org/a/7z1806-x64.exe";

    var client = HttpClient.newBuilder().build();
    var request = HttpRequest.newBuilder().GET().uri(URI.create(url)).build();

    Path localFile = Paths.get("7z.exe");
    HttpResponse<Path> response = client.send(request, BodyHandlers.ofFile(localFile));

File.java

Notice that the Java 11 client does not handle compression transparently. See the compression example above. In this case we download an installer for Windows, which is already compressed.

Upload with multipart

If the server endpoint just expects binary data in the request body an application could just send a POST request with BodyPublishers.ofFile. This publisher reads a file from the filesystem and sends the bytes in the body to the server.

But in this case we need to send some additional data in the POST request body and use a multipart form post with the Content-Type multipart/form-data. The request body is specially formatted as a series of parts, separated with boundaries. Unfortunately the Java 11 HTTP client does not provide any convenient support for this kind of body, but we can build it from scratch.

The following method takes a Map of key/value pairs and a boundary and then builds the multipart body.

  public static BodyPublisher ofMimeMultipartData(Map<Object, Object> data,
      String boundary) throws IOException {
    var byteArrays = new ArrayList<byte[]>();
    byte[] separator = ("--" + boundary + "\r\nContent-Disposition: form-data; name=")
        .getBytes(StandardCharsets.UTF_8);
    for (Map.Entry<Object, Object> entry : data.entrySet()) {
      byteArrays.add(separator);

      if (entry.getValue() instanceof Path) {
        var path = (Path) entry.getValue();
        String mimeType = Files.probeContentType(path);
        byteArrays.add(("\"" + entry.getKey() + "\"; filename=\"" + path.getFileName()
            + "\"\r\nContent-Type: " + mimeType + "\r\n\r\n").getBytes(StandardCharsets.UTF_8));
        byteArrays.add(Files.readAllBytes(path));
        byteArrays.add("\r\n".getBytes(StandardCharsets.UTF_8));
      }
      else {
        byteArrays.add(("\"" + entry.getKey() + "\"\r\n\r\n" + entry.getValue() + "\r\n")
            .getBytes(StandardCharsets.UTF_8));
      }
    }
    byteArrays.add(("--" + boundary + "--").getBytes(StandardCharsets.UTF_8));
    return BodyPublishers.ofByteArrays(byteArrays);

File.java

VirusTotal requires an API key to access the service. You get your key by joining the VirusTotal community.

The application then prepares the Map with all parameters we have to send to VirusTotal. In this case the API key and the file. As multipart boundary a random 256 length string is used.

    Map<Object, Object> data = new LinkedHashMap<>();
    data.put("apikey", virusTotalApiKey);
    data.put("file", localFile);
    String boundary = new BigInteger(256, new Random()).toString();

    request = HttpRequest.newBuilder()
              .header("Content-Type", "multipart/form-data;boundary=" + boundary)
              .POST(ofMimeMultipartData(data, boundary))
              .uri(URI.create("https://www.virustotal.com/vtapi/v2/file/scan"))
              .build();

    HttpResponse<String> vtResponse = client.send(request, BodyHandlers.ofString());

    try (JsonReader jsonReader = Json.createReader(new StringReader(vtResponse.body()))) {
      JsonObject jobj = jsonReader.readObject();
      String resource = jobj.getString("resource");
      URI uri = UrlBuilder.fromString("https://www.virustotal.com/vtapi/v2/file/report")
          .addParameter("apikey", virusTotalApiKey).addParameter("resource", resource)
          .toUri();

      HttpResponse<String> status = client.send(HttpRequest.newBuilder(uri).build(),
          BodyHandlers.ofString());

File.java

The VirusTotal API sends back a JSON. You don't get a scan result immediately back, because the scanning process takes several minutes. What the API sends back is a resource token that you can use to access the result. An application needs to periodically poll VirusTotal with this resource token to fetch the result of the malware scan. This is not shown here, the example just prints out the initial response of the upload.
HTTP/2 Server Push

HTTP/2 Server Push is a new way to send resources from a server to the client. In the traditional way a browser requests an HTML page, parses the code and sends additional requests for all the referenced resources on the page (JS, CSS, images, ...)

With HTTP/2 Server Push a server not only sends back the HTML it also sends back all the referenced resources that the browser needs to display the page.

In the following example the /indexWithPush endpoint not only sends back an HTML page but also a picture that is referenced on the page.

To handle these resources an application has to implement the PushPromiseHandler interface and then pass an instance of this implementation as third argument to the send() or sendAsync() method.

    request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://localhost:8443/indexWithPush"))
                .build();

    var asyncRequests = new CopyOnWriteArrayList<CompletableFuture<Void>>();

    PushPromiseHandler<byte[]> pph = (initial, pushRequest, acceptor) -> {
      CompletableFuture<Void> cf = acceptor.apply(BodyHandlers.ofByteArray())
          .thenAccept(response -> {
            System.out.println("Got pushed resource: " + response.uri());
            System.out.println("Body: " + response.body());
          });
      asyncRequests.add(cf);
    };

    client.sendAsync(request, BodyHandlers.ofByteArray(), pph)
          .thenApply(HttpResponse::body)
          .thenAccept(System.out::println)
          .join();

Push.java
WebSocket

Lastly, we look at a WebSocket example.

First we need to implement a WebSocket Listener. This is an interface composed of several methods, but all methods are implemented by default methods. Therefore you only have to implement the methods your application needs for its work. In this demo we listen for the open and close event and in onText print out any text message the server sends to us.

    Listener wsListener = new Listener() {
      @Override
      public CompletionStage<?> onText(WebSocket webSocket,
          CharSequence data, boolean last) {

        System.out.println("onText: " + data);

        return Listener.super.onText(webSocket, data, last);
      }

      @Override
      public void onOpen(WebSocket webSocket) {
        System.out.println("onOpen");
        Listener.super.onOpen(webSocket);
      }

      @Override
      public CompletionStage<?> onClose(WebSocket webSocket, int statusCode,
          String reason) {
        System.out.println("onClose: " + statusCode + " " + reason);
        return Listener.super.onClose(webSocket, statusCode, reason);
      }
    };

WebSocketDemo.java

Every WebSocket connection starts with a HTTP request. Similar to this we have to first create a HttpClient and then call newWebSocketBuilder().buildAsync() to build a WebSocket instance asynchronously. You can only build a WebSocket connection asynchronously, there is no blocking method available.

This example just blocks the calling thread with join() until it gets the WebSocket client. Not something you should do in a real application if you are writing your application in a non blocking fashion.

    var client = HttpClient.newHttpClient();

    WebSocket webSocket = client.newWebSocketBuilder()
               .buildAsync(URI.create("wss://localhost:8443/wsEndpoint"), wsListener).join();
    webSocket.sendText("hello from the client", true);
    
    TimeUnit.SECONDS.sleep(30);
    webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "ok");

WebSocketDemo.java

With the WebSocket instance the application can now send messages to the server. Text messages with sendText() and binary messages with sendBinary

sendClose() sends a close message to the server with a reason code. This method does not close the connection, it just initiates a proper shutdown of the connection. Usually a server closes the connection after receiving the close message.

To immediately and forcefully close a WebSocket connection you may call abort().

Notice that the Java 11 WebSocket client, like the WebSocket API in the browser, does not automatically reconnect to a server when the connection breaks. If your application requires a permanent connection you need to build a reconnection mechanism on top of the WebSocket client. For example, you could start a new WebSocket connection from the onClose event or implement a more robust mechanism by sending heartbeat messages and if they don't arrive tear down the connection and rebuild it.

This concludes our Java 11 HTTP client tour. You have seen a lot of examples and implementations for the missing convenient functionality for multipart, form data, URI builder and compression. With the new HTTP client Java finally has a robust HTTP client implementation built-in to the core. In many cases you no longer need to add an external HTTP client library to your project.

