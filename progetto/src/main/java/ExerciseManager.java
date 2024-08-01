package Log;

import java.io.IOException;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.util.Duration;

public class ExerciseManager {

    private Account account;
    private Exercise[] exercises;

    public ExerciseManager(Account account) {
        this.account = account;
        this.exercises = new Exercise[]{
            new NumberSumExercise(),
            new MultiplicationExercise(), // Aggiungi il nuovo esercizio qui
        };

        // Carica i progressi dell'utente per tutti gli esercizi
        for (Exercise exercise : exercises) {
            try {
                exercise.loadProgress(account.getNome());
            } catch (IOException e) {
                System.out.println("Errore nel caricamento del progresso per " + exercise.getName() + ": " + e.getMessage());
            }
        }
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
                    showDescriptionWindow(selectedExercise, selectedDifficulty, primaryStage);
                } else {
                    showAlert("Errore", "Seleziona un livello di difficoltà.");
                }
            } else {
                showAlert("Errore", "Seleziona un esercizio.");
            }
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(welcomeLabel, exerciseListView, difficultyComboBox, viewExerciseButton);

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

    private void showDescriptionWindow(Exercise exercise, String difficulty, Stage primaryStage) {
        Stage descriptionStage = new Stage();
        descriptionStage.setTitle("Descrizione dell'Esercizio");

        Label descriptionLabel = new Label(exercise.getDescription());
        descriptionLabel.setWrapText(true);

        Button backButton = new Button("Indietro");
        Button proceedButton = new Button("Prosegui");

        backButton.setOnAction(e -> descriptionStage.close());

        proceedButton.setOnAction(e -> {
            descriptionStage.close();
            showExerciseWindow(exercise, difficulty, primaryStage);
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(descriptionLabel, backButton, proceedButton);

        Scene scene = new Scene(layout, 400, 200);
        descriptionStage.setScene(scene);
        descriptionStage.show();
    }

    private void showExerciseWindow(Exercise exercise, String difficulty, Stage primaryStage) {
        Stage exerciseStage = new Stage();
        exerciseStage.setTitle(exercise.getName());
    
        Label exerciseLabel = new Label("Esercizio: " + exercise.getName());
        Label difficultyLabel = new Label("Difficoltà: " + difficulty);
        Label questionLabel = new Label();
        TextArea answerField = new TextArea();
        answerField.setPromptText("Inserisci la tua risposta");
    
        Button submitButton = new Button("Invia Risposta");
        Button exitButton = new Button("Esci dall’Esercizio");
        Label timerLabel = new Label(); // Label to display the timer
    
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(exerciseLabel, difficultyLabel, questionLabel, answerField, submitButton, exitButton, timerLabel);
    
        Scene scene = new Scene(layout, 400, 300);
        exerciseStage.setScene(scene);
        exerciseStage.show();
    
        int[] currentQuestionIndex = {0};
    
        // Show the first question
        updateQuestion(exercise, currentQuestionIndex[0], questionLabel);
    
        // Start the timer
        startTimer(timerLabel, difficulty, exercise, exerciseStage); // Pass the exercise instance here
    
        submitButton.setOnAction(e -> {
            try {
                int userAnswer = Integer.parseInt(answerField.getText());
                exercise.submitAnswer(userAnswer);
    
                if (currentQuestionIndex[0] < 2) {
                    currentQuestionIndex[0]++;
                    updateQuestion(exercise, currentQuestionIndex[0], questionLabel);
                    answerField.clear();
                } else {
                    boolean success = exercise.checkAllAnswers();
                    if (success) {
                        exercise.completeDifficulty(difficulty,account.getNome());
                        showAlert("Successo", "Hai completato correttamente tutte le domande! Livello completato.");
                    } else {
                        showAlert("Errore", "Non tutte le risposte sono corrette. Riprova.");
                    }
                    try {
                        exercise.saveResult(account.getNome(), success, difficulty);
                    } catch (IOException ex) {
                        showAlert("Errore", "Impossibile salvare il risultato.");
                    }
                    exercise.resetExercise();
                    exerciseStage.close();
                }
            } catch (NumberFormatException ex) {
                showAlert("Errore", "Inserisci una risposta valida.");
            }
        });
    
        exitButton.setOnAction(e -> {
            if (!exercise.checkAllAnswers()) {
                showAlert("Fallimento", "Hai abbandonato l'esercizio. Sarà considerato come fallito.");
                try {
                    exercise.saveResult(account.getNome(), false, difficulty);
                } catch (IOException ex) {
                    showAlert("Errore", "Impossibile salvare il risultato.");
                }
            }
            exercise.resetExercise();
            exerciseStage.close();
        });
    }
    

    private void updateQuestion(Exercise exercise, int questionIndex, Label questionLabel) {
        if (exercise instanceof NumberSumExercise) {
            questionLabel.setText("Domanda " + (questionIndex + 1) + ": " + ((NumberSumExercise) exercise).getExerciseDetails(questionIndex));
        } else if (exercise instanceof MultiplicationExercise) {
            questionLabel.setText("Domanda " + (questionIndex + 1) + ": " + ((MultiplicationExercise) exercise).getExerciseDetails(questionIndex));
        }
    }

    private void startTimer(Label timerLabel, String difficulty, Exercise exercise, Stage exerciseStage) {
        int timeLimit;
    
        switch (difficulty) {
            case "Facile":
                timeLimit = 60;
                break;
            case "Medio":
                timeLimit = 120;
                break;
            case "Difficile":
                timeLimit = 180;
                break;
            default:
                timeLimit = 60; // Default to easy if difficulty is not recognized
                break;
        }
    
        final int[] timeRemaining = {timeLimit}; // Time remaining in seconds
    
        // Timer update every second
        PauseTransition timerUpdate = new PauseTransition(Duration.seconds(1));
        timerUpdate.setOnFinished(event -> {
            if (timeRemaining[0] > 0) {
                timeRemaining[0]--;
                timerLabel.setText("Tempo rimanente: " + timeRemaining[0] + " secondi");
                timerUpdate.playFromStart(); // Restart the timer
            } else {
                showAlert("Tempo Scaduto", "Il tempo per completare l'esercizio è scaduto.");
                try {
                    // Save result as failed due to timeout
                    exercise.saveResult(account.getNome(), false, difficulty);
                } catch (IOException e) {
                    showAlert("Errore", "Impossibile salvare il risultato.");
                }
                exercise.resetExercise();
                exerciseStage.close();
            }
        });
    
        timerUpdate.play(); // Start the timer
    }
    

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
