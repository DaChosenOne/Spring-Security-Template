package com.samano.security.template.configuration;

import com.samano.security.template.constant.SecurityConstant;
import com.samano.security.template.repository.UserRepository;
import com.samano.security.template.security.AuthenticationFilter;
import com.samano.security.template.security.AuthorizationFilter;
import com.samano.security.template.security.JwtAccessDeniedHandler;
import com.samano.security.template.security.JwtAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/*
This class provide custom security, overriding his methods
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    //Inject JwtAccessDeniedHandler
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    //Inject JwtAuthenticationEntryPoint
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    //Inject UserDetailsService
    private final UserDetailsService userDetailsService;
    //Inject BCryptPasswordEncoder
    private final BCryptPasswordEncoder passwordEncoder;
    //Inject UserRepository
    private final UserRepository userRepository;

    //The constructor was created with all attributes
    @Autowired
    public SecurityConfiguration(JwtAccessDeniedHandler jwtAccessDeniedHandler,
                                 JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                                 @Qualifier("userDetailsService") UserDetailsService userDetailsService,
                                 BCryptPasswordEncoder passwordEncoder,
                                 UserRepository userRepository) {
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    //Override encode method with BCryptPasswordEncoder
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }


    //Implements our custom security policy
    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http
               //Disable cors and csrf
               .cors().and()
               .csrf().disable().authorizeRequests()
               //Disable security from public urls
               .antMatchers(SecurityConstant.PUBLIC_URLS).permitAll()
               //Any request we needed login
               .anyRequest().authenticated().and()
               //Sett our custom JwtAccessDeniedHandler
               .exceptionHandling().accessDeniedHandler(jwtAccessDeniedHandler)
               //Sett our custom JwtAuthenticationEntryPoint
               .authenticationEntryPoint(jwtAuthenticationEntryPoint)
               .and()
               //Set filter from Authentication filter
               .addFilter(getAuthenticationFilter())
               //Set filter from our custom AuthorizationFilter
               .addFilter(new AuthorizationFilter(authenticationManager(),userRepository))
               .sessionManagement()
               //Set creation policy of type "stateless"
               .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

       http.headers().frameOptions().disable();
    }

    //Get authentication filter and return thr URL login
    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager());
        filter.setFilterProcessesUrl(SecurityConstant.LOGIN_URL);
        return filter;
    }

    //Create Bean from Authentication Manager
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

}
