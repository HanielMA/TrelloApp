package com.trelloapp.service;

import java.util.Optional;

import com.trelloapp.domain.User;
import com.trelloapp.dto.UserDTO;
import com.trelloapp.dto.UserParams;

public interface UserService extends org.springframework.security.core.userdetails.UserDetailsService {
	
	User update(User user, UserParams params);

    Optional<UserDTO> findOne(Long id);

    Optional<UserDTO> findMe();
    
    User create(UserParams params);
}
