package com.example.engazadmin.ServicesInfo;

import java.io.Serializable;

public class InfoModel implements Serializable {

    private String id = "";
    private String infoFirstTitle,infoDescription1,infoDescription2,infoDescription3,
            infoDescription4,infoDescription5,infoSecondTitle,infoDescription6,infoDescription7;
//    private String infoImage;

    public InfoModel() {
    }

    public InfoModel(String title1, String description1,String description2,
                     String description3,String description4,String description5,String title2, String description6,String description7) {

        if (title1.trim().equals("") || description1.trim().equals("") ||description2.trim().equals("") ||
                description3.trim().equals("") ||description4.trim().equals("") ||description5.trim().equals("") ||
                title2.trim().equals("") || description6.trim().equals("")|| description7.trim().equals("")) {
            title1 = "";
            description1 = "";
            description2 = "";
            description3 = "";
            description4 = "";
            description5 = "";
            title2 = "";
            description6 = "";
            description7 = "";

        }
        infoFirstTitle = title1;
        infoDescription1 = description1;
        infoDescription2 = description2;
        infoDescription3 = description3;
        infoDescription4 = description4;
        infoDescription5 = description5;
        infoSecondTitle = title2;
        infoDescription6 = description6;
        infoDescription7 = description7;
//        infoImage = image;


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInfoFirstTitle() {
        return infoFirstTitle;
    }

    public void setInfoFirstTitle(String infoFirstTitle) {
        this.infoFirstTitle = infoFirstTitle;
    }

    public String getInfoDescription1() {
        return infoDescription1;
    }

    public void setInfoDescription1(String infoDescription1) {
        this.infoDescription1 = infoDescription1;
    }

    public String getInfoDescription2() {
        return infoDescription2;
    }

    public void setInfoDescription2(String infoDescription2) {
        this.infoDescription2 = infoDescription2;
    }

    public String getInfoDescription3() {
        return infoDescription3;
    }

    public void setInfoDescription3(String infoDescription3) {
        this.infoDescription3 = infoDescription3;
    }

    public String getInfoDescription4() {
        return infoDescription4;
    }

    public void setInfoDescription4(String infoDescription4) {
        this.infoDescription4 = infoDescription4;
    }

    public String getInfoDescription5() {
        return infoDescription5;
    }

    public void setInfoDescription5(String infoDescription5) {
        this.infoDescription5 = infoDescription5;
    }

    public String getInfoSecondTitle() {
        return infoSecondTitle;
    }

    public void setInfoSecondTitle(String infoSecondTitle) {
        this.infoSecondTitle = infoSecondTitle;
    }

    public String getInfoDescription6() {
        return infoDescription6;
    }

    public void setInfoDescription6(String infoDescription6) {
        this.infoDescription6 = infoDescription6;
    }

    public String getInfoDescription7() {
        return infoDescription7;
    }

    public void setInfoDescription7(String infoDescription7) {
        this.infoDescription7 = infoDescription7;
    }
}
