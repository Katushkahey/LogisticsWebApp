package com.tsystems.logisticsProject.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import java.util.Arrays;

@Configuration
public class MessagingConfig {

    private static final String DEFAULT_BROKER_URL = "tcp://localhost:61616";
    private static final String TOPIC = "ActiveMQ.Advisory.Producer.Topic.LogisticsWebApp.update";

    @Bean
    public ActiveMQConnectionFactory getConnectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(DEFAULT_BROKER_URL);
        connectionFactory.setTrustedPackages(Arrays.asList("com.tsystems.logisticsProject"));
        return connectionFactory;
    }

    @Bean
    public JmsTemplate getJmsTemplate() {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(getConnectionFactory());
        template.setPubSubDomain(true);
        template.setDefaultDestinationName(TOPIC);
        return template;
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return  objectMapper;
    }

}