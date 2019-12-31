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

