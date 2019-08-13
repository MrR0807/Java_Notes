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



























