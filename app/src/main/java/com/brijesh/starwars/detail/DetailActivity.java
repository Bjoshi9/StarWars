package com.brijesh.starwars.detail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brijesh.starwars.R;
import com.brijesh.starwars.data.model.People;
import com.brijesh.starwars.widget.ErrorView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class DetailActivity extends AppCompatActivity implements DetailView {

    private static final String DETAIL_URL = "DetailUrl";
    private String detailUrl;
    private TextView tvName, tvMass, tvHeight, tvCreatedOn;
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
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        detailUrl = getIntent().getStringExtra(DETAIL_URL);
        tvName = findViewById(R.id.tvName);
        tvHeight = findViewById(R.id.tvHeight);
        tvMass = findViewById(R.id.tvMass);
        tvCreatedOn = findViewById(R.id.tvCreatedOn);
        progressBar = findViewById(R.id.progressBar);
        clDetail = findViewById(R.id.clDetail);
        evDetail = findViewById(R.id.evDetail);
        detailPresenter = new DetailPresenter(this);
        detailPresenter.getPeopleDetail(detailUrl);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void showDetails(People people) {
        tvName.setText(people.getName());
        tvHeight.setText(people.getHeight() + " (meters)");
        tvMass.setText(people.getMass() + " (kg)");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        try {
            tvCreatedOn.setText(new SimpleDateFormat("dd MMM, yyyy' at 'HH:mm").format(simpleDateFormat.parse(people.getCreated())));
        } catch (ParseException e) {

        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
