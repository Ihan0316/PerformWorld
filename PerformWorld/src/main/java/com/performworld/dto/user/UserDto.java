package com.performworld.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

public class UserDto {

    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String address1;
    private String address2;
    private String postcode;
}