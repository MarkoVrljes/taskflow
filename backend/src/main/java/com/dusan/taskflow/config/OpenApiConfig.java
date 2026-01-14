package com.dusan.taskflow.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI taskflowOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Taskflow API")
                        .description("Team Task API for workspaces, projects, tasks, and comments.")
                        .version("0.1.0"));
    }

    @Bean
    public GroupedOpenApi apiGroup() {
        return GroupedOpenApi.builder()
                .group("taskflow")
                .pathsToMatch("/**")
                .build();
    }
}
