package com.tsystems.logisticsProject.event;

import org.springframework.beans.factory.annotation.Autowired;
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
    public void processEntityEvent(UpdateEvent event) {
//        LOGGER.debug("###LOGIWEB### entity update ({}) detected and message being sent", event);
        messageSender.sendMessage();
    }
}
