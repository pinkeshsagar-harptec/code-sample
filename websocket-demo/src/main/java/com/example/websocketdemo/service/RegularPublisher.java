package com.example.websocketdemo.service;

import org.springframework.stereotype.Component;

//@Component
public class RegularPublisher {

    private final String message;

    public RegularPublisher(String message) {
        this.message = message;
    }

    public String publishEmergency() {
        System.out.println("publishing message");
        return "This is Message => "+message;
    }


}
