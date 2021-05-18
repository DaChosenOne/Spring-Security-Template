package com.samano.security.template.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
// Este controlador prueba que los roles funcionen correctamente
@RestController
@RequestMapping(path = "/main")
public class MainController {

    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String isUser(HttpServletRequest httpServlet){
        return "Role User";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String isAdmin(){
        return "Role Admin";
    }

    @GetMapping("/superadmin")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public String isSuperAdmin(){
        return "Role Super Admin";
    }
}
