package com.example.gchc.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * 数据库迁移 - 在应用启动时自动执行表结构变更
 */
@Component
@Order(0)
@Slf4j
public class DatabaseMigration implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseMigration(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        try {
            // 检查是否存在 code 列，如果存在则迁移到 image_url
            Integer iconCodeCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'icons' AND COLUMN_NAME = 'code'",
                Integer.class
            );
            if (iconCodeCount != null && iconCodeCount > 0) {
                log.info("开始迁移 icons 表：code -> image_url");
                // 先删除旧数据，因为emoji编码已不可用
                jdbcTemplate.execute("TRUNCATE TABLE icons");
                // 删除旧唯一索引
                try {
                    jdbcTemplate.execute("ALTER TABLE icons DROP INDEX uk_icons_code");
                } catch (Exception ignored) {
                    // 索引可能不存在，忽略
                }
                // 修改列名和类型
                jdbcTemplate.execute("ALTER TABLE icons CHANGE COLUMN code image_url VARCHAR(500) NOT NULL");
                // 添加新唯一索引
                jdbcTemplate.execute("ALTER TABLE icons ADD UNIQUE KEY uk_icons_image_url (image_url)");
                log.info("icons 表迁移完成");
            }
        } catch (Exception e) {
            log.warn("数据库迁移（icons表）跳过或失败: {}", e.getMessage());
        }

        try {
            // 检查 milestones 表是否已有 title 列
            Integer titleCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'milestones' AND COLUMN_NAME = 'title'",
                Integer.class
            );
            if (titleCount != null && titleCount == 0) {
                log.info("开始迁移 milestones 表：添加 title 列");
                // 添加 title 列
                jdbcTemplate.execute("ALTER TABLE milestones ADD COLUMN title VARCHAR(100) NOT NULL DEFAULT '' AFTER year");
                // 将旧的 year 值复制到 title（旧 year 作为标题）
                jdbcTemplate.execute("UPDATE milestones SET title = year");
                // 清空 year 列，让用户重新填写年份
                jdbcTemplate.execute("UPDATE milestones SET year = ''");
                log.info("milestones 表迁移完成：year 改为年份，原 year 值已移至 title");
            }
        } catch (Exception e) {
            log.warn("数据库迁移（milestones表）跳过或失败: {}", e.getMessage());
        }

        try {
            // 修改 carousels 表 title 列为允许 NULL
            jdbcTemplate.execute("ALTER TABLE carousels MODIFY COLUMN title VARCHAR(255) NULL");
            log.info("carousels 表 title 列已修改为允许 NULL");
        } catch (Exception e) {
            log.warn("数据库迁移（carousels表）跳过或失败: {}", e.getMessage());
        }

        try {
            // 检查 news_categories 表是否已存在
            Integer newsCategoryTableCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.TABLES WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'news_categories'",
                Integer.class
            );
            if (newsCategoryTableCount != null && newsCategoryTableCount == 0) {
                log.info("开始创建 news_categories 表");
                jdbcTemplate.execute(
                    "CREATE TABLE IF NOT EXISTS news_categories (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "description VARCHAR(255) NULL, " +
                    "sort_order INT NOT NULL DEFAULT 0, " +
                    "enabled TINYINT(1) NOT NULL DEFAULT 1, " +
                    "created_at DATETIME(6) NULL, " +
                    "updated_at DATETIME(6) NULL, " +
                    "UNIQUE KEY uk_news_categories_name (name)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci"
                );
                jdbcTemplate.update(
                    "INSERT INTO news_categories (name, sort_order, enabled, created_at, updated_at) VALUES (?, ?, 1, NOW(), NOW())",
                    "公司新闻", 1
                );
                jdbcTemplate.update(
                    "INSERT INTO news_categories (name, sort_order, enabled, created_at, updated_at) VALUES (?, ?, 1, NOW(), NOW())",
                    "行业资讯", 2
                );
                jdbcTemplate.update(
                    "INSERT INTO news_categories (name, sort_order, enabled, created_at, updated_at) VALUES (?, ?, 1, NOW(), NOW())",
                    "技术分享", 3
                );
                log.info("news_categories 表创建完成，默认分类已插入");
            }
        } catch (Exception e) {
            log.warn("数据库迁移（news_categories表）跳过或失败: {}", e.getMessage());
        }

        try {
            // 检查 users 表是否已有 avatar 列
            Integer avatarCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'users' AND COLUMN_NAME = 'avatar'",
                Integer.class
            );
            if (avatarCount != null && avatarCount == 0) {
                log.info("开始迁移 users 表：添加 avatar 列");
                jdbcTemplate.execute("ALTER TABLE users ADD COLUMN avatar VARCHAR(500) NULL AFTER phone");
                log.info("users 表 avatar 列添加完成");
            }
        } catch (Exception e) {
            log.warn("数据库迁移（users表avatar列）跳过或失败: {}", e.getMessage());
        }

        try {
            // 检查 messages 表是否已存在
            Integer messagesTableCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.TABLES WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'messages'",
                Integer.class
            );
            if (messagesTableCount != null && messagesTableCount == 0) {
                log.info("开始创建 messages 表");
                jdbcTemplate.execute(
                    "CREATE TABLE IF NOT EXISTS messages (" +
                    "id BIGINT NOT NULL AUTO_INCREMENT, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "email VARCHAR(100) NOT NULL, " +
                    "phone VARCHAR(20) NULL, " +
                    "message TEXT NOT NULL, " +
                    "replied TINYINT(1) NOT NULL DEFAULT 0, " +
                    "created_at DATETIME(6) NULL, " +
                    "updated_at DATETIME(6) NULL, " +
                    "PRIMARY KEY (id)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci"
                );
                log.info("messages 表创建完成");
            }
        } catch (Exception e) {
            log.warn("数据库迁移（messages表）跳过或失败: {}", e.getMessage());
        }
    }
}
