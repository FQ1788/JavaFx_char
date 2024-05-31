package com.example.javafxchar.service;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class CharService {

    public void showErr(String error) {
        Alert alert = new Alert(Alert.AlertType.NONE, error, ButtonType.OK);
        alert.showAndWait();
    }
}
