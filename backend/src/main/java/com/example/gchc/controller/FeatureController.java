package com.example.gchc.controller;

import com.example.gchc.entity.Feature;
import com.example.gchc.repository.FeatureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/features")
@RequiredArgsConstructor
@Slf4j
public class FeatureController {

    private final FeatureRepository featureRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllFeatures() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Feature> features = featureRepository.findAllByOrderByCreatedAtDesc();
            response.put("success", true);
            response.put("data", features);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取核心优势列表失败", e);
            response.put("success", false);
            response.put("message", "获取核心优势列表失败");
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<Map<String, Object>> getFeatureById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            return featureRepository.findById(id)
                    .map(feature -> {
                        response.put("success", true);
                        response.put("data", feature);
                        return ResponseEntity.ok(response);
                    })
                    .orElseGet(() -> {
                        response.put("success", false);
                        response.put("message", "核心优势不存在");
                        return ResponseEntity.ok(response);
                    });
        } catch (Exception e) {
            log.error("获取核心优势失败", e);
            response.put("success", false);
            response.put("message", "获取核心优势失败");
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createFeature(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String title = (String) request.get("title");
            String description = (String) request.get("description");

            if (title == null || title.isEmpty()) {
                response.put("success", false);
                response.put("message", "标题不能为空");
                return ResponseEntity.ok(response);
            }

            Feature feature = new Feature();
            feature.setTitle(title);
            feature.setDescription(description);
            
            String styleStr = (String) request.get("style");
            if (styleStr != null && !styleStr.isEmpty()) {
                try {
                    feature.setStyle(Integer.parseInt(styleStr));
                } catch (NumberFormatException e) {
                    feature.setStyle(1);
                }
            } else {
                feature.setStyle(1);
            }

            Object sortOrderObj = request.get("sortOrder");
            if (sortOrderObj != null) {
                feature.setSortOrder(Integer.parseInt(sortOrderObj.toString()));
            } else {
                feature.setSortOrder(0);
            }

            Feature savedFeature = featureRepository.save(feature);
            log.info("核心优势保存成功: {}", savedFeature.getId());

            response.put("success", true);
            response.put("data", savedFeature);
            response.put("message", "保存成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("保存核心优势失败", e);
            response.put("success", false);
            response.put("message", "保存失败: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PutMapping("/{id:\\d+}")
    public ResponseEntity<Map<String, Object>> updateFeature(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            return featureRepository.findById(id)
                    .map(existingFeature -> {
                        String title = (String) request.get("title");
                        String description = (String) request.get("description");

                        if (title != null) existingFeature.setTitle(title);
                        if (description != null) existingFeature.setDescription(description);
                        
                        String styleStr = (String) request.get("style");
                        if (styleStr != null && !styleStr.isEmpty()) {
                            try {
                                existingFeature.setStyle(Integer.parseInt(styleStr));
                            } catch (NumberFormatException e) {
                                // 保持原有样式
                            }
                        }

                        Object sortOrderObj = request.get("sortOrder");
                        if (sortOrderObj != null) {
                            existingFeature.setSortOrder(Integer.parseInt(sortOrderObj.toString()));
                        }

                        Feature updatedFeature = featureRepository.save(existingFeature);
                        log.info("核心优势更新成功: {}", id);

                        response.put("success", true);
                        response.put("data", updatedFeature);
                        response.put("message", "更新成功");
                        return ResponseEntity.ok(response);
                    })
                    .orElseGet(() -> {
                        response.put("success", false);
                        response.put("message", "核心优势不存在");
                        return ResponseEntity.ok(response);
                    });
        } catch (Exception e) {
            log.error("更新核心优势失败", e);
            response.put("success", false);
            response.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Map<String, Object>> deleteFeature(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (featureRepository.existsById(id)) {
                featureRepository.deleteById(id);
                log.info("核心优势删除成功: {}", id);
                response.put("success", true);
                response.put("message", "删除成功");
            } else {
                response.put("success", false);
                response.put("message", "核心优势不存在");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("删除核心优势失败", e);
            response.put("success", false);
            response.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}