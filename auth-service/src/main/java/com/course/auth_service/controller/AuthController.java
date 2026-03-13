package com.course.auth_service.controller;

import com.course.auth_service.dto.*;
import com.course.auth_service.service.AuthService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @POST
    @Path("/register")
    public Response register(RegisterRequest request) {
        AuthResponse body = service.register(request);
        return Response.status(Response.Status.CREATED).entity(body).build();
    }

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        AuthResponse body = service.login(request);
        return Response.ok(body).build();
    }

    @GET
    @Path("/me")
    public Response getProfile(@HeaderParam("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        UserProfileResponse profile = service.getProfile(token);
        return Response.ok(profile).build();
    }

    @PUT
    @Path("/me")
    public Response updateProfile(@HeaderParam("Authorization") String authHeader, UpdateProfileRequest request) {
        String token = extractToken(authHeader);
        UserProfileResponse profile = service.updateProfile(token, request);
        return Response.ok(profile).build();
    }

    @POST
    @Path("/logout")
    public Response logout(@HeaderParam("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        service.logout(token);
        return Response.ok(Map.of("message", "Logged out successfully")).build();
    }

    private String extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing or invalid Authorization header");
        }
        return authHeader.substring(7);
    }
}