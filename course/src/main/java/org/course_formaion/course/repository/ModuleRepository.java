package org.course_formaion.course.repository;

import org.course_formaion.course.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuleRepository extends JpaRepository<Module, Long> {

    List<Module> findByCourseIdOrderByNumeroOrdreAsc(Long courseId);
}