package com.trelloapp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.trelloapp.domain.User;
import com.trelloapp.dto.UserDTO;
import com.trelloapp.repository.UserRepository;

@Service
public class SecurityContextServiceImpl implements SecurityContextService {

    private final UserRepository userRepository;

    @Autowired
    public SecurityContextServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO currentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final Optional<User> currentUser = userRepository.findOneByUsername(authentication.getName());
        return new UserDTO(currentUser.orElseThrow(null), false);
    }
}
