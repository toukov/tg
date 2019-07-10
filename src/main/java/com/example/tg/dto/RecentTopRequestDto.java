package com.example.tg.dto;

import com.example.tg.GameDataRepository;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import javax.validation.constraints.NotNull;

public class RecentTopRequestDto extends FiltersAndPagingDto {
    private List<String> markets;

    @NotNull
    @JsonProperty("days_ago")
    private Integer daysAgo;

    @NotNull
    @JsonProperty("top_amount")
    private GameDataRepository.TopAmount topAmount;

    public RecentTopRequestDto() {
        // empty
    }

    public List<String> getMarkets() {
        return markets;
    }

    public void setMarkets(List<String> markets) {
        this.markets = markets;
    }

    public Integer getDaysAgo() {
        return daysAgo;
    }

    public void setDaysAgo(Integer daysAgo) {
        this.daysAgo = daysAgo;
    }

    public GameDataRepository.TopAmount getTopAmount() {
        return topAmount;
    }

    public void setTopAmount(GameDataRepository.TopAmount topAmount) {
        this.topAmount = topAmount;
    }
}
