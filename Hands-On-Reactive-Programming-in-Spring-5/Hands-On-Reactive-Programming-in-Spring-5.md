## Why Reactive Spring?

### Reactivity on the service level

The most popular traditional technique for writing code in Java is **imperative programming.**

Let's consider the next diagram:

![pictures/imperative-programming.png](imperative-programming.png)

OrdersService callsShoppingCardServicewhile processing the user request. Suppose that under the hood ShoppingCardService executes a long-running I/O operation, for example, an HTTP request or database query. To understand the disadvantages of imperative programming let's consider the following example of the most common implementation of the aforementioned interaction between components:

```
interface ShoppingCardService {                                    // (1)
   Output calculate(Input value);                                  //
}                                                                  //

class OrdersService {                                              // (2)
   private final ShoppingCardService scService;                    //
                                                                   //
   void process() {                                                //
      Input input = ...;                                           //
      Output output = scService.calculate(input);                  // (2.1)
      ...                                                          // (2.2)
   }                                                               //
}                                                                  //

```

As we can understand from the preceding code, in Java world, the execution of scService.calculate(input) blocks the Thread on which the processing of the OrdersService logic takes place. Thus, to run a separate independent processing in OrderService we have to allocate an additional Thread.As we will see in this chapter, the allocation of an additional Thread might be wasteful.

Nonetheless, in Java, that problem may be solved by applying a callback technique for the purpose of  cross-component communication:

```
interface ShoppingCardService {                                    // (1)
   void calculate(Input value, Consumer<Output> c);                //
}                                                                  //

class OrdersService {                                              // (2)
   private final ShoppingCardService scService;                    // 
                                                                   //                                                
   void process() {                                                //
      Input input = ...;                                           //
      scService.calculate(input, output -> {                       // (2.1)
...                                                       // (2.2)
      });                                                          //
   }                                                               //
}                                                                  //
```

Now,OrdersService passes the function-callback to react at the end of the operation. This embraces the fact thatOrdersService is now decoupled fromShoppingCardService and the first one may be notified via the functional callback where the implementation of theShoppingCardService#calculate  method, which calls the given function, may either be synchronous or asynchronous:

```
class SyncShoppingCardService implements ShoppingCardService {     // (1)
   public void calculate(Input value, Consumer<Output> c) {        //
      Output result = new Output();                                //
      c.accept(result);                                            // (1.1)
   }                                                               //
}                                                                  //

class AsyncShoppingCardService implements ShoppingCardService{    // (2)
   public void calculate(Input value, Consumer<Output> c) {        //
      new Thread(() -> {                                           // (2.1)
         Output result = template.getForObject(...);               // (2.2) 
         ...                                                       //
         c.accept(result);                                        // (2.3)
      }).start();                                                  // (2.4)
   }                                                               //
}                                                                  //
```
This point is the SyncShoppingCardService class declaration. This implementation assumes the absence of blocking operations. Since we do not have an I/O execution, the result may be returned immediately by passing it to the callback function (1.1).
This point in the preceding code is the AsyncShoppingCardService class declaration. In the case, when we have blocking I/O as depicted in point (2.2), we may wrap it in the separate Thread (2.1)(2.4). After retrieving the result,  it will be processed and passed to the callback function.

The advantage of that technique is that components are decoupled in time by the callback function. This means that after calling the scService.calculate method, we will be able to proceed with other operations immediately without waiting for the response in the blocking fashion from ShoppingCardService.

Fortunately, the callback technique is not the only option. Another one is  java.util.concurrent.Future, which, to some degree, hides the executionalbehavior and decouples components as well:

```
interface ShoppingCardService {                                    // (1)
   Future<Output> calculate(Input value);                          // 
}                                                                  //

class OrdersService {                                              // (2)
   private final ShoppingCardService scService;                    //
                                                                   //
   void process() {                                                //
      Input input = ...;                                           //
      Future<Output> future = scService.calculate(input);          // (2.1)
      ...                                                          //
      Output output = future.get();                                // (2.2)
      ...                                                          //
   }                                                               //
}                                                                  //
```

As we may notice from the previous code, with the Future class, we achieve deferred retrieval of the result. With the support of the Future class, we avoid callback hell and hide multi-threading complexity behind a specific Future implementation. Anyway, to get the result we need, we must potentially block the current Thread and synchronize with the external execution that noticeably decreases scalability.

As an improvement, Java 8 offersCompletionStage andCompletableFuture as a direct implementation for CompletionStage. In turn, those classes provide promise-like APIs and make it possible to build code such as the following:

```
interface ShoppingCardService {                                    // (1)
   CompletionStage<Output> calculate(Input value);                 //
}                                                                  //

class OrdersService {                                              // (2)
   private final ComponentB componentB;                            //
   void process() {                                                //
      Input input = ...;                                           //
      componentB.calculate(input)                                  // (2.1)
                .thenApply(out1 -> { ... })                        // (2.2)
                .thenCombine(out2 -> { ... })                      //       
                .thenAccept(out3 -> { ... })                       //
   }                                                               //
}                                                                  //
```

The overall behavior of the CompletionStage is similar to Future, but CompletionStage provides a fluent API which makes it possible to write methods such as thenAccept andthenCombine. These define transformational operations on the result and thenAccept, which defines the final consumers, to handle the transformed result.

The fact that CPU time will be shared between several threads introduces the notion of **context switching. This means that to resume a thread later, it is required to save and load registers, memory maps, and other related elements which in general are computationally-intensive operations. Consequently, its application with a high number of active threads, and few CPUs, will be inefficient.**

In turn, a typical Java thread has its overhead in memory consumption. A typical stack size for a thread on a 64-bit Java VM is 1,024 KB. **On the other hand, by switching to traditional thread pools with a limited size and a pre-configured queue for requests, the client waits too long for a response, which is less reliable, increases the average response timeout, and finally may cause unresponsiveness of the application.**

# Chapter 2. Reactive Programming in Spring - Basic Concepts

## Early reactive solutions in Spring

We have previously mentioned that there are a lot of patterns and programming techniques that are capable of becoming building blocks for the reactive system. For example, callbacks and CompletableFuture are commonly used to implement the message-driven architecture.

### Observer pattern

At first glance, it may appear that the Observer pattern is not related to reactive programming. However it defines the foundations of reactive programming.

The Observer pattern involves a subject that holds a list of its dependants, called Observers. The subject notifies its observers of any state changes, usually by calling one of their methods. 

![Observer-pattern-UML-class-diagram](pictures/Observer-pattern-UML-class-diagram.png)

As the preceding diagram shows, a typical Observer pattern consists of two interfaces, Subject and Observer. Here, an Observer is registered in Subject and listens for notifications from it. A Subject may generate events on its own or may be called by other components. Let's define a Subject interface in Java:

```
public interface Subject<T> {
   void registerObserver(Observer<T> observer);
   void unregisterObserver(Observer<T> observer);
   void notifyObservers(T event);
}
```

In turn, theObserverinterface may look like the following:

```
public interface Observer<T> {
   void observe(T event);
}
```

A third component may be responsible for finding all of the instances of the Subject and all registration procedures. For example, such a role may come intoplay with the Dependency Injection container. This scans the classpath for eachObserver with the@EventListenerannotation and the correct signature. After that, it registers the found components to theSubject.

Now, let's implement two very simple observers that simply receive String messages and print them to the output stream:

```
public class ConcreteObserverA implements Observer<String> {
   @Override
   public void observe(String event) {
      System.out.println("Observer A: " + event);
   }
}
public class ConcreteObserverB implements Observer<String> {
   @Override
   public void observe(String event) {
      System.out.println("Observer B: " + event);
   }
}
```

We also need to write an implementation of the Subject<String>, which produces String events, as shown in the following code:
   
```
public class ConcreteSubject implements Subject<String> {
   private final Set<Observer<String>> observers =                 // (1)
           new CopyOnWriteArraySet<>();

   public void registerObserver(Observer<String> observer) {
      observers.add(observer);
   }

   public void unregisterObserver(Observer<String> observer) {
      observers.remove(observer);
   }

   public void notifyObservers(String event) {                     // (2)
      observers.forEach(observer -> observer.observe(event));      // (2.1)
   }
}
```

As we can see from the preceding example, the implementation of the Subject holds the Set of observers (1) that are interested in receiving notifications. In turn, a modification (subscription or cancellation of the subscription) of the mentioned Set<Observer> is possible with the support of the registerObserver and unregisterObserver methods. To broadcast events, the Subject has a notifyObservers method (2) that iterates over the list of observers and invokes the observe() method with the actual event (2.1) for each Observer. To be secure in the multithreaded scenario, we use CopyOnWriteArraySet, a thread-safeSetimplementation that creates a new copy of its elements each timethe update operation happens.
   
Do keep in mind that when we have a lot of observers that handle events with some noticeable latency—as introduced by downstream processing—we may parallel message propagation using additional threads or Thread pool. This approach may lead to the next implementation of the notifyObservers method:

```
private final ExecutorService executorService = 
   Executors.newCachedThreadPool();

public void notifyObservers(String event) {
   observers.forEach(observer ->
           executorService.submit(() -> observer.observe(event)));
}
```

However, with such improvements, we are stepping on the slippery road of homegrown solutions that are usually not the most efficient, and that most likely hide bugs. 

**To prevent excessive resource usage, we may restrict the thread pool size and violate the *liveness* property of the application.** Situations such as this arise when all available threads attempt to push some events to the same sluggish Observer.

### Publish-Subscribe pattern with @EventListener

Spring now provides an ``@EventListener`` annotation for event handling and the ``ApplicationEventPublisher`` class for event publishing.

Here we need to clarify that the ``@EventListener`` and the ``ApplicationEventPublisher`` implement the Publish-Subscribe pattern, which may be seen as a variation of the Observer pattern.


































