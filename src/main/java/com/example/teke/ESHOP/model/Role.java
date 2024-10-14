package com.example.teke.ESHOP.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {

    MEMBER(
            Set.of(
                    Permission.MEMBER_READ,
                    Permission.MEMBER_CREATE
            )
    ),


    ADMIN(
            Set.of(
                    Permission.MEMBER_READ,
                    Permission.MEMBER_CREATE,
                    Permission.ADMIN_READ,
                    Permission.ADMIN_CREATE
            )
    );

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}

