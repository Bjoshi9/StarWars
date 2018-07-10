package com.brijesh.starwars.home;

import com.brijesh.starwars.BaseView;
import com.brijesh.starwars.data.model.People;

import java.util.List;

interface HomeView extends BaseView {
    int PEOPLE_LIST = 0x04;
    void addPeopleList(List<People> peopleList);
    void setTotalCount(int totalCount);
}
