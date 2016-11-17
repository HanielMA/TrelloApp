package com.trelloapp.dto;

import java.util.Optional;

import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trelloapp.domain.User;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Builder
@ToString
@EqualsAndHashCode
public final class UserParams {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(UserParams.class);

    private final String email;
    @Size(min = 8, max = 100)
    private final String password;
    private final String name;

    public UserParams(@JsonProperty("email") String email,
                      @JsonProperty("password") String password,
                      @JsonProperty("name") String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    public Optional<String> getPassword() {
        return Optional.ofNullable(password);
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public User toUser() {
        User user = new User();
        user.setUsername(email);
        user.setPassword(password);
        user.setName(name);
        return user;
    }

    public UsernamePasswordAuthenticationToken toAuthenticationToken() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }

}
