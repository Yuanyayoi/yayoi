package com.example.gchc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String message;
    private Boolean replied = false;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
