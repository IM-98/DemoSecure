package com.demosecure.demosecure.security;

import com.demosecure.demosecure.service.DemoUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtilities jwtUtilities;
    private final DemoUserDetailsService demoUserDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        //recupere token dans request
        String token = jwtUtilities.getToken(request);
        //Si token ok
        if (token != null && jwtUtilities.validateToken(token)) {
            //extrait email du token
            String email = jwtUtilities.extractUsername(token);
            //recupere le user concerne
            UserDetails userDetails = demoUserDetailsService.loadUserByUsername(email);
            if (userDetails != null) {
                //authentifie le user
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
                log.debug("authenticated user with email :{}", email);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        //continue la chaine de traitement de la requete
        filterChain.doFilter(request, response);
    }

}
