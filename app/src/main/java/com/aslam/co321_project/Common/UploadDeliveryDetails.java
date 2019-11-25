package com.aslam.co321_project.Common;


import java.util.List;

public class UploadDeliveryDetails {
    String distributorId;
    String pharmacyId;
    String driverId;
    String randomId;
    String distributorName;
    String pharmacyName;
    String driverName;
    String cityName;
    List<String> boxList;

    public UploadDeliveryDetails() {
    }

    public UploadDeliveryDetails(String distributorName, String pharmacyName, String driverName, String cityName,
                                 String distributorId, String pharmacyId, String driverId, String randomId, List<String> splittedBoxList) {
        this.distributorName = distributorName;
        this.pharmacyName = pharmacyName;
        this.driverName = driverName;
        this.cityName = cityName;
        this.distributorId = distributorId;
        this.pharmacyId = pharmacyId;
        this.driverId = driverId;
        this.randomId = randomId;
        this.boxList = splittedBoxList;
    }
}
