- Chapter 1: The First Step - Concurrency Design Principles:
  - Concurrency versus parallelism
  - Synchronization
  - Possible problems in concurrent applications
  - A methodology to design concurrent algorithms
    - The starting point - a sequential version of the algorithm
    - Step 1 - analysis
    - Step 2 - design
    - Step 3 - implementation
    - Step 4 - testing
    - Step 5 - tuning
    - Conclusion
  - Java Concurrency API
    - Basic concurrency classes
    - Synchronization mechanisms
    - Executors
    - The fork/join framework
    - Concurrent data structures
  - Concurrency design patterns
    - Signaling
    - Rendezvous
    - Mutex
    - Multiplex
    - Barrier
    - Read-write lock
    - Thread pool
    - Thread local storage
- Chapter 2: Working with Basic Elements - Threads and Runnables
  - Threads in Java
  - Threads in Java - characteristics and states
  - The Thread class and the Runnable interface
  - First example: matrix multiplication
    - Serial version
    - A thread per element
    - A thread per row
    - The number of threads is determined by the processors
- Chapter 3: Managing Lots of Threads - Executors

----------------------------

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

-------------------------------------------

# Chapter 2: Working with Basic Elements - Threads and Runnables

When you implement a concurrent application, no matter the language, you have to create different execution threads that run in parallel in a non-deterministic order unless you use a synchronization element (such as a semaphore).

In Java you can create execution threads in two ways:
* Extending the __Thread__ class
* Implementing the __Runnable__ interface

## Threads in Java

Java implements execution threads using the Thread class. You can create an execution thread in your application using the following mechanisms:
* You can __extend the Thread__ class and override the run() method
* You can __implement the Runnable__ interface and pass an object of that class to the constructor of a Thread object

In both cases, you will have a Thread object, but the __second approach is recommended__ over the first one. Its main advantages are:
* Runnable is an interface: You can implement other interfaces and extend other classes. With the Thread class you can only extend that class.
* Runnable objects can be executed with threads, but also in other Java concurrency objects as executors. This gives you more flexibility to change your concurrent applications.
* You can use the same Runnable object with different threads.

Once you have a __Thread object, you must use the start() method to create a new execution thread and execute the run() method of the Thread__. If you call the run() method directly, you will be calling a normal Java method and no new execution thread will be created.

## Threads in Java - characteristics and states

* All threads in Java have a priority, an integer value that can be between the values Thread.MIN_PRIORITY and Thread.MAX_PRIORITY (Actually, their values are 1 and 10.)
* By default, all threads are created with the priority Thread.NORM_PRIORITY (actually, its value is 5).
* This priority is a hint to the Java Virtual Machine and to the underlying operating system about which threads are preferred, but it's not a contract. There's no guarantee about the order of execution of the threads.

You can create two kinds of threads in Java:
* Daemon threads
* Non-daemon threads

The difference between them is in how they affect the end of a program. A Java program ends its execution when one of the following circumstances occurs:
* The program executes the exit() method of the Runtime class and the user has authorization to execute that method
* All the non-daemon threads of the application have ended its execution, no matter if there are daemon threads running or not

With these characteristics, daemon threads are usually used to execute auxiliary tasks in the applications as garbage collectors or cache managers.

Finally, threads can pass through different states depending on the situation. These are the possible statuses of a thread:
* __NEW__: The Thread has been created but it hasn't started its execution yet
* __RUNNABLE__: The Thread is running in the Java Virtual Machine
* __BLOCKED__: The Thread is waiting for a lock
* __WAITING__: The Thread is waiting for the action of another thread
* __TIME_WAITING__: The Thread is waiting for the action of another thread but has a time limit
* __THREAD__: The Thread has finished its execution

## The Thread class and the Runnable interface

Interesting methods of the Thread class:
* __getId()__: This method returns the identifier of the Thread. It is a positive integer number assigned when it's created. It is unique
during its entire life and it can't be changed.
* __getName()/setName()__: This method allows you to get or set the name of the Thread.
* __getPriority()/setPriority()__
* __isDaemon()/setDaemon()__
* __getState()__
* __join()__: This method suspends the execution of the thread that makes the call until the end of the execution of the thread used to call the method.
* __setUncaughtExceptionHandler()__: This method is used to establish the controller of unchecked exceptions that can occur while you're executing the threads.
* __currentThread()__

## First example: matrix multiplication

    public class MatrixGenerator {

        public static double[][] generate(int rows, int columns) {
            var result = new double[rows][columns];

            Random random = new Random();
            for (int i=0; i< rows; i++) {
                for (int j = 0; j < columns; j++) {
                    result[i][j] = random.nextDouble()*10;
                }
            }
            return result;
        }
    }


### Serial version

    public class SerialVersion {

        public static double[][] multiply(double[][] matrix1, double[][] matrix2) {
            var rows1 = matrix1.length;
            var columns1 = matrix1[0].length;
            var columns2 = matrix2[0].length;

            var result = new double[rows1][columns2];

            for (int i = 0; i < rows1; i++) {
                for (int j = 0; j < columns2; j++) {
                    result[i][j] = 0;
                    for (int k = 0; k < columns1; k++) {
                        result[i][j] += matrix1[i][k] * matrix2[k][j];
                    }
                }
            }
            return result;
        }
    }


    @Test
    public void multiplySerial() {
        final double[][] expected = new double[][] {{14, 32}, {32,77}};
        var matrix1 = new double[][]{{1, 2, 3}, {4, 5, 6}};
        var matrix2 = new double[][]{{1, 4}, {2, 5}, {3, 6}};

        var result = SerialVersion.multiply(matrix1, matrix2);

        assertArrayEquals(expected, result);
    }
    
    @Test
    public void multiplySerial__measure() {
        var matrix1 = MatrixGenerator.generate(200, 200);
        var matrix2 = MatrixGenerator.generate(200, 200);

        var before = Instant.now();
        SerialVersion.multiply(matrix1, matrix2);
        var after = Instant.now();
        System.out.printf("Serial: %d%n", Duration.between(before, after).toMillis());
    }


### A thread per element

    public class IndividualMultiplierTask implements Runnable {

      private final double[][] matrix1;
      private final double[][] matrix2;
      private final double[][] result;
      private final int row;
      private final int column;

      public IndividualMultiplierTask(double[][] matrix1, double[][] matrix2, double[][] result, int row, int column) {
          this.matrix1 = matrix1;
          this.matrix2 = matrix2;
          this.result = result;
          this.row = row;
          this.column = column;
      }

      @Override
      public void run() {
          var columns1 = matrix1[0].length;
          result[row][column] = 0;
          for (int k = 0; k < columns1; k++) {
              result[row][column] += matrix1[row][k] * matrix2[k][column];
          }
      }
    }


    public static double[][] multiply(double[][] matrix1, double[][] matrix2, int threadCount) {
        var rows1 = matrix1.length;
        var columns2 = matrix2[0].length;

        var result = new double[rows1][columns2];

        List<Thread> threads = new ArrayList<>(threadCount);

        for (int row = 0; row < rows1; row++) {
            for (int column2 = 0; column2 < columns2; column2++) {
                IndividualMultiplierTask task = new IndividualMultiplierTask(matrix1, matrix2, result, row, column2);
                Thread t = new Thread(task);
                t.start();
                threads.add(t);

                if (threads.size() % threadCount == 0) {
                    waitForThreads(threads);
                }
            }
        }
        waitForThreads(threads);
        return result;
    }

    private static void waitForThreads(List<Thread> threads) {
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        threads.clear();
    }


    @Test
    public void multiplyParallelIndividual() {
        final double[][] expected = new double[][] {{14, 32}, {32,77}};
        var matrix1 = new double[][]{{1, 2, 3}, {4, 5, 6}};
        var matrix2 = new double[][]{{1, 4}, {2, 5}, {3, 6}};

        var result = ParallelIndividualMultiplier.multiply(matrix1, matrix2, 10);

        assertArrayEquals(expected, result);
    }

    @Test
    public void multiplyParallelIndividual__measure() {
        var matrix1 = MatrixGenerator.generate(2000, 2000);
        var matrix2 = MatrixGenerator.generate(2000, 2000);

        var before = Instant.now();
        ParallelIndividualMultiplier.multiply(matrix1, matrix2, 10);
        var after = Instant.now();
        System.out.printf("Parallel Individual: %d%n", Duration.between(before, after).toMillis());
    }

### A thread per row

    public class RowMultiplierTask implements Runnable {

        private final double[][] matrix1;
        private final double[][] matrix2;
        private final double[][] result;
        private final int row;

        public RowMultiplierTask(double[][] matrix1, double[][] matrix2, double[][] result, int row) {
            this.matrix1 = matrix1;
            this.matrix2 = matrix2;
            this.result = result;
            this.row = row;
        }

        @Override
        public void run() {
            var columns1 = matrix1[0].length;
            var columns2 = matrix2[0].length;

            for (int j = 0; j < columns2; j++) {
                result[row][j] = 0;
                for (int k = 0; k < columns1; k++) {
                    result[row][j] += matrix1[row][k] * matrix2[k][j];
                }
            }
        }
    }
    
    

    public class ParallelIndividualMultiplier {

        public static double[][] multiply(double[][] matrix1, double[][] matrix2, int threadCount) {
            var rows1 = matrix1.length;
            var columns2 = matrix2[0].length;

            var result = new double[rows1][columns2];

            List<Thread> threads = new ArrayList<>(threadCount);

            for (int row = 0; row < rows1; row++) {
                for (int column2 = 0; column2 < columns2; column2++) {
                    IndividualMultiplierTask task = new IndividualMultiplierTask(matrix1, matrix2, result, row, column2);
                    Thread t = new Thread(task);
                    t.start();
                    threads.add(t);

                    if (threads.size() % threadCount == 0) {
                        waitForThreads(threads);
                    }
                }
            }
            waitForThreads(threads);
            return result;
        }

        private static void waitForThreads(List<Thread> threads) {
            for (Thread t : threads) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            threads.clear();
        }
    }

    @Test
    public void multiplyParallelRow() {
        final double[][] expected = new double[][] {{14, 32}, {32,77}};
        var matrix1 = new double[][]{{1, 2, 3}, {4, 5, 6}};
        var matrix2 = new double[][]{{1, 4}, {2, 5}, {3, 6}};

        var result = ParallelRowMultiplier.multiply(matrix1, matrix2, 10);

        assertArrayEquals(expected, result);
    }

    @Test
    public void multiplyParallelRow__measure() {
        var matrix1 = MatrixGenerator.generate(2000, 2000);
        var matrix2 = MatrixGenerator.generate(2000, 2000);

        var before = Instant.now();
        ParallelRowMultiplier.multiply(matrix1, matrix2, 10);
        var after = Instant.now();
        System.out.printf("Parallel Row (millis): %d%n", Duration.between(before, after).toMillis());
        System.out.printf("Parallel Row (seconds): %d%n", Duration.between(before, after).toSeconds());
    }
    
### The number of threads is determined by the processors

Finally, in the last version, we only create as many threads as there are cores or processors available to the JVM. We use the availableProcessors() method of the Runtime class to calculate that number.

    public class GroupMultiplierTask implements Runnable {

        private final double[][] matrix1;
        private final double[][] matrix2;
        private final double[][] result;

        private final int startIndex;
        private final int endIndex;

        public GroupMultiplierTask(double[][] matrix1, double[][] matrix2, double[][] result, int startIndex, int endIndex) {
            this.matrix1 = matrix1;
            this.matrix2 = matrix2;
            this.result = result;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        @Override
        public void run() {
            var columns1 = matrix1[0].length;
            var columns2 = matrix2[0].length;

            for (int i = startIndex; i < endIndex; i++) {
                for (int j = 0; j < columns2; j++) {
                    result[i][j] = 0;
                    for (int k = 0; k < columns1; k++) {
                        result[i][j] += matrix1[i][k] * matrix2[k][j];
                    }
                }
            }
        }
    }



    public class GroupMultiplier {

        public static double[][] multiply(double[][] matrix1, double[][] matrix2) {
            var rows1 = matrix1.length;
            var columns2 = matrix2[0].length;

            var result = new double[rows1][columns2];

            List<Thread> threads = new ArrayList<>();

            var threadCount = Runtime.getRuntime().availableProcessors();
            int startIndex, endIndex, step;
            step = rows1 / threadCount == 0
                    ? rows1
                    : rows1 / threadCount;
            startIndex = 0;
            endIndex = step;

            ThreadCountLoop:
            for (int i = 0; i < threadCount; i++) {
                GroupMultiplierTask task = new GroupMultiplierTask(matrix1, matrix2, result, startIndex, endIndex);
                var t = new Thread(task);
                threads.add(t);
                t.start();

                startIndex = endIndex;
                endIndex = i == (threadCount - 2) //Last thread takes the remaining part of rows
                        ? rows1
                        : (endIndex + step) > rows1 //Avoid index out of bounds with endIndex
                            ? rows1
                            : endIndex + step;

                if (isLast(startIndex, endIndex)) {
                    break ThreadCountLoop;
                }
            }

            waitForThreads(threads);
            return result;
        }

        private static boolean isLast(int startIndex, int lastIndex) {
            return startIndex == lastIndex;
        }

        private static void waitForThreads(List<Thread> threads) {
            for (Thread thread: threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Test
    public void multiplyGroup() {
        final double[][] expected = new double[][] {{14, 32}, {32,77}};
        var matrix1 = new double[][]{{1, 2, 3}, {4, 5, 6}};
        var matrix2 = new double[][]{{1, 4}, {2, 5}, {3, 6}};

        var result = GroupMultiplier.multiply(matrix1, matrix2);

        assertArrayEquals(expected, result);
    }

    @Test
    public void multiplyGroup__measure() {
        var matrix1 = MatrixGenerator.generate(21, 21);
        var matrix2 = MatrixGenerator.generate(21, 21);

        var before = Instant.now();
        GroupMultiplier.multiply(matrix1, matrix2);
        var after = Instant.now();
        System.out.printf("Group (millis): %d%n", Duration.between(before, after).toMillis());
        System.out.printf("Group (seconds): %d%n", Duration.between(before, after).toSeconds());
    }


Naive results:
* Serial (millis): 148919
* Serial (seconds): 148
* Parallel Individual (millis): 1150177
* Parallel Individual (seconds): 1150
* Parallel Row (millis): 73986
* Parallel Row (seconds): 73
* Group (millis): 77757
* Group (seconds): 77


# Chapter 3: Managing Lots of Threads - Executors

With low level (Threads and implementing Runnable) approach, you're responsible for creating and manning the thread objects and implementing the mechanisms of synchronization between the threads. However, it can create some problems, especially with those applications with a lot of concurrent tasks. If you create too many threads, you can degrade the performance of your application or even hang the entire system.

Java version 5 included the Executor framework to solve these problems and provide an efficient solution that is easier to use for programmers than the traditional concurrency mechanisms.


### Basic characteristics of executors

The main characteristics of executors are:
* You don't need to create any Thread objects. If you want to execute a concurrent task, you only create an instance of the task (for example, a class that implements the Runnable interface) and send it to the executor. It will manage the thread that will execute the task.
* Executors reduce the overhead introduced by thread creation reusing the threads. Internally, it manages a pool of threads named worker-threads. If you send a task to the executor and a __worker-thread__ is idle, the executor uses that thread to execute the task.
* It's easy to control the resources used by the executor. You can limit the maximum number of worker-threads of your executor. If you send more tasks than worker-threads, the executor stores them in a queue. When a worker-thread finishes the execution of a task, they take another from the queue.
* __You have to finish the execution of an executor explicitly. You have to indicate to the executor that it has to finish its execution and kill the created threads. If you don't do this, it won't finish its execution and your application won't end__.

## Basic components of the Executor framework

The basic components of the framework are:
* __The Executor interface__: This is the basic interface of the Executor framework. It only defines a method that allows the programmer to send a __Runnable__ object to an executor.
* __The ExecutorService interface__: This interface extends the Executor interface and includes more methods to increase the functionality of the framework, such as the following:
  * Execute tasks that return a result: The run() method provided by the Runnable interface doesn't return a result, but with executors, you can have tasks that return a result 
  * Execute a list of tasks with a single method call
  * Finish the execution of an executor and wait for its termination
* __The ThreadPoolExecutor class__: This class implements the __ExecutorService__ interfaces. In addition, it includes some additional methods to get the status of the executor (the number of worker-threads, number of executed tasks, and so on), methods to establish the parameters of the executor (minimum and maximum number of worker-threads, time that idle threads will wait for new tasks, and so on), and methods that allow programmers to extend and adapt functionality.
* __The Executors class__: This class provides utility methods to create Executor objects and other related classes.

## Other methods of interest

The Executors class provides other methods to create ThreadPoolExecutor objects. These methods are:
* __newCachedThreadPool()__: This method creates a ThreadPoolExecutor object that reuses a worker-thread if it's idle, but it creates a new one if it's necessary. There is no maximum number of worker-threads.
* __newSingleThreadExecutor()__: This method creates a ThreadPoolExecutor object that uses only a single worker-thread. The tasks you send to the executor are stored in a queue until the worker-thread can execute them.


There are two types of concurrent data structures in Java:
* __Blocking data structures__: When you call a method and the library can't do that operation (for example, you try to obtain an element, and the data structure is empty), they block the thread until the operation can be done.
* __Non-blocking data structures__: When you call a method and the library can't do that operation (because the structure is empty or full), the method returns a special value or throws an exception.

## Cancellation of tasks

You can cancel the execution of a task after you send it to an executor. When you send a Runnable object to an executor using the submit() method, it returns an implementation of the Future interface. This class allows you to control the execution of the task. It has the cancel() method that attempts to cancel the execution of the task. 
It receives a Boolean value as a parameter. If it takes the true value and the executor is executing this task, the thread executing the task will be interrupted. These are the situations when the task you want to cancel can't be canceled:
* The task has already been canceled
* The task has finished its execution
* The task is running and you supplied false as a parameter to the cancel() method
* Other reasons not specified in the API documentation

The cancel() method returns a Boolean value to indicate whether the task has been canceled or not.

## Scheduling the execution of tasks

The ThreadPoolExecutor class is a basic implementation of the Executor and ExecutorService interfaces. But the Java concurrency API provides an extension of this class to allow the execution of scheduled tasks. This is the ScheduledThreadPoolExeuctor class, and you can:
* Execute a task after a delay
* Execute a task periodically; this includes the execution of tasks at a fixed rate or with a fixed delay


## Overriding the executor methods
The executor framework is a very flexible mechanism. You can implement your own executor extending one of the existing classes (ThreadPoolExecutor or ScheduledThreadPoolExecutor) to get the desired behavior. These classes include methods that make it easy to change how the executor works. If you override ThreadPoolExecutor, you can override the following methods:

* beforeExecute(): This method is invoked before the execution of concurrent tasks in an executor. It receives the Runnable object that is going to be executed and the Thread object that will execute it. The Runnable object that this method receives is an instance of the FutureTask class and not the Runnable object you sent to the executor using the submit() method.
* afterExecute(): This method is invoked after the execution of a concurrent task in the executor. It receives the Runnable object that has been executed and a Throwable object that stores a possible exception thrown inside the task. As in the beforeExecute() method, the Runnable object is an instance of the FutureTask class.
* newTaskFor(): This method creates the task that is going to execute the Runnable object you sent using the submit() method. It must return an implementation of the RunnableFuture interface. By default, Open JDK 9 and Oracle JDK 9 returns an instance of the FutureTask class, but this might change in future implementations.

If you extend the ScheduledThreadPoolExecutor class, you can override the decorateTask() method. This method is like the newTaskFor() method for scheduled tasks. It allows you to override the tasks executed by the executor.


## Additional information about executors

These are some methods you can override:
* shutdown(): You must explicitly call this method to end the execution of the executor. You can override it to add some code to free additional resources used by your own executor.
* shutdownNow(): The difference between shutdown() and shutdownNow() is that the shutdown() method waits for the finalization of all the tasks that are waiting in the executor.
* submit(), invokeall(), or invokeany(): You call these methods to send concurrent tasks to the executor. You can override them if you need to do some actions before or after a task is inserted in the task queue of the executor. Note that adding a custom action before or after the task is enqueued is different from adding a custom action before or after it's executed, which we did when overriding beforeExecute() and afterExecute() methods.

ScheduledThreadPoolExecutor class has other methods to execute periodic tasks or tasks after a delay:
* schedule(): This method executes a task after the given delay. The task is executed only once.
* scheduleAtFixedRate(): This method executes a periodic task with the given period. The difference with the ScheduleWithFixedDelay() method is that in the last one, the delay between two executions goes from the end of the first one to the start of the second one, and in the first one, the delay between two executions goes between the start of both.

# Getting Data from Tasks - The Callable and Future Interfaces

## Introducing the Callable and Future interfaces

In an executor, you can execute two kinds of tasks:
* Tasks based on the __Runnable__ interface: These tasks implement the run() method that doesn't return any results.
* Tasks based on the __Callable__ interface: These tasks implement the call() interface that returns an object as a result.

**The call() method can throw any checked exception. You can process the exceptions implementing your own executor and overriding the afterExecute() method**.

## The Future interface

When you send a Callable task to an executor, it will return an implementation of the **Future** interface that allows you to control the execution and the status of the task and to get the result. The main characteristics of this interface are:
* You can **cancel the execution** of the task using the cancel() method. This method has a Boolean parameter to specify whether you want to interrupt the task whether it's running or not.
* You can check whether the task has been cancelled (with the isCancelled() method) or has finished (with the isDone() method).
* You can get the value returned by the task using the get() method. There are two variants of this method. The first one doesn't have parameters and returns the value returned by the task if it has finished its execution. **If the task hasn't finished its execution, it suspends the execution thread until the tasks finish.** The second variant admits two parameters: a period of time and TimeUnit of that period. The main difference with the first one is that the thread waits for the period of time passed as a parameter. **If the period ends and the task hasn't finished its execution, the method throws a TimeoutException exception.**


## First example - a best-matching algorithm for words

The main objective of a best-matching algorithm for words is to find the words most similar to a string passed as a parameter.

## The common classes


    public class WordsLoader {

      public static List<String> load(String path) {
          try {
              return Files.readAllLines(Path.of(path));
          } catch (IOException e) {
              e.printStackTrace();
              return Collections.emptyList();
          }
      }
    }
    
    public class LevenshteinDistance {

    public static int calculate(String s1, String s2) {

        var s1L = s1.length() + 1;
        var s2L = s2.length() + 1;
        int[][] distances = new int[s1L][s2L];

        for (int i = 1; i < s1L; i++) {
            distances[i][0] = i;
        }

        for (int i = 1; i < s2L; i++) {
            distances[0][i] = i;
        }

        for (int row = 1; row < s1L; row++) {
            for (int column = 1; column < s2L; column++) {
                if (s1.charAt(row - 1) == s2.charAt(column - 1)) {
                    distances[row][column] = distances[row - 1][column - 1];
                } else {
                    distances[row][column] = Math.min(Math.min(distances[row - 1][column - 1], distances[row - 1][column]),
                            distances[row][column - 1]) + 1;
                }
            }
        }
        return distances[s1.length()][s2.length()];
    }
    }
    
    
    public class BestMatchingData {

    private int minDistance;

    private List<String> words;

    public int getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(int minDistance) {
        this.minDistance = minDistance;
    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }

    @Override
    public String toString() {
        return "BestMatchingData{" +
                "minDistance=" + minDistance +
                ", words=" + words +
                '}';
    }
    }
    
    
    
## Serial version


    public class BestMatchingSerialCalculation {

    public static BestMatchingData bestMatchingData(String word, List<String> dictionary) {

        List<String> closestWords = new ArrayList<>();
        int minDistance = Integer.MAX_VALUE;

        for (var w : dictionary) {
            var distance = LevenshteinDistance.calculate(word, w);
            if (distance < minDistance) {
                minDistance = distance;
                closestWords.clear();
                closestWords.add(w);
            } else if (minDistance == distance) {
                closestWords.add(w);
            }
        }

        BestMatchingData data = new BestMatchingData();
        data.setWords(closestWords);
        data.setMinDistance(minDistance);
        return data;
    }
    }
    
    
## Concurrent version



    public class BestMatchingTask implements Callable<BestMatchingData> {

        private final int startIndex;
        private final int lastIndex;
        private final String word;
        private final List<String> dictionary;

        public BestMatchingTask(int startIndex, int lastIndex, String word, List<String> dictionary) {
            this.startIndex = startIndex;
            this.lastIndex = lastIndex;
            this.word = Objects.requireNonNullElse(word, "empty");
            this.dictionary = Objects.requireNonNullElse(dictionary, Collections.emptyList());
        }

        @Override
        public BestMatchingData call() {
            List<String> closestWords = new ArrayList<>();
            int minDistance = Integer.MAX_VALUE;

            for (var w : dictionary.subList(startIndex, lastIndex)) {
                var distance = LevenshteinDistance.calculate(word, w);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestWords.clear();
                    closestWords.add(w);
                } else if (minDistance == distance) {
                    closestWords.add(w);
                }
            }

            BestMatchingData data = new BestMatchingData();
            data.setWords(closestWords);
            data.setMinDistance(minDistance);
            return data;
        }
      }


    public class BestMatchingConcurrentCalculation {


        public static BestMatchingData bestMatchingData(String word, List<String> dictionary) throws ExecutionException, InterruptedException {

            int startIndex = 0;
            int endIndex = 0;
            int threadCount = Runtime.getRuntime().availableProcessors();
            int step = dictionary.size() / threadCount;

            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            List<Future<BestMatchingData>> results = new ArrayList<>();

            for (int thread = 0; thread < threadCount; thread++) {
                startIndex = endIndex;
                //Last thread's endIndex up to the end
                endIndex = (thread + 1) == threadCount
                        ? dictionary.size()
                        : endIndex + step;

                var task = new BestMatchingTask(startIndex, endIndex, word, dictionary);
                Future<BestMatchingData> future = executor.submit(task);
                results.add(future);
            }

            executor.shutdown();
            List<String> words = new ArrayList<>();
            int minDistance = Integer.MAX_VALUE;

            for (var f : results) {
                BestMatchingData data = f.get();
                if (data.getMinDistance() < minDistance) {
                    words.clear();
                    minDistance = data.getMinDistance();
                    words.addAll(data.getWords());
                } else if (data.getMinDistance() == minDistance) {
                    words.addAll(data.getWords());
                }
            }

            BestMatchingData result = new BestMatchingData();
            result.setMinDistance(minDistance);
            result.setWords(words);
            return result;
        }
     }



## Calculations


    @Test
    public void LevenshteinDistance__calculate() {
        var result = LevenshteinDistance.calculate("Good", "Evening");

        assertThat(result).isEqualTo(7);
    }

    @Test
    public void LevenshteinDistance__calculate2() {
        var result = LevenshteinDistance.calculate("belekokisaofajgi", "fkajfjfjafuufufufufuauuasdlkljfajdfilarjailnfma");

        assertThat(result).isEqualTo(41);
    }

    @Test
    public void serial__calculation() {
        List<String> dictionary = WordsLoader.load("C:\\Users\\BC6250\\IdeaProjects\\test\\src\\main\\resources\\words.data");

        var now = Instant.now();
        BestMatchingData bestMatchingData = BestMatchingSerialCalculation.bestMatchingData("stitter", dictionary);
        var later = Instant.now();

        System.out.println("Dictionary size: " + dictionary.size());
        System.out.println(bestMatchingData);
        System.out.printf("Serial time (millis): %d%n", Duration.between(now, later).toMillis());
        System.out.printf("Serial time (sec): %d%n", Duration.between(now, later).toSeconds());
    }

    @Test
    public void concurrent__calculation() throws ExecutionException, InterruptedException {
        List<String> dictionary = WordsLoader.load("C:\\Users\\BC6250\\IdeaProjects\\test\\src\\main\\resources\\words.data");

        var now = Instant.now();
        BestMatchingData bestMatchingData = BestMatchingConcurrentCalculation.bestMatchingData("stitter", dictionary);
        var later = Instant.now();

        System.out.println("Dictionary size: " + dictionary.size());
        System.out.println(bestMatchingData);
        System.out.printf("Serial time (millis): %d%n", Duration.between(now, later).toMillis());
        System.out.printf("Serial time (sec): %d%n", Duration.between(now, later).toSeconds());
    }


## The second example - creating an inverted index for a collection of documents

## Common classes


    public class Document {

        private String fileName;
        private Map<String, Integer> wordFreq = new HashMap<>();

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public Map<String, Integer> getWordFreq() {
            return wordFreq;
        }

        public void setWordFreq(Map<String, Integer> wordFreq) {
            this.wordFreq = wordFreq;
        }

        @Override
        public String toString() {
            return "Document{" +
                    "fileName='" + fileName + '\'' +
                    ", wordFreq=" + wordFreq +
                    '}';
        }
    }
    

    public class DocumentParse {

        private final static Pattern PATTERN = Pattern.compile("\\P{IsAlphabetic}+");

        public static Map<String, Integer> parse(String path) {
            try {
                return Files.lines(Path.of(path))
                        .flatMap(PATTERN::splitAsStream)
                        .filter(not(String::isBlank))
                        .map(word -> Normalizer.normalize(word, Normalizer.Form.NFKD))
                        .map(String::toLowerCase)
                        .collect(Collectors.toMap(Function.identity(), word -> 1, Integer::sum));
            } catch (IOException e) {
                e.printStackTrace();
                return Collections.emptyMap();
            }
        }
    }


## Serial version


    public class SerialIndexing {

        public static Map<String, List<String>> createIndex(Path folderPath) {
            Objects.requireNonNull(folderPath);

            if (not(Files.isDirectory(folderPath))) {
                throw new IllegalArgumentException("Invalid directory path");
            }

            File[] files = folderPath.toFile().listFiles();
            Map<String, List<String>> invertedIndex = new HashMap<>();

            for (File f : files) {
                if (f.getName().strip().endsWith(".txt")) {
                    Map<String, Integer> vocabulary = DocumentParse.parse(f.getAbsolutePath());
                    updateInvertedIndex(vocabulary, invertedIndex, f.getName());
                }
            }

            return invertedIndex;
        }

        private static void updateInvertedIndex(Map<String, Integer> vocabulary, Map<String, List<String>> invertedIndex, String fileName) {
            for (String word : vocabulary.keySet())
                if (word.length() >= 3) {
                    invertedIndex
                            .computeIfAbsent(word, k -> new ArrayList<>())
                            .add(fileName);
                }
        }
    }


## Concurrent version - a task per document

A **CompletionService** object is a mechanism that has an executor and allows you to decouple the production of tasks and the consumption of the results of those tasks. You can send tasks to the executor using the submit() method and get the results of the tasks when they finish using the poll() or take() methods.


**AbstractExecutorService** interface:

* **invokeAll** (Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit): This method returns a list of Future objects associated with the list of Callable tasks passed as parameters when all the tasks have finished their execution or the timeout specified by the second and third parameters expires.

* **invokeAny** (Collection<? Extends Callable<T>> tasks, long timeout, TimeUnit unit): This method returns the result of the first task of the list of Callable tasks passed as a parameter that finishes their execution without throwing an exception if they finish before the timeout specified by the second and third parameters expires. If the timeout expires, the method throws a TimeoutException exception.

Let's discuss the following methods about the **CompletionService** interface:

* The **poll()** method: We have used a version of this method with two parameters, but there is also a version without parameters. From the internal data structures, this version retrieves and removes the Future object of the next task that has finished since the last call to the poll() or take() methods. If no tasks have finished, its execution returns a null value.
* The **take()** method: This method is similar to the previous one, but if no tasks have finished, it sleeps the thread until one task finishes its execution.

# Optimizing Divide and Conquer Solutions - The Fork/Join Framework

**Fork/join framework** uses a pool of threads that executes the tasks you send to the executor, reusing them for multiple tasks.

The divide and conquer algorithm is a very popular design technique. To solve a problem using this technique, you divide it into smaller problems. You repeat the process in a recursive way until the problems you have to solve are small enough to be solved directly. You have to be very careful selecting the base case that is resolved directly. **A bad choice of the size of that problem can give you poor performance.**

This framework is based on the **ForkJoinPool class**, which is a special kind of executor, two operations, the **fork()** and **join()** methods (and their different variants), and an internal algorithm named the **work-stealing** algorithm.

## Basic characteristics of the fork/join framework

With this framework, you will implement tasks whose main method will be something like this:

    if (problem.size() > DEFAULT_SIZE) {
      divideTasks();
      executeTask();
      taskResults=joinTasksResult();
      return taskResults;
    } else {
      taskResults=solveBasicProblem();
      return taskResults;
    }

* The **fork()** method: This method allows you to send a child task to the fork/join executor
* The **join()** method: This method allows you to wait for the finalization of a child task and returns its result

Critical feature: **the work-stealing algorithm**, which determines which tasks are to be executed. When a task is waiting for the finalization of a child task using the join() method, the thread that is executing that task takes another task from the pool of tasks that are waiting and starts its execution. In this way, the threads of the fork/join executor are always executing a task by improving the performance of the application.

**Default fork/join executor will automatically use the number of threads determined by the available processors of your computer.**

## Limitations of the fork/join framework

* The basic problems that you're not going to subdivide have to be not very large, but also not very small. According to the Java API documentation, it should have between 100 and 10,000 basic computational steps.
* You should not use blocking I/O operations, such as reading user input or data from a network socket that is waiting until the data is available. Such operations will cause your CPU cores to idle, thereby reducing the level of parallelism, so you will not achieve full performance.
* You can't throw checked exceptions inside a task. You have to include the code to handle them (for example, wrapping into unchecked RuntimeException).

## Components of the fork/join framework

* **The ForkJoinPool class**: This class implements the Executor and ExecutorService interfaces, and it is the Executor interface you're going to use to execute your fork/join tasks.
* **The ForkJoinTask class**: This is the base abstract class of all of the fork/join tasks. It's an abstract class, and it provides the fork() and join() methods and some variants of them. It also implements the Future interface and provides methods to know whether the task finished in a normal way, whether it was cancelled, or if it threw an unchecked exception.
* **The RecursiveTask class**: This class extends the ForkJoinTask class. It's also an abstract class, and it should be your starting point to implement fork/join tasks that **return results**.
* **The RecursiveAction class**: This class extends the ForkJoinTask class. It's also an abstract class, and it should be your starting point to implement fork/join tasks that **don't return results**.
* **The CountedCompleter class**: This class extends the ForkJoinTask class. It should be your starting point to implement tasks that **trigger other tasks when they're completed**.

# Processing Massive Datasets with Parallel Streams - The Map and Reduce Model

## MapReduce versus MapCollect

**MapReduce** is a programming model to process very large datasets in distributed environments with a lot of machines working in a cluster. It has two steps, generally implemented by two methods:

* Map: This filters and transforms the data
* Reduce: This applies a summary operation in the data

Hadoop project is an open source implementation of this model.

The **reduce() method** has a limitation. As we mentioned before, it must return a single value. You shouldn't use the reduce() method to generate a collection or a complex object. The first problem is performance. As the documentation of the stream API specifies, the
**accumulator function returns a new value every time it processes an element. If your accumulator function works with collections, it processes an element and creates a new collection every time, which is very inefficient.** Another problem is that, if you work with **parallel streams, all the threads will share the identity value**.


If you want to make a reduction that generates a collection or a complex object, you have the following two options:
* Apply a mutable reduction with the collect() method.
* Create the collection and use the forEach() method to fill the collection with the required values.


      public static void basicSearch(String query[]) throws IOException {
        Path path = Paths.get("index", "invertedIndex.txt");
        HashSet<String> set = new HashSet<>(Arrays.asList(query));
        QueryResult results = new QueryResult(new ConcurrentHashMap<>());
        try (Stream<String> invertedIndex = Files.lines(path)) {
          invertedIndex.parallel()
                      .filter(line -> set
                      .contains(Utils.getWord(line)))
                      .flatMap(ConcurrentSearch::basicMapper)
                      .forEach(results::append);
    
          results.getAsList()
                      .stream()
                      .sorted()
                      .limit(100)
                      .forEach(System.out::println);
        }
      }

# Processing Massive Datasets with Parallel Streams - The Map and Collect Model

## The collect() method

There are two different versions of the collect() method. The first version accepts the following three functional parameters:
* Supplier: This is a function that creates an object of the intermediate data type. **If you use a sequential stream, this method will be called once. If you use a parallel stream, this method may be called many times and must produce a fresh object every time.**
* Accumulator: This function is called to process an input element and store it in the intermediate data structure.
* Combiner: This function is called to merge two intermediate data structures into one. **This function will be only called with parallel streams.**

# Parallel data processing and performance


Note
that you might think that you could achieve finer-grained control over which operations you
want to perform in parallel and which one sequentially while traversing the stream by
combining these two methods. For example, you could do something like the following:
stream.parallel()
.filter(...)
.sequential()
.map(...)
.parallel()
.reduce();
But the last call to parallel or sequential wins and affects the pipeline globally. In this example,
the pipeline will be executed in parallel because that’s the last call in the pipeline.

# Asynchronous Stream Processing - Reactive Streams

The biggest problem with these kinds of systems is resource consumption. **A fast producer can overload a slower consumer.** The queue of data between those components can increase its size in excess and affects the behavior of the whole system. The back pressure mechanism ensures that the queue which mediates between the producer and a consumer has a limited number of elements.

Reactive streams define three main elements:
* A publisher of information: **Flow.Publisher interface**
* One or more subscribers of that information: **Flow.Subscriber interface**
* A subscription between the publisher and a consumer: **Flow.Subscription interface**

Java 9 has included three interfaces, the Flow.Publisher, the Flow.Subscriber, and the Flow.Subscription.

## The Flow.Publisher interface

* subscribe(): This method receives as a parameter an implementation of the Flow.Subscriber interface and adds that subscriber to its internal list of subscribers. This method doesn't return any results.

## The Flow.Subscriber interface

* onSubscribe(): This method is invoked by the publisher to complete the subscription of a subscriber. It sends to the subscriber the Flow.Subscription object that manages the communication between the publisher and the subscriber.
* onNext(): This method is invoked by the publisher when it wants to send a new item to the subscriber. In this method, the subscriber has to process that item. It doesn't return any results.
* onError(): This method is invoked by the publisher when an unrecoverable error has occurred and no other methods of the subscriber will be called. It receives as a parameter a Throwable object with the error that has occurred.
* onComplete(): This method is invoked by the publisher when it's not going to send any more items. It doesn't receive parameters and it doesn't return a result.

## The Flow.Subscription interface

* cancel(): This method is invoked by the subscriber to tell the publisher it doesn't want any more items.
* request(): This method is invoked by the subscriber to tell the publisher it wants more items. It receives the number of items the subscriber wants as a parameter.

## The SubmissionPublisher class

This class implements the Flow.Publisher interface.

* subscribe(): This method is provided by the Flow.Publisher interface. It's used to subscribe a Flow.Subscriber object to this publisher
* offer(): This method publishes an item to each subscriber by asynchronously invoking its onNext() method
* submit(): This method publishes an item to each subscriber by asynchronously invoking its onNext() method, blocking uninterruptedly while resources for any subscriber are unavailable
* close(): This method calls the onComplete() method of all the subscribers of this publisher





























