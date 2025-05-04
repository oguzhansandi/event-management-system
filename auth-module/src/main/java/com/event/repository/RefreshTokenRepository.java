package com.event.repository;

import com.event.model.RefreshToken;
import com.event.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    public void deleteAllByUser(User user);
}
