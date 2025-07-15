package com.example.user_system.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFilterDto {

    private String firstName;

    private String lastName;

    private String contactInformation;
}