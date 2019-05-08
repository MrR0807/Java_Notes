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




