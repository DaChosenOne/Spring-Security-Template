package com.samano.security.template.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity(name = "authorities")
@Getter
@Setter
@NoArgsConstructor
public class AuthorityEntity extends BaseEntity{

    private String name;

    @ManyToMany(mappedBy = "authorities")
    private List<RoleEntity> roles;
}
