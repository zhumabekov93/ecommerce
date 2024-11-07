package com.project.ecommerce.repository;

import com.project.ecommerce.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Event, String> {
}
