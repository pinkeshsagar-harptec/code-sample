package com.example.websocketdemo.service;

import org.springframework.stereotype.Component;

@Component
public class RegularPublisher {

    public String publishEmergency() {
        System.out.println("publishing message");
        return "This is Message from PM and it is Atma nirbhar bharat";
    }
}
