package com.example.gchc.config;

import com.example.gchc.entity.User;
import com.example.gchc.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        addAvatarColumnIfNotExists();

        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("a****@********");
            admin.setPhone("138******00");
            admin.setRole("SUPER_ADMIN");
            admin.setEnabled(true);
            userRepository.save(admin);
            logger.info("创建默认超级管理员账户: admin / admin123");
        } else {
            User admin = userRepository.findByUsername("admin").orElse(null);
            if (admin != null && !"SUPER_ADMIN".equals(admin.getRole())) {
                admin.setRole("SUPER_ADMIN");
                userRepository.save(admin);
                logger.info("将admin账户升级为超级管理员");
            }
        }

        List<User> users = userRepository.findPage(0, Integer.MAX_VALUE);
        for (User u : users) {
            if (u.getRole() == null || "USER".equals(u.getRole())) {
                u.setRole("ADMIN");
                userRepository.save(u);
                logger.info("将用户 {} 角色更新为 ADMIN", u.getUsername());
            }
        }
    }

    private void addAvatarColumnIfNotExists() {
        try {
            jdbcTemplate.execute("ALTER TABLE users ADD COLUMN avatar VARCHAR(500) NULL AFTER phone");
            logger.info("为users表添加avatar字段");
        } catch (Exception e) {
            logger.debug("avatar字段已存在或添加失败: {}", e.getMessage());
        }
    }
}
