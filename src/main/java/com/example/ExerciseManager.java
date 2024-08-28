package com.example;

import java.io.IOException;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Node;



public class ExerciseManager {

    private Account account;
    private Exercise[] exercises;

    public ExerciseManager(Account account) {
        this.account = account;
        this.exercises = new Exercise[]{
       
            new esercizioEfficienza() // Aggiungi il nuovo esercizio qui
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
            welcomeLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 30));
            welcomeLabel.setTextFill(Color.web("#FFD700")); 
            welcomeLabel.setAlignment(Pos.CENTER);
    


        ListView<Exercise> exerciseListView = new ListView<>();
        exerciseListView.getItems().addAll(exercises);

            exerciseListView.setCellFactory(lv -> new ListCell<>() {
                @Override
                protected void updateItem(Exercise item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("-fx-background-color: #282c34; -fx-text-fill: #61dafb;"); // Default style
                    } else {
                        setText(item.getName());
                        if (isSelected()) {
                            setStyle("-fx-background-color: #61dafb; -fx-text-fill: #282c34; -fx-font-weight: bold;"); // Selected style
                        } else {
                            setStyle("-fx-background-color: #282c34; -fx-text-fill: #61dafb;");
                        }
                    }
                }
            });


        ComboBox<String> difficultyComboBox = new ComboBox<>();
        difficultyComboBox.setPromptText("Seleziona difficoltà");
            difficultyComboBox.setStyle("-fx-background-color: #61dafb; -fx-text-fill: white;");


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
            viewExerciseButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

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

        VBox layout = new VBox(15);
            layout.setPadding(new Insets(20));
            layout.setStyle("-fx-background-color: #2e2e2e;");
        layout.getChildren().addAll(welcomeLabel, exerciseListView, difficultyComboBox, viewExerciseButton);

        for (Exercise exercise : exercises) {
            layout.getChildren().add(exercise.getExerciseProgressBar());
        }

        //Bottone LogOut
        Button logoutButton = new Button("Logout");
            logoutButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white;");
       
        layout.getChildren().add(logoutButton);

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        logoutButton.setOnAction(e -> { //Apro nuova pagina con schermata di login e chiudo vecchia
            primaryStage.close();
            LoginPage ReloginPage = new LoginPage();
            ReloginPage.show(primaryStage);
            
            });
    }

    private void showDescriptionWindow(Exercise exercise, String difficulty, Stage primaryStage) {
        Stage descriptionStage = new Stage();
        descriptionStage.setTitle("Descrizione dell'Esercizio");

        Label descriptionLabel = new Label(exercise.getDescription(difficulty));
        descriptionLabel.setWrapText(true);
            descriptionLabel.setFont(Font.font("Comic Sans MS", 14));
            descriptionLabel.setTextFill(Color.web("#FFFFFF"));
            
        Button backButton = new Button("Indietro");
        Button proceedButton = new Button("Prosegui");
            backButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white;");
            proceedButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        backButton.setOnAction(e -> descriptionStage.close());

        proceedButton.setOnAction(e -> {
            descriptionStage.close();
            showExerciseWindow(exercise, difficulty, primaryStage);
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
            layout.setStyle("-fx-background-color: #282c34;");
        layout.getChildren().addAll(descriptionLabel, backButton, proceedButton);

        Scene scene = new Scene(layout, 400, 200);
        descriptionStage.setScene(scene);
        descriptionStage.show();
    }

    private void showExerciseWindow(Exercise exercise, String difficulty, Stage primaryStage) {
        Stage exerciseStage = new Stage();
        exerciseStage.setTitle(exercise.getName());
        int[] currentQuestionIndex = {0};
        
        //Intestazione Eserciizio
        Label exerciseLabel = new Label("Esercizio: " + exercise.getName());
            exerciseLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 24));
            exerciseLabel.setTextFill(Color.web("#FFFFFF"));
        
        //Difficoltà
        Label difficultyLabel = new Label("Difficoltà: " + difficulty);
            difficultyLabel.setFont(Font.font("Comic Sans MS", 18));
            difficultyLabel.setTextFill(Color.web("#FFD700")); 

        TextArea answerField = new TextArea();
            answerField.setMinSize(160, 40);
            answerField.setMaxSize(500, 60);
        answerField.setPromptText("Inserisci la tua risposta");

        //Bottoni e stile
        Button submitButton = new Button("Invia Risposta");
        Button exitButton = new Button("Esci dall’Esercizio");
        Label timerLabel = new Label();
            submitButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px; -fx-cursor: hand;");
            exitButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px; -fx-cursor: hand;");
            timerLabel.setFont(Font.font("Comic Sans MS", 16));
            timerLabel.setTextFill(Color.web("#FFFFFF")); 

            //Layout Schermata Esercizio
        VBox layout = new VBox(20);
            layout.setPadding(new Insets(20));
            layout.setStyle("-fx-background-color: #2e2e2e;"); 
       
         VBox core = new VBox(); //contiene testo, contesto e descrizione esercizio. Da modificare a seconda delle esigenze nella classe di ciascun esercizio tramite metodo finestraEsercizio() 
           
         Object[] campoInput = new Object[1]; //Per gestire diversi tipi di input scelta multipla, testo etc
            
            // Chiamare finestraEsercizio per visualizzare la domanda iniziale
            exercise.finestraEsercizio(currentQuestionIndex[0], difficulty, core, campoInput);
           
            
        layout.getChildren().addAll(exerciseLabel, difficultyLabel, core,
                                     answerField, submitButton, exitButton, timerLabel);
    
        Scene scene = new Scene(layout, 800, 600);
        exerciseStage.setScene(scene);
        exerciseStage.show();
    
      
    
        // Inizializzare il timer
        startTimer(timerLabel, difficulty, exercise, exerciseStage);
       
        submitButton.setOnAction(e -> {
            try {
                String userInput = null;

        if (campoInput[0] instanceof TextInputControl) {  // per il testo
           
            userInput = ((TextInputControl) campoInput[0]).getText();

        } else if (campoInput[0] instanceof ToggleGroup) { // per scelta multipla
            ToggleGroup group = (ToggleGroup) campoInput[0];

            RadioButton selectedButton = (RadioButton) group.getSelectedToggle();
                userInput = selectedButton.getText();
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
    

    private void startTimer(Label timerLabel, String difficulty, Exercise exercise, Stage exerciseStage) {
        int timeLimit;
    //TODO: cambiare timelimit indietro, messo a 10 per provare i fine tempo 
        switch (difficulty) {
            case "Facile":
                timeLimit = 100;
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
    
        final int[] timeRemaining = {timeLimit}; //rimanenza
    
        // aggiornamento
        PauseTransition timerUpdate = new PauseTransition(Duration.seconds(1));
        timerUpdate.setOnFinished(event -> {
            if (timeRemaining[0] > 0) {
                timeRemaining[0]--;
                timerLabel.setText("Tempo rimanente: " + timeRemaining[0] + " secondi");
                timerUpdate.playFromStart(); // Restart the timer
            } else {
                 Platform.runLater(() -> { ///NOTE: usiamo perchè il shoaAndWait ha problemi con animazioni in atto 
                    showAlert("Tempo Scaduto", "Il tempo per completare l'esercizio è scaduto.");
                    exerciseStage.close();
                });
                try {
                

                    exercise.saveResult(account.getNome(), false, difficulty); //salviamo come fallito
                
                } catch (IOException e) {
                    showAlert("Errore", "Impossibile salvare il risultato.");
                }
                
            }
        });
    
        timerUpdate.play(); //Partenza timer
    }
    

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
