package com.github.modelflat.SpringBootSecuredAppExample.repo;

import com.github.modelflat.SpringBootSecuredAppExample.model.Post;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepo extends MongoRepository<Post, ObjectId> {

    @DeleteQuery("{ \"byUser\": ?0 }")
    void deletePostsByUserId(String username);

}
