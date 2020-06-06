package com.example.bicycleapp;

public class RentalDTO {
    private String stationName; //대여소 이름
    private double latitude; //위도
    private double longitude; //경도

    public RentalDTO(){
        super();
    }

    public RentalDTO(String stationName, double latitude, double longitude){
        this.stationName=stationName;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName){
        this.stationName=stationName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
