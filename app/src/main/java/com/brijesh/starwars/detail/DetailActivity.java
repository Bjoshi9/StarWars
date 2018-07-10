package com.brijesh.starwars.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brijesh.starwars.R;
import com.brijesh.starwars.data.model.People;
import com.brijesh.starwars.widget.ErrorView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class DetailActivity extends AppCompatActivity implements DetailView {

    private static final String DETAIL_URL = "DetailUrl";
    private String detailUrl;
    private TextView tvName;
    private DetailPresenter detailPresenter;
    private ProgressBar progressBar;
    private ErrorView evDetail;
    private ConstraintLayout clDetail;

    public static void startActivity(Context context, String detailUrl) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DETAIL_URL, detailUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        detailUrl = getIntent().getStringExtra(DETAIL_URL);
        tvName = findViewById(R.id.tvName);
        progressBar = findViewById(R.id.progressBar);
        clDetail = findViewById(R.id.clDetail);
        evDetail = findViewById(R.id.evDetail);
        detailPresenter = new DetailPresenter(this);
        detailPresenter.getPeopleDetail(detailUrl);
    }

    @Override
    public void showDetails(People people) {
        tvName.setText(people.getName());
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
        evDetail.setTitle(errorMessage);
    }

    @Override
    public void setViewFor(int aViewFor) {
        hideProgressbar();
        switch (aViewFor) {
            case API_ERROR:
            case HTTP_ERROR:
                clDetail.setVisibility(View.GONE);
                showError("YODA Training them. Please try again");
                evDetail.setVisibility(View.VISIBLE);
                evDetail.setOnRetryListener(new ErrorView.RetryListener() {
                    @Override
                    public void onRetry() {
                        evDetail.setVisibility(View.GONE);
                        detailPresenter.getPeopleDetail(detailUrl);
                    }
                });
                break;
            case NO_NETWORK:
                clDetail.setVisibility(View.GONE);
                showError("YODA Cannot connect to the internet.");
                evDetail.setOnRetryListener(new ErrorView.RetryListener() {
                    @Override
                    public void onRetry() {
                        evDetail.setVisibility(View.GONE);
                        detailPresenter.getPeopleDetail(detailUrl);
                    }
                });
                evDetail.setVisibility(View.VISIBLE);
                break;
            case DETAIL_VIEW:
                evDetail.setVisibility(View.GONE);
                clDetail.setVisibility(View.VISIBLE);
                break;

        }
    }
}
