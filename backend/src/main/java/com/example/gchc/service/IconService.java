package com.example.gchc.service;

import com.example.gchc.entity.Icon;
import com.example.gchc.repository.IconRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class IconService {

    private final IconRepository iconRepository;

    private static final String UPLOAD_DIR = new File(".").getAbsolutePath() + File.separator + "uploads";

    public List<Icon> getAllIcons() {
        return iconRepository.findAll();
    }

    public List<Icon> getEnabledIcons() {
        return iconRepository.findAllEnabled();
    }

    public Icon getIconById(Long id) {
        return iconRepository.findById(id).orElse(null);
    }

    public Icon createIcon(Map<String, String> request) {
        String imageUrl = request.get("imageUrl");
        String name = request.get("name");
        String description = request.get("description");

        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new IllegalArgumentException("图标图片不能为空");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("图标名称不能为空");
        }
        if (iconRepository.existsByImageUrl(imageUrl)) {
            throw new IllegalArgumentException("图标图片已存在");
        }

        Icon icon = new Icon();
        icon.setImageUrl(imageUrl);
        icon.setName(name);
        icon.setDescription(description);
        icon.setEnabled(true);
        icon.setSortOrder(0);
        icon.setCreatedAt(LocalDateTime.now());
        icon.setUpdatedAt(LocalDateTime.now());

        iconRepository.insert(icon);
        log.info("图标创建成功: {}", icon.getId());
        return icon;
    }

    public Icon updateIcon(Long id, Map<String, String> request) {
        Icon icon = iconRepository.findById(id).orElse(null);
        if (icon == null) {
            throw new IllegalArgumentException("图标不存在");
        }

        String imageUrl = request.get("imageUrl");
        String name = request.get("name");
        String description = request.get("description");

        if (imageUrl != null && !imageUrl.equals(icon.getImageUrl()) && iconRepository.existsByImageUrl(imageUrl)) {
            throw new IllegalArgumentException("图标图片已存在");
        }

        if (imageUrl != null) icon.setImageUrl(imageUrl);
        if (name != null) icon.setName(name);
        if (description != null) icon.setDescription(description);
        icon.setUpdatedAt(LocalDateTime.now());

        iconRepository.update(icon);
        log.info("图标更新成功: {}", id);
        return icon;
    }

    public void deleteIcon(Long id) {
        if (!iconRepository.existsById(id)) {
            throw new IllegalArgumentException("图标不存在");
        }
        iconRepository.deleteById(id);
        log.info("图标删除成功: {}", id);
    }

    public String uploadIconImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("请选择要上传的图片");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !isAllowedImage(originalFilename)) {
            throw new IllegalArgumentException("不支持的文件格式，仅支持jpg、jpeg、png、gif、svg、webp");
        }

        try {
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = "icon_" + UUID.randomUUID().toString().substring(0, 8) + extension;
            Path filePath = Paths.get(UPLOAD_DIR, newFilename);
            Files.write(filePath, file.getBytes());

            String fileUrl = "/uploads/" + newFilename;
            log.info("图标图片上传成功: {}", fileUrl);
            return fileUrl;
        } catch (IOException e) {
            log.error("图标图片上传失败", e);
            throw new RuntimeException("图片上传失败: " + e.getMessage());
        }
    }

    private boolean isAllowedImage(String filename) {
        String lower = filename.toLowerCase();
        return lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png")
                || lower.endsWith(".gif") || lower.endsWith(".svg") || lower.endsWith(".webp");
    }
}