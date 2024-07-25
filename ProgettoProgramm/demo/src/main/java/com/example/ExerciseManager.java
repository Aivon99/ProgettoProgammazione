package com.example;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ExerciseManager {

    private Account account;
    private Exercise[] exercises;

    public ExerciseManager(Account account) {
        this.account = account;
        this.exercises = new Exercise[]{
            new NumberSumExercise(), // Esercizio specifico
            new Exercise("Ordinamento di array", new String[]{"Facile", "Medio", "Difficile"}),
            new Exercise("Ricerca binaria", new String[]{"Facile", "Medio", "Difficile"})
        };
    }

    public void show(Stage primaryStage) {
        primaryStage.setTitle("Benvenuto");

        Label welcomeLabel = new Label("Benvenuto, " + account.getNome() + "!");
        welcomeLabel.setFont(Font.font("Arial", 24));

        ListView<Exercise> exerciseListView = new ListView<>();
        exerciseListView.getItems().addAll(exercises);

        exerciseListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Exercise item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getName());
            }
        });

        ComboBox<String> difficultyComboBox = new ComboBox<>();
        difficultyComboBox.setPromptText("Seleziona difficoltà");

        exerciseListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                difficultyComboBox.getItems().clear();
                for (String difficulty : newSelection.getDifficulties()) {
                    if (newSelection.canAccessDifficulty(difficulty)) {
                        difficultyComboBox.getItems().add(difficulty);
                    }
                }
            }
        });

        Button viewExerciseButton = new Button("Visualizza Esercizio");
        viewExerciseButton.setOnAction(e -> {
            Exercise selectedExercise = exerciseListView.getSelectionModel().getSelectedItem();
            if (selectedExercise != null) {
                String selectedDifficulty = difficultyComboBox.getValue();
                if (selectedDifficulty != null) {
                    showExerciseDetails(selectedExercise, selectedDifficulty);
                } else {
                    showAlert("Errore", "Seleziona un livello di difficoltà.");
                }
            } else {
                showAlert("Errore", "Seleziona un esercizio.");
            }
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(
            welcomeLabel, exerciseListView, difficultyComboBox, viewExerciseButton
        );

        // Aggiungi una ProgressBar con il nome dell'esercizio per ogni esercizio
        for (Exercise exercise : exercises) {
            layout.getChildren().add(exercise.getExerciseProgressBar());
        }

        Button logoutButton = new Button("Logout");
        layout.getChildren().add(logoutButton);

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        logoutButton.setOnAction(e -> primaryStage.close());
    }

    private void showExerciseDetails(Exercise exercise, String difficulty) {
        Stage detailsStage = new Stage();
        detailsStage.setTitle("Dettagli Esercizio");

        Label exerciseLabel = new Label("Esercizio: " + exercise.getName());
        Label difficultyLabel = new Label("Difficoltà: " + difficulty);

        TextArea exerciseDetails = new TextArea();
        if (exercise instanceof NumberSumExercise) {
            exerciseDetails.setText(((NumberSumExercise) exercise).getExerciseDetails());
        } else {
            exerciseDetails.setText("Dettagli dell'esercizio " + exercise.getName() + " al livello " + difficulty);
        }
        exerciseDetails.setEditable(false);

        TextArea answerField = new TextArea();
        answerField.setPromptText("Inserisci la tua risposta");
        Button submitButton = new Button("Invia Risposta");
        submitButton.setOnAction(e -> {
            if (exercise instanceof NumberSumExercise) {
                int userAnswer;
                try {
                    userAnswer = Integer.parseInt(answerField.getText());
                    if (((NumberSumExercise) exercise).checkAnswer(userAnswer)) {
                        exercise.completeDifficulty(difficulty);
                        showAlert("Successo", "Risposta corretta! Livello completato.");
                        // Aggiorna la ProgressBar dell'esercizio specifico
                        exercise.getProgressBar().setProgress(exercise.getCompletionPercentage());
                    } else {
                        showAlert("Errore", "Risposta sbagliata. Riprova.");
                    }
                } catch (NumberFormatException ex) {
                    showAlert("Errore", "Inserisci una risposta valida.");
                }
            }
        });

        Button closeButton = new Button("Chiudi");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(exerciseLabel, difficultyLabel, exerciseDetails, answerField, submitButton, closeButton);

        Scene scene = new Scene(layout, 400, 300);
        detailsStage.setScene(scene);
        detailsStage.show();

        closeButton.setOnAction(e -> detailsStage.close());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
