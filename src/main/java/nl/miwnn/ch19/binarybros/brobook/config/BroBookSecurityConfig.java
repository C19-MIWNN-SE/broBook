package nl.miwnn.ch19.binarybros.brobook.config;

/*
 * @author Mart Stukje
 * Configure the security for broBook
 * */

import nl.miwnn.ch19.binarybros.brobook.service.BroBookUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class BroBookSecurityConfig {

    private final BroBookUserService broBookUserService;
    private final BroBookAuthenticationSuccessHandler successHandler;

    public BroBookSecurityConfig(BroBookUserService broBookUserService, BroBookAuthenticationSuccessHandler successHandler) {
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
                                "/webjars/**"

                        ).permitAll()
                        .anyRequest().authenticated()
                ).formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(successHandler)
                        .permitAll()
                ).logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                ).userDetailsService(broBookUserService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
