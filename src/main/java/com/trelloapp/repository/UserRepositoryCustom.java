package com.trelloapp.repository;

import java.util.Optional;

import com.trelloapp.domain.User;
import com.trelloapp.dto.UserDTO;

interface UserRepositoryCustom {
    Optional<UserDTO> findOne(Long userId, User currentUser);
}

