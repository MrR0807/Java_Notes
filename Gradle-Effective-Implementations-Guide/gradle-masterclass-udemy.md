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

We can have a list of ``dependsOn``:

```groovy
task doFinish (dependsOn: [doSomething, doSomethingElse]) {
	doLast {
		println "Hello"
	}
}
```

```groovy
task doOne {
	doLast {
		println "Do one"
	}
}

task doTwo {
	doLast {
		println "Do two"
	}
}

task doThree {
	doLast {
		println "Do three"
	}
}


doOne.dependsOn = [doTwo, doThree]
or
doOne.dependsOn doTwo, doThree
```

# Lesson 26. Filtering Tasks with dependsOn

Instead of listing task/tasks, we can use clojure. Clojure should return a Task or Collection<Task>.

```groovy
println "${project.tasks.findAll { task -> task.name.startsWith('doStep2') }}"
```

# Lesson 27. Using dependsOn in conditional logic

```groovy
doOne.dependsOn doTwo, tasks.findAll { task -> task.name.startsWith('doThree') }
```

# Lesson 28. The Task depedency graph

After configuration phase, Gradle knows all the tasks that have to be executed. And this is done by building a direct acyclic graph (you cannot have circular dependencies).

# Lesson 29. Hooking into the task Graph

We can access Gradle Task Graph via - ```logger.info "$project.gradle.taskGraph"```. The class returned by this method implements ```TaskExecutionGraph```. It has several interesting methods like ```afterTask (Closure closure)``` and ```beforeTask (Closure closure)```.

Get a Task list.
	
```groovy
project.gradle.taskGraph.whenReady {
  logger.info "$project.gradle.taskGraph.allTasks"	
}
```

```whenReady``` is required, because otherwise, ```allTasks``` property might be accessed before tasks were configured.

Having access to the task graph can give you dynamic configuration capabilities, like setting a specific property if some task is on the graph like:

```groovy
project.gradle.taskGraph.whenReady { taskGraph -> 
  if (taskGraph.hasTask(doStep2) {
	project.version = "1.0"
  else {
	project.version = "1.0-SNAPSHOT"
  }
}
```

# Lesson 30. Introduction to Plugins

Gradle core is quite minimal regarding "useful" functionality. Gradle relies on plugins like java, java-library, etc.

# Lesson 31. Applying Plugins

When java plugin is added, a lot of different tasks are added. Not only tasks, but properties and other domain specific objects. Some example of tasks:
* clean 
* compileJava
* testClasses
* jar

Java plugin also adds conviention. A good example of that is project layout (src/main/java, src/main/resources etc).

# Lesson 32. 1st Java program

# Lesson 33. Starting to use Gradle with Java

# Lesson 34. 3rd party dependencies

You can browse Project API where you'll find repositories and dependencies methods. There you can browse ```DependencyHandler``` and ```RepositoryHandler``` for more APIs.

# Lesson 35. Building our 1st Java application

# Lesson 36. Assembling our Java application

Gradle does not provide runnable Jar out of the box. We have to configure that ourselves.

jar {
    println archiveBaseName.get()
    archiveBaseName.set('hello')
    println archiveBaseName.get()
    manifest {
        attributes 'Main-Class' : 'lt.test.Hello'
    }

    println "manifest attributes: $manifest.attributes"
}

Because Manifest file is just a text file, ```attributes``` property task a simple ```Map```, hence we can include whatever we want. Furthermore, that is why in documentation there isn't any definied key values.

# Lesson 37. Assembling our Java application

Out of the box, depedencies are not included into the jar, hence we need to add them manually:

```groovy

jar {
    manifest {
        attributes 'Main-Class' : 'lt.test.Hello'
    }

    from {
        project.configurations.runtimeClasspath.collect {File file -> project.zipTree(file)}
    }
}
```

# My Notes

To get a list of possible configurations and their respective dependencies:
```groovy
task testMe {
    project.configurations.forEach(c -> { 
	println c.name
        println c.dependencies
    })
}
```

# Lesson 38. Building a Java Web application

Lesson about ``war`` generation.

```groovy

compileJava {
    sourceCompatibility = '16'
    targetCompatibility = '16'
}
```

```compileOnly``` equals to Maven's ```provided```.

There is a ``war`` plugin - ``war``.

# Lesson 39. Deploying our Java Web application using Gradle

To copy war to Tomcat, we'll use ``class Copy``. When we assign a type of class, we essentially "extend" that class (this is just my interpretation):

```groovy
task copyDocs (type: Copy) {
	from "scr/main/doc"
	into "build/target/doc"
}
```

```groovy
ext.tomcatWebapps = 'C:/software/tomcatfolder/'

task deployToTomcat (type: Copy, dependsOn: 'war') {
	from war.archivePath
	into "$tomcatWebapps"
}
```

# Lesson 40. What are transitive dependencies

Transitive dependencies - dependencies that are dependencies of your direct dependencies.

# Lesson 41. Analysing Dependencies

```shell
$ gradle -q dependencies
```

Outputs all dependencies.

To exclude transitive dependency:

```groovy

implementation ('org.springframework:spring-webmvc:4.0.3.RELEASE') {
	exclude group: 'commons-logging', module: 'commons-logging'
}
```

# Lesson 42. Dependency Reports

``project-report`` plugin helps create dependency reports.

# Lesson 43. What is a Multi-Project build?

Multi-project has a root project and then it's sub-projects. Each sub-project, can have other sub-projects. We can have also, projects depending on one another.

# Lesson 44. Our 1st Multi-Project build

If you run tasks on root-project, it will run on all sub-projects. For example, if you run ``build`` task it will build all projects. However, if you run only ``sub-project`` task, it will run only for that sub-project.

# Lesson 45. Setting-Up our Multi-Project Build

``settings.gradle`` is mandatory in multi-project build.

```groovy

subprojects {
	apply plugin: 'java'
	
	group = 'com.example.hello'
	version = '0.0.1-SNAPSHOT'
	
	sourceCompatibility = 1.8
	targetCompatibility = 1.8
	
	repositories {
		mavenCentral()
	}
}
```

# Lesson 46. Setting-Up our Project Dependencies

```groovy
# Configure specific project. Let's configure specific depedencies
project(':other') {
	dependencies {
		implementation 'org.apache.commons:commons-math3:3.2'
	}
}

project ('and-other') {
	dependencies {
		# This shows that 'and-other' depends on 'other' project
		implementation project (':other')
	}
}
```

# Lesson 47. Implementation and API Scopes

``implemenation`` scope was introduced so transitive dependencies between sub-projects do not leak. In other words if A subproject depends on B subproject, and B subproject has dependencies with ``implementation spring-whatever`` then this dependency will not be reachable for A subproject. To solve this, include ``java-library`` plugin in subproject and make transitive dependency ``api`` instead of ``implementation``. 

# Lesson 48. Introduction to a Multi-Project Web application

![image](https://user-images.githubusercontent.com/24605837/126510970-b501724f-5d70-4069-a13f-24c700c22e0c.png)

In the project, opening ```settings.gradle``` shows:

```groovy
include 'academy-utils'
include 'academy-web'
include 'academy-audit-inteface'
...

# theName is setup in gradle.properties file
rootProject.name = theName

# Renaming buildFileName for easier navigation between subproject gradle files. If you have a lot of subprojects, each will have a build.gradle, which leads
# to confusing development when you open several build.gradle files in IDE
rootProject.children.each { subproject -> subproject.buildFileName = "${subproject.name}.gradle" }
```

```gradle.properties```:

```properties
theGroup=com.denofprogramming
theName=academy
theVersion=0.0.2-SNAPSHOT
theSourceCompatibility=1.0
```

# Lesson 49. Overview of a Multi-Project Web application

# Lesson 50. Building the Multi-Project Web application

```academy-web.gradle```

```groovy
plugins {
	id 'war'
}

ext.tomcatHome = 'C:/somepath'
ext.tomcatWebapps = "$tomcatHome/webapps"

war {
	archiveName = 'academy-web.war'
}

task deployToTomcat (type: Copy, dependsOn: 'build') {
	from war
	into "$tomcatWebapps"
}

dependencies {
	implementation 'org.springframework:spring-webmvc:4.0.3.RELEASE'
	implementation 'javax.servlet:javax.servlet-api:3.0.1"
}
```

If we'd run ``gradle -q dependencies`` we'd get a list of dependencies that were not declared in given build file. These dependencies come from root project's ```gradle.build```:

```groovy

subprojects {
	
	apply plugin: 'java-library'
	
	description = 'Teaching Gradle''
	group = theGroup
	version = theVersion
	
	sourceCompatibility = theSourceCompatibility
	targetCompatibility = theSourceCompatibility
	
	repositories {
		mavenCentral()
	}
	
	dependencies {
		testImplementation 'somedependency'
	}
}


project(':academy-audit') {
	dependencies {
		api project(':academy-audit-interface')
	}
}

... and so on ...
```


# Lesson 51. FindBugs with Gradle

```apply plugin: 'findbugs'```























