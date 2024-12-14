package com.example.carbooking.models;

public class ReservationObject {

    private int id;
    private String carName;
    private String userBooking;
    private String pickUpDate;
    private String pickUpTime;
    private String price;
    private String imagePath;

    public ReservationObject() {}

    public ReservationObject(int id, String carName, String userBooking, String pickUpDate, String pickUpTime, String price, String imagePath) {
        this.id = id;
        this.carName = carName;
        this.userBooking = userBooking;
        this.pickUpDate = pickUpDate;
        this.pickUpTime = pickUpTime;
        this.price = price;
        this.imagePath = imagePath;
    }

    public ReservationObject(String carName, String userBooking, String pickUpDate, String pickUpTime, String price, String imagePath) {
        this.carName = carName;
        this.userBooking = userBooking;
        this.pickUpDate = pickUpDate;
        this.pickUpTime = pickUpTime;
        this.price = price;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getUserBooking() {
        return userBooking;
    }

    public void setUserBooking(String userBooking) {
        this.userBooking = userBooking;
    }

    public String getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(String pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(String pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
