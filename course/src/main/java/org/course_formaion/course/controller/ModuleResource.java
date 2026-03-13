package org.course_formaion.course.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.course_formaion.course.entity.Module;
import org.course_formaion.course.service.ModuleService;
import org.springframework.stereotype.Component;

import java.util.List;

@Path("/courses/{courseId}/modules")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
public class ModuleResource {

    private final ModuleService service;

    public ModuleResource(ModuleService service) {
        this.service = service;
    }

    @POST
    public Response addModule(@PathParam("courseId") Long courseId, Module module) {
        Module created = service.addModuleToCourse(courseId, module);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    public List<Module> listModules(@PathParam("courseId") Long courseId) {
        return service.getModulesByCourse(courseId);
    }

    @PUT
    @Path("/{id}")
    public Module update(@PathParam("courseId") Long courseId,
                         @PathParam("id") Long moduleId, Module module) {
        return service.updateModule(moduleId, module);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("courseId") Long courseId,
                           @PathParam("id") Long moduleId) {
        service.deleteModule(moduleId);
        return Response.noContent().build();
    }
}
