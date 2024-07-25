package com.example;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;

public class Exercise {
    private String name;
    private String[] difficulties;
    private ProgressBar progressBar;
    private double completionPercentage;
    private boolean[] difficultyAccess; // Per tenere traccia se un livello è completato

    public Exercise(String name, String[] difficulties) {
        this.name = name;
        this.difficulties = difficulties;
        this.progressBar = new ProgressBar(0); // Inizializza la ProgressBar
        this.completionPercentage = 0;
        this.difficultyAccess = new boolean[difficulties.length];
        for (int i = 0; i < difficulties.length; i++) {
            difficultyAccess[i] = false; // All'inizio nessun livello è completato
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
        HBox hBox = new HBox(10); // Spazio di 10 pixel tra la ProgressBar e l'etichetta
        hBox.getChildren().addAll(progressBar, label);
        hBox.setAlignment(Pos.CENTER_LEFT); // Allinea gli elementi a sinistra
        return hBox;
    }

    public void completeDifficulty(String difficulty) {
        int index = getDifficultyIndex(difficulty);
        if (index != -1) {
            difficultyAccess[index] = true;
            updateCompletionPercentage();
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
        completionPercentage = (completed / (double) difficulties.length);
        progressBar.setProgress(completionPercentage);
    }

    public double getCompletionPercentage() {
        return completionPercentage;
    }

    public boolean canAccessDifficulty(String difficulty) {
        int index = getDifficultyIndex(difficulty);
        if (index == 0) {
            return true; // Il livello "Facile" è sempre accessibile
        } else if (index == 1) {
            return difficultyAccess[0]; // Livello "Medio" è accessibile se il livello "Facile" è completato
        } else if (index == 2) {
            return difficultyAccess[0] && difficultyAccess[1]; // Livello "Difficile" è accessibile se entrambi i livelli precedenti sono completati
        }
        return false;
    }
}
