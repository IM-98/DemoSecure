package com.demosecure.demosecure.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "roles", schema = "demosecure")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private RoleName name;

    public RoleEntity (RoleName roleName) {this.name = roleName;}
    public String getRoleName() {
        return name.toString();
    }
}