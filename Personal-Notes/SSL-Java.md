# Java TLS and self-signed certificates guide

There are 7 possible scenarios:

* When your application is acting as a server within Organization perimeter. Your application has to provide self-signed certificate to the client.
* When your application is acting as a server for an outside Organization application (3rd party). Your application has to provide self-signed certificate for the client. 
* When your application is client of other application within Organization perimeter. Your application has to validate self-signed certificate from the server.
* When your application is client of other application outside Organization perimeter (3rd party). Your application has to validate CA certificate from the server.
* When your application is client of other application within or outside Organization perimeter. Your application chooses to trust all certificates (only for local development/prototyping)
* Mutual TLS. When both client and server exchange certificates within Organization perimeter (both exchange self-signed certificates).
* Mutual TLS. When both client and server exchange certificates outside Organization perimeter (one party exchanges self-signed, other CA).

## Java Implementations

### Little Bit of Theory

![img_1.png](img_1.png)

Description of Figure follows:

The heart of the JSSE architecture is the **SSLContext**. The context eventually creates end objects (**SSLSocket** and **SSLEngine**) which actually implement the SSL/TLS protocol. 
SSLContexts are initialized with two callback classes, **KeyManager** and **TrustManager**, which allow applications to first select authentication material to send and second to verify credentials sent by a peer.

A **KeyManager** is responsible for choosing which credentials to present to a peer.
A **TrustManager** is responsible for verifying the credentials received from a peer.

### When your application is acting as a server within Organization perimeter
TODO
### When your application is acting as a server for an outside Organization application (3rd party)
TODO
### When your application is client of other application within Organization perimeter
TODO
### When your application is client of other application outside Organization perimeter (3rd party)
TODO
### When your application is client of other application within or outside Organization perimeter (trust everybody).
TODO
### Mutual TLS within Organization perimeter
TODO
### Mutual TLS outside Organization perimeter
TODO


## Misc Java Code Snippets


### Count trusted certificates
```
private static int countTrustedCertificates(TrustManagerFactory defaultTrust) {
    // Get hold of the default trust manager
    X509TrustManager defaultTm = null;
    for (var tm : defaultTrust.getTrustManagers()) {
        if (tm instanceof X509TrustManager) {
            defaultTm = (X509TrustManager) tm;
            break;
        }
    }
    var acceptedIssuers = defaultTm.getAcceptedIssuers();
    return acceptedIssuers.length;
}
```

### Custom ``CompositeX509ExtendedTrustManager ``

#### Why it's required?

For communication between internal services, some Organizations use self-signed certificates on both client and server. Simple and cheap (free!). Most of the time, these services only communicate over 
HTTPS with other internal services, so it's fine to use custom keystore; there is no need for standard/default certificates which come with Java (you can find them for example in ``lib/security/cacerts``).
However, there are sometimes cases where it's required to be able to talk to both internal and external services. Unfortunately there’s no simple way to use multiple ``Keystore``s in Java.

#### First Obvious but actually wrong step

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

Or in ``SSLContextImpl`` class:
```
    private X509TrustManager chooseTrustManager(TrustManager[] tm)
            throws KeyManagementException {
        // We only use the first instance of X509TrustManager passed to us.
        for (int i = 0; tm != null && i < tm.length; i++) {
            if (tm[i] instanceof X509TrustManager) {
                if (tm[i] instanceof X509ExtendedTrustManager) {
                    return (X509TrustManager)tm[i];
                } else {
                    return new AbstractTrustManagerWrapper(
                                        (X509TrustManager)tm[i]);
                }
            }
        }

        // nothing found, return a dummy X509TrustManager.
        return DummyX509TrustManager.INSTANCE;
    }
```

In conclusion, to use two or more ``TrustManager``s it's required to have a custom ``X509TrustManager`` implementation.


#### Solution with detail explanation

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
            e.printStackTrace();
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

```
-Djavax.net.debug=ssl,handshake
```
