package com.demosecure.demosecure.model.dto;

import lombok.Data;

@Data
public class SignUpDto {
    private String username;
    private String email;
    private String password;
    private Long idRole;
}