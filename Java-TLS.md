# Java TLS and self-signed certificates guide

* When your applications is a client, which trusts all TLS certificates;
* When your applications is a client, which trusts only organization TLS certificates;
* When your applications is a client, which trusts your organization and default Java's certificates;

* When your application is a server, which sends a certificate to the client (one-way TLS);

* Mutual TLS (two-way TLS). When both client and server exchange certificates within Organization perimeter (both exchange self-signed certificates);
* Mutual TLS (two-way TLS). TLS based on trusting the Certificate Authority.

## Java Implementations

### Little Bit of Theory

![img_1.png](img_1.png)

The heart of the JSSE (Java Secure Socket Extension) architecture is the **SSLContext**. The context eventually creates end 
objects (**SSLSocket** and **SSLEngine**) which actually implement the SSL/TLS protocol.
SSLContexts are initialized with two callback classes, **KeyManager** and **TrustManager**, which allow applications to first select authentication 
material to send and second to verify credentials sent by a peer.

A **KeyManager** is responsible for choosing which credentials to present to a peer.
A **TrustManager** is responsible for verifying the credentials received from a peer.

### Java Security Standard Algorithm Names

In the upcoming code, you'll see String parameters of secure Algorithm names used, like: ``SSLContext.getInstance("TLS");`` or 
``CertificateFactory.getInstance("X.509");`` and so on. You don't need to know them by heart as they are documented
in [Java Security Standard Algorithm Names](https://docs.oracle.com/en/java/javase/15/docs/specs/security/standard-names.html). 

For example, already mentioned [CertificateFactory](https://docs.oracle.com/en/java/javase/15/docs/specs/security/standard-names.html#certificatefactory-types), 
contains only one algorithm - ``X.509``.

### Playground for testing TLS communication

#### Server
Java has simple built in ``HttpServer`` and ``HttpsServer`` servers. For more information you can read [here](https://docs.oracle.com/en/java/javase/11/docs/api/jdk.httpserver/com/sun/net/httpserver/package-summary.html) 
and [here](https://docs.oracle.com/en/java/javase/11/docs/api/jdk.httpserver/com/sun/net/httpserver/HttpsServer.html). 
For testing purposes they are great as they do not require any external dependencies and are quick to launch.

##### UnSecureServer (HttpServer)
```
public class UnSecureServer {

    public static void main(String[] args) throws IOException {
        var server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/hello", new HelloController());
        server.start();
        System.out.println("Server is ready to receive requests");
    }

    private static class HelloController implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            try (OutputStream responseBody = exchange.getResponseBody()) {
                System.out.println("Request received");
                exchange.getResponseHeaders().set("Content-Type", "text/plain");
                String payload = "Hello";
                exchange.sendResponseHeaders(200, payload.length());
                responseBody.write(payload.getBytes(StandardCharsets.UTF_8));
            }
        }
    }
}
```

Yes, this is all it's required to launch a simple server.

##### SecureServer (HttpsServer)

Secure server requires a little more work. Namely:
- Create ``KeyStore`` which will contain server's private and public keys;
- Load ``KeyStore`` into ``HttpsServer``.

To generate ``KeyStore`` I'll use ``keytool``:
```
keytool -v -genkeypair -keystore server-identity.jks -dname "CN=test, OU=Unknown, O=Unknown, L=Unknown, ST=Unknown, C=Unknown" -storepass secret -keypass secret -keyalg RSA -keysize 4096 -alias server -validity 3650 -deststoretype pkcs12 -ext SubjectAlternativeName=DNS:localhost,DNS:yourfqdn
```

To read more about parameters refer to "When your application is a server, which sends a certificate to the client (one-way TLS)". 
I've placed generated ``server-identity.jks`` under ``src/main/resources/server``. Also extract public key. It will be used by client later on:
```
keytool -v -exportcert -file server.cer -alias server -keystore server-identity.jks -storepass secret -rfc
```

Move generated ``server.cer`` to ``src/main/resources/certs``.

```
public class SecureServer {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {
        var server = HttpsServer.create(new InetSocketAddress(8443), 0);

        var sslContext = SSLContext.getInstance("TLS");
        sslContext.init(createKeyManagers(), null, null);
        server.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
            @Override
            public void configure(HttpsParameters params) {
                params.setSSLParameters(sslContext.getDefaultSSLParameters());
                params.setNeedClientAuth(false); //Whether mutual TLS is enabled
            }
        });
        server.createContext("/hello", new HelloController());
        server.start();
        System.out.println("Server is ready to receive requests");
    }

    private static KeyManager[] createKeyManagers() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        var password = "secret";
        var instance = KeyStore.getInstance(KeyStore.getDefaultType());
        var keyStoreFile = Files.newInputStream(Path.of("src/main/resources/server/server-identity.jks"));
        instance.load(keyStoreFile, password.toCharArray());

        var keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(instance, password.toCharArray());

        return keyManagerFactory.getKeyManagers();
    }

    private static class HelloController implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try (OutputStream responseBody = exchange.getResponseBody()) {
                System.out.println("Request received");
                exchange.getResponseHeaders().set("Content-Type", "text/plain");
                String payload = "Hello";
                exchange.sendResponseHeaders(200, payload.length());
                responseBody.write(payload.getBytes(StandardCharsets.UTF_8));
            }
        }
    }
}
```

#### HttpClient

I'll use Java's ``HttpClient``, because it does not require any dependencies and is easy to configure.

``HttpClient`` for Http:
```
HttpClient.newBuilder()
      .version(HttpClient.Version.HTTP_1_1)
      .connectTimeout(Duration.ofSeconds(10L))
      .followRedirects(HttpClient.Redirect.NEVER)
      .proxy(HttpClient.Builder.NO_PROXY)
      .build();
```

``HttpClient`` for Https:
```
var sslContext = ...;

HttpClient.newBuilder()
      .version(HttpClient.Version.HTTP_1_1)
      .connectTimeout(Duration.ofSeconds(10L))
      .followRedirects(HttpClient.Redirect.NEVER)
      .proxy(HttpClient.Builder.NO_PROXY)
      .sslContext(sslContext)
      .build();
```

For example, to test communication with ``UnSecureServer``, just launch ``UnSecureServer`` and then launch ``TestCommunicationWithServer`` which will send request:
```
public class TestCommunicationWithServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        var httpClient = HttpClient.newHttpClient();

        var request = HttpRequest.newBuilder(URI.create("http://localhost:8080/hello")).build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response);
        System.out.println(response.body());
    }
}
```

**When testing communication with ``SecureServer``, don't forget to change URL to: ``https://localhost:8443/hello``**.

## When your applications is a client, which trusts all TLS certificates

### What you'll need

Running ``SecureServer`` to test client. **Do not use this in production, because it's unsafe.**

### Creating SSLContext
```
public class TrustAllCertificatesSSLContext {

    public static SSLContext trustingSSLContext() {
        var trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        try {
            var sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, null); //When nulls are passed in ``init`` method, that means that Java's default should be used. 
            return sslContext;
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Couldn't initialize", e);
        }
    }
}
```

### Using Java's HTTP Client
```
public class TrustingHttpClientConfiguration {

    public static HttpClient httpClient() {
        var sslContext = TrustAllCertificatesSSLContext.trustingSSLContext();

        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10L))
                .followRedirects(HttpClient.Redirect.NEVER)
                .proxy(HttpClient.Builder.NO_PROXY)
                .sslContext(sslContext)
                .build();
    }
}
```

### Using Spring's RestTemplate
```
@Configuration
public class TrustingRestTemplateConfiguration {

    @Bean
    public static RestTemplate restTemplate(RestTemplateBuilder builder) {
        var restTemplate = builder.build();
        var sslContext = TrustAllCertificatesSSLContextForRestTemplate.trustingSSLContext();

        var httpClient = HttpClients.custom().setSSLContext(sslContext).build();
        var requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }
}
```

You'll also will need additional dependency:
```
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>add-version</version>
</dependency>
```

Notice that I'm using ``TrustAllCertificatesSSLContext.trustingSSLContext()`` in both ``RestTemplate`` and ``Java's HttpClient`` implementations.

## When your applications is a client, which trusts only organization TLS certificates

There are a couple of strategies to implement this:
* Download/Generate and place certificate within application ``resources``;
* Mount application on volume containing certificate;
* Create new ``TrustStore`` with only organization certificates via ``keytool``;
* Build docker image which contains correct ``TrustStore``.

### Download and place certificate within application ``resources`` and create ``TrustStore`` with only organisation certificate

#### What you'll need

Organization certificate, preferably both root CA and issuing CAs certificate. Or ``SecureServer`` certificate from "Playground" section. 

#### Creating SSLContext

Firstly, place certificates into ``src/main/resources/certs/`` folder. Then, modify ``ALIAS_AND_CERTIFICATE_PATHS`` ``Map`` accordingly:
```
public class ReadCertificatesFromResourcesAndCreateTrustStoreOnlyContainingThem {

    private static final Map<String, String> ALIAS_AND_CERTIFICATE_PATHS = Map.of(
            "alias1", "certs/cert1.crt",
            "alias2", "certs/cert2.crt"
    );

    public SSLContext sslContext() {
        var keyStore = organisationCertificateKeyStore();
        var trustManagers = trustingOnlyOrganisationCertificates(keyStore);

        try {
            var sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, null);
            return sslContext;
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Couldn't initialize", e);
        }
    }

    private TrustManager[] trustingOnlyOrganisationCertificates(KeyStore keyStore) {
        try {
            var trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            return trustManagerFactory.getTrustManagers();
        } catch (NoSuchAlgorithmException | KeyStoreException e) {
            throw new RuntimeException("Couldn't initialize", e);
        }
    }

    private KeyStore organisationCertificateKeyStore() {
        try {
            var keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null); //To create an empty keystore pass null as the InputStream argument [from JavaDocs]

            for (var certificateAliasAndPath : ALIAS_AND_CERTIFICATE_PATHS.entrySet()) {
                //Read certificate file as bytes. Using classLoader instead of Files due to have resources are read differently when they're packaged in a JAR
                var organisationRootCertBytes = this.getClass().getClassLoader().getResourceAsStream(certificateAliasAndPath.getValue()).readAllBytes();
                var certificateFactory = CertificateFactory.getInstance("X.509");//Currently, there is only one type of factory
                var certificate = certificateFactory.generateCertificate(new ByteArrayInputStream(organisationRootCertBytes));
                keyStore.setCertificateEntry(certificateAliasAndPath.getKey(), certificate); //Add certificates to keyStore
            }
            return keyStore;
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException("Couldn't initialize", e);
        }
    }
}
```

##### Java's HTTP Client
```
public class TrustingHttpClientConfiguration {

    public static HttpClient httpClient() {
        var sslContext = new ReadCertificatesFromResourcesAndCreateTrustStoreOnlyContainingThem().sslContext();

        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10L))
                .followRedirects(HttpClient.Redirect.NEVER)
                .proxy(HttpClient.Builder.NO_PROXY)
                .sslContext(sslContext)
                .build();
    }
}
```

#### Spring's RestTemplate
```
@Configuration
public class TrustingRestTemplateConfiguration {

    @Bean
    public static RestTemplate restTemplate(RestTemplateBuilder builder) {
        var restTemplate = builder.build();
        var sslContext = new ReadCertificatesFromResourcesAndCreateTrustStoreOnlyContainingThem().sslContext();

        var httpClient = HttpClients.custom().setSSLContext(sslContext).build();
        var requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }
}
```

### Mount application on volume containing certificate and create ``TrustStore`` with only organisation certificate

TODO

#### What you'll need

TODO

#### Creating SSLContext

TODO

#### Java's HTTP Client

TODO

#### Spring's RestTemplate

TODO

### Create new ``TrustStore`` with only organization certificates via ``keytool``

#### What you'll need

Organization certificate, preferably both root CA and issuing CAs certificate. Or ``SecureServer`` certificate from "Playground" section.

#### Creating ``TrustStore`` with ``keytool``

Create new TrustStore using ``keytool``:
```
keytool -v -importcert -file organization.cer -alias organizationCer1 -keystore client-truststore.jks -storepass truststorepass -noprompt
```

#### Creating SSLContext

Place ``client-truststore.jks`` into ``resources/certs`` and then create ``SSLContext``:

```
public class SSLContextUsingExistingTrustStore {

    private static final String SERVER_CER_PATH = "certs/client-truststore.jks";
    private static final char[] DO_NOT_PASS_PASSWORD_THIS_WAY = "truststorepass".toCharArray(); //This should be inject using secret management

    public SSLContext sslContext() {
        var keyStore = organisationCertificateKeyStore();
        var trustManagers = trustingOnlyOrganisationCertificates(keyStore);

        try {
            var sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, null);
            return sslContext;
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Couldn't initialize", e);
        }
    }

    private TrustManager[] trustingOnlyOrganisationCertificates(KeyStore keyStore) {
        try {
            var trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            return trustManagerFactory.getTrustManagers();
        } catch (NoSuchAlgorithmException | KeyStoreException e) {
            throw new RuntimeException("Couldn't initialize", e);
        }
    }

    private KeyStore organisationCertificateKeyStore() {
        try {
            var certificateAsInputStream = this.getClass().getClassLoader().getResourceAsStream(SERVER_CER_PATH);
            var keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(certificateAsInputStream, DO_NOT_PASS_PASSWORD_THIS_WAY);

            return keyStore;
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException("Couldn't initialize", e);
        }
    }
}
```

In clients use ``SSLContextUsingExistingTrustStore`` to generate ``SSLContext``.

#### Use created ``TrustStore`` via Java's properties

Following [JSSE documentation](https://docs.oracle.com/en/java/javase/15/security/java-secure-socket-extension-jsse-reference-guide.html#GUID-460C3E5A-A373-4742-9E84-EB42A7A3C363), one can do like so:
```
public class TestCommunicationWithServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.setProperty("javax.net.ssl.trustStore", "src/main/resources/certs/client-truststore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "truststorepass");

        var httpClient = HttpClient.newHttpClient();

        var request = HttpRequest.newBuilder(URI.create("https://localhost:8443/hello")).build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response);
        System.out.println(response.body());
    }
}
```

Notice that I can use default ``HttpClient`` without needing to build one with custom ``SSLContext``.

#### Use created ``TrustStore`` via Java's VM options

Or, I can start Java application with properties like so:
```
java -Djavax.net.ssl.trustStore=src/main/resources/certs/client-truststore.jks -Djavax.net.ssl.trustStorePassword=truststorepass MyApp
```

Then client is very simple:
```
public class TestCommunicationWithServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        var httpClient = HttpClient.newHttpClient();

        var request = HttpRequest.newBuilder(URI.create("https://localhost:8443/hello")).build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response);
        System.out.println(response.body());
    }
}
```

### Build docker image which contains correct ``TrustStore``

TODO

#### What you'll need

TODO

#### Creating SSLContext

TODO

#### Java's HTTP Client

TODO

#### Spring's RestTemplate

TODO

## When your applications is a client, which trusts your organization and default Java's certificates

There are a couple of strategies to implement this:
* Download/Generate and place your organization certificate within application ``resources``;
* Mount application on volume containing your organization certificate;
* Import certificate into Java's default ``TrustStore``;
* Build docker image which contains correct ``TrustStore``.

### Download and place certificate within application ``resources``, create ``TrustStore`` and merge with default Java's ``TrustStore``

#### What you'll need

Organization certificate, preferably both root CA and issuing CAs certificate. Or ``SecureServer`` certificate from "Playground" section.

#### Creating SSLContext

To understand in depth why ``CompositeX509ExtendedTrustManager`` is required, refer to "Custom ``CompositeX509ExtendedTrustManager``" section. I've placed code regarding that class 
in mentioned section as well, hence I won't repeat it here.

```
public class SSLContextUsingCustomCertsAndDefault {

    private static final Map<String, String> ALIAS_AND_CERTIFICATE_PATHS = Map.of(
            "alias1", "certs/cert1.crt",
            "alias2", "certs/cert2.crt"
    );

    public SSLContext sslContext() {
        var keyStore = organisationCertificateKeyStore();
        var trustManagers = trustingOnlyOrganisationCertificates(keyStore);
        var compositeTrustManager = new CompositeX509ExtendedTrustManager(trustManagers[0]);

        try {
            var sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{compositeTrustManager}, null);
            return sslContext;
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Couldn't initialize", e);
        }
    }

    private TrustManager[] trustingOnlyOrganisationCertificates(KeyStore keyStore) {
        try {
            var trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            return trustManagerFactory.getTrustManagers();
        } catch (NoSuchAlgorithmException | KeyStoreException e) {
            throw new RuntimeException("Couldn't initialize", e);
        }
    }

    private KeyStore organisationCertificateKeyStore() {
        try {
            var keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null); //To create an empty keystore pass null as the InputStream argument [from JavaDocs]

            for (var certificateAliasAndPath : ALIAS_AND_CERTIFICATE_PATHS.entrySet()) {
                //Read certificate file as bytes. Using classLoader instead of Files due to how resources are read differently when they're packaged as a JAR
                var organisationRootCertBytes = this.getClass().getClassLoader().getResourceAsStream(certificateAliasAndPath.getValue()).readAllBytes();
                var certificateFactory = CertificateFactory.getInstance("X.509");//Currently, there is only one type of factory
                var certificate = certificateFactory.generateCertificate(new ByteArrayInputStream(organisationRootCertBytes));
                keyStore.setCertificateEntry(certificateAliasAndPath.getKey(), certificate); //Add certificates to keyStore
            }
            return keyStore;
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException("Couldn't initialize", e);
        }
    }
}
```

The code should be familiar, the only difference is this line: ```var compositeTrustManager = new CompositeX509ExtendedTrustManager(trustManagers[0]);``` and: 
```sslContext.init(null, new TrustManager[]{compositeTrustManager}, null);```. 

The latter is quite self-explanatory, because of method signature ```public final void init(KeyManager[] km, TrustManager[] tm, SecureRandom random)```. As stated in
"Custom ``CompositeX509ExtendedTrustManager``" section, this is an example of bad Java's API, because ``SSLContext`` only uses one TrustManager (first one it finds), however accepts an array, which is misleading.

The formal is where we create our new ``compositeTrustManager``, which contains both default and custom ``KeyStore``s. We can easily count/list all certificates, by adding a ``println`` 
in ``sslContext`` method:

```
...
public SSLContext sslContext() {
    var keyStore = organisationCertificateKeyStore();
    var trustManagers = trustingOnlyOrganisationCertificates(keyStore);
    var compositeTrustManager = new CompositeX509ExtendedTrustManager(trustManagers[0]);
    
    System.out.println("-".repeat(10));
    System.out.println(compositeTrustManager.getAcceptedIssuers().length);
    System.out.println("-".repeat(10));
...
```

The number of certificates is equal to default's Java's trusted certificates (in my case it's 91) + 2 added organization's. To find out Java's trusted certificates count, go to Java's directory in 
your computer. In my case it's in ``C:\Program Files\Java\jdk-15\lib\security>`` and run the command ``keytool -list -keystore cacerts -storepass changeit``:
```
C:\Program Files\Java\jdk-15\lib\security>keytool -list -keystore cacerts -storepass changeit
Keystore type: JKS
Keystore provider: SUN

Your keystore contains 91 entries

verisignclass2g2ca [jdk], 1998-05-18, trustedCertEntry,
Certificate fingerprint (SHA-256): 3A:43:E2:20:FE:7F:3E:A9:65:3D:1E:21:74:2E:AC:2B:75:C2:0F:D8:98:03:05:BC:50:2C:AF:8C:2D:9B:41:A1
...
```

You will see the count of entries in the statement.

To list certificates' issuers:
```
...
public SSLContext sslContext() {
    var keyStore = organisationCertificateKeyStore();
    var trustManagers = trustingOnlyOrganisationCertificates(keyStore);
    var compositeTrustManager = new CompositeX509ExtendedTrustManager(trustManagers[0]);

    System.out.println("-".repeat(10));
    Stream.of(compositeTrustManager.getAcceptedIssuers())
            .map(X509Certificate::getIssuerDN)
            .forEach(System.out::println);
    System.out.println("-".repeat(10));
...
```

At the end of the list, you should see your added certificates as well.

##### Java's HTTP Client
```
public class TrustingHttpClientConfiguration {

    public static HttpClient httpClient() {
        var sslContext = new SSLContextUsingCustomCertsAndDefault().sslContext();

        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10L))
                .followRedirects(HttpClient.Redirect.NEVER)
                .proxy(HttpClient.Builder.NO_PROXY)
                .sslContext(sslContext)
                .build();
    }
}
```

#### Spring's RestTemplate
```
@Configuration
public class TrustingRestTemplateConfiguration {

    @Bean
    public static RestTemplate restTemplate(RestTemplateBuilder builder) {
        var restTemplate = builder.build();
        var sslContext = new SSLContextUsingCustomCertsAndDefault().sslContext();

        var httpClient = HttpClients.custom().setSSLContext(sslContext).build();
        var requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }
}
```

### Mount application on volume containing certificate, create ``TrustStore`` and merge with default Java's ``TrustStore``

TODO

#### What you'll need

TODO

#### Creating SSLContext

TODO

#### Java's HTTP Client

TODO

#### Spring's RestTemplate

TODO

### Import certificate into Java's default ``TrustStore``

My Java placed in ``C:\Program Files\Java\jdk-15\lib\security>``. There is a file called ``cacerts``. This is default Java's ``TrustStore``. Using keytool import server certificate:
```
keytool -import -alias server -keystore "C:\Program Files\Java\jdk-15\lib\security\cacerts" -file server.cer -storepass changeit
```

**You need to run ``cmd`` in Admin mode. And don't forget to create a copy of cacerts to later on rollback to default.**

Then your ``TestCommunicationWithServer`` can be as simple as:
```
public class TestCommunicationWithServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        var httpClient = HttpClient.newHttpClient();

        var request = HttpRequest.newBuilder(URI.create("https://localhost:8443/hello")).build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response);
        System.out.println(response.body());
    }
}
```


### Build docker image which contains correct ``TrustStore``

TODO

#### What you'll need

TODO

#### Creating SSLContext

TODO

#### Java's HTTP Client

TODO

#### Spring's RestTemplate

TODO


## When your application is a server, which sends a certificate to the client (one-way TLS)

Let's create a simple Spring Boot server (from here on **server**). This is completely empty Spring project, just with ``HelloController``:
```
@RestController
public class HelloController {

    @GetMapping(value = "hello", produces = MediaType.TEXT_PLAIN_VALUE)
    public String hello() {
        return "hello";
    }
}
```

Run ``TestCommunicationWithServer`` to validate that it's working (note that we're currently calling ``http://localhost:8080/hello``):
```
(GET http://localhost:8080/hello) 200
```

Now secure server by modifying ``application.yml``:
```
server:
  port: 8443
  ssl:
    enabled: true
```

You will probably ask yourself why the port is set to 8443. The port convention for a tomcat server with https is 8443, and for http, it is 8080. So, we could use port 8080 for https connections, 
but it is a bad practice. See [Wikipedia](https://en.wikipedia.org/wiki/List_of_TCP_and_UDP_port_numbers) for more information about port conventions.

Restart the server so that it can apply the changes you made. You will probably get the following exception: ``IllegalArgumentException: Resource location must not be null.``

You are getting this message because the server requires a keystore with the certificate of the server to ensure that there is a secure connection with the outside world.

To solve this issue, you are going to create a keystore with a public and private key for the server. The public key will be shared with users so that they can encrypt the communication. 
The communication between the user and server can be decrypted with the private key of the server.

To create a keystore with a public and private key, execute the following command in your terminal:
```
keytool -v -genkeypair -keystore server-identity.jks -dname "CN=test, OU=Unknown, O=Unknown, L=Unknown, ST=Unknown, C=Unknown" -storepass secret -keypass secret -keyalg RSA -keysize 4096 -alias server -validity 3650 -deststoretype pkcs12 -ext SubjectAlternativeName=DNS:localhost,DNS:yourfqdn
```

Where:
* CN - Common Name;
* O - Organization;
* L - Locality;
* S - State or Province Name;
* C - Country Name.

``SubjectAlternativeName`` plays a role in hostname verification. It is a part of HTTPS that involves a server identity check to ensure that the client is talking to the correct server 
and has not been redirected by a man in the middle attack.

More can be found on [Server Identity and Subject Alternative Name](https://tools.ietf.org/html/rfc2818#section-3.1).

Tell your server where the location of the keystore is and provide the passwords. ``application.yml``:
```
server:
  port: 8443
  ssl:
    enabled: true
    key-store: classpath:identity.jks
    key-password: secret
    key-store-password: secret
```

Run the server and change the client's request to ``var request = HttpRequest.newBuilder(URI.create("https://localhost:8443/hello")).build();``. Both port and protocol has to be adjusted.
When the client is run, the following message will appear:``PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target``.
This means that the client wants to communicate over HTTPS and during the handshake procedure it received the certificate of the server which it doesn't recognize yet.

### Export certificate of the server
```
keytool -v -exportcert -file server.cer -alias server -keystore server-identity.jks -storepass secret -rfc
```

You'll get ``server.cer`` which can be used by the client in following already outlined strategies ``When your applications is a client, which trusts all TLS certificates``, 
``When your applications is a client, which trusts only organization TLS certificates``, ``When your applications is a client, which trusts your organization and default Java's certificates``.

## Mutual TLS (two-way TLS). When both client and server exchange certificates within Organization perimeter (both exchange self-signed certificates)

The next step is to require the authentication of the client. This will force the client to identify itself, and in that way, the server can also validate the identity of the client and whether or not 
it is a trusted one. You can enable this by telling the server that you also want to validate the client with the property client-auth. Put the following properties in the ``application.yml`` of the server:

```
server:
  port: 8443
  ssl:
    enabled: true
    key-store: classpath:identity.jks
    key-password: secret
    key-store-password: secret
    client-auth: need
```

If you run the client, it will fail with the following error message: ``javax.net.ssl.SSLHandshakeException: Received fatal alert: bad_certificate.`` 
This indicates that the certificate of the client is not valid because there is no certificate at all. Create one with the following command:
```
keytool -v -genkeypair -keystore client-identity.jks -dname "CN=client, OU=Unknown, O=Unknown, L=Unknown, ST=Unknown, C=Unknown" -storepass secret -keypass secret -keyalg RSA -keysize 4096 -alias client -validity 3650 -deststoretype pkcs12
```

#### Export certificate of the client
```
keytool -v -exportcert -file client.cer -alias client -keystore client-identity.jks -storepass secret -rfc
```

#### Create ``TrustStore`` for the server containing only client's certificate
```
keytool -v -importcert -file client.cer -alias client -keystore truststore.jks -storepass truststorepass -noprompt
```

Update ``application.yaml``:
```
server:
  port: 8443
  ssl:
    enabled: true
    key-store: classpath:identity.jks
    key-password: secret
    key-store-password: secret
    client-auth: need
    trust-store: classpath:truststore.jks
    trust-store-password: truststorepass
```

#### Create ``TrustStore`` for the server containing client's certificate and default Java's certificates



#### Create client's ``SSLContext`` containing both KeyManager and TrustManager

Custom ``KeyManager`` is required, because client needs to send certificate information to the server. As if it's acting like a server itself. And ``TrustManager`` to validate server's certificate.

The class is pretty much the same as previous cases, but just with added twist - ``clientsKeyManager`` method.

```
public class SSLContextWithKeyManagerAndTrustManager {

    private static final String CLIENT_CER_PATH = "certs/client-identity.jks";
    private static final char[] DO_NOT_PASS_PASSWORD_THIS_WAY_CLIENT = "secret".toCharArray(); //This should be inject using secret management

    private static final String SERVER_CER_PATH = "certs/server.cer";

    public SSLContext sslContext() {
        var trustManagers = serversTrustManager(SERVER_CER_PATH);
        var keyManager = clientsKeyManager(CLIENT_CER_PATH, DO_NOT_PASS_PASSWORD_THIS_WAY_CLIENT);

        try {
            var sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManager, trustManagers, null);
            SSLContext.setDefault(sslContext);
            return sslContext;
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Couldn't initialize", e);
        }
    }

    private TrustManager[] serversTrustManager(String path) {
        try {
            var keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null); //To create an empty keystore pass null as the InputStream argument [from JavaDocs]
            //Read certificate file as bytes. Using classLoader instead of Files due to have resources are read differently when they're packaged as JAR
            var organisationRootCertBytes = this.getClass().getClassLoader().getResourceAsStream(path).readAllBytes();
            var certificateFactory = CertificateFactory.getInstance("X.509");//Currently, there is only one type of factory
            var certificate = certificateFactory.generateCertificate(new ByteArrayInputStream(organisationRootCertBytes));
            keyStore.setCertificateEntry("server", certificate); //Add certificates to keyStore

            var trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            return trustManagerFactory.getTrustManagers();
        } catch (NoSuchAlgorithmException | KeyStoreException | CertificateException | IOException e) {
            throw new RuntimeException("Couldn't initialize", e);
        }
    }

    private KeyManager[] clientsKeyManager(String path, char[] password) {
        try {
            var certificateAsInputStream = this.getClass().getClassLoader().getResourceAsStream(path);
            var keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(certificateAsInputStream, password);

            var keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, password);

            return keyManagerFactory.getKeyManagers();
        } catch (NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException | CertificateException | IOException e) {
            throw new RuntimeException("Couldn't initialize", e);
        }
    }
}
```

## Mutual TLS (two-way TLS). TLS based on trusting the Certificate Authority




## Custom ``CompositeX509ExtendedTrustManager``

### Why it's required?

For communication between internal services, some Organizations use self-signed certificates on both client and server. Simple and cheap (free!). Most of the time, these services only communicate over
HTTPS with other internal services, so it's fine to use custom keystore; there is no need for standard/default certificates which come with Java (you can find them for example in ``lib/security/cacerts``).
However, there are sometimes cases where it's required to be able to talk to both internal and external services. Unfortunately there’s no simple way to use multiple ``Keystore``s in Java.

### First Obvious but actually wrong step

As previously stated, ``SSLContext`` is the heart of JSSE architecture. In order to use custom ``TrustManager`` with custom ``SSLContext`` one has to do these steps:
```
var sslContext = SSLContext.getInstance("TLS");
sslContext.init(null, trustManagers, null);
```

``SSLContext`` algorithms can be found [here](https://docs.oracle.com/en/java/javase/15/docs/specs/security/standard-names.html#sslcontext-algorithms). ``SSLContext::init`` method's
signature:
```
public final void init(KeyManager[] km, TrustManager[] tm, SecureRandom random) throws KeyManagementException
```

First instinct is just to merge multiple ``TrustManager``s into an array and provide to ``SSLContext::init``. However, [documentation reveals](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/javax/net/ssl/SSLContext.html#init(javax.net.ssl.KeyManager%5B%5D,javax.net.ssl.TrustManager%5B%5D,java.security.SecureRandom)):
> Initializes this context. Either of the first two parameters may be null in which case the installed security providers will be searched for the highest priority implementation of the appropriate factory. Likewise, the secure random parameter may be null in which case the default implementation will be used.
>
> **Only the first instance of a particular key and/or trust manager implementation type in the array is used.** (For example, only the first javax.net.ssl.X509KeyManager in the array will be used.)

Or in Java's ``SSLContextImpl`` class:
```
private X509TrustManager chooseTrustManager(TrustManager[] tm) throws KeyManagementException {
    // We only use the first instance of X509TrustManager passed to us.
    for (int i = 0; tm != null && i < tm.length; i++) {
        if (tm[i] instanceof X509TrustManager) {
            if (tm[i] instanceof X509ExtendedTrustManager) {
                return (X509TrustManager)tm[i];
            } else {
                return new AbstractTrustManagerWrapper((X509TrustManager)tm[i]);
            }
        }
    }

    // nothing found, return a dummy X509TrustManager.
    return DummyX509TrustManager.INSTANCE;
}
```

In conclusion, to use two or more ``TrustManager``s it's required to have a custom ``X509TrustManager`` implementation.

### Solution with explanation

This is very simple, straightforward extension of standard Java's ``X509ExtendedTrustManager`` which pretty much just loops through two ``TrustManager``s and delegates.
Maybe the trickiest part is constructor, thus I'm going to go into more details:

```
var defaultTrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
defaultTrustManagerFactory.init((KeyStore) null); //If set to null - default KeyStore will be used
```

Creating a default ``TrustManagerFactory`` with default ``KeyStore`` via ``defaultTrustManagerFactory.init((KeyStore) null);``. According to [documentation](https://docs.oracle.com/en/java/javase/15/security/java-secure-socket-extension-jsse-reference-guide.html#GUID-7932AB21-2FED-402E-A806-3088402BAEA6):

> If a **null** ``KeyStore`` parameter is passed to the SunJSSE PKIX or SunX509 ``TrustManagerFactory``, then the factory uses the following process to try to find trust material:
>  * If the ``javax.net.ssl.trustStore`` property is defined, then the ``TrustManagerFactory`` attempts to find a file using the file name specified by that system property, and uses that file for the ``KeyStore`` parameter.
     > If the ``javax.net.ssl.trustStorePassword`` system property is also defined, then its value is used to check the integrity of the data in the truststore before opening it. If the javax.net.ssl.trustStore property is defined but the specified file does not exist, then a default ``TrustManager`` using an empty keystore is created.
>  * If the ``javax.net.ssl.trustStore`` system property was not specified, then:
     >     * if the file ``java-home/lib/security/jssecacerts`` exists, that file is used;
>     * if the file ``java-home/lib/security/cacerts`` exists, that file is used;
>     * if neither of these files exists, then the TLS cipher suite is anonymous, does not perform any authentication, and thus does not need a truststore.

As far as looping through ``TrustManager``s, currently (2021.01.05) there is only one type of ``TrustManager`` implementation - ``X509TrustManager``.
You can check it via ``System.out.println(defaultTrustManagerFactory.getTrustManagers().length);`` or looking at Java's documentation -
[JavaDocs TrustManager](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/javax/net/ssl/TrustManager.html),
[TrustManagerFactory Algorithms](https://docs.oracle.com/en/java/javase/15/docs/specs/security/standard-names.html#trustmanagerfactory-algorithms).

To sum up, firstly default ``TrustManagerFactory`` is created, with one ``TrustManager`` implementation. When mentioned single implementation is found, it's added to the list of ``trustManagers``.
The second ``TrustManager`` is provided via constructor.

```
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompositeX509ExtendedTrustManager extends X509ExtendedTrustManager {

    private static final String CERTIFICATE_EXCEPTION_MESSAGE = "None of the TrustManagers trust this certificate chain";

    private final List<X509ExtendedTrustManager> trustManagers = new ArrayList<>();
    private final X509Certificate[] acceptedIssuers;

    public CompositeX509ExtendedTrustManager(TrustManager otherTrustManager) {
        addDefaultTrustManager();
        addOtherTrustManager(otherTrustManager);

        acceptedIssuers = trustManagers.stream()
                .map(X509ExtendedTrustManager::getAcceptedIssuers)
                .flatMap(Arrays::stream)
                .distinct()
                .toArray(X509Certificate[]::new);
    }

    private void addDefaultTrustManager() {
        try {
            var defaultTrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            defaultTrustManagerFactory.init((KeyStore) null); //If set to null - default KeyStore will be used
            for (var tm : defaultTrustManagerFactory.getTrustManagers()) {
                if (tm instanceof X509ExtendedTrustManager) {
                    trustManagers.add((X509ExtendedTrustManager) tm);
                }
            }
        } catch (KeyStoreException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Couldn't initialize", e);
        }
    }

    private void addOtherTrustManager(TrustManager otherTrustManager) {
        if (otherTrustManager instanceof X509ExtendedTrustManager) {
            trustManagers.add((X509ExtendedTrustManager) otherTrustManager);
        } else {
            throw new RuntimeException("Couldn't initialize");
        }
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {
        checkTrusted(trustManager -> trustManager.checkClientTrusted(chain, authType, socket));
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {
        checkTrusted(trustManager -> trustManager.checkServerTrusted(chain, authType, socket));
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine) throws CertificateException {
        checkTrusted(trustManager -> trustManager.checkClientTrusted(chain, authType, engine));
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine) throws CertificateException {
        checkTrusted(trustManager -> trustManager.checkServerTrusted(chain, authType, engine));
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        checkTrusted(trustManager -> trustManager.checkClientTrusted(chain, authType));
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        checkTrusted(trustManager -> trustManager.checkServerTrusted(chain, authType));
    }

    public void checkTrusted(TrustManagerCallBackConsumer callBackConsumer) throws CertificateException {
        var certificateExceptions = new ArrayList<CertificateException>();

        for (var trustManager : trustManagers) {
            try {
                callBackConsumer.checkTrusted(trustManager);
                return;
            } catch (CertificateException e) {
                certificateExceptions.add(e);
            }
        }

        CertificateException certificateException = new CertificateException(CERTIFICATE_EXCEPTION_MESSAGE);
        certificateExceptions.forEach(certificateException::addSuppressed);

        throw certificateException;
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return Arrays.copyOf(acceptedIssuers, acceptedIssuers.length);
    }

    private interface TrustManagerCallBackConsumer {
        void checkTrusted(X509ExtendedTrustManager trustManager) throws CertificateException;
    }
}
```

# Misc

```
-Djavax.net.debug=ssl,handshake
```

## Resources

* https://docs.oracle.com/en/java/javase/15/security/java-secure-socket-extension-jsse-reference-guide.html#GUID-A6B7B05A-3696-4F86-A05C-9500EEC91C2D
* https://docs.oracle.com/en/java/javase/15/docs/specs/security/standard-names.html
* http://codyaray.com/2013/04/java-ssl-with-multiple-keystores
* https://stackoverflow.com/questions/1793979/registering-multiple-keystores-in-jvm
* https://stackoverflow.com/questions/23144353/how-do-i-initialize-a-trustmanagerfactory-with-multiple-sources-of-trust
* https://github.com/Hakky54/sslcontext-kickstart
* https://github.com/Hakky54/sslcontext-kickstart/blob/master/sslcontext-kickstart/src/main/java/nl/altindag/ssl/trustmanager/CompositeX509ExtendedTrustManager.java
* https://github.com/Hakky54/mutual-tls-ssl
* https://dzone.com/articles/hakky54mutual-tls-1
* https://www.baeldung.com/x-509-authentication-in-spring-security
* https://docs.oracle.com/en/java/javase/11/docs/api/jdk.httpserver/com/sun/net/httpserver/package-summary.html















