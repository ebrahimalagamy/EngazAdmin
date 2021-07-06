package com.example.engazadmin.Branches;

import java.io.Serializable;
import java.util.ArrayList;

public class BranchModel implements Serializable {

    private String id = "";
    private String branchName;
    private String branchTimeWork;
    private String branchDaysWork;
    private String branchStatus;
    private String branchImage;
    private ArrayList<String> services = new ArrayList<>();


    public BranchModel() {
    }

    public BranchModel(String name, String timeWork, String daysWork, String status, String image) {

        if (name.trim().equals("") || timeWork.trim().equals("") || daysWork.trim().equals("") || status.trim().equals("")) {
            name = "No name";
            timeWork = "No time";
            daysWork = "No day";
            status = "No status";

        }
        branchName = name;
        branchTimeWork = timeWork;
        branchDaysWork = daysWork;
        branchStatus = status;
        branchImage = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getServices() {
        return services;
    }

    public void setServices(ArrayList<String> services) {
        this.services = services;
    }

    public void addServices(String service) {
        this.services.add(service);
    }

    public String getBranchImage() {
        return branchImage;
    }

    public void setBranchImage(String branchImage) {
        this.branchImage = branchImage;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchTimeWork() {
        return branchTimeWork;
    }

    public void setBranchTimeWork(String branchTimeWork) {
        this.branchTimeWork = branchTimeWork;
    }

    public String getBranchDaysWork() {
        return branchDaysWork;
    }

    public void setBranchDaysWork(String branchDaysWork) {
        this.branchDaysWork = branchDaysWork;
    }

    public String getBranchStatus() {
        return branchStatus;
    }

    public void setBranchStatus(String branchStatus) {
        this.branchStatus = branchStatus;
    }
}
