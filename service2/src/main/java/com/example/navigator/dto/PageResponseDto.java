package com.example.navigator.dto;
import java.io.Serializable;

import java.util.List;

public class PageResponseDto<T> implements Serializable{
    private List<T> data;
    private int totalPages;

    public PageResponseDto() {
    }

    public PageResponseDto(List<T> data, int totalPages) {
        this.data = data;
        this.totalPages = totalPages;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
