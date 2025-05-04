package com.event.services.impl;

import com.event.dto.RegistrationResponse;
import com.event.exception.BaseException;
import com.event.exception.ErrorMessage;
import com.event.exception.MessageType;
import com.event.model.Event;
import com.event.model.Registration;
import com.event.model.User;
import com.event.repository.EventRepository;
import com.event.repository.RegistrationRepository;
import com.event.repository.UserRepository;
import com.event.services.CommonServices;
import com.event.services.IRegistrationsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RegistrationsServiceImpl implements IRegistrationsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private CommonServices commonServices;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RegistrationResponse joinToEvent(Long eventID) {

        try {
            String username = commonServices.getCurrentUsername();

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new BaseException(
                            new ErrorMessage(MessageType.USER_NOT_FOUND, "user : " + username
                            )));
            Event event = eventRepository.findById(eventID)
                    .orElseThrow(() -> new BaseException(
                            new ErrorMessage(MessageType.NO_RECORD_EXIST, "event : " + eventID)
                    ));

            if (registrationRepository.existsByUserAndEvent(user, event)) {
                throw new BaseException(new ErrorMessage(
                        MessageType.DUPLICATE_REQUEST, "Kullanıcı daha önceden kayıt olmuş"
                ));
            }

            if (event.getAvailableSeats() <= 0) {
                throw new BaseException(new ErrorMessage(
                        MessageType.CAPACITY_FULL, "etkinlik kapasitesi : " + event.getAvailableSeats()
                ));
            }

            event.setAvailableSeats(event.getAvailableSeats() - 1);
            eventRepository.save(event);

            Registration registration = new Registration();
            registration.setEvent(event);
            registration.setUser(user);
            registration.setRegistrationDate(new Date());

            Registration savedRegistration = registrationRepository.save(registration);
            RegistrationResponse response = modelMapper.map(savedRegistration, RegistrationResponse.class);

            response.setUser(user.getUsername());
            response.setEvent(event.getName());
            return response;
        }catch (Exception e) {
            throw new BaseException(new ErrorMessage(MessageType.REGISTER_FAILED,e.getMessage()));
        }
    }

    @Override
    public List<RegistrationResponse> getRegistirationEvents() {
        String username = commonServices.getCurrentUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BaseException(
                        new ErrorMessage(MessageType.USER_NOT_FOUND, "kullanıcı bulunamadı : " + username)
                ));

        List<Registration> registrations = registrationRepository.findAllByUser(user);

        if (registrations.isEmpty()){
            throw new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, "kullanıcı etkinliğe kayıt olmamış : " + user.getUsername()));
        }

        return registrations.stream().map(registration -> {
            RegistrationResponse response = modelMapper.map(registration, RegistrationResponse.class);
            response.setUser(registration.getUser().getUsername());
            response.setEvent(registration.getEvent().getName());
            return response;
        }).toList();
    }
}
