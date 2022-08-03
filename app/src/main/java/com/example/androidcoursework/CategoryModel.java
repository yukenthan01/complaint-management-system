package com.example.androidcoursework;

public class CategoryModel {
    String category,categoryEmail;

    public CategoryModel() {
    }

    public CategoryModel(String category, String categoryEmail) {
        this.category = category;
        this.categoryEmail = categoryEmail;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryEmail() {
        return categoryEmail;
    }

    public void setCategoryEmail(String categoryEmail) {
        this.categoryEmail = categoryEmail;
    }
}
