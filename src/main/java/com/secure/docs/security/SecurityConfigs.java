package com.secure.docs.security;

import com.secure.docs.configs.OAuth2LoginSuccessHandler;
import com.secure.docs.model.AppRole;
import com.secure.docs.model.Role;
import com.secure.docs.model.User;
import com.secure.docs.repository.RoleRepository;
import com.secure.docs.repository.UserRepository;
import com.secure.docs.security.jwt.AuthEntryPointJwt;
import com.secure.docs.security.jwt.AuthTokenFilter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.LocalDate;
import java.util.Arrays;

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

    @Autowired
    @Lazy
    OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFiler(){
        return new AuthTokenFilter();
    }

    //defines all the request need to be authenticated  (class SpringBootWebSecurityConfiguration)
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf-> csrf.csrfTokenRepository(CookieCsrfTokenRepository
                        .withHttpOnlyFalse())
                        .ignoringRequestMatchers("api/auth/public/**"));

        // http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("api/admin/**").hasRole("ADMIN")
                .requestMatchers("api/csrf-token").permitAll()
                .requestMatchers("api/auth/public/**").permitAll()
                .requestMatchers("/oauth2/**").permitAll()
                .anyRequest().authenticated())
                .oauth2Login(oauth2->{
                    oauth2.successHandler(oAuth2LoginSuccessHandler);
                });

        http.exceptionHandling(exception ->
                exception.authenticationEntryPoint(unauthorizedHandler));
        http.addFilterBefore(authenticationJwtTokenFiler(),
                UsernamePasswordAuthenticationFilter.class);
        http.cors(
                cors -> cors.configurationSource(corsConfigurationSource())
        );

        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager
            (AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        // Allow specific origins
        corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:3000"));

        // Allow specific HTTP methods
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Allow specific headers
        corsConfig.setAllowedHeaders(Arrays.asList("*"));
        // Allow credentials (cookies, authorization headers)
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(3600L);
        // Define allowed paths (for all paths use "/**")
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); // Apply to all endpoints
        return source;
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
    public CommandLineRunner initData(RoleRepository roleRepository,
                                      UserRepository userRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));

            Role newRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));


            if (!userRepository.existsByUserName("user1")) {
                User user1 = new User("user1", "user1@example.com", passwordEncoder.encode("password1"));
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
                User admin1 = new User("admin", "admin@example.com", passwordEncoder.encode("password"));
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
                User user2 = new User("chamath", "usercham@example.com", passwordEncoder.encode("pass"));
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