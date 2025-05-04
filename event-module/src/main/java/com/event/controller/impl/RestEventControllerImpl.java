package com.event.controller.impl;

import com.event.controller.IRestEventController;
import com.event.controller.RootEntity;
import com.event.dto.EventRequest;
import com.event.dto.EventResponse;
import com.event.services.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api")
public class RestEventControllerImpl implements IRestEventController {

    @Autowired
    private IEventService eventService;

    @Override
    @PostMapping("/events")
    public RootEntity<EventResponse> createEvent(@RequestBody EventRequest request) {
        return RootEntity.ok(eventService.createEvent(request));
    }

    @Override
    @GetMapping("/events")
    public RootEntity<List<EventResponse>> getAllEvents() {
        return RootEntity.ok(eventService.getAllEvents());
    }

    @Override
    @GetMapping("/events/{eventID}")
    public RootEntity<EventResponse> findEvent(@PathVariable("eventID") Long eventID) {
        return RootEntity.ok(eventService.findEvent(eventID));
    }

    @Override
    @PutMapping("/events/{eventID}")
    public RootEntity<EventResponse> updateEvent(
            @PathVariable("eventID") Long eventID,
            @RequestBody EventRequest request) {
        return RootEntity.ok(eventService.updateEvent(eventID, request));
    }

    @Override
    @DeleteMapping("events/{eventID}")
    public RootEntity<String> deleteEvent(@PathVariable("eventID") Long eventID) {
        return RootEntity.ok(eventService.deleteEvent(eventID));
    }
}
