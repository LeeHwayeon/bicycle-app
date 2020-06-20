package com.example.myweather;

public class WeatherInfo {
    String weather_Name;
    String temp_Min;
    String temp_Max;


    public WeatherInfo(String weather_Name, String temp_Min, String temp_Max) {
        this.weather_Name = weather_Name;
        this.temp_Min = temp_Min;
        this.temp_Max = temp_Max;

    }

    public String getWeather_Name() {
        return weather_Name;
    }

    public String getTemp_Min() {
        return temp_Min;
    }

    public String getTemp_Max() {
        return temp_Max;
    }

    public void setWeather_Name(String weather_Name) {
        this.weather_Name = weather_Name;
    }
}

