package com.course.auth_service.config;

import com.course.auth_service.controller.AuthController;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.stereotype.Component;

@Component
@ApplicationPath("/")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(AuthController.class);
        register(IllegalArgumentExceptionMapper.class);
        packages("com.course.auth_service.controller");
        property(ServletProperties.FILTER_FORWARD_ON_404, true);
    }
}