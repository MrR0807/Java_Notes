# Modularity Matters

Goal of modularity: **managing and reducing complexity.**

**Modularization** is the act of decomposing a system into self-contained but interconnected modules. Modules are identifiable artifacts containing code, with metadata describing the module and its relation to other modules.

Modules must adhere to three core tenets:
* **Strong encapsulation**. A module must be able to conceal part of its code from other modules.
* **Well-defined interfaces**. Encapsulation is fine, but if modules are to work together, not everything can be encapsulated. Code that is not encapsulated is, by definition, part of the public API of a module.
* **Explicit dependencies**. Modules often need other modules to fulfill their obligations. Such dependencies must be part of the module definition, in order for modules to be self-contained.

## JARs as Modules?

JAR files seem to be the closest we can get to modules pre-Java 9.

## Classpath Hell

The classpath is used by the Java runtime to locate classes. In our example, we run Main, and all classes that are directly or indirectly referenced from this class need to be loaded at some point. You can **view the classpath as a list of all classes that may be loaded at runtime**.

A condensed view of the resulting classpath could looks like this:

    java.lang.Object
    java.lang.String
    ...
    sun.misc.BASE64Encoder
    sun.misc.Unsafe
    ...
    javax.crypto.Cypher
    javax.crypto.SecretKey
    ...
    com.myapp.Main
    ...
    com.google.common.base.Joiner
    ...
    com.google.common.base.internal.Joiner
    org.hibernate.validator.HibernateValidator
    org.hibernate.validator.constraints.NotEmpty
    ...
    org.hibernate.validator.internal.engine.ConfigurationImpl

There’s no notion of JARs or logical grouping anymore. **All classes are sequenced into a flat list**, in the order defined by the -classpath argument. When the JVM loads a class, it reads the classpath in sequential order to find the right one. As soon as the class is found, the search ends and the class is loaded.

**Classes are loaded lazily.**

More insidious problems arise when duplicate classes are on the classpath. Maven resolves dependencies transitively, it’s not uncommon for two versions of the same library (say, Guava 19 and Guava 18) to end up in this set, through no fault of your own. Now both library JARs are flattened into the classpath, in an undefined order. **Whichever version of the library classes comes first is loaded.**

## Java 9 Modules

Modules can either export or strongly encapsulate packages. Furthermore, they express dependencies on other modules explicitly.

The most essential platform module in the modular JDK is **java.base**. It exposes packages such as java.lang and java.util, which no other module can do without. Because you cannot avoid using types from these packages, **every module requires java.base implicitly**.

These are the most important benefits of the Java Platform Module System:

**Reliable configuration**.
The module system checks whether a given combination of modules satisfies all dependencies before compiling or running code. This leads to fewer run-time errors.

**Strong encapsulation**.
Modules explicitly choose what to expose to other modules. Accidental dependencies on internal implementation details are prevented.

**Scalable development**.
Explicit boundaries enable teams to work in parallel while still creating maintainable codebases. Only explicitly exported public types are shared, creating boundaries that are automatically enforced by the module system.

**Security**.
Strong encapsulation is enforced at the deepest layers inside the JVM. This limits the attack surface of the Java runtime. Gaining reflective access to sensitive internal classes is not possible anymore.

**Optimization**.
Because the module system knows which modules belong together, including platform modules, no other code needs to be considered during JVM startup. It also opens up the possibility to create a minimal configuration of modules for distribution.

# Modules and the Modular JDK

Prior to the Java module system, the runtime library of the JDK consisted of a hefty rt.jar, weighing in at more than 60 megabytes. It contains most of the runtime classes for Java: the ultimate monolith of the Java platform.

## The Modular JDK

The JDK now consists of about 90 platform modules, instead of a monolithic library. **The Java module system does not allow compile-time circular dependencies between modules**.

## Incubator Modules

All incubator modules have the jdk.incubator prefix. Like a new HttpClient API was shipped in the jdk.incubator.httpclient module.

## Module Descriptors

A module has a name, it groups related code and possibly other resources, and is described by a module descriptor. The module descriptor lives in a file called **module-info.java**.

    module java.prefs {
      requires java.xml;

      exports java.util.prefs;

    }

* The **requires** keyword indicates a dependency, in this case on module java.xml
* A single package from the java.prefs module is **exported** to other modules

Modules live in a global namespace; therefore, module names must be unique. A **module** descriptor always **starts with the module keyword**, followed by the name of the module.

## Is Public Still Public?

Until Java 9, things were quite straightforward. If you had a public class or interface, it could be used by every other class. As of Java 9, public means public only to all other packages inside that module. Only when the package containing the public type is exported can it be used by other modules.

## Implied Readability

    module java.sql {
      requires transitive java.logging;
      requires transitive java.xml;

      exports java.sql;
      exports javax.sql;
      exports javax.transaction.xa;
    }

The **requires** keyword is now followed by the **transitive** modifier. A normal **requires** allows a module to access types in exported packages from the required module only. **requires transitive** means that any module requiring java.sql will now automatically be requiring java.logging and java.xml.

A **nontransitive dependency** means the dependency is necessary to support the internal implementation of that module. **A transitive dependency means the dependency is necessary to support the API of the module.**

## Qualified Exports

In some cases, you’ll want to **expose a package only to certain other modules**. You can do this by using **qualified exports** in the module descriptor.

    module java.xml {
      exports com.sun.xml.internal.stream.writers to java.xml.ws
    }

The exported package is accessible only by the modules specified after to. Multiple module names, separated by a comma, can be provided as targets for a qualified export.
In general, avoid using qualified exports between modules in an application.

## Module Resolution and the Module Path

Modules are resolved from the module path, as opposed to the classpath. Whereas the classpath is a flat list of types (even when using JAR files), the module path contains only modules.

When you want to run an application packaged as a module, you need all of its dependencies as well. Module resolution is the process of computing a minimal required set of modules given a dependency graph and a root module chosen from that graph. Every module reachable from the root module ends up in the set of resolved modules. It contains these steps:

* Start with a single root module and add it to the resolved set.
* Add each required module ( requires or requires transitive in module-info.java) to the resolved set.
* Repeat previous step for each new module added to the resolved set.

### Example

    module app {
        requires java.sql;
    }

Steps of module resolution:
* Add app to the resolved set; observe that it requires java.sql.
* Add java.sql to the resolved set; observe that it requires java.xml and java.logging.
* Add java.xml to the resolved set; observe that it requires nothing else.
* Add java.logging to the resolved set; observe that it requires nothing else.
* No new modules have been added; resolution is complete.

## Using the Modular JDK Without Modules

Java 9 can be used like previous versions of Java, **without moving your code into modules**. Code compiled and loaded outside a module ends up in the unnamed module. In contrast, all modules you’ve seen so far are explicit modules, defining their name in module-info.java. The unnamed module is special: it reads all other modules, including the java.logging module in this case.

# Working with Modules

### Example:

    package com.modules.hello;

    public class HelloWorld {

        public static void main(String[] args){
            System.out.println("Hello world");
        }
    }


The layout of the sources on the filesystem looks as follows:

    \---src
        \---helloworld
            |   module-info.java
            |
            \---com
                \---modules
                    \---hello


Compared to the traditional layout of Java source files, there are two major differences:
* There is an extra level of indirection: below src we introduce another directory, helloworld. **The directory is named after the name of the module we’re creating.** 
* Inside this module directory we find both the source file (nested in its package structure as usual) and a module descriptor.

Our Modular Hello World example is quite minimalistic:

    module helloworld {
    }

**The name must match the name of the directory containing the module descriptor.**

## Naming Modules

In Java it’s customary to make package names globally unique by using reverse DNS notation.

## Compilation

Prior to Java 9, the Java compiler is invoked with a destination directory and a set of sources to compile:

    javac -d . com/modules/hello/Hello.java
    java com.modules.hello.Hello

After modules:

    javac -d out src/helloworld/com/modules/hello/Hello.java src/helloworld/module-info.java

Generates:

    \---out
        |   module-info.class
        |
        \---com
            \---modules
                \---hello
                        Hello.class

Or you can generate with:

    javac -d out/helloworld src/helloworld/com/modules/hello/Hello.java src/helloworld/module-info.java

Generates: 

    \---out
        \---helloworld
            |   module-info.class
            |
            \---com
                \---modules
                    \---hello
                            Hello.class

This is know as **exploded module** format. It’s best to name the directory containing the exploded module after the module, but not required. Ultimately, the module system takes the name of the module from the descriptor, not from the directory name.

## Compiling multiple modules

What you’ve seen so far is the so-called **single-module mode** of the Java compiler. Typically, the project you want to compile consists of multiple modules. These modules may or may not refer to each other. Or the project might be a single module but uses other (already compiled) modules. For these cases, additional compiler flags have been introduced: **--module-source-path** and **--module-path**. These are the module aware counterparts of the -sourcepath and -classpath flags that have been part of javac for a long time.

## Packaging

Modules can be packaged and used in JAR files. This results in modular JAR files. A modular JAR file is similar to a regular JAR file, except it also contains a module-info.class. To package up the Modular Hello World example:

    jar -cfe <output dir> <main-class> -C <compiled classes> . 
    jar -cfe archive/my.jar com.modules.hello.Hello -C out/helloworld .

-c (create)
-f (The archive file name)
-e or --main-class (The application entry point)

Generated jar contains.The contents of the JAR are now similar to the exploded module, with the addition of a MANIFEST.MF file. Everything what is included in directory after -C:

    \---myjar
        |   module-info.class
        |
        +---com
        |   \---modules
        |       \---hello
        |               Hello.class
        |
        \---META-INF
                MANIFEST.MF

## Running Modules

Both the exploded module format and modular JAR file can be run. The exploded module format can be started with the following command:

    java --module-path out/helloworld/ --module helloworld/com.modules.hello.Hello

Or

    java --module-path out/ --module helloworld/com.modules.hello.Hello

Either way - it works.

**You can also use the short-form -p flag instead of --module-path. The --module flag can be shortened to -m**.

Running jar:

    java -p archive -m helloworld

JAR knows the class to execute from its metadata. We explicitly set the entry point to com.modules.hello.Hello when constructing the modular JAR.

You can trace the actions taken by the module system by adding --show-module-resolution to the java command:

    java --show-module-resolution -p archive -m helloworld

Output:

    root helloworld .../archive/my.jar
    java.base binds java.management jrt:/java.management
    java.base binds jdk.security.auth jrt:/jdk.security.auth
    ...
    jdk.javadoc requires java.xml jrt:/java.xml
    jdk.jdeps requires java.compiler jrt:/java.compiler
    jdk.jdeps requires jdk.compiler jrt:/jdk.compiler
    java.security.jgss requires java.naming jrt:/java.naming
    ...
    jdk.accessibility requires java.desktop jrt:/java.desktop
    jdk.unsupported.desktop requires java.desktop jrt:/java.desktop
    java.rmi requires java.logging jrt:/java.logging


To limit only to some modules you can use --limit-modules:

    java --show-module-resolution --limit-modules java.base -p archive -m helloworld

Output:
    
    root helloworld .../archive/my.jar
    
In this case, no other modules are required (besides the implicitly required platform module java.base) to run helloworld.

An error would be encountered at startup if another module were necessary to run helloworld and it’s not present on the module path (or part of the JDK platform modules). This form of reliable configuration is a huge improvement over the old classpath situation. **Before the module system, a missing dependency is noticed only when the JVM tries to load a nonexistent class at run-time**.

## Module Path

The module path is a list of paths to individual modules and directories containing modules. Each directory on the module path can contain zero or more module definitions, where a module definition can be an exploded module or a modular JAR file. An example module path containing all three options looks like this: 

    out/:myexplodedmodule/:mypackagedmodule.jar

All modules inside the out directory are on the module path, in conjunction with the module myexplodedmodule (a directory) and mypackagedmodule (a modular JAR file).

## Linking Modules

Using **jlink**, you can create a runtime image containing only the necessary modules to run an application. Using the following command, we create a new runtime image with the helloworld module as root:

    jlink --module-path archive/;$JAVA_HOME/jmods --add-modules helloworld --launcher hello=helloworld --output helloworld-image/

The first option constructs a module path containing the mods directory (where helloworld lives) and the directory of the JDK installation containing the platform modules we want to link into the image. Unlike with javac and java, you have to explicitly add platform modules to the jlink module path. Then, --add-modules indicates helloworld is the root module that needs to be runnable in the runtime image. With --launcher, we define an entry point to directly run the module in the image. Last, --output indicates a directory name for the runtime image.

# No Module Is an Island

## Introducing the EasyText Example

Source https://github.com/java9-modularity/examples/tree/master/chapter3/easytext-singlemodule

### File tree

       \---src
           \---easytext
               |   module-info.java
               |
               \---modules
                   \---easytext
                           Main.java

# A Tale of Two Modules

        \---src
            +---analysis
            |   |   module-info.java
            |   |
            |   \---modules
            |       \---easytext
            |           \---analysis
            |                   FleschKincaid.java
            |
            \---cli
                |   module-info.java
                |
                \---modules
                    \---easytext
                        \---cli
                                Main.java


    module easytext.analysis {
        exports javamodularity.easytext.analysis;
    }
    
    module easytext.cli {
        requires easytext.analysis;
    }

The difference is that the Main class now delegates the algorithmic analysis to the FleschKincaid class. Since we have two interdependent modules, we need to compile them using the multimodule mode of javac:

    javac -d out --module-source-path src/ --module easytext.cli
    javac -d <output_dir> --module-source-path <source_path> --module <main_module>
    
However, this will not compile due to folder naming:

    error: module easytext.cli not found in module source path
    
You have to rename the folders to corresponding names within module-info.java:

**Renamed folders' tree structure:**

    ---src
       +---easytext.analysis
       |   |   module-info.java
       |   |
       |   \---modules
       |       \---easytext
       |           \---analysis
       |                   FleschKincaid.java
       |
       \---easytext.cli
           |   module-info.java
           |
           \---modules
               \---easytext
                   \---cli
                           Main.java
                           

After that, you can run one of two commands:

    javac -d out --module-source-path src --module easytext.cli

Or 

    javac -d out --module-source-path src $(find src -name "*.java")
    

Another check the module system enforces is for cyclic dependencies. In the previous chapter, you learned that readability relations between modules must be acyclic at compile-time. **Within modules, you can still create cyclic relations between classes, as has always been the case.** It’s debatable whether you really want to do so from a software engineering perspective, but you can. However, at the module level, there is no choice. **Dependencies between modules must form an acyclic, directed graph.** By extension, there can never be cyclic dependencies between classes in different modules. **If you do introduce a cyclic dependency, the compiler won’t accept it. Adding requires easytext.cli to the *analysis module* descriptor introduces a cycle**

## Interfaces and Instantiation

Ideally, we’d abstract away the different analyses behind an interface. After all, we’re just passing in sentences and getting back a score for each algorithm:

    public interface Analyzer {
        String getName();
        double analyze(List<List<String>> text);
    }

The Analyzer interface is stable and can live in its own module—say, easytext.analysis.api. That’s what the frontend modules should know and care about. The analysis implementation modules then require this API module as well and implement the Analyzer interface. So far, so good.

However, there’s a problem. Even though the frontend modules care only about calling the analyze method through the Analyzer interface, they still need a concrete instance to call this method on:

    Analyzer analyzer = ???

# Services

Previous problem can be solved via factory pattern.

Modules:

    +---easytext.api
    |   |   easytext.api.iml
    |   |   module-info.java
    |   |
    |   \---modules
    |       \---easytext
    |           \---api
    |                   Analyzer.java
    |
    +---easytext.cli
    |   |   easytext.cli.iml
    |   |   module-info.java
    |   |
    |   \---modules
    |       \---easytext
    |           \---cli
    |                   Main.java
    |
    +---easytext.coleman
    |   |   easytext.coleman.iml
    |   |   module-info.java
    |   |
    |   \---modules
    |       \---easytext
    |           \---coleman
    |                   Coleman.java
    |
    +---easytext.factory
    |   |   easytext.factory.iml
    |   |   module-info.java
    |   |
    |   \---modules
    |       \---easytext
    |           \---factory
    |                   AnalyzerFactory.java
    |
    \---easytext.kincaid
        |   easytext.kincaid.iml
        |   module-info.java
        |
        \---modules
            \---easytext
                \---kincaid
                        FleschKincaid.java


Modules' info:

    module easytext.api {
        exports modules.easytext.api;
    }

    module easytext.cli {
        requires easytext.factory;
    }

    module easytext.coleman {
        requires easytext.api;

        exports modules.easytext.coleman;
    }


    module easytext.factory {
        requires easytext.kincaid;
        requires easytext.coleman;
        requires transitive easytext.api;

        exports modules.easytext.factory;
    }

    module easytext.kincaid{
        requires easytext.api;

        exports modules.easytext.kincaid;
    }

The frontend modules are now blissfully unaware of the analysis modules and implementation classes. There is no direct requires relation anymore between the consumer and providers of analyses. Frontend modules can be compiled independently from analysis implementation modules. When the factory offers additional analyses, the frontends will happily use them without any modification.

On the other hand, the same tight coupling issues are still present at the factory module level and below. Whenever a new analysis module comes along, the factory needs to get a dependency on it and expand the getAnalyzer implementation. And the analysis modules still need to export their implementation classes for the factory to use.

## Services for Implementation Hiding

**The main problem is that the factory still has to know about all available implementations at compile-time, and the implementation classes must be exported.**

The decoupling story can be improved a lot by the services mechanism in the Java module system. Using services, we can truly just share public interfaces, and strongly encapsulate implementation code in packages that are not exported.

Services are expressed both in module descriptors and in code by using the Service Loader API. In that sense, using services is intrusive: you need to design your application to use them.

## Providing Services

Refactoring the Coleman algorithm (provided by the easytext.algorithm.coleman module) to a service provider.


    module easytext.coleman {
        requires easytext.api;

        provides modules.easytext.api.Analyzer
                with modules.easytext.coleman.Coleman;
    }

The provides with syntax declares that this module provides an implementation of the Analyzer interface with the ColemanAnalyzer as an implementation class. **Both the service type (after provides) and the implementation class (after with) must be fully qualified type names.** Most important, the package containing the ColemanAnalyzer implementation class is not exported from this provider module.

## Consuming Services

Consuming a service in the Java module system requires two steps. The first step is adding a uses clause to module-info.java in the CLI module:

    module easytext.cli {
        requires easytext.api;

        uses modules.easytext.api.Analyzer;
    }

The uses clause instructs the ServiceLoader that this module wants to use implementations of Analyzer. The ServiceLoader then makes Analyzer instances available to the module. **Compilation will not fail when no service providers are found.** A uses clause also doesn’t guarantee there will be providers during run-time. The application will start successfully without any providers of services.

    javac -d myout --module-source-path easytext/ --module easytext.cli,easytext.kincaid,easytext.coleman

## Service Life Cycle

A new ServiceLoader is instantiated every time you call ServiceLoader::load. Such a new ServiceLoader in turn reinstantiates provider classes when they are requested. Requesting services from an existing ServiceLoader instance returns cached instances of provider classes.


    ServiceLoader<Analyzer> first = ServiceLoader.load(Analyzer.class);
    System.out.println("Using the first analyzers");
    for (Analyzer analyzer: first) {
        System.out.println(analyzer.hashCode());
    }
    
    Iterable<Analyzer> second = ServiceLoader.load(Analyzer.class);
    System.out.println("Using the second analyzers");
    for (Analyzer analyzer: second) {
        System.out.println(analyzer.hashCode());
    }

    System.out.println("Using the first analyzers again, hashCode is the same");
    for (Analyzer analyzer: first) {
        System.out.println(analyzer.hashCode());
    }

    first.reload();
    System.out.println("Reloading the first analyzers, hashCode is different");
    for (Analyzer analyzer: first) {
        System.out.println(analyzer.hashCode());
    }

Results:

    Using the first analyzers
    1379435698
    Using the second analyzers
    876563773
    Using the first analyzers again, hashCode is the same
    1379435698
    Reloading the first analyzers, hashCode is different
    87765719

## Service Provider Methods

**Service instances can be created in two ways. Either the service implementation class must have a public no-arg constructor, or a static provider method can be used.**

A provider method is a public static no-arg method called provider, where the return type is the service type. It must return a service instance of the correct type (or a subtype).

When using the provider method approach, the provides .. with clause refers to the class containing the provider method after with. This can very well be the service implementation class itself, but it can also be another class. A class appearing after with must have either a provider method or a public no-arg constructor.

    public static Analyzer provider(){
		System.out.println("Calling provider");
		return new Coleman();
	}

Or

    public class AnalyzerFactory {
        public static Analyzer provider(){
            System.out.println("Calling factory");
            return new Coleman();
        }
    }

Now we do have to change module-info.java to reflect this change. The provides .. with must now point to the class containing the static provider method.

    module easytext.coleman {
        requires easytext.api;

        provides modules.easytext.api.Analyzer
                with modules.easytext.coleman.AnalyzerFactory;
    }


## Default Service Implementations

It’s perfectly possible to put an implementation into the same module exporting the service type. When a service type has an obvious default implementation, why not provide it from the same module directly? Bundling a default service implementation with the service type guarantees that at least one implementation is always available. In that case, no defensive coding is necessary on the consumer’s part.

## Service Implementation Selection

Sometimes you want to filter and select an implementation based on certain characteristics.

## Service Type Inspection and Lazy Instantiation

With Java 9, ServiceLoader is enhanced to support service implementation type inspection before instantiation. Besides iterating over all provided instances as we’ve done so far, it’s also possible to inspect a stream of ServiceLoader.Provider descriptions. The ServiceLoader.Provider class makes it possible to inspect a service provider before requesting an instance. **The stream method on ServiceLoader returns a stream of ServiceLoader.Provider objects to inspect.**

    Interface ServiceLoader.Provider<S> {
        S get()	                    //Returns an instance of the provider.
        Class<? extends S> type()	//Returns the provider type.
    }


    @Retention(RetentionPolicy.RUNTIME)
    public @interface Fast {
        public boolean value() default true;
    }
    
    @Fast
    public class ReallyFastAnalyzer implements Analyzer {
        // Implementation of the analyzer
    }
    
    
    public class Main {
        public static void main(String args[]) {
            ServiceLoader<Analyzer> analyzers = ServiceLoader.load(Analyzer.class);
            analyzers.stream()
                     .filter(provider -> isFast(provider.type()))
                     .map(ServiceLoader.Provider::get)
                     .forEach(analyzer -> System.out.println(analyzer.getName()));
        }
        
        private static boolean isFast(Class<?> clazz) {
            return clazz.isAnnotationPresent(Fast.class) && clazz.getAnnotation(Fast.class).value() == true;
        }
    }

## Services and Linking

We can create an image for the EasyText implementation with services as well.

    jlink --module-path mods/:$JAVA_HOME/jmods --add-modules easytext.cli \ --output image
    
The api and cli modules are part of the image as expected, but what about the two analysis provider modules? If we run the application this way, **it starts correctly because service providers are optional.** But it is pretty useless without any analyzers.

**Service providers are not automatically included in the image based on uses clauses.**

To include analyzers, we use the --add-modules argument when executing jlink for each provider module we want to add:
    
    jlink --module-path mods/:$JAVA_HOME/jmods \
        --add-modules easytext.cli \
        --add-modules easytext.analysis.coleman \
        --add-modules easytext.analysis.kincaid \
        --output image
        

This looks better, but we will still find a problem when starting the application:
    
    image/bin/java -m easytext.cli input.txt

The application exits with an exception: java.lang.IllegalStateException: SyllableCounter not found. **The kincaid module uses another service of type SyllableCounter.** This is a case where a service provider uses another service to implement its functionality. We already know that **jlink doesn’t automatically include service providers, so the module containing the SyllableCounter example wasn’t included either.** We use --add-modules once more to finally get a fully functional image:

    jlink --module-path mods/:$JAVA_HOME/jmods \
        --add-modules easytext.cli \
        --add-modules easytext.analysis.coleman \
        --add-modules easytext.analysis.kincaid \
        --add-modules easytext.analysis.naivesyllablecounter \
        --output image


# Modularity Patterns

When designing for reuse, you have two main drivers:
* **Adhering to the Unix philosophy of doing only one thing and doing it well.**
* Minimizing the number of dependencies the module has itself. Otherwise, you’re burdening all reusing consumers with those transitive dependencies.

Typically, reusable modules will be smaller and more focused than single-use application modules. On the other hand, orchestrating many small modules brings about its own complexity. When reuse is of no immediate concern, having larger modules can make sense.

## Lean Modules

Simplifying and minimizing the publicly exported part of your module is beneficial for two reasons. First, a simple and small API is easier to use than a large and convoluted one. Users of the module are not burdened with unnecessary details.

## Implied Readability

easytext.cli -> easytext.repository.api -> easytext.domain.api

In this example, the TextRepository interface lives in the module easytext.repository.api, and the Text class that its findText method returns lives in another module, easytext.domain.api. The module-info.java of easytext.client (which calls the TextRepository):

	module easytext.client {
		requires easytext.repository.api;
		uses easytext.repository.api.TextRepository;
	}

The easytext.repository.api in turn depends on the easytext.domain.api, since it uses Text as the return type in the TextRepository interface:

	module easytext.repository.api {
		exports easytext.repository.api;
		requires easytext.domain.api;
	}

Note that Text has a wordcount method, which we’ll be using later in the client code. The easytext.domain.api module exports the package containing this Text class:

	module easytext.domain.api {
		exports easytext.domain.api;
	}

If we compile this, the following error is produced by the compiler:
	
	./src/easytext.client/easytext/client/Client.java:13: error: wordcount() in
	Text is defined in an inaccessible class or interface
	repository.findText("HHGTTG").wordcount();

Even though we’re not mentioning the Text type directly in easytext.client, we’re trying to call a method on this type as it is returned from the repository. Therefore, the client module needs to read the easytext.domain.api module, which exports Text.

A solution:

	module easytext.repository.api {
		exports easytext.repository.api;
		requires transitive easytext.domain.api;
	}


Note the additional **transitive** keyword in the exports clause. It effectively says that the repository module reads easytext.domain.api, and every module requiring easytext.repository.api also automatically reads easytext.domain.api.

Besides return types, this applies to argument types and thrown exceptions from different modules as well.

## Aggregator Modules

Sometimes you don’t want to burden the user of the library with the question of which exact modules to use. Maybe the modules of the library are split up in a way that benefits the library maintainer but might confuse the user.

**An aggregator module** contains no code; it has only a module descriptor setting up implied readability to all other modules:

	module library {
		requires transitive library.one;
		requires transitive library.two;
		requires transitive library.three;
	}

## Safely Splitting a Module

There is another useful application of the aggregator module pattern. It’s when you have a single, monolithic module that you need to split up after it has been released. New users of the library now have a choice of using either one of the individual modules, or largelibrary for readability on the whole API. Existing users of the library do not have to change their code or module descriptors when upgrading to this new version of largelibrary.

## Split Packages

One scenario you can run into when splitting modules is the introduction of split packages. **A split package is a single package that spans multiple modules**.

module.one contains packages:
* splitpackage.A
* splitpackage.B
* splitpackage.internal.InternalA
* splitpackage.internal.InternalB

module.two

* splitpackage.A
* splitpackage.B
* splitpackage.internal.InternalA
* splitpackage.internal.InternalB

In this example, module.one and module.two both contain classes from the same packages called splitpackage and splitpackage.internal.

**Only one module may export a given package to another module. Because exports are declared in terms of package names, having two modules export the same package leads to inconsistencies. If this were allowed, a class with the exact same fully qualified name might be exported from both modules.** Even if the split package is not exported from the modules, the module system won’t allow it. All modules from the module path are loaded within the same classloader. A classloader can have only a single definition of a package, and whether it’s exported or encapsulated doesn’t matter.

	   +---easytext.coleman
	   |   |   easytext.coleman.iml
	   |   |   module-info.java
	   |   |
	   |   \---modules
	   |       \---easytext
	   |           +---coleman
	   |           |       Coleman.java
	   |           |
	   |           \---internal
	   |                   Test.java
	   |
	   +---easytext.factory
	   |   |   easytext.factory.iml
	   |   |   module-info.java
	   |   |
	   |   \---modules
	   |       \---easytext
	   |           \---factory
	   |                   AnalyzerFactory.java
	   |
	   \---easytext.kincaid
	       |   easytext.kincaid.iml
	       |   module-info.java
	       |
	       \---modules
		   \---easytext
		       +---internal
		       |       Test.java
		       |
		       \---kincaid
			       FleschKincaid.java

This will compile, but will not run:

	Error occurred during initialization of boot layer
	java.lang.LayerInstantiationException: Package modules.easytext.internal in both module easytext.kincaid and module easytext.coleman



















