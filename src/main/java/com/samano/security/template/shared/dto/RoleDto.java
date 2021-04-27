package com.samano.security.template.shared.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoleDto {

    private String name;

    private List<AuthorityDto> authorities;

}
