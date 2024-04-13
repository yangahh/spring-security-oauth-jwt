package org.example.springsecurityoauthjwt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String username;
    private String role;
    private String email;
    private String name;
}
