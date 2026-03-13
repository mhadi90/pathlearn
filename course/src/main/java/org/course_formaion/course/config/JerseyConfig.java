package org.course_formaion.course.config;


import com.fasterxml.jackson.core.util.JacksonFeature;
import org.course_formaion.course.controller.CourseResource;
import org.course_formaion.course.controller.ModuleResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(JacksonFeature.class);
        register(CourseResource.class);
        register(ModuleResource.class);
    }
}
