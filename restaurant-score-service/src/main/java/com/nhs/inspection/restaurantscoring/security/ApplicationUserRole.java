package com.nhs.inspection.restaurantscoring.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.nhs.inspection.restaurantscoring.security.ApplicationUserPermission.SCORE_READ;
import static com.nhs.inspection.restaurantscoring.security.ApplicationUserPermission.SCORE_WRITE;

public enum ApplicationUserRole {
    INSPECTOR(Sets.newHashSet(SCORE_READ, SCORE_WRITE)),
    PUBLIC(Sets.newHashSet(SCORE_READ));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}