package org.course.enrollment_service.dto;


public class InscriptionRequest {
    private Long idUtilisateur;
    private Long idFormation;

    public Long getIdUtilisateur() { return idUtilisateur; }
    public void setIdUtilisateur(Long idUtilisateur) { this.idUtilisateur = idUtilisateur; }
    public Long getIdFormation() { return idFormation; }
    public void setIdFormation(Long idFormation) { this.idFormation = idFormation; }
}