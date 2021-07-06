package com.example.engazadmin.Services;

import java.io.Serializable;
import java.util.ArrayList;

public class ServicesModel implements Serializable {

    private String id = "";
    private String servicesDescription;
//    private String servicesMore;
    private String servicesImage;
    private ArrayList<String> information = new ArrayList<>();

    public ServicesModel() {
    }

    public ServicesModel(String description, String image) {

        if (description.trim().equals("")) {
            description = "No description";
//            more = "No information";

        }
        servicesDescription = description;
//        servicesMore = more;
        servicesImage = image;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getInformation() {
        return information;
    }

    public void setInformation(ArrayList<String> information) {
        this.information = information;
    }

    public void addInformation(String information) {
        this.information.add(information);
    }

    public String getServicesDescription() {
        return servicesDescription;
    }

    public void setServicesDescription(String servicesDescription) {
        this.servicesDescription = servicesDescription;
    }

//    public String getServicesMore() {
//        return servicesMore;
//    }
//
//    public void setServicesMore(String servicesMore) {
//        this.servicesMore = servicesMore;
//    }

    public String getServicesImage() {
        return servicesImage;
    }

    public void setServicesImage(String servicesImage) {
        this.servicesImage = servicesImage;
    }
}
