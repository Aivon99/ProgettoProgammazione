package com.example;
public class Account {
    private String nome;
    private String email;
    private String password; // Considera l'uso di password hashate in un'applicazione reale
 
    public Account(String nome, String email, String password) {
        this.nome = nome;
        this.email = email;
        this.password = password;
    }
 
    public String getNome() {
        return nome;
    }
 
    public String getEmail() {
        return email;
    }
 
    public String getPassword() {
        return password;
    }
 
    // Metodo per controllare se la password è corretta
    public boolean verifyPassword(String password) {
        return this.password.equals(password);
    }
 
    @Override
    public String toString() {
        return "Nome: " + nome + ", Email: " + email;
    }
}