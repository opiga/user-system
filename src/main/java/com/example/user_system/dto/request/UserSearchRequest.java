package com.example.user_system.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserSearchRequest {

    @NonNull
    private UserFilterDto filter;

    @Min(0)
    @Builder.Default
    private int page = 0;

    @Min(1)
    @Builder.Default
    private int size = 10;

}