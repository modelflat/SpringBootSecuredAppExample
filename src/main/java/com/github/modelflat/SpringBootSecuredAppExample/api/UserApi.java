package com.github.modelflat.SpringBootSecuredAppExample.api;

import com.github.modelflat.SpringBootSecuredAppExample.model.Post;
import com.github.modelflat.SpringBootSecuredAppExample.repo.PostRepo;
import com.github.modelflat.SpringBootSecuredAppExample.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserApi {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PostRepo postRepo;

    /**
     * Gets user posts page by username
     * @param name username
     */
    @RequestMapping(
            method = RequestMethod.GET,
            path = "/{name}"
    )
    public String getUserProfile(Principal principal, @PathVariable String name) {
        Post sample = new Post();
        sample.setByUser( name );
        return "Logged in as " + principal.getName() + "\n\n" +
                postRepo.findAll(Example.of( sample )).stream()
                .map(post -> "---\n" + post.getBody() + "\n\n")
                .reduce("", (acc, el) -> el);//.orElse("No posts by this user!");
    }

    /**
     * Make new post as username
     */
    @RequestMapping(
            method = RequestMethod.POST,
            path = "/{name}",
            consumes = MediaType.TEXT_PLAIN_VALUE
    )
    public void makePost(@PathVariable String name, @RequestBody String postBody) {
        postRepo.insert( new Post(name, postBody) );
    }

    /**
     * Deletes user and all posts by username
     * @param name username
     */
    @RequestMapping(
            method = RequestMethod.DELETE,
            path = "/{name}"
    )
    public void deleteUser(@PathVariable String name) {
        // delete all posts
        postRepo.deletePostsByUserId(name);
        // delete user
        userRepo.delete(name);
    }
}


