package com.example.store.dto;

import com.example.store.data.Authority;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserRequest {
    private String name;
    private String password;
    private String email;
    private Authority authorities;
}
