package com.trelloapp.auth;


import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.trelloapp.Application;
import com.trelloapp.domain.User;
import com.trelloapp.service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public final class TokenHandler {

	private static final Logger logger = LoggerFactory.getLogger(TokenHandler.class);
	
    private final String secret;
    private final UserService userService;

    @Autowired
    public TokenHandler(@Value("${app.jwt.secret}") String secret, UserService userService) {
        this.secret = secret;
        this.userService = userService;
    }

    Optional<UserDetails> parseUserFromToken(String token) {
        String username = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return Optional.ofNullable(userService.loadUserByUsername(username));
    }

    public String createTokenForUser(UserDetails user) {
        final ZonedDateTime afterOneWeek = ZonedDateTime.now().plusWeeks(1);
        String jwts = Jwts.builder()
                .setSubject(user.getUsername())
                .signWith(SignatureAlgorithm.HS512, secret)
                .setExpiration(Date.from(afterOneWeek.toInstant()))
                .compact();
        logger.debug("El token jwt generado es: " + jwts);
        return jwts;
    }
}

