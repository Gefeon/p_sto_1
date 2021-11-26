package com.javamentor.qa.platform.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Alexey Achkasov
 * @version 1.0, 25.11.2021
 */
@Getter @Setter
@AllArgsConstructor
public class AuthenticationRequestDto {
    private String username;
    private String password;
}
