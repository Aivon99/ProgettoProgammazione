package com.example;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
 
public class AccountManager {
    private static final String CREDENTIALS_FILE = "credentials.txt";
 
    // Carica tutti gli account dal file
    public List<Account> loadAccounts() {
        List<Account> accounts = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(CREDENTIALS_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(":");
                if (parts.length == 3) {
                    accounts.add(new Account(parts[0], parts[1], parts[2]));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File delle credenziali non trovato.");
        }
        return accounts;
    }
 
    // Salva un nuovo account nel file utilizzando PrintWriter
    public boolean addAccount(Account account) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CREDENTIALS_FILE, true))) {
            writer.println(account.getNome() + ":" + account.getEmail() + ":" + account.getPassword());
            return true;
        } catch (IOException e) {
            System.out.println("Errore durante la scrittura delle credenziali.");
            return false;
        }
    }
 
    // Verifica se un'email e una password corrispondono a un account esistente
    public boolean authenticate(String email, String password) {
        List<Account> accounts = loadAccounts();
        for (Account account : accounts) {
            if (account.getEmail().equals(email) && account.verifyPassword(password)) {
                return true;
            }
        }
        return false;
    }
}