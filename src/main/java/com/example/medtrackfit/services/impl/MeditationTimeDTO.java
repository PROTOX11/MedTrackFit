package com.example.medtrackfit.services.impl;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MeditationTimeDTO {
    @JsonProperty("meditationTime")
    private int meditationTime;

    public int getMeditationTime() {
        return meditationTime;
    }

    public void setMeditationTime(int meditationTime) {
        this.meditationTime = meditationTime;
    }
}