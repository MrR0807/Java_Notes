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




































