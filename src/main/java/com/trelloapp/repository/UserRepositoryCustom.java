package com.trelloapp.repository;

import java.util.Optional;

import com.trelloapp.domain.User;

interface UserRepositoryCustom {
    Optional<User> findOne(Long userId, User currentUser);
}

