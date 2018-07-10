package com.brijesh.starwars.data.model;

public class HttpError {
    int httpErrorCode;
    int apiCode;

    public HttpError(int httpErrorCode, int apiCode) {
        this.httpErrorCode = httpErrorCode;
        this.apiCode = apiCode;
    }

    public int getHttpErrorCode() {
        return httpErrorCode;
    }

    public int getApiCode() {
        return apiCode;
    }
}
