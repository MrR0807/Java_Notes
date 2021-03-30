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

## Object pool

When objects are expensive to create and they are needed only for short periods of time. The Object Pool provides a cache for instantiated objects tracking which ones are in use and which are available.

A very naive implementation:
```
public class ObjectPoolDemo {

    public static void main(String[] args) {
        var objectPool = new ObjectPool();
        var expensiveObject1 = objectPool.get();
        objectPool.returnObject(expensiveObject1);
        var expensiveObject2 = objectPool.get();
        objectPool.returnObject(expensiveObject2);
        var expensiveObject3 = objectPool.get();
        objectPool.returnObject(expensiveObject3);
        var expensiveObject4 = objectPool.get();
    }

    private static class ObjectPool {

        private final Set<ExpensiveObject> available = new HashSet<>();
        private final Set<ExpensiveObject> inUse = new HashSet<>();

        private final ReentrantLock getLock = new ReentrantLock();
        private final ReentrantLock returnLock = new ReentrantLock();

        public ObjectPool() {
            int limit = 3;
            for (var i = 0; i < limit; i++) {
                available.add(new ExpensiveObject(i));
            }
        }

        public ExpensiveObject get() {
            getLock.lock();
            try {
                var iterator = available.iterator();
                if (iterator.hasNext()) {
                    var next = iterator.next();
                    iterator.remove();
                    inUse.add(next);
                    return next;
                } else {
                    throw new RuntimeException("No more left");
                }
            } finally {
                getLock.unlock();
            }
        }

        public void returnObject(ExpensiveObject expensiveObject) {
            returnLock.lock();
            try {
                inUse.remove(expensiveObject);
                available.add(expensiveObject);
            } finally {
                returnLock.unlock();
            }
        }

    }

    private static record ExpensiveObject(int id) {}
}
```

### Object pool vs Flyweight

Pooled objects can simultaneously be used by a single "client" only. For that, a pooled object must be checked out from the pool, then it can be used by a client, and then the client must return the object back to the pool. Multiple instances of identical objects may exist, up to the maximal capacity of the pool.

In contrast, a Flyweight object is singleton, and it can be used simultaneously by multiple clients.

As for concurrent access, pooled objects can be mutable and they usually don't need to be thread safe, as typically, only one thread is going to use a specific instance at the same time. Flyweight must be immutable.

As for performance and scalability, pools can become bottlenecks, if all the pooled objects are in use and more clients need them, threads will become blocked waiting for available object from the pool. This is not the case with Flyweight.

Object pool increases used memory, while Flyweight does not.

# Structural patterns

## Adapter

In Java, you can consider the ``java.io.InputStreamReader`` class and the ``java.io.OutputStreamWriter`` class as examples of object adapters. They adapt an existing ``InputStream``/``OutputStream`` object to a ``Reader``/``Writer`` interface.

Example is JDK:
* [InputStreamReader:](https://docs.oracle.com/javase/8/docs/api/java/io/InputStreamReader.html#InputStreamReader-java.io.InputStream-) ``public InputStreamReader(InputStream in)``
* [OutputStreamWriter:](https://docs.oracle.com/javase/8/docs/api/java/io/OutputStreamWriter.html#OutputStreamWriter-java.io.OutputStream-) ``public OutputStreamWriter(OutputStream out)``

The adapter pattern allows the interface of an existing class to be used as another interface. Adapter lets classes work together that couldn't otherwise because of incompatible interfaces.

```
public class AdapterDemo {

    private record Rectangle(double length, double width) {
    }

    private static class Calculator {
        public double getArea(Rectangle rectangle) {
            return rectangle.length() * rectangle.width();
        }
    }

    private record Triangle(double base, double height) {
    }

    private static class CalculatorAdapter {
        public double getArea(Triangle triangle) {
            var calculator = new Calculator();
            var rectangle = new Rectangle(triangle.base(), triangle.height() * 5);
            return calculator.getArea(rectangle);
        }
    }

    public static void main(String[] args) {
        var calculatorAdapter = new CalculatorAdapter();
        var triangle = new Triangle(20, 10);
        System.out.println(calculatorAdapter.getArea(triangle));
    }
}
```

## Bridge

The bridge pattern is often confused with the adapter pattern, and is often implemented using the object adapter pattern (In this adapter pattern, the adapter contains an instance of the class it wraps).

> Consider you have a weapon with different enchantments, and you are supposed to allow mixing different weapons with different enchantments. What would you do? Create multiple copies of each of the weapons for each of the enchantments or would you just create separate enchantment and set it for the weapon as needed? Bridge pattern allows you to do the second.
 
In Plain Words:

> Bridge pattern is about preferring composition over inheritance. Implementation details are pushed from a hierarchy to another object with a separate hierarchy.

Wikipedia says:

> The bridge pattern is a design pattern used in software engineering that is meant to "decouple an abstraction from its implementation so that the two can vary independently"
 
```
public class BridgeDemo {

    public static void main(String[] args) {
        var weapon = new Sword(new SoulEatingEnchantment());
        weapon.swing();
        var weapon2 = new Sword(new FlyingEnchantment());
        weapon2.swing();
    }

    public interface Weapon {
        void swing();

        Enchantment getEnchantment();
    }

    public static record Sword(Enchantment enchantment) implements Weapon {

        @Override
        public void swing() {
            System.out.println("The sword is swinged.");
            enchantment.apply();
        }

        @Override
        public Enchantment getEnchantment() {
            return enchantment;
        }
    }

    public static record Hammer(Enchantment enchantment) implements Weapon {

        @Override
        public void swing() {
            System.out.println("The hammer is swinged.");
            enchantment.apply();
        }

        @Override
        public Enchantment getEnchantment() {
            return enchantment;
        }
    }

    public interface Enchantment {
        void apply();
    }

    public static class FlyingEnchantment implements Enchantment {

        @Override
        public void apply() {
            System.out.println("The item flies and strikes the enemies finally returning to owner's hand.");
        }
    }

    public static class SoulEatingEnchantment implements Enchantment {

        @Override
        public void apply() {
            System.out.println("The item eats the soul of enemies.");
        }
    }
}
```

### Adapter vs Bridge

> Adapter makes things work after they're designed; Bridge makes them work before they are. [GoF, p219]

## Composite

The composite pattern describes a group of objects that are treated the same way as a single instance of the same type of object. The intent of a composite is to "compose" objects into tree structures to represent part-whole hierarchies. Implementing the composite pattern lets clients treat individual objects and compositions uniformly.

### What solution does the Composite design pattern describe?

* Define a unified Component interface for both part (Leaf) objects and whole (Composite) objects.
* Individual Leaf objects implement the Component interface directly, and Composite objects forward requests to their child components.

This enables clients to work through the Component interface to treat Leaf and Composite objects uniformly: **Leaf objects perform a request directly, and Composite objects forward the request to their child components recursively downwards the tree structure.** This makes client classes easier to implement, change, test, and reuse.

```
public class CompositeDemo {

    public static void main(String[] args) {
        //Initialize four ellipses
        Ellipse ellipse1 = new Ellipse();
        Ellipse ellipse2 = new Ellipse();
        Ellipse ellipse3 = new Ellipse();
        Ellipse ellipse4 = new Ellipse();

        //Creates two composites containing the ellipses
        var graphic2 = CompositeGraphic.of(ellipse1, ellipse2, ellipse3);
        var graphic3 = CompositeGraphic.of(ellipse4);

        //Create another graphics that contains two graphics
        var graphic1 = CompositeGraphic.of(graphic2, graphic3);

        //Prints the complete graphic (Four times the string "Ellipse").
        graphic1.print();
    }

    /** "Component" */
    interface Graphic {
        //Prints the graphic.
        public void print();
    }

    /** "Composite" */
    static class CompositeGraphic implements Graphic {
        //Collection of child graphics.
        private final ArrayList<Graphic> childGraphics = new ArrayList<>();

        //Adds the graphic to the composition.
        public void add(Graphic graphic) {
            childGraphics.add(graphic);
        }

        public static CompositeGraphic of(Graphic... graphic) {
            var compositeGraphic = new CompositeGraphic();
            for (var g : graphic) {
                compositeGraphic.add(g);
            }
            return compositeGraphic;
        }

        //Prints the graphic.
        @Override
        public void print() {
            for (Graphic graphic : childGraphics) {
                graphic.print();  //Delegation
            }
        }
    }

    /** "Leaf" */
    static class Ellipse implements Graphic {
        //Prints the graphic.
        @Override
        public void print() {
            System.out.println("Ellipse");
        }
    }
}
```

### Composite pattern and Visitor

The point of composite is to apply the same operation to a bunch of elements that share an interface. The point of visitor is to extend a bunch of elements with a new operation without changing their implementation nor the caller's implementation. Therefore you often see:

```
Composite c = new Composite();
Visitor v = new ConcreteVisitor();
c.visit(v);
```

This way you can keep the implementation of the composite and the classes that are in the composite static, and only vary the kind of Visitor you apply to them.

## Decorator

All subclasses of ``java.io.InputStream``, ``OutputStream``, ``Reader`` and ``Writer`` have a constructor taking an instance of same type.

Decorator use can be more efficient than subclassing, because an object's behavior can be augmented without defining an entirely new object.

### What solution does it describe?

Define Decorator objects that
* implement the interface of the extended (decorated) object (Component) transparently by forwarding all requests to it;
* perform additional functionality before/after forwarding a request.

```
public class DecoratorDemo {

    interface Coffee {
        double getCost();

        String getIngredients();
    }

    static record SimpleCoffee() implements Coffee {

        @Override
        public double getCost() {
            return 1;
        }

        @Override
        public String getIngredients() {
            return "Coffee";
        }
    }

    abstract static class CoffeeDecorator implements Coffee {

        private final Coffee coffee;

        public CoffeeDecorator(Coffee coffee) {
            this.coffee = coffee;
        }

        @Override
        public double getCost() {
            return coffee.getCost();
        }

        @Override
        public String getIngredients() {
            return coffee.getIngredients();
        }
    }

    static class WithMilk extends CoffeeDecorator {
        public WithMilk(Coffee coffee) {
            super(coffee);
        }

        @Override
        public double getCost() {
            return super.getCost() + 0.5;
        }

        @Override
        public String getIngredients() {
            return super.getIngredients() + ", Milk";
        }
    }

    static class WithSprinkles extends CoffeeDecorator {
        public WithSprinkles(Coffee coffee) {
            super(coffee);
        }

        @Override
        public double getCost() {
            return super.getCost() + 0.2;
        }

        @Override
        public String getIngredients() {
            return super.getIngredients() + ", Sprinkles";
        }
    }

    public static void main(String[] args) {
        Coffee c = new SimpleCoffee();
        System.out.println(c.getCost() + " " + c.getIngredients());

        c = new WithMilk(c);
        System.out.println(c.getCost() + " " + c.getIngredients());

        c = new WithSprinkles(c);
        System.out.println(c.getCost() + " " + c.getIngredients());
    }
}
```

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












