package com.trelloapp.service;

import com.trelloapp.domain.User;

public interface SecurityContextService {
    User currentUser();
}