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












## My own implementation using Jdbc and DelegatingPasswordEncoder

schema.sql:
```
CREATE TABLE product (
  id BIGINT NOT NULL IDENTITY(1,1),
  product_name VARCHAR(255) NOT NULL,

  CONSTRAINT PK__products__id PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users (
    id          INT         NOT NULL AUTO_INCREMENT,
    username    VARCHAR(45) NOT NULL,
    password    VARCHAR(500) NOT NULL,
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

data.sql:
```
INSERT INTO product (product_name)
VALUES ('apple'), ('orange'), ('carrot');

INSERT INTO authorities (username, authority) VALUES ('john', 'write');
INSERT INTO users (username, password, enabled) VALUES ('john', '{bcrypt}$2a$10$AxC8HJv26wuwbLCPl9acsetiloyaSNSremmFWBUeBIHDemBi1eQ7i', 1);
```

**The password is 12345**.

```
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
            .and()
            .httpBasic();
        http.authorizeRequests().anyRequest().authenticated();
    }
}
```

```
@Configuration
public class UserDetailsConfig {

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
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

```
@RestController
public class HelloController {

    private final ProductsRepo productsRepo;

    public HelloController(ProductsRepo productsRepo) {
        this.productsRepo = productsRepo;
    }

    @GetMapping("products")
    public List<Product> listProducts() {
        return productsRepo.listProducts();
    }
}
```

```
public record Product(long id, String name) {

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
```

```
@Repository
public class ProductsRepo {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ProductsRepo(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<Product> listProducts() {
        return jdbcTemplate.query("SELECT id, product_name FROM product", (resultSet, i) -> {
            var id = resultSet.getLong("id");
            var productName = resultSet.getString("product_name");
            return new Product(id, productName);
        });
    }
}
```















































