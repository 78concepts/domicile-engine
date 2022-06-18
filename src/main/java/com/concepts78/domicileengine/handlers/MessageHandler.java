package com.concepts78.domicileengine.handlers;

import org.springframework.messaging.Message;

public interface MessageHandler {
    void handle(Message message);
}
