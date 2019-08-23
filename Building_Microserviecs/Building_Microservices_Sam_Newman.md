# Chapter 1. Microservices

The question I am often asked is how small is small? A microservice as something that could be rewritten in two weeks. Or I nearly always ask the question who has a system that is too big and that you’d like to break down?

### Autonomous

All communication between the services themselves are via network calls, to enforce separation between the services and avoid the perils of tight coupling.
The **golden rule: can you make a change to a service and deploy it by itself without changing anything else?** If the answer is no, then many of the advantages we discuss throughout this book will be hard for you to achieve. **To do decoupling well, you’ll need to model your services right and get the APIs right. I’ll be talking about that a lot.**

### Resilience

If one component of a system fails, but that failure doesn’t cascade, you can isolate the problem and the rest of the system can carry on working.

### Scaling

With a large, monolithic service, we have to scale everything together. One small part of our overall system is constrained in performance, but if that behavior is locked up in a giant monolithic application, we have to handle scaling everything as a piece. With smaller services, we can just scale those services that need scaling.

### Ease of Deployment

A one-line change to a million-line-long monolithic application requires the whole application to be deployed in order to release the change. That could be a large-impact, high-risk deployment.

### Organizational Alignment

We know that smaller teams working on smaller codebases tend to be more productive.

### Composability

One of the key promises of distributed systems and service-oriented architectures is that we open up opportunities for reuse of functionality.

### Optimizing for Replaceability

If you work at a medium-size or bigger organization, chances are you are aware of some big, nasty legacy system sitting in the corner. The one no one wants to touch. The one that is vital to how your company runs, but that happens to be written in some odd Fortran variant and runs only on hardware that reached end of life 25 years ago. Why hasn’t it been replaced? You know why: it’s too big and risky a job.
With our individual services being small in size, the cost to replace them with a better implementation, or even delete them altogether, is much easier to manage.

**You should instead think of microservices as a specific approach for SOA in the same way that XP or Scrum are specific approaches for Agile software development.**

## No Silver Bullet

I should call out that microservices are no free lunch or silver bullet, and make for a bad choice as a golden hammer. They have all the associated complexities of distributed systems, and while we have learned a lot about how to manage distributed systems well it is still hard.

# Chapter 2. The Evolutionary Architect

Architects need to shift their thinking away from creating the perfect end product, and instead focus on helping create a framework in which the right systems can emerge, and continue to grow as we learn more.

One thing that people often forget is that our system doesn’t just accommodate users; it also accommodates developers and operations people who also have to work there, and who have the job of making sure it can change as required. To borrow a term from Frank Buschmann, architects have a duty to ensure that **the system is habitable for developers too.**

## Zoning

So, to continue the metaphor of **the architect as town planner** for a moment, what are our zones? These are our service boundaries, or perhaps coarse-grained groups of services. As architects, we need to **worry much less about what happens inside the zone than what happens between the zones.** That means we need to spend time thinking about how our services talk to each other, or ensuring that we can properly monitor the overall health of our system.

## The Required Standard

One of the key ways to identify what should be constant from service to service is to define what a well-behaved, good service looks like. What is a “good citizen” service in your system? What capabilities does it need to have to ensure that your system is manageable and that one bad service doesn’t bring down the whole system?

### Monitoring

I would suggest ensuring that all services emit health and general monitoring-related metrics in the same way. You can choose:
* **Push mechanism** - where each service needs to push this data into a central location;
* **Pull mechanism** - where data is scraped from the nodes themselves.

Logging falls into the same category here: we need it in one place.

### Interfaces

This isn’t just about picking the technology and the protocol. If you pick HTTP/REST, for example, will you use verbs or nouns? How will you handle pagination of resources? How will you handle versioning of end points?

## Governance Through Code

### Exemplars

Ideally, these should be real-world services you have that get things right, rather than isolated services that are just implemented to be perfect examples. By ensuring your exemplars are actually being used, you ensure that all the principles you have actually make sense.

## Exception Handling

# Chapter 3. How to Model Services

### Loose Coupling

When services are loosely coupled, a change to one service should not require a change to another. The whole point of a microservice is being able to make a change to one service and deploy it, without needing to change any other part of the system.

A loosely coupled service knows as little as it needs to about the services with which it collaborates.

### High Cohesion

We want related behavior to sit together, and unrelated behavior to sit elsewhere. So we want to find boundaries within our problem domain that help ensure that related behavior is in one place, and that communicate with other boundaries as loosely as possible.

### Shared and Hidden Models

For example, we can then consider the finance department and the warehouse to be two separate **bounded contexts.** They both have an explicit interface to the outside world (in terms of inventory reports, pay slips, etc.), and they have details that only they need to know about (forklift trucks, calculators).

Now the finance department doesn’t need to know about the detailed inner workings of the warehouse. It does need to know some things, though — for example it needs to know about stock levels to keep the accounts up to date.

### Modules and Services

**When starting out, however, keep a new system on the more monolithic side;** getting service boundaries wrong can be costly, so waiting for things to stabilize as you get to grips with a new domain is sensible.

It's best to firstly grow the codebase and define bounded contexts via modules than separate services.

### Premature Decomposition

Prematurely decomposing a system into microservices can be costly, especially if you are new to the domain. In many ways, having an existing codebase you want to decompose into microservices is much easier than trying to go to microservices from the beginning.

# Turtles All the Way Down

At the start, you will probably identify a number of coarse-grained bounded contexts. But these bounded contexts can in turn contain further bounded contexts. For example, you could decompose the warehouse into capabilities associated with *order fulfillment, inventory management, or goods receiving.* When considering the boundaries of your microservices, first think in terms of the larger, coarser-grained contexts, and then subdivide along these nested contexts when you’re looking for the benefits of splitting out these seams.

Whether you choose the nested approach over the full separation approach should be based on your organizational structure. If order fulfillment, inventory management, and goods receiving are managed by different teams, they probably deserve their status as top-level microservices. If, on the other hand, all of them are managed by one team, then the nested model makes more sense.

# Chapter 4. Integration

Getting integration right is the single most important aspect of the technology associated with microservices in my opinion.

## Looking for the Ideal Integration Technology

### Avoid Breaking Changes

Every now and then, we may make a change that requires our consumers to also change. We’ll discuss how to handle this later, but we want to pick technology that ensures this happens as rarely as possible.

### Keep Your APIs Technology-Agnostic

Avoid integration technology that dictates what technology stacks we can use to implement our microservices.

### Make Your Service Simple for Consumers
### Hide Internal Implementation Detail

## Interfacing with Customers

### The Shared Database

By far the most common form of integration that I or any of my colleagues see in the industry is database (DB) integration.
First, we are allowing external parties to view and bind to internal implementation details. If I decide to change my schema to better represent my data, or make my system easier to maintain, I can break my consumers. The DB is effectively a very large, shared API that is also quite brittle.

Second, my consumers are tied to a specific technology choice. Perhaps right now it makes sense to store customers in a relational database, so my consumers use an appropriate (potentially DB-specific) driver to talk to it. What if over time we realize we would be better off storing data in a nonrelational database?

Remember when we talked about the core principles behind good microservices? Strong cohesion and loose coupling — with database integration, we lose both things.

**Avoid at (nearly) all costs.**

## Synchronous Versus Asynchronous

Synchronous communication can be easier to reason about. We know when things have completed successfully or not. 
Asynchronous communication can be very useful for long-running jobs, where keeping a connection open for a long period of time between the client and server is impractical. It also works very well when you need low latency.

These two different modes of communication can enable two different idiomatic styles of collaboration: **request/response** or **event-based.**

## Orchestration Versus Choreography

As we start to model more and more complex logic, we have to deal with the problem of managing business processes that stretch across the boundary of individual services. Let’s take an example from MusicCorp, and look at what happens when we create a customer:
* A new record is created in the loyalty points bank for the customer.
* Our postal system sends out a welcome pack.
* We send a welcome email to the customer.

When it comes to actually implementing this flow, there are two styles of architecture we
could follow:
* With **orchestration**, we rely on a central brain to guide and drive the process, much like the conductor in an orchestra. 
* With **choreography**, we inform each part of the system of its job, and let it work out the details, like dancers all finding their way and reacting to others around them in a ballet.

Let’s think about what an orchestration solution would look like for this flow. Here, probably the simplest thing to do would be to have our customer service act as the central brain. On creation, it talks to the loyalty points bank, email service, and postal service, through a series of request/response calls. **The downside to this orchestration approach is that the customer service can become too much of a central governing authority. It can become the hub in the middle of a web, and a central point where logic starts to live.**

With a choreographed approach, we could instead just have the customer service emit an event in an asynchronous manner, saying Customer created. The email service, postal service, and loyalty points bank then just subscribe to these events and react accordingly. This approach is significantly more decoupled. If some other service needed to reach to the creation of a customer, it just needs to subscribe to the events and do its job when needed. **The downside is that the explicit view of the business process is now only implicitly reflected in our system.**

In general, I have found that systems that tend more toward the **choreographed approach are more loosely coupled, and are more flexible and amenable to change.** You do need to do extra work to monitor and track the processes across system boundaries, however. I have found most heavily orchestrated implementations to be extremely brittle, with a higher cost of change. With that in mind, I **strongly prefer aiming for a choreographed system, where each service is smart enough to understand its role in the whole dance.**


**Synchronous calls are simpler, and we get to know if things worked straightaway.** If we like the semantics of request/response but are dealing with longer-lived processes, we could just initiate **asynchronous requests and wait for callbacks.** On the other hand, **asynchronous event collaboration helps us adopt a choreographed approach, which can yield significantly more decoupled services.**

## Technologies that fit well with request/response

### Remote Procedure Calls (RPC)

Remote procedure call refers to the technique of making a local call and having it execute on a remote service somewhere. There are a number of different types of RPC technology out there. Some of this technology relies on having an interface definition (SOAP, Thrift, protocol buffers).
All these technologies, have the same, core characteristic in that they make a local call look like a remote call.

### Local Calls Are Not Like Remote Calls

The core idea of RPC is to hide the complexity of a remote call. Many implementations of RPC, though, hide too much. **The drive in some forms of RPC to make remote method calls look like local method calls hides the fact that these two things are very different.** I can make large numbers of local, in-process calls without worrying overly about the performance. With RPC, though, the cost of marshalling and un-marshalling payloads can be significant, not to mention the time taken to send things over the network.

Famously, the first of the fallacies of distributed computing is “The network is reliable”. **Networks aren’t reliable. They can and will fail, even if your client and the server you are speaking to are fine.**

### Is RPC Terrible?

Despite its shortcomings, I wouldn’t go so far as to call RPC terrible. Just be aware of some of the potential pitfalls associated with RPC if you’re going to pick this model. **Don’t abstract your remote calls to the point where the network is completely hidden, and ensure that you can evolve the server interface without having to insist on lock-step upgrades for clients.**

Compared to database integration, RPC is certainly an improvement when we think about options for request/response collaboration. But there’s another option to consider.

### Representational State Transfer (REST)

Most important is the concept of resources.
REST itself doesn’t really talk about underlying protocols, although it is most commonly used over HTTP. Some of the features that HTTP gives us as part of the specification, such as verbs, make implementing REST over HTTP easier, whereas with other protocols you’ll have to handle these features yourself.

### REST and HTTP

HTTP itself defines some useful capabilities that play very well with the REST style. For example, the HTTP verbs (e.g., GET, POST, and PUT) already have well-understood meanings in the HTTP specification as to how they should work with resources.

HTTP also brings a large ecosystem of supporting tools and technology. We get to use HTTP caching proxies and load balancers, and many monitoring tools already have lots of support for HTTP out of the box.

Note that HTTP can be used to implement RPC too. SOAP, for example, gets routed over HTTP, but unfortunately uses very little of the specification. Verbs are ignored, as are simple things like HTTP error codes.

### Hypermedia As the Engine of Application State

Another principle introduced in REST that can help us avoid the coupling between client and server is the concept of **hypermedia as the engine of application state (HATEOAS).**

Hypermedia is a concept whereby a piece of content contains links to various other pieces of content in a variety of formats (e.g., text, images, sounds). This should be pretty familiar to you, as **it’s what the average web page does: you follow links, which are a form of hypermedia controls, to see related content.** The idea behind HATEOAS is that clients should perform interactions (potentially leading to state transitions) with the server via these links to other resources.

Let’s look at a hypermedia control that we might have for MusicCorp.

    <album>
      <name>Give Blood</name>
      <link rel="/artist" href="/artist/theBrakes" />
      <description>
          Awesome, short, brutish, funny and loud. Must buy!
      </description>
      <link rel="/instantpurchase" href="/instantPurchase/1234" />
    </album>

In this document, we have two hypermedia controls. The client reading such a document needs to know that a control with a relation of artist is where it needs to navigate to get information about the artist, and that instantpurchase is part of the protocol used to purchase the album.

As a client, I don’t need to know which URI scheme to access to buy the album, I just need to access the resource, find the buy control, and navigate to that. The buy control could change location, the URI could change, or the site could even send me to another service altogether, and as a client I wouldn’t care. **This gives us a huge amount of decoupling between the client and server.**

One of the **downsides** is that **this navigation of controls can be quite chatty, as the client needs to follow links to find the operation it wants to perform.** Ultimately, this is a trade-off. I would suggest you **start with having your clients navigate these controls first, then optimize later if necessary.**

### JSON, XML, or Something Else?

The fact that JSON is a much simpler format means that consumption is also easier. JSON does have some downsides, though. XML defines the link control we used earlier as a hypermedia control. The JSON standard doesn’t define anything similar, so in-house styles are frequently used to shoe-horn this concept in. The **Hypertext Application Language (HAL)** attempts to fix this by defining some common standards for hyperlinking for JSON

### Beware Too Much Convenience

**Some frameworks actually make it very easy to simply take database representations of objects, deserialize them into in-process objects, and then directly expose these externally.** I remember at a conference seeing this demonstrated using Spring Boot and cited as a major advantage. **The inherent coupling that this setup promotes will in most cases cause far more pain than the effort required to properly decouple these concepts.*

## Implementing Asynchronous Event-Based Collaboration

### Technology Choices

There are two main parts we need to consider here: 
* A way for our microservices to emit events
* A way for our consumers to find out those events have happened

Traditionally, message brokers like RabbitMQ try to handle both problems. Producers use an API to publish an event to the broker. The broker handles subscriptions, allowing consumers to be informed when an event arrives.

It can add complexity to the development process, because it is another system you may need to run to develop and test your services. However, it can be an incredibly effective way to implement loosely coupled, event-driven architectures. In general, I’m a fan.

Do be wary, though, about the world of middleware, of which the message broker is just a small part. Queues in and of themselves are perfectly sensible, useful things. However, vendors tend to want to package lots of software with them, which can lead to more and more smarts being pushed into the middleware, as evidenced by things like the Enterprise Service Bus. **Keep your middleware dumb, and keep the smarts in the endpoints.**

Another approach is to try to use HTTP as a way of propagating events. **ATOM** is a REST- compliant specification that defines semantics (among other things) for publishing feeds of resources.

**But Message Broker is better between these two.**

### Complexities of Asynchronous Architectures

**Asynchronous architectures lead to an increase in complexity.** For example, when considering longrunning async request/response, we have to think about what to do when the response comes back. Does it come back to the same node that initiated the request? If so, what if that node is down?

The associated complexity with event-driven architectures and asynchronous programming in general leads me to believe that you should be cautious in how eagerly you start adopting these ideas. **Ensure you have good monitoring in place, and strongly consider the use of correlation IDs, which allow you to trace requests across process boundaries.**

### Services as State Machines

Our *customer microservice* **owns** all logic associated with behavior in this context.

When a consumer wants to change a customer, it sends an appropriate request to the customer service. The customer service, based on its logic, gets to decide if it accepts that request or not. **Our customer service controls all lifecycle events associated with the customer itself.** We want to avoid dumb, anemic services that are little more than CRUD wrappers. **If the decision about what changes are allowed to be made to a customer leak out of the customer service itself, we are losing cohesion.**

### Reactive Extensions

At its heart, Rx inverts traditional flows. Rather than asking for some data, then performing operations on it, you observe the outcome of an operation (or set of operations) and react when something changes.

### DRY and the Perils of Code Reuse in a Microservice World

Sometimes, however, the use of shared code can create this very coupling. For example, at one client we had a library of common domain objects that represented the core entities in use in our system. This library was used by all the services we had. But when a change was made to one of them, all services had to be updated. Our system communicated via message queues, which also had to be drained of their now invalid contents, and woe betide you if you forgot.

### Client Libraries

The argument is that this makes it easy to use your service, and avoids the duplication of code required to consume the service itself. The problem, of course, is that if the same people create both the server API and the client API, there is the danger that logic that should exist on the server starts leaking into the client.

### Access by Reference

Let’s consider the example where we ask the email service to send an email when an order has been shipped. Now we could send in the request to the email service with the customer’s email address, name, and order details. However, if the email service is actually queuing up these requests, or pulling them from a queue, things could change in the meantime. It might make more sense to just send a URI for the Customer and Order resources, and let the email server go look them up when it is time to send the email.

## Versioning

### Defer It for as Long as Possible

The best way to reduce the impact of making breaking changes is to avoid making them in the first place.

**Another key to deferring a breaking change is to encourage good behavior in your clients, and avoid them binding too tightly to your services in the first place.** Use as little as possible from the service, deserialize only what you require.

### Catch Breaking Changes Early

I am strongly in favor of using consumer-driven contracts.

Once you realize you are going to break a consumer, you have the choice to either try to avoid the break altogether or else embrace it and start having the right conversations with the people looking after the consuming services.

### Use Semantic Versioning

**Semantic versioning is a specification that allows just that. With semantic versioning, each version number is in the form MAJOR.MINOR.PATCH.** When the MAJOR number increments, it means that backward incompatible changes have been made. When MINOR increments, new functionality has been added that should be backward compatible. Finally, a change to PATCH states that bug fixes have been made to existing functionality.

### Coexist Different Endpoints

If we want to release a breaking change, we deploy a new version of the service that exposes both the old and new versions of the endpoint.

When I last used this approach, we had gotten ourselves into a bit of a mess with the number of consumers we had and the number of breaking changes we had made. This meant that we were actually coexisting three different versions of the endpoint.
**To make this more manageable, we internally transformed all requests to the V1 endpoint to a V2 request, and then V2 requests to the V3 endpoint. This meant we could clearly delineate what code was going to be retired when the old endpoint(s) died.**

For systems making use of HTTP, I have seen this done with both **version numbers in request headers and also in the URI itself—for example, /v1/customer/ or /v2/customer/.** I’m torn as to which approach makes the most sense.

### Use Multiple Concurrent Service Versions

Another versioning solution often cited is to have different versions of the service live at once, and for older consumers to route their traffic to the older version.

Web Shop -> Old microservice -> Same_DB
Admin -> New microservice -> Same_DB

Coexisting concurrent service versions for a short period of time can make perfect sense, especially when you’re doing things like blue/green deployments or canary releases. The longer it takes for you to get consumers upgraded to the newer version and released, the more you should look to coexist different endpoints in the same microservice rather than coexist entirely different versions. I remain unconvinced that this work is worthwhile for the average project.

## User Interfaces

### API Composition

Assuming that our services already speak XML or JSON to each other via HTTP, an obvious option available to us is to have our user interface interact directly with these APIs. A web-based UI could use JavaScript GET requests to retrieve data, or POST requests to change it.

[Direct API communication](Direct_API_communication.PNG)

### UI Fragment Composition

Rather than having our UI make API calls and map everything back to UI controls, we could have our services provide parts of the UI directly, and then just pull these fragments in to create a UI.

[UI Fragment Composition](UI_Fragment_Composition.PNG)

### Backends for Frontends

A common solution to the problem of chatty interfaces with backend services, or the need to vary content for different types of devices, is to have a server-side aggregation endpoint, or API gateway.

[API Gateway](API_Gateway.PNG)

The problem that can occur is that normally we’ll have one giant layer for all our services. This leads to everything being thrown in together, and suddenly we start to lose isolation of our various user interfaces, limiting our ability to release them independently. 

**A model I prefer and that I’ve seen work well is to restrict the use of these backends to one specific user interface or application.** This pattern is sometimes referred to as **backends for frontends (BFFs).**
The danger with this approach is the same as with any aggregating layer; it can take on logic it shouldn’t. The business logic for the various capabilities these backends use should stay in the services themselves.

[Back-end for front-end](BFF.PNG)

## Summary

* Avoid database integration at all costs.
* Understand the trade-offs between REST and RPC, but strongly consider REST as a good starting point for request/response integration.
* Prefer choreography over orchestration.
* Avoid breaking changes and the need to version by understanding Postel’s Law and using tolerant readers.
* Think of user interfaces as compositional layers.

# CHAPTER 5. Splitting the Monolith

## It’s All About Seams

The problem with the monolith is that all too often it is the opposite of both. Rather than tend toward cohesion, and keep things together that tend to change together, we acquire and stick together all sorts of unrelated code. Likewise, loose coupling doesn’t really exist.

In his book Working Effectively with Legacy Code (Prentice-Hall), Michael Feathers defines the concept of a ***seam* — that is, a portion of the code that can be treated in isolation and worked on without impacting the rest of the codebase.**

Bounded contexts make excellent seams, because by definition they represent cohesive and yet loosely coupled boundaries in an organization.

### Example: Breaking Foreign Key Relationships

How do we fix things here? Well, we need to make a change in two places. First, we need to stop the finance code from reaching into the line item table, as this table really belongs to the catalog code, and we don’t want database integration happening once catalog and finance are services in their own rights. **The quickest way to address this is rather than having the code in finance reach into the line item table, we’ll expose the data via an API call in the catalog package that the finance code can call.**

At this point it becomes clear that we may well end up having to make two database calls to generate the report. This is correct. And the same thing will happen if these are two separate services. Typically concerns around performance are now raised. **Sometimes making one thing slower in exchange for other things is the right thing to do, especially if slower is still perfectly acceptable.**

But what about the foreign key relationship? Well, we lose this altogether. **This becomes a constraint we need to now manage in our resulting services rather than in the database level.** This may mean that we need to implement our own consistency check across services, or else trigger actions to clean up related data.

### Staging the Break

So we’ve found seams in our application code, grouping it around bounded contexts. We’ve used this to identify seams in the database, and we’ve done our best to split those out. What next? Do you do a big-bang release, going from one monolithic service with a single schema to two services, each with its own schema? I would actually recommend that you split out the schema but keep the service together before splitting the application code out into separate microservices.

### Transactional Boundaries

If we have pulled apart the schema into two separate schemas, one for customerrelated data including our order table, and another for the warehouse, we have lost this transactional safety. The order placing process now spans two separate transactional boundaries.

Possible solutions:
* **Try Again Later** - eventual consistency. Rather than using a transactional boundary to ensure that the system is in a consistent state when the transaction completes, instead we accept that the system will get itself into a consistent state at some point in the future.
* **Abort the Entire Operation** - we have to do is issue a compensating transaction, kicking off a new transaction to wind back what just happened. For us, that could be something as simple as issuing a DELETE statement to remove the order from the database. Then we’d also need to report back via the UI that the operation failed. However, if we have not one or two operations we want to be consistent, but three, four, or five. Handling compensating transactions for each failure mode becomes quite challenging to comprehend, let alone implement.
* **Distributed Transactions**

### Distributed Transactions

An alternative to manually orchestrating compensating transactions is to use a distributed transaction. Distributed transactions try to span multiple transactions within them, using some overall governing process called a transaction manager to orchestrate the various transactions being done by underlying systems.




































