# Chapter 1: The First Step - Concurrency Design Principles

### Concurrency versus parallelism

The most accepted definition talks about concurrency as being when you have more than one task in a single processor with a single core. In this case, the operating system's task scheduler quickly switches from one task to another, so it seems that all the tasks run simultaneously. The same definition talks about parallelism as being when you have more than one task running simultaneously on different computers, processors, or cores inside a processor.

### Synchronization
We have two kinds of synchronization:
* __Control synchronization__: When, for example, one task depends on the end of another task, the second task can't start before the first has finished
* __Data access synchronization__: When two or more tasks have access to a shared variable and only one of the tasks can access the variable

### Possible problems in concurrent applications

* Data race
* Deadlock
* Livelock. A livelock occurs when you have two tasks in your system that are always changing their states due to the actions of the other. Consequently, they are in a loop of state changes and unable to continue. For example, you have two tasks - Task 1 and Task 2, and both need two resources - Resource 1 and Resource 2. Suppose that Task 1 has a lock on Resource 1, and Task 2 has a lock on Resource 2. As they are unable to gain access to the resource they need, they free their resources and begin the cycle again.
* Resource starvation
* Priority inversion. Priority inversion occurs when a low priority task holds a resource that is needed by a high priority task, so the low priority task finishes its execution before the high priority task.

## A methodology to design concurrent algorithms

### The starting point - a sequential version of the algorithm

Sequential version of the algorithm will give us two advantages:

* We can use the sequential algorithm to test whether our concurrent algorithm generates correct/same results
* We can measure the throughput of both algorithms to see if the use of concurrency gives us a real improvement in the response time or in the amount of data the algorithm can process in a time.

### Step 1 - analysis

Good candidates for this process are loops, where one step is independent of the other steps, or portions of code are independent of other parts of the code (for example, an algorithm to initialize an application that opens the connections with the database, loads the configuration files, and initializes some objects; all these tasks are independent of each other).

### Step 2 - design

You can take two different approaches:
* __Task decomposition__: You do task decomposition when you split the code into two or more independent tasks that can be executed at once. Maybe some of these tasks have to be executed in a given order or have to wait at the same point. You must use synchronization mechanisms to get this behavior.
* __Data decomposition__: You do data decomposition when you have multiple instances of the same task that work with a subset of the dataset. This dataset will be a shared resource, so if the tasks need to modify the data, you have to protect access to it, implementing a critical section.

### Step 3 - implementation
### Step 4 - testing
### Step 5 - tuning

### Conclusion

Finally, when you implement a concurrent application (from scratch or based on a sequential algorithm), you must take into account the following points:
* __Efficiency__: The parallel algorithm must end in less time than the sequential algorithm. The first goal of parallelizing an algorithm is that its running time is less than the sequential one, or it can process more data in the same time.
* __Simplicity__: When you implement an algorithm (parallel or not), you must keep it as simple as possible. It will be easier to implement, test, debug, and maintain, and it will have less errors.

## Java Concurrency API
### Basic concurrency classes

The basic classes of the Concurrency API are:

* The __Thread__ class: This class represents all the threads that execute a concurrent Java application
* The __Runnable__ interface: This is another way to create concurrent applications in Java
* The __ThreadLocal__ class: This is a class to store variables locally to a thread
* The __ThreadFactory__ interface: This is the base of the Factory design pattern, that you can use to create customized threads 

### Synchronization mechanisms

The Java Concurrency API includes different synchronization mechanisms that allow you to:

* Define a critical section to access a shared resource
* Synchronize different tasks at a common point

The following mechanisms are the most important synchronization mechanisms:

* The __synchronized__ keyword: The synchronized keyword allows you to define a critical section in a block of code or in an entire method.
* The __Lock__ interface: Lock provides a more flexible synchronization operation than the _synchronized_ keyword. There are different kinds of Locks: ReentrantLock, to implement a Lock that can be associated with a condition; ReentrantReadWriteLock that separates the read and write operations; and StampedLock, a new feature of Java 8 that includes three modes for controlling read/write access.
* The __Semaphore__ class: The class that implements the classical semaphore to implement the synchronization. Java supports binary and general semaphores.
* The __CountDownLatch__ class: A class that allows a task to wait for the finalization of multiple operations.
* The __CyclicBarrier__ class: A class that allows the synchronization of multiple threads at a common point.
* The __Phaser__ class: A class that allows you to control the execution of tasks divided into phases. None of the tasks advance to the next phase until all of the tasks have finished the current phase.

### Executors

The executor framework is a mechanism that allows you to separate thread creation and management for the implementation of concurrent tasks. You don't have to worry about the creation and management of threads, only to create tasks and send them to the executor. The main classes involved in this framework are:

* The __Executor__ and __ExecutorService__ interface: This includes the execute() method common to all executors
* __ThreadPoolExecutor__: This is a class that allows you to get an executor with a pool of threads and, optionally, define a maximum number of parallel tasks
* __ScheduledThreadPoolExecutor__: This is a special kind of executor to allow you to execute tasks after a delay or periodically
* __Executors__: This is a class that facilitates the creation of executors
* The __Callable__ interface: This is an alternative to the Runnable interface - a separate task that can return a value
* The __Future__ interface: This is an interface that includes the methods to obtain the value returned by a Callable interface and to control its status

### The fork/join framework

The fork/join framework defines a special kind of executor specialized in the resolution of problems with the divide and conquer technique. It includes a mechanism to optimize the execution of the concurrent tasks that solve these kinds of problems. Fork/Join is specially tailored for fine-grained parallelism, as it has very low overhead in order to place the new tasks into the queue and take queued tasks for execution. The main classes and interfaces involved in this framework are:

* __ForkJoinPool__: This is a class that implements the executor that is going to run the tasks
* __ForkJoinTask__: This is a task that can be executed in the ForkJoinPool class
* __ForkJoinWorkerThread__: This is a thread that is going to execute tasks in the ForkJoinPool class

### Concurrent data structures

The Java Concurrency API includes a lot of data structures that can be used in concurrent applications without risk. We can classify them into two groups:

* __Blocking data structures__: These include methods that block the calling task when, for example, the data structure is empty and you want to get a value.
* __Non-blocking data structures__: If the operation can be made immediately, it won't block the calling tasks. It returns a null value or throws an exception.

These are some of the data structures:

* __ConcurrentLinkedDeque__: This is a non-blocking list
* __ConcurrentLinkedQueue__: This is a non-blocking queue
* __LinkedBlockingDeque__: This is a blocking list
* __LinkedBlockingQueue__: This is a blocking queue
* __PriorityBlockingQueue__: This is a blocking queue that orders its elements based on their priority
* __ConcurrentSkipListMap__: This is a non-blocking navigable map
* __ConcurrentHashMap__: This is a non-blocking hash map
* __AtomicBoolean, AtomicInteger, AtomicLong, and AtomicReference__: These are atomic implementations of the basic Java data types


## Concurrency design patterns

### Signaling

This design pattern explains how to implement the situation where a task has to notify an event to another task. The easiest way to implement this pattern is with a semaphore or a mutex, using the ReentrantLock or Semaphore classes of the Java language or even the wait() and notify() methods included in the Object class.

See the following example:

    public void task1() { 
        section1(); 
        commonObject.notify(); 
    } 
 
    public void task2() { 
        commonObject.wait(); 
        section2(); 
    } 

Under these circumstances, the section2() method will always be executed after the section1() method.

### Rendezvous

This design pattern is a generalization of the Signaling pattern. In this case, the first task waits for an event of the second task and the second task waits for an event of the first task. The solution is similar to that of Signaling, but in this case, you must use two objects instead of one.

See the following example:

    public void task1() { 
        section1_1(); 
        commonObject1.notify(); 
        commonObject2.wait(); 
        section1_2(); 
    } 

    public void task2() { 
        section2_1(); 
        commonObject2.notify(); 
        commonObject1.wait(); 
        section2_2(); 
    } 

Under these circumstances, section2_2() will always be executed after section1_1() and section1_2() after section2_1().

### Mutex

A mutex is a mechanism that you can use to implement a critical section, ensuring the mutual exclusion. That is to say, only one task can execute the portion of code protected by the mutex at once. In Java, you can implement a critical section using the _synchronized_ keyword (that allows you to protect a portion of code or a full method), the _ReentrantLock_ class, or the _Semaphore_ class.

Look at the following example:

    public void task() { 
        preCriticalSection(); 
        try { 
            lockObject.lock() // The critical section begins 
            criticalSection(); 
        } catch (Exception e) { 
 
        } finally { 
            lockObject.unlock(); // The critical section ends 
            postCriticalSection(); 
        }

### Multiplex

The Multiplex design pattern is a generalization of the Mutex. In this case, a determined number of tasks can execute the critical section at once. It is useful, for example, when you have multiple copies of a resource. The easiest way to implement this design pattern in Java is using the Semaphore class initialized to the number of tasks that can execute the critical section at once.

Look at the following example:

    public void task() { 
        preCriticalSection(); 
        semaphoreObject.acquire(); 
        criticalSection(); 
        semaphoreObject.release(); 
        postCriticalSection(); 
    } 


### Barrier

This design pattern explains how to implement the situation where you need to synchronize some tasks at a common point. None of the tasks can continue with their execution until all the tasks have arrived at the synchronization point. Java Concurrency API provides the CyclicBarrier class, which is an implementation of this design pattern.

Look at the following example:

    public void task() {
        preSyncPoint();
        barrierObject.await();
        postSyncPoint();
    }

### Read-write lock

When you protect access to a shared variable with a lock, only one task can access that variable, independently of the operation you are going to perform on it. Sometimes, you will have variables that you modify a few times but you read many times. To solve this problem, we can use the read-write lock design pattern. This pattern defines a special kind of lock with two internal locks: one for read operations and another for write operations. The behavior of this lock is as follows:

* If one task is doing a read operation and another task wants to do another read operation, it can do it
* If one task is doing a read operation and another task wants to do a write operation, it's blocked until all the readers finish
* If one task is doing a write operation and another task wants to do an operation (read or write), it's blocked until the writer finishes

The Java Concurrency API includes the class __ReentrantReadWriteLock__ that implements this design pattern.

### Thread pool

This design pattern tries to remove the overhead introduced by creating a thread per task you want to execute. It's formed by a set of threads and a queue of tasks you want to execute. The set of threads usually has a fixed size. When a thread finishes the execution of a task, it doesn't finish its execution. It looks for another task in the queue. If there is another task, it executes it. If not, the thread waits until a task is inserted in the queue, but it's not destroyed.

The Java Concurrency API includes some classes that implement the __ExecutorService__ interface that internally uses a pool of threads.


### Thread local storage

This design pattern defines how to use global or static variables locally to tasks. When you have a static attribute in a class, all the objects of a class access the same occurrences of the attribute. If you use thread local storage, each thread accesses a different instance of the variable.

The Java Concurrency API includes the __ThreadLocal__ class to implement this design pattern.




























