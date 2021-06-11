package com.example.websocketdemo.controller;

import com.example.websocketdemo.service.WebSocketPublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ws")
public class WSController {

    @Autowired
    private WebSocketPublisherService webSocketPublisherService;

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody Request request) {
        webSocketPublisherService.startPublishing(request.getTopic());
        return ResponseEntity.ok("Successfully publishing on " + request.getTopic());
    }
}
