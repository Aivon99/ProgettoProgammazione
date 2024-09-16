package com.example;

import javafx.application.Application;
import javafx.stage.Stage;

public class LoginApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        LoginPage loginPage = new LoginPage();
        loginPage.show(primaryStage);
        
        // Imposta la dimensione della finestra
        primaryStage.setWidth(800);  // Imposta la larghezza della finestra
        primaryStage.setHeight(700); // Imposta l'altezza della finestra
    }

    public static void main(String[] args) {
        launch(args);
    }
}
