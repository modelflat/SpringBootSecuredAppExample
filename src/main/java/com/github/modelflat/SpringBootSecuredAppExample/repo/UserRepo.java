package com.github.modelflat.SpringBootSecuredAppExample.repo;

import com.github.modelflat.SpringBootSecuredAppExample.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo extends MongoRepository<User, String> {
}
