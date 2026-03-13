package org.course.enrollment_service.repository;


import org.course.enrollment_service.entity.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InscriptionRepository extends JpaRepository<Inscription, Long> {
    List<Inscription> findByIdUtilisateur(Long idUtilisateur);
    List<Inscription> findByIdFormation(Long idFormation);
    Optional<Inscription> findByIdUtilisateurAndIdFormation(Long idUtilisateur, Long idFormation);
    boolean existsByIdUtilisateurAndIdFormation(Long idUtilisateur, Long idFormation);
}
