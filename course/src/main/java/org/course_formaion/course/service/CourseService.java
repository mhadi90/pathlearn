package org.course_formaion.course.service;


import org.course_formaion.course.entity.Course;
import org.course_formaion.course.repository.CourseRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepo repository;

    public CourseService(CourseRepo repository) {
        this.repository = repository;
    }

    public List<Course> findAll() {
        return repository.findAll();
    }

    public Course findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + id));
    }

    public Course create(Course course) {
        course.setId(null); // sécurité : empêcher overwrite
        return repository.save(course);
    }

    public Course update(Long id, Course input) {
        Course existing = findById(id);

        existing.setTitre(input.getTitre());
        existing.setDescription(input.getDescription());
        existing.setDomaine(input.getDomaine());
        existing.setNiveau(input.getNiveau());
        existing.setDuree(input.getDuree());
        existing.setPrix(input.getPrix());
        existing.setIdFormateur(input.getIdFormateur());
        existing.setNote(input.getNote());
        existing.setStatut(input.getStatut());

        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}