package com.samano.security.template.bootstrap;

import com.samano.security.template.entity.AuthorityEntity;
import com.samano.security.template.entity.RoleEntity;
import com.samano.security.template.entity.UserEntity;
import com.samano.security.template.repository.AuthorityRepository;
import com.samano.security.template.repository.RoleRepository;
import com.samano.security.template.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.samano.security.template.constant.AuthorityConstant.*;
import static com.samano.security.template.constant.RoleConstant.*;

@Component
@AllArgsConstructor
public class SetupDataApp implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {


        List<AuthorityEntity> adminAuthorities = createAuthorityIfNotFound(Arrays.stream
                (ADMIN_AUTHORITIES).map(AuthorityEntity::new).collect(Collectors.toList()));

        List<AuthorityEntity> userAuthorities = createAuthorityIfNotFound(Arrays.stream
                (USER_AUTHORITIES).map(AuthorityEntity::new).collect(Collectors.toList()));

        List<AuthorityEntity> superAdminAuthorities = createAuthorityIfNotFound(Arrays.stream
                (SUPER_ADMIN_AUTHORITIES).map(AuthorityEntity::new).collect(Collectors.toList()));

        createRoleIfNotFound(ROLE_USER,userAuthorities);
        createRoleIfNotFound(ROLE_ADMIN,adminAuthorities);
        RoleEntity roleAdmin = createRoleIfNotFound(ROLE_SUPER_ADMIN,superAdminAuthorities);

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("admin@admin.com");
        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setFirstName("SUPER");
        userEntity.setLastName("ADMIN");
        userEntity.setPassword(passwordEncoder.encode("jailbreak"));
        userEntity.setIsActive(true);
        userEntity.setIsNotLocked(false);
        userEntity.setJoinDate(new Date());
        userEntity.setLastLoginDate(new Date());
        userEntity.setRoles(Collections.singletonList(roleAdmin));
        createUserIfNotFound(userEntity);
    }

    @Transactional
    public void createUserIfNotFound(UserEntity userEntity) {
        if(userRepository.findByEmail(userEntity.getEmail()) == null)
            userRepository.save(userEntity);
    }

    @Transactional
    public RoleEntity createRoleIfNotFound(String roleUser, List<AuthorityEntity> userAuthorities) {
        RoleEntity roleEntity = roleRepository.findByName(roleUser);
        if(roleEntity == null){
            RoleEntity newRole = new RoleEntity();
            newRole.setName(roleUser);
            newRole.setAuthorities(userAuthorities);
            roleEntity = roleRepository.save(newRole);
        }
        return roleEntity;
    }

    @Transactional
    public List<AuthorityEntity> createAuthorityIfNotFound(List<AuthorityEntity> authorityEntities) {
        List<AuthorityEntity> listToReturn = new ArrayList<>();
        for(AuthorityEntity auth: authorityEntities){
            AuthorityEntity authorityEntity = authorityRepository.findByName(auth.getName());
            listToReturn.add(Objects.requireNonNullElseGet(authorityEntity, () -> authorityRepository.save(new AuthorityEntity(auth.getName()))));
        }
        return listToReturn;
    }
}
