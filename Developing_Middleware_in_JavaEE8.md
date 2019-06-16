# What is JavaEE?

The Java EE platform follows the four-tier architecture, the tiers being as follows:

* **EIS tier**: This is the enterprise information system (EIS) tier, where we store and retrieve all of our business data. Usually, it's a relational database system that's accessed using the Java Persistence API through our business tier.
* **Business tier**: The business tier is responsible for managing business components that expose functionalities to other modules or separate systems. Enterprise JavaBeans, messaging, and other services are maintained in this tier.
* **Web tier**: The web tier is where your web components/pages live.
* **Client tier**: Either a thin client (web browser) or a thick one (another Java application) that consumes the services we provide in our enterprise middleware solution.
