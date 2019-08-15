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
























