```
private static TrustManagerFactory initTrustManagerFactory() throws NoSuchAlgorithmException, KeyStoreException {
    var defaultTrust = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    defaultTrust.init((KeyStore) null); //Setting to Null actually does not setting anything to null, but pushes to use default KeyStore
    return defaultTrust;
}
 
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

```
-Djavax.net.debug=ssl,handshake
```
