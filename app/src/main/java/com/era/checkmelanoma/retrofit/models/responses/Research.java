package com.era.checkmelanoma.retrofit.models.responses;

public class Research {

    int id;
    String place;
    Long date;
    String url;
    int research_percent;

    public Research(int id, String place, Long date, String url, int research_percent) {
        this.id = id;
        this.place = place;
        this.date = date;
        this.url = url;
        this.research_percent = research_percent;
    }

    public int getId() {
        return id;
    }

    public String getPlace() {
        return place;
    }

    public Long getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

    public int getResearch_percent() {
        return research_percent;
    }
}
