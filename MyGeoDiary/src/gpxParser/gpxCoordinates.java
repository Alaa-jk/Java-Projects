package gpxParser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class gpxCoordinates implements Comparable<gpxCoordinates> {
    double northern_latitude;
    double eastern_longitude;
    LocalDate date;
    LocalTime time;
    double height;

    int Weight;

    public gpxCoordinates(double northern_latitude, double eastern_longitude, LocalDate date, LocalTime time,double height) {
        this.northern_latitude = northern_latitude;
        this.eastern_longitude = eastern_longitude;
        this.date = date;
        this.time = time;
        this.height = height;

        this.Weight = 0;
    }



    public double getheight() {
        return height;
    }


    public void setheight(double height) {
        this.height = height;
    }



    public double getNorthern_latitude() {
        return northern_latitude;
    }


    public void setNorthern_latitude(double northern_latitude) {
        this.northern_latitude = northern_latitude;
    }


    public double getEastern_longitude() {
        return eastern_longitude;
    }


    public void setEastern_longitude(double eastern_longitude) {
        this.eastern_longitude = eastern_longitude;
    }


    public LocalDate getDate() {
        return date;
    }


    public void setDate(LocalDate date) {
        this.date = date;
    }


    public LocalTime getTime() {
        return time;
    }


    public void setTime(LocalTime time) {
        this.time = time;
    }


    

    public double getHeight() {
        return height;
    }



    public void setHeight(double height) {
        this.height = height;
    }



    public int getWeight() {
        return Weight;
    }



    public void setWeight(int Weight) {
        this.Weight = Weight;
    }



    @Override
    public int compareTo(gpxCoordinates o) {

       return LocalDateTime.of(this.getDate(),this.getTime()).compareTo(LocalDateTime.of(o.getDate(),o.getTime())) ;     
    }

    
    
}
