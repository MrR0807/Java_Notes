# Lesson 14. Introduction of the Gradle Object Model

There are 6 core interfaces/classes in Gradle Object model.

Main interfaces are:
* ``Script<Interface>`` - is added to every gradle script file. Gradle script file is a file which ends with ``*.gradle``. It is so, that every script file has same properties and methods;
* ``Project<Interface>`` - this object is associated with ``build.gradle`` file;
* ``Gradle<Interface``;
* ``Settings<Interface>`` - this has more to do with multi-projects;
* ``Task<Interface>``;
* ``Action<Interface>``.

## Script

[Script in Gradle documentation](https://docs.gradle.org/current/dsl/org.gradle.api.Script.html)

We can access ``Script`` object in any Gradle file, just like:

```groovy
logger.info "Hello"
```

```groovy

apply { 
  println "Hello again!"
}
```

# Lesson 15. Gradle Lifecycle

Lifecycle phases:
* Initialization - Gradle support single and multi projects. During this phase Gradle decides whether it's a multi or single project. During initialization each gradle file gets an object;
* Configuration - where initialized projects (in previous phase) get's configured. For that to happen, each project has to have ``build.gradle`` file;
* Execution - where the actual ``build.gradle`` get's performed via launching ``Tasks`` and ``Actions``.

# Lesson 16. The Gradle Initialization Phase

This maps to ``init.gradle`` and ``settings.gradle`` files.

First file to be evaluated is ``init.gradle``. It allows to setup common properties, plugins, authentications etc.

``settings.gradle`` is for multi-project builds.

If you have a multi-project, then each subproject has to have ``build.gradle`` file.

If you add properties into ``init.gradle`` or other files in that folder (they will be picked up, because Gradle does care whether it's called ``init.gradle`` or ``thisisscript.gradle``. They will be executed anyway in alphabetical order), those properties will be available throughtout the whole project. However, as far as I understand, it has to be put on ``gradle`` object:

```groovy
gradle.ext.timestamp = {
  def df = new SimpleDateFormat("yyyy-MM-dd")
  df.setTimeZone(TimeZone.getTimeZone("UTC"))
  return df.format(new Date())
}
```

# Lesson 17. Lifecycle Phases and the Gradle Object Model

Every project has ``build.gradle`` file. When we run gradle, it instantize an object called ``Script<Interface>``. And it does for every script. If you have 3 ``build.gradle``, you'll have 3 ``Script`` objects.

``Script`` object ``delegates`` to ``Project`` object when Gradle is in **Configuration phase**. **The delegate changes, when in differetn Gradle lifecycle phase.**

When ``Script`` object works with ``settings.gradle`` file, then it delegates to ``Settings`` object. Hence, has different methods and properties.

When Gradle evaluates ``init.gradle``, then ``Script`` object will delegate to ``Gradle`` object.

Lastly, both ``Project`` and ``Settings`` objects have access to ``Gradle`` object through properties.

# Lesson 18. Lifecycle phases and Gradle object

To access gradle object in ``build.gradle`` file, one has to:

```groovy
println "timestamp @ ${project.gradle.timestamp()}"
```

or shorter variation

```groovy
println "timestamp @ ${gradle.timestamp()}"
```

or without curly bracelets, because we're not evaluating expression

```groovy
println "Gradle version: $gradle.gradleVersion"
```

# Lesson 19. Lifecycle phases and Project object

This is a delegate object for a ``build.gradle`` file.

We can access different properties and methods from Project object:

```groovy
println "$project.buildFile"                          //prints full path
println "${project.relativePath(project.buildFile)}"  //prints 'build.gradle'
```

# Lesson 20. Gradle Properties

In groovy, we can omnit ``get`` and parenthesis, hence ``getLogger()`` becomes ``logger``.

Gradle allows "key - value" pairs in a ``gradle.properties`` file. It is available to scripts in the ``settings.gradle`` and ``build.gradle``.

We can also pass properties through command line.

# Lesson 21. ... more on Gradle Properties!

``project.ext`` acts like a map where we can store arbitrary properties.

```groovy
project.ext.sayHello = "Hello"
```

``project.hasProperty()`` is a good method to check whether property exists.

# Lesson 22. Tasks and the Gradle Lifecycle

Each project is a collection of tasks. Task is an atomic piece of work.

In configuration phase, tasks are created and configured. In execution phase - they are executed.

A task can contain a collection of actions. Action is an atomic piece of work.

Collection of tasks are held in the **Task Container** which holds helper methods for looking up and creating tasks.

There are two helpful methods on the Task interface:
* doFirst()
* doLast()

Each method takes either Action or Closure. In named closure, it has access to the Task object.

```groovy
task ('hello').doLast {
  println "Hello from $task.name"
}
```

or 

```groovy
task hello {
  doLast {
    println "Hello from $task.name"
  }
}
```

or 

```groovy
task hello {
	doLast ({println "Hello from task: $name"})
}
```

or 

```groovy
task('hello').doLast ({println "Hello from task: $name still"})
```

# Lesson 23. Our 1st Gradle Task

We can access task via project as a property:

```groovy
task hi

project.hi.doLast {
  println "Hello"
}
```

# Lesson 24. Configuring Tasks

# Lesson 25. Introduction to dependsOn

It is used for sequencing tasks. 


































