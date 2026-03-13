package org.course.enrollment_service.service;


import org.course.enrollment_service.dto.InscriptionRequest;
import org.course.enrollment_service.dto.InscriptionResponse;
import org.course.enrollment_service.dto.ProgressionRequest;
import org.course.enrollment_service.entity.Inscription;
import org.course.enrollment_service.repository.InscriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnrollmentService {

    private final InscriptionRepository repo;

    public EnrollmentService(InscriptionRepository repo) {
        this.repo = repo;
    }

    public InscriptionResponse inscrire(InscriptionRequest request) {
        if (repo.existsByIdUtilisateurAndIdFormation(request.getIdUtilisateur(), request.getIdFormation())) {
            throw new IllegalArgumentException("Utilisateur déjà inscrit à cette formation");
        }

        Inscription ins = new Inscription();
        ins.setIdUtilisateur(request.getIdUtilisateur());
        ins.setIdFormation(request.getIdFormation());
        Inscription saved = repo.save(ins);
        return toResponse(saved);
    }

    public List<InscriptionResponse> getByUtilisateur(Long idUtilisateur) {
        return repo.findByIdUtilisateur(idUtilisateur).stream()
                .map(this::toResponse).toList();
    }

    public List<InscriptionResponse> getByFormation(Long idFormation) {
        return repo.findByIdFormation(idFormation).stream()
                .map(this::toResponse).toList();
    }

    public InscriptionResponse getById(Long id) {
        Inscription ins = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inscription introuvable"));
        return toResponse(ins);
    }

    public InscriptionResponse updateProgression(Long id, ProgressionRequest request) {
        Inscription ins = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inscription introuvable"));

        if (ins.getStatut() == Inscription.Statut.ANNULEE) {
            throw new IllegalArgumentException("Impossible de modifier une inscription annulée");
        }

        int pct = request.getPourcentageProgression();
        if (pct < 0 || pct > 100) {
            throw new IllegalArgumentException("Le pourcentage doit être entre 0 et 100");
        }

        ins.setPourcentageProgression(pct);
        if (pct == 100) {
            ins.setStatut(Inscription.Statut.TERMINEE);
            ins.setDateFin(LocalDateTime.now());
        }

        return toResponse(repo.save(ins));
    }

    public InscriptionResponse annuler(Long id) {
        Inscription ins = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inscription introuvable"));

        if (ins.getStatut() == Inscription.Statut.TERMINEE) {
            throw new IllegalArgumentException("Impossible d'annuler une formation terminée");
        }

        ins.setStatut(Inscription.Statut.ANNULEE);
        ins.setDateFin(LocalDateTime.now());
        return toResponse(repo.save(ins));
    }

    private InscriptionResponse toResponse(Inscription ins) {
        return new InscriptionResponse(
                ins.getId(),
                ins.getIdUtilisateur(),
                ins.getIdFormation(),
                ins.getStatut().name(),
                ins.getPourcentageProgression(),
                ins.getDateInscription() != null ? ins.getDateInscription().toString() : null,
                ins.getDateFin() != null ? ins.getDateFin().toString() : null
        );
    }
}

