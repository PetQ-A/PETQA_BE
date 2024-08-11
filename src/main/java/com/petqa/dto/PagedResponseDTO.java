package com.petqa.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PagedResponseDTO<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private boolean first;
    private boolean last;
    private int size;
    private int numberOfElements;
    private boolean empty;
}