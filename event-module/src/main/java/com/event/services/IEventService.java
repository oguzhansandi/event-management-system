package com.event.services;

import com.event.dto.EventRequest;
import com.event.dto.EventResponse;

import java.util.List;

public interface IEventService {

    public EventResponse createEvent(EventRequest request);

    public List<EventResponse> getAllEvents();

    public EventResponse findEvent(Long eventID);

    public EventResponse updateEvent(Long eventID, EventRequest request);

    public String deleteEvent(Long eventID);
}
