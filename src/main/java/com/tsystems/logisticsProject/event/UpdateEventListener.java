package com.tsystems.logisticsProject.event;

import com.tsystems.logisticsProject.messageSender.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class UpdateEventListener {

    //    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateEventListener.class);
    private final MessageSender messageSender;

    @Autowired
    public UpdateEventListener(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @TransactionalEventListener
    @Async
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
//        LOGGER.debug(" entity update ({}) detected and message being sent", applicationEvent);
        messageSender.send();
    }
}
