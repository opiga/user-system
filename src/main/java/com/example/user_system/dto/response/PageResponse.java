package com.example.user_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageResponse<T> {

    private List<T> users;

    private int pageNumber;

    private int pageSize;

    private long totalElements;

    private int totalPages;
}