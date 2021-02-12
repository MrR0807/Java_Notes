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

































