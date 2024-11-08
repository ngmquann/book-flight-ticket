package com.bookflight.ticket.configuration;

import com.bookflight.ticket.enums.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class CustomFilterSecurity {
    @Autowired
    private CustomJWTFilter customJWTFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests( auth->{
                    auth
                            .requestMatchers("/api/auth/**").permitAll()
                            .requestMatchers(HttpMethod.POST,"/api/airport/create").hasAuthority(RoleType.ADMIN.name())
                            .requestMatchers(HttpMethod.DELETE,"/api/airport/delete/**").hasAuthority(RoleType.ADMIN.name())
                            //book
                            .requestMatchers("/api/ticket/book").hasAnyAuthority(RoleType.USER.name(), RoleType.ADMIN.name())
                            //flight
                            .requestMatchers("/api/flight").permitAll()
                            .requestMatchers("/api/flight/detail/**").permitAll()
                            .requestMatchers("/api/flight/search").permitAll()
                            .requestMatchers(HttpMethod.POST,"/api/flight/create").hasAuthority(RoleType.ADMIN.name())
                            .requestMatchers("/api/admin/**").hasAuthority(RoleType.ADMIN.name())
                            .requestMatchers("/api/user/**").hasAnyAuthority(RoleType.USER.name())
                            .anyRequest().authenticated();
                })
                .sessionManagement(session  -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(customJWTFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
