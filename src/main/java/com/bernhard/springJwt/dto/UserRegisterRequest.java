package com.bernhard.springJwt.dto;

import com.bernhard.springJwt.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private Role role;

}
