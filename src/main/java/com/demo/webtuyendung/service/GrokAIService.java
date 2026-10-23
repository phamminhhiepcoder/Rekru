package com.demo.webtuyendung.service;

import com.demo.webtuyendung.dto.CVAnalysisResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class GrokAIService {

    @Value("${grok.api.key}")
    private String apiKey;

    @Value("${grok.api.url}")
    private String apiUrl;

    @Value("${grok.model}")
    private String model;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public GrokAIService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    public CVAnalysisResult analyze(String criteria, String cvText) {
        try {
            log.info("Analyzing CV with Grok model: {}", model);
            String prompt = createPrompt(criteria, cvText);

            // Tạo request body
            Map<String, Object> request = Map.of(
                "model", model,
                "temperature", 0.3,
                "messages", List.of(
                    Map.of("role", "system", "content", "Bạn là chuyên gia phân tích CV và tuyển dụng chuyên nghiệp. Hãy phân tích chính xác và trả về JSON đúng format."),
                    Map.of("role", "user", "content", prompt)
                ),
                "response_format", Map.of("type", "json_object")
            );

            // Gọi Grok API
            String responseBody = webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(30))
                .block();

            log.info("Grok API Response: {}", responseBody);

            // Parse response
            Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");
            
            if (choices == null || choices.isEmpty()) {
                throw new RuntimeException("Empty response from Grok AI");
            }

            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            String content = (String) message.get("content");

            CVAnalysisResult result = objectMapper.readValue(content, CVAnalysisResult.class);
            
            log.info("CV analyzed successfully - score: {}", result.getScore());
            return result;

        } catch (Exception e) {
            log.error("Error analyzing CV with Grok AI", e);
            throw new RuntimeException("Failed to analyze CV: " + e.getMessage(), e);
        }
    }

    private String createPrompt(String criteria, String cvText) {
        String truncatedCV = cvText.length() > 8000 
            ? cvText.substring(0, 8000) + "..." 
            : cvText;

        return String.format("""
            Bạn là chuyên gia tuyển dụng AI. Phân tích CV sau theo tiêu chí tuyển dụng.
            
            TIÊU CHÍ TUYỂN DỤNG:
            %s
            
            NỘI DUNG CV:
            %s
            
            Trả về JSON theo format SAU ĐÂY (bắt buộc):
            {
              "score": 85,
              "name": "Nguyễn Văn A",
              "email": "email@example.com",
              "phone": "0912345678",
              "strengths": [
                "5 năm kinh nghiệm React/Node.js",
                "Có kinh nghiệm lead team",
                "Thành thạo tiếng Anh"
              ],
              "weaknesses": [
                "Chưa có kinh nghiệm TypeScript",
                "Chưa dùng AWS"
              ],
              "summary": "Ứng viên xuất sắc với kinh nghiệm phong phú..."
            }
            
            LƯU Ý:
            - score: 0-100 (phản ánh độ phù hợp)
            - strengths: 3-5 điểm mạnh cụ thể
            - weaknesses: 1-3 điểm yếu
            - summary: 1-2 câu đánh giá tổng quan
            - BẮT BUỘC trả về đúng JSON format trên
            """, criteria, truncatedCV);
    }
}