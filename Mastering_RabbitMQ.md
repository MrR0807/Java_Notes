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

































