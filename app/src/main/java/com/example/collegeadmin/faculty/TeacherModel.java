package com.example.collegeadmin.faculty;

public class TeacherModel {
    String name,email,position,department,image,key;

    public TeacherModel(String name, String email, String position, String department, String image,String key) {
        this.name = name;
        this.email = email;
        this.position = position;
        this.department = department;
        this.image = image;
        this.key = key;

    }

    public TeacherModel() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPosition() {
        return position;
    }

    public String getDepartment() {
        return department;
    }
}
