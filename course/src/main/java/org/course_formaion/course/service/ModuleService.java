package org.course_formaion.course.service;


import org.course_formaion.course.entity.Course;
import org.course_formaion.course.entity.Module;
import org.course_formaion.course.repository.CourseRepo;
import org.course_formaion.course.repository.ModuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final CourseRepo courseRepository;

    public ModuleService(ModuleRepository moduleRepository, CourseRepo courseRepository) {
        this.moduleRepository = moduleRepository;
        this.courseRepository = courseRepository;
    }

    public Module addModuleToCourse(Long courseId, Module module) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));

        module.setId(null);
        module.setCourse(course);

        return moduleRepository.save(module);
    }

    public List<Module> getModulesByCourse(Long courseId) {
        return moduleRepository.findByCourseIdOrderByNumeroOrdreAsc(courseId);
    }

    public Module updateModule(Long moduleId, Module input) {
        Module existing = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new IllegalArgumentException("Module not found: " + moduleId));

        existing.setTitre(input.getTitre());
        existing.setDescription(input.getDescription());
        existing.setNumeroOrdre(input.getNumeroOrdre());
        existing.setDureeMinutes(input.getDureeMinutes());

        return moduleRepository.save(existing);
    }

    public void deleteModule(Long moduleId) {
        moduleRepository.deleteById(moduleId);
    }
}
