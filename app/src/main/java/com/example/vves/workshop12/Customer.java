package com.example.vves.workshop12;


import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Customer {


    private Long id;
    private String name;
    private String emailAddress;
    private Integer category;
    private Integer check_state;
    private String path;
    private String date_pick;
    private String real_data;
    private Bitmap image;

    public Customer(Long id, String name, String emailAddress, Integer category, Integer checkState, String path, String date_pick, String real_data, Bitmap image) {
        this.id = id;
        this.name = name;
        this.emailAddress = emailAddress;
        this.category = category;
        this.check_state = checkState;
        this.path = path;
        this.date_pick = date_pick;
        this.real_data = real_data;
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getReal_data() {
        return real_data;
    }

    public void setReal_data(String real_data) {
        this.real_data = real_data;
    }

    public String getDate_pick() {
        return date_pick;
    }

    public void setDate_pick(String date_pick) {
        this.date_pick = date_pick;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getCheck_state() {
        return check_state;
    }

    public void setCheck_state(Integer check_state) {
        this.check_state = check_state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress + "\n";
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

}

