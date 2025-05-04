package com.event.services;

import com.event.exception.BaseException;
import com.event.exception.ErrorMessage;
import com.event.exception.MessageType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class CommonServices {

    public String getCurrentUsername(){
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername();
            } else {
                return principal.toString();
            }
        }catch (Exception e){
            throw new BaseException(new ErrorMessage(MessageType.USER_NOT_FOUND, e.getMessage()));
        }
    }
}
