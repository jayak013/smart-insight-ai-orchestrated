package com.example.smartinsight.service;

import com.example.smartinsight.repository.ProductRepository;
import com.example.smartinsight.repository.SalesRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AIService {
    private static final Logger log = LoggerFactory.getLogger(AIService.class);
    private final ChatModel chatModel;
    private final ProductRepository productRepository;
    private final SalesRecordRepository salesRecordRepository;
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AIService(ChatModel chatModel,
                     ProductRepository productRepository,
                     SalesRecordRepository salesRecordRepository,
                     JdbcTemplate jdbcTemplate) {
        this.chatModel = chatModel;
        this.productRepository = productRepository;
        this.salesRecordRepository = salesRecordRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<String, Object> getAdvancedInsights(String userQuery) {
        String schemaContext = """
                Table: PRODUCT
                Columns: ID (Long), NAME (String), CATEGORY (String), PRICE (Double), STOCK_QUANTITY (Integer), LAST_UPDATED (Timestamp)
                
                Table: SALES_RECORD
                Columns: ID (Long), PRODUCT_ID (Long, FK to PRODUCT.ID), QUANTITY_SOLD (Integer), SALE_PRICE (Double), SALE_DATE (Timestamp)
                """;

        String systemText = """
                You are a smart business analyst for a retail company.
                You have access to a H2 database with the following schema:
                {schema}
                
                Your goal is to answer the user's question by either:
                1. Generating a SQL query if the user asks for specific data or statistics.
                2. Providing a narrative analysis if the user asks for advice or predictions.
                
                If you generate SQL, wrap it in [SQL] and [/SQL] tags.
                If you provide analysis, be professional and data-driven.
                
                ALWAYS output your response in valid JSON format with these fields:
                - 'interpretation': What you think the user wants.
                - 'sql': The generated SQL query (if applicable, otherwise null).
                - 'analysis': Your textual response or insight.
                - 'suggestion': A recommended business action.
                """;

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemText);
        var systemMessage = systemPromptTemplate.createMessage(Map.of("schema", schemaContext));
        var userMessage = new UserMessage(userQuery);

        Prompt prompt = new Prompt(java.util.List.of(systemMessage, userMessage));
        
        try {
            log.debug("Sending advanced prompt to AI model...");
            String rawAiResponse = chatModel.call(prompt).getResult().getOutput().getText();
            log.debug("Received raw AI response: {}", rawAiResponse);
            
            // Basic JSON cleaning if LLM adds markdown blocks
            String cleanJson = rawAiResponse.replaceAll("```json", "").replaceAll("```", "").trim();
            
            Map<String, Object> responseMap = new java.util.HashMap<>();
            responseMap.put("ai_response", cleanJson);

            try {
                Map<String, Object> parsedResponse = objectMapper.readValue(cleanJson, Map.class);
                String sql = (String) parsedResponse.get("sql");
                if (sql != null && !sql.isBlank()) {
                    log.info("Executing AI-generated SQL: {}", sql);
                    List<Map<String, Object>> results = executeSql(sql);
                    responseMap.put("data", results);
                }
            } catch (Exception e) {
                log.warn("Failed to parse AI response or execute SQL: {}", e.getMessage());
            }

            return responseMap;

        } catch (Exception e) {
            log.error("AI Service advanced insight failure", e);
            return Map.of("error", "AI Service unavailable: " + e.getMessage());
        }
    }

    public List<Map<String, Object>> executeSql(String sql) {
        try {
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            log.error("SQL execution failed: {}", sql, e);
            return List.of(Map.of("error", e.getMessage()));
        }
    }

    public String getInsights(String userQuery) {
        // Keeping the old method for backward compatibility or simple queries
        String productsContext = productRepository.findAll().stream()
                .map(p -> String.format("ID: %d, Name: %s, Category: %s, Price: %.2f, Stock: %d",
                        p.getId(), p.getName(), p.getCategory(), p.getPrice(), p.getStockQuantity()))
                .collect(Collectors.joining("\n"));

        String systemText = """
                You are a data analyst assistant. You have access to the following product data:
                {context}
                
                Answer the user's question based ONLY on this data. 
                """;

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemText);
        var systemMessage = systemPromptTemplate.createMessage(Map.of("context", productsContext));
        var userMessage = new UserMessage(userQuery);

        Prompt prompt = new Prompt(java.util.List.of(systemMessage, userMessage));
        
        try {
            log.debug("Sending simple prompt to AI model...");
            String response = chatModel.call(prompt).getResult().getOutput().getText();
            log.debug("Received simple AI response");
            return response;
        } catch (Exception e) {
            log.error("AI Service simple insight failure", e);
            return "Error: " + e.getMessage();
        }
    }
}
