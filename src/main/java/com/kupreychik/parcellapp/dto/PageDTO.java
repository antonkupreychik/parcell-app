package com.kupreychik.parcellapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@ToString
public final class PageDTO<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int pageSize;
    private int pageNumber;
    private SortDTO sort;


    public PageDTO(Page<T> page, Pageable pageable) {
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.pageSize = page.getSize();
        this.pageNumber = page.getNumber();
        this.sort = new SortDTO(pageable.getSort());
    }
}