package com.demosecure.demosecure.service;

import com.demosecure.demosecure.dao.IRoleRepository;
import com.demosecure.demosecure.dao.IUserRepository;
import com.demosecure.demosecure.mappers.UserMapper;
import com.demosecure.demosecure.model.RoleEntity;
import com.demosecure.demosecure.model.RoleName;
import com.demosecure.demosecure.model.UserEntity;
import com.demosecure.demosecure.model.dto.BearerToken;
import com.demosecure.demosecure.model.dto.LoginDto;
import com.demosecure.demosecure.model.dto.SignUpDto;
import com.demosecure.demosecure.security.JwtUtilities;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DemoUserSecurityService implements IDemoUserSecurityService {

    private final AuthenticationManager authenticationManager;
    private final IUserRepository IUserRepository;

    private final IRoleRepository IRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtilities jwtUtilities;

    private final UserMapper userMapper;


    private static final String TOKEN_TYPE = "Bearer";

    @Override
    public ResponseEntity<?> register(SignUpDto rSignUpDto) {
        //verifie si username dans la base
        if (IUserRepository.existsByUsername(rSignUpDto.getUsername())) {
            return new ResponseEntity<>("Username deja pris !", HttpStatus.BAD_REQUEST);
        }

        //verifie si email existe deja dans la base
        if (IUserRepository.existsByEmail(rSignUpDto.getEmail())) {
            return new ResponseEntity<>("Email deja pris !", HttpStatus.BAD_REQUEST);
        }

        UserEntity user = userMapper.map(rSignUpDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));


        //role pour le user
        RoleEntity roles = IRoleRepository.findByName(RoleName.USER).get();
        user.setRoles(Collections.singleton(roles));

        //Sauvegarde le user
        IUserRepository.save(user);

        //creation du token
        String token = jwtUtilities.generateToken(rSignUpDto.getEmail(), Collections.singletonList(roles.getRoleName()));
        //renvoi du token dans la reponse
        return new ResponseEntity<>(new BearerToken(token, TOKEN_TYPE), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> register(SignUpDto rSignUpDto, RoleName rRole) {

        if (IUserRepository.existsByUsername(rSignUpDto.getUsername())) {
            return new ResponseEntity<>("Username deja pris !", HttpStatus.BAD_REQUEST);
        }

        //verifie si email existe deja dans la base
        if (IUserRepository.existsByEmail(rSignUpDto.getEmail())) {
            return new ResponseEntity<>("Email deja pris !", HttpStatus.BAD_REQUEST);
        }

        UserEntity user = UserEntity.builder()
                .username(rSignUpDto.getUsername())
                .email(rSignUpDto.getEmail())
                .password(passwordEncoder.encode(rSignUpDto.getPassword()))
                .roles(Collections.singleton(IRoleRepository.findByName(rRole).get()))
                .build();


        //Sauvegarde le user
        IUserRepository.save(user);

        //creation du token
        String token = jwtUtilities.generateToken(rSignUpDto.getEmail(), Collections.singletonList(IRoleRepository.findByName(rRole).get().getRoleName()));
        //renvoi du token dans la reponse
        return new ResponseEntity<>(new BearerToken(token, TOKEN_TYPE), HttpStatus.OK);
    }

    @Override
    public BearerToken authenticate(LoginDto rLoginDto) {
        //Auth user
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(rLoginDto.getUsernameOrEmail(), rLoginDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //recupere user pour generer son token
        UserEntity user = IUserRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        //roles du user
        List<String> rolesNames = new ArrayList<>();
        user.getRoles().forEach(r -> rolesNames.add(r.getRoleName()));
        return new BearerToken(jwtUtilities.generateToken(user.getUsername(), rolesNames), TOKEN_TYPE);
    }

    @Override
    public ResponseEntity<?> update(SignUpDto updatedUser) {
       if(!IUserRepository.existsByUsername(updatedUser.getUsername())) {
           return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
       }
       else {
           UserEntity user = IUserRepository.findByUsername(updatedUser.getUsername()).get();
           user = userMapper.updateUserRole(user, updatedUser, IRoleRepository);
           IUserRepository.save(user);
           return new ResponseEntity<>(HttpStatus.OK);
       }


    }

}