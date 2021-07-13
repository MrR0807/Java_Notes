# 14. Introduction of the Gradle Object Model

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

# 15. Gradle Lifecycle

Lifecycle phases:
* Initialization - Gradle support single and multi projects. During this phase Gradle decides whether it's a multi or single project. During initialization each gradle file gets an object;
* Configuration - where initialized projects (in previous phase) get's configured. For that to happen, each project has to have ``build.gradle`` file;
* Execution - where the actual ``build.gradle`` get's performed via launching ``Tasks`` and ``Actions``.

# 16. The Gradle Initialization Phase


































