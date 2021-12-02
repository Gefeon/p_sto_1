package com.javamentor.qa.platform.models.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@AllArgsConstructor
@ApiModel(description = "A JSON object containing user login and password")
public class AuthenticationRequestDto {
    @NotBlank(message = "Username cannot be empty")
    @ApiModelProperty(example = "your username", required = true)
    private String username;
    @NotBlank(message = "Password cannot be empty")
    @ApiModelProperty(example = "your password", required = true)
    private String password;
}
