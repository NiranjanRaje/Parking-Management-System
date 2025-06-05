package org.example.parkingmanagementsystem.config;

import org.example.parkingmanagementsystem.handler.CustomAuthSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Autowired
    CustomAuthSuccessHandler customAuthHandler;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/parking-management-system/admin/register",
                                "/parking-management-system/participant/register",
                                "/parking-management-system/login",
                                "/parking-management-system/logout").permitAll()
                        .requestMatchers("/parking-management-system/admin/**").hasRole("admin")
                        .requestMatchers("/parking-management-system/participant/**").hasRole("user")
                        .anyRequest().authenticated()

                )
                .formLogin(form -> form.loginPage("/parking-management-system/login")
                        .permitAll()
                        .successHandler(customAuthHandler)
                        .failureUrl("/parking-management-system/login?error=true")
                )
                .logout(logout -> logout
                        .logoutUrl("/parking-management-system/logout")
                        .logoutSuccessUrl("/parking-management-system/login?logout")
                        .permitAll()
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint((request, response, authException) -> {
                                    response.sendRedirect("/parking-management-system/login");
                                })
                );



        return http.build();
    }


}