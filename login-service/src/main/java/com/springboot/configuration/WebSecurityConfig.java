package com.springboot.configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.springboot.filter.JwtAuthenticationFilter;
import com.springboot.filter.UserAuthorizationFilter;
import com.springboot.services.UserService;
import com.springboot.utils.ConfigUtils;
import com.springboot.utils.JwtTokenUtils;

import lombok.RequiredArgsConstructor;

// @Configuration
// @EnableWebSecurity
@RequiredArgsConstructor
// @EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(ConfigUtils.passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(request -> {
            CorsConfiguration cors = new CorsConfiguration();
            cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            cors.setAllowedHeaders(List.of("*"));
            return cors;
        });
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests().antMatchers("/login").permitAll();
        // .and().formLogin()
        // .loginProcessingUrl("/api/v1/login");

        // only allow authenticated users to access the rest of the application
        http.authorizeRequests()
                .antMatchers("/api/users/**")
                .hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().anyRequest().authenticated();

        // apply filter for authentication and authorization
        JwtTokenUtils tokenUtils = new JwtTokenUtils();
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(userService, authenticationManagerBean(),
                tokenUtils);

        http.addFilter(filter);
        http.addFilterBefore(new UserAuthorizationFilter(tokenUtils),
                UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
