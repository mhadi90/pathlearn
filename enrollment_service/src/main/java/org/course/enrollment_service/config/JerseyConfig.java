package org.course.enrollment_service.config;


import org.course.enrollment_service.controller.InscriptionController;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.stereotype.Component;
import jakarta.ws.rs.ApplicationPath;

@Component
@ApplicationPath("/")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(InscriptionController.class);
        register(IllegalArgumentExceptionMapper.class);
        packages("com.course.enrollment_service.controller");
        property(ServletProperties.FILTER_FORWARD_ON_404, true);
    }
}
