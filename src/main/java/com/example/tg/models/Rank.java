package com.example.tg.models;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class Rank {
    private Long ts;
    private Integer rank;

    public Rank() {
        // empty
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
