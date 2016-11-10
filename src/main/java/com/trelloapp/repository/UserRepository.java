package com.trelloapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trelloapp.domain.User;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
	Optional<User> findOneByUsername(String username);
}