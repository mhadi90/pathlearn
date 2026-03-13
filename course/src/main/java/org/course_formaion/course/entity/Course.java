package org.course_formaion.course.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "formation")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String domaine;
    private Integer niveau;
    private Integer duree;
    private BigDecimal prix;

    @Column(name = "id_formateur")
    private Long idFormateur;

    private BigDecimal note;
    private String statut;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "date_modif")
    private LocalDateTime dateModif;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.dateModif = this.createdAt;
    }

    @PreUpdate
    void onUpdate() {
        this.dateModif = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("numeroOrdre ASC")
    private java.util.List<Module> modules = new java.util.ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getDescription() {
        return description;
    }

    public String getDomaine() {
        return domaine;
    }

    public Integer getNiveau() {
        return niveau;
    }

    public Integer getDuree() {
        return duree;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public Long getIdFormateur() {
        return idFormateur;
    }

    public BigDecimal getNote() {
        return note;
    }

    public String getStatut() {
        return statut;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getDateModif() {
        return dateModif;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }

    public void setNiveau(Integer niveau) {
        this.niveau = niveau;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public void setIdFormateur(Long idFormateur) {
        this.idFormateur = idFormateur;
    }

    public void setNote(BigDecimal note) {
        this.note = note;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setDateModif(LocalDateTime dateModif) {
        this.dateModif = dateModif;
    }
}