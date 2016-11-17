package com.trelloapp.service;

import com.trelloapp.dto.UserDTO;

public interface SecurityContextService {
    UserDTO currentUser();
}