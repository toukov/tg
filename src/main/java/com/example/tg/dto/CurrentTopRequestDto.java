package com.example.tg.dto;

import javax.validation.constraints.NotNull;

public class CurrentTopRequestDto extends FiltersAndPagingDto {
    @NotNull
    private String market;

    @NotNull
    private Integer count;

    public CurrentTopRequestDto() {
        // empty
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
