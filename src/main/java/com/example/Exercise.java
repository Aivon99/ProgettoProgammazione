package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.ProgressBar;

public abstract class Exercise {
    private String name;
    private String[] difficulties;
    private ProgressBar progressBar;
    private double completionPercentage;
    private boolean[] difficultyAccess;

    public Exercise(String name, String[] difficulties) {
        this.name = name;
        this.difficulties = difficulties;
        this.progressBar = new ProgressBar(0);
        this.completionPercentage = 0;
        this.difficultyAccess = new boolean[difficulties.length];
        // Initialize difficultyAccess to false
        for (int i = 0; i < difficulties.length; i++) {
            difficultyAccess[i] = false;
        }
    }

    public String getName() {
        return name;
    }

    public String[] getDifficulties() {
        return difficulties;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public HBox getExerciseProgressBar() {
        Label label = new Label(name);
        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(progressBar, label);
        hBox.setAlignment(Pos.CENTER_LEFT);
        return hBox;
    }

    public void completeDifficulty(String difficulty, String username) {
        int index = getDifficultyIndex(difficulty);
        if (index != -1) {
            difficultyAccess[index] = true;
            updateCompletionPercentage();
            try {
                // Save user progress with the username
                saveProgress(username);
            } catch (IOException e) {
                System.err.println("Error saving progress: " + e.getMessage());
            }
        }
    }
    

    private int getDifficultyIndex(String difficulty) {
        for (int i = 0; i < difficulties.length; i++) {
            if (difficulties[i].equals(difficulty)) {
                return i;
            }
        }
        return -1;
    }

    private void updateCompletionPercentage() {
        int completed = 0;
        for (boolean access : difficultyAccess) {
            if (access) {
                completed++;
            }
        }
        completionPercentage = (double) completed / difficulties.length;
        progressBar.setProgress(completionPercentage);
    }

    public double getCompletionPercentage() {
        return completionPercentage;
    }

    public boolean canAccessDifficulty(String difficulty) {
        int index = getDifficultyIndex(difficulty);
        switch (index) {
            case 0:
                return true;
            case 1:
                return difficultyAccess[0];
            case 2:
                return difficultyAccess[0] && difficultyAccess[1];
            default:
                return false;
        }
    }

    // metodi astratti
    public abstract String getDescription();
    public abstract void resetExercise();
    public abstract boolean checkAllAnswers();
    public abstract void submitAnswer(int answer);
    public abstract void handleExerciseSuspension();
    public abstract String getExerciseDetails(int index);
    public abstract void saveResult(String username, boolean success, String difficulty) throws IOException;
    public abstract void finestraEsercizio(int index, String difficulty, VBox layout);
    // Save user progress to a file
private void saveProgress(String username) throws IOException {
    String fileName = getFileName(username); // Pass the username to get the correct file name
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
        for (boolean access : difficultyAccess) {
            writer.write(access ? '1' : '0');
        }
        writer.newLine();
    }
}


    protected String getFileName(String username) {
        return username + "_" + name.replaceAll("\\s", "_") + "_progress.txt";
    }

    // Metodo per caricare il progresso dell'utente
    public void loadProgress(String username) throws IOException {
        String fileName = getFileName(username);
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine();
            if (line != null) {
                for (int i = 0; i < line.length(); i++) {
                    difficultyAccess[i] = (line.charAt(i) == '1');
                }
                updateCompletionPercentage(); // Update the progress bar
            }
        } catch (IOException e) {
            System.out.println("Progress not found for " + username + " and " + name + ". Starting from scratch.");
            throw e;
        }
    }
}

