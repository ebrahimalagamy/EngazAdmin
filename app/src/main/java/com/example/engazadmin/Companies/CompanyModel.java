package com.example.engazadmin.Companies;

import java.io.Serializable;
import java.util.ArrayList;

public class CompanyModel implements Serializable {

    private String id = "";
    private String companyName;
    private String companyImage;
    private ArrayList<String> branches = new ArrayList<>();

    public CompanyModel() {
    }

    public CompanyModel(String name, String imageUrl) {
        if (name.trim().equals("")) {
            name = "No Name";
        }
        companyName = name;
        companyImage = imageUrl;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getBranches() {
        return branches;
    }

    public void setBranches(ArrayList<String> branches) {
        this.branches = branches;
    }

    public void addBranch(String branch) {
        this.branches.add(branch);
    }

    public String getName() {
        return companyName;
    }

    public void setName(String name) {
        companyName = name;
    }

    public String getImageUrl() {
        return companyImage;
    }

    public void setImageUrl(String imageUrl) {
        companyImage = imageUrl;
    }
}