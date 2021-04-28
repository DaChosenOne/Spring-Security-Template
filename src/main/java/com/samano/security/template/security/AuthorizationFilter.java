package com.samano.security.template.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.samano.security.template.constant.SecurityConstant;
import com.samano.security.template.entity.UserEntity;
import com.samano.security.template.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private  final UserRepository userRepository;

    public AuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(SecurityConstant.HEADER_STRING);
        if(header == null || !header.startsWith(SecurityConstant.TOKEN_HEADER)){
            chain.doFilter(request,response);
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request,response);
        
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstant.HEADER_STRING);
        if(token!=null){
            token = token.replace(SecurityConstant.TOKEN_HEADER,"");
            String user = getSubject(token);
            if(user!=null){
                UserEntity userEntity = userRepository.findByEmail(user);
                UserPrincipal userPrincipal = new UserPrincipal(userEntity);
                return new UsernamePasswordAuthenticationToken(user,null, userPrincipal.getAuthorities());
            }
        }
        return null;
    }

    public String getSubject(String token){
        JWTVerifier verifier = getJwtVerifier();
        return  verifier.verify(token).getSubject();
    }

    private JWTVerifier getJwtVerifier() {
        JWTVerifier verifier;
        try{
            Algorithm algorithm = Algorithm.HMAC512(SecurityConstant.JWT_SECRET);
            verifier = JWT.require(algorithm).withIssuer(SecurityConstant.LLC).build();
        }catch (JWTVerificationException exception){
            throw  new JWTVerificationException(SecurityConstant.TOKEN_CANNOT_BE_VERIFIED);
        }
        return  verifier;
    }

}
