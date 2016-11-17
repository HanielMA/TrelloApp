package com.trelloapp.service;

import java.util.Optional;

import com.trelloapp.dto.UserDTO;
import com.trelloapp.dto.UserParams;

public interface UserService extends org.springframework.security.core.userdetails.UserDetailsService {
	
	void update(UserDTO userDTO, UserParams params);

    Optional<UserDTO> findOne(Long id);

    Optional<UserDTO> findMe();
    
    UserDTO create(UserParams params);
    
    Optional<UserDTO> findOneByUsername(String username);
}
