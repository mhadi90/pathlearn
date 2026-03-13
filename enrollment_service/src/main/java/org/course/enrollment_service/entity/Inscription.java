package org.course.enrollment_service.entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inscription")
public class Inscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_utilisateur", nullable = false)
    private Long idUtilisateur;

    @Column(name = "id_formation", nullable = false)
    private Long idFormation;

    @Column(name = "date_inscription")
    private LocalDateTime dateInscription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Statut statut;

    @Column(name = "pourcentage_progression")
    private Integer pourcentageProgression;

    @Column(name = "date_fin")
    private LocalDateTime dateFin;

    @PrePersist
    void onCreate() {
        this.dateInscription = LocalDateTime.now();
        this.statut = Statut.EN_COURS;
        this.pourcentageProgression = 0;
    }

    public enum Statut {
        EN_COURS, TERMINEE, ANNULEE
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdUtilisateur() { return idUtilisateur; }
    public void setIdUtilisateur(Long idUtilisateur) { this.idUtilisateur = idUtilisateur; }
    public Long getIdFormation() { return idFormation; }
    public void setIdFormation(Long idFormation) { this.idFormation = idFormation; }
    public LocalDateTime getDateInscription() { return dateInscription; }
    public Statut getStatut() { return statut; }
    public void setStatut(Statut statut) { this.statut = statut; }
    public Integer getPourcentageProgression() { return pourcentageProgression; }
    public void setPourcentageProgression(Integer pourcentageProgression) { this.pourcentageProgression = pourcentageProgression; }
    public LocalDateTime getDateFin() { return dateFin; }
    public void setDateFin(LocalDateTime dateFin) { this.dateFin = dateFin; }
}