package com.samano.security.template.shared.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterModel {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
