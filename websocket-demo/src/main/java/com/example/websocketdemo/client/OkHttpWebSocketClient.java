package com.example.websocketdemo.client;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * WebSocket client made with ok http.
 */
public class OkHttpWebSocketClient {

    public static void main(String[] args) {
        String url = "ws://localhost:8080/examples";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        StringListener stringListener = new StringListener();
        client.newWebSocket(request, stringListener);

        client.dispatcher().executorService().shutdown();
    }
}
