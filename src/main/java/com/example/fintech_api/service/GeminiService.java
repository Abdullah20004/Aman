package com.example.fintech_api.service;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

    @Value("${gemini.api.key}")
    private String apiKey;

    public String generateSqlFromDescription(String description) throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(GEMINI_API_URL + "?key=" + apiKey);
            post.setHeader("Content-Type", "application/json");

            String prompt = "Generate a valid PostgreSQL SELECT query based on the following description: " + description +
                    ". The query must target tables in the 'gle_prod_config' or 'gle_prod_data' schemas and must not include INSERT, UPDATE, DELETE, or DROP statements.";
            String requestBody = "{\"contents\":[{\"parts\":[{\"text\":\"" + prompt + "\"}]}]}";

            post.setEntity(new StringEntity(requestBody));

            try (CloseableHttpResponse response = client.execute(post)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                String sql = extractSqlFromResponse(responseBody);
                validateSql(sql);
                return sql;
            }
        }
    }

    private String extractSqlFromResponse(String responseBody) {
        int start = responseBody.indexOf("\"text\":\"") + 8;
        int end = responseBody.indexOf("\"", start);
        return responseBody.substring(start, end).replace("\\n", " ").trim();
    }

    private void validateSql(String sql) throws Exception {
        String lowerSql = sql.toLowerCase();
        if (lowerSql.contains("insert") || lowerSql.contains("update") ||
                lowerSql.contains("delete") || lowerSql.contains("drop")) {
            throw new IllegalArgumentException("SQL query must be a SELECT statement");
        }
    }
}