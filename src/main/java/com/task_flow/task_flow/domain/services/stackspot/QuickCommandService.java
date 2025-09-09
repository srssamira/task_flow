package com.task_flow.task_flow.domain.services.stackspot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.task_flow.task_flow.domain.entities.TaskEntity;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class QuickCommandService {

    private static final String CLIENT_ID = "4869b6df-aed6-4f04-a55c-93bc7a8113fb";
    private static final String CLIENT_SECRET = "3SEH5Z4HLNqMs3e3pjS5tWxr58jbIBy1lZ30F1X8lnzDY8r7dKS488fi9UsM3IIr";
    private static final String REALM = "zup";
    private static final String TOKEN_URL = "https://idm.stackspot.com/zup/oidc/oauth/token";
    private static final String OPTIMIZATION_URL = "https://genai-code-buddy-api.stackspot.com/v1/quick-commands/create-execution/suggest-optimization";
    private static final String CALLBACK_URL = "https://genai-code-buddy-api.stackspot.com/v1/quick-commands/callback/";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public QuickCommandService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    // 1. Obter o access token
    public String getAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", CLIENT_ID);
        map.add("grant_type", "client_credentials");
        map.add("client_secret", CLIENT_SECRET);


        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(TOKEN_URL, request, Map.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return (String) response.getBody().get("access_token");
        }
        throw new RuntimeException("Failed to get access token: " + response);
    }

    // 2. Usar o access token para chamar o endpoint de otimização
    public String executeOptimizationQuickCommand(List<TaskEntity> taskEntities) {
        String accessToken = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of("input_data", Map.of("tasks", taskEntities));
        try {
            String jsonInput = objectMapper.writeValueAsString(body);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonInput, headers);
            String response = restTemplate.postForObject(OPTIMIZATION_URL, requestEntity, String.class);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }

    // 3. Buscar callback de execução
    public String getQuickCommandOptimizationCallback(String executionId) {
        String accessToken = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                CALLBACK_URL + executionId, HttpMethod.GET, requestEntity, String.class
            );
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }
}