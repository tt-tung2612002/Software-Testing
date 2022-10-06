package com.springboot.dto.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUserSignUpRequest {

    private Integer id;

    private String username;

    private String password;

    private String email;

    private String displayName;

    private String phoneNumber;

    private List<Integer> roles;

}
