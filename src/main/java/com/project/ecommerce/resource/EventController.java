package com.project.ecommerce.resource;

import com.project.ecommerce.model.Event;
import com.project.ecommerce.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    @GetMapping()
    public List<Event> getEvents() {
        return eventService.getAllEvents();
    }
}
