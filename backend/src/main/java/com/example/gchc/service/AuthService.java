/**
 * 认证服务类
 * 处理用户登录认证逻辑
 * 使用 Spring Security 进行身份验证，并生成 JWT token
 */
package com.example.gchc.service;

import com.example.gchc.dto.LoginRequest;
import com.example.gchc.dto.LoginResponse;
import com.example.gchc.dto.UserDTO;
import com.example.gchc.entity.User;
import com.example.gchc.repository.UserRepository;
import com.example.gchc.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @Service 注解标识这是一个服务层组件
 * Spring 会自动扫描并创建该类的实例，供其他组件注入使用
 */
@Service
public class AuthService {

    /**
     * 认证管理器，用于验证用户身份
     * Spring Security 提供的核心认证组件
     */
    private final AuthenticationManager authenticationManager;

    /**
     * 用户详情服务，用于加载用户信息
     * 自定义实现类为 CustomUserDetailsService
     */
    private final UserDetailsService userDetailsService;

    /**
     * JWT 工具类，用于生成和验证 token
     */
    private final JwtUtil jwtUtil;

    /**
     * 用户数据访问层，用于查询用户信息
     */
    private final UserRepository userRepository;

    /**
     * 构造函数，注入所需依赖
     * 
     * @param authenticationManager 认证管理器
     * @param userDetailsService 用户详情服务
     * @param jwtUtil JWT 工具类
     * @param userRepository 用户数据访问层
     */
    public AuthService(AuthenticationManager authenticationManager, UserDetailsService userDetailsService,
                      JwtUtil jwtUtil, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    /**
     * 用户登录方法
     * 执行以下步骤:
     * 1. 使用 Spring Security 验证用户名和密码
     * 2. 加载用户详情
     * 3. 生成 JWT token
     * 4. 构建登录响应
     * 
     * @param request 登录请求，包含用户名和密码
     * @return 登录响应，包含 token 和用户信息
     */
    public LoginResponse login(LoginRequest request) {
        // 步骤1: 使用认证管理器验证用户名和密码
        // UsernamePasswordAuthenticationToken 封装了用户名和密码
        // authenticate 方法会自动调用 UserDetailsService.loadUserByUsername 获取用户信息
        // 并使用 PasswordEncoder 验证密码是否正确
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        // 步骤2: 加载用户详情（包含用户名、密码、权限等）
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        
        // 步骤3: 从数据库获取完整的用户实体
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        
        // 步骤4: 使用 JWT 工具类生成 token（包含角色信息）
        String token = jwtUtil.generateToken(userDetails.getUsername(), user.getRole());
        
        // 步骤5: 构建并返回登录响应
        return new LoginResponse(
                token,                          // JWT token
                "Bearer",                       // token 类型（标准的 Authorization 头格式）
                jwtUtil.getExpirationInSeconds(), // token 过期时间（秒）
                UserDTO.fromEntity(user)        // 用户信息（转换为 DTO）
        );
    }
}
