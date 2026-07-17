package com.example.gchc.controller;

import com.example.gchc.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/public/upload")
public class PublicUploadController {

    private static final Logger logger = LoggerFactory.getLogger(PublicUploadController.class);

    @Value("${server.port:8080}")
    private String serverPort;

    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".bmp"};

    @GetMapping("/test")
    public ResponseEntity<ApiResponse<String>> testUpload() {
        logger.info("公共上传端点测试被访问");
        return ResponseEntity.ok(ApiResponse.success("公共上传端点测试成功"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadFile(@RequestParam("file") MultipartFile file) {
        logger.info("收到公共文件上传请求，文件名: {}, 大小: {} bytes", file.getOriginalFilename(), file.getSize());

        if (file.isEmpty()) {
            logger.warn("上传文件为空");
            return ResponseEntity.badRequest().body(ApiResponse.error("请选择要上传的文件"));
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !isAllowedExtension(originalFilename)) {
            logger.warn("文件格式不支持: {}", originalFilename);
            return ResponseEntity.badRequest().body(ApiResponse.error("不支持的文件格式，仅支持jpg、jpeg、png、gif、bmp格式"));
        }

        try {
            String uploadDir = new File(".").getAbsolutePath() + File.separator + "uploads";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID().toString() + extension;
            Path filePath = Paths.get(uploadDir, newFilename);
            Files.write(filePath, file.getBytes());
            logger.info("文件保存成功: {}", filePath.toString());

            String fileUrl = "/uploads/" + newFilename;
            logger.info("文件URL: {}", fileUrl);

            Map<String, String> result = new HashMap<>();
            result.put("url", fileUrl);

            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (IOException e) {
            logger.error("文件上传失败", e);
            return ResponseEntity.internalServerError().body(ApiResponse.error("文件上传失败: " + e.getMessage()));
        }
    }

    private boolean isAllowedExtension(String filename) {
        String lowerFilename = filename.toLowerCase();
        for (String extension : ALLOWED_EXTENSIONS) {
            if (lowerFilename.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}