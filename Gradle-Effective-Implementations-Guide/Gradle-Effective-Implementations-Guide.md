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

