package com.taskflow.taskflow;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers(disabledWithoutDocker = true)
class IntegrationTests {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void registerProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.enabled", () -> true);
    }

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void tenantIsolationAndRbac() throws Exception {
        String ownerToken = registerAndLogin("owner@example.com", "password123");
        String viewerToken = registerAndLogin("viewer@example.com", "password123");

        Map<String, Object> workspace = createWorkspace(ownerToken, "Test Workspace");
        String workspaceId = workspace.get("id").toString();

        Map<String, Object> project = createProject(ownerToken, workspaceId, "Project A");
        String projectId = project.get("id").toString();

        Map<String, Object> task = createTask(ownerToken, projectId, "Task 1");
        String taskId = task.get("id").toString();

        ResponseEntity<String> viewerTask = exchange(
                HttpMethod.GET,
                "/tasks/" + taskId,
                viewerToken,
                null);
        assertThat(viewerTask.getStatusCode().value()).isEqualTo(404);

        String inviteToken = createInvite(ownerToken, workspaceId, "viewer@example.com", "VIEWER");
        acceptInvite(viewerToken, inviteToken);

        ResponseEntity<String> viewerCreateTask = exchange(
                HttpMethod.POST,
                "/projects/" + projectId + "/tasks",
                viewerToken,
                Map.of("title", "Should Fail"));
        assertThat(viewerCreateTask.getStatusCode().value()).isEqualTo(403);
    }

    private String registerAndLogin(String email, String password) throws Exception {
        ResponseEntity<String> register = exchange(
                HttpMethod.POST,
                "/auth/register",
                null,
                Map.of("email", email, "password", password));
        assertThat(register.getStatusCode().is2xxSuccessful()).isTrue();

        ResponseEntity<String> login = exchange(
                HttpMethod.POST,
                "/auth/login",
                null,
                Map.of("email", email, "password", password));
        assertThat(login.getStatusCode().is2xxSuccessful()).isTrue();

        Map<?, ?> data = objectMapper.readValue(login.getBody(), Map.class);
        return data.get("accessToken").toString();
    }

    private Map<String, Object> createWorkspace(String token, String name) throws Exception {
        ResponseEntity<String> response = exchange(
                HttpMethod.POST,
                "/workspaces",
                token,
                Map.of("name", name));
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        return objectMapper.readValue(response.getBody(), Map.class);
    }

    private Map<String, Object> createProject(String token, String workspaceId, String name) throws Exception {
        ResponseEntity<String> response = exchange(
                HttpMethod.POST,
                "/workspaces/" + workspaceId + "/projects",
                token,
                Map.of("name", name));
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        return objectMapper.readValue(response.getBody(), Map.class);
    }

    private Map<String, Object> createTask(String token, String projectId, String title) throws Exception {
        ResponseEntity<String> response = exchange(
                HttpMethod.POST,
                "/projects/" + projectId + "/tasks",
                token,
                Map.of("title", title));
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        return objectMapper.readValue(response.getBody(), Map.class);
    }

    private String createInvite(String token, String workspaceId, String email, String role) throws Exception {
        ResponseEntity<String> response = exchange(
                HttpMethod.POST,
                "/workspaces/" + workspaceId + "/invites",
                token,
                Map.of("email", email, "role", role));
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Map<?, ?> data = objectMapper.readValue(response.getBody(), Map.class);
        return data.get("token").toString();
    }

    private void acceptInvite(String token, String inviteToken) {
        ResponseEntity<String> response = exchange(
                HttpMethod.POST,
                "/invites/accept?token=" + inviteToken,
                token,
                null);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    private ResponseEntity<String> exchange(
            HttpMethod method,
            String path,
            String token,
            Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (token != null) {
            headers.setBearerAuth(token);
        }

        HttpEntity<Object> entity = new HttpEntity<>(body, headers);
        String url = "http://localhost:" + port + path;
        return restTemplate.exchange(url, method, entity, String.class);
    }
}

