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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

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

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http
               .cors().and()
               .csrf().disable().authorizeRequests()
               .antMatchers(SecurityConstant.PUBLIC_URLS).permitAll()
               .anyRequest().authenticated().and()
               .exceptionHandling().accessDeniedHandler(jwtAccessDeniedHandler)
               .authenticationEntryPoint(jwtAuthenticationEntryPoint)
               .and()
               .addFilter(getAuthenticationFilter())
               .addFilter(new AuthorizationFilter(authenticationManager(),userRepository))
               .sessionManagement()
               .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

       http.headers().frameOptions().disable();
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager());
        filter.setFilterProcessesUrl(SecurityConstant.LOGIN_URL);
        return filter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

}
