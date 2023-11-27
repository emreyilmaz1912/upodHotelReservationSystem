package com.emreyilmaz.upodhotelreservationsystem.controllers;

import com.emreyilmaz.upodhotelreservationsystem.DBUtils;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginControler implements Initializable {
    @FXML
    private Button button_login;

    @FXML
    private TextField tf_username;
    @FXML
    private TextField tf_password;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        button_login.setOnAction(event -> {
            DBUtils.logInUser(event,tf_username.getText(),tf_password.getText());
        });
    }
}
