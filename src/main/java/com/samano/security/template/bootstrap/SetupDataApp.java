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
/*
* This class create all roles and authorities from our class AuthorityConstant, and create one user with role
* SUPER_ADMIN_ROLE with all authorities
 */
@Component
@AllArgsConstructor
public class SetupDataApp implements ApplicationListener<ContextRefreshedEvent> {

    //Inject role repository
    private final RoleRepository roleRepository;

    //Inject user repository
    private final UserRepository userRepository;

    //Inject authority repository
    private final AuthorityRepository authorityRepository;

    //Inject password encoder
    private final BCryptPasswordEncoder passwordEncoder;

    //This method run after the application execute
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        //Get all authorities from ADMIN and save them to the list
        List<AuthorityEntity> adminAuthorities = createAuthorityIfNotFound(Arrays.stream
                (ADMIN_AUTHORITIES).map(AuthorityEntity::new).collect(Collectors.toList()));

        //Get all authorities from USER and save them to the list
        List<AuthorityEntity> userAuthorities = createAuthorityIfNotFound(Arrays.stream
                (USER_AUTHORITIES).map(AuthorityEntity::new).collect(Collectors.toList()));

        //Get all authorities from SUPER_ADMIN and save them to the list
        List<AuthorityEntity> superAdminAuthorities = createAuthorityIfNotFound(Arrays.stream
                (SUPER_ADMIN_AUTHORITIES).map(AuthorityEntity::new).collect(Collectors.toList()));

        //The role was create with user authorities
        createRoleIfNotFound(ROLE_USER,userAuthorities);

        //The role was create with admin authorities
        createRoleIfNotFound(ROLE_ADMIN,adminAuthorities);

        //The role was create with super_admin authorities
        RoleEntity roleAdmin = createRoleIfNotFound(ROLE_SUPER_ADMIN,superAdminAuthorities);

        // Create new User Entity Object and sett values
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("admin@admin.com");
        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setFirstName("SUPER");
        userEntity.setLastName("ADMIN");
        //The password was encode
        userEntity.setPassword(passwordEncoder.encode("jailbreak"));
        userEntity.setIsActive(true);
        userEntity.setIsNotLocked(false);
        userEntity.setJoinDate(new Date());
        userEntity.setLastLoginDate(new Date());
        //The admin role was saved  as item on as List
        userEntity.setRoles(Collections.singletonList(roleAdmin));
        createUserIfNotFound(userEntity);
    }

    //The user are created if not found in the database
    @Transactional
    public void createUserIfNotFound(UserEntity userEntity) {
        if(userRepository.findByEmail(userEntity.getEmail()) == null)
            userRepository.save(userEntity);
    }

    //The Role are created if he not found un the database
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

    //All authorities in the list are created if they are not found in the database and return the list
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
