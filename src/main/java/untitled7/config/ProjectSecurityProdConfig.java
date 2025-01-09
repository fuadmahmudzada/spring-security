package untitled7.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import untitled7.exceptionhandling.CustomAccessDeniedHandler;
import untitled7.exceptionhandling.CustomBasicAuthenticationEntryPoint;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Profile("prod")
public class ProjectSecurityProdConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        //bu bize request headerden gelen datani oxumaq ucun imkan verir
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
        http.cors(cors -> {
                            CorsConfiguration config = new CorsConfiguration();
                            config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                            config.setAllowedMethods(Collections.singletonList("*"));
                            config.setAllowCredentials(true);
                            config.setAllowedHeaders(Collections.singletonList("*"));
                            config.setMaxAge(3600L);
                            cors.configurationSource(request -> config);
                        }
                )
                .csrf(csrfConfigurer -> csrfConfigurer.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));
//                .sessionManagement(smc -> smc.invalidSessionUrl("/invalidsession").maximumSessions(1).maxSessionsPreventsLogin(true));
        //CookieCsrfTokenRepository ile csrf token generate edib onu cookie de saxlamali oldugunu deyirik
        //csrfTokenRequestAttributeHandler objectini csrrquest handlerin icersini atiriq
        http.csrf(csrfConfigurer -> csrfConfigurer.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                .ignoringRequestMatchers("/register", "/contact")
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));
        http.securityContext(contextConfig -> contextConfig.requireExplicitSave(false));
        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
        http.requiresChannel(requiresChannelConfiguration
                -> requiresChannelConfiguration.anyRequest().requiresSecure());
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards").authenticated()
                .requestMatchers("/notices", "/contact", "/error", "/register", "/invalidsession").permitAll());
        http.formLogin(withDefaults());
        http.httpBasic(sbc -> sbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        http.exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}
