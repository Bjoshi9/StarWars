package com.brijesh.starwars.widget;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brijesh.starwars.R;

public class ErrorView extends LinearLayout {

    private ImageView mErrorImageView;
    private TextView mTitleTextView;
    private Button mRetryButton;

    int imageRes;

    String title;
    int titleColor;

    boolean showTitle;
    boolean showImage;
    boolean showRetryButton;

    String retryButtonText;
    int retryButtonBackground;
    int retryButtonTextColor;

    private RetryListener mListener;

    public ErrorView(Context context) {
        this(context, null);
    }

    public ErrorView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.ev_style);
    }

    public ErrorView(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, 0);
    }

    public ErrorView(Context context, AttributeSet attrs, int defStyle, int defStyleRes) {
        super(context, attrs);
        init(attrs, defStyle, defStyleRes);
    }

    private void init(AttributeSet attrs, int defStyle, int defStyleRes) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ErrorView, defStyle, defStyleRes);

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.layout_error_view, this, true);
            setOrientation(VERTICAL);
            setGravity(Gravity.CENTER);
            setBackgroundColor(getResources().getColor(android.R.color.white));
            setLayoutTransition(new LayoutTransition());
            mErrorImageView = findViewById(R.id.error_image);
            mTitleTextView = findViewById(R.id.error_title);
            mRetryButton = findViewById(R.id.error_retry);
            try {
                title = a.getString(R.styleable.ErrorView_ev_title);
                titleColor = a.getColor(R.styleable.ErrorView_ev_titleColor, getResources().getColor(R.color.error_view_text));
                showTitle = a.getBoolean(R.styleable.ErrorView_ev_showTitle, true);
                showImage = a.getBoolean(R.styleable.ErrorView_ev_showImage, true);
                if (showImage) {
                    imageRes = a.getResourceId(R.styleable.ErrorView_ev_errorImage, R.drawable.yoda_error_image);
                }
                showRetryButton = a.getBoolean(R.styleable.ErrorView_ev_showRetryButton, true);
                retryButtonText = a.getString(R.styleable.ErrorView_ev_retryButtonText);
                retryButtonBackground = a.getResourceId(R.styleable.ErrorView_ev_retryButtonBackground, R.color.retry_back_ground);
                retryButtonTextColor = a.getColor(R.styleable.ErrorView_ev_retryButtonTextColor, getResources().getColor(android.R.color.white));


                if (title != null) {
                    setTitle(title);
                }

                if (retryButtonText != null) {
                    mRetryButton.setText(retryButtonText);
                }

                if (showImage) {
                    if (imageRes != 0) {
                        setImage(imageRes);
                    }
                    mErrorImageView.setVisibility(VISIBLE);
                } else {
                    mErrorImageView.setVisibility(GONE);
                }

                mTitleTextView.setVisibility(showTitle ? VISIBLE : GONE);
                mRetryButton.setVisibility(showRetryButton ? VISIBLE : GONE);

                mTitleTextView.setTextColor(titleColor);
                mRetryButton.setTextColor(retryButtonTextColor);
                mRetryButton.setBackgroundResource(retryButtonBackground);

            } finally {
                a.recycle();
            }

            mRetryButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onRetry();
                    }
                }
            });
        }
    }

    public void setOnRetryListener(RetryListener listener) {
        mListener = listener;
    }

    /**
     * Loads ErrorView configuration from a {@link Config} object.
     *
     * @param config configuration to load from
     */
    public void setConfig(Config config) {
        if (config.getImage() != null) {
            Object image = config.getImage();
            if (image instanceof Integer) {
                setImage((int) image);
            } else if (image instanceof Drawable) {
                setImage((Drawable) image);
            } else if (image instanceof Bitmap) {
                setImage((Bitmap) image);
            }
        }

        if (config.getTitle() != null) {
            setTitle(config.getTitle());
        }

        if (config.getTitleColor() != 0) {
            setTitleColor(config.getTitleColor());
        }

        showRetryButton(config.shouldShowRetryButton());
        showImage(config.shouldShowImage());
        showTitle(config.shouldShowTitle());
        if (config.getRetryButtonText() != null) {
            setRetryButtonText(config.getRetryButtonText());
        }

        if (config.getRetryButtonTextColor() != 0) {
            setRetryButtonTextColor(config.getRetryButtonTextColor());
        }
    }

    /**
     * Returns the current ErrorView configuration.
     */
    public Config getConfig() {
        return Config.create()
                .image(getImage())
                .title(getTitle())
                .titleColor(getTitleColor())
                .retryVisible(isRetryButtonVisible())
                .retryText(getRetryButtonText())
                .retryTextColor(getRetryButtonTextColor())
                .build();
    }

    /**
     * Sets error image to a given drawable resource
     *
     * @param res drawable resource.
     * @deprecated Use {@link #setImage(int)} instead.
     */
    @Deprecated
    public void setImageResource(int res) {
        setImage(res);
    }

    /**
     * Sets the error image to a given {@link Drawable}.
     *
     * @param drawable {@link Drawable} to use as error image.
     * @deprecated Use {@link #setImage(Drawable)} instead.
     */
    @Deprecated
    public void setImageDrawable(Drawable drawable) {
        setImage(drawable);
    }

    /**
     * Sets the error image to a given {@link Bitmap}.
     *
     * @param bitmap {@link Bitmap} to use as error image.
     * @deprecated Use {@link #setImage(Bitmap)} instead.
     */
    @Deprecated
    public void setImageBitmap(Bitmap bitmap) {
        setImage(bitmap);
    }

    /**
     * Sets error image to a given drawable resource
     *
     * @param res drawable resource.
     */
    public void setImage(int res) {
        mErrorImageView.setImageResource(res);
    }

    /**
     * Sets the error image to a given {@link Drawable}.
     *
     * @param drawable {@link Drawable} to use as error image.
     */
    public void setImage(Drawable drawable) {
        mErrorImageView.setImageDrawable(drawable);
    }

    /**
     * Sets the error image to a given {@link Bitmap}.
     *
     * @param bitmap {@link Bitmap} to use as error image.
     */
    public void setImage(Bitmap bitmap) {
        mErrorImageView.setImageBitmap(bitmap);
    }

    /**
     * Returns the current error iamge
     */
    public Drawable getImage() {
        return mErrorImageView.getDrawable();
    }

    /**
     * Sets the error title to a given {@link String}.
     *
     * @param text {@link String} to use as error title.
     */
    public void setTitle(String text) {
        mTitleTextView.setText(text);
    }

    /**
     * Sets the error title to a given string resource.
     *
     * @param res string resource to use as error title.
     */
    public void setTitle(int res) {
        mTitleTextView.setText(res);
    }

    /**
     * Returns the current title string.
     */
    public String getTitle() {
        return mTitleTextView.getText().toString();
    }

    /**
     * Sets the error title text to a given color.
     *
     * @param res color resource to use for error title text.
     */
    public void setTitleColor(int res) {
        mTitleTextView.setTextColor(res);
    }

    /**
     * Returns the current title text color.
     */
    public int getTitleColor() {
        return mTitleTextView.getCurrentTextColor();
    }

    /**
     * Sets the retry button's text to a given {@link String}.
     *
     * @param text {@link String} to use as retry button text.
     */
    public void setRetryButtonText(String text) {
        mRetryButton.setText(text);
    }

    /**
     * Sets the retry button's text to a given string resource.
     *
     * @param res string resource to be used as retry button text.
     */
    public void setRetryButtonText(int res) {
        mRetryButton.setText(res);
    }

    /**
     * Returns the current retry button text.
     */
    public String getRetryButtonText() {
        return mRetryButton.getText().toString();
    }

    /**
     * Sets the retry button's text color to a given color.
     *
     * @param color int color to be used as text color.
     */
    public void setRetryButtonTextColor(int color) {
        mRetryButton.setTextColor(color);
    }

    /**
     * Returns the current retry button text color.
     */
    public int getRetryButtonTextColor() {
        return mRetryButton.getCurrentTextColor();
    }

    /**
     * Shows or hides the error title
     */
    public void showTitle(boolean show) {
        mTitleTextView.setVisibility(show ? VISIBLE : GONE);
    }

    /**
     * Indicates whether the title is currently visible.
     */
    public boolean isTitleVisible() {
        return mTitleTextView.getVisibility() == VISIBLE;
    }

    /**
     * Shows or hides the retry button.
     */
    public void showRetryButton(boolean show) {
        mRetryButton.setVisibility(show ? VISIBLE : GONE);
    }

    public void showImage(boolean show) {
        mErrorImageView.setVisibility(show ? VISIBLE : GONE);
    }

    /**
     * Indicates whether the retry button is visible.
     */
    public boolean isRetryButtonVisible() {
        return mRetryButton.getVisibility() == VISIBLE;
    }

    public static class Config {
        private Object mImage;
        private String mTitle;
        private int mTitleColor;
        private boolean mShowRetryButton = true;
        private boolean mShowImage = true;
        private boolean mShowTitle = true;
        private String mRetryButtonText;
        private int mRetryButtonTextColor;

        public static Builder create() {
            return new Builder();
        }

        private Config() {
            /* empty constructor */
        }

        public Object getImage() {
            return mImage;
        }

        public String getTitle() {
            return mTitle;
        }

        public int getTitleColor() {
            return mTitleColor;
        }

        public boolean shouldShowRetryButton() {
            return mShowRetryButton;
        }

        public boolean shouldShowImage() {
            return mShowImage;
        }

        public boolean shouldShowTitle() {
            return mShowTitle;
        }

        public String getRetryButtonText() {
            return mRetryButtonText;
        }

        public int getRetryButtonTextColor() {
            return mRetryButtonTextColor;
        }

        public static class Builder {
            private Config config;

            private Builder() {
                config = new Config();
            }

            public Builder image(int res) {
                config.mImage = res;
                return this;
            }

            public Builder image(Drawable drawable) {
                config.mImage = drawable;
                return this;
            }

            public Builder image(Bitmap bitmap) {
                config.mImage = bitmap;
                return this;
            }

            public Builder title(String title) {
                config.mTitle = title;
                return this;
            }

            public Builder titleColor(int color) {
                config.mTitleColor = color;
                return this;
            }

            public Builder retryVisible(boolean visible) {
                config.mShowRetryButton = visible;
                return this;
            }

            public Builder imageVisible(boolean visible) {
                config.mShowImage = visible;
                return this;
            }

            public Builder titleVisible(boolean visible) {
                config.mShowTitle = visible;
                return this;
            }

            public Builder retryText(String text) {
                config.mRetryButtonText = text;
                return this;
            }

            public Builder retryTextColor(int color) {
                config.mRetryButtonTextColor = color;
                return this;
            }

            public Config build() {
                return config;
            }
        }
    }

    public interface RetryListener {
        void onRetry();
    }
}