package com.example;
 
import java.util.List;
 
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
 
public class LoginPage {
 
    private AccountManager accountManager = new AccountManager();
 
    public void show(Stage primaryStage) {
        primaryStage.setTitle("Pagina di Login");
 
        // Creazione dei controlli
        Label welcomeLabel = new Label("Benvenuto nel Gioco!");
        welcomeLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 30));
        welcomeLabel.setTextFill(Color.web("#FF6347")); // Tomato color
 
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        emailField.setPromptText("esempio@dominio.com");
        emailField.setStyle("-fx-background-color: #fff3e0;"); // Light cream color
 
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("********");
        passwordField.setStyle("-fx-background-color: #fff3e0;"); // Light cream color
 
        Button loginButton = new Button("Accedi");
        Button createAccountButton = new Button("Crea Account");
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        createAccountButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
 
        // Creazione del layout
        GridPane loginLayout = new GridPane();
        loginLayout.setPadding(new Insets(20));
        loginLayout.setHgap(15);
        loginLayout.setVgap(15);
        loginLayout.setStyle("-fx-background-image: url('background-image.jpg'); -fx-background-size: cover;"); // Background image
        loginLayout.add(welcomeLabel, 0, 0, 2, 1);
        loginLayout.add(emailLabel, 0, 1);
        loginLayout.add(emailField, 1, 1);
        loginLayout.add(passwordLabel, 0, 2);
        loginLayout.add(passwordField, 1, 2);
        loginLayout.add(loginButton, 0, 3);
        loginLayout.add(createAccountButton, 1, 3);
 
        // Impostazione della scena
        Scene scene = new Scene(loginLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
 
        // Gestione degli eventi dei pulsanti
        loginButton.setOnAction(e -> handleLogin(emailField.getText(), passwordField.getText(), primaryStage));
        createAccountButton.setOnAction(e -> openCreateAccountWindow(primaryStage));
    }
 
    private void handleLogin(String email, String password, Stage primaryStage) {
        List<Account> accounts = accountManager.loadAccounts();
        for (Account account : accounts) {
            if (account.getEmail().equals(email) && account.verifyPassword(password)) {
                openExerciseManagerWindow(account, primaryStage);
                return;
            }
        }
        showAlert("Errore di Accesso", "Email o password errati.");
    }
 
    private void openCreateAccountWindow(Stage primaryStage) {
        // Creazione della finestra di creazione dell'account
        Stage createAccountStage = new Stage();
        createAccountStage.initModality(Modality.APPLICATION_MODAL);
        createAccountStage.setTitle("Crea Nuovo Account");
 
        // Creazione dei controlli
        Label nameLabel = new Label("Nome:");
        TextField nameField = new TextField();
        nameField.setPromptText("Inserisci il tuo nome");
 
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        emailField.setPromptText("Inserisci la tua email");
 
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Inserisci la tua password");
 
        Button createButton = new Button("Crea Account");
        Button cancelButton = new Button("Annulla");
        createButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        cancelButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white;");
 
        // Creazione del layout
        GridPane createAccountLayout = new GridPane();
        createAccountLayout.setPadding(new Insets(20));
        createAccountLayout.setHgap(15);
        createAccountLayout.setVgap(15);
        createAccountLayout.setStyle("-fx-background-image: url('background-image.jpg'); -fx-background-size: cover;"); // Background image
        createAccountLayout.add(nameLabel, 0, 0);
        createAccountLayout.add(nameField, 1, 0);
        createAccountLayout.add(emailLabel, 0, 1);
        createAccountLayout.add(emailField, 1, 1);
        createAccountLayout.add(passwordLabel, 0, 2);
        createAccountLayout.add(passwordField, 1, 2);
        createAccountLayout.add(createButton, 0, 3);
        createAccountLayout.add(cancelButton, 1, 3);
 
        // Impostazione della scena
        Scene scene = new Scene(createAccountLayout, 500, 300);
        createAccountStage.setScene(scene);
        createAccountStage.show();
 
        // Gestione degli eventi dei pulsanti
        createButton.setOnAction(e -> handleCreateAccount(nameField.getText(), emailField.getText(), passwordField.getText(), createAccountStage));
        cancelButton.setOnAction(e -> createAccountStage.close());
    }
 
    private void handleCreateAccount(String nome, String email, String password, Stage createAccountStage) {
        Account newAccount = new Account(nome, email, password);
        if (nome.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Errore", "Tutti i campi devono essere compilati.");
            return;
        }
        if (accountManager.addAccount(newAccount)) {
            showAlert("Successo", "Account creato con successo!");
            createAccountStage.close(); // Chiudi la finestra di creazione dell'account
        } else {
            showAlert("Errore", "Errore nella creazione dell'account. Forse l'email esiste già.");
        }
    }
 
    private void openExerciseManagerWindow(Account account, Stage primaryStage) {
        ExerciseManager exerciseManager = new ExerciseManager(account);
        exerciseManager.show(primaryStage);
    }
 
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
 