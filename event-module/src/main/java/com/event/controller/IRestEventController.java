package com.event.controller;

import com.event.dto.EventRequest;
import com.event.dto.EventResponse;

import java.util.List;

public interface IRestEventController {

    public RootEntity<EventResponse> createEvent(EventRequest request);

    public RootEntity<List<EventResponse>> getAllEvents();

    public RootEntity<EventResponse> findEvent(Long eventID);

    public RootEntity<EventResponse> updateEvent(Long eventID, EventRequest request);

    public RootEntity<String> deleteEvent(Long eventID);
}
