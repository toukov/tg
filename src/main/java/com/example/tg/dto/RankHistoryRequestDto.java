package com.example.tg.dto;

import com.example.tg.GameDataRepository;

import java.util.Map;

import javax.validation.constraints.NotNull;

public class RankHistoryRequestDto {
    @NotNull
    private GameDataRepository.HistoryFrequency frequency;

    private Map<String, Integer> filters;

    public RankHistoryRequestDto() {
        // empty
    }

    public GameDataRepository.HistoryFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(GameDataRepository.HistoryFrequency frequency) {
        this.frequency = frequency;
    }

    public Map<String, Integer> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Integer> filters) {
        this.filters = filters;
    }
}
