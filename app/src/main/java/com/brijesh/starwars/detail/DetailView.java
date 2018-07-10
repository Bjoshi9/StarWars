package com.brijesh.starwars.detail;

import com.brijesh.starwars.BaseView;
import com.brijesh.starwars.data.model.People;

public interface DetailView extends BaseView {
    int DETAIL_VIEW = 0x04;

    void showDetails(People people);
}
