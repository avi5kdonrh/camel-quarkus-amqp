package com.redhat;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import org.apache.camel.component.amqp.AMQPComponent;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.messaginghub.pooled.jms.JmsPoolConnectionFactory;

public class AMQPConfig {

    /*
     * @Produces
     * @Named("my-amqp-caching") public AMQPComponent amqpc() throws Exception { String brokerUrl =
     * System.getProperty("amqp.broker", "amqp://localhost:5672"); JmsConnectionFactory delegate = new
     * JmsConnectionFactory(brokerUrl); AMQPComponent amqpComponent = new AMQPComponent(); CachingConnectionFactory
     * connectionFactory = new CachingConnectionFactory(delegate); connectionFactory.setCacheConsumers(true);
     * connectionFactory.setClientId("caching-client"); amqpComponent.setClientId("caching-client");
     * amqpComponent.setConnectionFactory(connectionFactory);
     * System.out.println(">>>>>>>>>>> Building caching component >>"); return amqpComponent; }
     */

    @Produces
    @Named("my-amqp-producer")
    public AMQPComponent producer() throws Exception {

        String brokerUrl = System.getProperty("amqp.broker", "amqp://localhost:5672");

        JmsConnectionFactory delegate = new JmsConnectionFactory(brokerUrl);
        AMQPComponent amqpComponent = new AMQPComponent();

        JmsPoolConnectionFactory pcf = new JmsPoolConnectionFactory();
        pcf.setConnectionFactory(delegate);
        pcf.setMaxConnections(10);
        pcf.setMaxSessionsPerConnection(100);

        amqpComponent.setConnectionFactory(pcf);
        System.out.println(">>>>>>>>>>> Building pooled producer >>");
        return amqpComponent;
    }

    @Produces
    @Named("my-amqp-consumer")
    public AMQPComponent consumer() throws Exception {

        String brokerUrl = System.getProperty("amqp.broker",
                "failover:(amqp://localhost:5672)?failover.maxReconnectAttempts=20&failover.reconnectDelay=4000");

        JmsConnectionFactory connectionFactory = new JmsConnectionFactory(brokerUrl);
        connectionFactory.setClientID("myclient");
        JmsPoolConnectionFactory pcf = new JmsPoolConnectionFactory();
        pcf.setConnectionFactory(connectionFactory);
        pcf.setMaxConnections(1);
        pcf.setConnectionIdleTimeout(20000);
        pcf.setConnectionCheckInterval(10000);
        pcf.setMaxSessionsPerConnection(100);

        AMQPComponent amqpComponent = new AMQPComponent();
        amqpComponent.setConnectionFactory(pcf);
        System.out.println(">>>>>>>>>>> Building pooled consumer >>");
        return amqpComponent;
    }

}
