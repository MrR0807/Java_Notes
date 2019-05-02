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







