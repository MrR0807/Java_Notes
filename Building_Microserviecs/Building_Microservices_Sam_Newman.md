# Chapter 1. Microservices

The question I am often asked is how small is small? A microservice as something that could be rewritten in two weeks. Or I nearly always ask the question who has a system that is too big and that you’d like to break down?

### Autonomous

All communication between the services themselves are via network calls, to enforce separation between the services and avoid the perils of tight coupling.
The **golden rule: can you make a change to a service and deploy it by itself without changing anything else?** If the answer is no, then many of the advantages we discuss throughout this book will be hard for you to achieve. **To do decoupling well, you’ll need to model your services right and get the APIs right.**

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

An alternative to manually orchestrating compensating transactions is to use a distributed transaction. Distributed transactions try to span multiple transactions within them, using some overall governing process called a **transaction manager** to orchestrate the various transactions being done by underlying systems.

The most common algorithm for handling distributed transactions — especially shortlived transactions, as in the case of handling our customer order — is to use a **twophase commit.** With a two-phase commit, first comes the **voting phase.** This is where each participant (also called a cohort in this context) in the distributed transaction tells the transaction manager whether it thinks its local transaction can go ahead. If the transaction manager gets a yes vote from all participants, then it tells them all to go ahead and perform their commits. A single no vote is enough for the transaction manager to send out a rollback to all parties.

Problems:
* If the transaction manager goes down, the pending transactions never complete.
* If a cohort fails to respond during voting, everything blocks.
* And there is also the case of what happens if a commit fails after voting.

### So What to Do?

All of these solutions add complexity. As you can see, distributed transactions are hard to get right and can actually inhibit scaling.

If you do encounter state that really, really wants to be kept consistent, do everything you can to avoid splitting it up in the first place. **Try really hard.**

If you really need to go ahead with the split, think about moving from a purely technical view of the process (e.g., a database transaction) and actually create a concrete concept to represent the transaction itself. For example, you might create the idea of an “inprocess-order”.

## Reporting

### The Reporting Database

In a standard, monolithic service architecture, all our data is stored in one big database. This means all the data is in one place, so reporting across all the information is actually pretty easy, as we can simply join across the data via SQL queries or the like. Typically we won’t run these reports on the main database for fear of the load generated by our queries impacting the performance of the main system, so often these reporting systems hang on a read replica.

### Data Retrieval via Service Calls

There are many variants of this model, but they all rely on pulling the required data from the source systems via API calls. For a very simple reporting system, like a dashboard that might just want to show the number of orders placed in the last 15 minutes, this might be fine. This approach breaks down rapidly with use cases that require larger volumes of data, however. Imagine a use case where we want to report on customer purchasing behavior for our music shop over the last 24 months, looking at various trends in customer behavior and how this has impacted on revenue.

One of the key challenges is that the APIs exposed by the various microservices may well not be designed for reporting use cases.

While we could speed up some of the data retrieval by adding cache headers to the resources exposed by our service, and have this data cached in something like a reverse proxy, the nature of reporting is often that we access the long tail of data.

You could resolve this by exposing **batch APIs** to make reporting easier.

### Data Pumps

Rather than have the reporting system pull the data, we could instead have the data pushed to the reporting system.

An alternative option is to have a standalone program that directly accesses the database of the service that is the source of data, and pumps it into a reporting database.

Customer Service (app) -> Database
                            ||
                            \/
                          Customer data pump app
                            ||
                            \/
Reporting tools (app) -> Central reporting database


**To start with, the data pump should be built and managed by the same team that manages the service.** This can be something as simple as a command-line program triggered via Cron. This program needs to have intimate knowledge of both the internal database for the service, and also the reporting schema. The pump’s job is to map one from the other.

### Event Data Pump

For example, our customer service may emit an event when a given customer is created, or updated, or deleted. For those microservices that expose such event feeds, we have the option of writing our own event subscriber that pumps data into the reporting database.

Customer Service ----State change even--->  Customer Reporting Mapper -> Central reporting database

The coupling on the underlying database of the source microservice is now avoided. Instead, we are just binding to the events emitted by the service, which are designed to be exposed to external consumers.

The main downsides to this approach are that all the required information must be broadcast as events, and it may not scale as well as a data pump for larger volumes of data that has the benefit of operating directly at the database level.

### Understanding Root Causes

The first thing to understand is that growing a service to the point that it needs to be split is completely OK. **We *want* the architecture of our system to change over time in an incremental fashion.**

# CHAPTER 6 Deployment

### A Brief Introduction to Continuous Integration

With CI, the core goal is to keep everyone in sync with each other, which we achieve by making sure that newly checked-in code properly integrates with existing code. To do this, a CI server detects that the code has been committed, checks it out, and carries out some verification like making sure the code compiles and that tests pass.

The approach I prefer is to **have a single CI build per microservice**, to allow us to quickly make and validate a change prior to deployment into production. Here each microservice has its own source code repository, mapped to its own CI build. When making a change, I run only the build and tests I need to. I get a single artifact to deploy. Alignment to team ownership is more clear too. If you own the service, you own the repository and the build.

The tests for a given microservice should live in source control with the microservice’s source code too, to ensure we always know what tests should be run against a given service. So, each microservice will live in its own source code repository, and its own CI build process.

### Build Pipelines and Continuous Delivery

Continuous delivery (CD) to have different stages in our build, creatingwhat is known as a build pipeline. One stage for the faster tests, one for the slower tests, etc.

To fully embrace this concept, we need to model all the processes involved in getting our software from check-in to production, and know where any given version of the software is in terms of being cleared for release.

Compile & fast tests -> Slow tests -> UAT -> Performance testing -> Production

As a version of our code moves through the pipeline, if it passes one of these automated verification steps it moves to the next stage. Other stages may be manual. For example, if we have a manual user acceptance testing (UAT) process I should be able to use a CD tool to model it. I can see the next available build ready to be deployed into our UAT environment, deploy it, and if it passes our manual checks, mark that stage as being successful so it can move to the next.

### And the Inevitable Exceptions

When a team is starting out with a new project, especially a greenfield one where they are working with a blank sheet of paper, it is quite likely that there will be a large amount of churn in terms of working out where the service boundaries lie. This is a good reason, in fact, for keeping your initial services on the larger side until your understanding of the domain stabilizes.

### Multiple Services Per Host

There are some challenges with this model, though. First, it can make monitoring more difficult. For example, when tracking CPU, do I need to track the CPU of one service independent of the others? Or do I care about the CPU of the box as a whole? Side effects can also be hard to avoid. If one service is under significant load, it can end up reducing the resources available to other parts of the system. Gilt, when scaling out the number of services it ran, hit this problem. Initially it coexisted many services on a single box, but uneven load on one of the services would have an adverse impact on everything else running on that host.

Deployment of services can be somewhat more complex too, as ensuring one deployment doesn’t affect another leads to additional headaches.

### Application Containers

If you’re familiar with deploying .NET applications behind IIS or Java applications into a servlet container, you will be well acquainted with the model where multiple distinct services or applications sit inside a single application container, which in turn sits on a single host.

Downsides:
* Technology constrain;
* Monitoring capabilities they provide won’t be sufficient;
* Attempting to do proper lifecycle management of applications on top of platforms like the JVM can be problematic, and more complex than simply restarting a JVM. Analyzing resource use and threads is also much more complex, as you have multiple applications sharing the same process;
* This approach is again an attempt to optimize for scarcity of resources that simply may not hold up anymore.

### Single Service Per Host

Pros:
* With a single-service-per-host model we avoid side effects of multiple hosts living on a single host, making monitoring and remediation much simpler. We have potentially reduced our single points of failure. 
* We also can more easily scale one service independent from others, and deal with security concerns more easily by focusing our attention only on the service and host that requires it.
* In my opinion, if you don’t have a viable PaaS available, then this model does a very good job of reducing a system’s overall complexity.
Cons:
* Potentially less efficient resource utilization.
* We have more servers to manage.

## From Physical to Virtual

### Traditional Virtualization

**Virtualization** allows us to slice up a physical server into separate hosts, each of which can run different things.

**Slicing up the machine into ever increasing VMs isn’t free.** Think of our physical machine as a sock drawer. If we put lots of wooden dividers into our drawer, can we store more socks or fewer? The answer is fewer: **the dividers themselves take up room too!**

### Vagrant

Vagrant is a very useful deployment platform, which is normally used for dev and test rather than production.
You can spin up multiple VMs at a time, shut individual ones to test failure modes, and have the VMs mapped through to local directories so you can make changes and see them reflected immediately.
One of the downsides, though, is that running lots of VMs can tax the average development machine.

### Linux Containers

For Linux users, there is an alternative to virtualization. Rather than having a hypervisor to segment and control separate virtual hosts, Linux containers instead create a separate process space in which other processes live.

Each container is effectively a subtree of the overall system process tree. These containers can have physical resources allocated to them, something the kernel handles for us.

**First we don’t need a hypervisor.** Second, although each container can run its own operating system distribution, it has to share the same kernel (because the kernel is where the process tree lives). This means that our host operating system could run Ubuntu, and our containers CentOS, as long as they could both share the same kernel.

We don’t just benefit from the resources saved by not needing a hypervisor. We also gain in terms of feedback. Linux containers are much faster to provision than full-fat virtual machines.

**Linux containers aren’t without some problems:**
* Imagine I have lots of microservices running in their own containers on a host. How does the outside world see them? You need some way to route the outside world through to the underlying containers, something many of the hypervisors do for you with normal virtualization. I’ve seen many a person sink inordinate amounts of time into configuring port forwarding using IPTables to expose containers directly. 
* These containers cannot be considered completely sealed from each other. There are many documented and known ways in which a process from one container can bust out and interact with other containers or the underlying host.

### Docker

Docker is a platform built on top of lightweight containers. Docker manages the container provisioning, handles some of the networking problems for you, and even provides its own registry concept that allows you to store and version Docker applications.

Docker can also alleviate some of the downsides of running lots of services locally for dev and test purposes. **Rather than using Vagrant to host multiple independent VMs, each one containing its own service, we can host a single VM in Vagrant that runs a Docker instance. We then use Vagrant to set up and tear down the Docker platform itself, and use Docker for fast provisioning of individual services.**

Docker itself doesn’t solve all problems for us. **Think of it as a simple PaaS that works on a single machine.** If you want tools to help you manage services across multiple Docker instances across multiple machines, you’ll need to look at other software that adds these capabilities. There is a key need for a scheduling layer that lets you request a container and then finds a Docker container that can run it for you. In this space, Google’s recently open sourced Kubernetes.

## Summary

* Focus on maintaining the ability to release one service independently from another.
* Move to a single-service per host/container

# CHAPTER 7 Testing

## Types of Tests

### Unit Tests

We’re not launching services here, and are limiting the use of external files or network connections. The prime goal of these tests is to give us very fast feedback about whether our functionality is good.

### Service Tests

Service tests are designed to bypass the user interface and test services directly. In a monolithic application, we might just be testing a collection of classes that provide a service to the UI. For a system comprising a number of services, a service test would test an individual service’s capabilities.

The reason we want to test a single service by itself is to improve the isolation of the test to make finding and fixing problems faster. To achieve this isolation, we need to stub out all external collaborators so only the service itself is in scope.

### End-to-End Tests

Often they will be driving a GUI through a browser, but could easily be mimicking other sorts of user interaction, like uploading a file.

### Implementing Service Tests

Our service tests want to test a slice of functionality across the whole service, but to isolate ourselves from other services we need to find some way to stub out all of our collaborators.

### Mocking or Stubbing

When I talk about stubbing downstream collaborators, I mean that we create a stub service that responds with canned responses to known requests from the service under test. For example, I might tell my stub points bank that when asked for the balance of customer 123, it should return 15,000.

Mocks can be very useful to ensure that the expected side effects happen. For example, I might want to check that when I create a customer, a new points balance is set up for that customer.

### A Smarter Stub Service

Normally for stub services I’ve rolled them myself. I’ve used everything from Apache or Nginx to embedded Jetty containers. 

### Those Tricky End-to-End Tests

To implement an end-to-end test we need to deploy multiple services together, then run a test against all of them. Obviously, this test has much more scope, resulting in more confidence that our system works! On the other hand, these tests are liable to be slower and make it harder to diagnose failure.

Cons:
* Against which other services' versions to test? What if there are more new ones?
* There could be a lot of overlaping (indirectional calls). Some services might call other services and thus then those services create their on end-to-end test they might test the same calls.
* More moving parts. Some services might not be up, and test fails, but for a wrong reason.

If you have tests that **sometimes fail**, but everyone just re-runs them because they may pass again later, then you have flaky tests.

**End-to-end tests makes sense for a small amount of services. But once you have to deploy >5 services just to make sure that tests pass, its going to be a pain.**

### Consumer-Driven Tests to the Rescue

What is one of the key problems we are trying to address when we use the integration tests outlined previously? **We are trying to ensure that when we deploy a new service to production, our changes won’t break consumers.**

One way we can do this without requiring testing against the real consumer is by using a **consumer-driven contract (CDC).**

Example. The customer service has two separate consumers: the helpdesk and web shop. Both these consuming services have expectations for how the customer service will behave. In this example, you create two sets of tests: one for each consumer representing the helpdesk’s and web shop’s use of the customer service. A good practice here is to have someone from the producer and consumer teams collaborate on creating the tests, so perhaps people from the web shop and helpdesk teams pair with people from the customer service team.

Because these **CDCs are expectations on how the customer service should behave, they can be run against the customer service by itself** with any of its downstream dependencies stubbed out.

These tests are focused on how a consumer will use the service, and the trigger if they break is very different when compared with service tests. If one of these CDCs breaks during a build of the customer service, it becomes obvious which consumer would be impacted. At this point, you can either fix the problem or else start the discussion about introducing a breaking change.

### Pact

Pact is a consumer-driven testing tool. 

**How Pact works?**

The consumer starts by defining the expectations of the producer using a Ruby DSL. Then, you launch a local mock server, and run this expectation against it to create the Pact specification file. The Pact file is just a formal JSON specification; you could obviously handcode these, but using the language API is much easier. This also gives you a running mock server that can be used for further isolated tests of the consumer.

On the producer side, you then verify that this consumer specification is met by using the JSON Pact specification to drive calls against your API and verify responses. For this to work, the producer codebase needs access to the Pact file.

As the JSON Pact specification is created by the consumer, this needs to become an artifact that the producer build has access to. You could store this in your CI/CD tool’s artifact repository, or else use the Pact Broker, which allows you to store multiple versions of your Pact specifications.

### It’s About Conversations

It is important to understand that **CDCs require good communication and trust between the consumer and producing service.** If both parties are in the same team (or the same person!), then this shouldn’t be hard. However, if you are consuming a service provided with a **third party, you may not have the frequency of communication, or trust, to make CDCs work. In these situations, you may have to make do with limited larger-scoped integration tests just around the untrusted component.** Alternatively, if you are **creating an API for thousands of potential consumers, such as with a publicly available web service API, you may have to play the role of the consumer yourself.**

### So Should You Use End-to-End Tests?

You can view running end-to-end tests prior to production deployment as training wheels. While you are learning how CDCs work, and improving your production monitoring and deployment techniques, these end-to-end tests may form a useful safety net, where you are trading off cycle time for decreased risk. But as you improve those other areas, you can start to reduce your reliance on end-to-end tests to the point where they are no longer needed.

### Smoke Tests

Smoke test suite - a collection of tests designed to be run against newly deployed software to confirm that the deployment worked. A simple integration test where we just check that when the system under test is invoked it returns normally and does not blow up.

### Blue/Green Deployment

With blue/green, we have two copies of our software deployed at a time, but only one version of it is receiving real requests.

In production, we have v123 of the customer service live. We want to deploy a new version, v456. We deploy this alongside v123, but do not direct any traffic to it. Instead, we perform some testing in situ against the newly deployed version. Once the tests have worked, we direct the production load to the new v456 version of the customer service.

### Canary Releasing

**With canary releasing, we are verifying our newly deployed software by directing amounts of production traffic against the system to see if it performs as expected.** “Performing as expected” can cover a number of things, both functional and nonfunctional. For example, we could check that a newly deployed service is responding to requests within 500ms, or that we see the same proportional error rates from the new and the old service.

If the new release is bad, you get to revert quickly. If it is good, you can push increasing amounts of traffic through the new version. **Canary releasing differs from blue/green in that you can expect versions to coexist for longer, and you’ll often vary the amounts of traffic.**

Canary releasing is a powerful technique, and can help you verify new versions of your software with real traffic, while giving you tools to manage the risk of pushing out a bad release. It does require a more complex setup, however, than blue/green deployment, and a bit more thought. You could expect to coexist different versions of your services for longer than with blue/green, so you may be tying up more hardware for longer than before. You’ll also need more sophisticated traffic routing, as you may want to ramp up or down the percentages of the traffic to get more confidence that your release works.

## Cross-Functional Testing (Nonfunctional Requirements)

### Performance Tests

When decomposing systems into smaller microservices, we increase the number of calls that will be made across network boundaries. Where previously an operation might have involved one database call, it may now involve three or four calls across network boundaries to other services, with a matching number of database calls.

Start with tests that check core journeys in your system. You may be able to take end-to-end journey tests and simply run these at volume.

## Summary

* Optimize for fast feedback, and separate types of tests accordingly.
* Avoid the need for end-to-end tests wherever possible by using consumer-driven contracts.
* Use consumer-driven contracts to provide focus points for conversations between teams.

# Chapter 8. Monitoring

Breaking system up into smaller, fine-grained microservices results in multiple benefits. It also, however, **adds complexity when it comes to monitoring the system in production.**

* Logs (Kibana, logstash)
* Gather CPU metrics

### Service Metrics

**I would strongly suggest having your services expose basic metrics themselves. At a bare minimum, for a web service you should probably expose metrics like response times and error rates** - vital if your server isn’t fronted by a web server that is doing this for you. 

But you should really go further. For example, our accounts service may want to expose the number of times customers view their past orders, or your web shop might want to capture how much money has been made during the last day. Why do we care about this? Well, for a number of reasons. First, there is an old adage that 80% of software features are never used.

Second, we are getting better than ever at reacting to how our users are using our system to work out how to improve it. Metrics that inform us of how our systems behave can only help us here.

**I tend to err toward exposing everything and relying on my metrics system to handle this later.**

### Synthetic Monitoring

We can try to work out if a service is healthy by, for example, deciding what a good CPU level is, or what makes for an acceptable response time. Sometimes systems cannot generate enough events to have a determed good average/base point. The solution - generate fake events to help gather enough statistics.

### Correlation IDs

One approach that can be useful here is to use correlation IDs. When the first call is made, you generate a GUID for the call. This is then passed along to all subsequent calls and can be put into your logs in a structured way, much as you’ll already do with components like the log level or date. With the right log aggregation tooling, you’ll then be able to trace that event all the way through your system:

    15-02-2014 16:01:01 Web-Frontend INFO [abc-123] Register
    15-02-2014 16:01:02 RegisterService INFO [abc-123] RegisterCustomer ...
    15-02-2014 16:01:03 PostalSystem INFO [abc-123] SendWelcomePack ...
    15-02-2014 16:01:03 EmailSystem INFO [abc-123] SendWelcomeEmail ...
    15-02-2014 16:01:03 PaymentGateway ERROR [abc-123] ValidatePayment ...

Software such as Zipkin can also trace calls across multiple system boundaries. Based on the ideas from Google’s own tracing system, Dapper, Zipkin can provide very detailed tracing of interservice calls, along with a UI to help present the data.

### The Cascade

Cascading failures can be especially perilous. Imagine a situation where the network connection between our music shop website and the catalog service goes down. The services themselves appear healthy, but they can’t talk to each other.

Therefore, monitoring the integration points between systems is key. Each service instance should track and expose the health of its downstream dependencies, from the database to other collaborating services. You should also allow this information to be aggregated to give you a rolled-up picture. You’ll want to see the response time of the downstream calls, and also detect if it is erroring.

### Standardization

In my opinion, monitoring is one area where standardization is incredibly important. With services collaborating in lots of different ways to provide capabilities to users using multiple interfaces, you need to view the system in a holistic way.

## Summary

For each service:
* Track inbound response time at a bare minimum. Once you’ve done that, follow with error rates and then start working on application-level metrics.
* Track the health of all downstream responses, at a bare minimum including the response time of downstream calls, and at best tracking error rates. Libraries like Hystrix can help here.
* Standardize on how and where metrics are collected.
* Log into a standard location, in a standard format if possible. Aggregation is a pain if every service uses a different layout!
* Monitor the underlying operating system so you can track down rogue processes and do capacity planning.

For the system:
* Aggregate host-level metrics like CPU together with application-level metrics.
* Ensure your metric storage tool allows for aggregation at a system or service level, and drill down to individual hosts.
* Ensure your metric storage tool allows you to maintain data long enough to understand trends in your system.
* Have a single, queryable tool for aggregating and storing logs.
* Strongly consider standardizing on the use of correlation IDs.
* Understand what requires a call to action, and structure alerting and dashboards accordingly.
* Investigate the possibility of unifying how you aggregate all of your various metrics by seeing if a tool like Suro or Riemann makes sense for you.

# CHAPTER 9 Security

### Common Single Sign-On Implementations

A common approach to authentication and authorization is to use some sort of single sign-on (SSO) solution. **SAML**, which is the reigning implementation in the enterprise space, and **OpenID Connect** both provide capabilities in this area. More or less they use the same core concepts, although the terminology differs slightly.

When a principal tries to access a resource (like a web-based interface), she is directed to authenticate with an ***identity provider***. This may ask her to provide a username and password, or might use something more advanced like two-factor authentication. Once the identity provider is satisfied that the principal has been authenticated, it gives information to the ***service provider***, allowing it to decide whether to grant her access to the resource.

This ***identity provider*** could be an externally hosted system, or something inside your own organization. Google, for example, provides an OpenID Connect identity provider. For enterprises, though, it is common to have your own identity provider, which may be linked to your company’s directory service. A directory service could be something like the **Lightweight Directory Access Protocol (LDAP)** or **Active Directory.**

**SAML is a SOAP-based standard.**

**OpenID Connect is a standard that has emerged as a specific implementation of OAuth 2.0, based on the way Google and others handle SSO.**

### Single Sign-On Gateway

Rather than having each service manage handshaking with your identity provider, you can use a gateway to act as a proxy, sitting between your services and the outside world - [SSO_Gateway](SSO_Gateway.PNG). The idea is that we can centralize the behavior for redirecting the user and perform the handshake in only one place.

However, we still need to solve the problem of how the downstream service receives information about principals, such as their username or what roles they play. If you’re using HTTP, it could populate headers with this information.

Another problem is that if we have decided to offload responsibility for authentication to a gateway, it can be harder to reason about how a microservice behaves when looking at it in isolation. If you go the gateway route, make sure your developers can launch their services behind one without too much work.

## Service-to-Service Authentication and Authorization

Up to this point we’ve been using the term principal to describe anything that can authenticate and be authorized to do things, but our examples have actually been about humans using computers. But what about programs, or other services, authenticating with each other?

### Allow Everything Inside the Perimeter

Our first option could be to just assume that any calls to a service made from inside our perimeter are implicitly trusted.

For most of the organizations I see using this model, I worry that the implicit trust model is not a conscious decision, but more that people are unaware of the risks in the first place.

### HTTP(S) Basic Authentication

HTTP Basic Authentication allows for a client to send a username and password in a standard HTTP header. The problem is that doing this over HTTP is highly problematic, as the username and password are not sent in a secure manner. Thus, HTTP Basic Authentication should normally be used over HTTPS. 

The server needs to manage its own SSL certificates, which can become problematic when it is managing multiple machines. Some organizations take on their own certificate issuing process, which is an additional administrative and operational burden.

**Another downside is that traffic sent via SSL cannot be cached by reverse proxies like Varnish or Squid. This means that if you need to cache traffic, it will have to be done either inside the server or inside the client.**

### Use SAML or OpenID Connect

If you are already using SAML or OpenID Connect as your authentication and authorization scheme, you could just use that for service-to-service interactions too. If you’re using a gateway, you’ll need to route all in-network traffic via the gateway too.

This does mean you’ll need an account for your clients, sometimes referred to as a service account. Many organizations use this approach quite commonly. A word of warning, though: if you are going to create service accounts, try to keep their use narrow. **So consider each microservice having its own set of credentials.**

There are a couple of other downsides:
* Just as with Basic Auth, we need to securely store our credentials: where do the username and password live? 
* The other problem is that some of the technology in this space to do the authentication is fairly tedious to code for. SAML, in particular, makes implementing a client a painful affair. OpenID Connect has a simpler workflow, but as we discussed earlier it isn’t that well supported yet.

### Client Certificates

Here, each client has an X.509 certificate installed that is used to establish a link between client and server. The server can verify the authenticity of the client certificate, providing strong guarantees that the client is valid.

The operational challenges here in certificate management are even more onerous than with just using server-side certificates.

It isn’t just some of the basic issues of **creating and managing a greater number of certificates**; rather, it’s that with all the **complexities around the certificates themselves**, you can expect to spend a lot of time trying to diagnose why a service won’t accept what you believe to be a completely valid client certificate. And then we have to consider the **difficulty of revoking and reissuing certificates** should the worst happen.

### HMAC Over HTTP

Hash-based messaging code (HMAC). 

With HMAC the body request along with a private key is hashed, and the resulting hash is sent along with the request. The server then uses its own copy of the private key and the request body to re-create the hash. If it matches, it allows the request. The added benefit is that this traffic can then more easily be cached.

There are three downsides to this approach:
* First, both the client and server need a shared secret that needs to be communicated somehow.
* Second, this is a pattern, not a standard, and thus there are divergent ways of implementing it. JSON web tokens (JWT) are also worth looking at, as they implement a very similar approach and seem to be gaining traction.
* Finally, understand that this approach ensures only that no third party has manipulated the request and that the private key itself remains private. The rest of the data in the request will still be visible to parties snooping on the network.

### API Keys

API keys allow a service to identify who is making a call, and place limits on what they can do. Often the limits go beyond simply giving access to a resource, and can extend to actions like rate-limiting specific callers to protect quality of service for other people.

Part of their popularity stems from the fact that API keys are focused on ease of use for programs. Compared to handling a SAML handshake, API key–based authentication is much simpler and more straightforward.






































































