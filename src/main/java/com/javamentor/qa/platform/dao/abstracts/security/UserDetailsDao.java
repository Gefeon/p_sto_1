package com.javamentor.qa.platform.dao.abstracts.security;

import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

/**
 * @author Alexey Achkasov
 * @version 1.0, 26.11.2021
 */
public interface UserDetailsDao {
    Optional<UserDetails> loadUserByUsername(String username);
}
