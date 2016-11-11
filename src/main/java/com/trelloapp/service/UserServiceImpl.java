package com.trelloapp.service;

import java.util.Optional;

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
    public User update(User user, UserParams params) {
        params.getEmail().ifPresent(user::setUsername);
        params.getPassword().ifPresent(user::setPassword);
        params.getName().ifPresent(user::setName);
        return userRepository.save(user);
    }

	@Override
	public Optional<UserDTO> findOne(Long id) {
		final User currentUser = SecurityContextService.currentUser();
        final Optional<UserDTO> user = userRepository.findOne(id, currentUser);
        user.ifPresent(u -> {
            if (currentUser == null) return;
            u.setIsMyself(u.getId() == currentUser.getId());
        });
        return user;
	}

	@Override
	public Optional<UserDTO> findMe() {
		final User currentUser = SecurityContextService.currentUser();
        return findOne(currentUser.getId());
	}

	@Override
	public User create(UserParams params) {
		return userRepository.save(params.toUser());
	}


  
}
