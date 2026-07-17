/**
 * Spring Security 安全配置类
 * 配置系统的安全策略，包括:
 * - CSRF 防护
 * - Session 管理策略
 * - 路径权限控制
 * - 密码加密方式
 * - 认证管理器
 * - JWT 认证过滤器
 */
package com.example.gchc.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Configuration 标识这是一个配置类
 * @EnableWebSecurity 启用 Spring Security 的 Web 安全功能
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * JWT 认证过滤器
     * 用于验证 JWT token 的有效性
     */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 构造函数，注入 JWT 认证过滤器
     * 
     * @param jwtAuthenticationFilter JWT 认证过滤器
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * 配置安全过滤链（SecurityFilterChain）
     * 定义 HTTP 请求的安全策略
     * 
     * @param http HttpSecurity 对象，用于配置安全策略
     * @return SecurityFilterChain 安全过滤链
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 禁用 CSRF（跨站请求伪造）保护
            // 因为使用 JWT token 进行认证，不需要 CSRF 保护
            .csrf(AbstractHttpConfigurer::disable)
            
            // 配置 Session 管理策略为无状态（STATELESS）
            // 因为使用 JWT 进行认证，不需要服务器端 Session
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 配置请求授权规则
            .authorizeHttpRequests(auth -> auth
                // 公开接口：登录、token验证、静态资源、上传文件
                .requestMatchers("/auth/login").permitAll()
                .requestMatchers("/auth/validate").permitAll()
                .requestMatchers("/uploads/**").permitAll()
                .requestMatchers("/static/**").permitAll()
                .requestMatchers("/assets/**").permitAll()
                .requestMatchers("/admin/**").permitAll()
                .requestMatchers("/favicon.ico").permitAll()
                .requestMatchers("/favicon.png").permitAll()
                .requestMatchers("/").permitAll()
                .requestMatchers("/index.html").permitAll()
                .requestMatchers("/error").permitAll()
                // 前台 SPA 路由页面（需返回 index.html）
                .requestMatchers("/about", "/contact").permitAll()
                .requestMatchers("/products", "/products/*").permitAll()
                .requestMatchers("/news", "/news/*").permitAll()
                // 公开 API：前端展示用
                .requestMatchers("/api/carousels").permitAll()
                .requestMatchers("/api/features").permitAll()
                .requestMatchers("/api/icons/public").permitAll()
                .requestMatchers("/api/site/**").permitAll()
                .requestMatchers("/api/awards/public").permitAll()
                .requestMatchers("/api/milestones/public").permitAll()
                .requestMatchers("/api/core-values/public").permitAll()
                .requestMatchers("/api/news-categories/public").permitAll()
                .requestMatchers("/api/products/public").permitAll()
                .requestMatchers("/api/products/public/**").permitAll()
                .requestMatchers("/articles/public").permitAll()
                .requestMatchers("/articles/public/**").permitAll()
                .requestMatchers("/categories/public").permitAll()
                .requestMatchers("/categories/public/**").permitAll()
                .requestMatchers("/messages/public").permitAll()
                .requestMatchers("/messages/public/**").permitAll()
                .requestMatchers("/products/public").permitAll()
                .requestMatchers("/products/public/**").permitAll()
                // 无 /api 前缀的 site 接口全部为前台公开 GET 接口（写操作均在 /api/site/** 下）
                .requestMatchers("/site/**").permitAll()
                // 兼容旧版路径（无 /api 前缀的管理接口，非 public 子路径需要认证）
                .requestMatchers("/articles/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                .requestMatchers("/categories/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                .requestMatchers("/messages/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                // 用户管理接口：仅超级管理员可访问
                .requestMatchers("/api/admin/users/**").hasRole("SUPER_ADMIN")
                // 个人信息接口：已认证用户可访问
                .requestMatchers("/auth/profile").authenticated()
                .requestMatchers("/auth/change-password").authenticated()
                // 管理接口：需要 ADMIN 角色（包含 SUPER_ADMIN，因为 SUPER_ADMIN 继承 ADMIN 权限）
                .requestMatchers("/api/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                // 其他请求允许访问（用于 SPA 前端路由）
                .anyRequest().permitAll()
            )
            
            // 添加 JWT 认证过滤器
            // 在 UsernamePasswordAuthenticationFilter 之前执行
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        // 构建并返回安全过滤链
        return http.build();
    }

    /**
     * 配置密码编码器（PasswordEncoder）
     * 使用 BCrypt 算法对密码进行加密
     * 
     * @return PasswordEncoder 密码编码器实例
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCryptPasswordEncoder 是 Spring Security 推荐的密码加密方式
        // 它会自动加盐，安全性较高
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置认证管理器（AuthenticationManager）
     * 用于处理用户登录认证
     * 
     * @param config AuthenticationConfiguration 认证配置
     * @return AuthenticationManager 认证管理器实例
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        // 从认证配置中获取认证管理器
        return config.getAuthenticationManager();
    }
}