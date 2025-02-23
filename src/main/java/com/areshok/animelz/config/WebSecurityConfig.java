//package com.areshok.animelz.config;
//
//import com.areshok.animelz.service.impl.UserServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    private UserServiceImpl userService;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                    .antMatchers("/",
//                            "/search",
//                            "/registration",
//                            "/contacts",
//                            "/img/**",
//                            "/static/**",
//                            "/activate/*",
//                            "/anime",
//                            "/anime/**",
//                            "/uploads/**").permitAll()
//                    .anyRequest().authenticated()
//                .and()
//                    .formLogin()
//                    .loginPage("/login")
//                    .permitAll()
//                .and()
//                    .logout()
//                    .permitAll()
//                .and().csrf().disable();
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userService)
//                .passwordEncoder(passwordEncoder);
//    }
//}