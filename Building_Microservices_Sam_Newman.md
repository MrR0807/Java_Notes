# Chapter 1. Microservices

The question I am often asked is how small is small? A microservice as something that could be rewritten in two weeks. Or 
I nearly always ask the question who has a system that is too big and that you’d like to break down?

## Autonomous

All communication between the services themselves are via network calls, to enforce separation between the services and avoid the perils of tight coupling.
The **golden rule: can you make a change to a service and deploy it by itself without changing anything else?** If the answer is no, then many of the advantages we discuss throughout this book will be hard for you to achieve. **To do decoupling well, you’ll need to model your services right and get the APIs right. I’ll be talking about that a lot.**

## Resilience

If one component of a system fails, but that failure doesn’t cascade, you can isolate the problem and the rest of the system can carry on working.

## Scaling

With a large, monolithic service, we have to scale everything together. One small part of our overall system is constrained in performance, but if that behavior is locked up in a giant monolithic application, we have to handle scaling everything as a piece. With smaller services, we can just scale those services that need scaling.

## Ease of Deployment

A one-line change to a million-line-long monolithic application requires the whole application to be deployed in order to release the change. That could be a large-impact, high-risk deployment.

## Organizational Alignment

We know that smaller teams working on smaller codebases tend to be more productive.

## Composability

One of the key promises of distributed systems and service-oriented architectures is that we open up opportunities for reuse of functionality.

## Optimizing for Replaceability

If you work at a medium-size or bigger organization, chances are you are aware of some big, nasty legacy system sitting in the corner. The one no one wants to touch. The one that is vital to how your company runs, but that happens to be written in some odd Fortran variant and runs only on hardware that reached end of life 25 years ago. Why hasn’t it been replaced? You know why: it’s too big and risky a job.
With our individual services being small in size, the cost to replace them with a better implementation, or even delete them altogether, is much easier to manage.

**You should instead think of microservices as a specific approach for SOA in the same way that XP or Scrum are specific approaches for Agile software development.**

## No Silver Bullet

I should call out that microservices are no free lunch or silver bullet, and make for a bad choice as a golden hammer. They have all the associated complexities of distributed systems, and while we have learned a lot about how to manage distributed systems well it is still hard.

# Chapter 2. The Evolutionary Architect






