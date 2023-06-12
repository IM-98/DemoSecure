package com.demosecure.demosecure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        jsr250Enabled=true //>= pour utiliser annotation @RoleAllowed
)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter ;

    /**
     * Creation et injection auto des dependances via spring, on aurait pu aussi utiliser l'annotation @RequiredArgsConstructor
     * @param jwtAuthenticationFilter
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter){
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().configurationSource(request -> getCorsConfiguration()) //<= cors
                .and().csrf().disable() //<= desactive csrf
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //-< pas d'etat de session
                .and()
                //config des authorisations de base sur requetes http
                .authorizeHttpRequests(
                        (authorize) ->
                                authorize.requestMatchers("/api/auth/user/**").permitAll()
                                        .requestMatchers("/api/user/**").hasAuthority("USER") //acces a tous les user USER
                                        .requestMatchers("/api/auth/admin/**").hasAuthority("ADMIN") //acces a tous les user ADMIN
                                        .anyRequest().authenticated() //doit etre authentifie sur chaque request
                );
        //ajoute un filtre pour la gestion du token !
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        //renvoie la config
        return http.build();
    }

    /**
     * Determine une configuration Cors
     * @return CorsConfiguration
     */
    private CorsConfiguration getCorsConfiguration() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //headers
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        //origins (qui a le droit d'appeler, quels hosts)
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200"));
        //methodes http
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PUT", "OPTIONS", "PATCH", "DELETE"));
        //si app securisee avec Authorization header
        corsConfiguration.setAllowCredentials(true);
        //duree validite
        corsConfiguration.setMaxAge(4800L);
        //headers exposes en reponse
        corsConfiguration.setExposedHeaders(List.of("Authorization"));
        //renvoie la config
        return corsConfiguration;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}