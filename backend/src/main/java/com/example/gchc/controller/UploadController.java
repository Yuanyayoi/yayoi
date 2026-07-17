/**
 * 文件上传控制器
 * 处理图片等文件的上传请求
 * 所有接口路径前缀: /api
 */
package com.example.gchc.controller;

import com.example.gchc.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @RestController 标识这是一个 REST API 控制器
 * @RequestMapping("/api") 指定所有接口的基础路径
 */
@RestController
@RequestMapping("/api")
public class UploadController {

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    private static final String MIGRATION_MARKER = ".migration_completed";

    @Value("${server.port:8080}")
    private String serverPort;

    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".svg"};

    private String uploadDir;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        uploadDir = new File("uploads").getAbsolutePath();
        logger.info("========================================");
        logger.info("上传目录初始化: {}", uploadDir);
        
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            logger.info("创建上传根目录结果: {}", created);
        }
        
        initFolders();
        
        try {
            runImageMigration();
        } catch (Exception e) {
            logger.error("图片迁移执行出错", e);
        }
        logger.info("========================================");
    }

    @GetMapping("/upload/test")
    public ResponseEntity<ApiResponse<String>> testUpload() {
        logger.info("测试上传端点被访问");
        return ResponseEntity.ok(ApiResponse.success("上传端点测试成功"));
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "common") String folder) {
        logger.info("收到文件上传请求，文件名: {}, 大小: {} bytes, 目录: {}", file.getOriginalFilename(), file.getSize(), folder);

        if (file.isEmpty()) {
            logger.warn("上传文件为空");
            return ResponseEntity.badRequest().body(ApiResponse.error("请选择要上传的文件"));
        }
        
        folder = sanitizeFolderName(folder);

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !isAllowedExtension(originalFilename)) {
            logger.warn("文件格式不支持: {}", originalFilename);
            return ResponseEntity.badRequest().body(ApiResponse.error("不支持的文件格式，仅支持jpg、jpeg、png、gif、bmp、svg格式"));
        }
        
        if (!isSvgFile(originalFilename) && !isValidImageContent(file)) {
            logger.warn("文件内容验证失败（Magic Number 不匹配）: {}", originalFilename);
            return ResponseEntity.badRequest().body(ApiResponse.error("文件内容与扩展名不匹配"));
        }

        try {
            String extension = sanitizeExtension(originalFilename);
            String newFilename = UUID.randomUUID().toString() + extension;
            
            Path uploadPath = Paths.get(uploadDir).toRealPath();
            Path folderPath = uploadPath.resolve(folder).normalize();
            
            if (!folderPath.startsWith(uploadPath)) {
                logger.error("路径遍历攻击检测（目录）: {}", folder);
                return ResponseEntity.badRequest().body(ApiResponse.error("非法的目录路径"));
            }
            
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
                logger.info("创建上传子目录: {}", folderPath);
            }
            
            Path filePath = folderPath.resolve(newFilename).normalize();
            if (!filePath.startsWith(uploadPath)) {
                logger.error("路径遍历攻击检测: {}", newFilename);
                return ResponseEntity.badRequest().body(ApiResponse.error("非法的文件路径"));
            }
            Files.write(filePath, file.getBytes());
            logger.info("文件保存成功: {}", filePath.toString());

            String fileUrl = "/uploads/" + folder + "/" + newFilename;
            logger.info("文件URL: {}", fileUrl);

            Map<String, String> result = new HashMap<>();
            result.put("url", fileUrl);

            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (IOException e) {
            logger.error("文件上传失败", e);
            return ResponseEntity.internalServerError().body(ApiResponse.error("文件上传失败，请稍后重试"));
        }
    }

    @DeleteMapping("/upload/delete")
    public ResponseEntity<ApiResponse<String>> deleteFile(@RequestParam("fileUrl") String fileUrl) {
        logger.info("收到文件删除请求，URL: {}", fileUrl);

        try {
            String relativePath;
            if (fileUrl.startsWith("/uploads/")) {
                relativePath = fileUrl.substring("/uploads/".length());
            } else if (fileUrl.startsWith("uploads/")) {
                relativePath = fileUrl.substring("uploads/".length());
            } else {
                relativePath = fileUrl;
            }
            
            if (relativePath.contains("..")) {
                logger.warn("非法的文件路径（包含..）: {}", relativePath);
                return ResponseEntity.badRequest().body(ApiResponse.error("非法的文件路径"));
            }
            
            Path uploadPath = Paths.get(uploadDir).toRealPath();
            Path filePath = uploadPath.resolve(relativePath).normalize();
            if (!filePath.startsWith(uploadPath)) {
                logger.warn("路径遍历攻击检测: {}", relativePath);
                return ResponseEntity.badRequest().body(ApiResponse.error("非法的文件路径"));
            }

            if (Files.exists(filePath)) {
                Files.delete(filePath);
                logger.info("文件删除成功: {}", filePath.toString());
                return ResponseEntity.ok(ApiResponse.success("文件删除成功"));
            } else {
                logger.warn("文件不存在: {}", filePath.toString());
                return ResponseEntity.badRequest().body(ApiResponse.error("文件不存在"));
            }
        } catch (IOException e) {
            logger.error("文件删除失败", e);
            return ResponseEntity.internalServerError().body(ApiResponse.error("文件删除失败，请稍后重试"));
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
    
    private boolean isSvgFile(String filename) {
        return filename.toLowerCase().endsWith(".svg");
    }
    
    private boolean isValidImageContent(MultipartFile file) {
        try {
            byte[] header = new byte[8];
            System.arraycopy(file.getBytes(), 0, header, 0, Math.min(file.getBytes().length, 8));
            if ((header[0] & 0xFF) == 0xFF && (header[1] & 0xFF) == 0xD8 && (header[2] & 0xFF) == 0xFF) return true;
            if (header[0] == (byte)0x89 && header[1] == 0x50 && header[2] == 0x4E && header[3] == 0x47) return true;
            if (header[0] == 0x47 && header[1] == 0x49 && header[2] == 0x46) return true;
            if (header[0] == 0x42 && header[1] == 0x4D) return true;
            return false;
        } catch (IOException e) {
            logger.error("Magic Number 验证失败", e);
            return false;
        }
    }
    
    private String sanitizeExtension(String filename) {
        String lower = filename.toLowerCase();
        for (String ext : ALLOWED_EXTENSIONS) {
            if (lower.endsWith(ext)) {
                return ext;
            }
        }
        return ".jpg";
    }
    
    private String sanitizeFolderName(String folder) {
        if (folder == null || folder.isEmpty()) {
            return "common";
        }
        String sanitized = folder.replaceAll("[^a-zA-Z0-9_-]", "").toLowerCase();
        if (sanitized.isEmpty() || sanitized.contains("..")) {
            return "common";
        }
        return sanitized;
    }
    
    private void initFolders() {
        String[] folders = {"carousel", "product", "article", "logo", "team", "award", "avatar", "common"};
        for (String folder : folders) {
            File folderDir = new File(uploadDir, folder);
            if (!folderDir.exists()) {
                boolean created = folderDir.mkdirs();
                if (created) {
                    logger.info("创建上传子目录: {}", folderDir.getAbsolutePath());
                }
            } else {
                logger.info("上传子目录已存在: {}", folderDir.getAbsolutePath());
            }
        }
    }

    private boolean needsMigration(String url) {
        if (url == null || url.isEmpty()) return false;
        if (!url.startsWith("/uploads/")) return false;
        String path = url.substring("/uploads/".length());
        return !path.contains("/");
    }

    private String extractFilename(String url) {
        if (url == null) return null;
        int lastSlash = url.lastIndexOf("/");
        return lastSlash >= 0 ? url.substring(lastSlash + 1) : url;
    }

    private String migrateOneImage(String url, String folder) {
        if (!needsMigration(url)) return url;

        String filename = extractFilename(url);
        if (filename == null) return url;

        Path source = Paths.get(uploadDir).resolve(filename).normalize();
        Path targetDir = Paths.get(uploadDir).resolve(folder);
        Path target = targetDir.resolve(filename).normalize();

        try {
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }

            if (Files.exists(source)) {
                if (!Files.exists(target)) {
                    Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
                    logger.info("  ✓ 移动文件: {} -> {}/{}", filename, folder, filename);
                    return "/uploads/" + folder + "/" + filename;
                } else {
                    Files.delete(source);
                    logger.info("  ✓ 目标已存在，删除源文件: {}", filename);
                    return "/uploads/" + folder + "/" + filename;
                }
            } else if (Files.exists(target)) {
                logger.info("  ✓ 源文件已移动，仅更新URL: {}", filename);
                return "/uploads/" + folder + "/" + filename;
            } else {
                logger.warn("  ✗ 文件不存在（根目录和目标目录都找不到）: {}", url);
                return url;
            }
        } catch (IOException e) {
            logger.error("  ✗ 迁移文件失败: {} -> {}", url, folder, e);
            return url;
        }
    }

    private void runImageMigration() {
        File markerFile = new File(uploadDir, MIGRATION_MARKER);
        if (markerFile.exists()) {
            logger.info("图片迁移已完成（标记文件存在），跳过");
            return;
        }

        logger.info(">>> 开始图片分类迁移...");
        int totalMigrated = 0;

        try {
            Pattern urlPattern = Pattern.compile("(/uploads/[a-zA-Z0-9._-]+\\.(jpg|jpeg|png|gif|bmp|svg))");

            totalMigrated += migrateTableColumn("products", "id", "image", "product", urlPattern);
            totalMigrated += migrateTableColumn("articles", "id", "cover_image", "article", urlPattern);
            totalMigrated += migrateTableColumn("carousels", "id", "image_url", "carousel", urlPattern);
            totalMigrated += migrateTableColumn("team_members", "id", "avatar", "team", urlPattern);
            totalMigrated += migrateTableColumn("awards", "id", "image", "award", urlPattern);
            totalMigrated += migrateTableColumn("icons", "id", "image_url", "common", urlPattern);
            totalMigrated += migrateSiteSettings(urlPattern);
            totalMigrated += moveRemainingFilesToCommon();

            markerFile.createNewFile();
            logger.info(">>> 图片迁移完成！共迁移 {} 个文件", totalMigrated);
        } catch (Exception e) {
            logger.error(">>> 图片迁移过程中发生错误", e);
        }
    }

    private int migrateTableColumn(String tableName, String idColumn, String urlColumn, String folder, Pattern urlPattern) {
        logger.info("  处理表 {}.{} ...", tableName, urlColumn);
        int count = 0;
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                "SELECT " + idColumn + ", " + urlColumn + " FROM " + tableName + " WHERE " + urlColumn + " IS NOT NULL AND " + urlColumn + " LIKE '/uploads/%'"
            );

            for (Map<String, Object> row : rows) {
                Long id = ((Number) row.get(idColumn)).longValue();
                String oldUrl = (String) row.get(urlColumn);
                if (oldUrl == null) continue;

                Matcher matcher = urlPattern.matcher(oldUrl);
                StringBuffer sb = new StringBuffer();
                boolean changed = false;

                while (matcher.find()) {
                    String matched = matcher.group(1);
                    if (needsMigration(matched)) {
                        String replaced = migrateOneImage(matched, folder);
                        matcher.appendReplacement(sb, Matcher.quoteReplacement(replaced));
                        if (!replaced.equals(matched)) {
                            changed = true;
                            count++;
                        }
                    } else {
                        matcher.appendReplacement(sb, Matcher.quoteReplacement(matched));
                    }
                }
                matcher.appendTail(sb);
                String newUrl = sb.toString();

                if (changed) {
                    jdbcTemplate.update(
                        "UPDATE " + tableName + " SET " + urlColumn + " = ?, updated_at = NOW(6) WHERE " + idColumn + " = ?",
                        newUrl, id
                    );
                }
            }
            logger.info("  表 {}.{} 处理完成，迁移 {} 个文件引用", tableName, urlColumn, count);
        } catch (Exception e) {
            logger.warn("  处理表 {}.{} 时出错: {}", tableName, urlColumn, e.getMessage());
        }
        return count;
    }

    private int migrateSiteSettings(Pattern urlPattern) {
        logger.info("  处理表 site_settings ...");
        int count = 0;
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                "SELECT id, setting_key, setting_value FROM site_settings WHERE setting_value LIKE '%/uploads/%'"
            );

            for (Map<String, Object> row : rows) {
                Long id = ((Number) row.get("id")).longValue();
                String key = (String) row.get("setting_key");
                String value = (String) row.get("setting_value");
                if (value == null) continue;

                String folder = determineSettingFolder(key);
                Matcher matcher = urlPattern.matcher(value);
                StringBuffer sb = new StringBuffer();
                boolean changed = false;

                while (matcher.find()) {
                    String matched = matcher.group(1);
                    if (needsMigration(matched)) {
                        String replaced = migrateOneImage(matched, folder);
                        matcher.appendReplacement(sb, Matcher.quoteReplacement(replaced));
                        if (!replaced.equals(matched)) {
                            changed = true;
                            count++;
                        }
                    } else {
                        matcher.appendReplacement(sb, Matcher.quoteReplacement(matched));
                    }
                }
                matcher.appendTail(sb);
                String newValue = sb.toString();

                if (changed) {
                    jdbcTemplate.update(
                        "UPDATE site_settings SET setting_value = ?, updated_at = NOW(6) WHERE id = ?",
                        newValue, id
                    );
                }
            }
            logger.info("  表 site_settings 处理完成，迁移 {} 个文件引用", count);
        } catch (Exception e) {
            logger.warn("  处理 site_settings 时出错: {}", e.getMessage());
        }
        return count;
    }

    private int moveRemainingFilesToCommon() {
        logger.info("  处理根目录剩余文件...");
        int count = 0;
        File root = new File(uploadDir);
        File[] files = root.listFiles();
        if (files == null) return 0;

        Path commonDir = Paths.get(uploadDir).resolve("common");
        try {
            if (!Files.exists(commonDir)) {
                Files.createDirectories(commonDir);
            }
        } catch (IOException e) {
            logger.error("  创建 common 目录失败", e);
            return 0;
        }

        for (File file : files) {
            if (file.isFile() && isImageFile(file.getName()) && !file.getName().startsWith(".")) {
                try {
                    Path target = commonDir.resolve(file.getName());
                    if (!Files.exists(target)) {
                        Files.move(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
                        logger.info("  ✓ 移动剩余文件到common: {}", file.getName());
                        count++;
                    } else {
                        Files.delete(file.toPath());
                        logger.info("  ✓ 目标已存在，删除源文件: {}", file.getName());
                    }
                } catch (IOException e) {
                    logger.warn("  ✗ 移动文件失败: {}", file.getName(), e);
                }
            }
        }
        logger.info("  根目录剩余文件处理完成，移动 {} 个文件", count);
        return count;
    }

    private boolean isImageFile(String filename) {
        String lower = filename.toLowerCase();
        return lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png") 
            || lower.endsWith(".gif") || lower.endsWith(".bmp") || lower.endsWith(".svg");
    }

    private String determineSettingFolder(String key) {
        if (key == null) return "common";
        String k = key.toLowerCase();
        if (k.contains("logo") || k.contains("favicon")) return "logo";
        if (k.contains("carousel") || k.contains("banner")) return "carousel";
        if (k.contains("team")) return "team";
        if (k.contains("award")) return "award";
        return "common";
    }
}
