package com.trelloapp.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.trelloapp.domain.QUser;
import com.trelloapp.domain.User;
import com.trelloapp.dto.UserDTO;

class UserRepositoryImpl implements UserRepositoryCustom {

	private final JPAQueryFactory queryFactory;

    private final QUser qUser = QUser.user;

    @Autowired
    public UserRepositoryImpl(JPAQueryFactory queryFactory) {
    	this.queryFactory = queryFactory;
    }
    
	@Override
	public Optional<UserDTO> findOne(Long userId, User currentUser) {
        final User user = queryFactory.select(qUser)
                .from(qUser)
                .where(qUser.id.eq(userId))
                .fetchOne();      
        UserDTO usuarioDTO = UserDTO.builder().user(user).build();
        
        return Optional.ofNullable(usuarioDTO);
	}
	
}
