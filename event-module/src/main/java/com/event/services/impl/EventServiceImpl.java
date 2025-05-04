package com.event.services.impl;

import com.event.dto.EventRequest;
import com.event.dto.EventResponse;
import com.event.exception.BaseException;
import com.event.exception.ErrorMessage;
import com.event.exception.MessageType;
import com.event.model.Event;
import com.event.model.User;
import com.event.repository.EventRepository;
import com.event.repository.RegistrationRepository;
import com.event.repository.UserRepository;
import com.event.services.CommonServices;
import com.event.services.IEventService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements IEventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CommonServices commonServices;

    @Override
    public EventResponse createEvent(EventRequest request) {
        try{
            String username = commonServices.getCurrentUsername();

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.USER_NOT_FOUND, "user : " + username)));

            Event event = modelMapper.map(request, Event.class);
            event.setCreateDate(new Date());
            event.setCreatedBy(user);
            event.setAvailableSeats(request.getCapacity());
            Event savedEvent = eventRepository.save(event);
            return modelMapper.map(savedEvent, EventResponse.class);
        }catch (Exception e){
            throw new BaseException(new ErrorMessage(MessageType.CREATE_EVENT_FAILED, e.getMessage()));
        }
    }

    @Override
    public List<EventResponse> getAllEvents() {
        List<Event> allEvents = eventRepository.findAll();
        return allEvents.stream()
                .map(event -> modelMapper.map(event, EventResponse.class))
                .toList();
    }

    @Override
    public EventResponse findEvent(Long eventID) {
        Event event = eventRepository.findById(eventID)
                .orElseThrow(() -> new BaseException(
                        new ErrorMessage(MessageType.NO_RECORD_EXIST, "event bulanamadı : " + eventID)));
        return modelMapper.map(event, EventResponse.class);
    }

    @Override
    public EventResponse updateEvent(Long eventID, EventRequest request) {
        Event event = eventRepository.findById(eventID)
                .orElseThrow(() -> new BaseException(
                        new ErrorMessage(MessageType.NO_RECORD_EXIST, "event bulanamadı : " + eventID)));
        modelMapper.map(request, event);
        if(request.getCapacity() != null){
            int registeredCount = registrationRepository.countByEvent(event);

            if(event.getCapacity() < registeredCount ){
                throw new BaseException(
                        new ErrorMessage(
                         MessageType.INVALID_REQUEST,"Yeni kapasite mevcut kayıt sayısından az olamaz. Şuanki kayıtlı kullanıcı sayısı : " + registeredCount
                        ));
            }
            event.setAvailableSeats(request.getCapacity() - registeredCount);
        }

        Event savedEvent = eventRepository.save(event);
        return modelMapper.map(savedEvent, EventResponse.class);
    }

    @Override
    public String deleteEvent(Long eventID) {
        Event event = eventRepository.findById(eventID)
                .orElseThrow(() -> new BaseException(
                        new ErrorMessage(MessageType.NO_RECORD_EXIST, "event bulunamadı : " + eventID)
                ));

        eventRepository.delete(event);

        return "Etkinlik başarıyla silindi";
    }
}
