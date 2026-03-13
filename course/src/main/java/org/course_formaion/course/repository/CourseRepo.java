package org.course_formaion.course.repository;

import org.course_formaion.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepo extends JpaRepository<Course, Long> {
}
