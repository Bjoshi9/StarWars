package com.brijesh.starwars.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.brijesh.starwars.R;
import com.brijesh.starwars.data.model.People;
import com.brijesh.starwars.detail.DetailActivity;
import com.brijesh.starwars.listeners.OnItemClickListener;
import com.brijesh.starwars.widget.ErrorView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomeView, OnItemClickListener {

    private RecyclerView rvHome;
    private LinearLayoutManager layoutManager;
    private List<People> peopleList = new ArrayList<>();
    private HomeAdapter homeAdapter;
    private HomePresenter homePresenter;
    private ErrorView evHome;
    private ProgressBar progressBar;
    private int visibleThreshold = 3;
    private boolean mIsLoading;
    private int pageNo = 1;
    int totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        rvHome = findViewById(R.id.rvHome);
        evHome = findViewById(R.id.evHome);
        progressBar = findViewById(R.id.progressBar);
        layoutManager = new LinearLayoutManager(this);
        rvHome.setLayoutManager(layoutManager);
        homeAdapter = new HomeAdapter(peopleList, this);
        rvHome.setAdapter(homeAdapter);
        homePresenter = new HomePresenter(this);
        mIsLoading = true;
        homePresenter.getPeopleList(pageNo);
        rvHome.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = recyclerView.getChildCount();
                int itemCount = recyclerView.getAdapter().getItemCount();
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                if (!mIsLoading && (itemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)
                        && itemCount < totalItemCount) {
                    mIsLoading = true;
                    homePresenter.getPeopleList(++pageNo);
                }
            }
        });
    }

    @Override
    public void addPeopleList(List<People> peopleList) {
        this.peopleList.addAll(peopleList);
        homeAdapter.notifyDataSetChanged();

    }

    @Override
    public void setTotalCount(int totalCount) {
        totalItemCount = totalCount;
    }

    @Override
    public Activity getViewActivity() {
        return this;
    }

    @Override
    public void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressbar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String errorMessage) {
        evHome.setTitle(errorMessage);
    }

    @Override
    public void setViewFor(int aViewFor) {
        hideProgressbar();
        mIsLoading = false;
        switch (aViewFor) {
            case API_ERROR:
            case HTTP_ERROR:
                if (peopleList.isEmpty()) {
                    rvHome.setVisibility(View.GONE);
                    evHome.setVisibility(View.VISIBLE);
                    showError("My mind stopped.\nPlease try again");
                    evHome.setOnRetryListener(new ErrorView.RetryListener() {
                        @Override
                        public void onRetry() {
                            homePresenter.getPeopleList(pageNo);
                        }
                    });
                } else {
                    Toast.makeText(this, "My mind stopped.\nPlease try again", Toast.LENGTH_LONG).show();
                }
                break;
            case NO_NETWORK:
                rvHome.setVisibility(View.GONE);
                showError("Using my mind to connect, but it's not connected to internet.");
                evHome.setOnRetryListener(new ErrorView.RetryListener() {
                    @Override
                    public void onRetry() {
                        homePresenter.getPeopleList(pageNo);
                    }
                });
                evHome.setVisibility(View.VISIBLE);
                break;
            case PEOPLE_LIST:
                evHome.setVisibility(View.GONE);
                rvHome.setVisibility(View.VISIBLE);
                break;

        }

    }

    @Override
    public void onItemClick(int position) {
        DetailActivity.startActivity(this, peopleList.get(position).getUrl());
    }
}
