/**
 * JWT 认证过滤器
 * 用于验证 JWT token 的有效性
 * 
 * 工作流程:
 * 1. 从请求头中提取 Authorization token
 * 2. 验证 token 的有效性
 * 3. 如果 token 有效，将用户信息设置到 SecurityContext 中
 * 4. 如果 token 无效，返回 401 未授权错误
 */
package com.example.gchc.security;

import com.example.gchc.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String authorizationHeader = request.getHeader("Authorization");
        
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            
            try {
                // 验证 token 是否过期
                if (jwtUtil.isTokenExpired(token)) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token 已过期");
                    return;
                }
                
                String username = jwtUtil.extractUsername(token);
                String role = jwtUtil.extractRole(token);
                
                if (role == null) {
                    role = "ADMIN";
                }
                
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                if ("SUPER_ADMIN".equals(role)) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                }
                
                UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(
                                username, 
                                null, 
                                authorities
                        );
                
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                logger.error("JWT token 验证失败: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token 无效");
                return;
            }
        }
        
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        String method = request.getMethod();
        return (path.equals("/auth/login") && "POST".equalsIgnoreCase(method)) || 
               path.startsWith("/uploads/") ||
               path.startsWith("/static/") ||
               path.startsWith("/assets/") ||
               path.startsWith("/admin/") ||
               path.startsWith("/error") ||
               path.equals("/") ||
               path.equals("/favicon.ico") ||
               path.equals("/favicon.png") ||
               path.equals("/index.html") ||
               // 前台 SPA 路由页面
               path.equals("/about") ||
               path.equals("/contact") ||
               path.equals("/products") ||
               path.equals("/news") ||
               (path.startsWith("/products/") && !path.startsWith("/products/public")) ||
               (path.startsWith("/news/") && !path.startsWith("/news-categories/")) ||
               // 无 /api 前缀的前台公开 site 接口
               path.startsWith("/site/") ||
               // 公开 API，无需 JWT 检查
               path.equals("/api/carousels") ||
               path.equals("/api/features") ||
               path.startsWith("/api/icons/public") ||
               path.startsWith("/api/site/") ||
               path.startsWith("/api/awards/public") ||
               path.startsWith("/api/milestones/public") ||
               path.startsWith("/api/core-values/public") ||
               path.equals("/api/news-categories/public") ||
               path.startsWith("/api/news-categories/public/") ||
               path.equals("/api/products/public") ||
               path.startsWith("/api/products/public/") ||
               path.equals("/articles/public") ||
               path.startsWith("/articles/public/") ||
               path.equals("/categories/public") ||
               path.startsWith("/categories/public/") ||
               path.equals("/messages/public") ||
               path.startsWith("/messages/public/") ||
               path.equals("/products/public") ||
               path.startsWith("/products/public/") ||
               path.equals("/api/messages/public") ||
               path.startsWith("/public/");
    }
}
