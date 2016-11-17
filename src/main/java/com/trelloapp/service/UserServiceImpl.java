package com.trelloapp.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.trelloapp.domain.User;
import com.trelloapp.dto.UserDTO;
import com.trelloapp.dto.UserParams;
import com.trelloapp.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	private final UserRepository userRepository;
    private final SecurityContextService SecurityContextService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, SecurityContextService SecurityContextService) {
        this.userRepository = userRepository;
        this.SecurityContextService = SecurityContextService;
    }
    
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final Optional<User> user = userRepository.findOneByUsername(username);
        final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();
        user.ifPresent(detailsChecker::check);
        return user.orElseThrow(() -> new UsernameNotFoundException("user not found."));
	}

	@Override
    public void update(UserDTO userDTO, UserParams params) {
		Optional<User> user = userRepository.findOneByUsername(userDTO.getEmail());
		user.ifPresent(theUser -> {
			params.getEmail().ifPresent(theUser::setUsername);
			params.getPassword().ifPresent(theUser::setPassword);
			params.getName().ifPresent(theUser::setName);
		});     
		user.orElseThrow(() -> new UsernameNotFoundException("user not found."));
		userRepository.save(user.get());
    }

	@SuppressWarnings("unused")
	@Override
	public Optional<UserDTO> findOne(Long id) {
		final UserDTO currentUser = SecurityContextService.currentUser();
		User user = new User();
		user.setId(currentUser.getId());
		user.setName(currentUser.getName());
		user.setUsername(currentUser.getEmail());
		user.setPassword(currentUser.getPassword());
        final Optional<User> userFind = userRepository.findOne(id, user);
        final Optional<UserDTO> userDTO = Optional.of(UserDTO.builder().user(userFind.orElseThrow(() -> new UsernameNotFoundException("user not found."))).build());
        userDTO.ifPresent(u -> {
            if (currentUser == null) return;
            u.setIsMyself(u.getId() == currentUser.getId());
        });
        return userDTO;
	}
	
	@Override
	public Optional<UserDTO> findMe() {
		final UserDTO currentUser = SecurityContextService.currentUser();
        return findOne(currentUser.getId());
	}

	@Override
	public UserDTO create(UserParams params) {
		User user = userRepository.save(params.toUser());
		final UserDTO userDTO = new UserDTO(user, false);
		return userDTO;
	}
	
	@Override
	public Optional<UserDTO> findOneByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findOneByUsername(username);
		Optional<UserDTO> userDTO = Optional.empty();
		if(user.isPresent()){
			userDTO = Optional.of(new UserDTO(user.get(), false));
		} 
		return userDTO;
	}

}
