package com.example.smartinsight.controller;

import com.example.smartinsight.service.AIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
@Tag(name = "AI Orchestrator", description = "Endpoints for AI-powered insights and SQL execution")
public class AIController {
    private static final Logger log = LoggerFactory.getLogger(AIController.class);

    private final AIService aiService;

    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/query")
    @Operation(summary = "Simple AI Query", description = "Ask natural language questions about the existing product list.")
    public Map<String, String> queryData(@RequestBody Map<String, String> request) {
        String userQuery = request.get("query");
        log.info("Received simple AI query: {}", userQuery);
        String insight = aiService.getInsights(userQuery);
        return Map.of("insight", insight);
    }

    @PostMapping("/advanced-query")
    @Operation(summary = "Advanced AI BI Query", description = "Uses Text-to-SQL to provide deep insights using both Product and Sales history.")
    public Map<String, Object> getAdvancedInsights(@RequestBody Map<String, String> request) {
        String query = request.get("query");
        log.info("Received advanced AI query: {}", query);
        return aiService.getAdvancedInsights(query);
    }

    @PostMapping("/execute-sql")
    @Operation(summary = "Execute SQL", description = "Directly execute a SQL query against the H2 database.")
    public List<Map<String, Object>> executeSql(@RequestBody Map<String, String> request) {
        String sql = request.get("sql");
        log.info("Received direct SQL execution request: {}", sql);
        return aiService.executeSql(sql);
    }

}
