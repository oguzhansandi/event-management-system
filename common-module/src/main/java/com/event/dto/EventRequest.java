package com.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {
    private String name;
    private String description;
    private String location;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDateTime;

    private Long capacity;
    private Boolean publicEvent;
}

