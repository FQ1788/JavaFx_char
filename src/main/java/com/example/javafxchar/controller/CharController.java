package com.example.javafxchar.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

import java.util.Map;

public class CharController {
    @FXML
    private VBox msgLayout;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextArea messageArea;

    @FXML
    private TabPane tabPane;

    private StompController stompController;

    @FXML
    public void initialize() {
        msgLayout.heightProperty().addListener(i -> scrollPane.setVvalue(1.0));
        messageArea.setOnKeyPressed(event -> {
            if (KeyCode.ENTER.equals(event.getCode())) {
                if (event.isShiftDown()) {
                    messageArea.appendText("\n");
                } else {
                    sendMessage();
                }
            }
        });
    }

    /**
     * 加入聊天室
     */
    public void connectionChar() {
        checkAndClose();
        stompController = new StompController("ws://172.20.27.74:8033/ws");
        stompController.connection();
        stompController.subMessage("/chat/out", Map.class, i -> msgLayout.getChildren().add(new Label(i.get("content").toString())));
        tabPane.getSelectionModel().select(1);
    }

    public void checkConnect() {
        if (this.stompController == null) {
            Alert alert = new Alert(Alert.AlertType.NONE, "請先加入聊天室!", ButtonType.OK);
            alert.setTitle("警告!!");
            alert.showAndWait();
            tabPane.getSelectionModel().select(0);
        }
    }

    /**
     * 傳送訊息
     */
    public void sendMessage() {
        stompController.sendMessage("/app/chatIn", messageArea.getText());
        messageArea.clear();
    }

    /**
     * 檢查並結束連線
     */
    private void checkAndClose() {
        if (stompController != null) {
            stompController.closeConnection();
        }
    }
}
