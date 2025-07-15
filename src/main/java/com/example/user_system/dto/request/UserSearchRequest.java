package com.example.user_system.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserSearchRequest {

    @NonNull
    private UserFilterDto filter;

    @Min(0)
    private int page = 0;

    @Min(1)
    private int size = 10;

}
