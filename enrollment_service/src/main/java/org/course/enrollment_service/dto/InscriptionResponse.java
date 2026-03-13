package org.course.enrollment_service.dto;


import java.time.LocalDateTime;

public class InscriptionResponse {
    private Long id;
    private Long idUtilisateur;
    private Long idFormation;
    private String statut;
    private Integer pourcentageProgression;
    private String dateInscription;
    private String dateFin;

    public InscriptionResponse(Long id, Long idUtilisateur, Long idFormation, String statut,
                               Integer pourcentageProgression, String dateInscription, String dateFin) {
        this.id = id;
        this.idUtilisateur = idUtilisateur;
        this.idFormation = idFormation;
        this.statut = statut;
        this.pourcentageProgression = pourcentageProgression;
        this.dateInscription = dateInscription;
        this.dateFin = dateFin;
    }

    public Long getId() { return id; }
    public Long getIdUtilisateur() { return idUtilisateur; }
    public Long getIdFormation() { return idFormation; }
    public String getStatut() { return statut; }
    public Integer getPourcentageProgression() { return pourcentageProgression; }
    public String getDateInscription() { return dateInscription; }
    public String getDateFin() { return dateFin; }
}
