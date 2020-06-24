package com.example.myapplication;

public class CurrentRental {
    String rackTotCnt;
    String stationName;
    String parkingBikeTotCnt;
    String stationLatitude;
    String stationLongitude;

    public CurrentRental(String rackTotCnt, String stationName, String parkingBikeTotCnt, String stationLatitude, String stationLongitude){
        this.rackTotCnt = rackTotCnt;
        this.stationName = stationName;
        this.parkingBikeTotCnt = parkingBikeTotCnt;
        this.stationLatitude = stationLatitude;
        this.stationLongitude = stationLongitude;
    }

    public String getRackTotCnt() {
        return rackTotCnt;
    }

    public String getStationName() {
        return stationName;
    }

    public String getParkingBikeTotCnt() {
        return parkingBikeTotCnt;
    }

    public String getStationLatitude() {
        return stationLatitude;
    }

    public String getStationLongitude() {
        return stationLongitude;
    }
}
