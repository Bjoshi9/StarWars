package com.brijesh.starwars;

import android.app.Activity;

public interface BaseView {
    int NO_NETWORK = 0x01;
    int HTTP_ERROR = 0x02;
    int API_ERROR = 0x03;

    Activity getViewActivity();

    void showProgressbar();

    void hideProgressbar();

    void showError(String errorMessage);

    void setViewFor(int aViewFor);

}
