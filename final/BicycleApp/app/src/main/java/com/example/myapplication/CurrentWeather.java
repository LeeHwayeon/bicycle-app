package com.example.myapplication;

public class CurrentWeather {
    String current_icon;
    String current_main;
    String current_description;
    String current_feelsLike;
    String current_temp;
    String current_humidity;
    String current_windSpeed;
    String current_uvi;
    String current_minTemp;
    String current_maxTemp;


    public CurrentWeather(String current_icon, String current_main, String current_description, String current_temp, String current_feelsLike,
                          String current_humidity, String current_windSpeed, String current_uvi, String current_minTemp,
                          String current_maxTemp) {

        this.current_icon = current_icon;
        this.current_main = current_main;
        this.current_description = current_description;
        this.current_temp = current_temp;
        this.current_feelsLike = current_feelsLike;
        this.current_humidity = current_humidity;
        this.current_windSpeed = current_windSpeed;
        this.current_uvi = current_uvi;
        this.current_minTemp = current_minTemp;
        this.current_maxTemp = current_maxTemp;

    }

    public String getCurrent_icon() {
        return current_icon;
    }

    public String getCurrent_main() {
        return current_main;
    }

    public String getCurrent_description() {
        return current_description;
    }

    public String getCurrent_temp() {
        return current_temp;
    }

    public String getCurrent_feelsLike() {
        return current_feelsLike;
    }

    public String getCurrent_humidity() {
        return current_humidity;
    }

    public String getCurrent_windSpeed() {
        return current_windSpeed;
    }

    public String getCurrent_uvi() {
        return current_uvi;
    }

    public String getCurrent_minTemp() {
        return current_minTemp;
    }

    public String getCurrent_maxTemp() {
        return current_maxTemp;
    }
}

