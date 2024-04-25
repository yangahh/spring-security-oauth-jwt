package org.example.springsecurityoauthjwt.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String username;
    private String role;
    private String email;
    private String name;
}
