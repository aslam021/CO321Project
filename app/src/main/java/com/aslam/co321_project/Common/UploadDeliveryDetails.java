package com.aslam.co321_project.Common;


import java.util.LinkedList;

public class UploadDeliveryDetails {
    String distributorId;
    String pharmacyId;
    String driverId;
    String randomId;
    String distributorName;
    String pharmacyName;
    String driverName;
    String cityName;
    BoxList boxList;
    LinkedList<String> linkedList;

    public UploadDeliveryDetails() {
    }

    public UploadDeliveryDetails(String distributorName, String pharmacyName, String driverName, String cityName,
                                 String distributorId, String pharmacyId, String driverId, String randomId, BoxList boxList) {
        this.distributorName = distributorName;
        this.pharmacyName = pharmacyName;
        this.driverName = driverName;
        this.cityName = cityName;
        this.distributorId = distributorId;
        this.pharmacyId = pharmacyId;
        this.driverId = driverId;
        this.randomId = randomId;
        this.boxList = boxList;
    }

    public UploadDeliveryDetails(String distributorName, String pharmacyName, String driverName, String cityName,
                                 String distributorId, String pharmacyId, String driverId, String randomId, LinkedList<String> linkedList) {
        this.distributorName = distributorName;
        this.pharmacyName = pharmacyName;
        this.driverName = driverName;
        this.cityName = cityName;
        this.distributorId = distributorId;
        this.pharmacyId = pharmacyId;
        this.driverId = driverId;
        this.randomId = randomId;
        this.linkedList = linkedList;
    }
}
