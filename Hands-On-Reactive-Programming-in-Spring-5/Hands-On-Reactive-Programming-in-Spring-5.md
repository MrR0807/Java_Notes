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






















