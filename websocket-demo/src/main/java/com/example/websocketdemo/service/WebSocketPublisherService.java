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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.annotation.PostConstruct;

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

    public void startPublishing(String name,String message) {
        RegularPublisher regularPublisher = new RegularPublisher(message);
        ServerWebSocketContainer serverWebSocketContainer = new ServerWebSocketContainer(name)
                .setHandshakeHandler(handshakeHandler())
                .setAllowedOrigins("*").withSockJs();

        MethodInvokingMessageSource methodInvokingMessageSource = new MethodInvokingMessageSource();
        methodInvokingMessageSource.setObject(regularPublisher);
        methodInvokingMessageSource.setMethodName("publishEmergency");

        WebSocketOutboundMessageHandler webSocketOutboundMessageHandler = new WebSocketOutboundMessageHandler(serverWebSocketContainer);

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

    /*
    initialize websocket dynamically at start up time
     */
//    @PostConstruct
//    private void initializeWSAtStartup(){
//        startPublishing("/staticws");
//    }

}