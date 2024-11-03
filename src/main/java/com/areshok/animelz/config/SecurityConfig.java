package com.areshok.animelz.config;

import com.areshok.animelz.model.User;
import com.areshok.animelz.repository.UserRepository;
import com.areshok.animelz.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user = userRepository.findByUsername(username);

                if (user == null) {
                    throw new UsernameNotFoundException("User not found");
                }

                if (user.getActivationCode() != null ) {
                    throw new LockedException("Email not activated");
                }

                return user;
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {



        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/",
                                "/search",
                                "/filter/**",
                                "/registration",
                                "/contacts",
                                "/random",
                                "/recommendation",
                                "/img/**",
                                "/style.css",
                                "/static/**",
                                "/activate/*",
                                "/anime",
                                "/anime/**").permitAll()

                        .anyRequest().authenticated()

                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/user",true)
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll);

        return http.build();

    }

}
