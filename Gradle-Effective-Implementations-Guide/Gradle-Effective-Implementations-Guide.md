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

Gradle has several built-in tasks that we can execute. We type ``gradle -q tasks`` to see the tasks for our project:

```shell
$ gradle -q tasks

------------------------------------------------------------
All tasks runnable from root project
------------------------------------------------------------
Build Setup tasks
-----------------
init - Initializes a new Gradle build. [incubating]
wrapper - Generates Gradle wrapper files. [incubating]
Help tasks
----------
components - Displays the components produced by root project
'hello-world'. [incubating]
dependencies - Displays all dependencies declared in root project 'helloworld'.
dependencyInsight - Displays the insight into a specific n dependency
in root project 'hello-world'.
help - Displays a help message.
model - Displays the configuration model of root project 'hello-world'.
[incubating]
projects - Displays the sub-projects of root project 'hello-world'.
properties - Displays the properties of root project 'hello-world'.
tasks - Displays the tasks runnable from root project 'hello-world'.
Other tasks
-----------
helloWorld
To see all tasks and more detail, run gradle tasks --all
To see more detail about a task, run gradle help --task <task>
```

However, if I run with Gradle 7.0, I do not get helloWorld listed. I need to use ``--all`` flag:
```shell
$ gradle -q tasks --all

------------------------------------------------------------
Tasks runnable from root project 'GradleStuff'
------------------------------------------------------------

Build Setup tasks
-----------------
init - Initializes a new Gradle build.
wrapper - Generates Gradle wrapper files.

Help tasks
----------
buildEnvironment - Displays all buildscript dependencies declared in root project 'GradleStuff'.
dependencies - Displays all dependencies declared in root project 'GradleStuff'.
dependencyInsight - Displays the insight into a specific dependency in root project 'GradleStuff'.
help - Displays a help message.
javaToolchains - Displays the detected java toolchains.
outgoingVariants - Displays the outgoing variants of root project 'GradleStuff'.
projects - Displays the sub-projects of root project 'GradleStuff'.
properties - Displays the properties of root project 'GradleStuff'.
tasks - Displays the tasks runnable from root project 'GradleStuff'.

Other tasks
-----------
components - Displays the components produced by root project 'GradleStuff'. [deprecated]
dependentComponents - Displays the dependent components of components in root project 'GradleStuff'. [deprecated]
helloWorld
model - Displays the configuration model of root project 'GradleStuff'. [deprecated]
prepareKotlinBuildScriptModel
```

Here, we see our helloWorld task in the Other tasks section. The Gradle built-in tasks are displayed in the Help tasks section.

```shell
$ gradle -q help

Welcome to Gradle 7.0.2.

To run a build, run gradle <task> ...

To see a list of available tasks, run gradle tasks

To see a list of command-line options, run gradle --help

To see more detail about a task, run gradle help --task <task>
```

The properties task is very useful to see the properties available for our project. We haven't defined any property ourselves in the build script, but Gradle provides a lot of built-in properties. The following output shows some of the properties:

```shell
$ gradle -q properties

------------------------------------------------------------
Root project 'GradleStuff'
------------------------------------------------------------

allprojects: [root project 'GradleStuff']
ant: org.gradle.api.internal.project.DefaultAntBuilder@438e89bf
antBuilderFactory: org.gradle.api.internal.project.DefaultAntBuilderFactory@76798b57
artifacts: org.gradle.api.internal.artifacts.dsl.DefaultArtifactHandler_Decorated@8f5f071
asDynamicObject: DynamicObject for root project 'GradleStuff'
baseClassLoaderScope: org.gradle.api.internal.initialization.DefaultClassLoaderScope@5ca5de95
buildDir: C:\Users\BC6250\Desktop\GradleStuff\build
buildFile: C:\Users\BC6250\Desktop\GradleStuff\build.gradle
buildPath: :
buildScriptSource: org.gradle.groovy.scripts.TextResourceScriptSource@6bd177cf
buildscript: org.gradle.api.internal.initialization.DefaultScriptHandler@51c86ad1
childProjects: {}
class: class org.gradle.api.internal.project.DefaultProject_Decorated
...
```

The dependencies task will show dependencies (if any) for our project. Our first project doesn't have any dependencies, as the output shows when we run the task:

```shell
gradle -q dependencies

------------------------------------------------------------
Root project 'GradleStuff'
------------------------------------------------------------

No configurations
```

The projects tasks will display subprojects (if any) for a root project. Our project doesn't have any subprojects. Therefore, when we run the projects task, the output shows us our project has no subprojects:

```shell
$ gradle -q projects

------------------------------------------------------------
Root project 'GradleStuff'
------------------------------------------------------------

Root project 'GradleStuff'
No sub-projects
```

The model tasks displays information about the model that Gradle builds internally from our project build file.

```shell
$ gradle -q model

------------------------------------------------------------
Root project 'GradleStuff'
------------------------------------------------------------

+ tasks
      | Type:           org.gradle.model.ModelMap<org.gradle.api.Task>
      | Creator:        Project.<init>.tasks()
    + buildEnvironment
          | Type:       org.gradle.api.tasks.diagnostics.BuildEnvironmentReportTask
          | Value:      task ':buildEnvironment'
          | Creator:    Project.<init>.tasks.buildEnvironment()
          | Rules:
             ? copyToTaskContainer
    + components
          | Type:       org.gradle.api.reporting.components.ComponentReport
          | Value:      task ':components'
          | Creator:    Project.<init>.tasks.components()
          | Rules:
             ? copyToTaskContainer
...
```

## Task name abbreviation

Before we look at more Gradle command-line options, it is good to discuss a real-time save feature of Gradle: task name abbreviation. With task name abbreviation, we don't have to type the complete task name on the command line. We only have to type enough of the name to make it unique within the build.

In our first build, we only have one task, so the ``gradle h`` command should work just fine. However, we didn't take the built-in ``help`` task into account. So, to uniquely identify our ``helloWorld`` task, we use the ``hello`` abbreviation:

```shell
$ gradle -q hello
Hello world.
```

```shell
$ gradle -q hW
Hello world.
```

## Executing multiple tasks

With just a simple build script, we already discussed that we have a couple of default tasks besides our own task that we can execute. To execute multiple tasks, we only have to add each task name to the command line. Let's execute our ``helloWorld`` custom task and builtin ``tasks`` task:

```shell
gradle hello tasks
Hello world.ojects CONFIGURING [46ms]

> Task :tasks

------------------------------------------------------------
Tasks runnable from root project 'GradleStuff'
------------------------------------------------------------

Build Setup tasks
-----------------
init - Initializes a new Gradle build.
wrapper - Generates Gradle wrapper files.

Help tasks
----------
buildEnvironment - Displays all buildscript dependencies declared in root project 'GradleStuff'.
dependencies - Displays all dependencies declared in root project 'GradleStuff'.
dependencyInsight - Displays the insight into a specific dependency in root project 'GradleStuff'.
help - Displays a help message.
javaToolchains - Displays the detected java toolchains.
outgoingVariants - Displays the outgoing variants of root project 'GradleStuff'.
projects - Displays the sub-projects of root project 'GradleStuff'.
properties - Displays the properties of root project 'GradleStuff'.
tasks - Displays the tasks runnable from root project 'GradleStuff'.
```

Gradle executes the tasks in the same order as they are defined in the command line. **Gradle will only execute a task once during the build.** So even if we define the same task multiple times, it will only be executed once.

## Command-line options

The ``gradle`` command is used to execute a build. This command accepts several commandline options. We know the ``--quiet`` (or ``-q``) option to reduce the output of a build. If we use the ``--help`` (or ``-h`` or ``-?``) option, we see the complete list of options

## Logging options

Let's look at some of the options in more detail. The ``--quiet`` (or ``-q``), ``--debug`` (or ``-d``), ``--info`` (or ``-i``), ``--stacktrace`` (or ``-s``), and ``--full-stacktrace`` (or ``-S``) options control how much output we see when we execute tasks. To get the most detailed output, we use the ``--debug`` (or ``-d``) option. This option provides a lot of output with information about the steps and classes used to run the build.

**To get a better insight on the steps that are executed for our task, we can use the ``--info`` (or ``-i``) option.** The output is not as verbose as with ``--debug``, but it can provide a better understanding of the build steps:

```shell
$ gradle --info helloWorld
Initialized native services in: C:\Users\BC6250\.gradle\native
The client will now receive all logging from the daemon (pid: 22132). The daemon log file: C:\Users\BC6250\.gradle\daemon\7.0.2\daemon-22132.out.log
Starting 20th build in daemon [uptime: 33 mins 21.262 secs, performance: 100%, non-heap usage: 21% of 256 MiB]
Using 8 worker leases.
Now considering [C:\Users\BC6250\Desktop\GradleStuff] as hierarchies to watch
Not watching anything anymore
Watching the file system is enabled if available
Starting Build
Settings evaluated using settings file 'C:\Users\BC6250\Desktop\GradleStuff\settings.gradle'.
Projects loaded. Root project using build file 'C:\Users\BC6250\Desktop\GradleStuff\build.gradle'.
Included projects: [root project 'GradleStuff']

> Configure project :
Evaluating root project 'GradleStuff' using build file 'C:\Users\BC6250\Desktop\GradleStuff\build.gradle'.
All projects evaluated.
Selected primary task 'helloWorld' from project :
Tasks to be executed: [task ':helloWorld']
Tasks that were excluded: []
:helloWorld (Thread[Execution worker for ':',5,main]) started.

> Task :helloWorld
Caching disabled for task ':helloWorld' because:
  Build cache is disabled
Task ':helloWorld' is not up-to-date because:
  Task has not declared any outputs despite executing actions.
Hello world.
:helloWorld (Thread[Execution worker for ':',5,main]) completed. Took 0.014 secs.
```

If our build throws exceptions, we can see the stack trace information with the ``--stacktrace`` (or ``-s``) and ``--full-stacktrace`` (or ``-S``) options. The latter option will output the most information and is the most verbose. The ``--stacktrace`` and ``--fullstacktrace`` options can be combined with the other logging options.

## Changing the build file and directory

We created our build file with the ``build.gradle`` name. This is the default name for a build file. Gradle will look for a file with this name in the current directory to execute the build. However, we can change this with the ``--build-file`` (or ``-b``) and ``--project-dir`` (or ``-p``) command-line options.

## Running tasks without execution

With the ``--dry-run`` (or ``-m``) option, we can run all tasks without really executing them. When we use the dry-run option, we can see the tasks that are executed, so we get an insight on the tasks that are involved in a certain build scenario. We don't even have to worry whether the tasks are actually executed. Gradle builds up a **Directed Acyclic Graph** (DAG) with all tasks before any task is executed.
```shell
$ gradle -m hello 
:helloWorld 

SKIPPED  BUILD SUCCESSFUL in 8s
```

## Gradle daemon

We already discussed that Gradle executes in a JVM, and each time we invoke the gradle command, a new JVM is started, the Gradle classes and libraries are loaded, and the build is executed. We can reduce the build execution time if we don't have to load JVM and Gradle classes and libraries each time we execute a build. The ``--daemon`` command-line option starts a new Java process that will have all Gradle classes and libraries already loaded and then execute the build. Next time when we run Gradle with the ``--daemon`` option, only the build is executed as the JVM with the required Gradle classes and libraries is already running.

**The first time we execute Gradle with the --daemon option, the execution speed will not have improved as the Java background process was not started yet.** However, the next time, we can see a major improvement:

```shell

$ gradle --daemon helloWorld
Starting a new Gradle Daemon for this build (subsequent builds will be faster).
:helloWorld
Hello world.
BUILD SUCCESSFUL
Total time: 2.136 secs

$ gradle helloWorld
:helloWorld
Hello world.
BUILD SUCCESSFUL
Total time: 0.594 secs
```

Even though the daemon process is started, we can still run Gradle tasks without using the daemon. We use the --no-daemon command-line option to run a Gradle build, and then the daemon is not used:

```shell
$ gradle --no-daemon helloWorld
```

To stop the daemon process, we use the ``--stop`` command-line option:

```shell
$ gradle --stop
Stopping daemon(s).
Gradle daemon stopped.
```

To always use the --daemon command-line option, but we don't want to type it every time we run the gradle command, we can
* create an alias ```$ alias gradled='gradle --daemon'```
* Instead of using the --daemon command-line option, we can use the org.gradle.daemon Java system property to enable the daemon. ```$ export GRADLE_OPTS="-Dorg.gradle.daemon=true"```
* Finally, we can add a ``gradle.properties`` file to the root of our project directory. ``gradle.properties`` content: ```org.gradle.daemon=true```

## Profiling

Gradle also provides the ``--profile`` command-line option. This option records the time that certain tasks take to complete. The data is saved in an HTML file in the ``build/reports/profile`` directory.

## Offline usage

If we don't have access to a network at some location, we might get errors from our Gradle build, when a task needs to download something from the Internet, for example. We can use the ``--offline`` command-line option to instruct Gradle to not access any network during the build. This way we can still execute the build if all necessary files are already available offline and we don't get an error.

## Understanding the Gradle graphical user interface

*Note.*

The Gradle GUI has been deprecated and will be removed in Gradle 4.0.

# Chapter 2. Creating Gradle Build Scripts

In Gradle, projects and tasks are two important concepts. **A Gradle build always consists of one or more projects. A project defines some sort of component that we want to build.** There are no defining rules about what the component is. It can be a JAR file with utility classes to be used in other projects, or a web application to be deployed to the corporate intranet. A project doesn't have to be about building and packaging code, it can also be about doing things such as copying files on a remote server or deployment of applications to servers.

**A project has one or more tasks. A task is a small piece of work that is executed when we run a build**, for example, compiling source code, packaging code in an archive file, generating documentation, and so on.

## Writing a build script

```groovy
project.description = 'Simple project'

task simple {
    doLast {
        println 'Running simple task for project ' + project.description
    }
}
```

```shell
gradle simple -q
Running simple task for project Simple project
```

A couple of interesting things happen with this small build script. Gradle reads the script file and creates a Project object. The build script configures the Project object, and finally, the set of tasks to be executed is determined and executed.

So, it is important to note that Gradle creates a Project object for us. The Project object has several properties and methods and it is available in our build scripts. We can use the project variable name to reference the Project object, but we can also leave out this variable name to reference properties and methods of the Project object.

We used the explicit project variable name and Groovy property assignment syntax. The following build script uses a different syntax, which is a bit more like Java, to get the same result

```groovy
project.setDescription('Simple project')

project.getTasks().create('simple') {
    println 'Running simple task for project ' + project.description
}
```

Here, we use the Java syntax to set and get the value of the description property of the Project object.

## Defining tasks

A project has one or more tasks to execute some actions, so a task is made up of actions. These actions are executed when the task is executed. **Gradle supports several ways to add actions to our tasks.** In this section, we discuss about the different ways to add actions to a task.

We can use the ``doFirst`` and ``doLast`` methods to add actions to our task.

```groovy
task first {
    doFirst {
        println 'Running first'
    }
}
task second {
    doLast { Task task ->
        println "Running ${task.name}"
    }
}
```

```shell
$ gradle first second
Starting a Gradle Daemon (subsequent builds will be faster)

> Task :first
Running first

> Task :second
Running second
```

For the second task, we add the action to print text with the doLast method. The method accepts a closure as an argument. The task object is passed to the closure as a parameter. This means that we can use the task object in our actions. In the sample build file, we get the value for the name property of task and print it to the console.

Maybe it is a good time to look more closely at closures as they are an important part of Groovy and are used throughout Gradle build scripts. **Closures** are basically reusable pieces of code that **can be assigned to a variable or passed to a method**. A closure is defined by enclosing the piece of code with curly brackets ``({... })``. We can **pass one or more parameters to the closures**. **If the closure has only one argument, an implicit parameter, ``it``, can be used to reference the parameter value.** We could have written the second task as follows, and the result would still be the same:

```groovy
task second {
    doLast {
    // Using implicit 'it' closure parameter. The type of 'it' is a Gradle task.
        println "Running ${it.name}"
    }
}
```

We can also define a name for the parameter and use this name in the code. This is what we did for the second task.

```groovy
task second {
    doLast { Task task ->
        // Using explicit name 'task' as closure parameter.
        // We also defined the type of the parameter.
        // This can help the IDE to add code completion.
        println "Running ${task.name}"
    }
}
```

## Defining actions with the Action interface

Besides using closures to add actions to a task, we can also follows a more verbose way of passing an implementation class of the ``org.gradle.api.Action`` interface. The Action interface has one method: execute. This method is invoked when the task is executed.

```groovy
task first {
    doFirst(
            new Action() {
                void execute(O task) {
                    println "Running ${task.name}"
                }
            }
    )
}
```
*Note.*
Does not work. However, if I add generic type, then it works:
```groovy
task first {
    doFirst(
            new Action<Task>() {
                void execute(Task task) {
                    println "Running ${task.name}"
                }
            }
    )
}
```

## Build scripts are Groovy code

We already saw the use of so-called Groovy ``GString`` in our sample script. The ``GString`` object is defined as a String with double quotes and can contain references to variables defined in a ``${... }`` section. The variable reference is resolved when we get the value of the ``GString``.

However, other great Groovy constructs can also be used in Gradle scripts. The following sample script shows some of these constructs:

```groovy
task numbers {
    doLast {
        (1..4).each { number ->
            def squared = number * number;
            println "Square of ${number} = ${squared}"
        }
    }
}

task list {
    doFirst {
        def list = ['Groovy', 'Gradle']
        println list.collect { it.toLowerCase() }.join('&')
    }
}
```

## Defining dependencies between tasks

Until now, we have defined tasks independent of each other. However, in our projects, we need dependencies between tasks. For example, a task to package compiled class files is dependent on the task to compile the class files. The build system should then run the compile task first, and when the task is finished, the package task must be executed.

In Gradle, we can add task dependencies with the dependsOn method for a task. **We can specify a task name as the String value or task object as the argument.** We can even specify more than one task name or object to specify multiple task dependencies.

```groovy
task first {
    doLast { task ->
        println "Run ${task.name}"
    }
}

task second {
    doLast { task ->
        println "Run ${task.name}"
    }
}

second.dependsOn 'first'
```

```shell
$ gradle -q second
Run first
Run second
```

Note that we define the dependency of the second task on the first task, in the last line. When we run the script, we see that the first task is executed before the second task.

Another way of defining the dependency between tasks is to set the ``dependsOn`` property instead of using the ``dependsOn`` method. There is a subtle difference, Gradle just offers several ways to achieve the same result. In the following piece of code, we use the property to define the dependency of the second task. For the third task, we immediately define the property when we define the task:

```groovy
task first {
    doLast { task ->
        println "Run ${task.name}"
    }
}

task second {
    doLast { task ->
        println "Run ${task.name}"
    }
}

second.dependsOn = ['first']

task third (dependsOn: 'second') {
    doLast{ task ->
        println "Run ${task.name}"
    }
}
```

```shell
$ gradle -q third

Run first
Run second
Run third
```

The dependency between tasks is lazy. We can define a dependency on a task that is defined later in the build script. Gradle will set up all task dependencies during the configuration phase and not during the execution phase.

It is important to take a good look at your build scripts and see if things can be organized better and if the code can be reused instead of repeated. Even our simple build script can be rewritten as follows:

```groovy
def printTaskName = { task -> println "Run ${task.name}"}

// We use the variable with the closure.
task third(dependsOn: 'second') << printTaskName
task second(dependsOn: 'first') << printTaskName
task first << printTaskName
```

*Note.*

Because ``<<`` is not supported anymore, one should do like so:
```groovy
def printTaskName = { task -> println "Run ${task.name}" }

task first {
    doLast printTaskName
}

task second {
    doLast printTaskName
}

second.dependsOn = ['first']

task third(dependsOn: 'second') {
    doLast printTaskName
}
```

```groovy
def printTaskName = { task -> println "Run ${task.name}" }

task third {
    doLast (
            new Action<Task>() {
                void execute(Task task) {
                    printTaskName.call(task)
                }
            }
    )
}
```

## Defining dependencies via tasks

In our build scripts, we defined the task dependencies using the task name. However, there are more ways to define a task dependency. We can use the task object instead of the task name to define a task dependency:

```groovy
def printTaskName = { task -> println "Run ${task.name}"}

task first {
    doLast printTaskName
}

// Here we use first (not the string value 'first') as a value for dependsOn.
task second(dependsOn: first) {doLast printTaskName}
```

## Defining dependencies via closures

We can also use a closure to define the task dependencies. The closure must return a single task name or object, or a collection of task names or task objects. Using this technique, we can really fine-tune the dependencies for our task. For example, in the following build script, we define a dependency for the second task on all tasks in the project with task names that have the letter f in the task name:

```groovy
def printTaskName = { task -> println "Run ${task.name}"}

task second {
    doLast printTaskName
}

second.dependsOn {
    project.tasks.findAll { task ->
        task.name.contains 'f'
    }
}

task first {
    doLast printTaskName
}

task beforeSecond {
    doLast printTaskName
}
```

## Setting default tasks

To execute a task, we use the task name on the command line when we run gradle. So, if our build script contains a task with the first name, we can run the task with the following command:

```shell
$ gradle first
```

However, we can also define a default task or multiple default tasks that need to be executed, even if we don't explicitly set the task name. So, **if we run the gradle command without arguments, the default task of our build script will be executed.**

To set the default task or tasks, we use the defaultTasks method. We pass the names of the tasks that need to be executed to the method. In the following build script, we make the first and second tasks the default tasks:

```groovy
defaultTasks 'first', 'second'

task first {
    doLast {
        println "I am first"
    }
}
task second {
    doFirst {
        println "I am second"
    }
}
```

```shell
$ gradle

> Task :first
I am first

> Task :second
I am second
```

## Organizing tasks

In Chapter 1, Starting with Gradle, we already discussed that we could use the tasks task of Gradle to see the tasks that are available for a build. Let's suppose we have the following simple build script:

```groovy
defaultTasks 'second'

task first {
    doLast {
        println "I am first"
    }
}

task second(dependsOn: first) {
    doLast {
        println "I am second"
    }
}
```

When we run the tasks task on the command line, we get the following output:

```shell
$ gradle -q tasks
------------------------------------------------------------
All tasks runnable from root project
------------------------------------------------------------
Default tasks: second
Build Setup tasks
-----------------
init - Initializes a new Gradle build. [incubating]
wrapper - Generates Gradle wrapper files. [incubating]
Help tasks
----------
components - Displays the components produced by root project 'organize'.
[incubating]
dependencies - Displays all dependencies declared in root project
'organize'.
dependencyInsight - Displays the insight into a specific dependency in
root project 'organize'.
help - Displays a help message.
model - Displays the configuration model of root project 'organize'.
[incubating]
projects - Displays the sub-projects of root project 'organize'.
properties - Displays the properties of root project 'organize'.
tasks - Displays the tasks runnable from root project 'organize'.
Other tasks
-----------
second
To see all tasks and more detail, run gradle tasks --all
To see more detail about a task, run gradle help --task <task>
```

We see our task with the name second in the section Other tasks, but not the task with the name first. To see all tasks, including the tasks other tasks depend on, we must add the option --all to the tasks command:

```shell
$ gradle tasks --all
...
Other tasks
-----------
second
  first
```

*Note.*

I do not observe any of this. My tasks are only visible with added --all flag. If no flag, then none of the tasks are visible. Also, no indentation. 

## Adding a description to tasks

To describe our task, we can set the description property of a task. The value of the description property is used by the task of Gradle. Let's add a description to our two tasks, as follows:

```groovy
defaultTasks 'second'

// Use description property to set description.
task first(description: 'Base task') {
    doLast {
        println "I am first"
    }
}
task second(
        dependsOn: first,
        description: 'Secondary task') {
    doLast {
        println "I am second"
    }
}
```

```shell
$ gradle tasks --all

...
first - Base task
second - Secondary task
```

## Grouping tasks together

With Gradle, we can also group tasks together in so-called **task groups**. A **task group is a set of tasks that belong together, logically.** The task group is used, for example, in the output of the tasks task that we used earlier. Let's expand our sample build script by grouping the two tasks together in a sample task group.

```groovy
defaultTasks 'second'

// Define name of the task group we want to use
def taskGroup = 'base'

task first (
        description: 'Base task',
        group: taskGroup) {
    doLast {
        println 'I am first'
    }
}

task second (
        dependsOn: first,
        description: 'Secondary task',
        group: taskGroup) {
    doLast {
        println 'I am second'
    }
}
```

Next time when we run the tasks task, we can see our tasks grouped together in a Base tasks section:
```shell
$ gradle -q tasks

------------------------------------------------------------
Tasks runnable from root project 'GradleStuff'
------------------------------------------------------------

Default tasks: second

Base tasks
----------
first - Base task
second - Secondary task
...
```

## Getting more information about a task

Getting more information about a task We can get some more information about a task with the Gradle ``help`` task. We need to specify an extra argument for the ``help`` task: ``--task``, with the name of the task that we want more information about. Gradle will print some details about our task in the console.

```shell
$ gradle help --task second

> Task :help
Detailed task information for second

Path
     :second

Type
     Task (org.gradle.api.Task)

Description
     Secondary task

Group
     base
```

## Adding tasks in other ways

Until now, we have added tasks to our build project using the task keyword, followed by the name of the task. However, there are more ways to add tasks to our project. We can use a String value with the task name to define a new task, as follows:

```groovy
task 'simple' {
    doLast {
        task ->
            println "Running ${task.name}"
    }
}
```

```groovy
def simpleTask = 'simple'

task (simpleTask) {
    doLast {
        task ->
            println "Running ${task.name}"
    }
}
```

```groovy
def simpleTask = 'simple'

task "${simpleTask}" {
    doLast {
        task ->
            println "Running ${task.name}"
    }
}

['Dev', 'Acc', 'Prod'].each {environment ->
    task "deployTo${environment}" {
        doLast { task ->
            println "Deploying to ${environment}"
        }
    }
}
```

```shell
$ gradle -q tasks --all

...
Other tasks
-----------
deployToAcc
deployToDev
deployToProd
...
```

Another way to add a new task is through the ``tasks`` property of a project. Remember that in our build script, we have access to the ``Project`` object; either we use the project variable explicitly or we use methods and properties of the Project object implicitly, without using the project variable. The tasks property of a project is basically a container for all tasks in our project.

```groovy
def printTaskName = { task ->
    println "Running ${task.name}"
}
// Use tasks project variable to get access
// to the TaskContainer object.
// Then we use the create method of
// TaskContainer to create a new task.
project.tasks.create(name: 'first') << printTaskName
// Let Gradle resolve tasks to project variable.
tasks.create(name: 'second', dependsOn: 'first') << printTaskName
```

## Using task rules

We have seen how to add tasks dynamically to our build project. However, we can also define so-called **task rules**. These rules are very flexible and allow us to add tasks to our project based on several parameters and project properties.

Suppose, we want to add an extra task that shows the description of every task in our project. If we have a task first in our project, we want to add a descFirst task to show the description property of the first task. With task rules, we define a pattern for new tasks. In our sample, this is desc<TaskName>; it is the desc prefix, followed by the name of the existing task. The following build script shows the implementation of the task rule:

```groovy
task first(description: 'First task')

task second(description: 'Second task')

tasks.addRule("Pattern: desc<TaskName>: show description of a task.") { taskName ->
    if (taskName.startsWith('desc')) {
        // Remove 'desc' from the task name.
        def targetTaskName = taskName - 'desc'
        // Uncapitalize the task name.
        def targetTaskNameUncapitalize = targetTaskName[0].toLowerCase() + targetTaskName[1..-1]
        // Find the task in the project we search the description for.
        def targetTask = project.tasks.findByName(targetTaskNameUncapitalize)
        if (targetTask) {
            task(taskName) {
                doLast {
                    println "Description of task ${targetTask.name} -> ${targetTask.description}"
                }
            }
        }
    }
}
```

```shell
$ gradle tasks

...
Rules
-----
Pattern: desc<TaskName>: show description of a task.
...
```

So, we know we can invoke descFirst and descSecond for our project. Note that these two extra tasks are not shown in the Other tasks section, but the Rules section shows the pattern we can use.
If we execute the descFirst and descSecond tasks, we get the following output:
```shell
$ gradle descFirst descSecond
:descFirst
Description of task first -> First task
:descSecond
Description of task second -> Second task
```

## Accessing tasks as project properties

Accessing tasks as project properties Each task that we add is also available as a project property, and we can reference this property like we can reference any other property in our build script. We can, for example, invoke methods or get and set the property values of our task through the property reference. This means that we are very flexible in how we create our tasks and add behavior to the tasks. In the following script, we use the project property reference to a task to change the description property:

```groovy
// Create a simple task.
task simple {
    doLast { task ->
        println "Running ${task.name}"
    }
}
// The simple task is available as project property.
simple.description = 'Print task name'
// We can invoke methods from the Task object.
simple.doLast {
    println "Done"
}
// We can also reference the task via the project property explicitly.
project.simple.doFirst {
    println "Start"
}
```

When we run our task from the command line, we get the following output:
```shell
$ gradle -q simple
Start
Running simple
Done
```

## Adding additional properties to tasks

A task object already has several properties and methods. However, we can add any arbitrary new property to a task and use it. Gradle provides an ``ext`` namespace for the task object. We can set new properties and use them again once they are set. We can either set a property directly or use a closure to set a property with a value. In the following sample, we print the value of the message task property. The value of the property is assigned with the ```simple.ext.message = 'world'``` statement:

```groovy
// Create simple task.
task simple {
    doLast {
        println "Hello ${message}"
    }
}

// We set the value for the non-existing message property with the task extension support.
simple.ext.message = 'world'
```

```shell
$ gradle -q simple
Hello world
```

## Avoiding common pitfalls

*Note.*
To understand the difference between Configuration closure and execution closure, one has to know that Gradle has three distinct phases:
* Initialization - Gradle supports single and multi-project builds. During the initialization phase, Gradle determines which projects are going to take part in the build, and creates a Project instance for each of these projects.
* Configuration - During this phase the project objects are configured. The build scripts of all projects which are part of the build are executed.
* Execution - Gradle determines the subset of the tasks, created and configured during the configuration phase, to be executed. The subset is determined by the task name arguments passed to the gradle command and the current directory. Gradle then executes each of the selected tasks.

```groovy
task simple {
    println "This is executed during the configuration phase."
}

task two {
    doLast {
        println "This is executed during execution phase"
    }
}
```

```shell
Ė gradle simple two

> Configure project :
This is executed during the configuration phase.

> Task :two
This is executed during execution phase
```

## Skipping tasks

Sometimes, we want tasks to be excluded from a build. In certain circumstances, we just want to skip a task and continue executing other tasks.

### Using onlyIf predicates

Every task has an onlyIf method that accepts a closure as an argument. The result of the closure must be true or false. If the task must be skipped, the result of the closure must be false, otherwise the task is executed. The task object is passed as a parameter to the closure. Gradle evaluates the closure just before the task is executed.

```groovy
import static java.util.Calendar.*

task longrunning {
    // Only run this task if the closure returns true.
    onlyIf { task ->
        def now = Calendar.instance
        def weekDay = now[DAY_OF_WEEK]
        def weekDayInWeekend = weekDay in [SATURDAY, SUNDAY]
        return weekDayInWeekend
    }

    // Add an action.
    doLast {
        println "Do long running stuff"
    }
}
```

We can invoke the onlyIf method multiple times for a task. If one of the predicates returns false, the task is skipped. Besides using a closure to define the condition that determines whether the task needs to be executed or not, we can use an implementation of the ``org.gradle.api.specs.Spec`` interface. The ``Spec`` interface has one method: ``isSatisfiedBy``. We must write an implementation and return true if the task must be executed and false if we want the task to be skipped.

```groovy
// Create a new File object.

def file = new File('data.sample')

task handleFile {

    // Use Spec implementation to write a conditon for the onlyIf method.
    onlyIf(new Spec() {
        boolean isSatisfiedBy(task) {
            file.exists()
        }
    })

    doLast {
        println "Work with file ${file.name}"
    }
}
```

### Skipping tasks by throwing StopExecutionException

Another way to the skip execution of a task is to throw a ``StopExecutionException`` exception. If such an exception is thrown, the build will stop the current task and continue with the next task. We can use the ``doFirst`` method to add a precondition check for a task. In the closure, when we pass to the ``doFirst`` method, we can check for a condition and throw a ``StopExecutionException`` exception if necessary.

```groovy
// Define closure with the task actions.
def printTaskName = { task ->
    println "Running ${task.name}"
}

task first {
    doLast printTaskName
}

// Use doFirst method with closure that throws exception when task is executed during work hours.
first.doFirst {
    def today = Calendar.instance
    def workingHours = today[Calendar.HOUR_OF_DAY] in 8..17
    if (workingHours) {
        throw new StopExecutionException()
    }
}

// Create second task that depends on first task.
task second(dependsOn: 'first') {
    doLast printTaskName
}
```

```shell
$ gradle second

> Task :second
Running second
```

### Enabling and disabling tasks

We have seen how we can skip tasks with the onlyIf method or by throwing StopExecutionException. However, we can also use another method to skip a task. Every task has an ``enabled`` property. By default, the value of the property is true, which means that the task is enabled and executed. We can change the value and set it to false in order to disable the task and skip its execution.

```groovy
task listDirectory {
    def dir = new File('assemble')
// Set value for enabled task property.
    enabled = dir.exists()
// This is only executed if enabled is true.
    doLast {
        println "List directory contents: " + dir.listFiles().join(',')
    }
}
```

### Skipping from the command line

Until now, we have defined the rules to skip a task in the build file. However, we can use the ``--exclude-tasks (-x)`` command-line option if we run the build.

```groovy
def cloj = {task -> println "Hello ${task.name}"}

task one {
    doLast cloj;
}

task two {
    doLast cloj
}

task three {
    doLast cloj
}
```

```shell
$ gradle one two three

> Task :one
Hello one

> Task :two
Hello two

> Task :three
Hello three
```

```shell
$ gradle one two -x three

> Task :one
Hello one

> Task :two
Hello two
```

### Skipping tasks that are up to date

Until now, we have defined conditions that are evaluated to determine whether a task needs to be skipped or not. However, with Gradle, we can be even more flexible. Suppose, we have a task that works on a file and generates some output based on the file. For example, a compile task fits this pattern. In the following sample build file, we have the convert task that will take an XML file, parse the contents, and write data to a text file, as shown in the following code:

```groovy
task convert {
    def source = new File('source.xml')
    def output = new File('output.txt')
    doLast {
        def xml = new XmlSlurper().parse(source)
        output.withPrintWriter { writer ->
            xml.person.each { person -> writer.println "${person.name},${person.email}"}
        }
        println "Converted ${source.name} to ${output.name}"
    }
}
```

We can run this task a couple of times. Each time, the data is read from the XML file and written to the text file:
```shell
$ gradle convert
:convert
Converted source.xml to output.txt
BUILD SUCCESSFUL
Total time: 0.592 secs
$ gradle convert
:convert
Converted source.xml to output.txt
BUILD SUCCESSFUL
Total time: 0.592 secs
```

However, our input file hasn't changed between the task invocations, so the task doesn't have to be executed. We want the task to be executed only if the source file has changed, or the output file is missing, or has changed since the last run of the task.

Gradle supports this pattern, this support is known as **incremental build support**. **A task only needs to be executed if necessary**. This is a very powerful feature of Gradle. It will really speed up a build process as only the tasks that need to be executed are executed.

We need to change the definition of our task so that Gradle can determine whether the task needs to be executed based on changes in the input file or output file of the task. **A task has the properties ``inputs`` and ``outputs`` that are used for this purpose.** To define an input file, we invoke the file method of the inputs property with the value of our input file. We set the output file by invoking the file method of the outputs property.

```groovy
task convert {
    def source = new File('source.xml')
    def output = new File('output.txt')

    inputs.file source

    outputs.file output

    doLast {
        def xml = new XmlSlurper().parse(source)
        output.withPrintWriter { writer ->
            xml.person.each { person -> writer.println "${person.name},${person.email}"}
        }
        println "Converted ${source.name} to ${output.name}"
    }
}
```

```shell
$ gradle convert

> Task :convert
Converted source.xml to output.txt

BUILD SUCCESSFUL in 7s
1 actionable task: 1 executed
$ gradle convert

BUILD SUCCESSFUL in 7s
1 actionable task: 1 up-to-date
```

We can use the ``--rerun-tasks`` command-line option to ignore the incremental build feature.

We have defined a single file for the inputs and outputs properties. However, Gradle supports more ways to define values for these properties. The **inputs** property has methods to add a directory, multiple files, or even properties to be watched for changes. The **outputs** property has methods to add a directory or multiple files to be monitored for changes. If these methods are not appropriate for our build, we can even use the upToDateWhen method for the outputs property. We pass a closure or implementation of the ``org.gradle.api.specs.Spec`` interface to define a predicate that determines whether the output of the task is up to date.

```groovy
project.version = '1.0'
task createVersionDir {
    def outputDir = new File('output')
    
    // If project.version changes then thetask is no longer up-to-date
    inputs.property 'version', project.version
    outputs.dir outputDir
    doLast {
        println "Making directory ${outputDir.name}"
        mkdir outputDir
    }
}

task convertFiles {
    // Define multiple files to be checked as inputs. Or use inputs.dir 'input' to check a complete directory.
    inputs.files 'input/input1.xml', 'input/input2.xml'

    // Use upToDateWhen method to define predicate.
    outputs.upToDateWhen {
    // If output directory contains any file which name starts with output and has the xml extension, then the task is up-to-date.
    // We use the Groovy method any to check if at least one file applies to the condition. The ==~ syntax is a Groovy shortcut to
    // check if a regular expression is true.
        new File('output').listFiles() any { it.name ==~ /output.*\.xml$/ }
    }
    doLast {
        println "Running convertFiles"
    }
}
```

## Summary


