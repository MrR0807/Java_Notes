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
* Access-Control-Allow-Headers — Adds limitations to which headers youcan use in a specific request.

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



































