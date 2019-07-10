package com.example.tg;

import com.example.tg.models.User;

import java.io.IOException;
import java.security.Principal;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

public class AuthFilter implements ContainerRequestFilter {
    @Inject
    private AuthService authService;

    // SecurityContextImpl and AuthUser exist only so that username can be easily passed on to the
    // rest methods.
    public class SecurityContextImpl implements SecurityContext {
        private AuthUser user;

        public SecurityContextImpl(AuthUser user) {
            this.user = user;
        }

        @Override
        public Principal getUserPrincipal() {
            return user;
        }

        @Override
        public boolean isUserInRole(String role) {
            // not used
            return false;
        }

        @Override
        public boolean isSecure() {
            // not used
            return false;
        }

        @Override
        public String getAuthenticationScheme() {
            // not used
            return null;
        }
    }

    public class AuthUser implements Principal {
        private String username;

        public AuthUser(User user) {
            this.username = user.getUsername();
        }

        @Override
        public String getName() {
            return username;
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // no authentication required for health check end point
        if (requestContext.getUriInfo().getPath().startsWith("/v1.0/health")) {
            return;
        }

        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // throws if authentication fails
        User user = authService.authenticate(authHeader);

        AuthUser authUser = new AuthUser(user);
        SecurityContext sec = new SecurityContextImpl(authUser);
        requestContext.setSecurityContext(sec);
    }
}
