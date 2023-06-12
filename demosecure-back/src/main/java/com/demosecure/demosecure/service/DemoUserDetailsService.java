package com.demosecure.demosecure.service;

import com.demosecure.demosecure.dao.IUserRepository;
import com.demosecure.demosecure.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DemoUserDetailsService implements UserDetailsService {

    private final IUserRepository IUserRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        //Recuper le user selon son nom ou son email sachant que chacun est UNIQUE en base
        UserEntity user = IUserRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Utilisateur inconnu : " + usernameOrEmail));

        //Recupere les roles du user
        Set<GrantedAuthority> authorities = user
                .getRoles()
                .stream()
                .map((role) -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toSet());

        //Renvoie l'objet UserDetails utilise par Spring Security
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                authorities);
    }

}