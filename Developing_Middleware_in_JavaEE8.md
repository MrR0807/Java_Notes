# What is JavaEE?

The Java EE platform follows the four-tier architecture, the tiers being as follows:

* **EIS tier**: This is the enterprise information system (EIS) tier, where we store and retrieve all of our business data. Usually, it's a relational database system that's accessed using the Java Persistence API through our business tier.
* **Business tier**: The business tier is responsible for managing business components that expose functionalities to other modules or separate systems. Enterprise JavaBeans, messaging, and other services are maintained in this tier.
* **Web tier**: The web tier is where your web components/pages live.
* **Client tier**: Either a thin client (web browser) or a thick one (another Java application) that consumes the services we provide in our enterprise middleware solution.

# Dependency Injection Using CDI 2.0

Key features that CDI provides to our middleware solution:

* **DI (Dependency Injection)**: A popular technique for supplying components with other components they depend on. CDI provides a declarative approach for defining components and their scope of life, and of course obtaining them back. Moreover, DI in Java EE is used to easily retrieve essential platform components such as data sources, entity managers, enterprise Java beans, messaging destinations, and more.
* **Interceptors**: Interceptors provide a simple mechanism for handling cross-cutting concerns in enterprise applications. Interceptors are methods that can be forced to precede the call to a set of other methods, in order to perform pre and/or post operations that are common among them, such as logging, transaction management, and more. *This technique is commonly referred to by the term aspect-oriented programming.*
* **Event Handling**: Events are incidents that occur in our application, where one or more different objects are interested to react to. CDI provides a model for publishing events, where other objects can subscribe to those events.











