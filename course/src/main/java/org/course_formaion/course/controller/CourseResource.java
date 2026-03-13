package org.course_formaion.course.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.course_formaion.course.entity.Course;
import org.course_formaion.course.service.CourseService;
import org.springframework.stereotype.Component;

import java.util.List;

@Path("/courses")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
public class CourseResource {

    private final CourseService service;

    public CourseResource(CourseService service) {
        this.service = service;
    }

    @GET
    public List<Course> getAll() {
        return service.findAll();
    }

    @GET
    @Path("/{id}")
    public Course getOne(@PathParam("id") Long id) {
        return service.findById(id);
    }

    @POST
    public Response create(Course course) {
        Course created = service.create(course);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public Course update(@PathParam("id") Long id, Course course) {
        return service.update(id, course);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}