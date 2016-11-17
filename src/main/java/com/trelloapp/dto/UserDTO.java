package com.trelloapp.dto;

import com.trelloapp.domain.User;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString(exclude = {"user"})
@EqualsAndHashCode
public class UserDTO {
	
    private final User user;

    @Getter
    @Setter
    private Boolean isMyself = null;
    
    public UserDTO(User user, Boolean isMyself) {
    	this.user = user;
    	this.isMyself = isMyself;
    }
    
    public long getId() {
        return user.getId();
    }
    
    public void setId(long id) {
        user.setId(id);
    }

    public String getEmail() {
        return user.getUsername();
    }
    
    public void setEmail(String username) {
       user.setUsername(username);
    }

    public String getName() {
        return user.getName();
    }

    public void setName(String name) {
        user.setName(name);
     }
    
    public String getPassword() {
        return user.getPassword();
    }
    
    public void setPassword(String password) {
        user.setPassword(password);
    }

}
