package com.example.bicycleapp;

public class AccidentDTO {
    private String accident; //사고건수
    private double latitude; //위도
    private double longitude; //경도

    public AccidentDTO(){
        super();
    }

    public AccidentDTO(String accident, double latitude, double longitude){
        this.accident = accident;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getAccident(){ //사고건수
        return accident;
    }

    public void setAccident(String accident){
        this.accident = accident;
    }

    public double getLatitude(){ //위도
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() { //경도
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
