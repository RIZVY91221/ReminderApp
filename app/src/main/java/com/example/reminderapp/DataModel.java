package com.example.reminderapp;

public class DataModel {

    private String date,namazTime,azanTime,prayerName;

    public DataModel(String date, String namazTime, String azanTime, String prayerName) {
        this.date = date;
        this.namazTime = namazTime;
        this.azanTime = azanTime;
        this.prayerName = prayerName;
    }

    public String getDate() {
        return date;
    }

    public String getNamazTime() {
        return namazTime;
    }

    public String getAzanTime() {
        return azanTime;
    }

    public String getPrayerName() {
        return prayerName;
    }
}
