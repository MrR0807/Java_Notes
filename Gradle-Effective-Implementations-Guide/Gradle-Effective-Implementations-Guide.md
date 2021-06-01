# Chapter 1. Starting with Gradle

When we develop a software, we write, compile, test, package, and finally, distribute the code.

Gradle is flexible, but has sensible defaults for most projects. This means that we can rely on the defaults if we don't want something special, but we can still can use the flexibility to adapt a build to certain custom needs.

## Declarative builds and convention over configuration

Gradle uses a Domain Specific Language (DSL) based on Groovy to declare builds. The DSL provides a flexible language that can be extended by us. As the DSL is based on Groovy, we can write Groovy code to describe a build and use the power and expressiveness of the Groovy language.

Gradle is designed to be a build language and not a rigid framework. The Gradle core itself is written in Java and Groovy. To extend Gradle, we can use Java and Groovy to write our custom code.

## Incremental builds

With Gradle, we have incremental builds. This means the tasks in a build are only executed if necessary. For example, a task to compile source code will first check whether the sources have changed since the last execution of the task. If the sources have changed, the task is executed; but if the sources haven't changed, the execution of the task is skipped and the task is marked as being up to date.

## Multi-project builds

Gradle has great support for multi-project builds. A project can simply be dependent on other projects or be a dependency of other projects. We can define a graph of dependencies among projects, and Gradle can resolve these dependencies for us.

Gradle has support for partial builds. This means that Gradle will figure out whether a project, which our project depends on, needs to be rebuild or not. If the project needs rebuilding, Gradle will do this before building our own project.

## Gradle Wrapper

The **Gradle Wrapper** allows us to execute Gradle builds even if Gradle is not installed on a computer. This is a great way to distribute source code and provide the build system with it so that the source code can be built.

## Getting started

Although Gradle uses Groovy, we don't have to install Groovy ourselves. **Gradle bundles the Groovy libraries with the distribution and will ignore a Groovy installation that is already available on our computer.**

**At the time of writing this book, the current release is 2.12.**

*My note.*
I'll try to run with Gradle 7.0 and fix possible mismatches.

### Installing Gradle

Just follow the manual in webpage.

### Writing our first build script

Gradle uses the concept of **projects to define a related set of tasks**. A Gradle **build can have one or more projects**. A **project** is a very broad concept in Gradle, but it is mostly a **set of components that we want to build for our application**.

A **project has one or more tasks**. **Tasks** are a unit of work that need to be executed by the build. Examples of tasks are compiling source code, packaging class files into a JAR file, running tests, and deploying the application.

We now know a task is a part of a project, so to create our first task, we also create our first Gradle project. We use the ``gradle`` command to run a build. Gradle will look for a file named ``build.gradle`` in the current directory. **This file is the build script for our project.** We define our tasks that need to be executed in this build script file.

We create a new ``build.gradle`` file and open this in a text editor. We type the following code to define our first Gradle task:

```groovy
task helloWorld << {
    println 'Hello world.'
}
```

With this code, we will define a helloWorld task. The task will print the words Hello world. to the console. The println is a Groovy method to print text to the console and is basically a shorthand version of the System.out.println Java method.

The code between the brackets is a **closure**. A closure is a code block that **can be assigned to a variable or passed to a method.** Java doesn't support closures, but Groovy does. As Gradle uses Groovy to define the build scripts, we can use closures in our build scripts.

The ``<<`` syntax is, technically speaking, an operator shorthand for the ``leftShift()`` method, which actually means **add to**. Therefore, here we are defining that we want to add the closure (with the println 'Hello world' statement) to our task with the helloWorld name.

First, we save ``build.gradle``, and with the gradle helloWorld command, we execute our build:

*Note.*

<< was deprecated in Gradle 4.x and removed in Gradle 5.0. Use the ``Task.doLast()`` method instead, like this:
```groovy
task helloWorld {
    doLast {
        println 'Hello world.'
    }
}
```

```shell
$ gradle helloWorld

> Task :helloWorld
Hello world.

BUILD SUCCESSFUL in 9s
1 actionable task: 1 executed
```

The first line of output shows our line Hello world. Gradle adds some more output such as the fact that the build was successful and the total time of the build. As Gradle runs in the JVM, every time we run a Gradle build, the JVM must be also started. We can use the Gradle daemon to run our builds. We will discuss more about the Gradle daemon later, but it essentially keeps Gradle running in memory so that we don't get the penalty of starting the JVM each time we run Gradle.

We can run the same build again, but only with the output of our task using the Gradle ``-quiet`` or ``-q`` command-line option.

```shell
$ gradle -q helloWorld
Hello world.
```

## Default Gradle tasks
