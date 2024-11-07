package com.project.ecommerce.repository;

import com.project.ecommerce.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("{'preferences': { $in: ?0 }}")
    List<User> findByPreferences(Set<String> preferences);

    @Query(value = "{'purchaseHistory': ?0}", fields = "{'id': 1, 'email': 1}")
    List<User> findByPurchasedProduct(String productId);

    @Query("{'viewHistory': {$size: {$gt: ?0}}}")
    Page<User> findActiveUsers(int minViews, Pageable pageable);

    @Query(value = "{'preferences': {$in: ?0}}", fields = "{'id': 1, 'preferences': 1}")
    List<User> findSimilarUsers(Set<String> preferences);
}