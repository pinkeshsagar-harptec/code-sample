package com.example.websocketdemo.client;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * WebSocket client made with ok http.
 */
public class OkHttpWebSocketClient {

    public static void main(String[] args) {
            String dynamicUrl = "ws://localhost:8080/examples";
        String notWorkingWSURL = "ws://localhost:8080/staticws";
        String workingWSURL = "ws://localhost:8080/staticws/websocket";
        String user2Connection = "ws://localhost:8080/d61d3d49-15ab-4201-8c07-7a5c4666278c/sample/websocket";
        String newConnection = "ws://localhost:8080/d61d3d49-15ab-4201-8c07-7a5c4666278b/sample/websocket";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(user2Connection).build();
        StringListener stringListener = new StringListener();
        client.newWebSocket(request, stringListener);

        client.dispatcher().executorService().shutdown();
    }
}
