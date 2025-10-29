package com.redhat;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class AmqpRoutes extends RouteBuilder {

    @ConfigProperty(name = "greeting.message")
    String message;

    @Override
    public void configure() throws Exception {

        from("master:ns:timer:bar").id("timer-consumer-route").setBody(constant(message))
                .to("my-amqp:queue:SCIENCEQUEUE").log("Message sent from route ${routeId} to SCIENCEQUEUE");

        from("master:ns:my-amqp:queue:SCIENCEQUEUE?receiveTimeout=10000").id("amqp-consumer-route").id("consumer-route")
                .to("log:MyLogger?showBody=true");
    }
}
