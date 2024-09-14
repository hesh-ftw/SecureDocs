package com.secure.docs.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfigs {

    //defines all the request need to be authenticated  (class SpringBootWebSecurityConfiguration)
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                .anyRequest().authenticated());

        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }


    //configs of in memory multiple users

//    @Bean
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        if (!manager.userExists("user1")) {
//            manager.createUser(
//                    User.withUsername("user1")
//                            .password("{noop}password")
//                            .roles("USER")
//                            .build()
//            );
//        }
//        return manager;
//    }
//    @Bean
//    public UserDetailsService userDetailsService(DataSource dataSource){
//        JdbcUserDetailsManager manager= new JdbcUserDetailsManager(dataSource);
//        if (!manager.userExists("user1")){
//            manager.createUser(
//                    User.withUsername("user1")
//                            .password("{noop}password")
//                            .roles("USER")
//                            .build()
//            );
//        }
//        if (!manager.userExists("admin1")){
//            manager.createUser(
//                    User.withUsername("admin1")
//                            .password("{noop}password")
//                            .roles("ADMIN")
//                            .build()
//            );
//        }
//    return manager;
//    }
}
