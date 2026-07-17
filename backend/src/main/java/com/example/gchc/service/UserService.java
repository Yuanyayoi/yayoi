
package com.example.gchc.service;

import com.example.gchc.dto.UserDTO;
import com.example.gchc.entity.User;
import com.example.gchc.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<UserDTO> getAllUsers(Pageable pageable) {
        List<UserDTO> users = userRepository.findPage(pageable.getOffset(), pageable.getPageSize())
                .stream()
                .map(UserDTO::fromEntity)
                .toList();
        return new PageImpl<>(users, pageable, userRepository.countAll());
    }

    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(UserDTO::fromEntity);
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        // 使用传入的密码，如果没有则生成随机密码
        String rawPassword = userDTO.getPassword();
        if (rawPassword == null || rawPassword.isBlank()) {
            rawPassword = java.util.UUID.randomUUID().toString().substring(0, 12);
        }
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setRole(userDTO.getRole() != null ? userDTO.getRole() : "ADMIN");
        user.setEnabled(userDTO.getEnabled() != null ? userDTO.getEnabled() : true);
        return UserDTO.fromEntity(userRepository.save(user));
    }

    public Optional<UserDTO> updateUser(Long id, UserDTO userDTO) {
        return userRepository.findById(id).map(user -> {
            if (userDTO.getUsername() != null) user.setUsername(userDTO.getUsername());
            if (userDTO.getEmail() != null) user.setEmail(userDTO.getEmail());
            if (userDTO.getPhone() != null) user.setPhone(userDTO.getPhone());
            if (userDTO.getRole() != null) user.setRole(userDTO.getRole());
            if (userDTO.getEnabled() != null) user.setEnabled(userDTO.getEnabled());
            return UserDTO.fromEntity(userRepository.save(user));
        });
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
