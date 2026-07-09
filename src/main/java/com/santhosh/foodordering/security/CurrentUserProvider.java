package com.santhosh.foodordering.security;

import com.santhosh.foodordering.model.Users;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/** Convenience access to the currently authenticated user inside the service layer. */
@Component
public class CurrentUserProvider {

    public CustomUserDetails getPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails details)) {
            throw new AccessDeniedException("No authenticated user in the security context");
        }
        return details;
    }

    public Users getUser() {
        return getPrincipal().getUser();
    }

    public Long getId() {
        return getPrincipal().getId();
    }

    public boolean isAdmin() {
        return "ADMIN".equals(getUser().getRole().getRoleName());
    }
}
