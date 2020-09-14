package com.tsystems.logisticsProject.messageSender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

@Component
public class MessageSenderImpl implements MessageSender {

    private final JmsTemplate jmsTemplate;
    private static final String DEFAULT_MESSAGE = "update";

    @Autowired
    public MessageSenderImpl(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void send() {
        jmsTemplate.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(DEFAULT_MESSAGE);
            }
        });
    }
}
