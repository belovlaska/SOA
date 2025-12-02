package com.example.route.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class PageResponseDto<T> {
    private List<T> data;
    private int totalPages;
}
