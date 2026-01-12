package com.example.route.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class PageResponseDto<T> {
    private List<T> data;
    private int totalPages;
}
