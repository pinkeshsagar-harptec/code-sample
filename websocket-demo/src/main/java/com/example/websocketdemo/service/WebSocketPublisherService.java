package com.example.websocketdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.StandardIntegrationFlow;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.integration.endpoint.MethodInvokingMessageSource;
import org.springframework.integration.websocket.ServerWebSocketContainer;
import org.springframework.integration.websocket.outbound.WebSocketOutboundMessageHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

/**
 * Message publisher service that publishes messages every 10 seconds
 */
@Service
public class WebSocketPublisherService {

    @Autowired
    IntegrationFlowContext integrationFlowContext;

    @Bean
    public HandshakeHandler handshakeHandler() {
        return new DefaultHandshakeHandler(new TomcatRequestUpgradeStrategy());
    }

    @Autowired
    RegularPublisher regularPublisher;

    public void startPublishing(String name) {
        ServerWebSocketContainer serverWebSocketContainer = new ServerWebSocketContainer(name)
                .setHandshakeHandler(handshakeHandler())
                .setAllowedOrigins("*").withSockJs();

        MethodInvokingMessageSource methodInvokingMessageSource = new MethodInvokingMessageSource();
        methodInvokingMessageSource.setObject(regularPublisher);
        methodInvokingMessageSource.setMethodName("publishEmergency");

        WebSocketOutboundMessageHandler webSocketOutboundMessageHandler = new WebSocketOutboundMessageHandler(serverWebSocketContainer);
        webSocketOutboundMessageHandler.afterPropertiesSet();

        StandardIntegrationFlow standardIntegrationFlow = IntegrationFlows.from(methodInvokingMessageSource,
                polling -> polling.poller(pollerFactory -> pollerFactory.fixedRate(10000)))
                .split(new DecorateMessageWithSessionId(serverWebSocketContainer))
                .handle(webSocketOutboundMessageHandler)
                .get();

        IntegrationFlowContext.IntegrationFlowRegistration register = integrationFlowContext.registration(standardIntegrationFlow).autoStartup(false)
                .addBean(serverWebSocketContainer)
                .register();

        register.start();
    }
}