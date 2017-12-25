package com.github.modelflat.SpringBootSecuredAppExample.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class WebSecurity {

    /**
     * Checks currently authorized user against path id
     */
    public boolean checkUserId(AbstractAuthenticationToken token, String id) {
        return token.getName().equals( id );
    }

}
