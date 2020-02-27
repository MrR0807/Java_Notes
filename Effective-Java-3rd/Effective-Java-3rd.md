# Item 1: Consider static factory methods instead of constructors

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

# Item 2: Consider a builder when faced with many constructor parameters

Static factories and constructors share a limitation: they do not scale well to large numbers of optional parameters. There are three options:
* Telescoping constructor
* JavaBean convetions
* Builder pattern
```
// Builder Pattern  (Page 13)
public class NutritionFacts {
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    public static class Builder {
        // Required parameters
        private final int servingSize;
        private final int servings;

        // Optional parameters - initialized to default values
        private int calories      = 0;
        private int fat           = 0;
        private int sodium        = 0;
        private int carbohydrate  = 0;

        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings    = servings;
        }

        public Builder calories(int val)
        { calories = val;      return this; }
        public Builder fat(int val)
        { fat = val;           return this; }
        public Builder sodium(int val)
        { sodium = val;        return this; }
        public Builder carbohydrate(int val)
        { carbohydrate = val;  return this; }

        public NutritionFacts build() {
            return new NutritionFacts(this);
        }
    }
```

The Builder pattern is well suited to class hierarchies. Use a parallel hierarchy of builders, each nested in the corresponding class. Abstract classes have abstract builders; concrete classes have concrete builders.

```
public abstract class Pizza {
    public enum Topping { HAM, MUSHROOM, ONION, PEPPER, SAUSAGE }
    final Set<Topping> toppings;

    abstract static class Builder<T extends Builder<T>> {
        EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);
        public T addTopping(Topping topping) {
            toppings.add(Objects.requireNonNull(topping));
            return self();
        }

        abstract Pizza build();

        // Subclasses must override this method to return "this"
        protected abstract T self();
    }
    
    Pizza(Builder<?> builder) {
        toppings = builder.toppings.clone(); // See Item 50
    }
}
```
```
// Subclass with hierarchical builder (Page 15)
public class NyPizza extends Pizza {
    public enum Size { SMALL, MEDIUM, LARGE }
    private final Size size;

    public static class Builder extends Pizza.Builder<Builder> {
        private final Size size;

        public Builder(Size size) {
            this.size = Objects.requireNonNull(size);
        }

        @Override public NyPizza build() {
            return new NyPizza(this);
        }

        @Override protected Builder self() { return this; }
    }

    private NyPizza(Builder builder) {
        super(builder);
        size = builder.size;
    }

    @Override public String toString() {
        return "New York Pizza with " + toppings;
    }
}
```
```
public class Calzone extends Pizza {
    private final boolean sauceInside;

    public static class Builder extends Pizza.Builder<Builder> {
        private boolean sauceInside = false; // Default

        public Builder sauceInside() {
            sauceInside = true;
            return this;
        }

        @Override public Calzone build() {
            return new Calzone(this);
        }

        @Override protected Builder self() { return this; }
    }

    private Calzone(Builder builder) {
        super(builder);
        sauceInside = builder.sauceInside;
    }

    @Override public String toString() {
        return String.format("Calzone with %s and sauce on the %s",
                toppings, sauceInside ? "inside" : "outside");
    }
}
```

This technique, wherein a subclass method is declared to return a subtype of the return type declared in the superclass, is known as **covariant return typing.** It allows clients to use these builders without the need for casting.

**It’s often better to start with a builder in the first place. In summary, the Builder pattern is a good choice when designing classes whose constructors or static factories would have more than a handful of parameters.**

# Item 3: Enforce the singleton property with a private constructor or an enum type

Making a class a singleton can make it difficult to test its clients because it’s impossible to substitute a mock implementation for a singleton unless it implements an interface that serves as its type.

There are two common ways to implement singletons.
```
// Singleton with public final field
public class Elvis {
    public static final Elvis INSTANCE = new Elvis();
    private Elvis() { ... }
    public void leaveTheBuilding() { ... }
}

// Singleton with static factory
public class Elvis {
    private static final Elvis INSTANCE = new Elvis();
    private Elvis() { ... }
    public static Elvis getInstance() { return INSTANCE; }
    public void leaveTheBuilding() { ... }
}
```
Nothing that a client does can change this, with one caveat: a privileged client can invoke the private constructor reflectively (Item 65) with the aid of the AccessibleObject.setAccessible method.

Third way is to use single-element enum:

```
// Enum singleton - the preferred approach
public enum Elvis {
    INSTANCE;
    public void leaveTheBuilding() { ... }
}
```
This approach may feel a bit unnatural, but a single-element enum type is often the best way to implement a singleton.

# Item 4: Enforce noninstantiability with a private constructor

Occasionally you’ll want to write a class that is just a grouping of static methods and static fields. Such classes have acquired a bad reputation because some people abuse them to avoid thinking in terms of objects, but they do have valid uses.

### A class can be made noninstantiable by including a private constructor.

# Item 5: Prefer dependency injection to hardwiring resources
```
// Dependency injection provides flexibility and testability
public class SpellChecker {
    private final Lexicon dictionary;
    public SpellChecker(Lexicon dictionary) {
        this.dictionary = Objects.requireNonNull(dictionary);
    }
    public boolean isValid(String word) { ... }
    public List<String> suggestions(String typo) { ... }
}
```
A useful variant of the pattern is to pass a resource factory to the constructor. The Supplier<T> interface, introduced in Java 8, is perfect for representing factories. Methods that take a Supplier<T> on input should typically constrain the factory’s type parameter using a bounded wildcard type (Item 31) to allow the client to pass in a factory that creates any subtype of a specified type. For example, here is a method that makes a mosaic using a client-provided factory to produce each tile:

```
Mosaic create(Supplier<? extends Tile> tileFactory) { ... }
```
In summary, do not use a singleton or static utility class to implement a class that depends on one or more underlying resources whose behavior affects that of the class, and do not have the class create these resources directly. Instead, pass the resources, or factories to create them, into the constructor (or static factory or builder).

# Item 6: Avoid creating unnecessary objects

It is often appropriate to reuse a single object instead of creating a new function- ally equivalent object each time it is needed. Reuse can be both faster and more stylish. **An object can always be reused if it is immutable (Item 17).**

Some object creations are much more expensive than others. **If you’re going to need such an “expensive object” repeatedly, it may be advisable to cache it for reuse.**

**Another way to create unnecessary objects is autoboxing**, which allows the programmer to mix primitive and boxed primitive types, boxing and unboxing automatically as needed. Autoboxing blurs but does not erase the distinction between primitive and boxed primitive types.

### Prefer primitives to boxed primitives, and watch out for unintentional autoboxing.

# Item 7: Eliminate obsolete object references

Generally speaking, **whenever a class manages its own memory, the programmer should be alert for memory leaks.** Whenever an element is freed, any object references contained in the element should be nulled out.
**Another common source of memory leaks is caches.**
**A third common source of memory leaks is listeners and other callbacks.**

# Item 8: Avoid finalizers and cleaners

### Finalizers are unpredictable, often dangerous, and generally unnecessary.

### The Java 9 replacement for finalizers is cleaners. Cleaners are less dangerous than finalizers, but still unpredictable, slow, and generally unnecessary.

One shortcoming of finalizers and cleaners is that there is no guarantee they’ll be executed promptly [JLS, 12.6]. It can take arbitrarily long between the time that an object becomes unreachable and the time its finalizer or cleaner runs. This means that you should never do anything time-critical in a finalizer or cleaner.

So what should you do instead of writing a finalizer or cleaner for a class whose objects encapsulate resources that require termination, such as files or threads? **Just have your class implement AutoCloseable, and require its clients to invoke the close method on each instance when it is no longer needed.**

# Item 9: Prefer try-with-resources to try - finally

Always use try -with-resources in preference to try-finally when working with resources that must be closed. The resulting code is shorter and clearer, and the exceptions that it generates are more useful.

# Item 10: Obey the general contract when overriding equals

The easiest way to avoid problems is not to override the equals method, in **which case each instance of the class is equal only to itself.** This is the right thing to do if any of the following conditions apply:
* **Each instance of the class is inherently unique.**
* **There is no need for the class to provide a “logical equality” test.**
* **A superclass has already overridden equals , and the superclass behavior is appropriate for this class.**
* **The class is private or package-private, and you are certain that its equals method will never be invoked.**

So when is it appropriate to override equals? It is when a class has a notion of logical equality that differs from mere object identity and a superclass has not already overridden equals. **This is generally the case for *value classes*. A value class is simply a class that represents a value, such as Integer or String.**

**When you are finished writing your equals method, ask yourself three questions: Is it symmetric? Is it transitive? Is it consistent?**

Here are a few final caveats:
* **Always override hashCode when you override equals**
* **Don’t try to be too clever. If you simply test fields for equality, it’s not hard to adhere to the equals contract**

**IDEs generate equals (and hashCode) methods is generally preferable to implementing them manually because IDEs do not make careless mistakes, and humans do.**

# Item 11: Always override hashCode when you override equals

**You must override hashCode in every class that overrides equals.**

### The key provision that is violated when you fail to override hashCode is the second one: equal objects must have equal hash codes.

This one, for example, is always legal but should never be used:
```
// The worst possible legal hashCode implementation - never use!
@Override public int hashCode() { return 42; }
```
It’s legal because it ensures that equal objects have the same hash code. It’s atrocious because it ensures that every object has the same hash code. Therefore, every object hashes to the same bucket, and hash tables degenerate to linked lists.


The Objects class has a static method that takes an arbitrary number of objects and returns a hash code for them. This method, named hash , lets you write one-line hashCode methods whose quality is comparable to those written according to the recipe in this item.
```
// One-line hashCode method - mediocre performance
@Override public int hashCode() {
return Objects.hash(lineNum, prefix, areaCode);
}
```

# Item 12: Always override toString

### Providing a good toString implementation makes your class much more pleasant to use and makes systems using the class easier to debug.

The toString method is automatically invoked when an object is passed to println, printf, the string concatenation operator, or assert, or is printed by a debugger.

Nor should you write a toString method in most enum types because Java provides a perfectly good one for you. You should, however, **write a toString method in any abstract class whose subclasses share a common string representation.** For example, the toString methods on most collection implementations are inherited from the abstract collection classes.

To recap, override Object’s toString implementation in every instantiable class you write, unless a superclass has already done so. It makes classes much more pleasant to use and aids in debugging.

# Item 13: Override clone judiciously

So what does Cloneable do, given that it contains no methods? It determines the behavior of Object’s protected clone implementation: if a class implements Cloneable, Object’s clone method returns a field-byfield copy of the object; otherwise it throws CloneNotSupportedException.

A class implementing Cloneable is expected to provide a properly functioning public clone method. In order to achieve this, the class and all of its superclasses must obey a complex, unenforceable, thinly documented protocol. **The resulting mechanism is fragile, dangerous, and extralinguistic: it creates objects without calling a constructor.**

**Immutable classes should never provide a clone method because it would merely encourage wasteful copying.**

In effect, the clone method functions as a constructor; you must ensure that it does no harm to the original object and that it properly establishes invariants on the clone.

### A better approach to object copying is to provide a copy constructor or copy factory.

### Given all the problems associated with Cloneable, new interfaces should not extend it, and new extendable classes should not implement it.

# Item 14: Consider implementing Comparable

By implementing Comparable, you allow your class to interoperate with all of the many generic algorithms and collection implementations that depend on this interface. You gain a tremendous amount of power for a small amount of effort.

If you are writing a value class with an obvious natural ordering, such as alphabetical order, numerical order, or chronological order, you should implement the Comparable.

Let’s go over the provisions of the compareTo contract:
* The first provision says that if you reverse the direction of a comparison between two object references, the expected thing happens: **if the first object is less than the second, then the second must be greater than the first**; if the first object is equal to the second, then the second must be equal to the first; and if the first object is greater than the second, then the second must be less than the first.
* The second provision says that **if one object is greater than a second and the second is greater than a third, then the first must be greater than the third.**
* The final provision says that **all objects that compare as equal must yield the same results when compared to any other object.**

### If you want to add a value component to a class that implements Comparable, don’t extend it; write an unrelated class containing an instance of the first class. Then provide a “view” method that returns the contained instance.

This frees you to implement whatever compareTo method you like on the containing class, while allowing its client to view an instance of the containing class as an instance of the contained class when needed.

For example, consider the BigDecimal class, whose compareTo method is inconsistent with equals. If you create an empty HashSet instance and then add new BigDecimal("1.0") and new BigDecimal("1.00"), the set will contain two elements because the two BigDecimal instances added to the set are unequal when compared using the equals method. If, however, you perform the same procedure using a TreeSet instead of a HashSet, the set will contain only one element because the two BigDecimal instances are equal when compared using the compareTo method. (See the BigDecimal documentation for details.)

**In Java 7, static compare methods were added to all of Java’s boxed primitive classes.**

If a class has multiple significant fields, the order in which you compare them is critical. Start with the most significant field and work your way down.
```
// Multiple-field Comparable with primitive fields
public int compareTo(PhoneNumber pn) {
    int result = Short.compare(areaCode, pn.areaCode);
    if (result == 0) {
        result = Short.compare(prefix, pn.prefix);
        if (result == 0)
            result = Short.compare(lineNum, pn.lineNum);
        }
    return result;
}
```
**In Java 8, the Comparator interface was outfitted with a set of comparator construction methods, which enable fluent construction of comparators.**

```
private static final Comparator<PhoneNumber> COMPARATOR = 
    comparingInt((PhoneNumber pn) -> pn.areaCode)
    .thenComparingInt(pn -> pn.prefix)
    .thenComparingInt(pn -> pn.lineNum);

public int compareTo(PhoneNumber pn) {
    return COMPARATOR.compare(this, pn);
}
```

**In summary, whenever you implement a value class that has a sensible ordering, you should have the class implement the Comparable interface so that its instances can be easily sorted, searched, and used in comparison-based collections. When comparing field values in the implementations of the compareTo methods, avoid the use of the < and > operators. Instead, use the static compare methods in the boxed primitive classes or the comparator construction methods in the Comparator interface.**

# Item 15: Minimize the accessibility of classes and members

A well-designed component hides all its implementation details, cleanly separating its API from its implementation.

### The rule of thumb is simple: make each class or member as inaccessible as possible.

**If a top-level class or interface can be made package-private, it should be.**
The need for protected members should be relatively rare.

**It is acceptable to make a private member of a public class package-private in order to test it**, but it is not acceptable to raise the accessibility any higher. In other words, it is not acceptable to make a class, interface, or member a part of a package’s exported API to facilitate testing.

To summarize, you should reduce accessibility of program elements as much as possible (within reason). After carefully designing a minimal public API, you should prevent any stray classes, interfaces, or members from becoming part of the API. With the exception of public static final fields, which serve as constants, public classes should have no public fields. Ensure that objects referenced by public static final fields are immutable.

# Item 16: In public classes, use accessor methods, not public fields

Occasionally, you may be tempted to write degenerate classes that serve no purpose other than to group instance fields:

```
// Degenerate classes like this should not be public!
class Point {
    public double x;
    public double y;
}
```

**If a class is accessible outside its package, provide accessor methods**.

**However, if a class is package-private or is a private nested class, there is nothing inherently wrong with exposing its data fields.**

# Item 17: Minimize mutability

To make a class immutable, follow these five rules:
* **Don’t provide methods that modify the object’s state**
* **Ensure that the class can’t be extended**
* **Make all fields final**
* **Make all fields private**
* **Ensure exclusive access to any mutable components. If your class has any fields that refer to mutable objects, ensure that clients of the class cannot obtain references to these objects.**

Notice how the arithmetic operations create and return a new Complex instance rather than modifying this instance. This pattern is known as the ***functional approach* because methods return the result of applying a function to their operand, without modifying it. Contrast it to the *procedural or imperative approach* in which methods apply a procedure to their operand, causing its state to change.**

### Immutable objects are inherently thread-safe; they require no synchronization.

An immutable class can provide static factories (Item 1) that cache frequently requested instances to avoid creating new instances when existing ones would do.

**The major disadvantage of immutable classes is that they require a separate object for each distinct value.** Creating these objects can be costly, especially if they are large.

The performance problem is magnified if you perform a multistep operation that generates a new object at every step, eventually discarding all objects except the final result.

However, you could solve it via package-private mutable companion class.

Few design alternatives:
* Recall that to guarantee immutability, a class must not permit itself to be subclassed. This can be done by making the class final, but there is another, more flexible alternative. **Instead of making an immutable class final, you can make all of its constructors private or package-private and add public static factories in place of the public constructors.**

**This approach is often the best alternative. It is the most flexible because it allows the use of multiple package-private implementation classes.** To its clients that reside outside its package, the immutable class is effectively final because it is impossible to extend a class that comes from another package and that lacks a public or protected constructor.

However, some immutable classes have one or more nonfinal fields in which they cache the results of expensive computations the first time they are needed. If the same value is requested again, the cached value is returned, saving the cost of recalculation. This trick works precisely because the object is immutable, which guarantees that the computation would yield the same result if it were repeated.

### Classes should be immutable unless there’s a very good reason to make them mutable.
### Declare every field private final unless there’s a good reason to do otherwise

# Item 18: Favor composition over inheritance

**Unlike method invocation, inheritance violates encapsulation.** The superclass’s implementation may change from release to release, and if it does, the subclass may break, even though its code has not been touched.

Instead of extending an existing class, give your new class a private field that references an instance of the existing class. This design is called *composition* because the existing class becomes a component of the new one. Each instance method in the new class invokes the corresponding method on the contained instance of the existing class and returns the results. This is known as *forwarding*, and the methods in the new class are known as *forwarding methods*.

The disadvantages of wrapper classes are few:
* One caveat is that wrapper classes are not suited for use in callback frameworks, wherein objects pass selfreferences to other objects for subsequent invocations (“callbacks”).
```
// basic class which we will wrap
public class Model{ 
    Controller controller;

    Model(Controller controller){
        this.controller = controller; 
        controller.register(this); //Pass SELF reference
    }

    public void makeChange(){
        ... 
    }
} 

public class Controller{
    private final Model model;

    public void register(Model model){
        this.model = model;
    }

    // Here the wrapper just fails to count changes, 
    // because it does not know about the wrapped object 
    // references leaked
    public void doChanges(){
        model.makeChange(); 
    } 
}

// wrapper class
public class ModelChangesCounter{
    private final Model; 
    private int changesMade;

    ModelWrapper(Model model){
        this.model = model;
    }

    // The wrapper is intended to count changes, 
    // but those changes which are invoked from 
    // Controller are just skipped    
    public void makeChange(){
        model.makeChange(); 
        changesMade++;
    } 
}
```


* Some people worry about the performance impact of forwarding method invocations or the memory footprint impact of wrapper objects. Neither turn out to have much impact in practice. It’s tedious to write forwarding methods, but you have to write the reusable forwarding class for each interface only once, and forwarding classes may be provided for you.

**Inheritance is appropriate only in circumstances where the subclass really is a subtype of the superclass. In other words, a class B should extend a class A only if an “is-a” relationship exists between the two classes.** If you are tempted to have a class B extend a class A, ask yourself the question: Is every B really an A? If you cannot truthfully answer yes to this question, B should not extend A.

### To summarize, inheritance is powerful, but it is problematic because it violates encapsulation. It is appropriate only when a genuine subtype relationship exists between the subclass and the superclass. Even then, inheritance may lead to fragility if the subclass is in a different package from the superclass and the superclass is not designed for inheritance. To avoid this fragility, use composition and forwarding instead of inheritance, especially if an appropriate interface to implement a wrapper class exists. Not only are wrapper classes more robust than subclasses, they are also more powerful.

# Item 19: Design and document for inheritance or else prohibit it

First, the class must document precisely the effects of overriding any method. In other words, the class must document its self-use of overridable methods.

But doesn’t this violate the dictum that good API documentation should describe what a given method does and not how it does it? Yes, it does! **This is an unfortunate consequence of the fact that inheritance violates encapsulation. To document a class so that it can be safely subclassed, you must describe implementation details that should otherwise be left unspecified.** The **@implSpec** tag was added in Java 8 and used heavily in Java 9.

To allow programmers to write efficient subclasses without undue pain, a class may have to provide hooks into its internal workings in the form of judiciously chosen protected methods or, in rare instances, protected fields.

**The only way to test a class designed for inheritance is to write subclasses. Experience shows that three subclasses are usually sufficient to test an extendable class.**

### Constructors must not invoke overridable methods, directly or indirectly.

**The Cloneable and Serializable interfaces present special difficulties when designing for inheritance. It is generally not a good idea for a class designed for inheritance to implement either of these interfaces.**

### Designing a class for inheritance requires great effort and places substantial limitations on the class.

**The best solution to this problem is to prohibit subclassing in classes that are not designed and documented to be safely subclassed.** There are two ways to prohibit subclassing. The easier of the two is to **declare the class final.** The alternative is to make all the **constructors private or package-private and to add public static factories** in place of the constructors.

# Item 20: Prefer interfaces to abstract classes

### Existing classes can easily be retrofitted to implement a new interface.

Existing classes cannot, in general, be retrofitted to extend a new abstract class. Even if you managed, this can cause great collateral damage to the type hierarchy, forcing all descendants of the new abstract class to subclass it, whether or not it is appropriate.

### Interfaces are ideal for defining mixins. Loosely speaking, a mixin is a type that a class can implement in addition to its “primary type,” to declare that it provides some optional behavior. For example, Comparable is a mixin interface.

### Interfaces allow for the construction of nonhierarchical type frameworks.

Type hierarchies are great for organizing some things, but other things don’t fall neatly into a rigid hierarchy. For example:
```
public interface Singer {
    AudioClip sing(Song s);
}

public interface Songwriter {
    Song compose(int chartPosition);
}

```
**In real life, some singers are also songwriters. Because we used interfaces rather than abstract classes to define these types, it is perfectly permissible for a single class to implement both Singer and Songwriter.**

You can, however, combine the advantages of interfaces and abstract classes by providing an abstract skeletal implementation class to go with an interface. The interface defines the type, perhaps providing some default methods, while the skeletal implementation class implements the remaining non-primitive interface methods atop the primitive interface methods. Extending a skeletal implementation takes most of the work out of implementing an interface. This is the Template Method pattern

Collections Framework provides a skeletal implementation to go along with each main collection interface: AbstractCollection, AbstractSet, AbstractList, and AbstractMap.

# Item 21: Design interfaces for posterity

### It is not always possible to write a default method that maintains all invariants of every conceivable implementation.

For example, consider the removeIf method, which was added to the Collection interface in Java 8.

This is the best general-purpose implementation one could possibly write for the removeIf method, but sadly, it fails on some real-world Collection implementations. For example, consider org.apache.commons.collections4.collection.SynchronizedCollection. This class, from the Apache Commons library, is similar to the one returned by the static factory Collections.synchronizedCollection in java.util. The Apache version additionally provides the ability to use a client-supplied object for locking, in place of the collection. In other words, it is a wrapper class (Item 18), all of whose methods synchronize on a locking object before delegating to the wrapped collection. The Apache SynchronizedCollection class is still being actively maintained, but as of this writing, it does not override the removeIf method. If this class is used in conjunction with Java 8, it will therefore inherit the default implementation of removeIf, which does not, indeed cannot, maintain the class’s fundamental promise: to automatically synchronize around each method invocation.

### In the presence of default methods, existing implementations of an interface may compile without error or warning but fail at runtime.

**Using default methods to add new methods to existing interfaces should be avoided unless the need is critical.**

# Item 22: Use interfaces only to define types

When a class implements an interface, the interface serves as a type that can be used to refer to instances of the class. That a class implements an interface should therefore say something about what a client can do with instances of the class.

#### The constant interface pattern is a poor use of interfaces.

In summary, interfaces should be used only to define types. They should not be used merely to export constants.

# Item 23: Prefer class hierarchies to tagged classes
```
// Tagged class - vastly inferior to a class hierarchy! (Page 109)
class Figure {
    enum Shape { RECTANGLE, CIRCLE };

    // Tag field - the shape of this figure
    final Shape shape;

    // These fields are used only if shape is RECTANGLE
    double length;
    double width;

    // This field is used only if shape is CIRCLE
    double radius;

    // Constructor for circle
    Figure(double radius) {
        shape = Shape.CIRCLE;
        this.radius = radius;
    }

    // Constructor for rectangle
    Figure(double length, double width) {
        shape = Shape.RECTANGLE;
        this.length = length;
        this.width = width;
    }

    double area() {
        switch(shape) {
            case RECTANGLE:
                return length * width;
            case CIRCLE:
                return Math.PI * (radius * radius);
            default:
                throw new AssertionError(shape);
        }
    }
}
```
Tagged classes are verbose, error-prone, and inefficient. A tagged class is just a pallid imitation of a class hierarchy.

#### When you encounter an existing class with a tag field, consider refactoring it into a hierarchy.

# Item 24: Favor static member classes over nonstatic

There are four kinds of nested classes:
* Static member classes
* Nonstatic member classes
* Anonymous classes
* Local classes

One common use of a static member class is as a public helper class, useful only in conjunction with its outer class. For example, consider an enum describing the operations supported by a calculator (Item 34). The Operation enum should be a public static member class of the Calculator class. Clients of Calculator could then refer to operations using names like Calculator.Operation.PLUS and Calculator.Operation.MINUS.

### Each instance of a nonstatic member class is implicitly associated with an enclosing instance of its containing class.

One common use of a nonstatic member class is to define an Adapter [Gamma95] that allows an instance of the outer class to be viewed as an instance of some unrelated class. For example, implementations of the Map interface typically use nonstatic member classes to implement their collection views, which are returned by Map ’s keySet , entrySet , and values methods. Similarly, implementations of the collection interfaces, such as Set and List , typically use nonstatic member classes to implement their iterators:

```
// Typical use of a nonstatic member class
public class MySet<E> extends AbstractSet<E> {
     ... // Bulk of the class omitted
     @Override public Iterator<E> iterator() {
         return new MyIterator();
     }

     private class MyIterator implements Iterator<E> {
         ...
     }
}

```

#### If you declare a member class that does not require access to an enclosing instance, always put the static modifier in its declaration.

Reference to its enclosing instance can result in the enclosing instance being retained when it would otherwise be eligible for garbage collection (Item 7). The resulting memory leak can be catastrophic. **It is often difficult to detect because the reference is invisible.**

Before lambdas were added to Java (Chapter 6), anonymous classes were the preferred means of creating small function objects and process objects on the fly, but lambdas are now preferred (Item 42). Another common use of anonymous classes is in the implementation of static factory methods (see intArrayAsList in Item 20).

# Item 25: Limit source files to a single top-level class































