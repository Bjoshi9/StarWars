package com.brijesh.starwars.home;

import android.os.Handler;

import com.brijesh.starwars.BasePresenter;
import com.brijesh.starwars.BaseView;
import com.brijesh.starwars.data.model.ApiError;
import com.brijesh.starwars.data.model.HttpError;
import com.brijesh.starwars.data.model.PeopleResponse;
import com.brijesh.starwars.data.remote.StarWarsApiClient;
import com.brijesh.starwars.utils.Utils;

import de.greenrobot.event.EventBus;

public class HomePresenter implements BasePresenter {
    private final HomeView mView;
    private final EventBus mEventBus;
    private StarWarsApiClient starWarsApiClient;

    HomePresenter(HomeView homeView) {
        mView = homeView;
        mEventBus = new EventBus();
        starWarsApiClient = new StarWarsApiClient(mEventBus);
    }

    void getPeopleList(final int pageNo) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (Utils.getConnectivityStatus(mView.getViewActivity())) {
                    registerBus();
                    mView.showProgressbar();
                    starWarsApiClient.getPeopleList(pageNo);
                } else
                    mView.setViewFor(BaseView.NO_NETWORK);
            }
        });
    }

    @Override
    public void registerBus() {
        if (!mEventBus.isRegistered(this)) mEventBus.register(this);
    }

    @Override
    public void unregisterBus() {
        if (mEventBus.isRegistered(this)) mEventBus.unregister(this);
    }

    //region EventBus events
    public void onEvent(PeopleResponse peopleResponse) {
        mView.setViewFor(HomeView.PEOPLE_LIST);
        mView.setTotalCount(peopleResponse.getCount());
        mView.addPeopleList(peopleResponse.getPeopleList());
        mEventBus.cancelEventDelivery(peopleResponse);
        unregisterBus();
    }

    public void onEvent(HttpError httpError) {
        mView.setViewFor(BaseView.API_ERROR);
        mEventBus.cancelEventDelivery(httpError);
        unregisterBus();
    }

    public void onEvent(ApiError apiError) {
        mView.setViewFor(BaseView.API_ERROR);
        mEventBus.cancelEventDelivery(apiError);
        unregisterBus();
    }

    //endregion

}
