package progetto.programmazione;

import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginPage {

    private AccountManager accountManager = new AccountManager();

    public void show(Stage primaryStage) {
        primaryStage.setTitle("Pagina di Login");

        // Creazione dei controlli
        Label welcomeLabel = new Label("Benvenuto");
        welcomeLabel.setFont(Font.font("Arial", 24));

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Accedi");
        Button createAccountButton = new Button("Crea Account");

        // Creazione del layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(welcomeLabel, emailLabel, emailField, passwordLabel, passwordField, loginButton, createAccountButton);

        // Impostazione della scena
        Scene scene = new Scene(layout, 600, 450);
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

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button createButton = new Button("Crea Account");
        Button cancelButton = new Button("Annulla");

        // Creazione del layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(nameLabel, nameField, emailLabel, emailField, passwordLabel, passwordField, createButton, cancelButton);

        // Impostazione della scena
        Scene scene = new Scene(layout, 500, 500);
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
