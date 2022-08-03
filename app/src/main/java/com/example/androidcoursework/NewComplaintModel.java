package com.example.androidcoursework;


public class NewComplaintModel {
    String subject,description,category,
            address,city,postcode,state,date,userId,imageUrl,status;

    public NewComplaintModel() {
    }

    public NewComplaintModel(String subject, String description, String category, String address,
                             String city, String postcode, String state, String date,
                             String userId, String imageUrl,String status) {
        this.subject = subject;
        this.description = description;
        this.category = category;
        this.address = address;
        this.city = city;
        this.postcode = postcode;
        this.state = state;
        this.date = date;
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
