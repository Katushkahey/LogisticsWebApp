package com.tsystems.logisticsProject.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

    private final JmsTemplate jmsTemplate;
    private static final String DEFAULT_MESSAGE = "update";

    @Autowired
    public MessageSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendMessage() {
        jmsTemplate.send(session -> session.createTextMessage(DEFAULT_MESSAGE));
    }

    public void sendMessage(String str) {
        jmsTemplate.send(session -> session.createTextMessage(str));
    }


}
