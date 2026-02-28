package buralek.assignment.ground.application.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Offshore Ground Sampling API",
                version = "v1",
                description = "REST API for managing offshore soil samples"
        )
)
public class OpenApiConfig {
}
