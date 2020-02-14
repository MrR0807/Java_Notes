# Using Spring AMQP

The central component for managing a connection to the RabbitMQ broker is the ```ConnectionFactory``` interface. The responsibility of a ```ConnectionFactory``` implementation is to provide an instance of ```org.springframework.amqp.rabbit.connection.Connection```, which is a wrapper for ```com.rabbitmq.client.Connection```.

```ConnectionFactory``` implementations:
* ```CachingConnectionFactory```
* ```LocalizedQueueConnectionFactory```
* ```SimpleRoutingConnectionFactory```
* ```SingleConnectionFactory```

### CachingConnectionFactory

By default, establishes a single connection proxy that can be shared by the application. Sharing of the connection is possible since the “unit of work” for messaging with AMQP is actually a “channel”. The ```CachingConnectionFactory``` implementation supports caching of those channels, and it maintains separate caches for channels based on whether they are transactional.

### It is best not to use cache mode CONNECTION.
For reason please look at [documentation:4.1.2. Connection and Resource Management](https://docs.spring.io/spring-amqp/docs/current/reference/html/).

It is important to understand that the cache size is (by default) not a limit but is merely the number of channels that can be cached. With a cache size of, say, 10, any number of channels can actually be in use. If more than 10 channels are being used and they are all returned to the cache, 10 go in the cache. The remainder are physically closed.

### You should monitor the channels in use through the RabbitMQ Admin UI and consider increasing the cache size further if you see many channels being created and closed.

**!NOTE**. Channels used within the framework (for example, RabbitTemplate) are reliably returned to the cache. If you create channels outside of the framework, (for example, by accessing the connections directly and invoking createChannel()), you must return them (by closing) reliably, perhaps in a finally block, to avoid running out of channels.

### LocalizedQueueConnectionFactory
# TODO
### SimpleRoutingConnectionFactory
# TODO
### SingleConnectionFactory 

Is available only in the unit test code of the framework. It is simpler than CachingConnectionFactory, since it does not cache channels, but it is not intended for practical usage outside of simple tests due to its lack of performance and resilience.

### Naming Connections.

### Blocked Connections and Resource Constraints

Setup RabbitMQ 

Start RabbitMQ container:
# TODO
Join running container:
```
docker exec -it <container name> bash
```
Set memory threshold via rabbitmqctl (The default value of 0.4 stands for 40% of availalbe (detected) RAM or 40% of available virtual address space, whichever is smaller):
```
rabbitmqctl set_vm_memory_high_watermark absolute <memory_limit>
```
When using the absolute mode, it is possible to use one of the following memory units:
* M, MiB for mebibytes
* MB for megabytes
* G, GiB for gibibytes
* GB for gigabytes

Set to a small number, so RabbitMQ would not accept connections:
```
rabbitmqctl set_vm_memory_high_watermark absolute "80MB"
```

Or when the threshold or absolute limit is set to 0, it makes the memory alarm go off immediately and thus eventually blocks all publishing connections:
```
rabbitmqctl set_vm_memory_high_watermark 0
```


























