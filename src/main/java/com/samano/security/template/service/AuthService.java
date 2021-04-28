package com.samano.security.template.service;

import com.samano.security.template.shared.dto.UserDto;
import com.samano.security.template.shared.request.UserRegisterModel;

public interface AuthService {

    UserDto registerUser(UserRegisterModel userRegisterModel);
}
