This does not contain all design patterns. Only the ones I'm interested.

# Creational patterns

## Singleton

Implementation:
```
public final class Singleton {

    private static final ReentrantLock lock = new ReentrantLock();
    private static Singleton singleton;

    private Singleton() {
    }

    public static Singleton get() {
        lock.lock();
        if (singleton == null) {
            System.out.println(Thread.currentThread());
            singleton = new Singleton();
        }
        lock.unlock();
        return singleton;
    }
}
```

Favour ``Lock`` instead of using ``synchronized`` due to virtual threads being pinned down. Also, favour [try/finally](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/locks/Lock.html), to make sure that lock is released:
```
public static Singleton get() {
        lock.lock();
        try {
            if (singleton == null) {
                System.out.println(Thread.currentThread());
                singleton = new Singleton();
            }
            return singleton;
        } finally {
            lock.unlock();
        }
    }
```

Testing:
```
public class Test {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var set = new HashSet<Future<Singleton>>();
        var executorService = Executors.newFixedThreadPool(4);
        for (var i = 0; i < 1000; i++) {
            var submit = executorService.submit(Singleton::get);
            set.add(submit);
        }
        executorService.shutdown();

        var set2 = new HashSet<Singleton>();
        for (var future : set) {
            var singleton = future.get();
            set2.add(singleton);
        }
        System.out.println(set2.size());
        set2.forEach(System.out::println);
    }
}
```

If Singleton class does not defend against race condition, then instead of:
```
Thread[pool-1-thread-1,5,main]
1
designpatterns.Singleton@1e6d1014
```

You'll find:
```
Thread[pool-1-thread-3,5,main]
Thread[pool-1-thread-4,5,main]
Thread[pool-1-thread-2,5,main]
Thread[pool-1-thread-1,5,main]
4
designpatterns.Singleton@1f554b06
designpatterns.Singleton@614ddd49
designpatterns.Singleton@76707e36
designpatterns.Singleton@1e6d1014
```

### Eager Initialization

```
public final class Singleton {

    private static final Singleton singleton = new Singleton();

    private Singleton() {
        System.out.println("Class initialized");
    }

    public static Singleton get() {
        return singleton;
    }
}
```

However, it is initialized only when code is actually used somewhere, because JVM classloader is starting from ``main`` and following the path/initializing everything which is used.

### Bill Pugh's Solution

```
public final class Singleton {

    private Singleton() {
        System.out.println("Class initialized");
    }

    public static Singleton get() {
        return SingletonHelper.singlet;
    }
    
    private static final class SingletonHelper {
        private static final Singleton singlet = new Singleton();
    }
}
```

### Enum variation

```
public enum Singleton {
    INSTANCE;

    Singleton() {
        System.out.println("Class initialized");
    }

    public static Singleton get() {
        return INSTANCE;
    }
}
```

## Builder

Static factories and constructors share a limitation: they do not scale well to large numbers of optional parameters. There are three options:
* Telescoping constructor
* JavaBean convetions
* Builder pattern

```
public class NutritionFacts {
    
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    public NutritionFacts(Builder builder) {
        this.servingSize = builder.servingSize;
        this.servings = builder.servings;
        this.calories = builder.calories;
        this.fat = builder.fat;
        this.sodium = builder.sodium;
        this.carbohydrate = builder.carbohydrate;
    }

    public static class Builder {
        // Required parameters
        private final int servingSize;
        private final int servings;

        // Optional parameters - initialized to default values
        private int calories = 0;
        private int fat = 0;
        private int sodium = 0;
        private int carbohydrate = 0;

        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings = servings;
        }

        public Builder calories(int val) {
            calories = val;
            return this;
        }

        public Builder fat(int val) {
            fat = val;
            return this;
        }

        public Builder sodium(int val) {
            sodium = val;
            return this;
        }

        public Builder carbohydrate(int val) {
            carbohydrate = val;
            return this;
        }

        public NutritionFacts build() {
            return new NutritionFacts(this);
        }
    }
}
```

There are covariant builders (Effective Java), but after using them in real world, they are more confusing than useful.

## Factory method

Note that a static factory method is not the same as the Factory Method pattern from Design Patterns. A class can provide its clients with static factory methods instead of, or in addition to, public constructors. Providing a static factory method instead of a public constructor has both advantages and disadvantages:

### One advantage of static factory methods is that, unlike constructors, they have names

Programmers have been known to get around this restriction by providing two constructors whose parameter lists differ only in the order of their parameter types. This is a  eally bad idea. The user of such an API will never be able to remember which constructor is which and will end up calling the wrong one by mistake.
**In cases where a class seems to require multiple constructors with the same signature, replace the constructors with static factory methods and carefully chosen names to highlight their differences.**

### A second advantage of static factory methods is that, unlike constructors, they are not required to create a new object each time they’re invoked.

The Boolean.valueOf(boolean) method illustrates this technique: it never creates an object. This technique is similar to the Flyweight pattern.

### A third advantage of static factory methods is that, unlike constructors, they can return an object of any subtype of their return type.

One application of this flexibility is that an API can return objects without making their classes public. Hiding implementation classes in this fashion leads to a very compact API.

### A fourth advantage of static factories is that the class of the returned object can vary from call to call as a function of the input parameters.

The EnumSet class (Item 36) has no public constructors, only static factories. In the OpenJDK implementation, they return an instance of one of two subclasses, depending on the size of the underlying enum type: if it has sixty-four or fewer elements, as most enum types do, the static factories return a RegularEnumSet instance, which is backed by a single long ; if the enum type has sixty-five or more elements, the factories return a JumboEnumSet instance, backed by a long array.

### A fifth advantage of static factories is that the class of the returned object need not exist when the class containing the method is written.

Such flexible static factory methods form the basis of service provider frameworks, like the Java Database Connectivity API (JDBC). A service provider framework is a system in which providers implement a service, and the system makes the implementations available to clients, decoupling the clients from the implementations.

### The main limitation of providing only static factory methods is that classes without public or protected constructors cannot be subclassed.

Arguably this can be a blessing in disguise because it encourages programmers to use composition instead of inheritance (Item 18), and is required for immutable types (Item 17).

### A second shortcoming of static factory methods is that they are hard for programmers to find.

Here are some common names for static factory methods. This list is far from exhaustive:
* **from** A type-conversion method that takes a single parameter and returns a corresponding instance of this type, for example:
```
Date d = Date.from(instant);
```
* **of** An aggregation method that takes multiple parameters and returns an instance of this type that incorporates them, for example:
```
Set<Rank> faceCards = EnumSet.of(JACK, QUEEN, KING);
```
* **valueOf** A more verbose alternative to from and of, for example:
```
BigInteger prime = BigInteger.valueOf(Integer.MAX_VALUE);
```
* **instance or getInstance** Returns an instance that is described by its parameters (if any) but cannot be said to have the same value, for example:
```
StackWalker luke = StackWalker.getInstance(options);
```
* **create or newInstance** Like instance or getInstance , except that the method guarantees that each call returns a new instance, for example:
```
Object newArray = Array.newInstance(classObject, arrayLen);
```
* **get Type** Like getInstance , but used if the factory method is in a different class. Type is the type of object returned by the factory method, for example:
```
FileStore fs = Files.getFileStore(path);
```
* **new Type** Like newInstance, but used if the factory method is in a different class. Type is the type of object returned by the factory method, for example:
```
BufferedReader br = Files.newBufferedReader(path);
```
* **type** A concise alternative to get Type and new Type, for example:
```
List<Complaint> litany = Collections.list(legacyLitany);
```



## Dependency Injection

## Lazy initialization

## Object pool

# Structural patterns

## Adapter

## Bridge

## Composite

## Decorator

## Facade

## Flyweight

## Proxy

# Behavioral patterns

## Chain Of Responsibility

## Command Pattern

## Iterator

## Mediator

## Memento

## Observer or Publish/Subscribe

## State

## Strategy

## Template

## Visitor












