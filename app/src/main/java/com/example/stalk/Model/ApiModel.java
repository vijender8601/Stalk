package com.example.stalk.Model;

public class ApiModel {
    String contestName,contestRank,contestRating;

    public ApiModel() {

    }
    public ApiModel(String contestName, String contestRank, String contestRating) {
        this.contestName = contestName;
        this.contestRank = contestRank;
        this.contestRating = contestRating;
    }

    public String getContestName() {
        return contestName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    public String getContestRank() {
        return contestRank;
    }

    public void setContestRank(String contestRank) {
        this.contestRank = contestRank;
    }

    public String getContestRating() {
        return contestRating;
    }

    public void setContestRating(String contestRating) {
        this.contestRating = contestRating;
    }
}
