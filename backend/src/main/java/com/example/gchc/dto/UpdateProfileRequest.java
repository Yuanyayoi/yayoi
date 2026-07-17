package com.example.gchc.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String email;
    private String phone;
    private String avatar;
}
