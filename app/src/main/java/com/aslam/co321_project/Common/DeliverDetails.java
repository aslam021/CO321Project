package com.aslam.co321_project.Common;

public class DeliverDetails {
    private String title;
    private String subTitle;
    private String distributorId;
    private String pharmacyId;
    private String driverId;
    private String randomId;


    public DeliverDetails() {
    }

    public DeliverDetails(String title, String subTitle, String distributorId, String pharmacyId, String driverId, String randomId) {
        this.title = title;
        this.subTitle = subTitle;
        this.distributorId = distributorId;
        this.pharmacyId = pharmacyId;
        this.driverId = driverId;
        this.randomId = randomId;
    }

    public String getPharmacyId() {
        return pharmacyId;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getDistributorId() {
        return distributorId;
    }

    public String getRandomId() {
        return randomId;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getTitle() {
        return title;
    }
}
