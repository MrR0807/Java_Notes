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

# Item 2: CREATING AND DESTROYING OBJECTS Consider a builder when faced with many constructor parameters

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














