package com.aslam.co321_project.Common;


import java.util.LinkedList;

public class UploadDeliveryDetails {
    String distributorId;
    String pharmacyId;
    String driverId;
    String randomId;
    BoxList boxList;
    LinkedList<String> linkedList;

    public UploadDeliveryDetails() {
    }

    public UploadDeliveryDetails(String distributorId, String pharmacyId, String driverId, String randomId, BoxList boxList) {
        this.distributorId = distributorId;
        this.pharmacyId = pharmacyId;
        this.driverId = driverId;
        this.randomId = randomId;
        this.boxList = boxList;
    }

    public UploadDeliveryDetails(String distributorId, String pharmacyId, String driverId, String randomId, LinkedList<String> linkedList) {
        this.distributorId = distributorId;
        this.pharmacyId = pharmacyId;
        this.driverId = driverId;
        this.randomId = randomId;
        this.linkedList = linkedList;
    }
}
