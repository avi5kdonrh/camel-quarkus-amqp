package com.redhat;

import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class AmqpRoutes extends RouteBuilder {

    @ConfigProperty(name = "greeting.message")
    String message;

    @Override
    public void configure() throws Exception {

        from("master:ns:timer:test?period=5s").setBody(constant(UUID.randomUUID().toString())).multicast()
                .to("my-amqp-producer:queue:SCIENCEQUEUE").to("my-amqp-producer:queue:SCIENCEQUEUE-New");

        from("master:ns:my-amqp-consumer:queue:SCIENCEQUEUE?receiveTimeout=10000").id("amqp-consumer")
                .to("log:MyLogger?showBody=true");

        from("master:ns:my-amqp-consumer:SCIENCEQUEUE-New?receiveTimeout=10000").id("amqp-consumer1")
                .to("log:MyLogger1?showBody=true");

    }
}
