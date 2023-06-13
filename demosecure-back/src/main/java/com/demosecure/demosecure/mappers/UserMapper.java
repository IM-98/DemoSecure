package com.demosecure.demosecure.mappers;

import com.demosecure.demosecure.dao.IRoleRepository;
import com.demosecure.demosecure.model.RoleEntity;
import com.demosecure.demosecure.model.UserEntity;
import com.demosecure.demosecure.model.dto.SignUpDto;
import org.mapstruct.*;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;


@Mapper(
        componentModel = "spring",
        uses = IRoleRepository.class
)
 public interface UserMapper {

    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "id", ignore = true)
    UserEntity map(SignUpDto userDto);


    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateUserRole(SignUpDto userDto, @MappingTarget UserEntity entityToUpdate);



    @AfterMapping
    default void mapRole(@MappingTarget UserEntity userToUpdate, Long idRole, @Context IRoleRepository repo) {

        userToUpdate.setRoles(repo.findById(idRole).map(role -> {
            Set<RoleEntity> roles = new HashSet<>();
            roles.add(role);
            return roles;
        }).orElseThrow(() -> new NoSuchElementException("Role not found")));
    }


}
