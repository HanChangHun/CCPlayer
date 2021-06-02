package com.example.layout_test.ui.videos;

public interface PlayerController {
    void showProgressBar(boolean visible);

    void showRetryBtn(boolean visible);

    void showSubtitle(boolean show);

    void audioFocus();
}
