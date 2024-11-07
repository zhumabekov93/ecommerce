package com.project.ecommerce.service;

import com.project.ecommerce.model.Event;
import com.project.ecommerce.model.Product;
import com.project.ecommerce.model.User;
import com.project.ecommerce.model.enums.EventType;
import com.project.ecommerce.repository.EventRepository;
import com.project.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public void createUserEvent(User user, EventType eventType) {
        Event event = new Event();
        event.setUserId(user.getId());
        event.setDate(new Date());
        event.setEventType(eventType.name());
        eventRepository.save(event);
    }

    public void createProductEvent(User user, EventType eventType, Product product) {
        Event event = new Event();
        event.setDate(new Date());
        event.setEventType(eventType.name());
        event.setProduct(product);
        event.setUserId(user.getId());
        eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
}
