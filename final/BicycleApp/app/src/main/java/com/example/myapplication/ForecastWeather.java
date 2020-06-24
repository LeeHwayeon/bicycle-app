package com.example.myapplication;

public class ForecastWeather {
    String forecast_main;
    String forecast_minTemp;
    String forecast_maxTemp;

    String forecast_week;

    public ForecastWeather(String forecast_week, String forecast_main, String forecast_minTemp, String forecast_maxTemp) {
        this.forecast_main = forecast_main;
        this.forecast_minTemp = forecast_minTemp;
        this.forecast_maxTemp = forecast_maxTemp;
        this.forecast_week = forecast_week;
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
}