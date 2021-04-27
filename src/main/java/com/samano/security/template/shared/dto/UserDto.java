package com.samano.security.template.shared.dto;

import com.samano.security.template.entity.RoleEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    private String userId;

    private String firstName;

    private String lastName;

    private String email;

    private Date lastLoginDateDisplay;

    private Boolean isActive;

    private Boolean isNotLocked;

    private List<RoleDto> roles;
}
