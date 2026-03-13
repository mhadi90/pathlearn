package org.course.enrollment_service.controller;


import org.course.enrollment_service.dto.InscriptionRequest;
import org.course.enrollment_service.dto.InscriptionResponse;
import org.course.enrollment_service.dto.ProgressionRequest;
import org.course.enrollment_service.service.EnrollmentService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Path("/inscriptions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InscriptionController {

    private final EnrollmentService service;

    public InscriptionController(EnrollmentService service) {
        this.service = service;
    }

    @POST
    public Response inscrire(InscriptionRequest request) {
        InscriptionResponse body = service.inscrire(request);
        return Response.status(Response.Status.CREATED).entity(body).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        return Response.ok(service.getById(id)).build();
    }

    @GET
    @Path("/utilisateur/{idUtilisateur}")
    public Response getByUtilisateur(@PathParam("idUtilisateur") Long idUtilisateur) {
        return Response.ok(service.getByUtilisateur(idUtilisateur)).build();
    }

    @GET
    @Path("/formation/{idFormation}")
    public Response getByFormation(@PathParam("idFormation") Long idFormation) {
        return Response.ok(service.getByFormation(idFormation)).build();
    }

    @PUT
    @Path("/{id}/progression")
    public Response updateProgression(@PathParam("id") Long id, ProgressionRequest request) {
        return Response.ok(service.updateProgression(id, request)).build();
    }

    @PUT
    @Path("/{id}/annuler")
    public Response annuler(@PathParam("id") Long id) {
        return Response.ok(service.annuler(id)).build();
    }
}
