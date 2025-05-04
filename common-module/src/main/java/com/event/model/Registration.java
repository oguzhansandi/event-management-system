package com.event.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Table(name = "registration")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false, foreignKey = @ForeignKey(name = "fk_registration_event", foreignKeyDefinition = "FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE"))
    private Event event;


    private Date registrationDate;
}
