package com.example.javafxchar.controller;

import com.example.javafxchar.bean.Message;
import com.example.javafxchar.config.StompSessionHandlerConfig;
import javafx.application.Platform;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class StompController {

    private StompSession stompSession;

    private StompSessionHandler sessionHandler;

    private final String WS_URL;

    StompController(String wsUrl) {
        this.WS_URL = wsUrl;
        this.sessionHandler = new StompSessionHandlerConfig();
    }

    public void connection() {
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        ListenableFuture<StompSession> sessionListenableFuture = stompClient.connect(WS_URL, this.sessionHandler);

        try {
            this.stompSession = sessionListenableFuture.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.printf("連線失敗 %s", e.getMessage());
        }
    }

    /**
     * 發送訊息
     * @param sendUrl 發送目標
     * @param message 發送內容
     */
    public void sendMessage(String sendUrl, String message){
        stompSession.send(sendUrl, new Message(message));
    }

    /**
     * 監聽訊息
     */
    public <T> void subMessage(String url, Class<T> t, Consumer<T> action) {
        this.stompSession.subscribe(url, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return t;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                try {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            action.accept((T) payload);
                        }
                    });
                } catch (Exception e) {
                    System.out.printf("執行時發生異常, %s", e.getMessage());
                }
            }
        });
    }

    public void closeConnection() {
        if (stompSession.isConnected()) {
            stompSession.disconnect();
        }
    }
}
