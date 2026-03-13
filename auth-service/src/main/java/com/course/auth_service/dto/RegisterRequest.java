package com.course.auth_service.dto;


public class RegisterRequest {
    private String email;
    private String password;
    private String nom;
    private String prenom;
    private String role; // "VISITEUR" | "APPRENTI" | "FORMATEUR"

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}