package com.brijesh.starwars.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PeopleResponse {
    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("results")
    @Expose
    private List<People> peopleList;

    public List<People> getPeopleList() {
        return peopleList;
    }

    public int getCount() {
        return count;
    }
}
