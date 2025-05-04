package com.event.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventResponse {
    private Long id;
    private String name;
    private String description;
    private String location;
    private Date startDateTime;
    private Date endDateTime;
    private Long capacity;
    private Long availableSeats;
    private boolean publicEvent;
    private String createdByUsername;
}

