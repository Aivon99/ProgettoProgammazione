package com.example;

import javafx.application.Application;
import javafx.stage.Stage;
 
public class LoginApp extends Application {
 
    @Override
    public void start(Stage primaryStage) {
        LoginPage loginPage = new LoginPage();
        loginPage.show(primaryStage);
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}
