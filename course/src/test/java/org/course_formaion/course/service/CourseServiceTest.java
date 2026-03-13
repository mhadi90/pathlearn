package org.course_formaion.course.service;


import org.course_formaion.course.entity.Course;
import org.course_formaion.course.repository.CourseRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepo repo;

    @InjectMocks
    private CourseService service;

    private Course course;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setId(1L);
        course.setTitre("React Moderne");
        course.setDomaine("Développement");
        course.setNiveau(2);
        course.setPrix(new BigDecimal("79.99"));
        course.setNote(new BigDecimal("4.8"));
        course.setDuree(12);
    }

    // --- findAll ---

    @Test
    void findAll_returnsAllCourses() {
        when(repo.findAll()).thenReturn(List.of(course));

        List<Course> result = service.findAll();

        assertEquals(1, result.size());
        verify(repo).findAll();
    }

    // --- findById ---

    @Test
    void findById_existingId_returnsCourse() {
        when(repo.findById(1L)).thenReturn(Optional.of(course));

        Course result = service.findById(1L);

        assertEquals("React Moderne", result.getTitre());
    }

    @Test
    void findById_unknownId_throwsException() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.findById(99L));
    }

    // --- create ---

    @Test
    void create_setsIdNullAndSaves() {
        when(repo.save(any(Course.class))).thenReturn(course);

        Course input = new Course();
        input.setId(99L); // should be reset to null
        input.setTitre("New Course");

        Course result = service.create(input);

        assertNull(input.getId()); // id was reset
        assertNotNull(result);
        verify(repo).save(input);
    }

    // --- update ---

    @Test
    void update_existingCourse_updatesFields() {
        Course input = new Course();
        input.setTitre("Updated");
        input.setDomaine("Design");
        input.setNiveau(1);
        input.setDuree(5);
        input.setPrix(new BigDecimal("29.99"));
        input.setStatut("published");

        when(repo.findById(1L)).thenReturn(Optional.of(course));
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Course result = service.update(1L, input);

        assertEquals("Updated", result.getTitre());
        assertEquals("Design", result.getDomaine());
        assertEquals(new BigDecimal("29.99"), result.getPrix());
    }

    @Test
    void update_unknownId_throwsException() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> service.update(99L, new Course()));
    }


    @Test
    void delete_callsRepoDeleteById() {
        service.delete(1L);
        verify(repo).deleteById(1L);
    }

}