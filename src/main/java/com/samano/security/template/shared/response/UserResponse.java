package com.samano.security.template.shared.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String firstName;

    private String lastName;

    private String email;

    private Date lastLoginDateDisplay;

    private List<RoleResponse> roles;
}
