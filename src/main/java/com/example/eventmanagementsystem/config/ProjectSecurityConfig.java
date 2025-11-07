package com.example.eventmanagementsystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class ProjectSecurityConfig {

private final CustomDetailsService userDetailsService;
    
    public ProjectSecurityConfig(CustomDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider(CustomDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       
        http.csrf(csrf->csrf.disable())
             .authorizeHttpRequests(auth -> auth
                .requestMatchers("/register", "/login", "/css/**", "/js/**").permitAll()
                .requestMatchers("/admindashboard").hasRole("ADMIN")
                .requestMatchers("/userdashboard").hasRole("USER")
                .anyRequest().authenticated()
            )
             .formLogin(form ->form
                .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler(new CustomAuthSuccessHandler())
                .failureUrl("/login?error=true")
                .permitAll()
                )
            .logout(logout->logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
                );
        return http.build();
    }

}
