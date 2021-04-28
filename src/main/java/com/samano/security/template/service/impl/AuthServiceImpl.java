package com.samano.security.template.service.impl;

import com.samano.security.template.entity.UserEntity;
import com.samano.security.template.repository.UserRepository;
import com.samano.security.template.security.UserPrincipal;
import com.samano.security.template.service.AuthService;
import com.samano.security.template.shared.dto.UserDto;
import com.samano.security.template.shared.request.UserRegisterModel;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
@Qualifier("userDetailsService")
public class AuthServiceImpl implements AuthService, UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDto registerUser(UserRegisterModel userRegisterModel) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(s);
        if(userEntity == null){
            throw  new UsernameNotFoundException("USER NOT FOUND BY EMAIL:"+s);
        }else {
            userEntity.setLastLoginDateDisplay(userEntity.getLastLoginDate());
            userEntity.setLastLoginDate(new Date());
            userRepository.save(userEntity);
            return new UserPrincipal(userEntity);
        }
    }

}
