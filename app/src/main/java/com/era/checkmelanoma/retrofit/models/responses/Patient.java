package com.era.checkmelanoma.retrofit.models.responses;

public class Patient {

    int id;
    String name;
    Long date_of_birth;

    public Patient(int id, String name, Long date_of_birth) {
        this.id = id;
        this.name = name;
        this.date_of_birth = date_of_birth;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getDate_of_birth() {
        return date_of_birth;
    }
}
