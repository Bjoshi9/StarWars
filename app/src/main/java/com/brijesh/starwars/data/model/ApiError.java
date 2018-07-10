package com.brijesh.starwars.data.model;

public class ApiError {
    private int apiCode;

    public ApiError(int apiCode) {
        this.apiCode = apiCode;
    }

    public int getApiCode() {
        return apiCode;
    }
}
