package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AIServiceGateway {

    @Value("${groq.api.key:}")
    private String apiKey;

    private static final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";

    @SuppressWarnings({"unchecked", "rawtypes"})
    public String guiYeuCauAI(String prompt) throws Exception {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new RuntimeException("Chưa cấu hình API Key");
        }

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(10000);
        RestTemplate restTemplate = new RestTemplate(factory);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey.trim());

        Map message = new HashMap();
        message.put("role", "user");
        message.put("content", prompt);

        Map requestBody = new HashMap();
        requestBody.put("model", "openai/gpt-oss-120b");
        requestBody.put("messages", Collections.singletonList(message));
        requestBody.put("temperature", 0.3);

        HttpEntity entity = new HttpEntity(requestBody, headers);

        ResponseEntity response = restTemplate.postForEntity(GROQ_URL, entity, Map.class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map body = (Map) response.getBody();
            List choices = (List) body.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map firstChoice = (Map) choices.get(0);
                Map msgMap = (Map) firstChoice.get("message");
                if (msgMap != null && msgMap.get("content") != null) {
                    return (String) msgMap.get("content");
                }
            }
        }
        throw new RuntimeException("Phản hồi từ dịch vụ AI không hợp lệ");
    }
}