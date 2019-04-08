package com.jiacheng.homework;

public class TodayWeather {
    private String city;
    private String updateTime;
    private String temperature;
    private String humidity;
    private String PM2_5;
    private String quality;
    private String windDirection;
    private String windSpeed;
    private String date;
    private String maximumTemperature;
    private String minimumTemperature;
    private String type;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPM2_5() {
        return PM2_5;
    }

    public void setPM2_5(String PM2_5) {
        this.PM2_5 = PM2_5;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMaximumTemperature() {
        return maximumTemperature;
    }

    public void setMaximumTemperature(String maximumTemperature) {
        this.maximumTemperature = maximumTemperature;
    }

    public String getMinimumTemperature() {
        return minimumTemperature;
    }

    public void setMinimumTemperature(String minimumTemperature) {
        this.minimumTemperature = minimumTemperature;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "TodayWeather{" +
                "city='" + city +
                "', updateTime='" + updateTime +
                "', temperature='" + temperature +
                "', humidity='" + humidity +
                "', PM2.5='" + PM2_5 +
                "', quality='" + quality +
                "', windDirection='" + windDirection +
                "', windSpeed='" + windSpeed +
                "', date='" + date +
                "', maximumTemperature='" + maximumTemperature +
                "', miniumunTemperature='" + minimumTemperature +
                "', type='" + type +
                "'}";
    }
}
