package com.javamentor.qa.platform.dao.abstracts.security;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserDetailsDao {
    Optional<UserDetails> loadUserByUsername(String username);
}
