package com.samano.security.template.controller;

import com.samano.security.template.service.AuthService;
import com.samano.security.template.shared.dto.UserDto;
import com.samano.security.template.shared.request.UserRegisterModel;
import com.samano.security.template.shared.response.UserResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final ModelMapper modelMapper;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRegisterModel userRegisterModel){
        UserDto userDto = authService.registerUser(userRegisterModel);
        UserResponse userToReturn = modelMapper.map(userDto,UserResponse.class);
        return new ResponseEntity<>(userToReturn, HttpStatus.OK);
    }
}
