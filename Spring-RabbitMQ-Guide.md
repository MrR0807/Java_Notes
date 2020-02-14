# Using Spring AMQP

The central component for managing a connection to the RabbitMQ broker is the ```ConnectionFactory``` interface. The responsibility of a ```ConnectionFactory``` implementation is to provide an instance of ```org.springframework.amqp.rabbit.connection.Connection```, which is a wrapper for ```com.rabbitmq.client.Connection```.














