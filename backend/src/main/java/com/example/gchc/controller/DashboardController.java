package com.example.gchc.controller;

import com.example.gchc.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
    private final JdbcTemplate jdbcTemplate;

    public DashboardController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        logger.info("获取仪表盘统计数据");
        try {
            Map<String, Object> stats = new HashMap<>();
            
            stats.put("carouselCount", getCount("SELECT COUNT(*) FROM carousels"));
            stats.put("featureCount", getCount("SELECT COUNT(*) FROM features"));
            stats.put("productCount", getCount("SELECT COUNT(*) FROM products"));
            stats.put("articleCount", getCount("SELECT COUNT(*) FROM articles"));
            stats.put("categoryCount", getCount("SELECT COUNT(*) FROM categories"));
            stats.put("teamCount", getCount("SELECT COUNT(*) FROM team_members"));
            stats.put("awardCount", getCount("SELECT COUNT(*) FROM awards"));
            stats.put("messageCount", getCount("SELECT COUNT(*) FROM messages"));
            stats.put("publishedArticleCount", getCount("SELECT COUNT(*) FROM articles WHERE published = TRUE"));
            stats.put("enabledProductCount", getCount("SELECT COUNT(*) FROM products WHERE enabled = TRUE"));

            return ResponseEntity.ok(ApiResponse.success(stats));
        } catch (Exception e) {
            logger.error("获取统计数据失败", e);
            return ResponseEntity.internalServerError().body(ApiResponse.error("获取统计数据失败"));
        }
    }

    @GetMapping("/messages/latest")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getLatestMessages() {
        logger.info("获取最新留言");
        try {
            String sql = "SELECT id, name, email, subject, message, status, created_at FROM messages ORDER BY created_at DESC LIMIT 10";
            List<Map<String, Object>> messages = jdbcTemplate.queryForList(sql);
            return ResponseEntity.ok(ApiResponse.success(messages));
        } catch (Exception e) {
            logger.error("获取最新留言失败", e);
            return ResponseEntity.internalServerError().body(ApiResponse.error("获取最新留言失败"));
        }
    }

    private int getCount(String sql) {
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
            return count != null ? count : 0;
        } catch (Exception e) {
            logger.warn("执行统计SQL失败: {} - {}", sql, e.getMessage());
            return 0;
        }
    }
}
