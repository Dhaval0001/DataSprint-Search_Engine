package com.acc.search.engine.dto;

public class Doc {
    private int rank;
    private String name;

    public Doc(int rank, String name) {
        this.rank = rank;
        this.name = name;
    }

    public int getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
