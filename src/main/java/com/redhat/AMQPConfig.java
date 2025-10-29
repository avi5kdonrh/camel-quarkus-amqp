package com.redhat;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import org.apache.camel.component.amqp.AMQPComponent;

public class AMQPConfig {

    @Produces
    @Named("my-amqp")
    public AMQPComponent amqp() throws Exception {
        AMQPComponent amqpComponent = new AMQPComponent();
        amqpComponent.setClientId("my-client");
        System.out.println(">>>>>>>>>>> Building component >>");
        return amqpComponent;
    }
}
