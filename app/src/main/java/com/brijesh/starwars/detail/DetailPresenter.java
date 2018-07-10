package com.brijesh.starwars.detail;

import android.os.Handler;

import com.brijesh.starwars.BasePresenter;
import com.brijesh.starwars.BaseView;
import com.brijesh.starwars.data.model.ApiError;
import com.brijesh.starwars.data.model.HttpError;
import com.brijesh.starwars.data.model.People;
import com.brijesh.starwars.data.remote.StarWarsApiClient;
import com.brijesh.starwars.utils.Utils;

import de.greenrobot.event.EventBus;

public class DetailPresenter implements BasePresenter {
    private final DetailView mView;
    private EventBus mEventBus;
    private StarWarsApiClient starWarsApiClient;

    public DetailPresenter(DetailView view) {
        mView = view;
        mEventBus = new EventBus();
        starWarsApiClient = new StarWarsApiClient(mEventBus);
    }

    public void getPeopleDetail(final String url) {
        new Handler().post(
                new Runnable() {
                    @Override
                    public void run() {
                        if (Utils.getConnectivityStatus(mView.getViewActivity())) {
                            mView.showProgressbar();
                            registerBus();
                            starWarsApiClient.getPeopleDetail(url);
                        } else {
                            mView.setViewFor(BaseView.NO_NETWORK);
                        }
                    }
                }
        );
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
    public void onEvent(People people) {
        mView.setViewFor(DetailView.DETAIL_VIEW);
        mView.showDetails(people);
        mEventBus.cancelEventDelivery(people);
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
