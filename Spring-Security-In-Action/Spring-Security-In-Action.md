# Table of Content

- [Chapter 1. Security today](#chapter-1-security-today)
  * [Common security vulnerabilities in web applications](#common-security-vulnerabilities-in-web-applications)
    + [Broken authentication](#broken-authentication)
    + [What is session fixation?](#what-is-session-fixation)
    + [What is cross-site scripting (XSS)?](#what-is-cross-site-scripting-xss)
    + [What is cross-site request forgery (CSRF)?](#what-is-cross-site-request-forgery-csrf)
    + [Understanding injection vulnerabilities in web applications](#understanding-injection-vulnerabilities-in-web-applications)
    + [Dealing with the exposure of sensitive data](#dealing-with-the-exposure-of-sensitive-data)
    + [What is the lack of method access control?](#what-is-the-lack-of-method-access-control)
    + [Using dependencies with known vulnerabilities](#using-dependencies-with-known-vulnerabilities)
  * [Security applied in various architectures](#security-applied-in-various-architectures)
    + [Designing a one-piece web application](#designing-a-one-piece-web-application)
    + [Designing security for a backend/frontend separation](#designing-security-for-a-backendfrontend-separation)
    + [Understanding the OAuth 2 flow](#understanding-the-oauth-2-flow)
    + [Using API keys, cryptographic signatures, and IP validation to secure requests](#using-api-keys-cryptographic-signatures-and-ip-validation-to-secure-requests)
- [Chapter 2. Hello Spring Security](#chapter-2-hello-spring-security)
  * [Starting with the first project](#starting-with-the-first-project)
  * [Logging Spring Security](#logging-spring-security)
  * [Which are the default configurations?](#which-are-the-default-configurations)
  * [Overriding default configurations](#overriding-default-configurations)
    + [Overriding the UserDetailsService component](#overriding-the-userdetailsservice-component)
    + [Overriding the endpoint authorization configuration](#overriding-the-endpoint-authorization-configuration)
    + [Setting the configuration in different ways](#setting-the-configuration-in-different-ways)
    + [Overriding the AuthenticationProvider implementation](#overriding-the-authenticationprovider-implementation)
    + [Using multiple configuration classes in your project](#using-multiple-configuration-classes-in-your-project)
- [Chapter 3. Managing users](#chapter-3-managing-users)
  * [Implementing authentication in Spring Security](#implementing-authentication-in-spring-security)
  * [Describing the user](#describing-the-user)
    + [Demystifying the definition of the UserDetails contract](#demystifying-the-definition-of-the-userdetails-contract)
    + [Detailing on the GrantedAuthority contract](#detailing-on-the-grantedauthority-contract)
    + [Writing a minimal implementation of UserDetails](#writing-a-minimal-implementation-of-userdetails)
    + [Using a builder to create instances of the UserDetails type](#using-a-builder-to-create-instances-of-the-userdetails-type)
    + [Combining multiple responsibilities related to the user](#combining-multiple-responsibilities-related-to-the-user)
  * [Instructing Spring Security on how to manage users](#instructing-spring-security-on-how-to-manage-users)
    + [Understanding the UserDetailsService contract](#understanding-the-userdetailsservice-contract)
    + [Implementing the UserDetailsService contract](#implementing-the-userdetailsservice-contract)
    + [Implementing the UserDetailsManager contract](#implementing-the-userdetailsmanager-contract)
      - [USING A JDBCUSERDETAILSMANAGER FOR USER MANAGEMENT](#using-a-jdbcuserdetailsmanager-for-user-management)
      - [USING AN LDAPUSERDETAILSMANAGER FOR USER MANAGEMENT](#using-an-ldapuserdetailsmanager-for-user-management)
- [Chapter 4. Dealing with passwords](#chapter-4-dealing-with-passwords)
  * [Understanding the PasswordEncoder contract](#understanding-the-passwordencoder-contract)
    + [The definition of the PasswordEncoder contract](#the-definition-of-the-passwordencoder-contract)
    + [Implementing the PasswordEncoder contract](#implementing-the-passwordencoder-contract)
    + [Choosing from the provided implementations of PasswordEncoder](#choosing-from-the-provided-implementations-of-passwordencoder)
    + [Multiple encoding strategies with DelegatingPasswordEncoder](#multiple-encoding-strategies-with-delegatingpasswordencoder)
  * [More about the Spring Security Crypto module](#more-about-the-spring-security-crypto-module)
    + [Using key generators](#using-key-generators)
    + [Using encryptors for encryption and decryption operations](#using-encryptors-for-encryption-and-decryption-operations)
- [Chapter 5. Implementing authentication](#chapter-5-implementing-authentication)
  * [Understanding the AuthenticationProvider](#understanding-the-authenticationprovider)
    + [Representing the request during authentication](#representing-the-request-during-authentication)
    + [Implementing custom authentication logic](#implementing-custom-authentication-logic)
    + [Applying custom authentication logic](#applying-custom-authentication-logic)
  * [Using the SecurityContext](#using-the-securitycontext)
    + [Using a holding strategy for the security context](#using-a-holding-strategy-for-the-security-context)
    + [Using a holding strategy for asynchronous calls](#using-a-holding-strategy-for-asynchronous-calls)
    + [Using a holding strategy for standalone applications](#using-a-holding-strategy-for-standalone-applications)
    + [Forwarding the security context with DelegatingSecurityContextRunnable](#forwarding-the-security-context-with-delegatingsecuritycontextrunnable)
    + [Forwarding the security context with DelegatingSecurityContextExecutorService](#forwarding-the-security-context-with-delegatingsecuritycontextexecutorservice)
  * [Understanding HTTP Basic and form-based login authentications](#understanding-http-basic-and-form-based-login-authentications)
    + [Using and configuring HTTP Basic](#using-and-configuring-http-basic)
    + [Implementing authentication with form-based login](#implementing-authentication-with-form-based-login)
- [Chapter 6. Hands-on: A small secured web application](#chapter-6-hands-on-a-small-secured-web-application)
  * [Project requirements and setup](#project-requirements-and-setup)
  * [Implementing user management](#implementing-user-management)
  * [Implementing custom authentication logic](#implementing-custom-authentication-logic)
  * [My own implementation using Jdbc and DelegatingPasswordEncoder](#my-own-implementation-using-jdbc-and-delegatingpasswordencoder)
- [Chapter 7. Configuring authorization: Restricting access](#chapter-7-configuring-authorization-restricting-access)
  * [Restricting access based on authorities and roles](#restricting-access-based-on-authorities-and-roles)
    + [Restricting access for all endpoints based on user authorities](#restricting-access-for-all-endpoints-based-on-user-authorities)
    + [Restricting access for all endpoints based on user roles](#restricting-access-for-all-endpoints-based-on-user-roles)
    + [Restricting access to all endpoints](#restricting-access-to-all-endpoints)
- [Chapter 8. Configuring authorization: Applying restrictions](#chapter-8-configuring-authorization-applying-restrictions)
  * [Using matcher methods to select endpoints](#using-matcher-methods-to-select-endpoints)
  * [Selecting requests for authorization using MVC matchers](#selecting-requests-for-authorization-using-mvc-matchers)
  * [Selecting requests for authorization using Ant matchers](#selecting-requests-for-authorization-using-ant-matchers)
  * [Selecting requests for authorization using regex matchers](#selecting-requests-for-authorization-using-regex-matchers)
- [Chapter 9. Implementing filters](#chapter-9-implementing-filters)
  * [Implementing filters in the Spring Security architecture](#implementing-filters-in-the-spring-security-architecture)
  * [Adding a filter before an existing one in the chain](#adding-a-filter-before-an-existing-one-in-the-chain)
  * [Adding a filter after an existing one in the chain](#adding-a-filter-after-an-existing-one-in-the-chain)
  * [Adding a filter at the location of another in the chain](#adding-a-filter-at-the-location-of-another-in-the-chain)
  * [Filter implementations provided by Spring Security](#filter-implementations-provided-by-spring-security)
- [Chapter 10. Applying CSRF protection and CORS](#chapter-10-applying-csrf-protection-and-cors)
  * [Applying cross-site request forgery (CSRF) protection in applications](#applying-cross-site-request-forgery-csrf-protection-in-applications)
    + [How CSRF protection works in Spring Security](#how-csrf-protection-works-in-spring-security)
    + [Using CSRF protection in practical scenarios](#using-csrf-protection-in-practical-scenarios)
    + [Customizing CSRF protection](#customizing-csrf-protection)
  * [Using cross-origin resource sharing](#using-cross-origin-resource-sharing)
    + [How does CORS work?](#how-does-cors-work)
    + [Applying CORS policies with the @CrossOrigin annotation](#applying-cors-policies-with-the-crossorigin-annotation)
    + [Applying CORS using a CorsConfigurer](#applying-cors-using-a-corsconfigurer)
- [Chapter 11. Hands-on: A separation of responsibilities](#chapter-11-hands-on-a-separation-of-responsibilities)
  * [The scenario and requirements of the example](#the-scenario-and-requirements-of-the-example)
  * [Implementing and using tokens](#implementing-and-using-tokens)
    + [What is a token?](#what-is-a-token)
    + [What is a JSON Web Token?](#what-is-a-json-web-token)
  * [Implementing the authentication server](#implementing-the-authentication-server)
  * [Implementing the business logic server](#implementing-the-business-logic-server)
    + [Implementing the Authentication objects](#implementing-the-authentication-objects)
    + [Implementing the proxy to the authentication server](#implementing-the-proxy-to-the-authentication-server)
    + [Implementing the AuthenticationProvider interface](#implementing-the-authenticationprovider-interface)
    + [Implementing the filters](#implementing-the-filters)
    + [Writing the security configurations](#writing-the-security-configurations)
    + [Testing the whole system](#testing-the-whole-system)
- [Chapter 12. How does OAuth 2 work?](#chapter-12-how-does-oauth-2-work)
  * [The OAuth 2 framework](#the-oauth-2-framework)
  * [The components of the OAuth 2 authentication architecture](#the-components-of-the-oauth-2-authentication-architecture)
  * [Implementation choices with OAuth 2](#implementation-choices-with-oauth-2)
    + [Implementing the authorization code grant type](#implementing-the-authorization-code-grant-type)
      - [STEP 1: MAKING THE AUTHENTICATION REQUEST WITH THE AUTHORIZATION CODE GRANT TYPE](#step-1-making-the-authentication-request-with-the-authorization-code-grant-type)
      - [STEP 2: OBTAINING AN ACCESS TOKEN WITH THE AUTHORIZATION CODE GRANT TYPE](#step-2-obtaining-an-access-token-with-the-authorization-code-grant-type)
      - [STEP 3: CALLING THE PROTECTED RESOURCE WITH THE AUTHORIZATION CODE GRANT TYPE](#step-3-calling-the-protected-resource-with-the-authorization-code-grant-type)
    + [Implementing the password grant type](#implementing-the-password-grant-type)
      - [STEP 1: REQUESTING AN ACCESS TOKEN WHEN USING THE PASSWORD GRANT TYPE](#step-1-requesting-an-access-token-when-using-the-password-grant-type)
      - [STEP 2: USING AN ACCESS TOKEN TO CALL RESOURCES WHEN USING THE PASSWORD GRANT TYPE](#step-2-using-an-access-token-to-call-resources-when-using-the-password-grant-type)
    + [Implementing the client credentials grant type](#implementing-the-client-credentials-grant-type)
      - [STEP 1: REQUESTING AN ACCESS TOKEN WITH THE CLIENT CREDENTIAL GRANT TYPE](#step-1-requesting-an-access-token-with-the-client-credential-grant-type)
      - [STEP 2: USING AN ACCESS TOKEN TO CALL RESOURCES WITH THE CLIENT CREDENTIAL GRANT TYPE](#step-2-using-an-access-token-to-call-resources-with-the-client-credential-grant-type)
    + [Using refresh tokens to obtain new access tokens](#using-refresh-tokens-to-obtain-new-access-tokens)
  * [The sins of OAuth 2](#the-sins-of-oauth-2)
  * [Implementing a simple single sign-on application](#implementing-a-simple-single-sign-on-application)
    + [Managing the authorization server](#managing-the-authorization-server)
    + [Starting the implementation](#starting-the-implementation)
    + [Implementing ClientRegistration](#implementing-clientregistration)
    + [Implementing ClientRegistrationRepository](#implementing-clientregistrationrepository)
    + [The pure magic of Spring Boot configuration](#the-pure-magic-of-spring-boot-configuration)
    + [Obtaining details about an authenticated user](#obtaining-details-about-an-authenticated-user)
    + [Testing the application](#testing-the-application)
- [Chapter 13. OAuth 2: Implementing the authorization server](#chapter-13-oauth-2-implementing-the-authorization-server)
  * [Writing your own authorization server implementation](#writing-your-own-authorization-server-implementation)
  * [Defining user management](#defining-user-management)
  * [Registering clients with the authorization server](#registering-clients-with-the-authorization-server)
  * [Using the password grant type](#using-the-password-grant-type)
  * [Using the authorization code grant type](#using-the-authorization-code-grant-type)
  * [Using the client credentials grant type](#using-the-client-credentials-grant-type)
  * [Using the refresh token grant type](#using-the-refresh-token-grant-type)
- [Chapter 14. OAuth 2: Implementing the resource server](#chapter-14-oauth-2-implementing-the-resource-server)
  * [Implementing a resource server](#implementing-a-resource-server)
  * [Checking the token remotely](#checking-the-token-remotely)
      - [Using token introspection without Spring Security OAuth](#using-token-introspection-without-spring-security-oauth)
  * [Implementing blackboarding with a JdbcTokenStore](#implementing-blackboarding-with-a-jdbctokenstore)
  * [A short comparison of approaches](#a-short-comparison-of-approaches)
- [Chapter 15. OAuth 2: Using JWT and cryptographic signatures](#chapter-15-oauth-2-using-jwt-and-cryptographic-signatures)
  * [Using tokens signed with symmetric keys with JWT](#using-tokens-signed-with-symmetric-keys-with-jwt)
    + [Using JWTs](#using-jwts)
    + [Implementing an authorization server to issue JWTs](#implementing-an-authorization-server-to-issue-jwts)
    + [Implementing a resource server that uses JWT](#implementing-a-resource-server-that-uses-jwt)
    + [Using symmetric keys without the Spring Security OAuth project](#using-symmetric-keys-without-the-spring-security-oauth-project)
  * [Using tokens signed with asymmetric keys with JWT](#using-tokens-signed-with-asymmetric-keys-with-jwt)
    + [Generating the key pair](#generating-the-key-pair)
      - [GENERATING A PRIVATE KEY](#generating-a-private-key)
      - [OBTAINING THE PUBLIC KEY](#obtaining-the-public-key)
    + [Implementing an authorization server that uses private keys](#implementing-an-authorization-server-that-uses-private-keys)
    + [Implementing a resource server that uses public keys](#implementing-a-resource-server-that-uses-public-keys)
    + [Using asymmetric keys without the Spring Security OAuth project](#using-asymmetric-keys-without-the-spring-security-oauth-project)
    + [Using an endpoint to expose the public key](#using-an-endpoint-to-expose-the-public-key)
  * [Adding custom details to the JWT](#adding-custom-details-to-the-jwt)
    + [Configuring the authorization server to add custom details to tokens](#configuring-the-authorization-server-to-add-custom-details-to-tokens)
    + [Configuring the resource server to read the custom details of a JWT](#configuring-the-resource-server-to-read-the-custom-details-of-a-jwt)
- [Chapter 16. Global method security: Pre- and postauthorizations](#chapter-16-global-method-security-pre--and-postauthorizations)
  * [Enabling global method security](#enabling-global-method-security)
    + [Understanding call authorization](#understanding-call-authorization)
      - [USING PREAUTHORIZATION TO SECURE ACCESS TO METHODS](#using-preauthorization-to-secure-access-to-methods)
      - [USING POSTAUTHORIZATION TO SECURE A METHOD CALL](#using-postauthorization-to-secure-a-method-call)
    + [Enabling global method security in your project](#enabling-global-method-security-in-your-project)
  * [Applying preauthorization for authorities and roles](#applying-preauthorization-for-authorities-and-roles)
  * [Applying postauthorization](#applying-postauthorization)
  * [Implementing permissions for methods](#implementing-permissions-for-methods)
  * [Using the @Secured and @RolesAllowed annotations](#using-the-secured-and-rolesallowed-annotations)
- [Chapter 17. Global method security: Pre- and postfiltering](#chapter-17-global-method-security-pre--and-postfiltering)
  * [Applying prefiltering for method authorization](#applying-prefiltering-for-method-authorization)
  * [Applying postfiltering for method authorization](#applying-postfiltering-for-method-authorization)
  * [Using filtering in Spring Data repositories](#using-filtering-in-spring-data-repositories)
- [Chapter 18. Hands-on: An OAuth 2 application](#chapter-18-hands-on-an-oauth-2-application)
  * [The application scenario](#the-application-scenario)
  * [Configuring Keycloak as an authorization server](#configuring-keycloak-as-an-authorization-server)
    + [Registering a client for our system](#registering-a-client-for-our-system)
    + [Specifying client scopes](#specifying-client-scopes)
    + [Adding users and obtaining access tokens](#adding-users-and-obtaining-access-tokens)
    + [Defining the user roles](#defining-the-user-roles)
  * [Implementing the resource server](#implementing-the-resource-server)
  * [Testing the application](#testing-the-application)
    + [Proving an authenticated user can only add a record for themself](#proving-an-authenticated-user-can-only-add-a-record-for-themself)
    + [Proving that a user can only retrieve their own records](#proving-that-a-user-can-only-retrieve-their-own-records)
    + [Proving that only admins can delete records](#proving-that-only-admins-can-delete-records)
- [Chapter 19. Spring Security for reactive apps](#chapter-19-spring-security-for-reactive-apps)
- [Chapter 20. Spring Security testing](#chapter-20-spring-security-testing)
  * [Using mock users for tests](#using-mock-users-for-tests)
  * [Testing with users from a UserDetailsService](#testing-with-users-from-a-userdetailsservice)
  * [Using custom Authentication objects for testing](#using-custom-authentication-objects-for-testing)
    + [STEP 1: DEFINING A CUSTOM ANNOTATION](#step-1-defining-a-custom-annotation)
    + [STEP 2: CREATING A FACTORY CLASS FOR THE MOCK SECURITYCONTEXT](#step-2-creating-a-factory-class-for-the-mock-securitycontext)
    + [STEP 3: LINKING THE CUSTOM ANNOTATION TO THE FACTORY CLASS](#step-3-linking-the-custom-annotation-to-the-factory-class)
  * [Testing method security](#testing-method-security)
  * [Testing authentication](#testing-authentication)
  * [Testing CSRF configurations](#testing-csrf-configurations)
  * [Testing CORS configurations](#testing-cors-configurations)
  * [Testing reactive Spring Security implementations](#testing-reactive-spring-security-implementations)

# Chapter 1. Security today

## Common security vulnerabilities in web applications

An excellent start to understanding vulnerabilities is being aware of the Open Web Application Security Project, also known as OWASP (https://www.owasp.org). Among the common vulnerabilities that you should be aware of, you’ll find these:
* Broken authentication
* Session fixation
* Cross-site scripting (XSS)
* Cross-site request forgery (CSRF)
* Injections
* Sensitive data exposure
* Lack of method access control
* Using dependencies with known vulnerabilities

### Broken authentication

We can say that we have a broken authorization if a an individual with bad intentions somehow gains access to functionality or data that doesn’t belong to them. An authenticated user can access the /products/{name} endpoint. From the browser, a web app calls this endpoint to retrieve and display the user’s products from a database. But what happens if the app doesn’t validate to whom the products belong when returning these? Some user could find a way to get the details of another user.

![broken-authentication.PNG](pictures/broken-authentication.PNG)

### What is session fixation?

Session fixation vulnerability is a more specific, high-severity weakness of a web application. If present, it permits an attacker to impersonate a valid user by reusing a previously generated session ID. This vulnerability can happen if, during the authentication process, the web application does not assign a unique session ID. This can potentially lead to the reuse of existing session IDs. Exploiting this vulnerability consists of obtaining a valid session ID and making the intended victim’s browser use it.
Depending on how you implement your web application, there are various ways an individual can use this vulnerability. For example, if the application provides the session ID in the URL, then the victim could be tricked into clicking on a malicious link. If the application uses a hidden attribute, the attacker can fool the victim into using a foreign form and then post the action to the server. If the application stores the value of the session in a cookie, then the attacker can inject a script and force the victim’s browser to execute it.

### What is cross-site scripting (XSS)?

Cross-site scripting, also referred to as XSS, allows the injection of client-side scripts into web services exposed by the server, thereby permitting other users to run these. Before being used or even stored, you should properly “sanitize” the request to avoid undesired executions of foreign scripts.

Let’s take an example. A user posts a message or a comment in a web application. After posting the message, the site displays it so that everybody visiting the page can see it.

![cross-site-scripting.PNG](pictures/cross-site-scripting.PNG)

### What is cross-site request forgery (CSRF)?

Cross-site request forgery (CSRF) vulnerabilities are also common in web applications. CSRF attacks assume that a URL that calls an action on a specific server can be extracted and reused from outside the application (figure 1.8). If the server trusts the execution without doing any check on the origin of the request, one could execute it from any other place.
One of the ways of mitigating this vulnerability is to use tokens to identify the request or use cross-origin resource sharing (CORS) limitations. In other words, validate the origin of the request.

### Understanding injection vulnerabilities in web applications

In an injection attack, the attacker employing a vulnerability introduces specific data into the system. There are many types of injection attacks. Even the XSS that we mentioned in section 1.4.3 can be considered an injection vulnerability. In the end, injection attacks inject a client-side script with the means of harming the system somehow. Other examples could be SQL injection, XPath injection, OS command injection, LDAP injection, and the list continues.

### Dealing with the exposure of sensitive data

we’ll deal with credentials and private keys in the examples in this book. We might use secrets in configuration files, but we’ll place a note for these cases to remind you that **you should store sensitive data in vaults.** By setting such values in the configuration files, such as the application.properties or application .yml files in a Spring Boot project, you make those private values accessible to anyone who can see the source code.

Be careful of what your server returns to the client, especially, but not limited to, cases where the application encounters exceptions. Having exception stacks in the response is not a good choice either.

### What is the lack of method access control?

Even at the application level, you don’t apply authorization to only one of the tiers. Say you have a web application with a straightforward design. The app has a controller exposing endpoints. The controller directly calls a service that implements some logic and that uses persisted data managed through a repository. Imagine a situation where the authorization is done only at the endpoint level.

In this case, some future implementation could expose that use case without testing or without testing all the authorization requirements.

![lack-of-methods-access-controls.PNG](pictures/lack-of-methods-access-controls.PNG)

### Using dependencies with known vulnerabilities

Although not necessarily directly related to Spring Security, but still an essential aspect of the application-level security, the dependencies we use need attention. Fortunately, we have multiple possibilities for static analyses, quickly done by adding a plugin to your Maven or Gradle configuration.

## Security applied in various architectures

Architecture strongly influences choices in configuring Spring Security for your applications; so do functional and nonfunctional requirements. When you think of a tangible situation, to protect something, depending on what you want to protect, you use a metal door, bulletproof glass, or a barrier. You couldn’t just use a metal door in all the situations. If what you protect is an expensive painting in a museum, you still want people to be able to see it. You don’t, however, want them to be able to touch it, damage it, or even take it with them. In this case, functional requirements affect the solution we take for secure systems.

### Designing a one-piece web application

Let’s start with the case where you develop a component of a system that represents a web application. In this application, **there’s no direct separation in development between the backend and the frontend.** As long as you have a session, you need to take into consideration the session fixation vulnerability as well as the CSRF possibilities previously mentioned. You must also consider what you store in the HTTP session itself.

Server-side sessions are quasi-persistent. They are stateful pieces of data, so their lifetime is longer. The longer these stay in memory, the more it’s statistically probable that they’ll be accessed. For example, a person having access to the heap dump could read the information in the app’s internal memory. And don’t think that the heap dump is challenging to obtain! Especially when developing your applications with Spring Boot, you might find that the Actuator is also part of your application. The Spring Boot **Actuator** is a great tool. Depending on how you configure it, it **can return a heap dump with only an endpoint call.**

Going back to the vulnerabilities in terms of CSRF in this case, the easiest way to mitigate the vulnerability is to use anti-CSRF tokens. Fortunately, with Spring Security, this capability is available out of the box. CSRF protection as well as validation of the origin CORS is enabled by default. For authentication and authorization, you could choose to use the implicit login form configuration from Spring Security. You also benefit from mitigation of the session fixation vulnerability.

### Designing security for a backend/frontend separation

In these web applications, developers use a framework like Angular, ReactJS, or Vue.js to develop the frontend. The frontend communicates with the backend through REST endpoints. We’ll typically avoid using server-side sessions; client-side sessions replace those. This kind of system design is similar to the one used in mobile applications.

First, **CSRF and CORS configurations are usually more complicated.** You might want to scale the system horizontally, but it’s not mandatory to have the frontend with the backend at the same origin. For mobile applications, we can’t even talk about an origin.

**The most straightforward but least desirable approach as a practical solution is to use HTTP Basic for endpoint authentication.** While this approach is direct to understand and generally used with the first theoretical examples of authentication, it does have leaks that you want to avoid. For example, using HTTP Basic implies sending the credentials with each call. Credentials aren’t encrypted. The browser sends the username and the passwords as a Base64 encoding.

Alternative for authentication and authorization that offers a better approach, the **OAuth 2** flow.

### Understanding the OAuth 2 flow

We certainly want to find a solution to avoid resending credentials for each of the requests to the backend and store these on the client side. The OAuth 2 flow offers a better way to implement authentication and authorization in these cases.

The OAuth 2 framework defines two separate entities: the *authorization server* and the *resource server*. The purpose of the authorization server is to authorize the user and provide them with a token that specifies, among other things, a set of privileges that they can use. The part of the backend implementing this functionality is called the resource server. The endpoints that can be called are considered *protected resources*. Based on the obtained token, and after accomplishing authorization, a call on a resource is permitted or rejected.

1. The user accesses a use case in the application (also known as the client). The application needs to call a resource in the backend.
   2 To be able to call the resource, the application first has to obtain an access token, so it calls the authorization server to get the token. In the request, it sends the user credentials or a **refresh token**, in some cases.
   3 If the credentials or the **refresh token** are correct, the authorization server returns a (new) access token to the client.
   4 The header of the request to the resource server uses the access token when calling the needed resources.

![oauth2-flow.PNG](pictures/oauth2-flow.PNG)

A token is like an access card you use inside an office building. As a visitor, you first visit the front desk, where you receive an access card after identifying yourself. The access card can open some of the doors, but not necessarily all. Based on your identity, you can access precisely the doors that you’re allowed to and no more. The same happens with an access token. After authentication, the caller is provided with a token, and based on that, they can access the resources for which they have privileges.

A token has a fixed lifetime, usually being short-lived. When a token expires, the app needs to obtain a new one. If needed, the server can disqualify the token earlier than its expiration time. The following lists some of the advantages of this flow:
* The client doesn’t have to store the user credentials. The access token and, eventually, the refresh token are the only access details you need to save.
* The application doesn’t expose the user credentials, which are often on the network.
* If someone intercepts a token, you can disqualify the token without needing to invalidate the user credentials.
* A token can be used by a third entity to access resources on the user’s behalf, without having to impersonate the user. Of course, an attacker can steal the token in this case. But because the token usually has a limited lifespan, the timeframe in which one can use this vulnerability is limited.

### Using API keys, cryptographic signatures, and IP validation to secure requests

In some cases, you don’t need a username and a password to authenticate and authorize a caller, but you still want to make sure that nobody altered the exchanged messages. You might need this approach when requests are made between two backend components. A few practices include:
* Using static keys in request and response headers;
* Signing requests and responses with cryptographic signatures;
* Applying validation for IP addresses.

The use of static keys is the weakest approach. In the headers of the request and the response, we use a key. Requests and responses aren’t accepted if the header value is incorrect. Of course, this assumes that we often exchange the value of the key in the network; if the traffic goes outside the data center, it would be easy to intercept. Someone who gets the value of the key could replay the call on the endpoint. When we use this approach, it’s usually done together with IP address whitelisting.

A better approach to test the authenticity of communication is the use of cryptographic signatures. With this approach, a key is used to sign the request and the response. You don’t need to send the key on the wire, which is an advantage over static authorization values. The parties can use their key to validate the signature.

# Chapter 2. Hello Spring Security

## Starting with the first project

The only dependencies you need to write for our first project are **spring-boot-starter-web** and **spring-boot-starter-security**. Add controller:
```
@RestController
public class HelloController {
    
    @GetMapping("hello")
    public String hello() {
        return "hello";
    }
}
```

Once you run the application, besides the other lines in the console, you should see something that looks similar to this:
```
Using generated security password: 93a01cf0-794b-4b98-86ef-54860f36f7f3
```

Each time you run the application, it generates a new password and prints this password in the console as presented in the previous code snippet. You must use this password to call any of the application’s endpoints with HTTP Basic authentication. First, let’s try to call the endpoint without using the Authorization header:
```
curl http://localhost:8080/hello
```

And the response to the call:
```
{
  "status":401,
  "error":"Unauthorized",
  "message":"Unauthorized",
  "path":"/hello"
}
```

Let’s try it again but now with the proper credentials:
```
curl -u user:2b780da1-f0ec-452a-8c13-be70987c80d2 http://localhost:8080/hello
```

The response to the call now is:
```
Hello!
```

With cURL, you can set the HTTP basic username and password with the -u flag. Behind the scenes, cURL encodes the string <username>:<password> in Base64
and sends it as the value of the Authorization header prefixed with the string Basic. This call should generate the same result as the one using the ``-u`` option:

```
curl -H "Authorization: Basic dXNlcjo5M2EwMWNmMC03OTRiLTRiOTgtODZlZi01NDg2MGYzNmY3ZjM=" localhost:8080/hello
```

## Logging Spring Security

In ``application.yaml``:
```
logging:
  level:
    org:
      springframework:
        security: DEBUG
```

## Which are the default configurations?

In this section, we discuss the main actors in the overall architecture that take part in the process of authentication and authorization. In figure 2.2, you can see the big picture of the main actors in the Spring Security architecture and the relationships among these.

![main-actors-of-spring-security.PNG](pictures/main-actors-of-spring-security.PNG)

Let’s start with the way you provide the needed credentials for authentication.
**An object that implements a ``UserDetailsService`` contract with Spring Security manages the details about users.** Until now, we used the default implementation provided by Spring Boot. This implementation only registers the default credentials in the internal memory of the application. These default credentials are “user” with a default password that’s a universally unique identifier (UUID). This password is randomly generated when the Spring context is loaded.

And then we have the PasswordEncoder. The PasswordEncoder does two things:
* Encodes a password
* Verifies if the password matches an existing encoding
  Even if it’s not as obvious as the ``UserDetailsService`` object, the ``PasswordEncoder`` is mandatory for the Basic authentication flow. For now, you should be **aware that a ``PasswordEncoder`` exists together with the default ``UserDetailsService``. When we replace the default implementation of the ``UserDetailsService``, we must also specify a ``PasswordEncoder``.**


The ``AuthenticationProvider`` defines the authentication logic, delegating the user and password management. A default implementation of the ``AuthenticationProvider`` uses the default implementations provided for the ``UserDetailsService`` and the ``PasswordEncoder``.

## Overriding default configurations

You need to understand the options you have for overriding the default components because this is the way you plug in your custom implementations and apply security as it fits your application. With the projects we’ll work on, **you’ll often find multiple ways to override a configuration. This flexibility can create confusion. I frequently see a mix of different styles of configuring different parts of Spring Security in the same application, which is undesirable.**

### Overriding the UserDetailsService component

The first component we talked about in this chapter was ``UserDetailsService``. **As you saw, the application uses this component in the process of authentication.** I’ll use an implementation provided by Spring Security, named ``InMemoryUserDetailsManager``. Even if this implementation is a bit more than just a ``UserDetailsService``, for now, we only refer to it from the perspective of a ``UserDetailsService``.

**NOTE**. An ``InMemoryUserDetailsManager`` implementation isn’t meant for production-ready applications, but it’s an excellent tool for examples or proof of concepts.

```
@Configuration
public class ProjectConfig {
    
    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager();
    }
}
```

If you execute the code exactly as it is now, you’ll no longer see the autogenerated password in the console. The application now uses the instance of type UserDetailsService you added to the context instead of the default autoconfigured one. But, at the same time, you won’t be able to access the endpoint anymore for two reasons:
* You don’t have any users.
* You don’t have a ``PasswordEncoder``.

Let’s solve these two issues step by step. We need to:
* Create at least one user who has a set of credentials (username and password).
* Add the user to be managed by our implementation of ``UserDetailsService``.
* Define a bean of the type ``PasswordEncoder`` that our application can use to verify a given password with the one stored and managed by ``UserDetailsService``.

First, we declare and add a set of credentials that we can use for authentication to the instance of InMemoryUserDetailsManager.

When building the ``UserDetails``, we have to provide the username, the password, and at least one authority. The authority is an action allowed for that user, and we can use any string for this. I name the authority read, but because we won’t use this authority for the moment, this name doesn’t really matter.

```
@Configuration
public class ProjectConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        var userDetailsManager = new InMemoryUserDetailsManager();

        var user = User.withUsername("john")
                .password("12345")
                .authorities("read")
                .build();

        userDetailsManager.createUser(user);

        return userDetailsManager;
    }
}
```

But this is still not enough to allow us to call the endpoint. We also need to declare a ``PasswordEncoder``. When using the default ``UserDetailsService``, a ``PasswordEncoder`` is also autoconfigured. Because we overrode ``UserDetailsService``, we also have to declare a ``PasswordEncoder``. Trying the example now, you’ll see an exception when you call the endpoint.

The client gets back an HTTP 401 Unauthorized message and an empty response body:
```
curl -u john:12345 http://localhost:8080/hello
```

The result of the call in the app’s console is:
```
java.lang.IllegalArgumentException: There is no PasswordEncoder mapped for the id "null"
at org.springframework.security.crypto.password .DelegatingPasswordEncoder$UnmappedIdPasswordEncoder
...
```

To solve:
```
@Bean
public PasswordEncoder passwordEncoder() {
  return NoOpPasswordEncoder.getInstance();
}
```

**NOTE**. The ``NoOpPasswordEncoder`` instance treats passwords as plain text. It doesn’t encrypt or hash them.

Let’s try the endpoint with the new user having the username John and the password 12345:
```
curl -u john:12345 http://localhost:8080/hello
```

### Overriding the endpoint authorization configuration

With default configuration, all the endpoints assume you have a valid user managed by the application. Also, by default, your app uses HTTP Basic authentication as the authorization method, but you can easily override this configuration.

To make such changes, we start by extending the ``WebSecurityConfigurerAdapter`` class. Extending this class allows us to override the ``configure(HttpSecurity http)`` method as presented in the next listing.

The code configures endpoint authorization with the same behavior as the default one:
```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();
        http.authorizeRequests().anyRequest().authenticated();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var userDetailsManager = new InMemoryUserDetailsManager();

        var user = User.withUsername("john")
                .password("12345")
                .authorities("read")
                .build();

        userDetailsManager.createUser(user);

        return userDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
```

With a slight change, you can make all the endpoints accessible without the need for credentials. You’ll see how to do this in the following listing.
```
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.httpBasic();
    http.authorizeRequests().anyRequest().permitAll();
}
```

Now, we can call the ``/hello`` endpoint without the need for credentials:
```
curl http://localhost:8080/hello
```
Response:
```
Hello!
```

### Setting the configuration in different ways

One of the confusing aspects of creating configurations with Spring Security is having multiple ways to configure the same thing. In this section, you’ll learn alternatives for configuring ``UserDetailsService`` and ``PasswordEncoder``.

In the configuration class, instead of defining these two objects as beans, we set them up through the ``configure(AuthenticationManagerBuilder auth)`` method. We override this method from the ``WebSecurityConfigurerAdapter`` class and use its parameter of type ``AuthenticationManagerBuilder`` to set both the ``UserDetailsService`` and the ``PasswordEncoder`` as shown in the following listing.

```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();
        http.authorizeRequests().anyRequest().authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        var userDetailsManager = new InMemoryUserDetailsManager();

        var user = User.withUsername("john")
                .password("12345")
                .authorities("read")
                .build();

        userDetailsManager.createUser(user);

        auth.userDetailsService(userDetailsManager).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
}
```

The difference is that now this is done locally inside the second overridden method. We also call the ``userDetailsService()`` method from the ``AuthenticationManagerBuilder`` to register the UserDetailsService instance.

Mixing two is bad practice:

```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();
        http.authorizeRequests().anyRequest().authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        var userDetailsManager = new InMemoryUserDetailsManager();

        var user = User.withUsername("john")
                .password("12345")
                .authorities("read")
                .build();

        userDetailsManager.createUser(user);

        auth.userDetailsService(userDetailsManager);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
```

Using the ``AuthenticationManagerBuilder``, you can configure users for authentication directly. It creates the ``UserDetailsService`` for you in this case. The syntax, however, becomes even more complex and could be considered difficult to read. I’ve seen this choice more than once, even with production-ready systems.

```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();
        http.authorizeRequests().anyRequest().authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("john")
                .password("12345")
                .authorities("read")
                .and()
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
}
```

Generally, I don’t recommend this approach, as I find it better to separate and write responsibilities as decoupled as possible in an application.

### Overriding the AuthenticationProvider implementation

It’s time to learn that you can also customize the component that delegates to these, the ``AuthenticationProvider``.

![AuthenticationProvider.PNG](pictures/AuthenticationProvider.PNG)

Figure 2.3 shows the ``AuthenticationProvider``, which implements the authentication logic and delegates to the ``UserDetailsService`` and ``PasswordEncoder`` for user and password management. So we could say that with this section, we go one step deeper in the authentication and authorization architecture to learn how to implement custom authentication logic with ``AuthenticationProvider``.

I recommend that you respect the responsibilities as designed in the Spring Security architecture. This architecture is loosely coupled with fine-grained responsibilities. That design is one of the things that makes Spring Security flexible and easy to integrate in your applications. But depending on how you make use of its flexibility, you could change the design as well. You have to be careful with these approaches as they can complicate your solution. For example, you could choose to override the default ``AuthenticationProvider`` in a way in which you no longer need a ``UserDetailsService`` or ``PasswordEncoder``.

```
@Component
public class CustomAuthProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var username = authentication.getName();
        var password = String.valueOf(authentication.getCredentials());

        if ("john".equals(username) && "12345".equals(password)) {
            return new UsernamePasswordAuthenticationToken(username, password, List.of());
        } else {
            throw new AuthenticationCredentialsNotFoundException("Error!");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
```

```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationProvider authenticationProvider;

    public ProjectConfig(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();
        http.authorizeRequests().anyRequest().authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }
}
```

### Using multiple configuration classes in your project

It is, good practice to separate the responsibilities even for the configuration classes. For this example, we can separate user management configuration from authorization configuration. We do that by defining two configuration classes: ``UserManagementConfig`` and ``WebAuthorizationConfig``.

# Chapter 3. Managing users

This chapter is about understanding in detail one of the fundamental roles you encountered in the first example we worked on in chapter 2 - the ``UserDetailsService``. Along with the ``UserDetailsService``, we’ll discuss:
* ``UserDetails``, which describes the user for Spring Security.
* ``GrantedAuthority``, which allows us to define actions that the user can execute.
* ``UserDetailsManager``, which extends the ``UserDetailsService`` contract. Beyond the inherited behavior, it also describes actions like creating a user and modifying or deleting a user’s password.

## Implementing authentication in Spring Security

Figure 3.1 presents the authentication flow in Spring Security. This architecture is the backbone of the authentication process as implemented by Spring Security. **It’s really important to understand it because you’ll rely on it in any Spring Security implementation.**

In figure 3.1, the shaded boxes represent the components that we start with: the ``UserDetailsService`` and the ``PasswordEncoder``. These two components focus on the part of the flow that I often refer to as **“the user management part.”** In this chapter, the ``UserDetailsService`` and the ``PasswordEncoder`` are the components that **deal directly with user details and their credentials.**

![user-management-part.PNG](pictures/user-management-part.PNG)

As part of user management, we use the ``UserDetailsService`` and ``UserDetailsManager`` interfaces:
* The ``UserDetailsService`` is only responsible for retrieving the user by username. This action is the only one needed by the framework to complete authentication.
* The ``UserDetailsManager`` adds behavior that refers to adding, modifying, or deleting the user, which is a required functionality in most applications.

If the app only needs to authenticate the users, then implementing the ``UserDetailsService`` contract is enough to cover the desired functionality. To manage the users, ``UserDetailsService`` and the ``UserDetailsManager`` components need a way to represent them.

Spring Security offers the ``UserDetails`` contract, which you have to implement to describe a user in the way the framework understands.
Spring Security represents the actions that a user can do with the ``GrantedAuthority`` interface. We often call these **authorities**, and a user has one or more authorities.

![user-details-granted-authority-manager-service-relationship.PNG](pictures/user-details-granted-authority-manager-service-relationship.PNG)

## Describing the user

In this section, you’ll learn how to describe the users of your application such that Spring Security understands them. Learning how to represent users and make the framework aware of them is an essential step in building an authentication flow.

For Spring Security, a user definition should respect the ``UserDetails`` contract. The ``UserDetails`` contract represents the user as understood by Spring Security. The class of your application that describes the user has to implement this interface, and in this way, the framework understands it.

### Demystifying the definition of the UserDetails contract

```
public interface UserDetails extends Serializable {

	Collection<? extends GrantedAuthority> getAuthorities();

	String getPassword();

	String getUsername();

	boolean isAccountNonExpired();

	boolean isAccountNonLocked();

	boolean isCredentialsNonExpired();

	boolean isEnabled();
}
```

The ``getUsername()`` and ``getPassword()`` methods return, as you’d expect, the username and the password. The app uses these values in the process of authentication, and these are the only details related to authentication from this contract. The other five methods all relate to authorizing the user for accessing the application’s resources.

We say a user has or hasn’t the privilege to perform an action, and an authority represents the privilege a user has. We implement the ``getAuthorities()`` method to return the group of authorities granted for a user.

Furthermore, as seen in the UserDetails contract, a user can
* Let the account expire
* Lock the account
* Let the credentials expire
* Disable the account

Not all applications have accounts that expire or get locked with certain conditions. **If you do not need to implement these functionalities in your application, you can simply make these four methods return true.**

### Detailing on the GrantedAuthority contract

The authorities represent what the user can do in your application. Without authorities, all users would be equal. While there are simple applications in which the users are equal, in most practical scenarios, an application defines multiple kinds of users. **To describe the authorities in Spring Security, you use the ``GrantedAuthority`` interface.**

It represents a privilege granted to the user. A user can have none to any number of authorities, and usually, they have at least one. Here’s the implementation of the ``GrantedAuthority`` definition:
```
public interface GrantedAuthority extends Serializable {
  String getAuthority();
}
```

To create an authority, you only need to find a name for that privilege so you can refer to it later when writing the authorization rules. 

In this chapter, we’ll implement the ``getAuthority()`` method to return the authority’s name as a ``String``. The ``SimpleGrantedAuthority`` class offers a way to create immutable instances of the type ``GrantedAuthority``. Here we make use of a lambda expression and then use the ``SimpleGrantedAuthority`` class:
```
GrantedAuthority g1 = () -> "READ";
GrantedAuthority g2 = new SimpleGrantedAuthority("READ");
```

### Writing a minimal implementation of UserDetails

We start with a basic implementation in which each method returns a static value. Then we change it to a version that you’ll more likely find in a practical scenario, and one that allows you to have multiple and different instances of users.

With a class named DummyUser, let’s implement a minimal description of a user.

```
public class DummyUser implements UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "READ");
    }

    @Override
    public String getPassword() {
        return "12345";
    }

    @Override
    public String getUsername() {
        return "bill";
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
```

This minimal implementation means that all instances of the class represent the same user.

For a real application, you should create a class that you can use to generate instances that can represent different users. In this case, your definition would at least have the username and the password as attributes in the class.

```
public class SimpleUser implements UserDetails {
    
    private final String username;
    private final String password;

    public SimpleUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }
    ...
}
```

### Using a builder to create instances of the UserDetails type

Some applications are simple and don’t need a custom implementation of the ``UserDetails`` interface. In this section, we take a look at using a builder class provided by Spring Security to create simple user instances. Building the user in this way, you don’t need to have an implementation of the ``UserDetails`` contract:
```
UserDetails u = User.withUsername("bill")
                .password("12345")
                .authorities("read", "write")
                .accountExpired(false)
                .disabled(true)
                .build();
```

### Combining multiple responsibilities related to the user

In most cases, you find multiple responsibilities to which a user relates. And if you store users in a database, and then in the application, you would need a class to represent the persistence entity as well. Let’s consider we have a table in an SQL database in which we store the users. To make the example shorter, we give each user only one authority.

```
@Entity
public class User {
    @Id
    private Long id;
    private String username;
    private String password;
    private String authority;
// Omitted getters and setters
}
```

If you make the same class also implement the Spring Security contract for user details, the class becomes more complicated. From my point of view, it is a mess. I would get lost in it.

```
@Entity
public class User implements UserDetails {
    @Id
    private int id;
    private String username;
    private String password;
    private String authority;
    @Override
    public String getUsername() {
        return this.username;
    }
    @Override
    public String getPassword() {
        return this.password;
    }
    public String getAuthority() {
        return this.authority;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> this.authority);
    }
// Omitted code
}
```

How can we write this code to be cleaner? The root of the muddy aspect of the previous code example is a mix of two responsibilities. While it’s true that you need both in the application, in this case, nobody says that you have to put these into the same class. Let’s try to separate those by defining a separate class called ``SecurityUser``, which decorates the ``User`` class.
```
@Entity
public class User {
    @Id
    private int id;
    private String username;
    private String password;
    private String authority;
// Omitted getters and setters
}
```

```
public class SecurityUser implements UserDetails {
    private final User user;
    public SecurityUser(User user) {
        this.user = user;
    }
    @Override
    public String getUsername() {
        return user.getUsername();
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> user.getAuthority());
    }
// Omitted code
}
```

## Instructing Spring Security on how to manage users

In this section, we experiment with various ways of implementing the ``UserDetailsService`` class. You’ll understand how user management works by implementing the responsibility described by the ``UserDetailsService`` contract in our example. After that, you’ll find out how the ``UserDetailsManager`` interface adds more behavior to the contract defined by the ``UserDetailsService``. At the end of this section, we’ll use the provided implementations of the ``UserDetailsManager`` interface offered by Spring Security.

### Understanding the UserDetailsService contract

The ``UserDetailsService`` interface contains only one method, as follows:

```
public interface UserDetailsService {
	
	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
```

The authentication implementation calls the ``loadUserByUsername(String username)`` method to obtain the details of a user with a given username (figure 3.3). The username is, of course, considered unique.

![user-details-service-auth-provider.PNG](pictures/user-details-service-auth-provider.PNG)

### Implementing the UserDetailsService contract

Your application manages details about credentials and other user aspects. It could be that these are stored in a database or handled by another system that you access through a web service or by other means (figure 3.3). Regardless of how this happens in your system, the only thing Spring Security needs from you is an implementation to retrieve the user by username.

In the next example, we write a UserDetailsService that has an in-memory list of users.

```
public class User implements UserDetails {
    
    private final String username;
    private final String password;
    private final String authority;

    public User(String username, String password, String authority) {
        this.username = username;
        this.password = password;
        this.authority = authority;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> authority);
    }
    
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
```

In the package named services, we create a class called ``InMemoryUserDetailsService``.

```
public class InMemoryUserDetailsService implements UserDetailsService {
    
    private final List<UserDetails> users;

    public InMemoryUserDetailsService(List<UserDetails> users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return users.stream()
                .filter(userDetails -> username.equals(userDetails.getUsername()))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("Bad credentials"));
    }
}
```

```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();
        http.authorizeRequests().anyRequest().authenticated();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var user = new User("john", "12345", "read");

        return new InMemoryUserDetailsService(List.of(user));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
```

Test it:
```
curl -u john:12345 http://localhost:8080/hello
```

### Implementing the UserDetailsManager contract

In this section, we discuss using and implementing the ``UserDetailsManager`` interface. This interface extends and adds more methods to the ``UserDetailsService`` contract. Spring Security needs the ``UserDetailsService`` contract to do the authentication. But generally, in applications, there is also a need for managing users. Most of the time, an app should be able to add new users or delete existing ones. In this case, we implement a more particular interface defined by Spring Security, the ``UserDetailsManager``. It extends ``UserDetailsService`` and adds more operations that we need to implement.

```
public interface UserDetailsManager extends UserDetailsService {
    void createUser(UserDetails user);
    void updateUser(UserDetails user);
    void deleteUser(String username);
    void changePassword(String oldPassword, String newPassword);
    boolean userExists(String username);
}
```

#### USING A JDBCUSERDETAILSMANAGER FOR USER MANAGEMENT

The ``JdbcUserDetailsManager`` manages users in an SQL database. It connects to the database directly through JDBC. This way, the ``JdbcUserDetailsManager`` is independent of any other framework or specification related to database connectivity.

You’ll start working on our demo application about how to use the ``JdbcUserDetailsManager`` by creating a database and two tables. In our case, we name the database spring, and we name one of the tables ``users`` and the other ``authorities``. **These names are the default table names known by the ``JdbcUserDetailsManager``.** As you’ll learn at the end of this section, the ``JdbcUserDetailsManager`` implementation is flexible and lets you override these default names if you want to do so.

The ``JdbcUserDetailsManager`` implementation expects **three columns in the users table: a username, a password, and enabled, which you can use to deactivate the user.**

![using-jdbcdetailsservicemanager.PNG](pictures/using-jdbcdetailsservicemanager.PNG)

But the easiest would be to let Spring Boot itself run the scripts for you. To do this, just add two more files to your project in the resources folder: ``schema.sql`` and ``data.sql``.

``schema.sql``:
```
CREATE TABLE IF NOT EXISTS users (
    id          INT         NOT NULL AUTO_INCREMENT,
    username    VARCHAR(45) NOT NULL,
    password    VARCHAR(45) NOT NULL,
    enabled     BIT         NOT NULL,
    PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS authorities (
    id          INT         NOT NULL AUTO_INCREMENT,
    username    VARCHAR(45) NOT NULL,
    authority   VARCHAR(45) NOT NULL,
    PRIMARY KEY (id)
);
```

``data.sql``:
```
INSERT INTO authorities VALUES (NULL, 'john', 'write');
INSERT INTO users VALUES (NULL, 'john', '12345', 1);
```

The ``JdbcUserDetailsManager`` needs the ``DataSource`` to connect to the database.

```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();
        http.authorizeRequests().anyRequest().authenticated();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
```

Test:
```
curl -u john:12345 http://localhost:8080/hello
```

The ``JdbcUserDetailsManager`` also allows you to configure the queries used. In the previous example, we made sure we used the exact names for the tables and columns, as the ``JdbcUserDetailsManager`` implementation expects those. But it could be that for your application, these names are not the best choice.

```
    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        var usersByUsernameQuery = "SELECT username, password, enabled, FROM spring.users WHERE username = ?";
        var authsByUserQuery = "SELECT username, authority FROM spring.authorities where username = ?";
        var jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery(usersByUsernameQuery);
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(authsByUserQuery);
        return jdbcUserDetailsManager;
    }
```

#### USING AN LDAPUSERDETAILSMANAGER FOR USER MANAGEMENT

Spring Security also offers an implementation of ``UserDetailsManager`` for LDAP. In the project ssiach3-ex3, you can find a simple demonstration of using the ``LdapUserDetailsManager``. Because I can’t use a real LDAP server for this demonstration, I have set up an embedded one in my Spring Boot application. To set up the embedded LDAP server, I defined a simple LDAP Data Interchange Format (LDIF) file. The following listing shows the content of my LDIF file.
```
#Defines the base entity
dn: dc=springframework,dc=org
objectclass: top
objectclass: domain
objectclass: extensibleObject
dc: springframework

#Defines a group entity
dn: ou=groups,dc=springframework,dc=org
objectclass: top
objectclass: organizationalUnit
ou: groups

#Defines a user
dn: uid=john,ou=groups,dc=springframework,dc=org
objectclass: top
objectclass: person
objectclass: organizationalPerson
objectclass: inetOrgPerson
cn: John
sn: John
uid: john
userPassword: 12345
```

In the LDIF file, I add only one user for which we need to test the app’s behavior at the end of this example. We can add the LDIF file directly to the ``resources`` folder. This way, it’s automatically in the classpath, so we can easily refer to it later. I named the LDIF file ``server.ldif``. To work with LDAP and to allow Spring Boot to start an embedded LDAP server, you need to add ``pom.xml`` to the dependencies as in the following code snippet:
```
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-ldap</artifactId>
</dependency>

<dependency>
    <groupId>com.unboundid</groupId>
    <artifactId>unboundid-ldapsdk</artifactId>
</dependency>

```

In the ``application.properties`` file, you also need to add the configurations for the embedded LDAP server as presented in the following code snippet. The values the app needs to boot the embedded LDAP server include the location of the LDIF file, a port for the LDAP server, and the base domain component (DN) label values:
```
spring:
  ldap:
    embedded:
      ldif: classpath:server.ldif
      base-dn: dc=springframework,dc=org
      port: 33389
```

Once you have an LDAP server for authentication, you can configure your application to use it.

```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();
        http.authorizeRequests().anyRequest().authenticated();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        //Creates a context source  to specify the address of  the LDAP server
        var cs = new DefaultSpringSecurityContextSource("ldap://127.0.0.1:33389/dc=springframework,dc=org");
        cs.afterPropertiesSet();

        var manager = new LdapUserDetailsManager(cs);
        //Sets a username mapper to instruct the LdapUserDetailsManager on how to search for users
        manager.setUsernameMapper(new DefaultLdapUsernameToDnMapper("ou=groups", "uid"));
        //Sets the group search base that the app needs to search for users
        manager.setGroupSearchBase("ou=groups");
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
```

```
curl -u john:12345 http://localhost:8080/hello
```

# Chapter 4. Dealing with passwords

## Understanding the PasswordEncoder contract

We continue with a deep understanding of these beans and ways to implement them, so in this section, we analyze the PasswordEncoder.

![chapter-4-password-encoder.PNG](pictures/chapter-4-password-encoder.PNG)

### The definition of the PasswordEncoder contract

In this section, we discuss the definition of the PasswordEncoder contract. You implement this contract to tell Spring Security how to validate a user’s password. In the authentication process, the PasswordEncoder decides if a password is valid or not. Every system stores passwords encoded in some way. You preferably store them hashed so that there’s no chance someone can read the passwords. The PasswordEncoder can also encode passwords. The methods encode() and matches(), which the contract declares, are actually the definition of its responsibility.

Let’s first review the content of the PasswordEncoder interface:

```
public interface PasswordEncoder {

	String encode(CharSequence rawPassword);

	boolean matches(CharSequence rawPassword, String encodedPassword);

	default boolean upgradeEncoding(String encodedPassword) {
		return false;
	}
}
```

The purpose of the ``encode(CharSequence rawPassword)`` method is to return a transformation of a provided string. In terms of Spring Security functionality, it’s used to provide encryption or a hash for a given password. You can use the ``matches(CharSequence rawPassword, String encodedPassword)`` method afterward to check if an encoded string matches a raw password. You use the ``matches()`` method in the authentication process to test a provided password against a set of known credentials. The third method, called ``upgradeEncoding(CharSequence encodedPassword)``, defaults to false in the contract. If you override it to return true, then the encoded password is encoded again for better security.

**In some cases, encoding the encoded password can make it more challenging to obtain the cleartext password from the result. In general, this is some kind of obscurity that I, personally, don’t like.**

### Implementing the PasswordEncoder contract

If you override them, they should always correspond in terms of functionality: a string returned by the ``encode()`` method should always be verifiable with the ``matches()`` method of the same PasswordEncoder. In this section, you’ll implement the PasswordEncoder contract and define the two abstract methods declared by the interface.

The most straightforward implementation is a password encoder that considers passwords in plain text: that is, it doesn’t do any encoding on the password. Managing passwords in cleartext is what the instance of NoOpPasswordEncoder does precisely. If you were to write your own, it would look something like the following listing:
```
public class PlainTextPasswordEncoder implements PasswordEncoder {
    
    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return rawPassword.equals(encodedPassword);
    }
}
```

A simple implementation of PasswordEncoder that uses the hashing algorithm SHA-512 looks like the next listing:
```
public class Sha512PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return hashWithSHA512(rawPassword.toString());
    }
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String hashedPassword = encode(rawPassword);
        return encodedPassword.equals(hashedPassword);
    }

    private String hashWithSHA512(String input) {
        StringBuilder result = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte [] digested = md.digest(input.getBytes());
            for (byte b : digested) {
                result.append(Integer.toHexString(0xFF & b));
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Bad algorithm");
        }
        return result.toString();
    }
}
```

### Choosing from the provided implementations of PasswordEncoder

While knowing how to implement your PasswordEncoder is powerful, you also have to be aware that Spring Security already provides you with some advantageous implementations. If one of these matches your application, you don’t need to rewrite it. Provided implementations:
* ``NoOpPasswordEncoder`` — Doesn’t encode the password but keeps it in cleartext. We use this implementation only for examples. Because it doesn’t hash the password, **you should never use it in a real-world scenario.**
* ``StandardPasswordEncoder`` — Uses SHA-256 to hash the password. This implementation is now deprecated, and you **shouldn’t use it for your new implementations.** The reason why it’s deprecated is that it uses a hashing algorithm that we don’t consider strong enough anymore, but you might still find this implementation used in existing applications.
* ``Pbkdf2PasswordEncoder`` — Uses the password-based key derivation function 2 (PBKDF2).
* ``BCryptPasswordEncoder`` — Uses a bcrypt strong hashing function to encode the password.
* ``SCryptPasswordEncoder`` — Uses an scrypt hashing function to encode the password.

[Choose BCrypt](https://security.stackexchange.com/questions/4781/do-any-security-experts-recommend-bcrypt-for-password-storage/6415#6415).

To create instances of the ``Pbkdf2PasswordEncoder``, you have the following options:
* ``PasswordEncoder p = new Pbkdf2PasswordEncoder()``;
* ``PasswordEncoder p = new Pbkdf2PasswordEncoder("secret")``;
* ``PasswordEncoder p = new Pbkdf2PasswordEncoder("secret", 185000, 256)``;

The PBKDF2 is a pretty easy, slow-hashing function that performs an HMAC as many times as specified by an iterations argument. The three parameters received by the last call are the value of a key used for the encoding process, the number of iterations used to encode the password, and the size of the hash. The second and third parameters can influence the strength of the result. You can choose more or fewer iterations, as well as the length of the result. The longer the hash, the more powerful the password. However, be aware that performance is affected by these values: the more iterations, the more resources your application consumes.

If you do not specify one of the second or third values for the Pbkdf2PasswordEncoder implementation, the defaults are 185000 for the number of iterations and 256 for the length of the result.

Another excellent option offered by Spring Security is the ``BCryptPasswordEncoder``, which uses a bcrypt strong hashing function to encode the password. You can instantiate the ``BCryptPasswordEncoder`` by calling the no-arguments constructor. But you also have the option to specify a strength coefficient representing the log rounds (logarithmic rounds) used in the encoding process. Moreover, you can also alter the ``SecureRandom`` instance used for encoding:
```
PasswordEncoder p = new BCryptPasswordEncoder();
PasswordEncoder p = new BCryptPasswordEncoder(4);

SecureRandom s = SecureRandom.getInstanceStrong();
PasswordEncoder p = new BCryptPasswordEncoder(4, s);
```

The log rounds value that you provide affects the number of iterations the hashing operation uses. The number of iterations used is 2log rounds. For the iteration number computation, the value for the **log rounds can only be between 4 and 31.** You can specify this by calling one of the second or third overloaded constructors, as shown in the previous code snippet.

The last option I present to you is ``SCryptPasswordEncoder`` (figure 4.2). This password encoder uses an scrypt hashing function. For the ``ScryptPasswordEncoder``, you have two options to create its instances:
```
PasswordEncoder p = new SCryptPasswordEncoder();
PasswordEncoder p = new SCryptPasswordEncoder(16384, 8, 1, 32, 64);
```
The values in the previous examples are the ones used if you create the instance by calling the no-arguments constructor.

![scrypt-password-constructor.PNG](pictures/scrypt-password-constructor.PNG)

### Multiple encoding strategies with DelegatingPasswordEncoder

In some applications, you might find it useful to have various password encoders and choose from these depending on some specific configuration. A common scenario
in which I find the ``DelegatingPasswordEncoder`` in production applications is when the **encoding algorithm is changed, starting with a particular version of the application.**

Imagine somebody finds a vulnerability in the currently used algorithm, and you want to change it for newly registered users, but you do not want to change it for existing credentials. So you end up having multiple kinds of hashes. How do you manage this case? While it isn’t the only approach for this scenario, a good choice is to use a ``DelegatingPasswordEncoder`` object.
The ``DelegatingPasswordEncoder`` is an implementation of the ``PasswordEncoder`` interface that, instead of implementing its encoding algorithm, delegates to another instance of an implementation of the same contract. The hash starts with a prefix naming the algorithm used to define that hash. The ``DelegatingPasswordEncoder`` delegates to the correct implementation of the ``PasswordEncoder`` based on the prefix of the password.
It sounds complicated, but with an example, you can observe that it is pretty easy. You start by creating a collection of instances of your desired PasswordEncoder implementations, and you put these together in a DelegatingPasswordEncoder as in the following listing:
```
@Configuration
public class ProjectConfig {

    ...

    @Bean
    public PasswordEncoder passwordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("noop", NoOpPasswordEncoder.getInstance());
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("scrypt", new SCryptPasswordEncoder());
        return new DelegatingPasswordEncoder("bcrypt", encoders);
    }
}
```

The ``DelegatingPasswordEncoder`` is just a tool that acts as a ``PasswordEncoder`` so you can use it when you have to choose from a collection of implementations. In listing 4.4, the declared instance of ``DelegatingPasswordEncoder`` contains references to a ``NoOpPasswordEncoder``, a ``BCryptPasswordEncoder``, and an ``SCryptPasswordEncoder``, and delegates the default to the ``BCryptPasswordEncoder`` implementation.
Based on the prefix of the hash, the ``DelegatingPasswordEncoder`` uses the right ``PasswordEncoder`` implementation for matching the password. This prefix has the key that identifies the password encoder to be used from the map of encoders. If there is no prefix, the ``DelegatingPasswordEncoder`` uses the default encoder.

**NOTE**. The curly braces are part of the hash prefix, and those should **surround the name of the key**. For example, if the provided hash is ``{noop}12345``, the ``DelegatingPasswordEncoder`` delegates to the ``NoOpPasswordEncoder`` that we registered for the prefix noop. Again, don’t forget that the **curly braces are mandatory in the prefix.**

If the hash looks like the next code snippet, the password encoder is the one we assign to the prefix {bcrypt}, which is the ``BCryptPasswordEncoder``. This is also the one
to which the application will delegate if there is no prefix at all because we defined it as the default implementation:
```
{bcrypt}$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG
```

For convenience, Spring Security offers a way to create a ``DelegatingPasswordEncoder`` that has a map to all the standard provided implementations of ``PasswordEncoder``. The ``PasswordEncoderFactories`` class provides a ``createDelegatingPasswordEncoder()`` static method that returns the implementation of the ``DelegatingPasswordEncoder`` with bcrypt as a default encoder:
```
PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
```

To test ``DelegatingPasswordEncoder``:
```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();
        http.authorizeRequests().anyRequest().authenticated();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        var encoders = Map.of(
                "noop", NoOpPasswordEncoder.getInstance(),
                "bcrypt", new BCryptPasswordEncoder(),
                "scrypt", new SCryptPasswordEncoder());
        return new DelegatingPasswordEncoder("bcrypt", encoders);
    }
}
```

``schema.sql``:
```
CREATE SCHEMA spring;

CREATE TABLE IF NOT EXISTS users (
    id          INT         NOT NULL AUTO_INCREMENT,
    username    VARCHAR(45) NOT NULL,
    password    VARCHAR(45) NOT NULL,
    enabled     BIT         NOT NULL,
    PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS authorities (
    id          INT         NOT NULL AUTO_INCREMENT,
    username    VARCHAR(45) NOT NULL,
    authority   VARCHAR(45) NOT NULL,
    PRIMARY KEY (id)
);
```
``data.sql``:
```
INSERT INTO authorities VALUES (NULL, 'john', 'write');
INSERT INTO users VALUES (NULL, 'john', '{noop}12345', 1);
```

Test:
```
curl -u john:12345 http://localhost:8080/hello
```

## More about the Spring Security Crypto module

In this section, we discuss the Spring Security Crypto module (SSCM), which is the part of Spring Security that deals with cryptography. Using encryption and decryption functions and generating keys isn’t offered out of the box with the Java language. 

### Using key generators

A **key generator** is an object used to generate a specific kind of key, generally needed for an encryption or hashing algorithm. 
Two interfaces represent the two main types of key generators: ``BytesKeyGenerator`` and ``StringKeyGenerator``. We can build them directly by making use of the factory class ``KeyGenerators``. You can use a string key generator, represented by the ``StringKeyGenerator`` contract, to obtain a key as a string. **Usually, we use this key as a salt value for a hashing or encryption algorithm.** You can find the definition of the ``StringKeyGenerator`` contract in this code snippet:
```
public interface StringKeyGenerator {
  String generateKey();
}
```

The generator has only a ``generateKey()`` method that returns a string representing the key value. The next code snippet presents an example of how to obtain a ``StringKeyGenerator`` instance and how to use it to get a salt value:
```
StringKeyGenerator keyGenerator = KeyGenerators.string();
String salt = keyGenerator.generateKey();
```
The generator creates an 8-byte key, and it encodes that as a hexadecimal string. The method returns the result of these operations as a string. The second interface describing a key generator is the ``BytesKeyGenerator``, which is defined as follows:
```
public interface BytesKeyGenerator {
  int getKeyLength();
  byte[] generateKey();
}
```
In addition to the ``generateKey()`` method that returns the key as a byte[], the interface defines another method that returns the key length in number of bytes. A default ``ByteKeyGenerator`` generates keys of 8-byte length:
```
BytesKeyGenerator keyGenerator = KeyGenerators.secureRandom();
byte [] key = keyGenerator.generateKey();
int keyLength = keyGenerator.getKeyLength();
```
In the previous code snippet, the key generator generates keys of 8-byte length. If you want to specify a different key length, you can do this when obtaining the key generator instance by providing the desired value to the ``KeyGenerators.secureRandom()`` method:
```
BytesKeyGenerator keyGenerator = KeyGenerators.secureRandom(16);
```
The keys generated by the ``BytesKeyGenerator`` created with the ``KeyGenerators.secureRandom()`` method are unique for each call of the ``generateKey()`` method.

In some cases, we prefer an implementation that returns the same key value for each call of the same key generator. In this case, we can create a ``BytesKeyGenerator`` with the ``KeyGenerators.shared(int length)`` method. In this code snippet, ``key1`` and ``key2`` have the same value:
```
BytesKeyGenerator keyGenerator = KeyGenerators.shared(16);
byte [] key1 = keyGenerator.generateKey();
byte [] key2 = keyGenerator.generateKey();
```

### Using encryptors for encryption and decryption operations

An **encryptor** is an object that implements an encryption algorithm. When talking about security, encryption and decryption are common operations, so expect to need these within your application.
There are two types of encryptors defined by the SSCM: ``BytesEncryptor`` and ``TextEncryptor``. 
```
public interface TextEncryptor {
  String encrypt(String text);
  String decrypt(String encryptedText);
}
```

```
public interface BytesEncryptor {
  byte[] encrypt(byte[] byteArray);
  byte[] decrypt(byte[] encryptedByteArray);
}
```

Let’s find out what options we have to build and use an encryptor. The factory class Encryptors offers us multiple possibilities. For BytesEncryptor, we could use the ``Encryptors.standard()`` or the ``Encryptors.stronger()`` methods like this:
```
String salt = KeyGenerators.string().generateKey();
String password = "secret";
String valueToEncrypt = "HELLO";

BytesEncryptor e = Encryptors.standard(password, salt);
byte [] encrypted = e.encrypt(valueToEncrypt.getBytes());
byte [] decrypted = e.decrypt(encrypted);
```

Behind the scenes, the standard byte encryptor uses 256-byte AES encryption to encrypt input. To build a stronger instance of the byte encryptor, you can call the ``Encryptors.stronger()`` method:
```
BytesEncryptor e = Encryptors.stronger(password, salt);
```
The difference is small and happens behind the scenes, where the AES encryption on 256-bit uses Galois/Counter Mode (GCM) as the mode of operation. The standard mode uses cipher block chaining (CBC), which is considered a weaker method.

``TextEncryptors`` come in three main types. You create these three types by calling methods:
* ``Encryptors.text()``
* ``Encryptors.delux()``
* ``Encryptors.queryableText()``

Besides these methods to create encryptors, there is also a method that returns a dummy ``TextEncryptor``, which doesn’t encrypt the value. You can use the dummy ``TextEncryptor`` for demo examples or cases in which you want to test the performance of your application without spending time spent on encryption. The method that returns this no-op encryptor is ``Encryptors.noOpText()``. In the following code snippet, you’ll find an example of using a ``TextEncryptor``. Even if it is a call to an encryptor, in the example, encrypted and valueToEncrypt are the same:
``
String valueToEncrypt = "HELLO";
TextEncryptor e = Encryptors.noOpText();
String encrypted = e.encrypt(valueToEncrypt);
``

The ``Encryptors.text()`` encryptor uses the ``Encryptors.standard()`` method to manage the encryption operation, while the ``Encryptors.delux()`` method uses an
``Encryptors.stronger()`` instance like this:
```
String salt = KeyGenerators.string().generateKey();
String password = "secret";
String valueToEncrypt = "HELLO";

TextEncryptor e = Encryptors.text(password, salt);
String encrypted = e.encrypt(valueToEncrypt);
String decrypted = e.decrypt(encrypted);
```

For ``Encryptors.text()`` and ``Encryptors.delux()``, the ``encrypt()`` method called on the same input **repeatedly generates different outputs.** The different outputs occur because of the randomly generated initialization vectors used in the encryption process. **In the real world, you’ll find cases in which you don’t want this to happen, as in the case of the OAuth API key, for example.** This kind of input is called queryable text, and for this situation, you would make use of an ``Encryptors.queryableText()`` instance. This **encryptor guarantees that sequential encryption operations will generate the same output for the same input.** In the following example, the value of the ``encrypted1`` variable equals the value of the ``encrypted2`` variable:
```
String salt = KeyGenerators.string().generateKey();
String password = "secret";
String valueToEncrypt = "HELLO";

TextEncryptor e = Encryptors.queryableText(password, salt);
String encrypted1 = e.encrypt(valueToEncrypt);
String encrypted2 = e.encrypt(valueToEncrypt);
```

# Chapter 5. Implementing authentication

The ``AuthenticationProvider`` layer, however, is the one responsible for the logic of authentication. The ``AuthenticationProvider`` is where you find the conditions and instructions that decide whether to authenticate a request or not. The component that delegates this responsibility to the ``AuthenticationProvider`` is the ``AuthenticationManager``, which receives the request from the HTTP filter layer. In this chapter, let’s look at the authentication process, which has only two possible results:
* The **entity making the request is not authenticated**. The user is not recognized, and the application rejects the request without delegating to the authorization process. Usually, in this case, the response status sent back to the client is HTTP 401 Unauthorized.
* The **entity making the request is authenticated**. The details about the requester are stored such that the application can use these for authorization. As you’ll find out in this chapter, the ``SecurityContext`` interface is the instance that stores the details about the current authenticated request.

To remind you of the actors and the links between them, figure 5.1 provides the diagram that you also saw in chapter 2.

![chapter-5-authentication-provider.PNG](pictures/chapter-5-authentication-provider.PNG)

First, we need to discuss how to implement the AuthenticationProvider interface. You need to know how Spring Security understands a request in the authentication process.

## Understanding the AuthenticationProvider

In enterprise applications, you might find yourself in a situation in which the default implementation of authentication based on username and password does not apply. Additionally, when it comes to authentication, your application may require the implementation of several scenarios (figure 5.2). For example, you might want the user to be able to prove who they are by using a code received in an SMS message or displayed by a specific application. Or, you might need to implement authentication scenarios where the user has to provide a certain kind of key stored in a file. You might even need to use a representation of the user’s fingerprint to implement the authentication logic. A framework’s purpose is to be flexible enough to allow you to implement any of these required scenarios.

### Representing the request during authentication

You first need to understand how to represent the authentication event itself. 

**Authentication** is one of the essential interfaces involved in the process with the same name. The **Authentication** interface represents the authentication request event and holds the details of the entity that requests access to the application. You can use the information related to the authentication request event during and after the authentication process. The user requesting access to the application is called a **principal**.

![chapter-5-authentication-interface-principal.PNG](pictures/chapter-5-authentication-interface-principal.PNG)

The **Authentication** contract in Spring Security not only represents a principal, it also adds information on whether the authentication process finishes, as well as a collection of authorities.

Authentication interface:
```
public interface Authentication extends Principal, Serializable {

  Collection<? extends GrantedAuthority> getAuthorities();
  Object getCredentials();
  Object getDetails();
  Object getPrincipal();
  boolean isAuthenticated();
  void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException;
}
```

For the moment, the only methods of this contract that you need to learn are these:
* **isAuthenticated()** — Returns true if the authentication process ends or false if the authentication process is still in progress.
* **getCredentials()** — Returns a password or any secret used in the process of authentication.
* **getAuthorities()** — Returns a collection of granted authorities for the authenticated request.

### Implementing custom authentication logic

The **AuthenticationProvider** in Spring Security takes care of the authentication logic. The default implementation of the **AuthenticationProvider** interface delegates the responsibility of finding the system’s user to a **UserDetailsService**. It uses the **PasswordEncoder** as well for password management in the process of authentication. The following listing gives the definition of the **AuthenticationProvider**, which you need to implement to define a custom authentication provider for your application.
```
public interface AuthenticationProvider {
  Authentication authenticate(Authentication authentication) throws AuthenticationException;
  boolean supports(Class<?> authentication);
}
```

The AuthenticationProvider responsibility is strongly coupled with the Authentication contract. The authenticate() method receives an Authentication object as a parameter and returns an Authentication object. We implement the authenticate() method to define the authentication logic. We can quickly summarize the way you should implement the authenticate() method with three bullets:
* The method should throw an AuthenticationException if the authentication fails.
* If the method receives an authentication object that is not supported by your implementation of AuthenticationProvider, then the method should return null. This way, we have the possibility of using multiple Authentication types separated at the HTTP-filter level.
* The method should return an **Authentication** instance representing a fully authenticated object. For this instance, the **isAuthenticated()** method returns true, and it contains all the necessary details about the authenticated entity. Usually, the application also removes sensitive data like a password from this instance. After implementation, the password is no longer required and keeping these details can potentially expose them to unwanted eyes.

The second method in the AuthenticationProvider interface is ``supports(Class<?> authentication)``. You can implement this method to return true if the current AuthenticationProvider supports the type provided as an Authentication object. Observe that even if this method returns true for an object, there is still a chance that the authenticate() method rejects the request by returning null. Spring Security is designed like this to be more flexible and to allow you to implement an AuthenticationProvider that can reject an authentication request based on the request’s details, not only by its type.

An analogy of how the authentication manager and authentication provider work together to validate or invalidate an authentication request is having a more complex lock for your door. You can open this lock either by using a card or an old fashioned physical key (figure 5.4). The lock itself is the authentication manager that decides whether to open the door. To make that decision, it delegates to the two authentication providers: one that knows how to validate the card or the other that knows how to verify the physical key.

![chapter-5-authentication-manager-lock-analogy.PNG](pictures/chapter-5-authentication-manager-lock-analogy.PNG)

### Applying custom authentication logic

In this section, we implement custom authentication logic. You can find this example in the project ssia-ch5-ex1. Step by step, an example of how to implement a custom AuthenticationProvider:
* Declare a class that implements the ``AuthenticationProvider`` contract.
* Decide which kinds of Authentication objects the new ``AuthenticationProvider`` supports:
  * Override the supports(Class<?> c) method to specify which type of authentication is supported by the AuthenticationProvider that we define.
  * Override the authenticate(Authentication a) method to implement the authentication logic.
* Register an instance of the new AuthenticationProvider implementation with Spring Security.


Then, we have to decide what kind of ``Authentication`` interface implementation this ``AuthenticationProvider`` supports. That depends on what type we expect to be provided as a parameter to the authenticate() method. If we don’t customize anything at the authentication-filter level, then the class ``UsernamePasswordAuthenticationToken`` defines the type.

```
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        var username = authentication.getName();
        var password = authentication.getCredentials().toString();

        var userDetails = userDetailsService.loadUserByUsername(username);
        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
        } else {
            throw new BadCredentialsException("Something went wrong!");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
```

To plug in the new implementation of the ``AuthenticationProvider``, override the ``configure(AuthenticationManagerBuilder auth)`` method of the ``WebSecurityConfigurerAdapter`` class in the configuration class of the project:
```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationProvider authenticationProvider;

    public ProjectConfig(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        return new InMemoryUserDetailsManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }
}
```

That’s it! You successfully customized the implementation of the ``AuthenticationProvider``.

## Using the SecurityContext

It is likely that you will need details about the authenticated entity after the authentication process ends. You might, for example, need to refer to the username or the authorities of the currently authenticated user. Is this information still accessible after the authentication process finishes? Once the ``AuthenticationManager`` completes the authentication process successfully, it stores the ``Authentication`` instance for the rest of the request. The instance storing the ``Authentication`` object is called the **security context**.

![chapter-5-security-context.PNG](pictures/chapter-5-security-context.PNG)

The security context of Spring Security is described by the ``SecurityContext`` interface. The following listing defines this interface:
```
public interface SecurityContext extends Serializable {
  Authentication getAuthentication();
  void setAuthentication(Authentication authentication);
}
```

As you can observe from the contract definition, the primary responsibility of the SecurityContext is to store the Authentication object. But how is the SecurityContext itself managed? Spring Security offers three strategies to manage the SecurityContext with an object in the role of a manager. It’s named the SecurityContextHolder:
* MODE_THREADLOCAL—Allows each thread to store its own details in the security context. In a thread-per-request web application, this is a common approach as each request has an individual thread.
* MODE_INHERITABLETHREADLOCAL—Similar to MODE_THREADLOCAL but also instructs Spring Security to copy the security context to the next thread in case of an asynchronous method. This way, we can say that the new thread running the @Async method inherits the security context.
* MODE_GLOBAL—Makes all the threads of the application see the same security context instance.

Besides these three strategies for managing the security context provided by Spring Security, in this section, we also discuss **what happens when you define your own threads that are not known by Spring.** As you will learn, for these cases, **you need to explicitly copy the details from the security context to the new thread.**

### Using a holding strategy for the security context

The first strategy for managing the security context is the **MODE_THREADLOCAL strategy.** This strategy is also the **default for managing the security context used by Spring Security.** With this strategy, Spring Security uses ThreadLocal to manage the context. ThreadLocal is an implementation provided by the JDK. This implementation works as a collection of data but makes sure that **each thread of the application can see only the data stored in the collection.** This way, each request has access to its security context. **No thread will have access to another’s ThreadLocal.**

Each request (A, B, and C) has its own allocated thread (T1, T2, and T3). This way, each request only sees the details stored in their security context. But this also means that if a new thread is created (for example, when an asynchronous method is called), the new thread will have its own security context as well. The details from the parent thread (the original thread of the request) are not copied to the security context of the new thread.

Being the default strategy for managing the security context, this process does not need to be explicitly configured. Just ask for the security context from the holder using the static getContext() method wherever you need it after the end of the authentication process.

```
@GetMapping("/hello")
public String hello() {
  SecurityContext context = SecurityContextHolder.getContext();
  Authentication a = context.getAuthentication();
  return "Hello, " + a.getName() + "!";
}
```

Obtaining the authentication from the context is even more comfortable at the endpoint level, as Spring knows to inject it directly into the method parameters:
```
@GetMapping("/hello")
public String hello(Authentication a) {
  return "Hello, " + a.getName() + "!";
}
```

### Using a holding strategy for asynchronous calls

The situation gets more complicated if we have to deal with multiple threads per request. Look at what happens if you make the endpoint asynchronous. The thread that executes the method is no longer the same thread that serves the request. Being ``@Async``, the method is executed on a separate thread.

```
@GetMapping("/bye")
@Async
public void goodbye() {
  SecurityContext context = SecurityContextHolder.getContext();
  String username = context.getAuthentication().getName();
  // do something with the username
}
```

If you try the code as it is now, it throws a ``NullPointerException`` on the line that gets the name from the authentication. In this case, you could solve the problem by using the ``MODE_INHERITABLETHREADLOCAL`` strategy. This can be set either by calling the ``SecurityContextHolder.setStrategyName()`` method or by using the system property ``spring.security.strategy``. By setting this strategy, the framework knows to copy the details of the original thread of the request to the newly created thread of the asynchronous method.

The next listing presents a way to set the security context management strategy by calling the ``setStrategyName()`` method:
```
@Configuration
@EnableAsync
public class ProjectConfig {

  @Bean
  public InitializingBean initializingBean() {
    return () -> SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
  }
}
```

Calling the endpoint, you will observe now that the security context is propagated correctly to the next thread by Spring. Additionally, Authentication is not null anymore.

### Using a holding strategy for standalone applications

If what you need is a security context shared by all the threads of the application, you change the strategy to ``MODE_GLOBAL``. You would not use this strategy for a web server as it doesn’t fit the general picture of the application. But this can be a good use for a standalone application. As the following code snippet shows, you can change the strategy in the same way we did with ``MODE_INHERITABLETHREADLOCAL``.

### Forwarding the security context with DelegatingSecurityContextRunnable

What happens when your code starts new threads without the framework knowing about them? Sometimes we name these self-managed threads because it is we who manage them, not the framework. In this section, we apply some utility tools provided by Spring Security that help you propagate the security context to newly created threads.

One solution for this is to use the ``DelegatingSecurityContextRunnable`` to decorate the tasks you want to execute on a separate thread. The ``DelegatingSecurityContextRunnable`` extends ``Runnable``. You can use it following the execution of the task when there is no value expected. If you have a return value, then you can use the ``Callable<T>`` alternative, which is ``DelegatingSecurityContextCallable<T>``. Both classes represent tasks executed asynchronously, as any other ``Runnable`` or ``Callable``.

Listing 5.11 presents the use of ``DelegatingSecurityContextCallable``. Let’s start by defining a simple endpoint method that declares a ``Callable`` object. The ``Callable`` task returns the username from the current security context.

```
@GetMapping("/ciao")
public String ciao() throws Exception {
  Callable<String> task = () -> {
    SecurityContext context = SecurityContextHolder.getContext();
    return context.getAuthentication().getName();
  };
  ExecutorService e = Executors.newCachedThreadPool();
  try {
    return "Ciao, " + e.submit(task).get() + "!";
  } finally {
    e.shutdown();
  }
}
```

If you run the application as is, you get nothing more than a NullPointerException. Inside the newly created thread to run the callable task, the authentication does not exist anymore, and the security context is empty. To solve this problem, we decorate the task with DelegatingSecurityContextCallable, which provides the current context to the new thread, as provided by this listing:
```
@GetMapping("/ciao")
public String ciao() throws Exception {
  Callable<String> task = () -> {
    SecurityContext context = SecurityContextHolder.getContext();
    return context.getAuthentication().getName();
  };
  ExecutorService e = Executors.newCachedThreadPool();
  try {
    var contextTask = new DelegatingSecurityContextCallable<>(task);
    return "Ciao, " + e.submit(contextTask).get() + "!";
  } finally {
    e.shutdown();
  }
}
```

Calling the endpoint now, you can observe that Spring propagated the security context to the thread in which the tasks execute:
```
curl -u user:2eb3f2e8-debd-420c-9680-48159b2ff905 http://localhost:8080/ciao
```

The response body for this call is:
```
Ciao, user!
```

### Forwarding the security context with DelegatingSecurityContextExecutorService

An alternative to decorating tasks is to use a particular type of Executor - ``DelegatingSecurityContextExecutorService``.

```
@GetMapping("/hola")
public String hola() throws Exception {
  Callable<String> task = () -> {
    SecurityContext context = SecurityContextHolder.getContext();
    return context.getAuthentication().getName();
  };

  ExecutorService e = Executors.newCachedThreadPool();
  e = new DelegatingSecurityContextExecutorService(e);
  try {
    return "Hola, " + e.submit(task).get() + "!";
  } finally {
    e.shutdown();
  }
}
```

If you need to implement security context propagation for a scheduled task, then you will be happy to hear that Spring Security also offers you a decorator named ``DelegatingSecurityContextScheduledExecutorService``. It decorates a ``ScheduledExecutorService``, allowing you to work with scheduled tasks.

## Understanding HTTP Basic and form-based login authentications

### Using and configuring HTTP Basic

For theoretical scenarios, the defaults that HTTP Basic authentication comes with are great. But in a more complex application, you might find the need to customize some of these settings. For example, you might want to implement a specific logic for the case in which the authentication process fails. You might even need to set some values on the response sent back to the client in this case.

```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();
    }
}
```

You can also call the httpBasic() method of the HttpSecurity instance with a parameter of type Customizer. This parameter allows you to set up some configurations related to the authentication method, for example, the realm name, as shown in listing 5.16. You can think about the realm as a protection space that uses a specific authentication method.

```
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic(c -> c.realmName("OTHER"));
        http.authorizeRequests().anyRequest().authenticated();
    }
```

The lambda expression used is, in fact, an object of type ``Customizer<HttpBasicConfigurer<HttpSecurity>>``. The parameter of type ``HttpBasicConfigurer<HttpSecurity>`` allows us to call the realmName() method to rename the realm. You can use cURL with the -v flag to get a verbose HTTP response in which the realm name is indeed changed. However, note that you’ll find the WWW-Authenticate header in the response only when the HTTP response status is 401 Unauthorized and not when the HTTP response status is 200 OK. Here’s the call to cURL:
```
curl -v http://localhost:8080/hello
```

The response of the call is:
```
...
< WWW-Authenticate: Basic realm="OTHER"
...
```

Also, by using a Customizer, we can customize the response for a failed authentication. You need to do this if the client of your system expects something specific in the response in the case of a failed authentication. You might need to add or remove one or more headers. Or you can have some logic that filters the body to make sure that the application doesn’t expose any sensitive data to the client.

To customize the response for a failed authentication, we can implement an AuthenticationEntryPoint. Its commence() method receives the HttpServlet- Request, the HttpServletResponse, and the AuthenticationException that cause the authentication to fail. Listing 5.17 demonstrates a way to implement the AuthenticationEntryPoint, which adds a header to the response and sets the HTTP status to 401 Unauthorized.

```
public class CustomEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) 
            throws IOException, ServletException {
        httpServletResponse.addHeader("message", "Luke, I'm your father");
        httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value());
    }
}
```

```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic(c -> c.authenticationEntryPoint(new CustomEntryPoint()));
        http.authorizeRequests().anyRequest().authenticated();
    }
}
```

```
curl -v http://localhost:8080/hello
```
The response of the call is:
```
...
< HTTP/1.1 401
< Set-Cookie: JSESSIONID=459BAFA7E0E6246A463AD19B07569C7B; Path=/; HttpOnly
< message: Luke, I am your father!
...
```

### Implementing authentication with form-based login

When developing a web application, you would probably like to present a userfriendly login form where the users can input their credentials. As well, you might like your authenticated users to be able to surf through the web pages after they logged in and to be able to log out. For a small web application, you can take advantage of the form-based login method.

To change the authentication method to form-based login, in the ``configure(HttpSecurity http)`` method of the configuration class, instead of httpBasic(), call the formLogin() method of the HttpSecurity parameter. The following listing presents this change.
```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin();
        http.authorizeRequests().anyRequest().authenticated();
    }
}
```

Even with this minimal configuration, Spring Security has already configured a login form, as well as a log-out page for your project (http://localhost:8080/logout). The ``formLogin()`` method returns an object of type ``FormLoginConfigurer<HttpSecurity>``, which allows us to work on customizations.
```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().defaultSuccessUrl("/home", true);
        http.authorizeRequests().anyRequest().authenticated();
    }
}
```

If you need to go even more in depth with this, using the ``AuthenticationSuccessHandler`` and ``AuthenticationFailureHandler`` objects offers a more detailed customization approach. These interfaces let you implement an object through which you can apply the logic executed for authentication.

```
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication)
            throws IOException {
        var authorities = authentication.getAuthorities();
        var auth = authorities.stream()
                .filter(a -> a.getAuthority().equals("read"))
                .findFirst();
        if (auth.isPresent()) {
            httpServletResponse.sendRedirect("/home");
        } else {
            httpServletResponse.sendRedirect("/error");
        }
    }
}
```

There are situations in practical scenarios when a client expects a certain format of the response in case of failed authentication. They may expect a different HTTP status code than 401 Unauthorized or additional information in the body of the response. The most typical case I have found in applications is to send a request identifier. This request identifier has a unique value used to trace back the request among multiple systems, and the application can send it in the body of the response in case of failed authentication. Another situation is when you want to sanitize the response to make sure that the application doesn’t expose sensitive data outside of the system.
```
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) {
        httpServletResponse.setHeader("failed", LocalDateTime.now().toString());
    }
}
```

To use the two objects, you need to register them in the configure() method on the FormLoginConfigurer object returned by the formLogin() method.
```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .successHandler(new CustomAuthenticationSuccessHandler())
                .failureHandler(new CustomAuthenticationFailureHandler());
        http.authorizeRequests().anyRequest().authenticated();
    }
}
```

If you try to do:
```
curl -u user:a9867de7-a6c4-4f4b-be34-b9ca48a6fa5b http://localhost:8080/hello -v
```

You'll get a:
```
...
HTTP/1.1 302
...
```

This response status code is how the application tells you that it is trying to do a redirect. Even if you have provided the right username and password, it won’t consider these and will instead try to send you to the login form as requested by the formLogin method.
```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .successHandler(new CustomAuthenticationSuccessHandler())
                .failureHandler(new CustomAuthenticationFailureHandler())
            .and()
                .httpBasic();
        http.authorizeRequests().anyRequest().authenticated();
    }
}
```

```
curl -u user:384e8f47-2e39-47fb-a776-619ab142782f http://localhost:8080/hello
hello
```

# Chapter 6. Hands-on: A small secured web application

This hands-on example helps you to have a better overview of how all the components we discussed so far work together in a real application.

## Project requirements and setup

In this section, we implement a small web application where the user, after successful authentication, can see a list of products on the main page. For our project, a database stores the products and users for this application. The passwords for each user are hashed with either bcrypt or scrypt. I chose two hashing algorithms to give us a reason to customize the authentication logic in the example. A column in the users table stores the encryption type. A third table stores the users’ authorities.

Figure 6.1 describes the authentication flow for this application. I have shaded the components that we’ll customize differently. For the others, we use the defaults provided by Spring Security. The AuthenticationFilter intercepts the request and then delegates the authentication responsibility to the AuthenticationManager, which uses the AuthenticationProvider to authenticate the request. It returns the details of a successfully authenticated call so that the AuthenticationFilter can store these in the SecurityContext.

![chapter-6-project-structure.PNG](pictures/chapter-6-project-structure.PNG)

AuthenticationProviderService class, which implements the Authentication- Provider interface. This implementation defines the authentication logic where it needs to call a UserDetailsService to find the user details from a database and the PasswordEncoder to validate if the password is correct. For this application, we create
a JpaUserDetailsService that uses Spring Data JPA to work with the database.

The main steps we take to implement this project are as follows:
* Set up the database
* Define user management
* Implement the authentication logic
* Implement the main page
* Run and test the application

The ``schema.sql`` file contains all the queries that create or alter the structure of the database, while the ``data.sql`` file stores all the queries that work with data.

``schema.sql``:
```
CREATE SCHEMA spring;

CREATE TABLE IF NOT EXISTS spring.user (
    id          INT             NOT NULL AUTO_INCREMENT,
    username    VARCHAR(45)     NOT NULL,
    password    TEXT            NOT NULL,
 -- Algorithm stories either BCRYPT or SCRYPT
    algorithm   VARCHAR(45)     NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS spring.authority (
    id          INT             NOT NULL AUTO_INCREMENT,
    name        VARCHAR(45)     NOT NULL,
    user_id     INT             NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS spring.product (
    id          INT             NOT NULL AUTO_INCREMENT,
    name        VARCHAR(45)     NOT NULL,
    price       DECIMAL(19,2)   NOT NULL,
    currency    VARCHAR(45)     NOT NULL,
    PRIMARY KEY (id)
);
```

**NOTE**. It is advisable to have a many-to-many relationship between the authorities and the users. To keep the example simpler from the point of view of the persistence layer and focus on the essential aspects of Spring Security, I decided to make this one-to-many.

``data.sql``:
```
INSERT INTO spring.user (id, username, password, algorithm)
VALUES (1, 'john', '$2a$10$/5P0VqbNr.KP5XbRwG7H2.da.yIv545Jya11ciRihjxuvMyPXHcnO', 'BCRYPT');

INSERT INTO spring.authority (id, `name`, user_id)
VALUES (1,'READ', '1'), (2,'WRITE', '1');

INSERT INTO spring.product (id, `name`, price, currency)
VALUES (1, 'Chocolate', 10.00, 'USD');
```

In this code snippet, for user John, the password is hashed using bcrypt. **The raw password is 12345.**

``pom.xml``:
```
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
        </dependency>

        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

``application.yaml``:
```
spring:
  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:security;DB_CLOSE_ON_EXIT=FALSE
    username: sa
    password:
    platform: h2
  jpa:
    hibernate:
      ddl-auto: none
    database: h2
    open-in-view: false
  h2:
    console:
      enabled: true
      path: /h2
  application:
    name: security

logging:
  level:
    org:
      springframework:
        security: DEBUG
```


## Implementing user management

In this section, we discuss implementing the user management part of the application. The representative component of user management in regards to Spring Security is the UserDetailsService. You need to implement at least this contract to instruct Spring Security how to retrieve the details of your users.

Now that we have a project in place and the database connection configured, it is time to think about the implementations related to application security. The steps we need to take to build this part of the application that takes care of the user management are as follows:
1 Define the password encoder objects for the two hashing algorithms.
2 Define the JPA entities to represent the user and authority tables that store the details needed in the authentication process.
3 Declare the JpaRepository contracts for Spring Data. In this example, we only need to refer directly to the users, so we declare a repository named UserRepository.
4 Create a decorator that implements the UserDetails contract over the User JPA entity. Here, we use the approach to separate responsibilities discussed in section 3.2.5.
5 Implement the UserDetailsService contract. For this, create a class named JpaUserDetailsService. This class uses the UserRepository we create in step 3 to obtain the details about users from the database. If JpaUserDetailsService finds the users, it returns them as an implementation of the decorator we define in step 4.

For user management, we need to declare a UserDetailsService implementation, which retrieves the user by its name from the database.
```
@Entity
@Table(schema = "spring", name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 45)
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Algorithm algorithm;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userEntity", cascade = CascadeType.ALL)
    private Set<AuthorityEntity> authorities = new HashSet<>();
    
    ...
}
```

```
public enum Algorithm {
    BCRYPT, SCRYPT;
}
```

```
@Entity
@Table(schema = "spring", name = "authority")
public class AuthorityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 45)
    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
    ...
}
```

```
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    @EntityGraph(attributePaths = {"authorities"})
    Optional<UserEntity> findUserEntitiesByUsername(String username);
}
```

```
public class CustomUserDetails implements UserDetails {

    private final UserEntity userEntity;

    public CustomUserDetails(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userEntity.getAuthorities().stream()
                         .map(authorityEntity -> new SimpleGrantedAuthority(authorityEntity.getName()))
                         .toList();
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    public Algorithm getAlgorithm() {
        return userEntity.getAlgorithm();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
```

You can now implement the UserDetailsService:
```
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;

    public CustomUserDetailsService(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userJpaRepository.findUserEntitiesByUsername(username)
                                .map(CustomUserDetails::new)
                                .orElseThrow(() -> new UsernameNotFoundException("Bad credentials. Hohoho"));
    }
}
```


## Implementing custom authentication logic

Having completed user and password management, we can begin writing custom authentication logic. To do this, we have to implement an ``AuthenticationProvider``.

```
@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SCryptPasswordEncoder sCryptPasswordEncoder;

    public CustomAuthenticationProvider(CustomUserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder,
                                        SCryptPasswordEncoder sCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.sCryptPasswordEncoder = sCryptPasswordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var username = authentication.getName();
        var password = authentication.getCredentials().toString();

        var userDetails = userDetailsService.loadUserByUsername(username);
        return switch (userDetails.getAlgorithm()) {
            case BCRYPT -> checkPassword(userDetails, password, bCryptPasswordEncoder);
            case SCRYPT -> checkPassword(userDetails, password, sCryptPasswordEncoder);
        };
    }

    private Authentication checkPassword(CustomUserDetails user, String rawPassword, PasswordEncoder encoder) {
        if (encoder.matches(rawPassword, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
        } else {
            throw new BadCredentialsException("Bad credentials. Hohoho");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
```
```
@Configuration
public class PasswordEncodersConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SCryptPasswordEncoder sCryptPasswordEncoder() {
        return new SCryptPasswordEncoder();
    }
}
```


```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthenticationProvider authenticationProvider;

    public ProjectConfig(CustomAuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
            .and()
            .formLogin();
        http.authorizeRequests().anyRequest().authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }
}
```

## My own implementation using Jdbc and DelegatingPasswordEncoder

schema.sql:
```
CREATE SCHEMA spring;

CREATE TABLE IF NOT EXISTS spring.user (
    id          INT             NOT NULL AUTO_INCREMENT,
    username    VARCHAR(45)     NOT NULL,
    password    TEXT            NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS spring.authority (
    id          INT             NOT NULL AUTO_INCREMENT,
    name        VARCHAR(45)     NOT NULL,
    user_id     INT             NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS spring.product (
    id          INT             NOT NULL AUTO_INCREMENT,
    name        VARCHAR(45)     NOT NULL,
    price       DECIMAL(19,2)   NOT NULL,
    currency    VARCHAR(45)     NOT NULL,
    PRIMARY KEY (id)
);
```

data.sql:
```
INSERT INTO spring.user (id, username, password)
VALUES (1, 'john', '{bcrypt}$2a$10$AxC8HJv26wuwbLCPl9acsetiloyaSNSremmFWBUeBIHDemBi1eQ7i');

INSERT INTO spring.authority (id, `name`, user_id)
VALUES (1,'READ', 1), (2,'WRITE', 1);

INSERT INTO spring.product (id, `name`, price, currency)
VALUES (1, 'Chocolate', 10.00, 'USD');
```

**The password is 12345**.

```
public record Authority(Long id, String name) {
}

public record User(Long id, String username, String password, Set<Authority> authorities) {
}
```

```
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.authorities().stream()
                   .map(authorityEntity -> new SimpleGrantedAuthority(authorityEntity.name()))
                   .toList();
    }

    @Override
    public String getPassword() {
        return user.password();
    }

    @Override
    public String getUsername() {
        return user.username();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
```

```
@Repository
public class UserJdbcRepo {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final String SELECT_SQL = "SELECT u.id, u.username, u.password, a.id AS a_id, a.name AS a_name FROM spring.user AS u INNER JOIN spring.authority AS a ON u.id = a.user_id";

    public UserJdbcRepo(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public Optional<User> findUser(String username) {
        try {
            var user = jdbcTemplate.query(SELECT_SQL + " WHERE u.username = :userName", Map.of("userName", username), UserJdbcRepo::extractUser);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private static User extractUser(ResultSet rs) throws SQLException {
        var authorities = new HashSet<Authority>();
        User user = null;
        while (rs.next()) {
            if (user == null) {
                var userId = rs.getLong("id");
                var username1 = rs.getString("username");
                var password = rs.getString("password");
                user = new User(userId, username1, password, authorities);
            }
            authorities.add(new Authority(rs.getLong("a_id"), rs.getString("a_name")));
        }
        return user;
    }
}
```

```
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserJdbcRepo userJdbcRepo;

    public CustomUserDetailsService(UserJdbcRepo userJdbcRepo) {
        this.userJdbcRepo = userJdbcRepo;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userJdbcRepo.findUser(username)
                           .map(CustomUserDetails::new)
                           .orElseThrow(() -> new UsernameNotFoundException("Bad credentials. Hohoho"));
    }
}
```

```
@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        var encoders = Map.of(
                "noop", NoOpPasswordEncoder.getInstance(),
                "bcrypt", new BCryptPasswordEncoder(),
                "scrypt", new SCryptPasswordEncoder());
        return new DelegatingPasswordEncoder("bcrypt", encoders);
    }
}
```

```
@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var username = authentication.getName();
        var password = authentication.getCredentials().toString();

        var userDetails = userDetailsService.loadUserByUsername(username);
        return checkPassword(userDetails, password);
    }

    private Authentication checkPassword(CustomUserDetails user, String rawPassword) {
        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
        } else {
            throw new BadCredentialsException("Bad credentials. Hohoho");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
```

```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthenticationProvider authenticationProvider;

    public ProjectConfig(CustomAuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
            .and()
            .formLogin();
        http.authorizeRequests().anyRequest().authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }
}
```

```
public record Product(Long id, String name, BigDecimal price, String currency) {

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }
}
```

```
@Repository
public class ProductRepo {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ProductRepo(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<Product> listProducts() {
        return jdbcTemplate.query("SELECT id, name, price, currency FROM spring.product", (rs, i) -> {
            var id = rs.getLong("id");
            var productName = rs.getString("name");
            var price = rs.getBigDecimal("price");
            var currency = rs.getString("currency");
            return new Product(id, productName, price, currency);
        });
    }
}
```

```
@RestController
public class HelloController {

    private final ProductRepo productsRepo;

    public HelloController(ProductRepo productRepo) {
        this.productsRepo = productRepo;
    }

    @GetMapping("products")
    public List<Product> listProducts() {
        return productsRepo.listProducts();
    }
}
```

# Chapter 7. Configuring authorization: Restricting access

Up to now, we’ve only discussed authentication, which is, as you learned, the process in which the application identifies the caller of a resource. In the examples we worked on in the previous chapters, we didn’t implement any rule to decide whether to approve a request. We only cared if the system knew the user or not. **Authorization** is the process during which the system decides if an identified client has permission to access the requested resource. 

In Spring Security, once the application ends the authentication flow, it delegates the request to an authorization filter.

![chapter-7-authorization-flow.PNG](pictures/chapter-7-authorization-flow.PNG)

## Restricting access based on authorities and roles

In chapter 3, you implemented the GrantedAuthority interface. I introduced this contract when discussing another essential component: the ``UserDetails`` interface. We didn’t work with ``GrantedAuthority`` then because, as you’ll learn in this chapter, this interface is mainly related to the authorization process.

An authority is an action that a user can perform with a system resource. An authority has a name that the ``getAuthority()`` behavior of the object returns as a String. We use the name of the authority when defining the custom authorization rule. Often an authorization rule can look like this: “Jane is allowed to *delete* the product records,” or “John is allowed to *read* the document records.” In these cases, delete and read are the granted authorities. The application allows the users Jane and John to perform these actions, which often have names like read, write, or delete.

```
public interface GrantedAuthority extends Serializable {
  String getAuthority();
}
```

### Restricting access for all endpoints based on user authorities

Up to now in our examples, any authenticated user could call any endpoint of the application. From now on, you’ll learn to customize this access. We start a new project that I name ssia-ch7-ex1. I show you three ways in which you can configure access as mentioned using these methods:
* ``hasAuthority()`` — Receives as parameters only one authority for which the application configures the restrictions. Only users having that authority can call the endpoint.
* ``hasAnyAuthority()`` — Can receive more than one authority for which the application configures the restrictions. I remember this method as “has any of the given authorities.” The user must have at least one of the specified authorities to make a request.
* ``access()`` — Offers you unlimited possibilities for configuring access because the application builds the authorization rules based on the Spring Expression Language (SpEL).

However, ``access()`` makes the code more difficult to read and debug. For this reason, **I recommend it as the lesser solution and only if you cannot apply the hasAnyAuthority() or hasAuthority() methods.**

The only dependencies needed in your pom.xml file are ``spring-boot-starterweb`` and ``spring-boot-starter-security``.

We also add an endpoint in the application to test our authorization configuration:
```
@RestController
public class HelloController {

    @GetMapping("hello")
    public String hello() {
        return "Hello";
    }
}
```

In a configuration class, we declare an InMemoryUserDetailsManager as our UserDetailsService and add two users, John and Jane, to be managed by this instance. Each user has a different authority.

```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {
    
    @Bean
    public UserDetailsService userDetailsService() {
        var manager = new InMemoryUserDetailsManager();
        var john = User
                .withUsername("john")
                .password("12345")
                .authorities("read")
                .build();
        var jane = User
                .withUsername("jane")
                .password("12345")
                .authorities("write")
                .build();
        manager.createUser(john);
        manager.createUser(jane);
        return manager;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
```

The next thing we do is add the authorization configuration.

```
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();

        http.authorizeRequests().anyRequest().permitAll();
    }
```

The ``authorizeRequests()`` method lets us continue with specifying authorization rules on endpoints. The ``anyRequest()`` method indicates that the rule applies to all the requests, regardless of the URL or HTTP method used. The ``permitAll()`` method allows access to all requests, authenticated or not.

Let’s say we want to make sure that only users having WRITE authority can access all endpoints. For our example, this means only Jane. We can achieve our goal and restrict access this time based on a user’s authorities.

```
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();

        http.authorizeRequests().anyRequest().hasAuthority("write");
    }
```

We can now start to test the application by calling the endpoint with each of the two users:
```
curl -u jane:12345 http://localhost:8080/hello
Hello

curl -u john:12345 http://localhost:8080/hello

{
  "status":403,
  "error":"Forbidden",
  "message":"Forbidden",
  "path":"/hello"
}
```

In a similar way, you can use the hasAnyAuthority() method. This method has the parameter varargs; this way, it can receive multiple authority names.

```
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();

        http.authorizeRequests().anyRequest().hasAnyAuthority("write", "read");
    }
```

Then:
```
curl -u jane:12345 http://localhost:8080/hello
Hello

curl -u john:12345 http://localhost:8080/hello
Hello
```

The ``access()`` method is not all evil. As I stated earlier, it offers you flexibility. You’ll find situations in real-world scenarios in which you could use it to write more complex expressions, based on which the application grants access. You wouldn’t be able to implement these scenarios without the access() method.

```
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();

        String expression = "hasAuthority('read') and !hasAuthority('delete')";
        http.authorizeRequests().anyRequest().access(expression);
    }
```

### Restricting access for all endpoints based on user roles

Roles are another way to refer to what a user can do (figure 7.5). You find these as well in realworld applications, so this is why it is important to understand roles and the difference between roles and authorities.

![chapter-7-roles.PNG](pictures/chapter-7-roles.PNG)

Some applications always provide the same groups of authorities to specific users. Imagine, in your application, a user can either only have read authority or have all: read, write, and delete authorities. In this case, it might be more comfortable to think that those users who can only read have a role named READER, while the others have the role ADMIN.


The names that you give to roles are like the names for authorities—it’s your own choice. We could say that roles are coarse grained when compared with authorities. Behind the scenes, anyway, roles are represented using the same contract in Spring Security, GrantedAuthority. **When defining a role, its name should start with the ROLE_ prefix.** At the implementation level, this prefix specifies the difference between a role and an authority.

```
    @Bean
    public UserDetailsService userDetailsService() {
        var manager = new InMemoryUserDetailsManager();
        var john = User
                .withUsername("john")
                .password("12345")
                .authorities("ROLE_ADMIN")
                .build();
        var jane = User
                .withUsername("jane")
                .password("12345")
                .authorities("ROLE_MANAGER")
                .build();
        manager.createUser(john);
        manager.createUser(jane);
        return manager;
    }
```

To set constraints for user roles, you can use one of the following methods:
* ``hasRole()`` — Receives as a parameter the role name for which the application authorizes the request.
* ``hasAnyRole()`` — Receives as parameters the role names for which the application approves the request.
* ``access()`` - Uses a Spring expression to specify the role or roles for which the application authorizes requests. In terms of roles, you could use hasRole() or hasAnyRole() as SpEL expressions.

We use these in the same way, but to apply configurations for roles instead of authorities. My recommendations are also similar: use the hasRole() or hasAnyRole() methods as your first option, and fall back to using access() only when the previous two don’t apply.

```
@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();

        http.authorizeRequests().anyRequest().hasRole("ADMIN");
    }
```
    
When testing the application, you should observe that user John can access the endpoint, while Jane receives an HTTP 403 Forbidden.

When building users with the User builder class as we did in the example for this section, you specify the role by using the roles() method. This method creates the GrantedAuthority object and automatically adds the ROLE_ prefix to the names you provide.
```
var user1 = User.withUsername("john")
                .password("12345")
                .roles("ADMIN")
                .build();

var user2 = User.withUsername("jane")
                .password("12345")
                .roles("MANAGER")
                .build();
```

### Restricting access to all endpoints

The ``denyAll()`` method is just the opposite of the ``permitAll()`` method. 

# Chapter 8. Configuring authorization: Applying restrictions

Even though we didn’t call attention to it, the first matcher method you used was the anyRequest() method. As you used it in the previous chapters, you know now that it refers to all requests, regardless of the path or HTTP method. First, let’s talk about selecting requests by path; then we can also add the HTTP method to the scenario. To choose the requests to which we apply authorization configuration, we use matcher methods. Spring Security offers you three types of matcher methods:
* **MVC matchers** — You use MVC expressions for paths to select endpoints.
* **Ant matchers** — You use Ant expressions for paths to select endpoints.
* **regex matchers** — You use regular expressions (regex) for paths to select endpoints.

## Using matcher methods to select endpoints

We create an application that exposes two endpoints: /hello and /ciao. We want to make sure that only users having the ADMIN role can call the /hello endpoint. Similarly, we want to make sure that only users having the MANAGER role can call the /ciao endpoint.

```
@RestController
public class HelloController {

    @GetMapping("hello")
    public String hello() {
        return "Hello";
    }
    
    @GetMapping("ciao")
    public String ciao() {
        return "Ciao";
    }
}
```

In the configuration class, we declare an InMemoryUserDetailsManager as our UserDetailsService instance and add two users with different roles. The user John has the ADMIN role, while Jane has the MANAGER role.

```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();

        http.authorizeRequests()
            .mvcMatchers("/hello").hasRole("ADMIN")
            .mvcMatchers("/ciao").hasRole("MANAGER");
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var manager = new InMemoryUserDetailsManager();
        var john = User.withUsername("john")
                .password("12345")
                .authorities("ROLE_ADMIN")
                .build();
        var jane = User.withUsername("jane")
                .password("12345")
                .authorities("ROLE_MANAGER")
                .build();
        manager.createUser(john);
        manager.createUser(jane);
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
```

```
curl -u john:12345 http://localhost:8080/hello
Hello!
curl -u jane:12345 http://localhost:8080/hello
{"timestamp":"2021-04-21T13:58:54.588+00:00","status":403,"error":"Forbidden","message":"","path":"/hello"}

curl -u jane:12345 http://localhost:8080/ciao
Ciao
curl -u john:12345 http://localhost:8080/ciao
{"timestamp":"2021-04-21T13:59:06.558+00:00","status":403,"error":"Forbidden","message":"","path":"/ciao"}
```

If you now add any other endpoint to your application, it is accessible by default to anyone, even unauthenticated users. Let’s assume you add a new endpoint /hola as presented in the next listing.

```
...
    @GetMapping("hola")
    public String hola() {
        return "Hola";
    }
...
```

```
curl -u john:12345 http://localhost:8080/hola
Hola
curl -u jane:12345 http://localhost:8080/hola
Hola
```

You can also make this explicit:
```
http.authorizeRequests()
            .mvcMatchers("/hello").hasRole("ADMIN")
            .mvcMatchers("/ciao").hasRole("MANAGER")
            .anyRequest().permitAll();
```

Also, it'll work if you'll call without authenticating:
```
curl http://localhost:8080/hola
Hola
```

However, if bad credentials are provided, then it'll fail. But it makes sense, because authentication fails, not authorization, and authentication happens first:
```
curl -u bod:abcd http://localhost:8080/hola
{"timestamp":"2021-04-21T14:30:53.550+00:00","status":401,"error":"Unauthorized","message":"","path":"/hola"}
```

But if:
```
http.authorizeRequests()
    .mvcMatchers("/hello").hasRole("ADMIN")
    .mvcMatchers("/ciao").hasRole("MANAGER")
    .anyRequest().denyAll();
```

Then, without user fails:
```
curl http://localhost:8080/hola
{"timestamp":"2021-04-21T14:32:06.946+00:00","status":401,"error":"Unauthorized","message":"","path":"/hola"}
```

Hence, always lock down your application. Better approach is to use ``authenticated()``:
```
http.authorizeRequests()
    .mvcMatchers("/hello").hasRole("ADMIN")
    .mvcMatchers("/ciao").hasRole("MANAGER")
    .anyRequest().authenticated();
```

Now we must go more in depth with the syntaxes you can use.

In most practical scenarios, multiple endpoints can have the same authorization rules, so you don’t have to set them up endpoint by endpoint. As well, you sometimes need to specify the HTTP method, not only the path, as we’ve done until now. Sometimes, you only need to configure rules for an endpoint when its path is called with HTTP GET. In this case, you’d need to define different rules for HTTP POST and HTTP DELETE. In the next sections, we take each type of matcher method and discuss these aspects in detail.

## Selecting requests for authorization using MVC matchers

This matcher uses the standard MVC syntax for referring to paths. This syntax is the same one you use when writing endpoint mappings with annotations like @RequestMapping, @GetMapping, @PostMapping, and so forth. The two methods you can use to declare MVC matchers are as follows:
* ``mvcMatchers(HttpMethod method, String... patterns)`` — Lets you specify both the HTTP method to which the restrictions apply and the paths. This method is useful if you want to apply different restrictions for different HTTP methods for the same path.
* ``mvcMatchers(String... patterns)`` — Simpler and easier to use if you only need to apply authorization restrictions based on paths. The restrictions can automatically apply to any HTTP method used with the path.

For a long time, CSRF was present in the OWASP Top 10 vulnerabilities. In chapter 10, we’ll discuss how Spring Security mitigates this vulnerability by using CSRF tokens. But to make things simpler for the current example and to be able to call all endpoints, including those exposed with POST, PUT, or DELETE, we need to disable CSRF protection in our configure() method:
```
http.csrf().disable();
```

We start by defining four endpoints to use in our tests:
* /a using the HTTP method GET
* /a using the HTTP method POST
* /a/b using the HTTP method GET
* /a/b/c using the HTTP method GET

```
@RestController
public class HelloController {

    @GetMapping("a")
    public String hello() {
        return "Works!";
    }

    @PostMapping("a")
    public String postHello() {
        return "Works!";
    }

    @GetMapping("a/b")
    public String ciao() {
        return "Works!";
    }

    @GetMapping("a/b/c")
    public String hola() {
        return "Works!";
    }
}
```

We also need a couple of users with different roles. To keep things simple, we continue using an InMemoryUserDetailsManager. In the next listing, you can see the definition of the UserDetailsService in the configuration class.

```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();
        http.csrf().disable();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var manager = new InMemoryUserDetailsManager();
        var john = User.withUsername("john")
                       .password("12345")
                       .roles("ADMIN")
                       .build();
        var jane = User.withUsername("jane")
                       .password("12345")
                       .roles("MANAGER")
                       .build();
        manager.createUser(john);
        manager.createUser(jane);
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
```

Let’s start with the first scenario. For requests done with an HTTP GET method for the /a path, the application needs to authenticate the user. For the same path, requests using an HTTP POST method don’t require authentication. The application denies all other requests.

```
http.authorizeRequests()
    .mvcMatchers(HttpMethod.GET, "a").authenticated()
    .mvcMatchers(HttpMethod.POST, "a").permitAll()
    .anyRequest().denyAll();
```

```
curl http://localhost:8080/a
{"timestamp":"2021-04-21T14:53:06.261+00:00","status":401,"error":"Unauthorized","message":"","path":"/a"}
curl -XPOST http://localhost:8080/a
Works!

curl -u john:12345 http://localhost:8080/a
Works!
curl -XPOST -u john:12345 http://localhost:8080/a
Works!
```

But user John isn’t allowed to call path /a/b, so authenticating with his credentials for this call generates a 403 Forbidden:
```
curl -u john:12345 -XGET http://localhost:8080/a/b
{"timestamp":"2021-04-21T14:54:57.891+00:00","status":403,"error":"Forbidden","message":"","path":"/a/b"}
```

With this example, you now know how to differentiate requests based on the HTTP method. But, what if multiple paths have the same authorization rules? Of course, we can enumerate all the paths for which we apply authorization rules, but if we have too many paths, this makes reading code uncomfortable. As well, we might know from the beginning that a group of paths with the same prefix always has the same authorization rules. We want to make sure that if a developer adds a new path to the same group, it doesn’t also change the authorization configuration. To manage these cases, we use **path expressions**.

For the current project, we want to ensure that the same rules apply for all requests for paths starting with /a/b. These paths in our case are /a/b and /a/b/c. To achieve this, we use the ** operator.

```
http.authorizeRequests()
    .mvcMatchers("/a/b/**").authenticated()
    .anyRequest().permitAll();
```

With the configuration given, you can call path /a without being authenticated, but for all paths prefixed with /a/b, the application needs to authenticate the user.

```
curl -XGET http://localhost:8080/a
Works!
curl -XGET http://localhost:8080/a/b
{"timestamp":"2021-04-21T15:03:46.950+00:00","status":401,"error":"Unauthorized","message":"","path":"/a/b"}
curl -XGET http://localhost:8080/a/b/c
{"timestamp":"2021-04-21T15:03:48.623+00:00","status":401,"error":"Unauthorized","message":"","path":"/a/b/c"}

curl -XGET -u john:12345 http://localhost:8080/a
Works!
curl -XGET -u john:12345 http://localhost:8080/a/b
Works!
curl -XGET -u john:12345 http://localhost:8080/a/b/c
Works!
```

As presented in the previous examples, the ** operator refers to any number of pathnames. You can use it as we have done in the last example so that you can match requests with paths having a known prefix. You can also use it in the middle of a path to refer to any number of pathnames or to refer to paths ending in a specific pattern
like ``/a/**/c``. Therefore, ``/a/**/c`` would not only match ``/a/b/c`` but also ``/a/b/d/c`` and ``a/b/c/d/e/c`` and so on.

If you only want to match one pathname, then you can use a single ``*``. For example, ``a/*/c`` would match ``a/b/c`` and ``a/d/c`` but not ``a/b/d/c``.

Let’s turn now to a more suitable example of what you have learned in this section. We have an endpoint with a path variable, and we want to deny all requests that use a value for the path variable that has anything else other than digits.

```
    @GetMapping("product/{code}")
    public String productCode(@PathVariable("code") String code) {
        return code;
    }
```

The next listing shows you how to configure authorization such that only calls that have a value containing only digits are always permitted, while all other calls are denied.

```
http.authorizeRequests()
    .mvcMatchers("/product/{code:^[0-9]*$}").permitAll()
    .anyRequest().denyAll();
```

```
curl http://localhost:8080/product/1
1
curl http://localhost:8080/product/hello
{"timestamp":"2021-04-21T15:11:05.508+00:00","status":401,"error":"Unauthorized","message":"","path":"/product/hello"}
C:\Users\BC6250>curl http://localhost:8080/product/12312312131321
12312312131321
```

![chapter-8-mvc-matcher.PNG](pictures/chapter-8-mvc-matcher.PNG)

## Selecting requests for authorization using Ant matchers

In this section, we discuss Ant matchers for selecting requests for which the application applies authorization rules. Because Spring borrows the MVC expressions to match paths to endpoints from Ant, the syntaxes that you can use with Ant matchers are the same as those that you saw in previous section. **But there’s a trick I’ll show you in this section.**

**Because of this, I recommend that you use MVC matchers rather than Ant matchers.** The three methods when using Ant matchers are:
* antMatchers(HttpMethod method, String patterns) — Allows you to specify both the HTTP method to which the restrictions apply and the Ant patterns that refer to the paths. This method is useful if you want to apply different restrictions for different HTTP methods for the same group of paths.
* antMatchers(String patterns) — Simpler and easier to use if you only need to apply authorization restrictions based on paths. The restrictions automatically apply for any HTTP method.
* antMatchers(HttpMethod method), which is the equivalent of ant Matchers(httpMethod, ``“/**”``) — Allows you to refer to a specific HTTP method disregarding the paths.

The way that you apply these is similar to the MVC matchers in the previous section. Also, the syntaxes we use for referring to paths are the same. So what is different then? The MVC matchers refer exactly to how your Spring application understands matching requests to controller actions. And, sometimes, multiple paths could be interpreted
by Spring to match the same action. My favorite example that’s simple but makes a significant impact in terms of security is the following: any path (let’s take, for example, /hello) to the same action can be interpreted by Spring if you append another / after the path. **In this case, /hello and /hello/ call the same method. If you use an MVC matcher and configure security for the /hello path, it automatically secures the /hello/ path with the same rules.** This is huge! A developer not knowing this and using Ant matchers could leave a path unprotected without noticing it. And this, as you can imagine, creates a major security breach for the application.

```
@RestController
public class HelloController {

    @GetMapping("hello")
    public String hello() {
        return "Works!";
    }
}
```

```
http.authorizeRequests()
    .mvcMatchers("/hello").authenticated()
    .anyRequest().denyAll();
```

```
curl http://localhost:8080/hello
{"timestamp":"2021-04-21T15:28:05.665+00:00","status":401,"error":"Unauthorized","message":"","path":"/hello"}
curl http://localhost:8080/hello/
{"timestamp":"2021-04-21T15:28:07.182+00:00","status":401,"error":"Unauthorized","message":"","path":"/hello/"}

curl -u john:12345 http://localhost:8080/hello/
Works!
```

All of these responses are what you probably expected. But let’s see what happens if we change the implementation to use Ant matchers. If you just change the configuration class to use an Ant matcher for the same expression, the result changes.

```
http.authorizeRequests()
    .antMatchers("/hello").authenticated();
```

```
curl http://localhost:8080/hello
{"timestamp":"2021-04-21T15:38:05.610+00:00","status":401,"error":"Unauthorized","message":"","path":"/hello"}
curl http://localhost:8080/hello/
Works!
```

But if you do:
```
http.authorizeRequests()
    .antMatchers("/hello").authenticated()
    .anyRequest().denyAll();
```

```
curl http://localhost:8080/hello
{"timestamp":"2021-04-21T15:39:13.998+00:00","status":401,"error":"Unauthorized","message":"","path":"/hello"}
curl http://localhost:8080/hello/
{"timestamp":"2021-04-21T15:39:16.039+00:00","status":401,"error":"Unauthorized","message":"","path":"/hello/"}
```

## Selecting requests for authorization using regex matchers

In some cases, however, you might have requirements that are more particular, and you cannot solve those with Ant and MVC expressions. An example of such a requirement could be this: “Deny all requests when paths contain specific symbols or characters.” For these scenarios, you need to use a more powerful expression like a regex.

You can use regexes to represent any format of a string, so they offer limitless possibilities for this matter. But they have the disadvantage of being difficult to read, even when applied to simple scenarios. For this reason, you might prefer to use MVC or Ant matchers and fall back to regexes only when you have no other option.

The two methods that you can use to implement regex matchers are as follows:
* ``regexMatchers(HttpMethod method, String regex)`` — Specifies both the HTTP method to which restrictions apply and the regexes that refer to the paths. This method is useful if you want to apply different restrictions for different HTTP methods for the same group of paths.
* ``regexMatchers(String regex)`` — Simpler and easier to use if you only need to apply authorization restrictions based on paths. The restrictions automatically apply for any HTTP method.

To prove how regex matchers work, let’s put them into action with an example: building an application that provides video content to its users. The application that presents the video gets its content by calling the endpoint /video/{country}/{language}. For the sake of the example, the application receives the country and language in two path variables from where the user makes the request. We consider that any authenticated user can see the video content if the request comes from the US, Canada, or the UK, or if they use English.

```
    @GetMapping("/video/{country}/{language}")
    public String video(@PathVariable String country, @PathVariable String language) {
        return "Video allowed for %s %s".formatted(country, language);
    }
```

We also add two users with different authorities to test the implementation.
```
    @Bean
    public UserDetailsService userDetailsService() {
        var manager = new InMemoryUserDetailsManager();
        var john = User.withUsername("john")
                       .password("12345")
                       .authorities("read")
                       .build();
        var jane = User.withUsername("jane")
                       .password("12345")
                       .authorities("read", "premium")
                       .build();
        manager.createUser(john);
        manager.createUser(jane);
        return manager;
    }
```

```
http.authorizeRequests()
    .regexMatchers(".*/(us|uk|ca)+/(en|fr).*").authenticated()
    .anyRequest().hasAuthority("premium");
```

```
curl http://localhost:8080/video/us/en
{"timestamp":"2021-04-21T15:48:54.027+00:00","status":401,"error":"Unauthorized","message":"","path":"/video/us/en"}
curl -u john:12345 http://localhost:8080/video/us/en
Video allowed for us en
curl -u john:12345 http://localhost:8080/video/us/fr
Video allowed for us fr
curl -u john:12345 http://localhost:8080/video/us/ltu
{"timestamp":"2021-04-21T15:49:14.941+00:00","status":403,"error":"Forbidden","message":"","path":"/video/us/ltu"}
curl -u jane:12345 http://localhost:8080/video/us/ltu
Video allowed for us ltu
```

Regexes are powerful tools. You can use them to refer to paths for any given requirement. But because regexes are hard to read and can become quite long, they should remain your last choice. For example, a regex used to match an email address might look like the one in the next code snippet. Can you easily read and understand it?

```
(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])
```

# Chapter 9. Implementing filters

You learned about a component we named the authentication filter, which delegates the authentication responsibility to the authentication manager. You learned as well that a certain filter takes care of authorization configuration after successful authentication. In Spring Security, in general, HTTP filters manage each responsibility that must be applied to the request. The filters form a chain of responsibilities.

In this chapter, we’ll discuss how you can customize filters that are part of the authentication and authorization architecture in Spring Security. For example, you might want to augment authentication by adding one more step for the user, like checking their email address or using a one-time password. You can, as well, add functionality referring to auditing authentication events. You’ll find various scenarios where applications use auditing authentication: from debugging purposes to identifying a user’s behavior.

Knowing how to customize the HTTP filter chain of responsibilities is a valuable skill. In practice, applications come with various requirements, where using default configurations doesn’t work anymore. You’ll need to add or replace existing components of the chain. With the default implementation, you use the HTTP Basic authentication
method, which allows you to rely on a username and password. But in practical scenarios, there are plenty of situations in which you’ll need more than this. Maybe you need to implement a different strategy for authentication, notify an external system about an authorization event, or simply log a successful or failed authentication that’s later used in tracing and auditing (figure 9.3).

![chapter-9-filter-chain.PNG](pictures/chapter-9-filter-chain.PNG)

## Implementing filters in the Spring Security architecture

You learned in the previous chapters that the authentication filter intercepts the request and delegates authentication responsibility further to the authorization manager. If we want to execute certain logic before authentication, we do this by inserting a filter before the authentication filter.

The filters in Spring Security architecture are typical HTTP filters. We can create filters by implementing the ``Filter`` interface from the ``javax.servlet`` package. As for any other HTTP filter, you need to override the ``doFilter()`` method to implement its logic. This method receives as parameters the ``ServletRequest``, ``ServletResponse``, and ``FilterChain``:
* ``ServletRequest`` — Represents the HTTP request. We use the ServletRequest object to retrieve details about the request.
* ``ServletResponse`` — Represents the HTTP response. We use the ServletResponse object to alter the response before sending it back to the client or further along the filter chain.
* ``FilterChain`` — Represents the chain of filters. We use the FilterChain object to forward the request to the next filter in the chain.

The filter chain represents a collection of filters with a defined order in which they act. Spring Security provides some filter implementations and their order for us. Among the provided filters
* ``BasicAuthenticationFilter`` takes care of HTTP Basic authentication, if present.
* ``CsrfFilter`` takes care of cross-site request forgery (CSRF) protection, which we’ll discuss in chapter 10.
* ``CorsFilter`` takes care of cross-origin resource sharing (CORS) authorization rules, which we’ll also discuss in chapter 10.

You don’t need to know all of the filters as you probably won’t touch these directly from your code, but you do need to understand how the filter chain works and to be aware of a few implementations. In this book, I only explain those filters that are essential to the various topics we discuss.

It is important to understand that an application doesn’t necessarily have instances of all these filters in the chain. For example, in chapters 2 and 3, you learned that you need to call the ``httpBasic()`` method of the ``HttpSecurity`` class if you want to use the HTTP Basic authentication method. What happens is that if you call the ``httpBasic()`` method, an instance of the ``BasicAuthenticationFilter`` is added to the chain.

![chapter-9-filter-order.PNG](pictures/chapter-9-filter-order.PNG)

## Adding a filter before an existing one in the chain

For our first custom filter implementation, let’s consider a trivial scenario. We want to make sure that any request has a header called Request-Id. We assume that our application uses this header for tracking requests and that this header is mandatory. At the same time, we want to validate these assumptions before the application performs authentication. The authentication process might involve querying the database or other resource-consuming actions that we don’t want the application to execute if the format of the request isn’t valid. To solve the current requirement only takes two steps:
* Implement the filter. Create a RequestValidationFilter class that checks that the needed header exists in the request.
* Add the filter to the filter chain. Do this in the configuration class, overriding the configure() method.

![chapter-9-request-validator-filter.PNG](pictures/chapter-9-request-validator-filter.PNG)

To accomplish step 1, implementing the filter, we define a custom filter. Inside the doFilter() method, we write the logic of the filter. In our example, we check if the Request-Id header exists. If it does, we forward the request to the next filter in the chain by calling the doFilter() method. If the header doesn’t exist, we set an HTTP status 400 Bad Request on the response without forwarding it to the next filter in the chain:

```
public class RequestValidationFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;
        var httpResponse = (HttpServletResponse) response;
        
        String requestId = httpRequest.getHeader("Request-Id");
        if (requestId == null || requestId.isBlank()) {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        filterChain.doFilter(request, response);
    }
}
```

To implement step 2, applying the filter within the configuration class, we use the addFilterBefore() method of the HttpSecurity object because we want the application to execute this custom filter before authentication. This method receives two parameters:
* An instance of the custom filter we want to add to the chain — In our example, this is an instance of the RequestValidationFilter class presented in listing 9.1.
* The type of filter before which we add the new instance — For this example, because the requirement is to execute the filter logic before authentication, we need to add our custom filter instance before the authentication filter. The class BasicAuthenticationFilter defines the default type of the authentication filter.

To make the example simpler, I use the permitAll() method to allow all unauthenticated requests.

```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new RequestValidationFilter(), BasicAuthenticationFilter.class)
            .authorizeRequests().anyRequest().permitAll();
    }
}
```

```
@RestController
public class HelloController {

    @GetMapping("hello")
    public String hello() {
        return "Hello!";
    }
}
```

You can now run and test the application. Calling the endpoint without the header generates a response with HTTP status 400 Bad Request.

```
curl -v http://localhost:8080/hello

...
HTTP/1.1 400
...

curl -H "Request-Id:nonsense" http://localhost:8080/hello
Hello!
```

## Adding a filter after an existing one in the chain

Let’s assume that you have to execute some logic after the authentication process. Examples for this could be notifying a different system after certain authentication events or simply for logging and tracing purposes. 

```
public class AuthenticationLoggingFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest request) {
            var requestId = request.getHeader("Request-Id");

            LOGGER.info("Successfully authenticated request with id " + requestId);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
```

```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new RequestValidationFilter(), BasicAuthenticationFilter.class)
            .addFilterAfter(new AuthenticationLoggingFilter(), BasicAuthenticationFilter.class)
            .authorizeRequests().anyRequest().permitAll();
    }
}
```

```
curl -H "Request-Id:12345" http://localhost:8080/hello
Hello!

INFO 5876 --- [nio-8080-exec-2] c.l.s.f.AuthenticationLoggingFilter: Successfully authenticated request with id 12345
```

## Adding a filter at the location of another in the chain

In this section, we discuss adding a filter at the location of another one in the filter chain. You use this approach especially when providing a different implementation for a responsibility that is already assumed by one of the filters known by Spring Security. A typical scenario is authentication.

Let’s assume that instead of the HTTP Basic authentication flow, you want to implement something different. Instead of using a username and a password as input credentials based on which the application authenticates the user, you need to apply another approach. Some examples of scenarios that you could encounter are
* Identification based on a static header value for authentication
* Using a symmetric key to sign the request for authentication
* Using a one-time password (OTP) in the authentication process

In our first scenario, identification based on a static key for authentication, the client sends a string to the app in the header of HTTP request, which is always the same. The application stores these values somewhere, most probably in a database or a secrets vault. Based on this static value, the application identifies the client.
This approach offers weak security related to authentication, but architects and developers often choose it in calls between backend applications for its simplicity. The implementations also execute fast because these don’t need to do complex calculations, as in the case of applying a cryptographic signature. This way, static keys used for authentication represent a compromise where developers rely more on the infrastructure level in terms of security and also don’t leave the endpoints wholly unprotected.

In our second scenario, using symmetric keys to sign and validate requests, both client and server know the value of a key (client and server share the key). The client uses this key to sign a part of the request (for example, to sign the value of specific headers), and the server checks if the signature is valid using the same key. The server can store individual keys for each client in a database or a secrets vault.

And finally, for our third scenario, using an OTP in the authentication process, the user receives the OTP via a message or by using an authentication provider app like Google Authenticator.

![chapter-9-otp-overriding-sprin-filter.PNG](pictures/chapter-9-otp-overriding-sprin-filter.PNG)

Let’s implement an example to demonstrate how to apply a custom filter. To keep the case relevant but straightforward, we focus on configuration and consider a simple logic for authentication. In our scenario, we have the value of a static key, which is the same for all requests. To be authenticated, the user must add the correct value of the static key in the Authorization header.

```
public class StaticKeyAuthenticationFilter implements Filter {

    private static final String AUTHORIZATION_KEY = "Yes this is authorization key";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest request && servletResponse instanceof HttpServletResponse response) {
            var authorization = request.getHeader("Authorization");
            if (AUTHORIZATION_KEY.equals(authorization)) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
    }
}
```

```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAt(new StaticKeyAuthenticationFilter(), BasicAuthenticationFilter.class)
            .authorizeRequests().anyRequest().permitAll();
    }
}
```

```
curl -H "Request-Id:nonsense" http://localhost:8080/hello
HTTP/1.1 401

curl -H "Authorization:Yes this is authorization key" http://localhost:8080/hello
Hello!
```

When adding a filter at a specific position, Spring Security does not assume it is the only one at that position. You might add more filters at the same location in the chain. In this case, Spring Security doesn’t guarantee in which order these will act. I tell you this again because I’ve seen many people confused by how this works. Some developers think that when you apply a filter at a position of a known one, it will be replaced. This is not the case! **We must make sure not to add filters that we don’t need to the chain.**

Observe that we don’t call the httpBasic() method from the HttpSecurity class, because we don’t want the BasicAuthenticationFilter instance to be added to the filter chain.

In this case, because we don’t configure a UserDetailsService, Spring Boot automatically configures one, as you learned in chapter 2. But in our scenario, you don’t need a UserDetailsService at all because the concept of the user doesn’t exist. o disable the configuration of the default UserDetailsService, you can use the exclude attribute of the @SpringBootApplication annotation on the main class like this:
```
@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
```

## Filter implementations provided by Spring Security

In this section, we discuss classes provided by Spring Security, which implement the Filter interface.

Spring Security offers a few abstract classes that implement the Filter interface and for which you can extend your filter definitions. These classes also add functionality your implementations could benefit from when you extend them. For example, you could extend the GenericFilterBean class, which allows you to use initialization parameters that you would define in a web.xml descriptor file where applicable. A more useful class that extends the GenericFilterBean is OncePerRequestFilter. When adding a filter to the chain, the framework doesn’t guarantee it will be called only once per request. OncePerRequestFilter, as the name suggests, implements logic to make sure that the filter’s doFilter() method is executed only one time per request.

If you need such functionality in your application, use the classes that Spring provides. But if you don’t need them, I’d always recommend you to go as simple as possible with your implementations. Too often, I’ve seen developers extending the GenericFilterBean class instead of implementing the Filter interface in functionalities that don’t require the custom logic added by the GenericFilterBean class. When asked why, it seems they don’t know. They probably copied the implementation as they found it in examples on the web.

To make it crystal clear how to use such a class, let’s write an example.

In listing 9.9, you find the change I made for the AuthenticationLoggingFilter class. Instead of implementing the Filter interface directly, as was the case in the example in section 9.3, now it extends the OncePerRequestFilter class. The method we override here is doFilterInternal().

```
public class AuthenticationLoggingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var requestId = request.getHeader("Request-Id");
        
        LOGGER.info("Successfully authenticated request with id " + requestId);

        filterChain.doFilter(request, response);
    }
}
```

A few short observations about the OncePerRequestFilter class that you might find useful: 
* It supports only HTTP requests, but that’s actually what we always use. The advantageis that it casts the types, and we directly receive the requests as HttpServletRequest and HttpServletResponse. Remember, with the Filter interface, we had to cast the request and the response. 
* You can implement logic to decide if the filter is applied or not. Even if you added thefilter to the chain, you might decide it doesn’t apply for certain requests. You set this by overriding the shouldNotFilter(HttpServletRequest) method. By default, the filter applies to all requests. 
* By default, a OncePerRequestFilter doesn’t apply to asynchronous requests orerror dispatch requests. You can change this behavior by overriding the methods shouldNotFilterAsyncDispatch() and shouldNotFilterErrorDispatch().

# Chapter 10. Applying CSRF protection and CORS

## Applying cross-site request forgery (CSRF) protection in applications

You have probably observed that in most of the examples up to now, we only implemented our endpoints with HTTP GET. Moreover, when we needed to configure HTTP POST, we also had to add a supplementary instruction to the configuration to disable CSRF protection. The reason why you can’t directly call an endpoint with HTTP POST is because of CSRF protection, which is enabled by default in Spring Security.

In this section, we discuss CSRF protection and when to use it in your applications. CSRF is a widespread type of attack, and applications vulnerable to CSRF can force users to execute unwanted actions on a web application after authentication. You don’t want the applications you develop to be CSRF vulnerable and allow attackers to trick your users into making unwanted actions.

### How CSRF protection works in Spring Security

In this section, we discuss how Spring Security implements CSRF protection. It is important to first understand the underlying mechanism of CSRF protection. I encounter many situations in which misunderstanding the way CSRF protection works leads developers to misuse it, either disabling it in scenarios where it should be enabled or the other way around. Like any other feature in a framework, you have to use it correctly to bring value to your applications.

As an example, consider this scenario (figure 10.1): you are at work, where you use a web tool to store and manage your files. With this tool, in a web interface you can add new files, add new versions for your records, and even delete them. You receive an email asking you to open a page for a specific reason. You open the page, but the page is blank or it redirects you to a known website. You go back to your work but observe that all your files are gone!

What happened? You were logged into the application so you could manage your files. When you add, change, or delete a file, the web page you interact with calls some endpoints from the server to execute these operations. When you opened the foreign page by clicking the unknown link in the email, that page called the server and executed actions on your behalf (it deleted your files). It could do that because you logged in previously, so the server trusted that the actions came from you. You might think that someone couldn’t trick you so easily into clicking a link from a foreign email or message, but trust me, this happens to a lot of people

![chapter-10-CSRF-attack.PNG](pictures/chapter-10-CSRF-attack.PNG)

CSRF attacks assume that a user is logged into a web application. They’re tricked by the attacker into opening a page that contains scripts that execute actions in the same application the user was working on.

Because the user has already logged in (as we’ve assumed from the beginning), the forgery code can now impersonate the user and do actions on their behalf.

How do we protect our users from such scenarios? What CSRF protection wants to ensure is that only the frontend of web applications can perform mutating operations (by convention, HTTP methods other than GET, HEAD, TRACE, or OPTIONS). Then, a foreign page, like the one in our example, can’t act on behalf of the user.

How can we achieve this? What you know for sure is that before being able to do any action that could change data, a user must send a request using HTTP GET to see the web page at least once. When this happens, the application generates a unique token. The application now accepts only requests for mutating operations (POST, PUT, DELETE, and so forth) that contain this unique value in the header. The application considers that knowing the value of the token is proof that it is the app itself making the mutating request and not another system. Any page containing mutating calls, like POST, PUT, DELETE, and so on, should receive through the response the CSRF token, and the page must use this token when making mutating calls.

The starting point of CSRF protection is a filter in the filter chain called CsrfFilter. The CsrfFilter intercepts requests and allows all those that use these HTTP methods: GET, HEAD, TRACE, and OPTIONS. For all other requests, the filter expects to receive a header containing a token. If this header does not exist or contains an incorrect token value, the application rejects the request and sets the status of the response to HTTP 403 Forbidden.

What is this token, and where does it come from? These tokens are nothing more than string values. You have to add the token in the header of the request when you use any method other than GET, HEAD, TRACE, or OPTIONS. If you don’t add the header containing the token, the application doesn’t accept the request

![chapter-10-CSRF-token.PNG](pictures/chapter-10-CSRF-token.PNG)

The CsrfFilter (figure 10.3) uses a component named CsrfTokenRepository to manage the CSRF token values that generate new tokens, store tokens, and eventually invalidate these. By default, the CsrfTokenRepository stores the token on the HTTP session and generates the tokens as random universally unique identifiers (UUIDs). In most cases, this is enough, but as you’ll learn in section 10.1.3, you can use your own implementation of CsrfTokenRepository if the default one doesn’t apply to the requirements you need to implement.

Let’s create an application that exposes two endpoints. We can call one of these with HTTP GET and the other with HTTP POST. As you know by now, you are not able to call endpoints with POST directly without disabling CSRF protection.

![chapter-10-csrf-token-repository.PNG](pictures/chapter-10-csrf-token-repository.PNG)

As you learn with this example, the CsrfFilter adds the generated CSRF token to the attribute of the HTTP request named ``_csrf``. If we know this, we know that after the CsrfFilter, we can find this attribute and take the value of the token from it. For this small application, we choose to add a custom filter after the CsrfFilter

You use this custom filter to print in the console of the application the CSRF token that the app generates when we call the end point using HTTP GET. We can then copy the value of the token from the console and use it to make the mutating call with HTTP POST.

```
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String getHello() {
        return "Get Hello!";
    }
    @PostMapping("/hello")
    public String postHello() {
        return "Post Hello!";
    }
}
```

```
public class CsrfTokenLogger implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsrfTokenLogger.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        var csrf = request.getAttribute("_csrf");
        var token = (CsrfToken) csrf;

        LOGGER.info("CSRF token " + token.getToken());

        filterChain.doFilter(request, response);
    }
}
```

```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(new CsrfTokenLogger(), CsrfFilter.class)
            .authorizeRequests().anyRequest().permitAll();
    }
}
```

Observe that I don’t disable CSRF protection in the listing.

We can now test the endpoints. We begin by calling the endpoint with HTTP GET. Because the default implementation of the CsrfTokenRepository interface uses the HTTP session to store the token value on the server side, we also need to remember the session ID. For this reason, I add the -v flag to the call so that I can see more details from the response, including the session ID. Calling the endpoint
```
curl -v http://localhost:8080/hello
```
returns this (truncated) response:
```
... < Set-Cookie: JSESSIONID=21ADA55E10D70BA81C338FFBB06B0206; ... Get Hello!
```
Following the request in the application console, you can find a log line that contains the CSRF token:
```
INFO 21412 --- [nio-8080-exec-1] c.l.ssia.filters.CsrfTokenLogger : CSRF token c5f0b3fa-2cae-4ca8-b1e6-6d09894603df
```
If you call the endpoint using the HTTP POST method without providing the CSRF token, the response status is 403 Forbidden, as this command line shows:
```
curl -XPOST http://localhost:8080/hello
```
```
{ 
    "status":403, 
    "error":"Forbidden", 
    "message":"Forbidden", 
    "path":"/hello" 
}
```

But if you provide the correct value for the CSRF token, the call is successful. You also need to specify the session ID (JSESSIONID) because the default implementation of CsrfTokenRepository stores the value of the CSRF token on the session:
```
curl -X POST http://localhost:8080/hello -H 'Cookie: JSESSIONID=21ADA55E10D70BA81C338FFBB06B0206' -H 'X-CSRF-TOKEN: 1127bfda-57b1-43f0-bce5-bacd7d94694e'

Post Hello!
```

Side note. When using Windows, use ``"`` instead of ``'``. 

### Using CSRF protection in practical scenarios

In this section, we discuss applying CSRF protection in practical situations. Now that you know how CSRF protection works in Spring Security, you need to know where you should use it in the real world.

You use CSRF protection for web apps running in a browser, where you should expect that mutating operations can be done by the browser that loads the displayed content of the app. The most basic example I can provide here is a simple web application developed on the standard Spring MVC flow. We already made such an application when discussing form login in chapter 5, and that web app actually used CSRF protection. Did you notice that the login operation in that application used HTTP POST? Then why didn’t we need to do anything explicitly about CSRF in that case? The reason why we didn’t observe this was because we didn’t develop any mutating operation within it ourselves.

For the default login, Spring Security correctly applies CSRF protection for us. The framework takes care of adding the CSRF token to the login request. Let’s now develop a similar application to look closer at how CSRF protection works: 
* Build an example of a web application with the login form
* Look at how the default implementation of the login uses CSRF tokens
* Implement an HTTP POST call from the main page

Spring Boot project. You can find this example in the project ssia-ch10-ex2. The next code snippet presents the needed dependencies:
```
<dependency> 
    <groupId>org.springframework.boot</groupId> 
    <artifactId>spring-boot-starter-security</artifactId> 
</dependency> 

<dependency> 
    <groupId>org.springframework.boot</groupId> 
    <artifactId>spring-boot-starter-thymeleaf</artifactId> 
</dependency> 

<dependency> 
    <groupId>org.springframework.boot</groupId> 
    <artifactId>spring-boot-starter-web</artifactId> 
</dependency>
```

Then we need, of course, to configure the form login and at least one user. The following listing presents the configuration class, which defines the UserDetailsService, adds a user, and configures the formLogin method.

```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService uds() {
        var uds = new InMemoryUserDetailsManager();
        var u1 = User.withUsername("mary")
                     .password("12345")
                     .authorities("READ")
                     .build();
        uds.createUser(u1);
        return uds;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated();
        http.formLogin()
            .defaultSuccessUrl("/main", true);
    }
}
```

We add a controller class for the main page in a package named controllers and in a main.html file in the resources/templates folder of the Maven project. The main.html file can remain empty for the moment because on first execution of the application, we only focus on how the login page uses the CSRF tokens.

```
@Controller
public class MainController {
  
  @GetMapping("/main")
  public String main() {
    return "main.html";
  }
}
```

After running the application, you can access the default login page. If you inspect the form using the inspect element function of your browser, you can observe that the default implementation of the login form sends the CSRF token. This is why your login works with CSRF protection enabled even if it uses an HTTP POST request!

![chapter-10-figure-10-6.PNG](pictures/chapter-10-figure-10-6.PNG)

But what about developing our own endpoints that use POST, PUT, or DELETE as HTTP methods? For these, we have to take care of sending the value of the CSRF token if CSRF protection is enabled. To test this, let’s add an endpoint using HTTP POST to our application. We call this endpoint from the main page, and we create a second controller for this, called ProductController. Within this controller, we define an endpoint, /product/add, that uses HTTP POST.

```
@Controller
@RequestMapping("/product")
public class ProductController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    
    @PostMapping("/add")
    public String add(@RequestParam String name) {
        LOGGER.info("Adding product " + name);
        return "main.html";
    }
}
```

The endpoint receives a request parameter and prints it in the application console. The following listing shows the definition of the form defined in the main.html file.

After running the application, you can access the default login page. If you inspect the form using the inspect element function of your browser, you can observe that the default implementation of the login form sends the CSRF token. This is why your login works with CSRF protection enabled even if it uses an HTTP POST request!

But what about developing our own endpoints that use POST, PUT, or DELETE as HTTP methods? For these, we have to take care of sending the value of the CSRF token if CSRF protection is enabled. To test this, let’s add an endpoint using HTTP POST to our application. We call this endpoint from the main page, and we create a second controller for this, called ProductController. Within this controller, we define an endpoint, /product/add, that uses HTTP POST.

Now you can rerun the application and test the form. What you’ll observe is that when submitting the request, a default error page is displayed, which confirms an HTTP 403 Forbidden status on the response from the server (figure 10.7). The reason for the HTTP 403 Forbidden status is the absence of the CSRF token.

![chapter-10-figure-10-7.PNG](pictures/chapter-10-figure-10-7.PNG)

To solve this problem and make the server allow the request, we need to add the CSRF token in the request done through the form. An easy way to do this is to use a hidden input component, as you saw in the default form login.

![chapter-10-figure-10-8.PNG](pictures/chapter-10-figure-10-8.PNG)

After rerunning the application, you can test the form again. This time the server accepts the request, and the application prints the log line in the console, proving that the execution succeeds. Also, if you inspect the form, you can find the hidden input with the value of the CSRF token (figure 10.8).

![chapter-10-figure-10-8-again.PNG](pictures/chapter-10-figure-10-8-again.PNG)

CSRF tokens work well in an architecture where the same server is responsible for both the frontend and the backend, mainly for its simplicity. But CSRF tokens don’t work well when the client is independent of the backend solution it consumes. This scenario happens when you have a mobile application as a client or a web frontend developed independently. A web client developed with a framework like Angular, ReactJS, or Vue.js is ubiquitous in web application architectures, and this is why you need to know how to implement the security approach for these cases as well. We’ll discuss these kinds of designs in chapters 11 to 15.

### Customizing CSRF protection

In this section, the examples let you apply the most frequently encountered needs in customization of the CSRF protection mechanism. These are 
* Configuring paths for which CSRF applies
* Managing CSRF tokens

We use CSRF protection only when the page that consumes resources produced by the server is itself generated by the same server.

By default, CSRF protection applies to any path for endpoints called with **HTTP methods other than GET, HEAD, TRACE, or OPTIONS.**

```
@RestController
public class HelloController {
    
    @PostMapping("/hello") //Remains under CSRF protection
    public String postHello() {
        return "Post Hello!";
    }
    @PostMapping("/ciao")  //Not protected
    public String postCiao() {
        return "Post Ciao";
    }
}
```

```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    protected void configure(HttpSecurity http) throws Exception {
        http.csrf(c -> c.ignoringAntMatchers("/ciao"));
        http.authorizeRequests()
            .anyRequest().permitAll();
    }
}
```

The next code snippet shows how to use the ignoringRequestMatchers() method with an MvcRequestMatcher instead of using ignoringAntMatchers():
```
HandlerMappingIntrospector i = new HandlerMappingIntrospector(); 
MvcRequestMatcher r = new MvcRequestMatcher(i, "/ciao"); 
c.ignoringRequestMatchers(r);
```

Or, you can similarly use a regex matcher as in the next code snippet:
```
String pattern = ".*[0-9].*"; 
String httpMethod = HttpMethod.POST.name(); 
RegexRequestMatcher r = new RegexRequestMatcher(pattern, httpMethod); 
c.ignoringRequestMatchers(r);
```

Another need often found in the requirements of the application is customizing the management of CSRF tokens. As you learned, by default, the application stores CSRF tokens in the HTTP session on the server side. This simple approach is suitable for small applications, but it’s not great for applications that serve a large number of requests and that require horizontal scaling. The HTTP session is stateful and reduces the scalability of the application.

Let’s suppose you want to change the way the application manages tokens and store them somewhere in a database rather than in the HTTP session. Spring Security offers two contracts that you need to implement to do this: 
* CsrfToken — Describes the CSRF token itself
* CsrfTokenRepository — Describes the object that creates, stores, and loadsCSRF tokens

The CsrfToken object has three main characteristics that you need to specify when implementing the contract: 
* The name of the header in the request that contains the value of the CSRFtoken (default named X-CSRF-TOKEN) 
* The name of the attribute of the request that stores the value of the token(default named _csrf) 
* The value of the token
```
public interface CsrfToken extends Serializable { 
    String getHeaderName(); 
    String getParameterName(); 
    String getToken(); 
}
```

Generally, you only need the instance of the CsrfToken type to store the three details in the attributes of the instance. For this functionality, Spring Security offers an implementation called DefaultCsrfToken that we also use in our example. DefaultCsrfToken implements the CsrfToken contract and creates immutable instances containing the required values: the name of the request attribute and header, and the token itself.

CsrfTokenRepository is responsible for managing CSRF tokens in Spring Security. The interface CsrfTokenRepository is the contract that represents the component that manages CSRF tokens. To change the way the application manages the tokens, you need to implement the CsrfTokenRepository interface, which allows you to plug your custom implementation into the framework. Let’s change the current application we use in this section to add a new implementation for CsrfTokenRepository, which stores the tokens in a database.

![chapter-10-figure-10-10.PNG](pictures/chapter-10-figure-10-10.PNG)

In our example, we use a table in a database to store CSRF tokens. We assume the client has an ID to identify themselves uniquely. The application needs this identifier to obtain the CSRF token and validate it. Generally, this unique ID would be obtained during login and should be different each time the user logs in. This strategy of managing tokens is similar to storing them in memory. In this case, you use a session ID. So the new identifier for this example merely replaces the session ID.

An alternative to this approach would be to use CSRF tokens with a defined lifetime. With such an approach, tokens expire after a time you define. You can store tokens in the database without linking them to a specific user ID. You only need to check if a token provided via an HTTP request exists and is not expired to decide whether you allow that request.

To make our example shorter, we only focus on the implementation of the CsrfTokenRepository, and we need to consider that the client already has a generated identifier.

To allow the application to create the needed table in the database at the start, you can add the schema.xml file in the resources folder of the project.

```
CREATE TABLE IF NOT EXISTS `spring`.`token` (
`id` INT NOT NULL AUTO_INCREMENT,
`identifier` VARCHAR(45) NULL,
`token` TEXT NULL,
PRIMARY KEY (`id`));
```

We use Spring Data with a JPA implementation to connect to the database, so we need to define the entity class and the JpaRepository class.

```
@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String identifier;
    private String token;
    // Omitted code
}
```

```
public interface JpaTokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findTokenByIdentifier(String identifier);
}
```

With access to the implemented database, we can now start to write the CsrfTokenRepository implementation, which I call CustomCsrfTokenRepository.

```
public class CustomCsrfTokenRepository implements CsrfTokenRepository {

    @Autowired
    private JpaTokenRepository jpaTokenRepository;

    @Override
    public CsrfToken generateToken(HttpServletRequest httpServletRequest) {
        String uuid = UUID.randomUUID().toString();
        return new DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", uuid);
    }

    @Override
    public void saveToken(CsrfToken csrfToken, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String identifier = httpServletRequest.getHeader("X-IDENTIFIER");
        Optional<Token> existingToken = jpaTokenRepository.findTokenByIdentifier(identifier);
        if (existingToken.isPresent()) {
            Token token = existingToken.get();
            token.setToken(csrfToken.getToken());
        } else {
            Token token = new Token();
            token.setToken(csrfToken.getToken());
            token.setIdentifier(identifier);
            jpaTokenRepository.save(token);
        }
    }

    @Override
    public CsrfToken loadToken(HttpServletRequest httpServletRequest) {
        String identifier = httpServletRequest.getHeader("X-IDENTIFIER");

        return jpaTokenRepository.findTokenByIdentifier(identifier)
                          .map(token -> new DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", token.getToken()))
                          .orElse(null);
    }
}
```
```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public CsrfTokenRepository customTokenRepository() {
        return new CustomCsrfTokenRepository();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf(c -> {
            c.csrfTokenRepository(customTokenRepository());
            c.ignoringAntMatchers("/ciao");
        });
        http.authorizeRequests()
            .anyRequest().permitAll();
    }
}
```

The CSRF protection mechanism calls the generateToken() method when the application needs to generate a new token.

We use the UUID class to generate a new random UUID value, and we keep the same names for the request header and attribute, X-CSRF-TOKEN and _csrf, as in the default implementation offered by Spring Security.

The saveToken() method saves a generated token for a specific client. In the case of the default CSRF protection implementation, the application uses the HTTP session to identify the CSRF token. In our case, we assume that the client has a unique identifier. The client sends the value of its unique ID in the request with the header named X-IDENTIFIER. In the method logic, we check whether the value exists in the database. If it exists, we update the database with the new value of the token. If not, we create a new record for this ID with the new value of the CSRF token.

In the definition of the controller class presented in listing 10.9, we also add an endpoint that uses the HTTP GET method. We need this method to obtain the CSRF token when testing our implementation:
```
@GetMapping("/hello") 
public String getHello() { 
    return "Get Hello!"; 
}
```

You can now start the application and test the new implementation for managing the token. We call the endpoint using HTTP GET to obtain a value for the CSRF token. When making the call, we have to use the client’s ID within the X-IDENTIFIER header, as assumed from the requirement. A new value of the CSRF token is generated and stored in the database. Here’s the call:
```
curl -H "X-IDENTIFIER:12345" http://localhost:8080/hello 
Get Hello!
```

If you search the token table in the database, you find that the application added a new record for the client with identifier 12345. In my case, the generated value for the CSRF token, which I can see in the database, is 2bc652f5-258b-4a26-b456928e9bad71f8. We use this value to call the /hello endpoint with the HTTP POST method, as the next code snippet presents. Of course, we also have to provide the client ID that’s used by the application to retrieve the token from the database to compare with the one we provide in the request.
```
curl -XPOST -H "X-IDENTIFIER:12345" -H "X-CSRF-TOKEN:2bc652f5-258b-4a26-b456928e9bad71f8" http://localhost:8080/hello 
Post Hello!
```

![chapter-10-figure-10-11.PNG](pictures/chapter-10-figure-10-11.PNG)

If we try to call the /hello endpoint with POST without providing the needed headers, we get a response back with the HTTP status 403 Forbidden.

## Using cross-origin resource sharing

In this section, we discuss cross-origin resource sharing (CORS) and how to apply it with Spring Security. First, what is CORS and why should you care? The necessity for CORS came from web applications. By default, browsers don’t allow requests made for any domain other than the one from which the site is loaded. For example, if you access the site from example.com, the browser won’t let the site make requests to api.example.com.

We can briefly say that a browser uses the CORS mechanism to relax this strict policy and allow requests made between different origins in some conditions. You need to know this because it’s likely you will have to apply it to your applications, especially nowadays where the frontend and backend are separate applications. It is common that a frontend application is developed using a framework like Angular, ReactJS, or Vue and hosted at a domain like example.com, but it calls endpoints on the backend hosted at another domain like api.example.com.

### How does CORS work?

In this section, we discuss how CORS applies to web applications. If you are the owner of example.com, for example, and for some reason the developers from example.org decide to call your REST endpoints from their website, they won’t be able to. The same situation can happen if a domain loads your application using an iframe, for example (see figure 10.13).

![chapter-10-figure-10-13.PNG](pictures/chapter-10-figure-10-13.PNG)

Any situation in which an application makes calls between two different domains is prohibited. But, of course, you can find cases in which you need to make such calls. In these situations, CORS allows you to specify from which domain your application allows requests and what details can be shared. The CORS mechanism works based on HTTP headers (figure 10.14). The most important are 
* Access-Control-Allow-Origin — Specifies the foreign domains (origins)that can access resources on your domain. 
* Access-Control-Allow-Methods — Lets us refer only to some HTTP methodsin situations in which we want to allow access to a different domain, but only to specific HTTP methods. You use this if you’re going to enable example. com to call some endpoint, but only with HTTP GET, for example. 
* Access-Control-Allow-Headers — Adds limitations to which headers you can use in a specific request.

![chapter-10-figure-10-14.PNG](pictures/chapter-10-figure-10-14.PNG)

With Spring Security, by default, none of these headers are added to the response. So let’s start at the beginning: what happens when you make a cross-origin call if you don’t configure CORS in your application. When the application makes the request, it expects that the response has an Access-Control-Allow-Origin header containing the origins accepted by the server. If this doesn’t happen, as in the case of default Spring Security behavior, the browser won’t accept the response. Let’s demonstrate this with a small web application.

We define a controller class having an action for the main page and a REST endpoint. Because the class is a normal Spring MVC @Controller class, we also have to add the @ResponseBody annotation explicitly to the endpoint.

```
@Controller
public class MainController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @GetMapping("/")
    public String main() {
        return "main.html";
    }

    @PostMapping("/test")
    @ResponseBody
    public String test() {
        LOGGER.info("Test method called");
        return "Hello";
    }
}
```

Further, we need to define the configuration class where we disable CSRF protection to make the example simpler and allow you to focus only on the CORS mechanism.

```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
            .anyRequest().permitAll();
    }
}
```

Of course, we also need to define the main.html file in the resources/templates folder of the project. The main.html file contains the JavaScript code that calls the /test endpoint. To simulate the cross-origin call, we can access the page in a browser using the domain localhost. From the JavaScript code, we make the call using the IP address 127.0.0.1. Even if localhost and 127.0.0.1 refer to the same host, the browser sees these as different strings and considers these different domains.

```
<!DOCTYPE HTML>
<html lang="en">
    <head>
        <script>
            const http = new XMLHttpRequest();
            const url='http://127.0.0.1:8080/test';
            http.open("POST", url);
            http.send();
            http.onreadystatechange = (e) => {document.getElementById("output").innerHTML = http.responseText;}
        </script>
    </head>
    <body>
        <div id="output"></div>
    </body>
</html>
```

Starting the application and opening the page in a browser with localhost:8080, we can observe that the page doesn’t display anything. We expected to see HELLO on the page because this is what the /test endpoint returns. When we check the browser console, what we see is an error printed by the JavaScript call. The error looks like this:

![chapter-10-cross-origin-error.PNG](pictures/chapter-10-cross-origin-error.PNG)

The error message tells us that the response wasn’t accepted because the AccessControl-Allow-Origin HTTP header doesn’t exist. This behavior happens because we didn’t configure anything regarding CORS in our Spring Boot application, and by default, it doesn’t set any header related to CORS. So the browser’s behavior of not displaying the response is correct. **I would like you, however, to notice that in the application console, the log proves the method was called.**
```
INFO 25020 --- [nio-8080-exec-2] c.l.s.controllers.MainController : Test method called
```

This aspect is important! I meet many developers who understand CORS as a restriction similar to authorization or CSRF protection. Instead of being a restriction, CORS helps to relax a rigid constraint for cross-domain calls. **And even with restrictions applied, in some situations, the endpoint can be called.** This behavior doesn’t always happen. Sometimes, the browser first makes a call using the HTTP OPTIONS method to test whether the request should be allowed. We call this test request a preflight request. If the preflight request fails, the browser won’t attempt to honor the original request.

The preflight request and the decision to make it or not are the responsibility of the browser. You don’t have to implement this logic. But it is important to understand it, so you won’t be surprised to see cross-origin calls to the backend even if you did not specify any CORS policies for specific domains.

In our example, the browser makes the request, but we don’t accept the response if the origin is not specified in the response

**The CORS mechanism is, in the end, related to the browser and not a way to secure endpoints.** The only thing it guarantees is that only origin domains that you allow can make requests from specific pages in the browser.

### Applying CORS policies with the @CrossOrigin annotation

In this section, we discuss how to configure CORS to allow requests from different domains using the @CrossOrigin annotation. You can place the @CrossOrigin annotation directly above the method that defines the endpoint and configure it using the allowed origins and methods. As you learn in this section, the advantage of using the @CrossOrigin annotation is that it makes it easy to configure CORS for each endpoint.

To make the cross-origin call work in the application, the only thing you need to do is to add the @CrossOrigin annotation over the test() method in the controller class.

```
    @PostMapping("/test")
    @CrossOrigin("http://localhost:8080")
    @ResponseBody
    public String test() {
        LOGGER.info("Test method called");
        return "Hello";
    }
```

You can rerun and test the application. This should now display on the page the string returned by the /test endpoint: HELLO.

The value parameter of @CrossOrigin receives an array to let you define multiple origins; for example, @CrossOrigin({"example.com", "example.org"}). You can also set the allowed headers and methods using the allowedHeaders attribute and the methods attribute of the annotation. For both origins and headers, you can use the asterisk (*) to represent all headers or all origins. But I recommend you exercise caution with this approach. It’s always better to filter the origins and headers that you want to allow and never allow any domain to implement code that accesses your applications’ resources.

By allowing all origins, you expose the application to cross-site scripting (XSS) requests, which eventually can lead to DDoS attacks.

The advantage of using @CrossOrigin to specify the rules directly where the endpoints are defined is that it creates good transparency of the rules. The disadvantage is that it might become verbose, forcing you to repeat a lot of code. It also imposes the risk that the developer might forget to add the annotation for newly implemented endpoints.

### Applying CORS using a CorsConfigurer

```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors(c -> {
            CorsConfigurationSource source = request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of("example.com", "example.org"));
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                return config;
            };
            c.configurationSource(source);
        });
        
        http.csrf().disable();
        http.authorizeRequests()
            .anyRequest().permitAll();
    }
}
```

The cors() method that we call from the HttpSecurity object receives as a parameter a Customizer<CorsConfigurer> object. For this object, we set a CorsConfigurationSource, which returns CorsConfiguration for an HTTP request. CorsConfiguration is the object that states which are the allowed origins, methods, and headers. If you use this approach, you have to specify at least which are the origins and the methods. If you only specify the origins, your application won’t allow the requests. This behavior happens because a CorsConfiguration object doesn’t define any methods by default.

# Chapter 11. Hands-on: A separation of responsibilities

In this chapter, we’ll design a system of three actors: the client, the authentication server, and the business logic server. From these three actors, we’ll implement the backend part of the authentication server and a business logic server. As you’ll observe, our examples are more complex. This is a sign that we are getting closer and closer to real-world scenarios.

## The scenario and requirements of the example

You’ll find these components illustrated in figure 11.1. The three components are
* The **client** — This is the application consuming the backend. It could be a mobileapp or the frontend of a web application developed using a framework like Angular, ReactJS, or Vue.js. We don’t implement the client part of the system, but keep in mind that it exists in a real-world application. Instead of using the client to call endpoints, we use cURL.
* The **authentication server** — This is an application with a database of user credentials.The purpose of this application is to authenticate users based on their credentials (username and password) and send them a one-time password (OTP) through SMS. Because we won’t actually send an SMS in this example, we’ll read the value of the OTP from the database directly.
* The **business logic server** — This is the application exposing endpoints that our clientconsumes. We want to secure access to these endpoints. Before calling an endpoint, the user must authenticate with their username and password and then send an OTP. The user receives the OTP through an SMS message. Because this application is our target application, we secure it with Spring Security.

![chapter-11-figure-11-1.PNG](pictures/chapter-11-figure-11-1.PNG)

To call any endpoint on the business logic server, the client has to follow three steps: 
* Authenticate the username and password by calling the /login endpoint on the business logic server to obtain a randomly generated OTP.
* Call the /login endpoint with the username and OTP. 
* Call any endpoint by adding the token received in step 2 to the Authorization header of the HTTP request.

When the client authenticates the username and password, the business logic server sends a request for an OTP to the authentication server. After successful authentication, the authentication server sends a randomly generated OTP to the client via SMS. This way of identifying the user is called multi-factor authentication (MFA), and it’s pretty common nowadays. We generally need users to prove who they are both by using their credentials and with another means of identification (for example, they own a specific mobile device).

In the second authentication step, once the client has the code from the received SMS, the user can call the /login endpoint, again with the username and the code. The business logic server validates the code with the authentication server. If the code is valid, the client receives a token that it can use to call any endpoint on the business logic server.

In the third authentication step, the client can now call any endpoint by adding the token it receives in step 2 to the Authorization header of the HTTP request.

## Implementing and using tokens

A token is similar to an access card. An application obtains a token as a result of the authentication process and to access resources. Endpoints represent the resources in a web application. For a web application, a token is a string, usually sent through an HTTP header by clients that want to access a particular endpoint. This string can be plain like a pure universally unique identifier (UUID), or it might have a more complex shape like a JSON Web Token (JWT).

### What is a token?

Tokens provide a method that an application uses to prove it has authenticated a user, which allows the user to access the application’s resources.

What are tokens? A token is just an access card, theoretically. When you visit an office building, you first go to the reception desk. There, you identify yourself (authentication), and you receive an access card (token). You can use the access card to open some doors, but not necessarily all doors. This way, the token authorizes your access and decides whether you’re allowed to do something, like opening a particular door.

At the implementation level, tokens can even be regular strings. What’s most important is to be able to recognize these after you issue them. You can generate UUIDs and store them in memory or in a database. Let’s assume the following scenario: 
* The client proves its identity to the server with its credentials. 
* The server issues the client a token in the format of a UUID. This token, now associated with the client, is stored in memory by the server (figure 11.6).
* When the client calls an endpoint, the client provides the token and gets authorized. Figure 11.7 presents this step.

![chapter-11-figure-11-6.PNG](pictures/chapter-11-figure-11-6.PNG)

![chapter-11-figure-11-7.PNG](pictures/chapter-11-figure-11-7.PNG)

This is the general flow associated with using tokens in the authentication and authorization process. Which are its main advantages? Why would you use such a flow? Doesn’t it add more complexity than a simple login? (You can rely only on the user and the password anyway, you might think.) But tokens bring more advantages, so let’s enumerate them and then discuss them one by one: 
* Tokens help you avoid sharing credentials in all requests.
* You can define tokens with a short lifetime.
* You can invalidate tokens without invalidating the credentials.
* Tokens can also store details like user authorities that the client needs to sendin the request. 
* Tokens help you delegate the authentication responsibility to another componentin the system.

**Tokens help you avoid sharing credentials in all requests**. In chapters 2 through 10, we worked with HTTP Basic as the authentication method for all requests. And this method, as you learned, assumes you send credentials for each request. Sending credentials with each request isn’t OK because it often means that you expose them. The more often you expose the credentials, the bigger the chances are that someone intercepts them. With tokens, we change the strategy. We send credentials only in the first request to authenticate. Once authenticated, we get a token, and we can use it to get authorized for calling resources. This way, we only have to send credentials once to obtain the token.

**You can define tokens with a short lifetime**. If a deceitful individual steals the token, they won’t be able to use it forever. Most probably, the token might expire before they find out how to use it to break into your system.

Tokens can also store details needed in the request. We can use tokens to store details like authorities and roles of the user. This way, we can replace a server-side session with a clientside session, which offers us better flexibility for horizontal scaling.

### What is a JSON Web Token?

In this section, we discuss a more specific implementation of tokens — **the JSON Web Token (JWT)**. This token implementation has benefits that make it quite common in today’s applications.

You already learned in section 11.2.1 that a token is anything the server can identify later: a UUID, an access card, and even the sticker you receive when you buy a ticket in a museum. Let’s find out what a JWT looks like, and why a JWT is special. It’s easy to understand a lot about JWTs from the name of the implementation itself: 
* JSON — It uses JSON to format the data it contains.
* Web — It’s designed to be used for web requests.
* Token — It’s a token implementation.

A JWT has three parts, each part separated from the others by a dot (a period). You find an example in this code snippet:

![chapter-11-JWT.PNG](pictures/chapter-11-JWT.PNG)

The first two parts are the header and the body. The header (from the beginning of the token to the first dot) and the body (between the first and the second dot) are formatted as JSON and then are Base64 encoded. We use the header and the body to store details in the token. The next code snippet shows what the header and the body look like before these are Base64 encoded:

![chapter-11-JWT-header-and-body.PNG](pictures/chapter-11-JWT-header-and-body.PNG)

In the header, you store metadata related to the token. In this case, because I chose to sign the token (as you’ll soon learn in the example), the header contains the name of the algorithm that generates the signature (HS256). In the body, you can include details needed later for authorization. In this case, we only have the username. I recommend that you keep the token as short as possible and that you don’t add a lot of data in the body. Even if, technically, there’s no limitation, you’ll find that 
* If the token is long, it slows the request.
* When you sign the token, the longer the token, the more time the cryptographic algorithm needs for signing it.

The last part of the token (from the second dot to the end) is the digital signature, but this part can be missing. Because you’ll usually prefer to sign the header and the body, when you sign the content of the token, you can later use the signature to check that the content hasn’t changed. Without a signature, you can’t be sure that someone didn’t intercept the token when transferred on the network and chang its content.

![chapter-11-figure-11-8.PNG](pictures/chapter-11-figure-11-8.PNG)

In this chapter, we’ll use Java JSON Web Token (JJWT) as the library to create and parse JWTs. This is one of the most frequently used libraries to generate and parse JWT tokens in Java applications. Besides all the needed details related to how to use this library, on JJWT’s GitHub repository, I also found a great explanation of JWTs. You might find it useful to read as well:
```
https://github.com/jwtk/jjwt#overview
```

## Implementing the authentication server

In this section, we start the implementation of our hands-on example. The first dependency we have is the authentication server.

In our scenario, the authentication server connects to a database where it stores the user credentials and the OTPs generated during request authentication events. We need this application to expose three endpoints (figure 11.9): 
* /user/add — Adds a user that we use later for testing our implementation.
* /user/auth — Authenticates a user by their credentials and sends an SMS withan OTP. We take out the part that sends the SMS, but you can do this as an exercise. 
* /otp/check — Verifies that an OTP value is the one that the authentication servergenerated earlier for a specific user.

We create a new project and add the needed dependencies as the next code snippet shows.

```
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

application.yml
```
spring:
  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:auth-server;DB_CLOSE_ON_EXIT=FALSE
    username: sa
    password:
    platform: h2
  jpa:
    hibernate:
      ddl-auto: none
    database: h2
    open-in-view: false
  h2:
    console:
      enabled: true
      path: /h2
  application:
    name: auth-server

logging:
  level:
    org:
      springframework:
        security: DEBUG
```

We also need to make sure we create the database for the application. Because we store user credentials (username and password), we need a table for this.

I use a database named spring and add the scripts to create the two tables required in a schema.sql file. Remember to place the schema.sql file in the resources folder of your project as this is where Spring Boot picks it up to execute the scripts.

```
CREATE SCHEMA spring;

CREATE TABLE spring.user (
    username    VARCHAR(45) NULL,
    password    TEXT NULL,

    PRIMARY KEY (username)
);

CREATE TABLE spring.otp (
    username    VARCHAR(45) NOT NULL,
    code        VARCHAR(45) NULL,

    PRIMARY KEY (username)
);
```

I added Spring Security to the dependencies as well for this application. The only reason I did this for the authentication server is to have the BCryptPasswordEncoder that I like to use to hash the users’ passwords when stored in the database. To keep the example short and relevant to our purpose, I don’t implement authentication between the business logic server and the authentication server.

```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeRequests().anyRequest().permitAll();
        http.headers().frameOptions().disable(); //Required for accessing H2 via webpage. Otherwise - errors
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

```
@Entity
@Table(schema = "spring")
public class User {

    @Id
    private String username;
    private String password;
    
}
```

```
@Entity
@Table(schema = "spring")
public class Otp {

    @Id
    private String username;
    private String code;
}

```

```
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findUserByUsername(String username);
}
```

```
public interface OtpRepository extends JpaRepository<Otp, String> {

    Optional<Otp> findOtpByUsername(String username);
}
```

```
@Service
public class UserService {
    
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final OtpRepository otpRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, OtpRepository otpRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
    }

    @Transactional
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void auth(User user) {
        var maybeUser = userRepository.findUserByUsername(user.getUsername());
        
        if (maybeUser.isPresent()) {
            var u = maybeUser.get();
            if (passwordEncoder.matches(user.getPassword(), u.getPassword())) {
                renewOtp(u);
            } else {
                throw new BadCredentialsException("Bad credentials");
            }
        } else {
            throw new BadCredentialsException("Bad credentials");
        }
    }

    private void renewOtp(User user) {
        var code = generateCode();

        otpRepository.findOtpByUsername(user.getUsername())
                     .ifPresentOrElse(otp -> otp.setCode(code), () -> saveNewOtp(user, code));
    }

    private void saveNewOtp(User user, String code) {
        var otp = new Otp();
        otp.setUsername(user.getUsername());
        otp.setCode(code);
        otpRepository.save(otp);
    }

    private static String generateCode() {
        try {
            var random = SecureRandom.getInstanceStrong();
            var c = random.nextInt(9000) + 1000; //Generates a value between 0 and 8999. Add 1000. This way between 1000 and 9999.
            return String.valueOf(c);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Problem when generating the random code");
        }
    }

    public boolean check(Otp otpToValidate) {
        var maybeOtp = otpRepository.findOtpByUsername(otpToValidate.getUsername());
        return maybeOtp
                .map(otp -> otpToValidate.getCode().equals(otp.getCode()))
                .orElse(false);
    }
}
```

```
@RestController
public class AuthController {
    
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/add")
    public void addUser(@RequestBody User user) {
        userService.addUser(user);
    }

    @PostMapping("/user/auth")
    public void auth(@RequestBody User user) {
        userService.auth(user);
    }

    @PostMapping("/otp/check")
    public void check(@RequestBody Otp otp, HttpServletResponse response) {
        if (userService.check(otp)) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
```

With this setup, we now have the authentication server. Let’s start it and make sure that the endpoints work the way we expect. To test the functionality of the authentication server, we need to 
* Add a new user to the database by calling the /user/add endpoint 
* Validate that the user was correctly added by checking the users table in the database 
* Call the /user/auth endpoint for the user added in step 1 
* Validate that the application generates and stores an OTP in the otp table 
* Use the OTP generated in step 3 to validate that the /otp/check endpoint works as desired

We begin by adding a user to the database of the authentication server. We need at least one user to use for authentication.
```
curl -XPOST -H "content-type: application/json" -d "{\"username\":\"danielle\",\"password\":\"12345\"}" http://localhost:8080/user/add
```

After using the curl command presented by the previous code snippet to add a user, we check the database to validate that the record was added correctly. In my case, I can see the following details:
```
Username: danielle 
Password: $2a$10$.bI9ix.Y0m70iZitP.RdSuwzSqgqPJKnKpRUBQPGhoRvHA.1INYmy
```

We have a user, so let’s generate an OTP for the user by calling the /user/auth endpoint.
```
curl -XPOST -H "content-type: application/json" -d "{\"username\":\"danielle\",\"password\":\"12345\"}" http://localhost:8080/user/auth
```

In the otp table in our database, the application generates and stores a random fourdigit code. In my case, its value is 8173.

The last step for testing our authentication server is to call the /otp/check endpoint and verify that it returns an HTTP 200 OK status code in the response when the OTP is correct and 403 Forbidden if the OTP is wrong. The following code snippets show you the test for the correct OTP value, as well as the test for a wrong OTP value.
```
curl -v -XPOST -H "content-type: application/json" -d "{\"username\":\"danielle\",\"code\":\"8173\"}" http://localhost:8080/otp/check
```
the response status is
```
... 
< HTTP/1.1 200 
...
```

If the OTP value is wrong:
```
curl -v -XPOST -H "content-type: application/json" -d "{\"username\":\"danielle\",\"code\":\"9999\"}" http:/./localhost:8080/otp/check
```

the response status is
```
... 
< HTTP/1.1 403 
...
```

We just proved that the authentication server components work!

## Implementing the business logic server

In this section, we implement the business logic server. With this part of the system, you learn to implement and use JWTs for authentication and authorization.

To accomplish our task, at a high level, we need to:
* Create an endpoint that represents the resource we want to secure.
* Implement the first authentication step in which the client sends the user credentials (username and password) to the business logic server to log in.
* Implement the second authentication step in which the client sends the OTP the user receives from the authentication server to the business logic server. Once authenticated by the OTP, the client gets back a JWT, which it can use to access a user’s resources.
* Implement authorization based on the JWT. The business logic server validates the JWT received from a client and, if valid, allows the client to access the resource.

Technically, to achieve these four high-level points, we need to:
* Create the business logic server project
* Implement the Authentication objects that have the role of representing the two authentication steps.
* Implement a proxy to establish communication between the authentication server and the business logic server.
* Define the AuthenticationProvider objects that implement the authentication logic for the two authentication steps using the Authentication objects defined in step 2.
* Define the custom filter objects that intercept the HTTP request and apply the authentication logic implemented by the AuthenticationProvider objects.
* Write the authorization configurations.

```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>0.11.1</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>0.11.1</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>0.11.1</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>jakarta.xml.bind</groupId>
            <artifactId>jakarta.xml.bind-api</artifactId>
            <version>2.3.2</version>
        </dependency>
        <dependency>
            <groupId>org.glassfish.jaxb</groupId>
            <artifactId>jaxb-runtime</artifactId>
            <version>2.3.2</version>
        </dependency>
```

In this application, we only define a /test endpoint. Everything else we write in this project is to secure this endpoint.

```
@RestController
public class TestController {
    
    @GetMapping("/test")
    public String test() {
        return "Test";
    }
}
```

To secure the app now, we have to define the three authentication levels:
* Authentication with username and password to receive an OTP (figure 11.11)
* Authentication with OTP to receive a token (figure 11.12)
* Authentication with the token to access the endpoint (figure 11.13).

![chapter-11-figure-11-11.PNG](pictures/chapter-11-figure-11-11.PNG)

![chapter-11-figure-11-12.PNG](pictures/chapter-11-figure-11-12.PNG)

![chapter-11-figure-11-13.PNG](pictures/chapter-11-figure-11-13.PNG)

With the given requirements for this example, which is more complex and assumes multiple authentication steps, HTTP Basic authentication can’t help us anymore. We need to implement special filters and authentication providers to customize the authentication logic for our scenario.

![chapter-11-figure-11-14.PNG](pictures/chapter-11-figure-11-14.PNG)

Often, when developing an application, there’s more than one good solution. When designing an architecture, you should always think about all possible implementations and choose the best fit for your scenario. If more than one option is applicable and you can’t decide which is the best to implement, you should write a proof-of-concept for each option to help you decide which solution to choose. For our scenario, I present two options, and then we continue the implementation with one of these. I leave the other choice as an exercise for you to implement.

The first option for us is to define three custom Authentication objects, three custom AuthenticationProvider objects, and a custom filter to delegate to these by making use of the AuthenticationManager (figure 11.15).

![chapter-11-figure-11-15.PNG](pictures/chapter-11-figure-11-15.PNG)

The second option, which I chose to implement in this example, is to have two custom Authentication objects and two custom AuthenticationProvider objects. These objects can help us apply the logic related to the /login endpoint. These will 
* Authenticate the user with a username and password
* Authenticate the user with an OTP

Then we implement the validation of the token with a second filter. Figure 11.16 presents this approach.

![chapter-11-figure-11-16.PNG](pictures/chapter-11-figure-11-16.PNG)

Both approaches are equally good. I describe both of these only to illustrate that you can find cases in which you have multiple ways to develop the same scenario, especially because Spring Security offers quite a flexible architecture. I chose the second one because it offers me the possibility to recap more things, like having multiple custom filters and using the shouldNotFilter() method of the OncePerRequestFilter class.

### Implementing the Authentication objects

We need two types of Authentication objects, one to represent authentication by username and password and a second to represent authentication by OTP. As you learned in chapter 5, the Authentication contract represents the authentication process for a request. It can be a process in progress or after its completion. We need to implement the Authentication interface for both cases in which the application authenticates the user with their username and password, as well as for a OTP.

In listing 11.14, you find the UsernamePasswordAuthentication class, which implements authentication with username and password. To make the classes shorter, I extend the UsernamePasswordAuthenticationToken class and, indirectly, the Authentication interface. You saw the UsernamePasswordAuthenticationToken class in chapter 5, where we discussed applying custom authentication logic.

```
public class UsernamePasswordAuthentication extends UsernamePasswordAuthenticationToken {
    
    public UsernamePasswordAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public UsernamePasswordAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
```

Note that I define both constructors in this class. There’s a big difference between these: when you call the one with **two parameters, the authentication instance remains unauthenticated**, while the one with **three parameters sets the Authentication object as authenticated**. As you learned in chapter 5, when the Authentication instance is authenticated it means that the authentication process ends.

**The third parameter is the collection of granted authorities, which is mandatory for an authentication process that has ended.**

Similarly to the UsernamePasswordAuthentication, we implement the second Authentication object for the second authentication step with OTP. I name this class OtpAuthentication.

```
public class OtpAuthentication extends UsernamePasswordAuthenticationToken {
    
    public OtpAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public OtpAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
```

### Implementing the proxy to the authentication server

To complete authentication, we need a way to call the authentication server.

![chapter-11-figure-11-17.PNG](pictures/chapter-11-figure-11-17.PNG)

We can now write the AuthenticationServerProxy class, which we use to call the two REST endpoints exposed by the authentication server application.

```
@Component
public class AuthenticationServerProxy {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final String serverUrl = "http://localhost:8080";

    public void sendAuth(String username, String password) {
        var url = serverUrl + "/user/auth";

        var requestBody = """
                {
                  "username": "%s",
                  "password": "%s"
                }""".formatted(username, password);

        var request = HttpRequest.newBuilder(URI.create(url))
                               .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                               .header("Content-Type", "application/json")
                               .build();

        try {
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean sendOTP(String username, String code) {
        var url = serverUrl + "/otp/check";

        var requestBody = """
                {
                  "username": "%s",
                  "code": "%s"
                }""".formatted(username, code);

        var request = HttpRequest.newBuilder(URI.create(url))
                                 .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                                 .header("Content-Type", "application/json")
                                 .build();

        try {
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
            return response.statusCode() == 200;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Could not send request", e);
        }
    }
}
```

### Implementing the AuthenticationProvider interface

In this section, we implement the AuthenticationProvider classes. Now we have everything we need to start working on the authentication providers. We need these because this is where we write the custom authentication logic.

We create a class named UsernamePasswordAuthenticationProvider to serve the UsernamePasswordAuthentication type of Authentication.

Because we design our flow to have two authentication steps, and we have one filter that takes care of both steps, we know that authentication doesn’t finish with this provider. We use the constructor with two parameters to build the Authentication object: new UsernamePasswordAuthenticationToken (username, password). Remember, we discussed in section 11.4.1 that the constructor with two parameters doesn’t mark the object as being authenticated.

```
@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {
    
    private final AuthenticationServerProxy proxy;

    public UsernamePasswordAuthenticationProvider(AuthenticationServerProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var username = authentication.getName();
        var password = String.valueOf(authentication.getCredentials());

        proxy.sendAuth(username, password);
        
        return new UsernamePasswordAuthentication(username, password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthentication.class.isAssignableFrom(authentication);
    }
}
```

The logic implemented by this AuthenticationProvider is simple. It calls the authentication server to find out if the OTP is valid. If he OTP is correct and valid, it returns an instance of Authentication. The filter ends back the token in the HTTP response. If the OTP isn’t correct, the authentication rovider throws an exception.

```
@Component
public class OtpAuthenticationProvider implements AuthenticationProvider {

    private final AuthenticationServerProxy proxy;

    public OtpAuthenticationProvider(AuthenticationServerProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var username = authentication.getName();
        var code = String.valueOf(authentication.getCredentials());

        var result = proxy.sendOTP(username, code);
        
        if (result) {
            return new OtpAuthentication(username, code);
        } else {
            throw new BadCredentialsException("Bad credentials.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OtpAuthentication.class.isAssignableFrom(authentication);
    }
}
```

### Implementing the filters

In this section, we implement the custom filters that we add to the filter chain. Their purpose is to intercept requests and apply authentication logic. We chose to implement one filter to deal with authentication done by the authentication server and another one for authentication based on the JWT. We implement an InitialAuthenticationFilter class, which deals with the first authentication steps that are done using the authentication server.

We start by injecting the AuthenticationManager to which we delegate the authentication responsibility, override the doFilterInternal() method, which is called when the request reaches this filter in the filter chain, and override the shouldNotFilter() method. As we discussed in chapter 9, the shouldNotFilter() method is one of the reasons why we would choose to extend the OncePerRequestFilter class instead of implementing the Filter interface directly. When we override this method, we define a specific condition on when the filters execute. In our case, we want to execute any request only on the /login path and skip all others.

We continue writing the InitialAuthenticationFilter class with the first authentication step, the one in which the client sends the username and password to obtain the OTP. We assume that if the user doesn’t send an OTP (a code), we have to do authentication based on username and password. We take all the values from the HTTP request header where we expect them to be, and if a code wasn’t sent, we call the first authentication step by creating an instance of UsernamePasswordAuthentication and forwarding the responsibility to the AuthenticationManager.

We know (since chapter 2) that next, the AuthenticationManager tries to find a proper AuthenticationProvider. In our case, this is the UsernamePasswordAuthenticationProvider. It’s the one triggered because its supports() method states that it accepts the UsernamePasswordAuthentication type.

If, however, a code is sent in the request, we assume it’s the second authentication step. In this case, we create an OtpAuthentication object to call the AuthenticationManager.

```
@Component
public class InitialAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired //Due to circular depedencies
    private AuthenticationManager authenticationManager;
    @Value("${jwt.signing.key}")
    private String signingKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var username = request.getHeader("username");
        var password = request.getHeader("password");
        var code = request.getHeader("code");

        if (code == null) {
            var authentication = new UsernamePasswordAuthentication(username, password);
            authenticationManager.authenticate(authentication);
        } else {
            var otpAuthentication = new OtpAuthentication(username, code);
            var authentication = authenticationManager.authenticate(otpAuthentication);

            var secretKey = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));
            var jwt = Jwts.builder()
                          .setClaims(Map.of("username", username))
                          .signWith(secretKey)
                          .compact();

            response.setHeader("Authorization", jwt);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals("/login");
    }
}
```

The following code snippet builds the JWT. I use the setClaims() method to add a value in the JWT body and the signWith() method to attach a signature to the token. For our example, I use a symmetric key to generate the signature:

```
SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));

String jwt = Jwts.builder()
                 .setClaims(Map.of("username", username))
                 .signWith(key)
                 .compact();
```

This key is known only by the business logic server. The business logic server signs the token and can use the same key to validate the token when the client calls an endpoint. For simplicity of the example, I use here one key for all users. In a real-world scenario, however, I would have a different key for each user, but as an exercise, you can change this application to use different keys. **The advantage of using individual keys for users is that if you need to invalidate all the tokens for a user, you need only to change its key.**

Because we inject the value of the key used to sign the JWT from the properties, we need to change the application.properties file to define this value.

```
jwt.signing.key=ymLTU8rq83…
```

We also need to add the filter that deals with the requests on all paths other than /login. I name this filter JwtAuthenticationFilter. This filter expects that a JWT exists in the authorization HTTP header of the request. This filter validates the JWT by checking the signature, creates an authenticated Authentication object, and adds it to the SecurityContext. The following listing presents the implementation of the JwtAuthenticationFilter.

```
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String signingKey;

    public JwtAuthenticationFilter(@Value("${jwt.signing.key}") String signingKey) {
        this.signingKey = signingKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var jwt = request.getHeader("Authorization");

        var key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));

        var claims = Jwts.parserBuilder()
                       .setSigningKey(key)
                       .build()
                       .parseClaimsJws(jwt)
                       .getBody();

        var username = String.valueOf(claims.get("username"));
        var grantedAuthority = new SimpleGrantedAuthority("user");
        var auth = new UsernamePasswordAuthentication(username, null, List.of(grantedAuthority));

        SecurityContextHolder.getContext().setAuthentication(auth);
        
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/login");
    }
}
```

### Writing the security configurations

We have to do a few configurations so that our entire puzzle is coherent:
* Add the filters to the filter chain as you learned in chapter 9.
* Disable CSRF protection because, as you learned in chapter 10, this doesn’t apply when using different origins. Here, using a JWT replaces the validation that would be done with a CSRF token.
* Add the AuthenticationProvider objects so that the AuthenticationManager knows them.
* Use matcher methods to configure all the requests that need to be authenticated, as you learned in chapter 8.
* Add the AuthenticationManager bean in the Spring context so that we can inject it from the InitialAuthenticationFilter

```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    private final InitialAuthenticationFilter initialAuthenticationFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OtpAuthenticationProvider otpAuthenticationProvider;
    private final UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider;

    public ProjectConfig(InitialAuthenticationFilter initialAuthenticationFilter,
                         JwtAuthenticationFilter jwtAuthenticationFilter,
                         OtpAuthenticationProvider otpAuthenticationProvider,
                         UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider) {
        this.initialAuthenticationFilter = initialAuthenticationFilter;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.otpAuthenticationProvider = otpAuthenticationProvider;
        this.usernamePasswordAuthenticationProvider = usernamePasswordAuthenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.addFilterAt(initialAuthenticationFilter, BasicAuthenticationFilter.class)
            .addFilterAt(jwtAuthenticationFilter, BasicAuthenticationFilter.class);

        http.authorizeRequests().anyRequest().authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(otpAuthenticationProvider)
            .authenticationProvider(usernamePasswordAuthenticationProvider);
    }
    
    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
```

### Testing the whole system

Now that everything is in place, it’s time to run the two components of our system, the authentication server and the business logic server, and examine our custom authentication and authorization to see if this works as desired.
```
curl -H "username:danielle" -H "password:12345" http://localhost:9090/login
```

Once we call the /login endpoint, providing the correct username and password, we check the database for the generated OTP value. This should be a record in the otp table where the value of the username field is danielle. In my case, I have the following record:
```
Username: danielle 
Code: 6271
```

We assume this OTP was sent in an SMS message, and the user received it. We use it for the second authentication step. The cURL command in the next code snippet shows you how to call the /login endpoint for the second authentication step. I also add the -v option to see the response headers where I expect to find the JWT:
```
curl -v -H "username:danielle" -H "code:6271" http://localhost:9090/login
```

The (truncated) response is
```
. . . 
< HTTP/1.1 200 <Authorization: eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImRhbmllbGxlIn0.wg6LFProg7s_KvFxvnY GiZF-Mj4rr-0nJA1tVGZNn8U 
. . .
```

The JWT is right there where we expected it to be: in the authorization response header. Next, we use the token we obtained to call the /test endpoint:
```
curl -H "Authorization:eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImRhbmllbGxlIn0.wg6LFProg7s_KvFxvnYGiZF-Mj4rr-0nJA1tVGZNn8U" http://localhost:9090/test
```
The response body is
```
Test
```

# Chapter 12. How does OAuth 2 work?

The OAuth 2 framework is a vast subject that could take an entire book to cover. And I can’t argue with that, but in four chapters, you’ll learn everything you need to know about applying OAuth 2 with Spring Security. We’ll start this chapter with an overview, where you’ll discover that the main actors in the OAuth 2 framework are the user, the client, the resource server, and the authorization server.

## The OAuth 2 framework

Today, OAuth 2 is commonly used in securing web applications, so you’ve probably already heard about it. The chances are that you’ll need to apply OAuth 2 in your applications. And this is why we need to discuss applying OAuth 2 in Spring applications with Spring Security. We start with a little bit of theory and then move on to apply it with an application using SSO.

In most cases, OAuth 2 is referred to as an *authorization framework* (or a specification framework) whose primary purpose is to allow a third-party website or app access to a resource. Sometimes people refer to OAuth 2 as a *delegation protocol*. Whatever you choose to call it, it’s important to remember that OAuth 2 is not a specific implementation or a library.

I think that a great way to understand what OAuth 2 is and its usefulness is to start the discussion with examples we’ve already analyzed in this book. The most trivial way to authenticate, which you saw in plenty of examples up to now, is the HTTP Basic authentication method. Isn’t this enough for our systems such that we don’t have to add more complexity? No. With HTTP Basic authentication, we have two issues we need to take into consideration: 
* Sending credentials for each and every request
* Having the credentials of the users managed by a separate system

Sending credentials for each and every request might work with isolated cases, but that’s generally undesirable because it implies 
* Sharing credentials often over the network
* Having the client (browser, in case of a web application) store credentials somehowso that the client can send those to the server with the request to get authenticated and authorized

We want to get rid of these two points from our applications’ architecture because they weaken security by making credentials vulnerable. Most often, we want to have a separate system manage user credentials. Imagine, that you have to configure and use separate credentials for all the applications you work with in your organization (figure 12.2).

![chapter-11-figure-12-2.PNG](pictures/chapter-11-figure-12-2.PNG)

It would be better if we isolated the responsibility for credential management in one component of our system. Let’s call it, for now, the authorization server (figure 12.3).

![chapter-11-figure-12-3.PNG](pictures/chapter-11-figure-12-3.PNG)

This approach eliminates the duplication of credentials representing the same individual. In this way, the architecture becomes simpler and easier to maintain.

## The components of the OAuth 2 authentication architecture

As mentioned, OAuth 2 components include 
* The **resource server** — The application hosting resources owned by users.Resources can be users’ data or their authorized actions. 
* The **user (also known as the resource owner)** — The individual who owns resourcesexposed by the resource server. A user generally has a username and a password that they use to identify themselves.
* The **client** — The application that accesses the resources owned by the user ontheir behalf. The client uses a client ID and a client secret to identify itself. Be careful, these credentials are not the same as the user credentials. The client needs its own credentials to identify itself when making a request. 
* The **authorization server** — The application that authorizes the client to access theuser’s resources exposed by the resource server. When the authorization server decides that a client is authorized to access a resource on behalf of the user, it issues a token. The client uses this token to prove to the resource server that it was authorized by the authorization server. The resource server allows the client to access the resource it requested if it has a valid token.

![chapter-11-figure-12-4.PNG](pictures/chapter-11-figure-12-4.PNG)

## Implementation choices with OAuth 2

As you’ll learn, OAuth 2 implies multiple possible authentication flows, and you need to know which one applies to your case. In this section, I take the most common cases and evaluate these. It’s important to do this before starting with the first implementation so that you know what you’re implementing.

So how does OAuth 2 work? What does it mean to implement OAuth 2 authentication and authorization? Mainly, OAuth 2 refers to using tokens for authorization. Remember from section 11.2 that tokens are like access cards. Once you obtain a token, you can access specific resources. But OAuth 2 offers multiple possibilities for obtaining a token, called grants. Here are the most common OAuth 2 grants you can choose from: 
* Authorization code
* Password
* Refresh token
* Client credentials

### Implementing the authorization code grant type

![chapter-11-figure-12-5.PNG](pictures/chapter-11-figure-12-5.PNG)

This grant type is one of the most commonly used OAuth 2 flows, so it’s quite important to understand how it works and how to apply it.

Here’s how the authorization code grant type works. Following this, we dive into the details about each step.
* Make the authentication request
* Obtain an access token
* Call the protected resource

#### STEP 1: MAKING THE AUTHENTICATION REQUEST WITH THE AUTHORIZATION CODE GRANT TYPE

The client redirects the user to an endpoint of the authorization server where they need to authenticate. You can imagine you are using app X, and you need to access a protected resource. To access that resource for you, app X needs you to authenticate. It opens a page for you with a login form on the authorization server that you must fill in with your credentials.

**NOTE** What’s really important to observe here is that the user interacts directly with the authorization server. The user doesn’t send the credentials to the client app.

Technically, what happens here is that when the client redirects the user to the authorization server, the client calls the authorization endpoint with the following details in the request query: 
* **response_type** with the value *code*, which tells the authorization server thatthe client expects a code. The client needs the code to obtain an access token, as you’ll see in the second step. 
* **client_id** with the value of the client ID, which identifies the application itself. 
* **redirect_uri**, which tells the authorization server where to redirect the user after successful authentication. Sometimes the authorization server already knows a default redirect URI for each client. For this reason, the client doesn’t need to send the redirect URI.
* **scope**, which is similar to the granted authorities we discussed in chapter 5.
* **state**, which defines a cross-site request forgery (CSRF) token used for the CSRF protection we discussed in chapter 10.

After successful authentication, the authorization server calls back the client on the redirect URI and provides a code and the state value. The client checks that the state value is the same as the one it sent in the request to confirm that it was not someone else attempting to call the redirect URI. The client uses the code to obtain an access token as presented in step 2.

#### STEP 2: OBTAINING AN ACCESS TOKEN WITH THE AUTHORIZATION CODE GRANT TYPE

To allow the user to access resources, the code resulting from step 1 is the client’s proof that the user authenticated. You guessed correctly, this is why this is called the authorization code grant type. Now the client calls the authorization server with the code to get the token.

In many cases, these first two steps create confusion. People are generally puzzled about why the flow needs two calls to the authorization server and two different tokens — the authorization code and an access token. Take a moment to understand this: 
* The authorization server generates the first code as proof that the user directly interacted with it. The client receives this code and has to authenticate again using it and its credentials to obtain an access token. 
* The client uses the second token to access resources on the resource server.

So why didn’t the authorization server directly return the second token (access token)? Well, OAuth 2 defines a flow called the implicit grant type where the authorization server directly returns an access token. The **implicit grant type** is not enumerated in this section because it’s usage **is not recommended**, and most authorization servers today don’t allow it. The simple fact that the authorization server would call the redirect URI directly with an access token without making sure that it was indeed the right client receiving that token makes the flow less secure. By sending an authorization code first, the client has to prove again who they are by using their credentials to obtain an access token. The client makes a final call to get an access token and sends 
* The authorization code, which proves the user authorized them
* Their credentials, which proves they really are the same client and not someoneelse who intercepted the authorization codes

To return to step 2, technically, the client now makes a request to the authorization server. This request contains the following details: 
* **code**, which is the authorization code received in step 1. This proves that theuser authenticated. 
* **client_id** and client_secret, the client’s credentials.
* **redirect_uri**, which is the same one used in step 1 for validation.
* **grant_type** with the value authorization_code, which identifies the kindof flow used. A server might support multiple flows, so it’s essential always to specify which is the current executed authentication flow.

As a response, the server sends back an access_token. This token is a value that the client can use to call resources exposed by the resource server.

#### STEP 3: CALLING THE PROTECTED RESOURCE WITH THE AUTHORIZATION CODE GRANT TYPE

After successfully obtaining the access token from the authorization server, the client can now call for the protected resource. The client uses an access token in the authorization request header when calling an endpoint of the resource server.

**NOTE** The authorization code grant type has the great advantage of enabling the user to allow a client to execute specific actions without needing to share their credentials with the client. But this grant type has a weakness: what happens if someone intercepts the authorization code? Of course, the client needs to authenticate with its credentials, as we discussed previously. But what if the client credentials are also stolen somehow? Even if this scenario isn’t easy to achieve, we can consider it a vulnerability of this grant type. To mitigate this vulnerability, you need to rely on a more complex scenario as presented by the Proof Key for Code Exchange (PKCE) authorization code grant type.

### Implementing the password grant type

In this section, we discuss the **password grant type** (figure 12.7). This grant type is also known as the **resource owner credentials grant type**. Applications using this flow assume that the client collects the user credentials and uses these to authenticate and obtain an access token from the authorization server.

![chapter-11-figure-12-7.PNG](pictures/chapter-11-figure-12-7.PNG)

**You use this authentication flow only if the client and authorization server are built and maintained by the same organization.** Why? Let’s assume you build a microservices system, and you decide to separate the authentication responsibility as a different microservice to enhance scalability and keep responsibilities separated for each service. (This separation is used widely in many systems.)

Let’s assume further that your system’s users use either a client web application developed with a frontend framework like Angular, ReactJS, or Vue.js, or they use a mobile app. In this case, users might consider it strange to be redirected from your system to the same system for authentication and then back again. This is what would happen with a flow like the authorization code grant type. With the password grant type, you would instead expect to have the application present a login form to the user, and let the client take care of sending the credentials to the server to authenticate. The user doesn’t need to know how you designed the authentication responsibility in your application. Let’s see what happens when using the password grant type. The two tasks are as follows:
* Request an access token. 
* Use the access token to call resources.

#### STEP 1: REQUESTING AN ACCESS TOKEN WHEN USING THE PASSWORD GRANT TYPE

The flow is much simpler with the password grant type. The client collects the user credentials and calls the authorization server to obtain an access token. When requesting the access token, the client also sends the following details in the request: 
* **grant_type** with the value password.
* **client_id** and **client_secret**, which are the credentials used by the client to authenticate itself. 
* **scope**, which you can understand as the granted authorities.
* **username** and **password**, which are the user credentials. These are sent in plain text as values of the request header.

The client receives back an access token in the response. The client can now use the access token to call the endpoints of the resource server.

#### STEP 2: USING AN ACCESS TOKEN TO CALL RESOURCES WHEN USING THE PASSWORD GRANT TYPE

Once the client has an access token, it uses the token to call the endpoints on the resource server, which is exactly like the authorization code grant type. The client adds the access token to the requests in the authorization request header.

**NOTE** **The password grant type is less secure than the authorization code grant type**, mainly because it assumes sharing the user credentials with the client app. While it’s true that it’s more straightforward than the authorization code grant type and this is the main reason you also find it used plenty in theoretical examples, try to avoid this grant type in real-world scenarios. Even if the authorization server and the client are both built by the same organization, **you should first think about using the authorization code grant type. Take the password grant type as your second option.**

### Implementing the client credentials grant type

In this section, we discuss the client credentials grant type (figure 12.8). **This is the simplest of the grant types described by OAuth 2.** You can **use it** when no user is involved; that is, **when implementing authentication between two applications.** I like to think about the client credentials grant type as being a combination of the password grant type and an API key authentication flow. We assume you have a system that implements authentication with OAuth 2. Now you need to allow an external server to authenticate and call a specific resource that your server exposes.

![chapter-11-figure-12-8.PNG](pictures/chapter-11-figure-12-8.PNG)

The steps for the client credentials grant type are similar to the password grant type. The only exception is that the request for an access token doesn’t need any user credentials. Here are the steps to implement this grant type:
* Request an access token
* Use the access token to call resources

#### STEP 1: REQUESTING AN ACCESS TOKEN WITH THE CLIENT CREDENTIAL GRANT TYPE

To obtain an access token, the client sends a request to the authorization server with the following details: 
* **grant_type** with the value **client_credentials**
* **client_id** and **client_secret**, which represent the client credentials
* **scope**, which represents the granted authorities

In response, the client receives an access token. The client can now use the access token to call the endpoints of the resource server.

#### STEP 2: USING AN ACCESS TOKEN TO CALL RESOURCES WITH THE CLIENT CREDENTIAL GRANT TYPE

Once the client has an access token, it uses that token to call the endpoints on the resource server, which is exactly like the authorization code grant type and the password grant type. The client adds the access token to the requests in the authorization request header.

### Using refresh tokens to obtain new access tokens

In this section, we discuss refresh tokens (figure 12.9). Up to now, you learned that the result of an OAuth 2 flow, which we also call grant, is an access token. But we didn’t say much about this token. In the end, OAuth 2 doesn’t assume a specific implementation for tokens. What you’ll learn now is that a token, no matter how it’s implemented, can expire. It’s not mandatory — you can create tokens with an infinite lifespan — but, in general, you should make these as **short lived as possible**. The refresh tokens that we discuss in this section represent an alternative to using credentials for obtaining a new access token.

![chapter-11-figure-12-9.PNG](pictures/chapter-11-figure-12-9.PNG)

Let’s assume in your app, you implement tokens that never expire. That means that the client can use the same token again and again to call resources on the resource server. What if the token is stolen? In the end, don’t forget that the token is attached as a simple HTTP header on each and every request. If the token doesn’t expire, someone who gets their hands on the token can use it to access resources. A token that doesn’t expire is too powerful. It becomes almost as powerful as user credentials. We prefer to avoid this and make the token short lived. This way, at some point, an expired token can’t be used anymore. The client has to obtain another access token.

To obtain a new access token, the client can rerun the flow, depending on the grant type used. For example, if the grant type is authentication code, the client would redirect the user to the authorization server login endpoint, and the user must again fill in their username and password. Not really user friendly, is it? Imagine that the token has a 20-minute lifespan and you work for a couple of hours with the online app. During that time, the app would redirect you back about six times to log in again. (Oh no! That app logged me out again!) To avoid the need to reauthenticate, the authorization server can issue a refresh token, which has a different value and purpose than an access token. The app uses the refresh token to obtain a new access token instead of having to reauthenticate.

Refresh tokens also have advantages over reauthentication in the password grant type. Even if with the password grant type, if we don’t use refresh tokens, we would either have to ask the user to authenticate again or store their credentials. Storing the user credentials when using the password grant type is one of the biggest mistakes you can make! And I’ve seen this approach used in real applications! Don’t do it! If you store the username and password (and assuming you save these as plaintext or something reversible because you have to be able to reuse them), you expose those credentials. Refresh tokens help you solve this problem easily and safely. Instead of unsafely storing credentials and without needing to redirect the user every time, you can store a refresh token and use it to obtain a new access token when needed. Storing the refresh token is safer because you can revoke it if you find that it was exposed. Moreover, don’t forget that people tend to have the same credentials for multiple apps. So losing credentials is worse than losing a token that one could use with a specific application.

Finally, let’s look at how to use a refresh token. Where do you get a refresh token from? **The authorization server returns a refresh token together with an access token when using a flow like the authorization code or password grant types**. With the client credentials grant, there’s no refresh token because this flow doesn’t need user credentials. Once the client has a refresh token, the client should issue a request with the following details when the access token expires:
* **grant_type** with value **refresh_token**.
* **refresh_token** with the value of the refresh token.
* **client_id** and **client_secret** with the client credentials.
* **scope**, which defines the same granted authorities or less. **If more granted authorities need to be authorized, a reauthentication is needed.**

## The sins of OAuth 2

In this section, we discuss possible vulnerabilities of OAuth 2 authentication and authorization. It’s important to understand what can go wrong when using OAuth 2 so that you can avoid these scenarios. Of course, like anything else in software development, OAuth 2 isn’t bulletproof. It has its vulnerabilities of which we must be aware and which we must consider when building our applications. I enumerate here some of the most common: 
* **Using cross-site request forgery (CSRF) on the client** — With a user logged in, CSRF is possible if the application doesn’t apply any CSRF protection mechanism. We had a great discussion on CSRF protection implemented by Spring Security in chapter 10. 
* **Stealing client credentials** — Storing or transferring unprotected credentials can create breaches that allow attackers to steal and use them. 
* **Replaying tokens** — As you’ll learn in chapters 13 and 14, tokens are the keys we use within an OAuth 2 authentication and authorization architecture to access resources. You send these over the network, but sometimes, they might be intercepted. If intercepted, they are stolen and can be reused.
* **Token hijacking** — Implies someone interferes with the authentication processand steals tokens that they can use to access resources. This is also a potential vulnerability of using refresh tokens, as these as well can be intercepted and used to obtain new access tokens. I recommend this helpful article:

Remember, OAuth 2 is a framework. The vulnerabilities are the result of wrongly implementing functionality over it.

## Implementing a simple single sign-on application

In this section, we implement the first application of our book that uses the OAuth 2 framework with Spring Boot and Spring Security. A single sign-on (SSO) application is, as the name suggests, one in which you authenticate through an authorization server, and then the app keeps you logged in, using a refresh token. In our case, it represents only the client from the OAuth 2 architecture.

In this application (figure 12.10), we use GitHub as the authorization and resource servers, and we focus on the communication between the components with the authorization code grant type. In chapters 13 and 14, we’ll implement both an authorization server and a resource server in an OAuth 2 architecture.

![chapter-11-figure-12-10.PNG](pictures/chapter-11-figure-12-10.PNG)

### Managing the authorization server

In this section, we configure the authorization server. In this chapter, we won’t implement our own authorization server, but instead, we use an existing one: GitHub.

So what should we do to use a third-party like GitHub as an authorization server? This means that, in the end, our application won’t manage its users, and that anyone can log in to our application using their GitHub account. Like any other authorization server, GitHub needs to know the client application to which it issues tokens. Remember in section 12.3, where we discussed the OAuth 2 grants, that requests used a client ID and a client secret. A client uses these credentials to authenticate itself at the authorization server, so the OAuth application must be registered with the GitHub authorization server. To do this, we complete a short form (figure 12.11) using the following link:
```
https://github.com/settings/applications/new
```

When you add a new OAuth application, you need to specify a name for the application, the homepage, and the link to which GitHub will make the call back to your application. The OAuth 2 grant type on which this works is the authorization code grant type. This grant type assumes that the client redirects the user to the authorization server (GitHub, in our case) for login, and then the authorization server calls the client back at a defined URL, as we discussed in section 12.3.1. This is why you need to identify the callback URL here. Because I run the example on my system, I use the localhost in both cases. And because I don’t change the port (which is 8080 by default, as you already know), this makes http://localhost:8080 my homepage URL. I use the same URL for the callback.

![chapter-11-figure-12-11.PNG](pictures/chapter-11-figure-12-11.PNG)

![chapter-11-figure-12-12.PNG](pictures/chapter-11-figure-12-12.PNG)

Once you fill out the form and choose Register Application, GitHub provides you with a client ID and a client secret (figure 12.12).

This configuration is everything we need to do for the authorization server. Now that we have the client credentials, we can start working on our application.

### Starting the implementation

```
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-oauth2-client</artifactId>
    </dependency>
</dependencies>
```

```
@Controller
public class MainController {

    @GetMapping("/")
    public String main() {
        return "main.html";
    }
}
```

main.html
```
<h1>Hello there!</h1>
```

And now the real job! Let’s set the security configurations to allow our application to use the login with GitHub. A difference: instead of using httpBasic() or formLogin() as you learned in chapter 4, we call a different method named oauth2Login().

```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.oauth2Login();

        http.authorizeRequests().anyRequest().authenticated(); //Specifies that a user needs to be authenticated to make a request
    }
}
```

But you know what’s going on. As with httpBasic() or formLogin(), oauth2Login() simply adds a new authentication filter to the filter chain. In this case, the filter that the framework adds to the filter chain when you call the oauth2Login() method is the OAuth2LoginAuthenticationFilter (figure 12.13). This filter intercepts requests and applies the needed logic for OAuth 2 authentication.

![chapter-11-figure-12-13.PNG](pictures/chapter-11-figure-12-13.PNG)

### Implementing ClientRegistration

If you start it as is right now, you won’t be able to access the main page. The reason why you can’t access the page is that you have specified that for any request, the user needs to authenticate, but you didn’t provide any way to authenticate. We need to establish that GitHub is our authorization server. For this purpose, Spring Security defines the ClientRegistration contract.

The ClientRegistration interface represents the client in the OAuth 2 architecture. For the client, you need to define all its needed details, among which we have 
* The client ID and secret 
* The grant type used for authentication 
* The redirect URI 
* The scopes

```
ClientRegistration cr = ClientRegistration.withRegistrationId("github")
                .clientId("a7553955a0c534ec5e6b")
                .clientSecret("1795b30b425ebb79e424afa51913f1c724da0dbb")
                .scope(new String[]{"read:user"})
                .authorizationUri("https://github.com/login/oauth/authorize")
                .tokenUri("https://github.com/login/oauth/access_token")
                .userInfoUri("https://api.github.com/user")
                .userNameAttributeName("id")
                .clientName("GitHub")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUriTemplate("{baseUrl}/{action}/oauth2/code/{registrationId}")
                .build();
```

Oh! Where did all those details come from? I know it might look scary at first glance, but it’s nothing more than setting up the client ID and secret. Also, I define the scopes (granted authorities), a client name, and a registration ID of my choice. Besides these details, I had to provide the URLs of the authorization server: 
* Authorization URI — The URI to which the client redirects the user for authentication 
* Token URI — The URI that the client calls to obtain an access token and a refresh token 
* User info URI — The URI that the client can call after obtaining an access token to get more details about the user

Where did I get all those URIs? Well, if the authorization server is not developed by you, as in our case, you need to get them from the documentation. For GitHub, for example, you can find them here:
```
https://developer.github.com/apps/building-oauth-apps/authorizing-oauth-apps/
```

Wait! Spring Security is even smarter than this. The framework defines a class named CommonOAuth2Provider. This class partially defines the ClientRegistration instances for the most common providers you can use for authentication, which include: 
* Google 
* GitHub 
* Facebook 
* Okta

If you use one of these providers, you can define your ClientRegistration as presented:

```
ClientRegistration cr = CommonOAuth2Provider.GITHUB
        .getBuilder("github")
        .clientId("a7553955a0c534ec5e6b")
        .clientSecret("1795b30b42. . .")
        .build();
```

As you can see, this is much cleaner, and you don’t have to find and set the URLs for the authorization server manually. Of course, this applies only to common providers.

### Implementing ClientRegistrationRepository

In this section, you learn how to register the ClientRegistration instances for Spring Security to use for authentication.

![chapter-11-figure-12-14.PNG](pictures/chapter-11-figure-12-14.PNG)

The ClientRegistrationRepository interface is similar to the UserDetailsService interface, which you learned about in chapter 2. In the same way that a UserDetailsService object finds UserDetails by its username, a ClientRegistrationRepository object finds ClientRegistration by its registration ID.

You can implement the ClientRegistrationRepository interface to tell the framework where to find the ClientRegistration instances. Spring Security offers us an implementation for ClientRegistrationRepository, which stores in memory the instances of ClientRegistration: InMemoryClientRegistrationRepository. As you guessed, this works similarly to how InMemoryUserDetailsManager works for the UserDetails instances.

```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(buildClient());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.oauth2Login();

        http.authorizeRequests().anyRequest().authenticated(); //Specifies that a user needs to be authenticated to make a request
    }

    private static ClientRegistration buildClient() {
        return CommonOAuth2Provider.GITHUB
                .getBuilder("testing-my-application-oauth-please-delete")
                .clientId("8b40d47a32792ca6e46d")
                .clientSecret("aec3e54b83c5deb5138ad01f12cd0b797bb18098")
                .build();
    }
}
```

As an alternative to this way of registering ClientRegistrationRepository, you can use a Customizer object as a parameter of the oauth2Login() method of the HttpSecurity object.

```
http.oauth2Login(c -> {
    c.clientRegistrationRepository(clientRepository());
});
```

### The pure magic of Spring Boot configuration

In this section, I show you a third approach to configuring the application we built earlier in this chapter. Spring Boot is designed to use its magic and build the ClientRegistration and ClientRegistrationRepository objects directly from the properties file. This approach isn’t unusual in a Spring Boot project. The following code snippet shows how to set the client registration for our example in the application.properties file:

```
spring.security.oauth2.client.registration.github.client-id=a7553955a0c534ec5e6b
spring.security.oauth2.client.registration.github.client-secret=1795b30b425ebb79e424afa51913f1c724da0dbb
```

In this snippet, I only need to specify the client ID and client secret. Because the name for the provider is github, Spring Boot knows to take all the details regarding the URIs from the CommonOAuth2Provider class.

### Obtaining details about an authenticated user

In this section, we discuss getting and using details of an authenticated user. You’re already aware that in the Spring Security architecture, it’s the SecurityContext that stores the details of an authenticated user. Once the authentication process ends, the responsible filter stores the Authentication object in the SecurityContext. The application can take user details from there and use them when needed. The same happens with an OAuth 2 authentication as well.

The implementation of the Authentication object used by the framework is named OAuth2AuthenticationToken in this case. You can take it directly from the SecurityContext or let Spring Boot inject it for you in a parameter of the endpoint

```java
@Controller
public class MainController {
    
    @GetMapping("/")
    public String main(OAuth2AuthenticationToken token) {
        System.out.println(token.getPrincipal());
        System.out.println(token.getCredentials());
        System.out.println(token.getAuthorities());
        System.out.println(token);
        
        return "main.html";
    }
}
```

### Testing the application

I first make sure I’m not logged in to GitHub. I also make sure I open a browser console to check the history of request navigation. This history gives me an overview of the steps that happen in the OAuth 2 flow. If I am authenticated, then the application directly logs me. Then I start the app and access the main page of our application in the browser:

```shell
http://localhost:8080/
```

The application redirects me to the URL in the following code snippet (and presented in figure 12.16). This URL is configured in the CommonOauth2Provider class for GitHub as the authorization URL.
```shell
https://github.com/login/oauth/ authorize?response_type=code&client_id=a7553955a0c534ec5e6b&scope=read:user&state=fWwg5r9sKal4BMubg1oXBRrNn5y7VDW1A_rQ4UITbJk%3D&redirect_uri=http://localhost:8080/login/oauth2/code/github
```

![chapter-11-figure-12-16.PNG](pictures/chapter-11-figure-12-16.PNG)

Our application attaches the needed query parameters to the URL, as we discussed in section 12.3.1. These are 
* response_type with the value code 
* client_id 
* scope (the value read:user is also defined in the CommonOauth2Provider class) 
* state with the CSRF token

We use our GitHub credentials and log in to our application with GitHub. We are authenticated and redirected back, as you can see in figure 12.17.

![chapter-11-figure-12-17.PNG](pictures/chapter-11-figure-12-17.PNG)

The following code snippet shows the URL on which GitHub calls us back. You can observe that GitHub provides the authorization code that our application uses to request an access token:

```shell
http://localhost:8080/login/oauth2/code/ github?code=a3f20502c182164a4086&state=fWwg5r9sKal4BMubg1oXBRrNn5y7VDW1A _rQ4UITbJk%3D
```

We won’t see the calls to the token endpoint from the browser as this happens directly from our application. But we can trust that the application managed to get a token because we can see the user details printed in the console.
```shell
Name: [43921235], Granted Authorities: [[ROLE_USER, SCOPE_read:user]], User Attributes: [{login=lspil, id=43921235, node_id=MDQ6VXNlcjQzOTIxMjM1, avatar_url=https://avatars3.githubusercontent.com/u/43921235?v=4, gravatar_id=, url=https://api.github.com/users/lspil, html_url=https:// github.com/lspil, followers_url=https://api.github.com/users/lspil/ followers, following_url=https://api.github.com/users/lspil/following{/ other_user}, …
```

# Chapter 13. OAuth 2: Implementing the authorization server

In this chapter, we’ll discuss implementing an authorization server with Spring Security. As you learned in chapter 12, the authorization server is one of the components acting in the OAuth 2 architecture (figure 13.1). The role of the authorization server is to authenticate the user and provide a token to the client. The client uses this token to access resources exposed by the resource server on behalf of the user.

![chapter-13-figure-13-1.PNG](pictures/chapter-13-figure-13-1.PNG)

The Spring Security team announced a new authorization server is being developed. Naturally, it takes time for the new Spring Security authorization server to mature. Until then, the only choice we have for developing a custom authorization server with Spring Security is the way we’ll implement the server in this chapter.

Instead of implementing a custom authorization server, you could go with a thirdparty tool like Keycloak or Okta. In chapter 18, we’ll use Keycloak in our hands-on example. But in my experience, sometimes stakeholders won’t accept using such a solution, and you need to go with implementing custom code. Let’s learn how to do this and better understand the authorization server in the following sections of this chapter.

## Writing your own authorization server implementation

There’s no OAuth 2 flow without an authorization server. As I said earlier, OAuth 2 is mainly about obtaining an access token. And the authorization server is the component of the OAuth 2 architecture that issues access tokens.

**Note**. Spring Boot version has to be 2.2.5.RELEASE as well. And Java 16 is not supported. Hence, use Java 11. Another note is that 2.2.5.RELEASE is not working.

```
<parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.10.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>spring-security</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>spring-security</name>
    <description>Demo project for Spring Boot</description>
    <properties>
        <java.version>11</java.version>
        <spring-cloud.version>Hoxton.SR11</spring-cloud.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-oauth2</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
```

We can now define a configuration class, which I call AuthServerConfig. Besides the classic @Configuration annotation, we also need to annotate this class with @EnableAuthorizationServer. This way, we instruct Spring Boot to enable the configuration specific to the OAuth 2 authorization server. We can customize this configuration by extending the AuthorizationServerConfigurerAdapter class and overriding specific methods

```java
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
}
```

## Defining user management

In this section, we discuss user management. The authorization server is the component that deals with authenticating a user in the OAuth 2 framework. So, naturally, it needs to manage users. Fortunately, the user management implementation hasn’t changed from what you learned in chapters 3 and 4. We continue to use the UserDetails, UserDetailsService, and UserDetailsManager contracts to manage credentials. And to manage passwords, we continue to use the PasswordEncoder contract.

Figure 13.2 reminds you of the main components acting in the authentication process in Spring Security. What you should observe differently from the way we described the authentication architecture until now is that we don’t have a SecurityContext in this diagram anymore. This change happened because the result of authentication is not stored in the SecurityContext. The authentication is instead managed with a token from a TokenStore. You’ll learn more about the TokenStore in chapter 14, where we discuss the resource server.

![chapter-13-figure-13-2.PNG](pictures/chapter-13-figure-13-2.PNG)

Let’s find out how to implement user management in our authorization server. I always prefer to separate the responsibilities of the configuration classes. For this reason, I chose to define a second configuration class in our application, where I only write the configurations needed for user management. I named this class WebSecurityConfig, and you can see its implementation in the following listing.

```java
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        var uds = new InMemoryUserDetailsManager();

        var user = User.withUsername("john")
                .password("12345")
                .authorities("read")
                .build();

        uds.createUser(user);

        return uds;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
```

Now that we have users, we only need to link user management to the authorization server configuration. To do this, I expose the AuthenticationManager as a bean in the Spring context, and then I use it in the AuthServerConfig class.

```java
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    
    private final AuthenticationManager authenticationManager;

    public AuthServerConfig(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
    }
}
```

With these configurations in place, we now have users who can authenticate at our authentication server. But the OAuth 2 architecture implies that users grant privileges to a client. It is the client that uses resources on behalf of a user. In section 13.3, you’ll learn how to configure the clients for the authorization server.

## Registering clients with the authorization server

To call the authorization server, an app acting as a client in the OAuth 2 architecture needs its own credentials. The authorization server also manages these credentials and only allows requests from known clients (figure 13.3).

![chapter-13-figure-13-3.PNG](pictures/chapter-13-figure-13-3.PNG)

Do you remember the client application we developed in chapter 12? We used GitHub as our authentication server. GitHub needed to know about the client app, so the first thing we did was register the application at GitHub. We then received a client ID and a client secret: the client credentials. We configured these credentials, and our app used them to authenticate with the authorization server (GitHub). The same applies in this case. Our authorization server needs to know its clients because it accepts requests from them. Here the process should become familiar. The contract that defines the client for the authorization server is ClientDetails. The contract defining the object to retrieve ClientDetails by their IDs is ClientDetailsService.

Do these names sound familiar? These interfaces work like the UserDetails and the UserDetailsService interfaces, but these represent the clients. You’ll find that many of the things we discussed in chapter 3 work similarly for ClientDetails and ClientDetailsService. For example, our InMemoryClientDetailsService is an implementation of the ClientDetailsService interface, which manages ClientDetails in memory. It works similarly to the InMemoryUserDetailsManager class for UserDetails. Likewise, JdbcClientDetailsService is similar to JdbcUserDetailsManager.

![chapter-13-figure-13-4.PNG](pictures/chapter-13-figure-13-4.PNG)

We can sum up these similarities in a few points that you can easily remember: 
* ClientDetails is for the client what UserDetails is for the user. 
* ClientDetailsService is for the client what UserDetailsService is for the user. 
* InMemoryClientDetailsService is for the client what InMemoryUserDetailsManager is for the user. 
* JdbcClientDetailsService is for the client what JdbcUserDetailsManager is for the user.

```java
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;

    public AuthServerConfig(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        var service = new InMemoryClientDetailsService();

        var cd = new BaseClientDetails();
        cd.setClientId("client");
        cd.setClientSecret("secret");
        cd.setScope(List.of("read"));
        cd.setAuthorizedGrantTypes(List.of("password"));

        service.setClientDetailsStore(Map.of("client", cd));
        clients.withClientDetails(service);
    }
}
```

Or same can be written in:
```java
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("client")
                .secret("secret")
                .authorizedGrantTypes("password")
                .scopes("read");
    }
```

## Using the password grant type

Let’s start the application and test it. We can request a token at the /oauth/token endpoint. Spring Security automatically configures this endpoint for us. We use the client credentials with HTTP Basic to access the endpoint and send the needed details as query parameters. As you know from chapter 12, the parameters we need to send in this request are
* **grant_type** with the value password
* **username** and **password**, which are the user credentials
* **scope**, which is the granted authority

```
curl -v -XPOST -u client:secret "http://localhost:8080/oauth/token?grant_type=password&username=john&password=12345&scope=read"
```

```
{"access_token":"1c4030f8-3206-4f1e-bb59-dbb68a9f5297","token_type":"bearer","expires_in":43199,"scope":"read"}
```

Observe the access token in the response. With the default configuration in Spring Security, a token is a simple UUID. The client can now use this token to call the resources exposed by the resource server.

## Using the authorization code grant type

You used this grant type with the client application we developed in chapter 12, and you know it’s one of the most commonly used OAuth 2 grant types.

![chapter-13-figure-13-6.PNG](pictures/chapter-13-figure-13-6.PNG)

As you learned in section 13.3, it’s all about how you register the client. So, all you need to do to use another grant type is set it up in the client registration.

For the authorization code grant type, you also need to provide the redirect URI. This is the URI to which the authorization server redirects the user once it completes authentication. When calling the redirect URI, the authorization server also provides the access code.

```java
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    // Omitted code
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("client")
                .secret("secret")
                .authorizedGrantTypes("authorization_code")
                .scopes("read")
                .redirectUris("http://localhost:9090/home");
    }
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager);
    }
}
```

You can have multiple clients, and each might use different grants. But it’s also possible  to set up multiple grants for one client. The authorization server acts according to  the client’s request.

```java
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    // Omitted code
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("client1")
                .secret("secret1")
                .authorizedGrantTypes("authorization_code")
                .scopes("read")
                .redirectUris("http://localhost:9090/home")
                .and()
                .withClient("client2")
                .secret("secret2")
                .authorizedGrantTypes("authorization_code", "password", "refresh_token")
                .scopes("read")
                .redirectUris("http://localhost:9090/home");
    }
    @Override
    public void configure(
            AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager);
    }
}
```

When we want to accept the authorization code grant type, the server also needs to provide a page where the client redirects the user for login. We implement this page using the form-login configuration you learned in chapter 5. You need to override the configure() method as presented:

```java
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    // Omitted code
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin();
    }
}
```

You can now start the application and access the link in your browser as presented by the following code snippet. Then you are redirected to the login page as presented in figure 13.7.

```shell
http://localhost:8080/oauth/authorize?response_type=code&client_id=client&scope=read
```

![chapter-13-fiture-13-7.PNG](pictures/chapter-13-fiture-13-7.PNG)

After logging in, the authorization server explicitly asks you to grant or reject the requested scopes. Figure 13.8 shows this form.

![chapter-13-fiture-13-8.PNG](pictures/chapter-13-fiture-13-8.PNG)

Once you grant the scopes, the authorization server redirects you to the redirect URI and provides an access token. In the next code snippet, you find the URL to which the authorization server redirected me.

```shell
http://localhost:9090/home?code=qeSLSt //This is authorization code
```

Your application can use the authorization code now to obtain a token calling the /oauth/token endpoint:

```shell
curl -v -XPOST -u client:secret "http://localhost:8080/oauth/token?grant_type=authorization_code&scope=read&code=qeSLSt"
```

The response body is

```json
{
    "access_token":"0fa3b7d3-e2d7-4c53-8121-bd531a870635",
    "token_type":"bearer",
    "expires_in":43052,
    "scope":"read"
}
```

Mind that an authorization code can only be used once. If you try to call the /oauth/ token endpoint using the same code again, you receive an error like the one displayed in the next code snippet. You can only obtain another valid authorization code by asking the user to log in again.

```json
{
    "error":"invalid_grant",
    "error_description":"Invalid authorization code: qeSLSt"
}
```

## Using the client credentials grant type

You may remember from chapter 12 that we use this grant type for backend-to-backend authentications. It’s not mandatory in this case, but sometimes we see this grant type as an alternative to the API key authentication method we discussed in chapter 8. We might use the client credentials grant type also when we secure an endpoint that’s unrelated to a specific user and for which the client needs access. Let’s say you want to implement an endpoint that returns the status of the server. The client calls this endpoint to check the connectivity and eventually displays a connection status to the user or an error message. Because this endpoint only represents a deal between the client and the resource server, and is not involved with any user-specific resource, the client should be able to call it without needing the user to authenticate. For such a scenario, we use the client credentials grant type.

As you’d expect, to use the client credentials grant type, a client must be registered with this grant.

```java
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig
extends AuthorizationServerConfigurerAdapter {
    // Omitted code
    @Override
    public void configure(
    ClientDetailsServiceConfigurer clients) throws Exception {
    clients.inMemory()
        .withClient("client")
        .secret("secret")
        .authorizedGrantTypes("client_credentials")
        .scopes("info");
    }
}
```

You can start the application now and call the /oauth/token endpoint to get an access token. The next code snippet shows you how to obtain this:
```shell
curl -v -XPOST -u client:secret "http://localhost:8080/oauth/token?grant_type=client_credentials&scope=info"
```

The response body is
```json
{ 
  "access_token":"431eb294-bca4-4164-a82c-e08f56055f3f", 
  "token_type":"bearer", 
  "expires_in":4300, 
  "scope":"info"
}
```

Be careful with the client credentials grant type. This grant type only requires the client to use its credentials. **Make sure that you don’t offer it access to the same scopes as flows that require user credentials. Otherwise, you might allow the client access to the users’ resources without needing the permission of the user.**

Figure 13.10 presents such a design in which the developer created a security breach by allowing the client to call a user’s resource endpoint without needing the user to authenticate first.

![chapter-13-fiture-13-10.PNG](pictures/chapter-13-fiture-13-10.PNG)

## Using the refresh token grant type

In this section, we discuss using refresh tokens with the authorization server developed with Spring Security. As you may recall from chapter 12, refresh tokens offer several advantages when used together with another grant type. You can use refresh tokens with the authorization code grant type and with the password grant type (figure 13.11).

![chapter-13-figure-13-11.PNG](pictures/chapter-13-figure-13-11.PNG)

If you want your authorization server to support refresh tokens, you need to add the refresh token grant to the grant list of the client.

```java
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("client")
                .secret("secret")
                .authorizedGrantTypes("password", "refresh_token") //Adds refresh token
                .scopes("read");
    }
```
Now try the same cURL command you used in section 13.4. You’ll see the response is similar but now includes a refresh token:

```shell
curl -v -XPOST -u client:secret "http://localhost:8080/oauth/token?grant_type=password&username=john&password=12345&scope=read"
```

```json
{
  "access_token":"f430bfa1-b409-4636-8ffd-2e59278811cc",
  "token_type":"bearer",
  "refresh_token":"30cf1bd7-9fac-4ed6-a633-370ae167885d",
  "expires_in":43199,
  "scope":"read"
}
```


# Chapter 14. OAuth 2: Implementing the resource server

In this chapter, we’ll discuss implementing a resource server with Spring Security.

The name resource server might not be suggestive to begin with, but in terms of OAuth 2, it **represents the backend you secure** just like any other app we secured in the previous chapters.

There are three options how the resource server validates tokens. **The first option allows the resource server to directly call the authorization server to verify an issued token.**

![chapter-14-figure-14-2.PNG](pictures/chapter-14-figure-14-2.PNG)

The second option **uses a common database where the authorization server stores tokens, and then the resource server can access and validate the tokens** (figure 14.3). This approach is also called **blackboarding.**

![chapter-14-figure-14-3.PNG](pictures/chapter-14-figure-14-3.PNG)

Finally, **the third option uses cryptographic signatures** (figure 14.4). The authorization server signs the token when issuing it, and the resource server validates the signature. Here’s where we generally use **JSON Web Tokens (JWTs)**. We discuss this approach in chapter 15.

## Implementing a resource server

![chapter-14-figure-14-4.PNG](pictures/chapter-14-figure-14-4.PNG)

We start with the implementation of our first resource server application, the last piece of the OAuth 2 puzzle. The reason why we have an authorization server that issues tokens is to allow clients to access a user’s resources. The resource server manages and protects the user’s resources.

**NOTE.** As in the case of the authorization server, the implementation of the resource server suffered changes in the Spring community. These changes affect us because now, in practice, you find different ways in which developers implement the resource server. I provide examples in which you can configure the resource server in two ways, such that when you encounter these in real-world scenarios, you will understand and be able to use both.

![chapter-14-figure-14-5.PNG](pictures/chapter-14-figure-14-5.PNG)

```java
<parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.10.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>spring-security</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>spring-security</name>
    <description>Demo project for Spring Boot</description>
    <properties>
        <java.version>11</java.version>
        <spring-cloud.version>Hoxton.SR11</spring-cloud.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-oauth2</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

To prove how it works, we need a resource that we want to access. We create a /hello endpoint for our tests by defining the usual controller.

```java
@RestController
public class HelloController {
    
    @GetMapping("hello")
    public String hello() {
        return "Hello";
    }
}
```

The other thing we need is a configuration class in which we use the @EnableResourceServer annotation to allow Spring Boot to configure what’s needed for our app to become a resource server.

```java
@Configuration
@EnableResourceServer
public class ResourceServerConfig {
}
```

We have a resource server now. But it’s not useful if you can’t access the endpoint, as is our case because we didn’t configure any way in which the resource server can check tokens. You know that requests made for resources need to also provide a valid access token. Even if it does provide a valid access token, a request still won’t work. Our resource server cannot verify that these are valid tokens, that the authorization server indeed issued them.

**NOTE.** As I mentioned in an earlier note, the resource server implementation changed as well. The @EnableResourceServer annotation, which is part of the Spring Security OAuth project, was recently marked as deprecated.

## Checking the token remotely

In this section, we implement token validation by allowing the resource server to call the authorization server directly. This approach is the simplest you can implement to enable access to the resource server with a valid access token. You choose this approach if the tokens in your system are plain (for example, simple UUIDs as in the default implementation of the authorization server with Spring Security).

![chapter-14-figure-14-6.PNG](pictures/chapter-14-figure-14-6.PNG)

The advantage of this approach is its simplicity. You can apply it to any kind of token implementation. The disadvantage of this approach is that for each request on the resource server having a new, as yet unknown token, the resource server calls the authorization server to validate the token. These calls can put an unnecessary load on the authorization server. Also, remember the rule of thumb: the network is not 100% reliable. You need to keep this in mind every time you design a new remote call in your architecture. You might also need to apply some alternative solutions for what happens if the call fails because of some network instability.

We want to allow a client to access the /hello endpoint if it provides an access token issued by an authorization server.

By default, the authorization server implements the endpoint /oauth/check_token that the resource server can use to validate a token. However, at present the authorization server implicitly denies all requests to that endpoint. Before using the /oauth/ check_token endpoint, you need to make sure the resource server can call it.

To allow authenticated requests to call the /oauth/check_token endpoint, we override the configure(AuthorizationServerSecurityConfigurer c) method in the AuthServerConfig class of the authorization server. Overriding the configure() method allows us to set the condition in which we can call the /oauth/ check_token endpoint.

```java
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    
    private final AuthenticationManager authenticationManager;

    public AuthServerConfig(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("client")
                .secret("secret")
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("read");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.checkTokenAccess("isAuthenticated()"); //Specifies the condition for which we can call the check_token enpoint
    }
}
```

**NOTE.** You can even make this endpoint accessible without authentication by using permitAll() instead of isAuthenticated(). But it’s not recommended to leave endpoints unprotected. Preferably, in a real-world scenario, you would use authentication for this endpoint.

Besides making this endpoint accessible, if we decide to allow only authenticated access, then we need a client registration for the resource server itself. For the authorization server, the resource server is also a client and requires its own credentials. We add these as for any other client. For the resource server, you don’t need any grant type or scope, but only a set of credentials that the resource server uses to call the check_token endpoint.

```java
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("client")
                .secret("secret")
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("read")
                  .and()
                .withClient("resourceserver")
                .secret("resourceserversecret");
    }
```

```java
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        var uds = new InMemoryUserDetailsManager();

        var user = User.withUsername("john")
                .password("12345")
                .authorities("read")
                .build();

        uds.createUser(user);

        return uds;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
```

You can now start the authorization server and obtain a token like you learned in chapter 13.

```shell
curl -v -XPOST -u client:secret "http://localhost:8080/oauth/token?grant_type=password&username=john&password=12345&scope=read"
```

```json
{
    "access_token":"4f2b7a6d-ced2-43dc-86d7-cbe844d3e16b",
    "token_type":"bearer",
    "refresh_token":"a4bd4660-9bb3-450e-aa28-2e031877cb36",
    "expires_in":43199,"scope":"read"
}
```

Next, we call the check_token endpoint to find the details about the access token we obtained in the previous code snippet.

```shell
curl -XPOST -u resourceserver:resourceserversecret "http://localhost:8080/oauth/check_token?token=4f2b7a6d-ced2-43dc-86d7-cbe844d3e16b"
```

```json
{
    "active":true,
    "exp":1581307166,
    "user_name":"john",
    "authorities":["read"],
    "client_id":"client",
    "scope":["read"]
}
```

Observe the response we get back from the check_token endpoint. It tells us all the details needed about the access token: 
* Whether the token is still active and when it expires 
* The user the token was issued for 
* The authorities that represent the privileges 
* The client the token was issued for

Now, if we call the endpoint using cURL, the resource server should be able to use it to validate tokens. We need to configure the endpoint of the authorization server and the credentials the resource server uses to access endpoint. We can do all this in the application.properties file.

```properties
server.port=9090
security.oauth2.resource.token-info-uri=http://localhost:8080/oauth/check_token
security.oauth2.client.client-id=resourceserver
security.oauth2.client.client-secret=resourceserversecret
```

```shell
curl -H "Authorization: bearer 4f2b7a6d-ced2-43dc-86d7-cbe844d3e16b" "http://localhost:9090/hello"
```

If you had called the endpoint without a token or with the wrong one, the result would have been a 401 Unauthorized status on the HTTP response.

```shell
curl -v "http://localhost:9090/hello"
```

The (truncated) response is:

```
...
< HTTP/1.1 401
...
{
    "error":"unauthorized",
    "error_description":"Full authentication is required to access this resource"
}
```

#### Using token introspection without Spring Security OAuth

A common concern nowadays is how to implement a resource server as in the previous example without Spring Security OAuth. If you remember, we discussed httpBasic(), formLogin(), and other authentication methods in the previous chapters. You learned that when calling such a method, you simply add a new filter to the filter chain, which enables a different authentication mechanism in your app. Guess what? In its latest versions, Spring Security also offers an oauth2ResourceServer() method that enables a resource server authentication method. However, mind that this functionality isn’t mature yet, and to use it, you need to add other dependencies that are not automatically figured out by Spring Boot. The following code snippet presents the required dependencies for implementing a resource server using token introspection:

```java
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-oauth2-resource-server</artifactId>
    <version>5.2.1.RELEASE</version>
</dependency>

<dependency>
    <groupId>com.nimbusds</groupId>
    <artifactId>oauth2-oidc-sdk</artifactId>
    <version>8.4</version>
    <scope>runtime</scope>
</dependency>
```

```java
@Configuration
public class ResourceServerConfig
extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .anyRequest().authenticated()
        .and()
        .oauth2ResourceServer(c -> c.opaqueToken(o -> {
                o.introspectionUri("…");
                o.introspectionClientCredentials("client", "secret");
            })
        );
    }
}
```

To make the code snippet easier to be read, I omitted the parameter value of the introspectionUri() method, which is the check_token URI, also known as the introspection token URI. As a parameter to the oauth2ResourceServer() method, I added a Customizer instance. Using the Customizer instance, you specify the parameters needed for the resource server depending on the approach you choose. For direct token introspection, you need to specify the URI the resource server calls to validate the token, and the credentials the resource server needs to authenticate when calling this URI.

## Implementing blackboarding with a JdbcTokenStore

In this section, we implement an application where the authorization server and the resource server use a shared database. We call this architectural style blackboarding. Why blackboarding? You can think of this as the authorization server and the resource server using a blackboard to manage tokens. This approach for issuing and validating tokens has the advantage of eliminating direct communication between the resource server and the authorization server.

![chapter-14-figure-14-8.PNG](pictures/chapter-14-figure-14-8.PNG)

The contract representing the object that manages tokens in Spring Security, both on the authorization server as well as for the resource server, is the TokenStore.

![chapter-14-figure-14-9.PNG](pictures/chapter-14-figure-14-9.PNG)

For the authorization server, you can visualize its place in the authentication architecture where we previously used SecurityContext. Once authentication finishes, the authorization server uses the TokenStore to generate a token

![chapter-14-figure-14-10.PNG](pictures/chapter-14-figure-14-10.PNG)

For the resource server, the authentication filter uses TokenStore to validate the token and find the user details that it later uses for authorization. The resource server then stores the user’s details in the security context.

![chapter-14-figure-14-11.PNG](pictures/chapter-14-figure-14-11.PNG)

Spring Security offers various implementations for the TokenStore contract, and in most cases, you won’t need to write your own implementation. For example, for all the previous authorization server implementations, we did not specify a TokenStore implementation. Spring Security provided a default token store of type InMemoryTokenStore. As you can imagine, in all these cases, the tokens were stored in the application’s memory. They did not persist! If you restart the authorization server, the tokens issued before the restart won’t be valid anymore.

To implement token management with blackboarding, Spring Security offers the JdbcTokenStore implementation. As the name suggests, this token store works with a database directly via JDBC. It works similarly to the JdbcUserDetailsManager we discussed in chapter 3, but instead of managing users, the JdbcTokenStore manages tokens.

JdbcTokenStore expects you to have two tables in the database. It uses one table to store access tokens (the name for this table should be oauth_access _token) and one table to store refresh tokens (the name for this table should be oauth_refresh_token). The table used to store tokens persists the refresh tokens.

We need to change our pom.xml file to declare the necessary dependencies to connect to our database. The next code snippet presents the dependencies I use in my pom.xml file:

```java
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-oauth2</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
```

schema.sql file
```sql
CREATE TABLE IF NOT EXISTS `oauth_access_token` (
    `token_id` varchar(255) NOT NULL,
    `token` blob,
    `authentication_id` varchar(255) DEFAULT NULL,
    `user_name` varchar(255) DEFAULT NULL,
    `client_id` varchar(255) DEFAULT NULL,
    `authentication` blob,
    `refresh_token` varchar(255) DEFAULT NULL,
PRIMARY KEY (`token_id`));

CREATE TABLE IF NOT EXISTS `oauth_refresh_token` (
    `token_id` varchar(255) NOT NULL,
    `token` blob,
    `authentication` blob,
PRIMARY KEY (`token_id`));
```

```java
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;
    
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("client")
                .secret("secret")
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("read");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .authenticationManager(authenticationManager)
                .tokenStore(tokenStore()); //Configure token store
    }

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }
}
```

We can now start our authorization server and issue tokens. We issue tokens in the same way we did in chapter 13 and earlier in this chapter. From this perspective, nothing’s changed. But now, we can see our tokens stored in the database as well.

```shell
curl -v -XPOST -u client:secret "http://localhost:8080/oauth/token?grant_type=password&username=john&password=12345&scope=read"
```

The response body is

```json
{
    "access_token":"009549ee-fd3e-40b0-a56c-6d28836c4384",
    "token_type":"bearer",
    "refresh_token":"fd44d772-18b3-4668-9981-86373017e12d",
    "expires_in":43199,
    "scope":"read"
}
```

The access token returned in the response can also be found as a record in the oauth_access_token table. Because I configure the refresh token grant type, I receive a refresh token. For this reason, I also find a record for the refresh token in the oauth_refresh_token table.

It’s time now to configure the resource server so that it also uses the same database.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-oauth2</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
```

In the application.properties file, I configure the data source so the resource server can connect to the same database as the authorization server.

In the configuration class of the resource server, we inject the data source and configure JdbcTokenStore.

```java
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    
    @Autowired
    private DataSource dataSource;
    
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.tokenStore(tokenStore());
    }
    
    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }
}
```

You can now start your resource server as well and call the /hello endpoint with the access token you previously issued.

```shell
curl -H "Authorization:Bearer 009549ee-fd3e-40b0-a56c-6d28836c4384" "http:// localhost:9090/hello"
```

The response body is
```shell
Hello
```

But having both the authorization server and the resource server depend on the same database presents a disadvantage. In the case of a large number of requests, this dependency might become a bottleneck and slow down the system.

## A short comparison of approaches

In this chapter, you learned to implement two approaches for allowing the resource server to validate tokens it receives from the client: 
* **Directly calling the authorization server.** When the resource server needs to validate a token, it directly calls the authorization server that issues that token. 
* **Using a shared database (blackboarding).** Both the authorization server and the resource server work with the same database. The authorization server stores the issued tokens in the database, and the resource server reads those for validation.

![chapter-14-figure-14-table-1.PNG](pictures/chapter-14-figure-14-table-1.PNG)

# Chapter 15. OAuth 2: Using JWT and cryptographic signatures

In this chapter, we’ll discuss using JSON Web Tokens (JWTs) for token implementation. You learned in chapter 14 that the resource server needs to validate tokens issued by the authorization server. And I told you three ways to do this: 
* Using direct calls between the resource server and the authorization server, which we implemented in section 14.2 
* Using a shared database for storing the tokens, which we implemented in section 14.3 
* Using cryptographic signatures, which we’ll discuss in this chapter

Using cryptographic signatures to validate tokens has the advantage of allowing the resource server to validate them without needing to call the authorization server directly and without needing a shared database. This approach to implementing token validation is commonly used in systems implementing authentication and authorization with OAuth 2.

## Using tokens signed with symmetric keys with JWT

The most straightforward approach to signing tokens is using symmetric keys. With this approach, using the same key, you can both sign a token and validate its signature. Using symmetric keys for signing tokens has the advantage of being simpler than other approaches we’ll discuss later in this chapter and is also faster. As you’ll see, however, it has disadvantages too. You can’t always share the key used to sign tokens with all the applications involved in the authentication process.

### Using JWTs

A JWT is a token implementation. A token consists of three parts: the header, the body, and the signature. The details in the header and the body are represented with JSON, and they are Base64 encoded. The third part is the signature, generated using a cryptographic algorithm that uses as input the header and the body (figure 15.1). The cryptographic algorithm also implies the need for a key. The key is like a password. Someone having a proper key can sign a token or validate that a signature is authentic. If the signature on a token is authentic, that guarantees that nobody altered the token after it was signed.

![chapter-15-figure-15-1.PNG](pictures/chapter-15-figure-15-1.PNG)

When a JWT is signed, we also call it a JWS (JSON Web Token Signed). Usually, applying a cryptographic algorithm for signing a token is enough, but sometimes you can choose to encrypt it. If a token is signed, you can see its contents without having any key or password. But even if a hacker sees the contents in the token, they can’t change a token’s contents because if they do so, the signature becomes invalid. To be valid, a signature has to 
* Be generated with the correct key 
* Match the content that was signed

If a token is encrypted, we also call it a JWE (JSON Web Token Encrypted). You can’t see the contents of an encrypted token without a valid key.

### Implementing an authorization server to issue JWTs

In this section, we implement an authorization server that issues JWTs to a client for authorization. You learned in chapter 14 that the component managing the tokens is the TokenStore. What we do in this section is use a different implementation of the TokenStore provided by Spring Security. The name of the implementation we use is JwtTokenStore, and it manages JWTs. We also test the authorization server in this section. Later, in section 15.1.3, we’ll implement a resource server and have a complete system that uses JWTs. You can implement token validation with JWT in two ways: 
* If we use the same key for signing the token as well as for verifying the signature, we say that the key is **symmetric**. 
* If we use one key to sign the token but a different one to verify the signature, we say that we use an **asymmetric** key pair.

In this example, we implement signing with a symmetric key. This approach implies that both the authorization server and the resource server know and use the same key. The authorization server signs the token with the key, and the resource server validates the signature using the same key.

```xml
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.10.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>spring-auth-server</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>spring-auth-server</name>
    <description>Demo project for Spring Boot</description>
    
    <properties>
        <java.version>11</java.version>
        <spring-cloud.version>Hoxton.SR11</spring-cloud.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-oauth2</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
```

We configure a JwtTokenStore in the same way we did in chapter 14 for the JdbcTokenStore. Additionally, we need to define an object of type JwtAccessTokenConverter. With the JwtAccessTokenConverter, we configure how the authorization server validates tokens; in our case, using a symmetric key.

```java
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    private final String jwtKey;
    private final AuthenticationManager authenticationManager;

    public AuthServerConfig(@Value("${jwt.key}") String jwtKey, AuthenticationManager authenticationManager) {
        this.jwtKey = jwtKey;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("client")
                .secret("secret")
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("read");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore())
                .accessTokenConverter(jwtAccessTokenConverter());
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        var converter = new JwtAccessTokenConverter();
        converter.setSigningKey(jwtKey);
        return converter;
    }
}
```

I stored the value of the symmetric key for this example in the application.properties file, as the next code snippet shows. However, don’t forget that the signing key is sensitive data, and you should store it in a secrets vault in a real-world scenario.

```properties
jwt.key=MjWP5L7CiD
```

Remember from our previous examples with the authorization server in chapters 13 and 14 that for every authorization server, we also define a UserDetailsServer and PasswordEncoder.

```java
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        var uds = new InMemoryUserDetailsManager();

        var user = User.withUsername("john")
                .password("12345")
                .authorities("read")
                .build();

        uds.createUser(user);

        return uds;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
```

We can now start the authorization server and call the /oauth/token endpoint to obtain an access token.

```shell
curl -v -XPOST -u client:secret "http://localhost:8080/oauth/token?grant_type=password&username=john&password=12345&scope=read"
```

The response body is

```json
{
    "access_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXV…",
    "token_type":"bearer",
    "refresh_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6Ikp…",
    "expires_in":43199,
    "scope":"read",
    "jti":"7774532f-b74b-4e6b-ab16-208c46a19560"
}
```

You can observe in the response that both the access and the refresh tokens are now JWTs. In the code snippet, I have shortened the tokens to make the code snippet more readable.

```json
{
    "user_name": "john",
    "scope": ["read"],
    "generatedInZone": "Europe/Bucharest",
    "exp": 1583874061,
    "authorities": ["read"],
    "jti": "38d03577-b6c8-47f5-8c06-d2e3a713d986",
    "client_id": "client"
}
```

### Implementing a resource server that uses JWT

In this section, we implement the resource server, which uses the symmetric key to validate tokens issued by the authorization server.

```xml
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.10.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>spring-security</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>spring-security</name>
    <description>Demo project for Spring Boot</description>
    <properties>
        <java.version>11</java.version>
        <spring-cloud.version>Hoxton.SR11</spring-cloud.version>
    </properties>
    <dependencies>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-oauth2</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
```

I didn’t add any new dependencies to what we already used in chapters 13 and 14. Because we need one endpoint to secure, I define a controller and a method to expose a simple endpoint that we use to test the resource server.

```java
@RestController
public class HelloController {

    @GetMapping("hello")
    public String hello() {
        return "Hello";
    }
}
```

Now that we have an endpoint to secure, we can declare the configuration class where we configure the TokenStore. We’ll configure the TokenStore for the resource server as we do for the authorization server. The most important aspect is to be sure we use the same value for the key. The resource server needs the key to validate a token’s signature.

```java
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final String jwtKey;

    public ResourceServerConfig(@Value("${jwt.key}")String jwtKey) {
        this.jwtKey = jwtKey;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.tokenStore(tokenStore());
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        var converter = new JwtAccessTokenConverter();
        converter.setSigningKey(jwtKey);
        return converter;
    }
}
```

```properties
server.port=9090
jwt.key=MjWP5L7CiD
```

A key used for symmetric encryption or signing is just a random string of bytes. You generate it using an algorithm for randomness. In our example, you can use any string value, say “abcde.” In a real-world scenario, it’s a good idea to use a randomly generated value with a length, preferably, longer than 258 bytes.

We can now start our resource server and call the /hello endpoint using a valid JWT that you obtained earlier from the authorization server. You have to add the token to the Authorization HTTP header on the request prefixed with the word “Bearer” in our example.

```shell
curl -H "Authorization:Bearer eyJhbGciOiJIUzI1NiIs…" http://localhost:9090/hello
```

The response body:
```shell
Hello
```

### Using symmetric keys without the Spring Security OAuth project

You, therefore, need to know this approach for future implementations and, of course, if you want to migrate an existing project to it. The next code snippet shows you how to configure JWT authentication using symmetric keys without the classes of the Spring Security OAuth project:

```java
@Configuration
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {
    @Value("${jwt.key}")
    private String jwtKey;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests().anyRequest().authenticated()
    .and()
    .oauth2ResourceServer(c -> c.jwt(j -> j.decoder(jwtDecoder())));
    }
    // Omitted code
}
```

As you can see, this time I use the jwt() method of the Customizer object sent as a parameter to oauth2ResourceServer(). Using the jwt() method, we configure the details needed by our app to validate tokens. In this case, because we are discussing validation using symmetric keys, I create a JwtDecoder in the same class to provide the value of the symmetric key. The next code snippet shows how I set this decoder using the decoder() method:

```java
@Bean
public JwtDecoder jwtDecoder() {
    byte [] key = jwtKey.getBytes();
    SecretKey originalKey = new SecretKeySpec(key, 0, key.length, "AES");
    NimbusJwtDecoder jwtDecoder =NimbusJwtDecoder.withSecretKey(originalKey).build();
    return jwtDecoder;
}
```

## Using tokens signed with asymmetric keys with JWT

In this section, we implement an example of OAuth 2 authentication where the authorization server and the resource server use an asymmetric key pair to sign and validate tokens. Sometimes having only a key shared by the authorization server and the resource server, as we implemented in section 15.1, is not doable. Often, this scenario happens if the authorization server and the resource server aren’t developed by the same organization. In this case, we say that the authorization server doesn’t “trust” the resource server, so you don’t want the authorization server to share a key with the resource server.

What is an asymmetric key pair and how does it work? The concept is quite simple. An asymmetric key pair has two keys: one called the private key and another called the public key. The authorization server uses the private key to sign tokens.

![chapter-15-figure-15-6.PNG](pictures/chapter-15-figure-15-6.PNG)

### Generating the key pair

#### GENERATING A PRIVATE KEY

To generate a private key, run the keytool command in the next code snippet. It generates a private key in a file named ssia.jks. I also use the password “ssia123” to protect the private key and the alias “ssia” to give the key a name. In the following command, you can see the algorithm used to generate the key, RSA:
```shell
keytool -genkeypair -alias ssia -keyalg RSA -keypass ssia123 -keystore ssia.jks -storepass ssia123
```

#### OBTAINING THE PUBLIC KEY

To get the public key for the previously generated private key, you can run the keytool command:

```shell
keytool -list -rfc --keystore ssia.jks | openssl x509 -inform pem -pubkey
```

You are prompted to enter the password used when generating the public key; in my case, ssia123. Then you should find the public key and a certificate in the output.

```
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAijLqDcBHwtnsBw+WFSzG
VkjtCbO6NwKlYjS2PxE114XWf9H2j0dWmBu7NK+lV/JqpiOi0GzaLYYf4XtCJxTQ
DD2CeDUKczcd+fpnppripN5jRzhASJpr+ndj8431iAG/rvXrmZt3jLD3v6nwLDxz
pJGmVWzcV/OBXQZkd1LHOK5LEG0YCQ0jAU3ON7OZAnFn/DMJyDCky994UtaAYyAJ
7mr7IO1uHQxsBg7SiQGpApgDEK3Ty8gaFuafnExsYD+aqua1Ese+pluYnQxuxkk2
Ycsp48qtUv1TWp+TH3kooTM6eKcnpSweaYDvHd/ucNg8UDNpIqynM1eS7KpffKQm
DwIDAQAB
-----END PUBLIC KEY-----
```

That’s it! We have a private key we can use to sign JWTs and a public key we can use to validate the signature.

### Implementing an authorization server that uses private keys

In this section, we configure the authorization server to use a private key for signing JWTs.

I copy the private key file, ssia.jks, in the resources folder of my application. I add the key in the resources folder because it’s easier for me to read it directly from the classpath. However, it’s not mandatory to be in the classpath. In the application.properties file, I store the filename, the alias of the key, and the password I used to protect the private key when I generated the password.

```properties
password=ssia123
privateKey=ssia.jks
alias=ssia
```

```java
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    private final String keyPassword;
    private final String privateKeyPath;
    private final String keyAlias;
    private final AuthenticationManager authenticationManager;

    public AuthServerConfig(@Value("${password}") String keyPassword, @Value("${privateKey}") String privateKeyPath,
                            @Value("${alias}") String keyAlias, AuthenticationManager authenticationManager) {
        this.keyPassword = keyPassword;
        this.privateKeyPath = privateKeyPath;
        this.keyAlias = keyAlias;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("client")
                .secret("secret")
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("read");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore())
                .accessTokenConverter(jwtAccessTokenConverter());
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        var keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(privateKeyPath), keyPassword.toCharArray());
        var converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair(keyAlias));
        return converter;
    }
}
```

You can now start the authorization server and call the /oauth/token endpoint to generate a new access token. Of course, you only see a normal JWT created, but the difference is now that to validate its signature, you need to use the public key in the pair.

```shell
curl -v -XPOST -u client:secret "http://localhost:8080/oauth/token?grant_type=password&username=john&password=12345&scope=read"
```

The response body is:
```json
{
    "access_token":"eyJhbGciOiJSUzI1NiIsInR5…",
    "token_type":"bearer",
    "refresh_token":"eyJhbGciOiJSUzI1NiIsInR…",
    "expires_in":43199,
    "scope":"read",
    "jti":"8e74dd92-07e3-438a-881a-da06d6cbbe06"
}
```

### Implementing a resource server that uses public keys

In this section, we implement a resource server that uses the public key to verify the token’s signature. When we finish this section, you’ll have a full system that implements authentication over OAuth 2 and uses a public-private key pair to secure the tokens. The authorization server uses the private key to sign the tokens, and the resource server uses the public one to validate the signature.

The resource server needs to have the public key of the pair to validate the token’s signature, so let’s add this key to the application.properties file.

```properties
server.port=9090
publicKey=-----BEGIN PUBLIC KEY-----MIIBIjANBghk…-----END PUBLIC KEY----
```

```java
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final String publicKey;

    public ResourceServerConfig(@Value("${publicKey}") String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.tokenStore(tokenStore());
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        var converter = new JwtAccessTokenConverter();
        converter.setVerifierKey(publicKey);
        return converter;
    }
}
```

```java
@RestController
public class HelloController {

    @GetMapping("hello")
    public String hello() {
        return "Hello";
    }
}
```

```shell
curl -H "Authorization:Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6I…" http://localhost:9090/hello
```

### Using asymmetric keys without the Spring Security OAuth project

Actually, using asymmetric keys doesn’t differ too much from using a project with symmetric keys. The only change is the JwtDecoder you need to use.

```java
public JwtDecoder jwtDecoder() {
    try {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        var key = Base64.getDecoder().decode(publicKey);
        var x509 = new X509EncodedKeySpec(key);
        var rsaKey = (RSAPublicKey) keyFactory.generatePublic(x509);
        return NimbusJwtDecoder.withPublicKey(rsaKey).build();
    } catch (Exception e) {
        throw new RuntimeException("Wrong public key");
    }
}
```

```java
@Configuration public class ResourceServerConfig extends WebSecurityConfigurerAdapter {
    @Value("${publicKey}")
    private String publicKey;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.oauth2ResourceServer(c -> c.jwt(j -> j.decoder(jwtDecoder())));
        http.authorizeRequests().anyRequest().authenticated();
    }
// Omitted code
}
```

### Using an endpoint to expose the public key

In this section, we discuss a way of making the public key known to the resource server—the authorization server exposes the public key. In the system we implemented in section 15.2, we use private-public key pairs to sign and validate tokens. We configured the public key at the resource server side. The resource server uses the public key to validate JWTs. But what happens if you want to change the key pair? It is a good practice not to keep the same key pair forever, and this is what you learn to implement in this section.

Being set in two places makes the keys more difficult to manage. But if we configure them on one side only, you could manage the keys easier. The solution is moving the whole key pair to the authorization server side and allowing the authorization server to expose the public keys with an endpoint.

For the authorization server, we keep the same setup as for the project we developed. We only need to make sure we make accessible the endpoint, which exposes the public key. Yes, Spring Boot already configures such an endpoint, but it’s just that. By default, all requests for it are denied. We need to override the endpoint’s configuration and allow anyone with client credentials to access it.

```java
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("client")
                .secret("secret")
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("read")
                    .and()
                .withClient("resourceserver")
                .secret("resourceserversecret");
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("isAuthenticated()");
    }
```

```shell
curl -u resourceserver:resourceserversecret http://localhost:8080/oauth/token_key
```

The response body is:
```json
{
    "alg":"SHA256withRSA",
    "value":"-----BEGIN PUBLIC KEY----- nMIIBIjANBgkq... -----END PUBLIC KEY-----"
}
```

For the resource server to use this endpoint and obtain the public key, you only need to configure the endpoint and the credentials in its properties file.

```properties
server.port=9090
security.oauth2.resource.jwt.key-uri=http://localhost:8080/oauth/token_key
security.oauth2.client.client-id=resourceserver
security.oauth2.client.client-secret=resourceserversecret
```

Because the resource server now takes the public key from the /oauth/token_key endpoint of the authorization server, you don’t need to configure it in the resource server configuration class. The configuration class of the resource server can remain empty.

```java
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
}
```

You can start the resource server as well now and call the /hello endpoint it exposes to see that the entire setup works as expected.

```shell
curl -H "Authorization:Bearer eyJhbGciOiJSUzI1NiIsInR5cCI…" http://localhost:9090/hello
```

## Adding custom details to the JWT

In this section, we discuss adding custom details to the JWT token. In most cases, you need no more than what Spring Security already adds to the token. However, in realworld scenarios, you’ll sometimes find requirements for which you need to add custom details in the token. In this section, we implement an example in which you learn how to change the authorization server to add custom details on the JWT and how to change the resource server to read these details. If you take one of the tokens we generated in previous examples and decode it, you see the defaults that Spring Security adds to the token.

![chapter-15-figure-15-8.PNG](pictures/chapter-15-figure-15-8.PNG)

As you can see in listing 15.8, by default, a token generally stores all the details needed for Basic authorization. But what if the requirements of your real-world scenarios ask for something more? Some examples might be 
* You use an authorization server in an application where your readers review books. Some endpoints should only be accessible for users who have given more than a specific number of reviews. 
* You need to allow calls only if the user authenticated from a specific time zone. 
* Your authorization server is a social network, and some of your endpoints should be accessible only by users having a minimum number of connections.

### Configuring the authorization server to add custom details to tokens

In this section, we discuss the changes we need to make to the authorization server for adding custom details to tokens. To make the example simple, I suppose that the requirement is to add the time zone of the authorization server itself.

To add additional details to your token, you need to create an object of type TokenEnhancer.

```java
public class CustomTokenEnhancer implements TokenEnhancer {
    
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        var token = new DefaultOAuth2AccessToken(oAuth2AccessToken);
        Map<String, Object> info = Map.of("generatedInZone", ZoneId.systemDefault().toString());
        token.setAdditionalInformation(info);
        return token;
    }
}
```

The enhance() method of a TokenEnhancer object receives as a parameter the token we enhance and returns the “enhanced” token, containing the additional details.

```java
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    ...
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        var tokenEnhancerChain = new TokenEnhancerChain();
        var tokenEnhancers = List.of(new CustomTokenEnhancer(), jwtAccessTokenConverter());
        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);

        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore())
                .tokenEnhancer(tokenEnhancerChain);
    }
    ...
}
```

As you can observe, configuring our custom token enhancer is a bit more complicated. We have to create a chain of token enhancers and set the entire chain instead of only one object, because the access token converter object is also a token enhancer.

Let’s start the authorization server, generate a new access token, and inspect it to see how it looks.

```shell
curl -v -XPOST -u client:secret "http://localhost:8080/oauth/token?grant_type=password&username=john&password=12345&scope=read"
```

```json
{
    "access_token":"eyJhbGciOiJSUzI…",
    "token_type":"bearer",
    "refresh_token":"eyJhbGciOiJSUzI1…",
    "expires_in":43199,
    "scope":"read",
    "generatedInZone":"Europe/Bucharest",
    "jti":"0c39ace4-4991-40a2-80ad-e9fdeb14f9ec"
}
```

If you decode the token:
```json
{
    "user_name": "john",
    "scope": ["read"],
    "generatedInZone": "Europe/Bucharest",
    "exp": 1582591525,
    "authorities": ["read"],
    "jti": "0c39ace4-4991-40a2-80ad-e9fdeb14f9ec",
    "client_id": "client"
}
```

### Configuring the resource server to read the custom details of a JWT

Once you change your authorization server to add custom details to a JWT, you’d like the resource server to be able to read these details. The changes you need to do in your resource server to access the custom details are straightforward.

We discussed in section 15.1 that AccessTokenConverter is the object that converts the token to an Authentication. This is the object we need to change so that it also takes into consideration the custom details in the token. Previously, you created a bean of type JwtAccessTokenConverter, as shown

```java
@Bean
public JwtAccessTokenConverter jwtAccessTokenConverter() {
    var converter = new JwtAccessTokenConverter();
    converter.setSigningKey(jwtKey);
    return converter;
}
```

We used this token to set the key used by the resource server for token validation. We create a custom implementation of JwtAccessTokenConverter, which also takes into consideration our new details on the token. The simplest way is to extend this class and override the extractAuthentication() method. This method converts the token in an Authentication object.

```java
public class AdditionalClaimsAccessTokenConverter extends JwtAccessTokenConverter {

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        var oAuth2Authentication = super.extractAuthentication(map);
        oAuth2Authentication.setDetails(map);
        return oAuth2Authentication;
    }
}
```

In the configuration class of the resource server, you can now use the custom access token converter.

```java
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    
    // Omitted code
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        var converter = new AdditionalClaimsAccessTokenConverter();
        converter.setVerifierKey(publicKey);
        return converter;
    }
}
```

An easy way to test the changes is to inject them into the controller class and return them in the HTTP response.

```java
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello(OAuth2Authentication authentication) {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        return "Hello! " + details.getDecodedDetails();
    }
}
```

```shell
curl -H "Authorization:Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6Ikp… " http://localhost:9090/hello
```

Response:

```shell
Hello! {user_name=john, scope=[read], generatedInZone=Europe/Bucharest, exp=1582595692, authorities=[read], 
jti=982b02be-d185-48de-a4d3-9b27337d1a46, client_id=client}
```

# Chapter 16. Global method security: Pre- and postauthorizations

Up to now, we discussed various ways of configuring authentication. We started with the most straightforward approach, HTTP Basic, in chapter 2, and then I showed you how to set form login in chapter 5. We covered OAuth 2 in chapters 12 through 15. But in terms of authorization, we only discussed configuration at the endpoint level.

For non-web applications, global method security offers the opportunity to implement authorization rules even if we don’t have endpoints. In web applications, this approach gives us the flexibility to apply authorization rules on different layers of our app, not only at the endpoint level.

## Enabling global method security

By default, global method security is disabled, so if you want to use this functionality, you first need to enable it. Also, global method security offers multiple approaches for applying authorization.

Briefly, you can do two main things with global method security: 
* **Call authorization** — Decides whether someone can call a method according to some implemented privilege rules (preauthorization) or if someone can access what the method returns after the method executes (postauthorization).
* **Filtering** — Decides what a method can receive through its parameters (prefiltering) and what the caller can receive back from the method after the method executes (postfiltering). We’ll discuss and implement filtering in chapter 17.

### Understanding call authorization

One of the approaches for configuring authorization rules you use with global method security is call authorization. The call authorization approach refers to applying authorization rules that decide if a method can be called, or that allow the method to be called and then decide if the caller can access the value returned by the method. Often we need to decide if someone can access a piece of logic depending on either the provided parameters or its result.

How does global method security work? What’s the mechanism behind applying the authorization rules? When we enable global method security in our application, we actually enable a Spring aspect. This aspect intercepts the calls to the method for which we apply authorization rules and, based on these authorization rules, decides whether to forward the call to the intercepted method.

Briefly, we classify the call authorization as 
* **Preauthorization** — The framework checks the authorization rules before the method call. 
* **Postauthorization** — The framework checks the authorization rules after the method executes.

#### USING PREAUTHORIZATION TO SECURE ACCESS TO METHODS

Usually, we don’t want a functionality to be executed at all if some conditions aren’t met. You can apply conditions based on the authenticated user, and you can also refer to the values the method received through its parameters.

#### USING POSTAUTHORIZATION TO SECURE A METHOD CALL

With postauthorization, Spring Security checks the authorization rules after the method executes. You can use this kind of authorization to restrict access to the method return in certain conditions. Because postauthorization happens after method execution, you can apply the authorization rules on the result returned by the method.

**But be careful with postauthorization!** If the method mutates something during its execution, the change happens whether or not authorization succeeds in the end. **Even with the @Transactional annotation, a change isn’t rolled back if postauthorization fails.** The exception thrown by the postauthorization functionality happens after the transaction manager commits the transaction.

### Enabling global method security in your project

Global method security is enabled with @EnableGlobalMethodSecurity annotation. Global method security offers us three approaches to define the authorization rules that we discuss in this chapter: 
* The pre-/postauthorization annotations 
* The JSR 250 annotation, @RolesAllowed 
* The @Secured annotation

Because in almost all cases, pre-/postauthorization annotations are the only approach used, we discuss this approach in this chapter. To enable this approach, we use the prePostEnabled attribute of the @EnableGlobalMethodSecurity annotation.

```java
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ProjectConfig {}
```

You can use global method security with any authentication approach, from HTTP Basic authentication to OAuth 2. To keep it simple and allow you to focus on new details, we provide global method security with HTTP Basic authentication.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

## Applying preauthorization for authorities and roles

The application we implement in this section has a simple scenario. It exposes an endpoint, /hello, which returns the string "Hello, " followed by a name. To obtain the name, the controller calls a service method. This method applies a preauthorization rule to verify the user has write authority.

I added a UserDetailsService and a PasswordEncoder to make sure I have some users to authenticate. To validate our solution, we need two users: one user with write authority and another that doesn’t have write authority. We prove that the first user can successfully call the endpoint, while for the second user, the app throws an authorization exception when trying to call the method.

```java
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ProjectConfig {

    @Bean
    public UserDetailsManager userDetailsManager() {
        var natalie = User.withUsername("natalie")
                .password("12345")
                .authorities("read")
                .build();
        var emma = User.withUsername("emma")
                .password("12345")
                .authorities("write")
                .build();

        return new InMemoryUserDetailsManager(natalie, emma);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
```

To define the authorization rule for this method, we use the @PreAuthorize annotation. The @PreAuthorize annotation receives as a value a Spring Expression Language (SpEL) expression that describes the authorization rule.

```java
@Service
public class HelloService {

    @PreAuthorize("hasAuthority('write')")
    public String getName() {
        return "Fantastico";
    }
}
```

```java
@RestController
public class HelloController {

    private final HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, " + helloService.getName();
    }
}
```

```shell
curl -u emma:12345 http://localhost:8080/hello
Hello, Fantastico
```

```shell
curl -u natalie:12345 http://localhost:8080/hello
```

```json
{
    "status":403,
    "error":"Forbidden",
    "message":"Forbidden",
    "path":"/hello"
}
```

Similarly, you can use any other expression we discussed in chapter 7 for endpoint authentication. Here’s a short recap of them: 
* ``hasAnyAuthority()`` — Specifies multiple authorities. The user must have at least one of these authorities to call the method. 
* ``hasRole()`` — Specifies a role a user must have to call the method. 
* ``hasAnyRole()`` — Specifies multiple roles. The user must have at least one of them to call the method.

Let’s extend our example to prove how you can use the values of the method parameters to define the authorization rules (figure 16.6).

![chapter-16-figure-16-6.PNG](pictures/chapter-16-figure-16-6.PNG)

The endpoint now takes a value through a path variable and calls a service class to obtain the “secret names” for a given username.

The endpoint now takes a value through a path variable and calls a service class to obtain the “secret names” for a given username.

```java
@RestController
public class HelloController {

    private final HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping("/secret/names/{name}")
    public String hello(@PathVariable String name) {
        return helloService.getName(name);
    }
}
```

```java
@Service
public class HelloService {

    private Map<String, List<String>> secretNames = Map.of(
            "natalie", List.of("Energico", "Perfecto"),
            "emma", List.of("Fantastico"));

    @PreAuthorize("#name == authentication.principal.username")
    public List<String> getName(String name) {
        return secretNames.get(name);
    }
}
```

The expression we use for authorization now is #name == authentication.principal. username. In this expression, we use #name to refer to the value of the getSecretNames() method parameter called name, and we have access directly to the authentication object that we can use to refer to the currently authenticated user. The expression we use indicates that the method can be called only if the authenticated user’s username is the same as the value sent through the method’s parameter. In other words, a user can only retrieve its own secret names.

```shell
curl -u emma:12345 http://localhost:8080/secret/names/emma
["Fantastico"]
```

```shell
curl -u emma:12345 http://localhost:8080/secret/names/natalie
```

```json
{
    "status":403,
    "error":"Forbidden",
    "message":"Forbidden",
    "path":"/secret/names/natalie"
}
```

```shell
curl -u natalie:12345 http://localhost:8080/secret/names/natalie
["Energico","Perfecto"]
```

## Applying postauthorization

Now say you want to allow a call to a method, but in certain circumstances, you want to make sure the caller doesn’t receive the returned value. When we want to apply an authorization rule that is verified after the call of a method, we use postauthorization. It may sound a little bit awkward at the beginning: why would someone be able to execute the code but not get the result? Well, it’s not about the method itself, but imagine this method retrieves some data from a data source, say a web service or a database. You can be confident about what your method does, but you can’t bet on the third party your method calls. So you allow the method to execute, but you validate what it returns and, if it doesn’t meet the criteria, you don’t let the caller access the return value.

To apply postauthorization rules with Spring Security, we use the @PostAuthorize annotation, which is similar to @PreAuthorize.

The scenario for our example, defines an object Employee. Our Employee has a name, a list of books, and a list of authorities. We associate each Employee to a user of the application. To stay consistent with the other examples in this chapter, we define the same users, Emma and Natalie. We want to make sure that the caller of the method gets the details of the employee only if the employee has read authority. Because we don’t know the authorities associated with the employee record until we retrieve the record, we need to apply the authorization rules after the method execution. For this reason, we use the @PostAuthorize annotation.

```java
//Getters are required, because Spring cannot handle records normally
public record Employee(String name, List<String> books, List<String> roles) {

    public String getName() {
        return this.name;
    }

    public List<String> getBooks() {
        return this.books;
    }

    public List<String> getRoles() {
        return this.roles;
    }
}
```

```java
@Service
public class BookService {

    private Map<String, Employee> records = Map.of(
            "emma", new Employee("Emma Thompson", List.of("Karamazov Brothers"), List.of("accountant", "reader")),
            "natalie", new Employee("Natalie Parker", List.of("Beautiful Paris"), List.of("researcher")));

    @PostAuthorize("returnObject.roles.contains('reader')")
    public Employee getBookDetails(String name) {
        return records.get(name);
    }
}
```

```java
@RestController
public class HelloController {

    private final BookService bookService;

    public HelloController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/book/details/{name}")
    public Employee getDetails(@PathVariable String name) {
        return bookService.getBookDetails(name);
    }
}
```

```shell
curl -u emma:12345 http://localhost:8080/book/details/emma
```

```json
{
    "name":"Emma Thompson",
    "books":["Karamazov Brothers"],
    "roles":["accountant","reader"]
}
```

```shell
curl -u natalie:12345 http://localhost:8080/book/details/emma
```

```json
{
    "name":"Emma Thompson",
    "books":["Karamazov Brothers"],
    "roles":["accountant","reader"]
}
```

```shell
curl -u emma:12345 http://localhost:8080/book/details/natalie
```

```json
{
    "status":403,
    "error":"Forbidden",
    "message":"Forbidden",
    "path":"/book/details/natalie"
}
```

```shell
curl -u natalie:12345 http://localhost:8080/book/details/natalie
```

```json
{
    "status":403,
    "error":"Forbidden",
    "message":"Forbidden",
    "path":"/book/details/natalie"
}
```

## Implementing permissions for methods

Up to now, you learned how to define rules with simple expressions for preauthorization and postauthorization. Now, let’s assume the authorization logic is more complex, and you cannot write it in one line. It’s definitely not comfortable to write huge SpEL expressions. I never recommend using long SpEL expressions in any situation, regardless if it’s an authorization rule or not. It simply creates hard-to-read code, and this affects the app’s maintainability. When you need to implement complex authorization rules, instead of writing long SpEL expressions, take the logic out in a separate class. Spring Security provides the concept of **permission**, which makes it easy to write the authorization rules in a separate class so that your application is easier to read and understand.

In this scenario, you have an application managing documents. Any document has an owner, which is the user who created the document. To get the details of an existing document, a user either has to be an admin or they have to be the owner of the document.

```java
public record Document(String owner) {}
```

To mock the database and make our example shorter for your comfort, I created a repository class that manages a few document instances in a Map.

```java
@Repository
public class DocumentRepository {

    private final Map<String, Document> documents = Map.of(
            "abc123", new Document("natalie"),
            "qwe123", new Document("natalie"),
            "asd555", new Document("emma"));

    public Optional<Document> findDocument(String code) {
        return Optional.ofNullable(documents.get(code));
    }
}
```

A service class defines a method that uses the repository to obtain a document by its code. The method in the service class is the one for which we apply the authorization rules. The logic of the class is simple. It defines a method that returns the Document by its unique code. We annotate this method with @PostAuthorize and use a hasPermission() SpEL expression. This method allows us to refer to an external authorization expression that we implement further in this example. Meanwhile, observe that the parameters we provide to the hasPermission() method are the returnObject, which represents the value returned by the method, and the name of the role for which we allow access, which is 'ROLE_admin'.

```java
@Service
public class DocumentService {

    private final DocumentRepository repository;

    public DocumentService(DocumentRepository repository) {
        this.repository = repository;
    }

    @PostAuthorize("hasPermission(returnObject, 'ROLE_admin')")
    public Document getDocument(String code) {
        return repository.findDocument(code)
                .orElseThrow(() -> new RuntimeException("Not found"));
    }
}
```

It’s our duty to implement the permission logic. And we do this by writing an object that implements the PermissionEvaluator contract. The PermissionEvaluator contract provides two ways to implement the permission logic: 
* By object and permission — Used in the current example, it assumes the permission evaluator receives two objects: one that’s subject to the authorization rule and one that offers extra details needed for implementing the permission logic. 
* By object ID, object type, and permission — Assumes the permission evaluator receives an object ID, which it can use to retrieve the needed object. It also receives a type of object, which can be used if the same permission evaluator applies to multiple object types, and it needs an object offering extra details for evaluating the permission.

```java
public interface PermissionEvaluator {

    boolean hasPermission(Authentication a, Object subject, Object permission);

    boolean hasPermission(Authentication a, Serializable id, String type, Object permission);
}
```

For your awareness and to avoid confusion, I’d also like to mention that you don’t have to pass the Authentication object. Spring Security automatically provides this parameter value when calling the hasPermission() method. The framework knows the value of the authentication instance because it is already in the SecurityContext.

```java
@Component
public class DocumentPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object o, Object o1) {
        var authorities = authentication.getAuthorities();
        if (o instanceof Document document && o1 instanceof String permission) {
            var isAdmin = authorities.stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(permission));
            var isOwner = document.owner().equals(authentication.getName());
            return isAdmin || isOwner;
        }

        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return false; //We don't need to implement the second method because we don't use it.
    }
}
```

To make Spring Security aware of our new PermissionEvaluator implementation, we have to define a MethodSecurityExpressionHandler in the configuration class. The following listing presents how to define a MethodSecurityExpressionHandler to make the custom PermissionEvaluator known.

```java
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ProjectConfig extends GlobalMethodSecurityConfiguration {

    private final DocumentPermissionEvaluator evaluator;

    public ProjectConfig(DocumentPermissionEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        var expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(evaluator);

        return expressionHandler;
    }

    @Bean
    public UserDetailsManager userDetailsManager() {
        var natalie = User.withUsername("natalie")
                .password("12345")
                .roles("admin")
                .build();
        var emma = User.withUsername("emma")
                .password("12345")
                .roles("manager")
                .build();

        return new InMemoryUserDetailsManager(natalie, emma);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
```

**NOTE**. We use here an implementation for MethodSecurityExpressionHandler named DefaultMethodSecurityExpressionHandler that Spring Security provides. You could as well implement a custom MethodSecurityExpressionHandler to define custom SpEL expressions you use to apply the authorization rules. You rarely need to do this in a real-world scenario

```java
@RestController
public class DocumentController {
    
    private final DocumentService service;

    public DocumentController(DocumentService service) {
        this.service = service;
    }

    @GetMapping("documents/{code}")
    public Document getDetails(@PathVariable String code) {
        return service.getDocument(code);
    }
}
```

Let’s run the application and call the endpoint to observe its behavior. User Natalie can access the documents regardless of their owner. User Emma can only access the documents she owns.

```shell
curl -u natalie:12345 http://localhost:8080/documents/abc123
```

```json
{
  "owner":"natalie"
}
```

```shell
curl -u natalie:12345 http://localhost:8080/documents/asd555
```

```json
{
  "owner":"emma"
}
```

```shell
curl -u emma:12345 http://localhost:8080/documents/asd555
```

```json
{
  "owner":"emma"
}
```

```shell
curl -u emma:12345 http://localhost:8080/documents/abc123
```

```json
{
    "status":403,
    "error":"Forbidden",
    "message":"Forbidden",
    "path":"/documents/abc123"
}
```

In a similar manner, you can use the second PermissionEvaluator method to write your authorization expression. The second method refers to using an identifier and subject type instead of the object itself. For example, say that we want to change the current example to apply the authorization rules before the method is executed, using @PreAuthorize. In this case, we don’t have the returned object yet. But instead of having the object itself, we have the document’s code, which is its unique identifier.

```java
@Component
public class DocumentPermissionEvaluator implements PermissionEvaluator {

    private final DocumentRepository documentRepository;

    public DocumentPermissionEvaluator(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object target, Object permission) {
        return false; //No longer defines the authorization rules through the first method.
    }
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if (targetId instanceof String code && permission instanceof String p) {
            var document = documentRepository.findDocument(code).orElseThrow();

            var isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(p));
            var isDocumentOwner = document.getOwner().equals(authentication.getName());
            
            return isAdmin || isDocumentOwner;
        }
        
        return false;
    }
}
```

Of course, we also need to use the proper call to the permission evaluator with the @PreAuthorize annotation.

```java
@Service
public class DocumentService {

    private final DocumentRepository repository;

    public DocumentService(DocumentRepository repository) {
        this.repository = repository;
    }

    @PreAuthorize("hasPermission(#code, 'document', 'ROLE_admin')")
    public Document getDocument(String code) {
        return repository.findDocument(code)
                .orElseThrow(() -> new RuntimeException("Not found"));
    }
}
```

You can rerun the application and check the behavior of the endpoint. **You should see the same result** as in the case where we used the first method of the permission evaluator to implement the authorization rules.

## Using the @Secured and @RolesAllowed annotations

The @EnableGlobalMethodSecurity annotation offers two other similar attributes that you can use to enable different annotations. You use the ``jsr250Enabled`` attribute to enable the @RolesAllowed annotation and the ``securedEnabled`` attribute to enable the @Secured annotation. Using these two annotations, @Secured and @RolesAllowed, is less powerful than using @PreAuthorize and @PostAuthorize,

```java
@EnableGlobalMethodSecurity(jsr250Enabled = true, securedEnabled = true)
```

Once you’ve enabled these attributes, you can use the @RolesAllowed or @Secured annotations to specify which roles or authorities the logged-in user needs to have to call a certain method.

```java
@Service
public class NameService {
    @RolesAllowed("ROLE_ADMIN")
    public String getName() {
        return "Fantastico";
    }
}
```

Similarily, you can use the @Secured annotation instead of the @RolesAllowed annotation.

```java
@Service
public class NameService {
    @Secured("ROLE_ADMIN")
    public String getName() {
        return "Fantastico";
    }
}
```

@Secured and @RolesAllowed are the same the only difference is @RolesAllowed is a standard annotation (i.e. not only spring security) whereas @Secured is spring security only.

# Chapter 17. Global method security: Pre- and postfiltering

In chapter 16, you learned how to apply authorization rules using global method security. We worked on examples using the @PreAuthorize and @PostAuthorize annotations. By using these annotations, you apply an approach in which the application either allows the method call or it completely rejects the call. Suppose you don’t want to forbid the call to a method, but you want to make sure that the parameters sent to it follow some rules. Or, in another scenario, you want to make sure that after someone calls the method, the method’s caller only receives an authorized part of the returned value. We name such a functionality filtering, and we classify it in two categories: 
* **Prefiltering** — The framework filters the values of the parameters before calling the method. 
* **Postfiltering** — The framework filters the returned value after the method call.

Filtering works differently than call authorization. With filtering, the framework executes the call and **doesn’t throw an exception if a parameter or returned value doesn’t follow an authorization rule you define**. Instead, it **filters out elements that don’t follow the conditions you specify.**

It’s important to mention from the beginning that you can only apply filtering to collections and arrays.

## Applying prefiltering for method authorization

```java
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ProjectConfig extends GlobalMethodSecurityConfiguration {
    @Bean
    public UserDetailsManager userDetailsManager() {
        var natalie = User.withUsername("john")
                .password("12345")
                .roles("read")
                .build();
        var emma = User.withUsername("don")
                .password("12345")
                .roles("write")
                .build();

        return new InMemoryUserDetailsManager(natalie, emma);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
```

```java
public record Product(String name, String owner) {}
```

The ProductService class defines the service method we protect with @PreFilter. In that listing, before the ``sellProducts()`` method, you can observe the use of the @PreFilter annotation. The Spring Expression Language (SpEL) used with the annotation is ``filterObject.owner == authentication.name``, which allows only values where the ``owner`` attribute of the Product equals the username of the logged-in user. On the left side of the equals operator in the SpEL expression; we use ``filterObject``. With ``filterObject``, we refer to objects in the list as parameters. Because we have a list of products, the ``filterObject`` in our case is of type Product. For this reason, we can refer to the product’s owner attribute. On the right side of the equals operator in the expression; we use the authentication object. For the @PreFilter and @PostFilter annotations, we can directly refer to the authentication object, which is available in the SecurityContext after authentication

The service method returns the list exactly as the method receives it. This way, we can test and validate that the framework filtered the list as we expected by checking the list returned in the HTTP response body.

```java
@Service
public class ProductService {

    @PreFilter("filterObject.owner == authentication.name")
    public List<Product> sellProducts(List<Product> products) {
        // sell products and return the sold products list
        return products;
    }
}
```

```java
@RestController
public class ProductController {
    
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/sell")
    public List<Product> sellProduct() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("beer", "john"));
        products.add(new Product("candy", "john"));
        products.add(new Product("chocolate", "don"));
        return service.sellProducts(products);
    }
}
```

```shell
curl -u john:12345 http://localhost:8080/sell
[{"name":"beer","owner":"john"},{"name":"candy","owner":"john"}]
```

```shell
curl -u don:12345 http://localhost:8080/sell
[{"name":"chocolate","owner":"don"}]
```

What you need to be careful about is the fact that the aspect changes the given collection. In our case, don’t expect it to return a new List instance. In fact, it’s the same instance from which the aspect removed the elements that didn’t match the given criteria. This is important to take into consideration. **You must always make sure that the collection instance you provide is not immutable.**

Providing an immutable collection to be processed results in an exception at execution time because the filtering aspect won’t be able to change the collection’s contents

```java
@RestController
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/sell")
    public List<Product> sellProduct() {
        List<Product> products = List.of(
                new Product("beer", "john"),
                new Product("candy", "john"),
                new Product("chocolate", "don"));
        return service.sellProducts(products);
    }
}
```

```shell
curl -u don:12345 http://localhost:8080/sell
```

```json
{
    "status":500,
    "error":"Internal Server Error",
    "message":"No message available",
    "path":"/sell"
    }
```

## Applying postfiltering for method authorization

Say we have the following scenario. An application that has a frontend implemented in Angular and a Spring-based backend manages some products. Users own products, and they can obtain details only for their products. To get the details of their products, the frontend calls endpoints exposed by the backend

https://confluence.danskenet.net/display/FINANDEV/QA+Practice

Similar to prefiltering, postfiltering also relies on an aspect. This aspect allows a call to a method, but once the method returns, the aspect takes the returned value and makes sure that it follows the rules you define. **The post-filter aspect filters from the returned collection or array those elements that don’t follow your rules.**

Make sure you apply the @PostFilter annotation only for methods that have as a return type an array or a collection.

```java
@Service
public class ProductService {
    
    @PostFilter("filterObject.owner == authentication.name")
    public List<Product> findProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("beer", "john"));
        products.add(new Product("candy", "john"));
        products.add(new Product("chocolate", "don"));
        return products;
    }
}
```

```java
@RestController
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/find")
    public List<Product> sellProduct() {
        return service.findProducts();
    }
}
```

```shell
curl -u john:12345 http://localhost:8080/find
[{"name":"beer","owner":"john"},{"name":"candy","owner":"john"}]
```

```shell
curl -u don:12345 http://localhost:8080/find
[{"name":"chocolate","owner":"don"}]
```

## Using filtering in Spring Data repositories

Using the @PreFilter annotation in the case of repositories is the same as applying this annotation at any other layer of your application. But when it comes to postfiltering, the situation changes. **Using @PostFilter on repository methods technically works fine, but it’s rarely a good choice from a performance point of view.**

Let’s work on an application where we first use the @PostFilter annotation on the Spring Data repository method, and then we change to the second approach where we write the condition directly in the query. This way, we have the opportunity to experiment with both approaches and compare them.

```java
public interface ProductRepository extends JpaRepository<Product, Integer> { 
    
    @PostFilter("filterObject.owner == authentication.name") 
    List<Product> findProductByNameContains(String text); 
}
```

We discussed earlier in this section that using @PostFilter in the repository isn’t the best choice. We should instead make sure we don’t select from the database what we don’t need.

We can provide SpEL expressions directly in the queries used by the repository classes. To achieve this, we follow two simple steps: 
* We add an object of type SecurityEvaluationContextExtension to the Spring context. We can do this using a simple @Bean method in the configuration class. 
* We adjust the queries in our repository classes with the proper clauses for selection.

```java
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ProjectConfig {
    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }
    // Omitted declaration of the UserDetailsService and PasswordEncoder
}
```

```java
public interface ProductRepository extends JpaRepository<Product, Integer> {
    
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:text% AND p.owner=?#{authentication.name}")
    List<Product> findProductByNameContains(String text);
}
```

# Chapter 18. Hands-on: An OAuth 2 application

## The application scenario

Say we need to build a backend for a fitness application. Besides other great features, the app also stores a history of users’ workouts. In this chapter, we’ll focus on the part of the application that stores the history of workouts. We presume our backend needs to implement three use cases. For each action defined by the use cases, we have specific security restrictions. The three use cases are these:
* **Add a new workout record for a user**. In a database table named workout, we add a new record that stores user, the start and the end times of the workout, and the difficulty of the workout, using an integer on a scale from 1 to 5. The authorization restriction for this use case asserts that authenticated users can only add workout records for themselves. The client calls an endpoint exposed by the resource server to add a new workout record. 
* **Find all the workouts for a user**. The client needs to display a list of workouts in the user’s history. The client calls an endpoint to retrieve that list. The authorization restriction in this case states that a user can only get their own workout records. 
* **Delete a workout**. Any user having the admin role can delete a workout for any other user. The client calls an endpoint to delete a workout record. The authorization restriction says that only an admin can delete records.

We need to implement three use cases for which we have two acting roles. The two roles are:
* Standard user - **fitnessuser**.
* Admin - **fitnessadmin**.

A fitnessuser can add a workout for themselves and can see their own workout history. A fitnessadmin can only delete workout records for any user. Of course, an admin can also be a user, and in this case, they can also add workouts for themselves or see their own recorded workouts.

![chapter-18-figure-18-2.PNG](pictures/chapter-18-figure-18-2.PNG)

## Configuring Keycloak as an authorization server

Keycloak is an excellent open source tool designed for identity and access management. You can download Keycloak from keycloak.org. Keycloak offers the ability to manage simple users locally and also provides advanced features such as user federation. You could connect it to your LDAP and Active Directory services or to different identity providers. For example, you could use Keycloak as a high-level authentication layer by connecting it to one of the common OAuth 2 providers we discussed in chapter 12.

In chapter 9 of *Enterprise Java Microservices by Ken Finnigan (Manning, 2018)*, you can also find a good discussion on securing microservices where the author uses Keycloak for user management.

To install Keycloak, you only need to download an archive containing the latest version from the official website https://www.keycloak.org/downloads. Then, unzip the archive in a folder, and you can start Keycloak using the standalone executable file, which you find in the bin folder. If you’re using Linux, you need to run standalone. sh. For Windows, you run standalone.bat.

Once you start the Keycloak server, access it in a browser at http://localhost:8080. In Keycloak’s first page, you configure an admin account by entering a username and a password (figure 18.4).

![chapter-18-figure-18-4.PNG](pictures/chapter-18-figure-18-4.PNG)

In the Administration Console, you can start configuring the authorization server. We need to know which OAuth 2–related endpoints Keycloak exposes. You find those endpoints in the General section of the Realm Settings page, which is the first page you land on after logging in to the Administration Console (figure 18.6).

![chapter-18-figure-18-6.PNG](pictures/chapter-18-figure-18-6.PNG)

In the next code snippet, I extracted a part of the OAuth 2 configuration that you find by clicking the OpenID Endpoint Configuration link. This configuration provides the token endpoint, the authorization endpoint, and the list of supported grant types. These details should be familiar to you, as we discussed them in chapters 12 through 15.

```json
{
"issuer": "http://localhost:8080/auth/realms/master",
"authorization_endpoint": "http://localhost:8080/auth/realms/master/protocol/openid-connect/auth",
"token_endpoint": "http://localhost:8080/auth/realms/master/protocol/openid-connect/token",
"jwks_uri": "http://localhost:8080/auth/realms/master/protocol/openid-connect/certs",
"grant_types_supported":[
    "authorization_code",
    "implicit",
    "refresh_token",
    "password",
    "client_credentials"
],
...
}
```

Now that we’ve installed Keycloak, set up the admin credentials, and made a few adjustments, we can configure the authorization server. Here’s a list of the configuration steps. 
* Register a client for the system. An OAuth 2 system needs at least one client recognized by the authorization server. The client makes authentication requests for users. In section 18.2.1, you’ll learn how to add a new client registration. 
* Define a client scope. The client scope identifies the purpose of the client in the system. We use the client scope definition to customize the access tokens issued by the authorization server. In section 18.2.2, you’ll learn how to add a client scope, and in section 18.2.4, we’ll configure it to customize the access token. 
* Add users for our application. To call the endpoints on the resource server, we need users for our application. You’ll learn how to add users managed by Keycloak in section 18.2.3. 
* Define user roles and custom access tokens. After adding users, you can issue access tokens for them. You’ll notice that the access tokens don’t have all the details we need to accomplish our scenario. You’ll learn how to configure roles for the users and customize the access tokens to present the details expected by the resource server we’ll implement using Spring Security in section 18.2.4.

### Registering a client for our system

In this section, we discuss registering a client when using Keycloak as an authorization server. Like in any other OAuth 2 system, we need to register the client applications at the authorization server level. To add a new client, we use Keycloak Administration Console. As presented in figure 18.9, you find a list of clients by navigating to the Clients tab on the left-hand menu. From here, you can also add a new client registration.

![chapter-18-figure-18-9.PNG](pictures/chapter-18-figure-18-9.PNG)

![chapter-18-figure-18-10.PNG](pictures/chapter-18-figure-18-10.PNG)

### Specifying client scopes

In this section, we define a scope for the client we registered in section 18.2.1. The client scope identifies the purpose of the client.

![chapter-18-figure-18-11.PNG](pictures/chapter-18-figure-18-11.PNG)

For the app that we build in this hands-on example, I added a new client scope named fitnessapp. When adding a new scope, also make sure that the protocol for which you set the client scope is **openid-connect**.

Once you create the new role, you assign it to your client as figure 18.13 presents. You get to this screen by navigating to the Clients menu and then selecting the Client Scopes tab.

![chapter-18-figure-18-12.PNG](pictures/chapter-18-figure-18-12.PNG)

![chapter-18-figure-18-13.PNG](pictures/chapter-18-figure-18-13.PNG)

### Adding users and obtaining access tokens

In this section, we create and configure users for our application. Previously, we configured the client and its scope in sections 18.2.1 and 18.2.2. But besides the client app, we need users to authenticate and access the services offered by our resource server. We configure three users that we use to test our application (figure 18.14). I named the users Mary, Bill, and Rachel.

![chapter-18-figure-18-14.PNG](pictures/chapter-18-figure-18-14.PNG)

When adding a new user in the Add User form, give it a unique username and check the box stating the email was verified (figure 18.15). Also, make sure the user has no Required User Actions. When a user has Required User Actions pending, you cannot use it for authentication; thus, you cannot obtain an access token for that user.

Once you create the users, you should find all of them in the Users list. Figure 18.16 presents the Users list.

![chapter-18-figure-18-15.PNG](pictures/chapter-18-figure-18-15.PNG)

![chapter-18-figure-18-16.PNG](pictures/chapter-18-figure-18-16.PNG)

![chapter-18-figure-18-17.PNG](pictures/chapter-18-figure-18-17.PNG)

Of course, users also need passwords to log in. Usually, they’d configure their own passwords, and the administrator shouldn’t know their credentials. In our case, we have no choice but to configure passwords ourselves for the three users (figure 18.17). To keep our example simple, I configured the password “12345” for all users. I also made sure that the password isn’t temporary by unchecking the Temporary check box. If you make the password temporary, Keycloak automatically adds a required action for the user to change the password at their first login. Because of this required action, we wouldn’t be able to authenticate with the user.

Having the users configured, you can now obtain an access token from your authorization server implemented with Keycloak.

```shell
curl -XPOST "http://localhost:8080/auth/realms/master/protocol/openid-connect/token" \
-H "Content-Type: application/x-www-form-urlencoded" \
--data-urlencode "grant_type=password" \
--data-urlencode "username=rachel" \
--data-urlencode "password=12345" \
--data-urlencode "scope=fitnessapp" \
--data-urlencode "client_id=fitnessapp"
```

![chapter-18-figure-18-18.PNG](pictures/chapter-18-figure-18-18.PNG)

You receive the access token in the body of the HTTP response.

```json
{
    "access_token":"eyJhbGciOiJIUzI…",
    "expires_in":6000,
    "refresh_expires_in":1800,
    "refresh_token":"eyJhbGciOiJIUz… ",
    "token_type":"bearer",
    "not-before-policy":0,
    "session_state":"1f4ddae7-7fe0-407e-8314-a8e7fcd34d1b",
    "scope":"fitnessapp"
}
```

The next code snippet presents the decoded JSON body of the JWT access token. Taking a glance at the code snippet, you can observe that the token doesn’t contain all the details we need to make our application work. **The roles and username are missing**.

```json
{
    "exp": 1585392296,
    "iat": 1585386296,
    "jti": "01117f5c-360c-40fa-936b-763d446c7873",
    "iss": "http://localhost:8080/auth/realms/master",
    "sub": "c42b534f-7f08-4505-8958-59ea65fb3b47",
    "typ": "Bearer",
    "azp": "fitnessapp",
    "session_state": "fce70fc0-e93c-42aa-8ebc-1aac9a0dba31",
    "acr": "1",
    "scope": "fitnessapp"
}
```

### Defining the user roles

Adding roles to a user is simple. The Roles tab in the left-hand menu allows you to find a list of all roles and add new roles, as presented in figure 18.19. I created two new roles, fitnessuser and fitnessadmin.

![chapter-18-figure-18-19.PNG](pictures/chapter-18-figure-18-19.PNG)

We now assign these roles to our users. I assigned the role fitnessadmin to Mary, our administrator, while Bill and Rachel, who are regular users, take the role fitnessuser.

![chapter-18-figure-18-20.PNG](pictures/chapter-18-figure-18-20.PNG)

Unfortunately, by default, these new details won’t appear in the access token. We have to customize the token according to the requirements of the application. We customize the token by configuring the client scope we created and assigned to the token in section 18.2.2. We need to add three more details to our tokens: 
* Roles — Used to apply a part of the authorization rules at the endpoint layer according to the scenario 
* Username — Filters the data when we apply the authorization rules 
* Audience claim (aud) — Used by the resource server to acknowledge the requests, as you’ll learn in section 18.3.

The next code snippet presents the fields that are added to the token once we finish setup.

```json
{
// ...
  "authorities": ["fitnessuser"],
  "aud": "fitnessapp",
  "user_name": "rachel",
// ...
}
```

![chapter-18-figure-18-21.PNG](pictures/chapter-18-figure-18-21.PNG)

Figure 18.22 shows how to create a mapper to add the roles to the token. We add the roles with the *authorities* key in the token because this is the way the resource server expects it.

![chapter-18-figure-18-22.PNG](pictures/chapter-18-figure-18-22.PNG)

With an approach similar to the one presented in figure 18.22, we can also define a mapper to add the username to the token.

![chapter-18-figure-18-23.PNG](pictures/chapter-18-figure-18-23.PNG)

Finally, we need to specify the audience. The audience claim (aud) defines the intended recipient of the access token.

![chapter-18-figure-18-24.PNG](pictures/chapter-18-figure-18-24.PNG)

If you obtain an access token again and decode it, you should find the authorities, user_name, and aud claims in the token’s body. Now we can use this JWT to authenticate and call endpoints exposed by the resource server.

![chapter-18-decoded-jwt.PNG](pictures/chapter-18-decoded-jwt.PNG)

## Implementing the resource server

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.5.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>spring-security</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>spring-security</name>
    <description>Demo project for Spring Boot</description>
    <properties>
        <java.version>11</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-oauth2</artifactId>
            <version>2.2.5.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-data</artifactId>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>11</source>
                    <target>11</target>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

schema.sql:
```sql
CREATE SCHEMA spring;

CREATE TABLE spring.workout (
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    user        VARCHAR(45) NULL,
    start       DATETIME2   NULL,
    end         DATETIME2   NULL,
    difficulty  INT         NULL,
    PRIMARY KEY (id)
);
```

data.sql:
```sql
INSERT INTO spring.workout (id, user, start, end, difficulty) VALUES
(1, 'bill',   '2020-06-10 15:05:05', '2020-06-10 16:10:07', '3'),
(2, 'rachel', '2020-06-10 15:05:10', '2020-06-10 16:10:20', '3'),
(3, 'bill',   '2020-06-12 12:00:10', '2020-06-12 13:01:10', '4'),
(4, 'rachel', '2020-06-12 12:00:05', '2020-06-12 12:00:11', '4');
```

application.yml
```yaml
spring:
  main:
    banner-mode: off
  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:resource-server;DB_CLOSE_ON_EXIT=FALSE
    username: sa
    password:
    platform: h2
  jpa:
    database: h2
    show-sql: true #Use ``true`` only when you need to debug. Otherwise performance will degraded due to stoud'ing all SQL opperations
    generate-ddl: false #Never let Hibernate/JPA generate your database schemas. Otherwise, you might find surprises if you've annotated incorrectly. See unidirectional @OneToMany.
    hibernate:
      ddl-auto: none #Do not autogenerate SQL tables. Sometimes it might lead to suboptimal structures/types. Control you database!
    properties:
      hibernate:
        #Turn it on only when you need for analysing queries. Do not leave it on!
        generate_statistics: false
        format_sql: true
        dialect: org.hibernate.dialect.H2Dialect
    open-in-view: false #Do not allow Spring to automagically fetch data from database. See https://vladmihalcea.com/the-open-session-in-view-anti-pattern/
  h2:
    console:
      enabled: true
      path: /h2
  application:
    name: spring-security

server:
  port: 9090
```

```java

@Entity
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String user;
    private LocalDateTime start;
    private LocalDateTime end;
    private int difficulty;
    // Omitted getter and setters
}
```

```java
public interface WorkoutRepo extends JpaRepository<WorkoutEntity, Long> {

    @Query("SELECT w FROM Workout w WHERE w.user = ?#{authentication.name}")
    List<WorkoutEntity> findAllByUser();
}
```

The controller directly calls the methods of this class. According to our scenario, we need to implement three methods:

* ``saveWorkout()`` — Adds a new workout record in the database
* ``findWorkouts()`` — Retrieves the workout records for a user
* ``deleteWorkout()`` — Deletes a workout record for a given ID

```java

@Service
public class WorkoutService {

    private final WorkoutRepo repo;

    public WorkoutService(WorkoutRepo repo) {
        this.repo = repo;
    }

    @PreAuthorize("#workout.user == authentication.name")
    public void saveWorkout(WorkoutEntity workout) {
        this.repo.save(workout);
    }

    public List<WorkoutEntity> findWorkouts() {
        return this.repo.findAllByUser();
    }

    public void deleteWorkout(Long id) {
        this.repo.deleteById(id);
    }
}
```

```java

@RestController
@RequestMapping("workout")
public class WorkoutController {

    private final WorkoutService service;

    public WorkoutController(WorkoutService service) {
        this.service = service;
    }

    @PostMapping("/")
    public void add(@RequestBody WorkoutEntity workout) {
        this.service.saveWorkout(workout);
    }

    @GetMapping("/")
    public List<WorkoutEntity> findAll() {
        return this.service.findWorkouts();
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        this.service.deleteWorkout(id);
    }
}
```

The last thing we need to define to have a complete application is the configuration class. We need to choose the way
the resource server validates tokens issued by the authorization server. We discussed three approaches in chapters 14
and 15:

* With a direct call to the authorization server
* Using a blackboarding approach
* With cryptographic signatures

Because we already know the authorization server issues JWTs, the most comfortable choice is to rely on the
cryptographic signature of the token. As you know from chapter 15, we need to provide the resource server the key to
validate the signature. Fortunately, Keycloak offers an endpoint where public keys are exposed:

```shell
http://localhost:8080/auth/realms/master/protocol/openid-connect/certs
```

We add this URI, together with the value of the aud claim we set on the token in the application.properties file:

```properties
claim.aud=fitnessapp
jwkSetUri=http://localhost:8080/auth/realms/master/protocol/openid-connect/certs
```

Now we can write the configuration file.

```java

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final String claimAud;
    private final String urlJwk;

    public ResourceServerConfig(@Value("${claim.aud}") String claimAud, @Value("${jwkSetUri}") String urlJwk) {
        this.claimAud = claimAud;
        this.urlJwk = urlJwk;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.tokenStore(tokenStore());
        resources.resourceId(this.claimAud);
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwkTokenStore(this.urlJwk);
    }
}
```

To create an instance of TokenStore, we use an implementation called JwkTokenStore. This implementation uses an endpoint
where we can expose multiple keys. To validate a token, JwkTokenStore looks for a specific key whose ID needs to exist
in the header of the provided JWT token.

If you call the keys URI, you see something similar to the next code snippet. In the HTTP response body, you have
multiple keys. We call this collection of keys the key set. Each key has multiple attributes, including the value of the
key and a unique ID for each key. The attribute kid represents the key ID in the JSON response.

```json
{
  "keys":[
    {
      "kid":"LHOsOEQJbnNbUn8PmZXA9TUoP56hYOtc3VOk0kUvj5U",
      "kty":"RSA",
      "alg":"RS256",
      "use":"sig",
      …
    }
    …
  ]
}
```

The JWT needs to specify which key ID is used to sign the token. The resource server needs to find the key ID in the JWT
header. If you generate a token with our resource server as we did in section 18.2 and decode the header of the token,
you can see the token contains the key ID as expected. In the next code snippet, you find the decoded header of a token
generated with our Keycloak authorization server:

```json
{
  "alg": "RS256",
  "typ": "JWT",
  "kid": "LHOsOEQJbnNbUn8PmZXA9TUoP56hYOtc3VOk0kUvj5U"
}
```

To complete our configuration class, let’s add the authorization rules for the endpoint level and the SecurityEvaluationContextExtension. Our application needs this extension to evaluate the SpEL expression we used at the repository layer.

```java
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final String claimAud;
    private final String urlJwk;

    public ResourceServerConfig(@Value("${claim.aud}") String claimAud, @Value("${jwkSetUri}") String urlJwk) {
        this.claimAud = claimAud;
        this.urlJwk = urlJwk;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.tokenStore(tokenStore());
        resources.resourceId(this.claimAud);
    }
    @Bean
    public TokenStore tokenStore() {
        return new JwkTokenStore(this.urlJwk);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers(HttpMethod.DELETE, "/**").hasAuthority("fitnessadmin")
                .anyRequest().authenticated();
    }
    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }
}
```

## Testing the application

The scenarios we need to test are the following: 
* A client can add a workout only for the authenticated user 
* A client can only retrieve their own workout records 
* Only admin users can delete a workout 
  
In my case, the Keycloak authorization server runs on port 8080, and the resource server I configured in the application.properties file runs on port 9090.

### Proving an authenticated user can only add a record for themself

According to the scenario, a user can only add a record for themself. In other words, if I authenticate as Bill, I shouldn’t be able to add a workout record for Rachel. To prove this is the app’s behavior, we call the authorization server and issue a token for one of the users, say, Bill. Then we try to add both a workout record for Bill and a workout record for Rachel. We prove that Bill can add a record for himself, but the app doesn’t allow him to add a record for Rachel. To issue a token, we call the authorization server as presented in the next code snippet:

```shell
curl -XPOST "http://localhost:8080/auth/realms/master/protocol/openid-connect/token" \
-H 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'grant_type=password' \
--data-urlencode 'username=bill' \
--data-urlencode 'password=12345' \
--data-urlencode 'scope=fitnessapp' \
--data-urlencode 'client_id=fitnessapp'
```

Among other details, you also get an access token for Bill. I truncated the value of the token in the following code snippet to make it shorter. The access token contains all the details needed for authorization, like the username and the authorities we added previously by configuring Keycloak in section 18.1.

```json
{
    "access_token": "eyJhbGciOiJSUzI1NiIsInR…",
    "expires_in": 6000,
    "refresh_expires_in": 1800,
    "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI…",
    "token_type": "bearer",
    "not-before-policy": 0,
    "session_state": "0630a3e4-c4fb-499c-946b-294176de57c5",
    "scope": "fitnessapp"
}
```

Having the access token, we can call the endpoint to add a new workout record. We first try to add a workout record for Bill. We expect that adding a workout record for Bill is valid because the access token we have was generated for Bill.

The next code snippet presents the cURL command you run to add a new workout for Bill. Running this command, you get an HTTP response status of 200 OK, and a new workout record is added to the database. Of course, as the value of the Authorization header, you should add your previously generated access token. I truncated the value of my token in the next code snippet to make the command shorter and easier to read:

```shell
curl -v -XPOST 'localhost:9090/workout/' \
-H 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOi...' \
-H 'Content-Type: application/json' \
--data-raw '{
    "user" : "bill",
    "start" : "2020-06-10T15:05:05",
    "end" : "2020-06-10T16:05:05",
    "difficulty" : 2
}'
```

If you call the endpoint and try to add a record for Rachel, you get back an HTTP response status of 403 Forbidden:

```shell
curl -v -XPOST 'localhost:9090/workout/' \
-H 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOi...' \
-H 'Content-Type: application/json' \
--data-raw '{
    "user" : "rachel",
    "start" : "2020-06-10T15:05:05",
    "end" : "2020-06-10T16:05:05",
    "difficulty" : 2
}'
```

The response body is
```json
{
    "error": "access_denied",
    "error_description": "Access is denied"
}
```

### Proving that a user can only retrieve their own records

To demonstrate this behavior, we generate access tokens for both Bill and Rachel, and we call the endpoint to retrieve their workout history. Neither one of them should see records for the other. To generate an access token for Bill, use this curl command:

```shell
curl -XPOST 'http://localhost:8080/auth/realms/master/protocol/openid-connect/token' \
-H 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'grant_type=password' \
--data-urlencode 'username=bill' \
--data-urlencode 'password=12345' \
--data-urlencode 'scope=fitnessapp' \
--data-urlencode 'client_id=fitnessapp'
```

Calling the endpoint to retrieve the workout history with the access token generated for Bill, the application only returns Bill’s records:

```shell
curl 'localhost:9090/workout/' -H 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSl...'
```

The response body is:

```json
[
    {
        "id": 1,
        "user": "bill",
        "start": "2020-06-10T15:05:05",
        "end": "2020-06-10T16:10:07",
        "difficulty": 3
    },
    ...
]
```

Next, generate a token for Rachel and call the same endpoint. To generate an access token for Rachel, run this curl command:

```shell
curl -XPOST 'http://localhost:8080/auth/realms/master/protocol/openid-connect/token' \
-H 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'grant_type=password' \
--data-urlencode 'username=rachel' \
--data-urlencode 'password=12345' \
--data-urlencode 'scope=fitnessapp' \
--data-urlencode 'client_id=fitnessapp'
```

Using the access token for Rachel to get the workout history, the application only returns records owned by Rachel:

```shell
curl 'localhost:9090/workout/' -H 'Authorization: Bearer eyJhaXciOiJSUzI1NiIsInR5cCIgOiAiSl...'
```

```json
[
    {
        "id": 2,
        "user": "rachel",
        "start": "2020-06-10T15:05:10",
        "end": "2020-06-10T16:10:20",
        "difficulty": 3
    },
    ...
]
```

### Proving that only admins can delete records

The third and last test scenario in which we want to prove the application behaves as desired is that only admin users can delete workout records. To demonstrate this behavior, we generate an access token for our admin user Mary and an access token for one of the other users who are not admins, let’s say, Rachel.

```shell
curl -XPOST 'http://localhost:8080/auth/realms/master/protocol/openid-connect/token' \
-H 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'grant_type=password' \
--data-urlencode 'username=rachel' \
--data-urlencode 'password=12345' \
--data-urlencode 'scope=fitnessapp' \
--data-urlencode 'client_id=fitnessapp'
```

```shell
curl -XDELETE 'localhost:9090/workout/2' --header 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsIn...'
```

If you use Rachel’s token to delete an existing workout, you get back a 403 Forbidden HTTP response status.

```shell
curl -XPOST 'http://localhost:8080/auth/realms/master/protocol/openid-connect/token' \
-H 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'grant_type=password' \
--data-urlencode 'username=mary' \
--data-urlencode 'password=12345' \
--data-urlencode 'scope=fitnessapp' \
--data-urlencode 'client_id=fitnessapp'
```

Calling the endpoint to delete a workout record with the access token for Mary returns an HTTP status 200 OK.

```shell
curl -XDELETE 'localhost:9090/workout/2' --header 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsIn...'
```

# Chapter 19. Spring Security for reactive apps

Not interested.

# Chapter 20. Spring Security testing

## Using mock users for tests

In this section, we discuss using mock users to test authorization configuration. This approach is the most straightforward and frequently used method for testing authorization configurations. When using a mock user, the test completely skips the authentication process (figure 20.3). The mock user is valid only for the test execution, and for this user, you can configure any characteristics you need to validate a specific scenario. You can, for example, give specific roles to the user (ADMIN, MANAGER, and so on) or use different authorities to validate that the app behaves as expected in these conditions.

![chapter-20-figure-20-3.PNG](pictures/chapter-20-figure-20-3.PNG)

To prove how to write such a test, let’s go back to the simplest example. This project exposes an endpoint for the path /hello with only the default Spring Security configuration. What do we expect to happen? 
* When calling the endpoint without a user, the HTTP response status should be 401 Unauthorized. 
* When calling the endpoint having an authenticated user, the HTTP response status should be 200 OK, and the response body should be Hello!.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.5.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>spring-security</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>spring-security</name>
    <description>Demo project for Spring Boot</description>
    <properties>
        <java.version>11</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>11</source>
                    <target>11</target>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

A convenient way to implement a test for the behavior of an endpoint is by using Spring’s MockMvc. In a Spring Boot application, you can autoconfigure the MockMvc utility for testing endpoint calls by adding an annotation over the class.

```java
@SpringBootTest
@AutoConfigureMockMvc
public class MainTests {

    @Autowired
    private MockMvc mvc;
}
```

Now that we have a tool we can use to test endpoint behavior, let’s get started with the first scenario. When calling the /hello endpoint without an authenticated user, the HTTP response status should be 401 Unauthorized.

```java
@SpringBootTest
@AutoConfigureMockMvc
public class MainTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void helloUnauthenticated() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/hello"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
```

To test the second scenario, we need a mock user. To validate the behavior of calling the /hello endpoint with an authenticated user, we use the @WithMockUser annotation. By adding this annotation above the test method, we instruct Spring to set up a SecurityContext that contains a UserDetails implementation instance. It’s basically skipping authentication. Now, calling the endpoint behaves like the user defined with the @WithMockUser annotation successfully authenticated.

With this simple example, we don’t care about the details of the mock user like its username, roles, or authorities. So we add the @WithMockUser annotation, which provides some defaults for the mock user’s attributes. Later in this chapter, you’ll learn to configure the user’s attributes for test scenarios in which their values are important.

```java
    @Test
    @WithMockUser
    void helloAuthenticated() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/hello"))
                .andExpect(MockMvcResultMatchers.content().string("Hello"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
```

But in some situations, we need to use a specific name or give the user specific roles or authorities to implement the test.

```java
    @Test
    @WithMockUser(username = "mary")
    void helloAuthenticated() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/hello"))
                .andExpect(MockMvcResultMatchers.content().string("Hello, mary"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
```

The framework interprets annotations like ``@WithMockUser`` before it executes the test method. This way, the test method creates the test request and executes it in an already configured security environment. When using a ``RequestPostProcessor``, the framework first calls the test method and builds the test request. The framework then applies the ``RequestPostProcessor``, which alters the request or the environment in which it’s executed before sending it. In this case, the framework configures the test dependencies, like the mock users and the SecurityContext, after building the test request.

Like setting up the username, you can set the authorities and roles for testing authorization rules. An alternative approach to creating a mock user is using a ``RequestPostProcessor``. We can provide a ``RequestPostProcessor`` the ``with()`` method.

``SecurityMockMvcRequestPostProcessors`` provided by Spring Security offers us lots of implementations for ``RequestPostProcessor``, which helps us cover various test scenarios.

The method ``user()`` of the class ``SecurityMockMvcRequestPostProcessors`` returns a ``RequestPostProcessor`` we can use as an alternative to the ``@WithMockUser`` annotation.

```java
    @Test
    void helloAuthenticated() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/hello").with(SecurityMockMvcRequestPostProcessors.user("mary")))
                .andExpect(MockMvcResultMatchers.content().string("Hello, mary"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
```

## Testing with users from a UserDetailsService

In this section, we discuss obtaining the user details for tests from a UserDetailsService. This approach is an alternative to creating a mock user. The difference is that, instead of creating a fake user, this time we need to get the user from a given UserDetailsService. You use this approach if you want to also test integration with the data source from where your app loads the user details (figure 20.6).

![chapter-20-figure-20-6.PNG](pictures/chapter-20-figure-20-6.PNG)

Note that, with this approach, we need to have a UserDetailsService bean in the context. To specify the user we authenticate from this UserDetailsService, we annotate the test method with @WithUserDetails. With the @WithUserDetails annotation, to find the user, you specify the username. The following listing presents the implementation of the test for the /hello endpoint using the @WithUserDetails annotation to define the authenticated user.

```java
@Configuration
public class ProjectConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        var john = User.withUsername("john")
                .password("12345")
                .authorities("read")
                .build();
        return new InMemoryUserDetailsManager(john);
    }
}
```

```java
    @Test
    @WithUserDetails("john")
    public void helloAuthenticated() throws Exception {
        mvc.perform(get("/hello"))
                .andExpect(status().isOk());
    }
```

## Using custom Authentication objects for testing

Generally, when using a mock user for a test, you don’t care which class the framework uses to create the Authentication instances in the SecurityContext. But say you have some logic in the controller that depends on the type of the object. Can you somehow instruct the framework to create the Authentication object for the test using a specific type? The answer is yes, and this is what we discuss in this section.

The logic behind this approach is simple. We define a factory class responsible for building the SecurityContext. This way, we have full control over how the SecurityContext for the test is built, including what’s inside it (figure 20.7). For example, we can choose to have a custom Authentication object.

![chapter-20-figure-20-7.PNG](pictures/chapter-20-figure-20-7.PNG)

Let’s open project and write a test in which we configure the mock SecurityContext and instruct the framework on how to create the Authentication object. An interesting aspect to remember about this example is that we use it to prove the implementation of a custom AuthenticationProvider. The custom AuthenticationProvider we implement in our case only authenticates a user named John.

However, as in the other two previous approaches we discussed the current approach skips authentication. For this reason, you see at the end of the example that we can actually give any name to our mock user. We follow three steps to achieve this behavior (figure 20.8): 
* Write an annotation to use over the test similarly to the way we use @WithMockUser or @WithUserDetails. 
* Write a class that implements the WithSecurityContextFactory interface. This class implements the createSecurityContext() method that returns the mock SecurityContext the framework uses for the test. 
* Link the custom annotation created in step 1 with the factory class created in step 2 via the @WithSecurityContext annotation.

### STEP 1: DEFINING A CUSTOM ANNOTATION

As properties of the annotation, you can define whatever details you need to create the mock Authentication object. I added only the username here for my demonstration. Also, don’t forget to use the annotation @Retention (RetentionPolicy.RUNTIME) to set the retention policy to runtime. Spring needs to read this annotation using Java reflection at runtime.

```java
@Retention(RetentionPolicy.RUNTIME)
public @interface WithCustomUser {
    
    String username();
}
```

### STEP 2: CREATING A FACTORY CLASS FOR THE MOCK SECURITYCONTEXT

The second step consists in implementing the code that builds the SecurityContext that the framework uses for the test’s execution. Here’s where we decide what kind of Authentication to use for the test.

```java
public class CustomSecurityContextFactory implements WithSecurityContextFactory<WithCustomUser> {
    
    @Override
    public SecurityContext createSecurityContext(WithCustomUser withCustomUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        var a = new UsernamePasswordAuthenticationToken(withCustomUser.username(), null, null);
        context.setAuthentication(a);
        return context;
    }
}
```

### STEP 3: LINKING THE CUSTOM ANNOTATION TO THE FACTORY CLASS

Using the @WithSecurityContext annotation, we now link the custom annotation we created in step 1 to the factory class for the SecurityContext we implemented in step 2.

```java
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = CustomSecurityContextFactory.class)
public @interface WithCustomUser {

    String username();
}
```

```java
    @Test
    @WithCustomUser(username = "mary")
    public void helloAuthenticated() throws Exception {
        mvc.perform(get("/hello"))
                .andExpect(status().isOk());
    }
```

Running the test, you observe a successful result. You might think, “Wait! In this example, we implemented a custom AuthenticationProvider that only authenticates a user named John. How could the test be successful with the username Mary?”. **As in the case of @WithMockUser and @WithUserDetails, with this method we skip the authentication logic.** So you can use it only to test what’s related to authorization and onward.

## Testing method security

In this section, we discuss testing method security. All the tests we wrote until now in this chapter refer to endpoints.

You can still use @WithMockUser, @WithUserDetails, or a custom annotation to define your own SecurityContext. But instead of using MockMvc, you directly inject from the context the bean defining the method you need to test.

Figure 20.9 represents graphically the three scenarios we test:
* Calling the method without an authenticated user, the method should throw AuthenticationException.
* Calling the method with an authenticated user that has an authority different than the expected one (write), the method should throw AccessDeniedException.
* Calling the method with an authenticated user that has the expected authority returns the expected result.

![chapter-20-figure-20-9.PNG](pictures/chapter-20-figure-20-9.PNG)

```java
@Service
public class NameService {

    @PreAuthorize("hasAuthority('write')")
    public String getName() {
        return "Fantastico";
    }
}
```

```java
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ProjectConfig {}
```

```java
@SpringBootTest
public class MainTests {

    @Autowired
    private NameService nameService;

    @Test
    void testNameServiceWithNoUser() {
        assertThrows(AuthenticationException.class, () -> nameService.getName());
    }

    @Test
    @WithMockUser(authorities = "read")
    void testNameServiceWithUserButWrongAuthority() {
        assertThrows(AccessDeniedException.class, () -> nameService.getName());
    }

    @Test
    @WithMockUser(authorities = "write")
    void testNameServiceWithUserButCorrectAuthority() {
        var result = nameService.getName();

        assertEquals("Fantastico", result);
    }
}
```

We don’t configure MockMvc anymore because we don’t need to call an endpoint. Instead, we directly inject the NameService instance to call the tested method.

## Testing authentication

In this section, we discuss testing authentication. Previously, in this chapter, you learned how to define mock users and test authorization configurations. But what about authentication? Can we also test the authentication logic? You need to do this if, for example, you have custom logic implemented for your authentication, and you want to make sure the entire flow works. When testing authentication, the test implementation requests work like normal client requests.

![chapter-20-figure-20-10.PNG](pictures/chapter-20-figure-20-10.PNG)

```java
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello!";
    }
}
```

```java
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    private CustomAuthenticationProvider authenticationProvider;

    public ProjectConfig(CustomAuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();
        http.authorizeRequests().anyRequest().authenticated();
    }
}
```

```java
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());

        if ("john".equals(username) && "12345".equals(password)) {
            return new UsernamePasswordAuthenticationToken(username, password, List.of());
        } else {
            throw new AuthenticationCredentialsNotFoundException("Error!");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
```

```java
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("Test calling /hello endpoint authenticating with valid credentials returns ok.")
    public void helloAuthenticatingWithValidUser() throws Exception {
        mvc.perform(get("/hello")
                .with(httpBasic("john","12345")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test calling /hello endpoint authenticating with wrong credentials returns unauthorized.")
    public void helloAuthenticatingWithInvalidUser() throws Exception {
        mvc.perform(get("/hello")
                .with(httpBasic("mary","12345")))
                .andExpect(status().isUnauthorized());
    }
}
```

Using the httpBasic() request postprocessor, we instruct the test to execute the authentication. This way, we validate the behavior of the endpoint when authenticating using either valid or invalid credentials. You can use the same approach to test the authentication with a form login.

## Testing CSRF configurations

For any mutating operation (POST, PUT, DELETE), the request needs to have a valid CSRF token in its headers. Spring Security provides an easy approach to test CSRF protection using a RequestPostProcessor.

```java
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll();
    }
}
```

```java
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String getHello() {
        return "Get Hello!";
    }

    @PostMapping("/hello")
    public String postHello() {
        return "Post Hello!";
    }
}
```

```java
@SpringBootTest
@AutoConfigureMockMvc
public class MainTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testHelloPOST() throws Exception {
        mvc.perform(post("/hello"))
                .andExpect(status().isForbidden());
    }
    @Test
    public void testHelloPOSTWithCSRF() throws Exception {
        mvc.perform(post("/hello").with(csrf()))
                .andExpect(status().isOk());
    }
}
```

## Testing CORS configurations

As you learned in chapter 10, if a browser loads a web app from one origin (say, example.com), the browser won’t allow the app to use an HTTP response that comes from a different origin (say, example.org). We use CORS policies to relax these restrictions.

All we need to do when writing tests for the CORS policies is to make sure that these headers (and maybe other CORS-related headers, depending on the complexity of your configurations) exist and have the correct values. For this validation, we can act precisely as the browser does when making a preflight request. We make a request using the HTTP OPTIONS method, requesting the value for the CORS headers.

```java
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors(c -> {
            CorsConfigurationSource source = request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of("*"));
                config.setAllowedMethods(List.of("*"));
                return config;
            };
            c.configurationSource(source);
        });

        http.csrf().disable();

        http.authorizeRequests().anyRequest().permitAll();
    }
}
```

```java
@RestController
public class HelloController {

    @PostMapping("/test")
    @ResponseBody
//    @CrossOrigin("http://localhost:8080")
    public String test() {
        return "HELLO";
    }
}
```

```java
@SpringBootTest
@AutoConfigureMockMvc
public class MainTests {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("Test CORS configuration for /test endpoint")
    public void testCORSForTestEndpoint() throws Exception {
        mvc.perform(options("/test")
                .header("Access-Control-Request-Method", "POST")
                .header("Origin", "http://www.example.com")
        )
                .andExpect(header().exists("Access-Control-Allow-Origin"))
                .andExpect(header().string("Access-Control-Allow-Origin", "*"))
                .andExpect(header().exists("Access-Control-Allow-Methods"))
                .andExpect(header().string("Access-Control-Allow-Methods", "POST"))
                .andExpect(status().isOk());
    }
}
```

## Testing reactive Spring Security implementations

Not interested.
