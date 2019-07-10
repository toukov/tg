package com.example.tg.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class FiltersAndPagingDto {
    private Integer page;

    @JsonProperty("page_size")
    private Integer pageSize;

    private Map<String, Integer> filters;

    public FiltersAndPagingDto() {
        // empty
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Map<String, Integer> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Integer> filters) {
        this.filters = filters;
    }
}
