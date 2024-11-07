package com.project.ecommerce.repository;


import com.project.ecommerce.model.UserInteraction;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;

public interface UserInteractionRepository extends MongoRepository<UserInteraction, String> {
    List<UserInteraction> findByUserId(String userId);
    List<UserInteraction> findByUserIdOrderByTimestampDesc(String userId);
}
