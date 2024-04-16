package org.example.springsecurityoauthjwt.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDTO {
    private String username;
    private String role;
    private String email;
    private String name;
}
