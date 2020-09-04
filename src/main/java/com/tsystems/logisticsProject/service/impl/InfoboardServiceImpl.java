package com.tsystems.logisticsProject.service.impl;

import com.tsystems.logisticsProject.event.MessageSender;
import com.tsystems.logisticsProject.service.InfoboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfoboardServiceImpl implements InfoboardService {

    private final MessageSender messageSender;

    @Autowired
    public InfoboardServiceImpl(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public void updateInfoboard() {
        messageSender.sendMessage("updated");
    }

}
