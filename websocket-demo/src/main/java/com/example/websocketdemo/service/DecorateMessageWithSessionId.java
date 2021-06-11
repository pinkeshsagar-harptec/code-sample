package com.example.websocketdemo.service;

import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.websocket.ServerWebSocketContainer;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.util.List;
import java.util.stream.Collectors;

public class DecorateMessageWithSessionId extends AbstractMessageSplitter {

    private final ServerWebSocketContainer serverWebSocketContainer;

    public DecorateMessageWithSessionId(ServerWebSocketContainer serverWebSocketContainer) {
        this.serverWebSocketContainer = serverWebSocketContainer;
    }

    @Override
    protected Object splitMessage(Message<?> message) {
        List<? extends Message<?>> collect = serverWebSocketContainer.getSessions()
                .keySet()
                .stream()
                .map(s -> {
                    return MessageBuilder.fromMessage(message)
                            .setHeader(SimpMessageHeaderAccessor.SESSION_ID_HEADER, s)
                            .build();
                })
                .collect(Collectors.toList());
        return collect;
    }
}
