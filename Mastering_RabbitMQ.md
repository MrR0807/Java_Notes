# Chapter 1. Getting Started

## Message brokers and message queue
### Message brokers

A Message Broker is an architectural pattern that can receive messages from multiple destinations, determine the correct destination, and route the message along the correct route. Message Brokers are centralized, in the architectural sense, to control and manage all messages. Therefore, all of the incoming and outgoing messages are sent through Message Brokers.

### Message Queues

A Message Queue is, briefly, a queue for messaging. Queue is the basic data structure behind the functioning of a Message Queue. Message Queue operations are similar to Queue data structure operations, such as the enqueue and dequeu operations. An enqueue operation leads to adding an element to the back of the queue. A dequeue operation leads to the deletion of an element from the front of the queue.

RabbitMQ uses **Advanced Message Queuing Protocol (AMQP)** that determines the policies of the Message Queues.

## An introduction to the advanced message queue protocol

AMQP defines the messaging properties, queue properties, how messages are routed between applications and clients, how Message Brokers ensure that the message is received or sent, and other concerns such as reliability and security.

# Chapter 2. Configuring RabbitMQ
## Overall configuration of RabbitMQ

RabbitMQ has three configuration ways:
* **Environment variables:** These are specified in the networking parameters and file locations
* **Configuration file:** This expresses the server component settings for permissions, limits, plugins, and clusters
* **Runtime parameters:** These define the cluster settings that would change at run time

In Unix-based systems, you can find the configuration file in the following folder:
```
/etc/rabbitmq/rabbitmq.config
```

## The RabbitMQ environment variables

In Unix-based operating systems, we can change the environment variables rather easily using the rabbitmq-env.conf file. In the environment configuration file, we can add the environment parameters as follows:
```
CONFIG_FILE=/etc/rabbitmq/testfile
```
After changing the rabbitmq-env.conf file, we have to restart the RabbitMQ server to reload the environment variables.

### Common environment variables

| Name | Default Value | Description |
| --- | --- | --- |
| RABBITMQ_BASE | * | This is the directory in which RabbitMQ server's database and log files are located. |
| RABBITMQ_CONFIG_FILE | * | This is the name of configuration file. The name doesn't consist of the extension ".config". |
| RABBITMQ_CONSOLE_LOG | | This variable can have one of the two values: "new" or "reuse". These variables are used to decide the console log file whether create a new log file or reuse the old log file. If these variables are not set, the console output will not be saved.|
| RABBITMQ_LOGS | * | This is the directory of the RabbitMQ log file. |
| RABBITMQ_LOG_BASE | * | This is the base directory that holds the log files. If RABBITMQ_LOGS or RABBITMQ_SASL_LOGS is set, then this variable has no effect on configuration. |
| RABBITMQ_MNESIA_BASE | * | This expresses the base location of the Mnesia databases files. If RABBITMQ_MNESIA_DIR is set, then this variable has no effect on configuration. |
| RABBITMQ_MNESIA_DIR | * | This variable specifies the location of Mnesia database files. |
| RABBITMQ_NODE_IP_ADDRESS | The empty string means that this binds to all network interfaces. | This is the binding address. You should change this attribute when you'd like to bind to a single network interface. |
| RABBITMQ_NODENAME | On Unix: rabbit@hostname | This is the node name of RabbitMQ server. This should be unique per Erlang node and machine combination. |
| RABBITMQ_NODE_PORT | 5672 | This is the binding port of RabbitMQ server. |
| RABBITMQ_PLUGINS_DIR | * | The location where plugins of RabbitMQ server are located. |
| RABBITMQ_SASL_LOGS | * | This is the location of RabbitMQ server's System Application Support Libraries' log files. |
| RABBITMQ_SERVICENAME | On Unix: rabbitmq-server | This variable specifies the service name that is installed on the service system of operating system. |
| RABBITMQ_SERVER_START_ARGS | None | Erlang parameters are used for the erl command when invoking the RabbitMQ server. This variable will not override RABBITMQ_SERVER_ERL_ARGS. |

### Unix-specific default location

Default locations of environment variables for Unix:

| Name | Location |
| --- | --- |
| RABBITMQ_BASE | This variable is not used for Unix |
| RABBITMQ_CONFIG_FILE | ${install_prefix}/etc/rabbitmq/rabbitmq |
| RABBITMQ_LOGS | $RABBITMQ_LOG_BASE/$RABBITMQ_NODENAME.log |
| RABBITMQ_LOG_BASE | ${install_prefix}/var/log/rabbitmq |
| RABBITMQ_MNESIA_BASE | ${install_prefix}/var/lib/rabbitmq/mnesia |
| RABBITMQ_MNESIA_DIR | $RABBITMQ_MNESIA_BASE/$RABBITMQ_NODENAME |
| RABBITMQ_PLUGINS_DIR | $RABBITMQ_HOME/plugins |
| RABBITMQ_SASL_LOGS | $RABBITMQ_LOG_BASE/$RABBITMQ_NODENAME-sasl.log |

## The configuration file

**The RabbitMQ environment variables mostly gives the control of location of files and directories, whereas the RabbitMQ configuration file gives the control of the engine, such as authentication, performance, memory limit, disc limit, exchanges, queues, bindings, and so on.** The configuration file is by default located in /etc/rabbitmq/rabbitmq.config for Unix-based computers.

Important variables with given default values:

| Variable Name | Description |
| --- | --- | 
| auth_mechanisms | This variable specifies the SASL authentication mechanisms. Default value: ['PLAIN', 'AMQPLAIN'] |
| auth_backends | This variable specifies the authentication databases to use in SASL. Other databases would be used with this plugin support. Default value: [rabbit_auth_backend_internal] |
| collect_statistics | This variable specifies the statistics collection mode. Default value: none Possible values: none; coarse; fine. |
| collect_statistics_interval | This variable specifies the statistics collection interval in miliseconds. Default value: 5000 |
| default_pass | This variable specifies the default password for the RabbitMQ server to create a user in a scratched database. Default value: Guest |
| default_permission | This variable specifies the default permissions of the default user. Default value: \[".\*", ".\*", ".\*"\] |
| default_user | This variable specifies the default username for the RabbitMQ server to create a user in a scratched database. |
| disk_free_limit | This variable specifies the disk's free space limit of the partition on which RabbitMQ has stored the data. If available disk space is lower than the disk free limit, then flow control is triggered. Moreover, the value should be related to the memory size. Default value: 50000000 |
| heartbeat | This variable specifies the heartbeat delay in seconds. Default value: 580. Possible values: 0 means heartbeats are disabled |
| hipe_compile | This variable specifies whether precompile parts of RabbitMQ with the high performance Erlang compiler or not. This variable directly affects the performance of the message rate. Hipe is supported only on Unix-based machines. Default value: False |
| log_levels | This variable specifies the granularity of logging. Default value: \[{connection, info}\] Possible values: none; error; warning; info. |
| msg_store_file_size_limit | This variable specifies the file size limit of storing each message. Default value: 16777216 |
| tcp_listeners | This variable specifies the ports that listen for AMQP connections without SSL. This variable may contain integers like 5672 that describes only the port and dictionary structure that describes both the IP and the port, for example, {"127.0.0.1", 5672}. Default value: \[5672\]
| tcp_listen_options | This variable specifies the socket options. Default value: \[binary, {packet, raw}, {reuseaddr, true}, {backlog, 128}, {nodelay, true}, {exit_on_close, false}\] | 
| server_properties | This variable specifies the key-value pairs that is to announce to clients on starting connection. Default Value: \[\] |
| ssl_listeners | This variable specifies the ports that listen for AMQP connections with SSL. This variable may contain integers like 5672 that describes only the port and dictionary structure that describes both the ip and port, such as, {"127.0.0.1", 5672}. Default value: \[\] |
| ssl_options | This variable specifies the configuration for the SSL type. Default value: \[\] |
| reverse_dns_lookup | This variable specifies whether RabbitMQ performs a reverse DNS lookup on client connections or not. Default value: False | 
| vm_memory_high_watermark | This variable specifies the memory threshold. Default value: 0.4, that is, 4/10 |

## Runtime parameters

To find out runtime parameters, run:
```
rabbitmqctl help
```

# Chapter 3. Architecture and Messaging

## Messaging and its use cases

Messaging is simply defined as communication between the message producer and the consumer of the message. **Message broker** is defined as a module that controls messaging flow. Controlling action isn't that simple, so message brokers needs lots of skills to accomplish this messaging functionality.

### Coupling of the software systems

Architectural coupling can be solved with message brokers. We need to create an abstraction between modules for messaging issue.

## Enterprise messaging

In enterprise messaging, we have to guarantee that the message is sent and received, since each of the messages is very important for our system's robustness. Message Brokers have a functionality to store all messages permanently to satisfy this kind of requirement.

## Messaging-related software architectures

### Message oriented middleware – Architecture

Message Oriented Middleware is simply defined as a component that allows software components, which have been placed on the same or different network, to communicate with one another. In a Producer/Consumer pattern, producers send their message to different consumers with the help of Message Oriented Middleware, **guaranteeing the message received**.

### Event-driven architecture

In an asynchronous system, operations take place independent of other operations; therefore, operations can take place without waiting for others. Since Message Broker's support asynchronous operations, they can be easily used in an Event Driven Architecture (EDA).
EDA is a push-based communication between producer and consumer. The structure of EDA consists of four elements:
* Event creator is just the source of event
* Event consumer is a listener of event that needs to know the event has occurred
* Event manager is a middleware between creator and consumer, which is the controller of the events and triggers the related event consumers
* Event is an action that is detected by a Event listener or consumer

## Messaging concepts

We have:
* **Producers** - who are responsible for creation of messages; 
* **Message Brokers** - who are responsible for ensuring the message sending from Producer to Consumer; 
* **Consumers** - who are responsible for receiving the messages; 
* **Messages** - who are the entity that will be sent and received. Messages have headers, which have information about the sender, receiver, and message format. Moreover, messages have bodies, which have the exact information that producers send to the consumers. Message bodies could be in different types of formats such as XML, JSON, binary data, and so on.

## Advanced Message Queuing Protocol (AMQP)

### AMQ elements

AMQ stands for Advanced Message Queuing. We can express the main architecture of the middleware as follows: producer/publisher creates or sends messages; then, messages arrive at Exchanges; after that, messages are routed through the Message Queues with related Bindings to the right consumer. So, we have four model elements:
* **Message Flow** - It explains the message life cycle
* **Exchanges** - It accepts messages from publisher, and then routes to the Message Queues
* **Message Queues** - It stores messages in memory or disk and delivers messages to the consumers
* **Bindings** - It specifies the relationship between an exchange and a message queue that tells how to route messages to the right Message Queues

### Message flow

* **Message** - This is produced by the Publisher application using AMQP Client with placing related information such as Content, Properties, and Routing Information to the Message.
* **Exchange** - This receives the Message, which is sent from the Producer, then routes message to the right Queues, which is set on the message's Routing Information. Message will be sent to multiple queues, since it is determined with the Bindings.
* **Message Queue** - This receives the Message and adds it to their waiting list. As soon as possible, Message Queue sends message to the related consumer. If Message Queue cannot send the Message, it stores the Message in a disk or memory.
* **Consumer** - This receives the Message and sends Acknowledgement Message (usually it is sent automatically) to the Publisher.

### Exchanges in AMQ

**Exchanges generally take message and route it into zero or more message queues.** The routing algorithm can be determined with the bindings. Exchanges are declared with following important properties:
* **Name** - Usually, server gives its name automatically
* **Durable** - Message Queue remains present or not, depending on whether durable is set or transient is set
* **Auto-delete** - When all queues finish, exchanges are deleted automatically

### Message queues

They store the messages in a **First-In-First-Out (FIFO)** way that is well defined in the queue data structure. Different from Queue data structure, if multiple readers from a queue is active, then one of the reader sometimes has a priority over another. Then, prior one takes the message before the other readers. Therefore, message queue in AMQ model is called as weak-FIFO.

Message Queues have the properties like Exchanges:
* **Name** - Defines the name of Message Queue
* **Durable** - If set, the Message Queue can't lose any message
* **Exclusive** - If set, the Message Queue will be deleted after connection is closed
* **Auto-delete** - If set, the Message Queue is deleted after last consumer has unsubscribed

### Bindings

**Bindings** are rules that Exchanges use to route messages between message queues. Thus, bindings clarify in which message queue the message will be sent. The binding is determined with **routing key**.

### AMQP messages

A Message consists of these following attributes:
* Content that is a binary data
* Header
* Properties

### Exchange types

#### The direct exchange type – amq.direct

* A message queue binds to the exchange using a **routing key**, K.
* Then, a publisher sends the Exchange a message with the routing key, R.
* The message is passed to the message queue if K equals to R.

#### The fan-out exchange type – amq.fanout

* A message queue binds to the exchange with no arguments.
* Whenever a publisher sends the Exchange a message, the message is passed to the message queues unconditionally.

#### The topic exchange type – amq.topic

* A message queue binds to the Exchange using a **routing *pattern***, P.
* A publisher sends the exchange a message with the routing key, R.
* The message is passed to the message queue if R matches P.
* Matching algorithm works as follows: The routing key used for a topic exchange must consist of zero or more words delimited by dots such as "news.tech". The routing pattern works like a regular expression such as "*" matches single word and # matches zero or more words. For instance, "news.*" matches the "news.tech".

#### The headers exchange type – amq.match

Headers Exchange Type is the most powerful exchange type in AMQP. Headers exchange route messages based on the matching message headers. Exchange ignores the routing key. Whenever creating the exchanges, we specify the related headers on the exchanges, so message's headers are matched with the exchange headers using "x-match" argument.

# Chapter 4. Clustering and High Availability

RabbitMQ has great skills to handle lots of messages in a single machine, such as more than 50k messages per second according. However, there are cases when that is not enought - so we have to have multiple RabbitMQ servers.

## High reliability in RabbitMQ

### Federation in RabbitMQ

The main goal of Federation is to transmit messages between brokers without the need of clustering. The Federation plugin is available with the standard RabbitMQ server installation. You can enable the Federation plugin using the following command:
```
rabbitmq-plugins enable rabbitmq_federation
```
Moreover, if you use the management plugin of the RabbitMQ server, you have a chance to monitor the federation using the same management plugin using the following command:
```
rabbitmq-plugins enable rabbitmq_federation_management
```

Three levels of configuration are involved in federation according to the RabbitMQ website:
* Upstreams: This defines how to connect to another RabbitMQ
* Upstream sets: This sets the upstream groups
* Policies: This is a set of rules of the Federation

## Clustering in RabbitMQ

Clustering is our main solution for handling client requests over the server applications. The RabbitMQ server also gives us cluster mechanism. Cluster mechanism replicates all the data/states across all the nodes for reliability and scalability. The general structure of the clusters would be changed dynamically, according to the addition or removal of any clusters from the systems. Furthermore, RabbitMQ tolerates the failure of each node.

Nodes should choose one of the Node type that affect the storage place; these are disk nodes or RAM nodes. If an administrator chooses a RAM node, RabbitMQ stores its state in memory. However, if an administrator chooses to store its state in a disk, then RabbitMQ stores its state on both, memory and disk.

# Chapter 5. Plugins and Plugin Development
TODO

# Chapter 6. Managing Your RabbitMQ Server
TODO

# Chapter 7. Monitoring
TODO

# Chapter 8. Security in RabbitMQ
TODO

# Chapter 9. Java RabbitMQ Client Programming
TODO




























































