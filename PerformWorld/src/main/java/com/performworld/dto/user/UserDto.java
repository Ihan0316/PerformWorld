package com.performworld.dto.user;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String userId;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String address1;
    private String address2;
    private String postcode;
    private String tierName;
}