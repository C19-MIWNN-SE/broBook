package nl.miwnn.ch19.binarybros.brobook.config;

/*
 * @author Mart Stukje
 * Configure the security for broBook
 * */

import nl.miwnn.ch19.binarybros.brobook.service.BroBookUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class BroBookSecurityConfig {

    private final BroBookUserService broBookUserService;
    private final BroBookAuthenticationSuccessHandler successHandler;

    public BroBookSecurityConfig(BroBookUserService broBookUserService,
                                 BroBookAuthenticationSuccessHandler successHandler) {
        this.broBookUserService = broBookUserService;
        this.successHandler = successHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/css/**",
                                "/login",
                                "/image/**",
                                "/webjars/**",
                                "/activate/**"
                        ).permitAll()
                        .requestMatchers(
                                "/cohort/add",
                                "/cohort/save",
                                "/cohort/*/add-manual",
                                "/cohort/*/import",
                                "/user/add",
                                "/user/save",
                                "/user/edit/**",
                                "/user/import"
                        ).hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(
                                "/user/**"
                        ).hasAnyRole("ADMIN")
                        .anyRequest().authenticated()
                ).formLogin(form -> form
                        .loginPage("/")
                        .loginProcessingUrl("/login")
                        .successHandler(successHandler)
                        .failureUrl("/?error=true")
                        .permitAll()
                ).logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                ).userDetailsService(broBookUserService);

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        var beheerder = User.builder()
                .username("beheerder")
                .password(encoder.encode("wachtwoord"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(beheerder);
    }
}
