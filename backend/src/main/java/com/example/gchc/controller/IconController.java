package com.example.gchc.controller;

import com.example.gchc.entity.Icon;
import com.example.gchc.service.IconService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/icons")
@RequiredArgsConstructor
@Slf4j
public class IconController {

    private final IconService iconService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllIcons() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Icon> icons = iconService.getAllIcons();
            response.put("success", true);
            response.put("data", icons);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取图标列表失败", e);
            response.put("success", false);
            response.put("message", "获取图标列表失败");
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/public")
    public ResponseEntity<Map<String, Object>> getEnabledIcons() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Icon> icons = iconService.getEnabledIcons();
            response.put("success", true);
            response.put("data", icons);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取图标列表失败", e);
            response.put("success", false);
            response.put("message", "获取图标列表失败");
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<Map<String, Object>> getIconById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Icon icon = iconService.getIconById(id);
            if (icon != null) {
                response.put("success", true);
                response.put("data", icon);
            } else {
                response.put("success", false);
                response.put("message", "图标不存在");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取图标失败", e);
            response.put("success", false);
            response.put("message", "获取图标失败");
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadIconImage(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            String imageUrl = iconService.uploadIconImage(file);
            response.put("success", true);
            response.put("data", imageUrl);
            response.put("message", "图片上传成功");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("图标图片上传失败", e);
            response.put("success", false);
            response.put("message", "上传失败: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createIcon(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String imageUrl = request.get("imageUrl");
            String name = request.get("name");

            if (imageUrl == null || imageUrl.isEmpty()) {
                response.put("success", false);
                response.put("message", "图标图片不能为空");
                return ResponseEntity.ok(response);
            }
            if (name == null || name.isEmpty()) {
                response.put("success", false);
                response.put("message", "图标名称不能为空");
                return ResponseEntity.ok(response);
            }

            Icon icon = iconService.createIcon(request);
            response.put("success", true);
            response.put("data", icon);
            response.put("message", "创建成功");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("创建图标失败", e);
            response.put("success", false);
            response.put("message", "创建失败: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PutMapping("/{id:\\d+}")
    public ResponseEntity<Map<String, Object>> updateIcon(@PathVariable Long id, @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Icon icon = iconService.updateIcon(id, request);
            response.put("success", true);
            response.put("data", icon);
            response.put("message", "更新成功");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("更新图标失败", e);
            response.put("success", false);
            response.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Map<String, Object>> deleteIcon(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            iconService.deleteIcon(id);
            response.put("success", true);
            response.put("message", "删除成功");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("删除图标失败", e);
            response.put("success", false);
            response.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}