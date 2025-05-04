package com.event.repository;

import com.event.model.Event;
import com.event.model.Registration;
import com.event.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    public boolean existsByUserAndEvent(User user, Event event);

    public List<Registration> findAllByUser(User user);

    public int countByEvent(Event event);

}
