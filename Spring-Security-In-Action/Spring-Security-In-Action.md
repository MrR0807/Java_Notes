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

        var user = User.withUsername("jhon")
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
curl -u jhon:12345 http://localhost:8080/hello
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

        var user = User.withUsername("jhon")
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






































