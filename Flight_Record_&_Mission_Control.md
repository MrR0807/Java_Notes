**JFR (Java Flight Recorder)** can record a whole lot of events—from your applications to your JVM to the OS. It is a high performance, but low overhead profiler.

**MC (Mission Control)** displays the application profiling data collected by JFR in a visual environment. You can select the category you want to analyze—from class loading to JVM internals (such as garbage collection), application threads, memory allocation, to complete application data analysis.

## Using the Command Line
You can start and configure a recording from the command line using the -XX:StartFlightRecording option of the java command, when starting the application. The following example illustrates how to run the MyApp application and immediately start a 60-second recording which will be saved to a file named myrecording.jfr:

java -XX:+UnlockCommercialFeatures -XX:+FlightRecorder -XX:StartFlightRecording=duration=60s,filename=myrecording.jfr MyApp

## Using Diagnostic Command
You can also control recordings by using Java-specific diagnostic commands.

The simplest way to execute a diagnostic command is to use the **jcmd tool (located in the Java installation directory)**. To issue a command, you have to pass the process identifier of the JVM (or the name of the main class) and the actual command as arguments to jcmd. For example, to start a 60-second recording on the running Java process with the identifier 5368 and save it to myrecording.jfr in the current directory, use the following:

jcmd 5368 JFR.start duration=60s filename=myrecording.jfr
**To see a list of all running Java processes, run the *jcmd* command without any arguments**. To see a complete list of commands available to a runnning Java application, specify help as the diagnostic command after the process identifier (or the name of the main class). The commands relevant to Java Flight Recorder are:

* **JFR.start**. Start a recording.
* **JFR.check**. Check the status of all recordings running for the specified process, including the recording identification number, file name, duration, and so on.
* **JFR.stop**. Stop a recording with a specific identification number (by default, recording 1 is stopped).
* **JFR.dump**. Dump the data collected so far by the recording with a specific identification number (by default, data from recording 1 is dumped).

**Note: These commands are available only if the Java application was started with the Java Flight Recorder enabled, that is, using the following options: -XX:+FlightRecorder**

## Setting Maximum Size and Age
You can configure an explicit recording to have a maximum size or age by using the following parameters:

maxsize=size
Append the letter k or K to indicate kilobytes, m or M to indicate megabytes, g or G to indicate gigabytes, or do not specify any suffix to set the size in bytes.

maxage=age
Append the letter s to indicate seconds, m to indicate minutes, h to indicate hours, or d to indicate days.

If both a size limit and an age are specified, the data is deleted when either limit is reached.

## Setting the Delay
When scheduling a recording. you might want to add a delay before the recording is actually started; for example, when running from the command line, you might want the application to boot or reach a steady state before starting the recording. To achieve this, use the delay parameter:

delay=delay
Append the letter s to indicate seconds, m to indicate minutes, h to indicate hours, or d to indicate days.

## Setting Compression
Although the recording file format is very compact, you can compress it further by adding it to a ZIP archive. To enable compression, use the following parameter:

compress=true
Note that CPU resources are required for the compression, which can negatively impact performance.

## Creating Recordings Automatically
When running with a default recording you can configure Java Flight Recorder to automatically save the current in-memory recording data to a file whenever certain conditions occur. If a disk repository is used, the current information in the disk repository will also be included.

## Creating a Recording On Exit
To save the recording data to the specified path every time the JVM exits, start your application with the following option:

-XX:FlightRecorderOptions=defaultrecording=true,dumponexit=true,dumponexitpath=path

Set path to the location where the recording should be saved. If you specify a directory, a file with the date and time as the name is created in that directory. If you specify a file name, that name is used. If you do not specify a path, the recording will be saved in the current directory.

## Creating a Recording Using Triggers
You can use the Console in Java Mission Control to set triggers. A trigger is a rule that executes an action whenever a condition specified by the rule is true. For example, you can create a rule that triggers a flight recording to commence whenever the heap size exceeds 100 MB. Triggers in Java Mission Control can use any property exposed through a JMX MBean as the input to the rule. They can launch many other actions than just Flight Recorder dumps.

## Actions in short
* Start JVM with -XX:StartFlightRecording=,filename=hello.jfr,disk=true,dumponexit=true,settings=profile,path-to-gc-roots=true
  * Use **jcmd** to identify running application id
  * Use JFR.start, JFR.stop, JFR.dump to control recording 
  * Use Mission Control to load up the file and analyze
* -XX:StartFlightRecording=duration=60s,filename=myrecording.jfr MyApp to start recording immediately 


* jcmd 3828 JFR.start name=helloworld
* jcmd 3828 JFR.dump name=helloworld filename=helloworldfile.jfr (**dump does not stop recording!**)
* find dumped file and load to MC
