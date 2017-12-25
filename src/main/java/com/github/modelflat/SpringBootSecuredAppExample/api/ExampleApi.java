package com.github.modelflat.SpringBootSecuredAppExample.api;

import com.github.modelflat.SpringBootSecuredAppExample.model.Role;
import com.github.modelflat.SpringBootSecuredAppExample.model.User;
import com.github.modelflat.SpringBootSecuredAppExample.repo.PostRepo;
import com.github.modelflat.SpringBootSecuredAppExample.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RestController
public class ExampleApi {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PostRepo postRepo;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Sample endpoint accessible by anyone
     */
    @RequestMapping(
            method = RequestMethod.GET,
            path = "/"
    )
    public String index(HttpSession session, Principal principal, Authentication auth) {
        return String.format(
                        "This page can be accessed by anyone. You are: %s (%s)\n" +
                        "\n\nPosts:\n%s\n\n\nyour http session ID is: " + session.getId(),
                principal != null ? principal.getName() : "<anon>",
                auth != null ?
                        auth.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .reduce((acc, el) -> acc + ", " + el).orElse("<no roles>")
                        : "<no roles>",
                postRepo.findAll().stream()
                        .map(post -> "<" + post.getByUser() + ">" + " says:\n" + post.getBody() )
                        .reduce((acc, el) -> acc + "\n\t---\n" + el).orElse("No posts available!")
                );
    }

    /**
     * Registration endpoint
     * @param user user data
     * @param iWantToBeAdmin register user as admin
     */
    @RequestMapping(
            method = RequestMethod.POST,
            path = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity register(@RequestBody User user,
                                   @RequestParam(required = false) boolean iWantToBeAdmin) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        if (iWantToBeAdmin) {
            roles.add( Role.ADMIN );
        }
        // save to mongo with encoded password
        try {
            userRepo.insert(
                    user.setRoles(roles).encodePassword(passwordEncoder)
            );
        } catch (DuplicateKeyException e) {
            return new ResponseEntity("User already exists!", HttpStatus.NOT_MODIFIED);
        }

        return new ResponseEntity(HttpStatus.CREATED);
    }

}
