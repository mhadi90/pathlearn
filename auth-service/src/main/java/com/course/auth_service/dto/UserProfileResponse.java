package com.course.auth_service.dto;

public class UserProfileResponse {
    private Long id;
    private String email;
    private String nom;
    private String prenom;
    private String role;
    private String dateCreation;

    public UserProfileResponse(Long id, String email, String nom, String prenom, String role, String dateCreation) {
        this.id = id;
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
        this.dateCreation = dateCreation;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getRole() { return role; }
    public String getDateCreation() { return dateCreation; }
}