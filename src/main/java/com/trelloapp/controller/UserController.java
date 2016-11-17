package com.trelloapp.controller;

import java.util.Optional;

import javax.annotation.Nullable;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.trelloapp.auth.TokenHandler;
import com.trelloapp.domain.User;
import com.trelloapp.dto.ErrorResponse;
import com.trelloapp.dto.UserDTO;
import com.trelloapp.dto.UserParams;
import com.trelloapp.repository.UserRepository;
import com.trelloapp.service.SecurityContextService;
import com.trelloapp.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private static final Integer DEFAULT_PAGE_SIZE = 5;
	
	private final UserRepository userRepository;
    private final UserService userService;
    private final SecurityContextService securityContextService;
    private final TokenHandler tokenHandler;

    @Autowired
    public UserController(UserRepository userRepository, UserService userService, SecurityContextService securityContextService, TokenHandler tokenHandler) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.securityContextService = securityContextService;
        this.tokenHandler = tokenHandler;
    }
    
    @RequestMapping
    public Page<User> list(@RequestParam(value = "page", required = false) @Nullable Integer page,
                           @RequestParam(value = "size", required = false) @Nullable Integer size) {
        final PageRequest pageable = new PageRequest(
                Optional.ofNullable(page).orElse(1) - 1,
                Optional.ofNullable(size).orElse(DEFAULT_PAGE_SIZE));
        return userRepository.findAll(pageable);
    }
	
	@RequestMapping("/me")
	public UserDTO showMe() {
		return userService.findMe().orElseThrow(UserNotFoundException::new);
	}
	
	@RequestMapping(value = "{id:\\d+}")
    public UserDTO show(@PathVariable("id") Long id) {
        return userService.findOne(id).orElseThrow(UserNotFoundException::new);
    }
	 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/me", method = RequestMethod.PATCH)
    public ResponseEntity updateMe(@Valid @RequestBody UserParams params) {
        UserDTO userDTO = securityContextService.currentUser();
        userService.update(userDTO, params);

        // when username was changed, re-issue jwt.
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-auth-token", tokenHandler.createTokenForUser(userService.loadUserByUsername(userDTO.getEmail())));

        return new ResponseEntity(headers, HttpStatus.OK);
    }
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ErrorResponse handleValidationException(DataIntegrityViolationException e) {
		return new ErrorResponse("email_already_taken", "This email is already taken.");
	}

	@SuppressWarnings("serial")
	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No user")
	private class UserNotFoundException extends RuntimeException {
	}
}
