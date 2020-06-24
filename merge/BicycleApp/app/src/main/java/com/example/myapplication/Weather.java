package com.example.myapplication;

public class Weather {
    String current_main;
    String current_description;
    String current_temp;
    String current_feelsLike;
    String current_humidity;
    String current_windSpeed;
    String current_uvi;
    String current_minTemp;
    String current_maxTemp;

    String forecast_main;
    String forecast_minTemp;
    String forecast_maxTemp;

    String forecast_week;

    private int type ;

    public Weather(int type, String current_main, String current_description, String current_temp, String current_feelsLike,
                   String current_humidity, String current_windSpeed, String current_uvi, String current_minTemp,
                   String current_maxTemp, String forecast_week, String forecast_main, String forecast_minTemp, String forecast_maxTemp) {

        this.current_main = current_main;
        this.current_description = current_description;
        this.current_temp = current_temp;
        this.current_feelsLike = current_feelsLike;
        this.current_humidity = current_humidity;
        this.current_windSpeed = current_windSpeed;
        this.current_uvi = current_uvi;
        this.current_minTemp = current_minTemp;
        this.current_maxTemp = current_maxTemp;

        this.forecast_main = forecast_main;
        this.forecast_minTemp = forecast_minTemp;
        this.forecast_maxTemp = forecast_maxTemp;
        this.forecast_week = forecast_week;

        this.type = type;

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

    public String getForecast_week() {
        return forecast_week;
    }

    public String getForecast_main() {
        return forecast_main;
    }

    public String getForecast_minTemp() {
        return forecast_minTemp;
    }

    public String getForecast_maxTemp() {
        return forecast_maxTemp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

