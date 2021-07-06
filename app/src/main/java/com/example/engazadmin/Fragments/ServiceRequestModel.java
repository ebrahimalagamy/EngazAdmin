package com.example.engazadmin.Fragments;

public class ServiceRequestModel {
    private String UserName;
    private String UserSSN;
    private String UserPhoneNumber;
    private String ServiceName;

    public ServiceRequestModel() {
    }

    public ServiceRequestModel(String userName, String userSSN, String userPhoneNumber, String serviceName) {
        UserName = userName;
        UserSSN = userSSN;
        UserPhoneNumber = userPhoneNumber;
        ServiceName = serviceName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserSSN() {
        return UserSSN;
    }

    public void setUserSSN(String userSSN) {
        UserSSN = userSSN;
    }

    public String getUserPhoneNumber() {
        return UserPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        UserPhoneNumber = userPhoneNumber;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }
}

