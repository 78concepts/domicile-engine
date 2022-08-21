package com.concepts78.domicile.engine.handlers;

import org.springframework.messaging.Message;

public interface MessageHandler {
    void handle(Message message);
}
