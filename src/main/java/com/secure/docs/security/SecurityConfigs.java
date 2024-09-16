package com.secure.docs.security;

import com.secure.docs.model.AppRole;
import com.secure.docs.model.Role;
import com.secure.docs.model.User;
import com.secure.docs.repository.RoleRepository;
import com.secure.docs.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import javax.sql.DataSource;
import java.time.LocalDate;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@AllArgsConstructor

@EnableMethodSecurity(              //for method level security restrictions
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true
)
public class SecurityConfigs {

    //defines all the request need to be authenticated  (class SpringBootWebSecurityConfiguration)
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("api/admin/**").hasRole("ADMIN")
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


    //add custom users to db for testing
    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository) {
        return args -> {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));

            Role newRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));


            if (!userRepository.existsByUserName("user1")) {
                User user1 = new User("user1", "user1@example.com", "{noop}password1");
                user1.setAccountNonLocked(true);
                user1.setAccountNonExpired(true);
                user1.setCredentialsNonExpired(true);
                user1.setEnabled(true);
                user1.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
                user1.setAccountExpiryDate(LocalDate.now().plusYears(1));
                user1.setTwoFactorEnabled(false);
                user1.setSignUpMethod("email");
                user1.setRole(userRole);
                userRepository.save(user1);
            }

            if (!userRepository.existsByUserName("admin")) {
                User admin1 = new User("admin", "admin@example.com", "{noop}password");
                admin1.setAccountNonLocked(true);
                admin1.setAccountNonExpired(true);
                admin1.setCredentialsNonExpired(true);
                admin1.setEnabled(true);
                admin1.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
                admin1.setAccountExpiryDate(LocalDate.now().plusYears(1));
                admin1.setTwoFactorEnabled(false);
                admin1.setSignUpMethod("email");
                admin1.setRole(adminRole);
                userRepository.save(admin1);
            }

            if (!userRepository.existsByUserName("chamath")) {
                User user2 = new User("chamath", "usercham@example.com", "{noop}champw");
                user2.setAccountNonLocked(true);
                user2.setAccountNonExpired(true);
                user2.setCredentialsNonExpired(true);
                user2.setEnabled(true);
                user2.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
                user2.setAccountExpiryDate(LocalDate.now().plusYears(1));
                user2.setTwoFactorEnabled(false);
                user2.setSignUpMethod("email");
                user2.setRole(newRole);
                userRepository.save(user2);
            }
        };
    }

}