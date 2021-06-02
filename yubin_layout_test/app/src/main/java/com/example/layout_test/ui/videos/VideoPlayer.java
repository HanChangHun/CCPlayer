package com.example.layout_test.ui.videos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.layout_test.R;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


import java.util.ArrayList;
import java.util.Objects;

import static com.example.layout_test.ui.videos.VideoFileAdapter.videoFiles;

public class VideoPlayer {
    private String TAG = "201521037";
    private Context context;
    private PlayerController playerController;

    private PlayerView playerView;
    private SimpleExoPlayer exoPlayer;
    private ComponentListener componentListener;
    private String path;

    public VideoPlayer(PlayerView playerView,
                       Context context,
                       String path,
                       PlayerController mView) {
        this.playerView = playerView;
        this.context = context;
        this.playerController = mView;
        this.path = path;
        initializePlayer();
    }

    protected void initializePlayer() {
        playerView.requestFocus();

        componentListener = new ComponentListener();

        if (path != null) {
            Uri uri = Uri.parse(path);
            exoPlayer = new SimpleExoPlayer.Builder(context).build();
            DataSource.Factory factory = new DefaultDataSourceFactory(context,
                    Util.getUserAgent(context,
                            "CCPlayer"));
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            playerView.setPlayer(exoPlayer);
            playerView.setKeepScreenOn(true);
            exoPlayer.setPlayWhenReady(true);
            exoPlayer.addListener(componentListener);

            ProgressiveMediaSource mediaSource = new ProgressiveMediaSource.Factory(factory,
                    extractorsFactory).createMediaSource(uri);
            exoPlayer.prepare(mediaSource);
        }
    }

    public void pausePlayer() {
        exoPlayer.setPlayWhenReady(false);
    }

    public void resumePlayer() {
        exoPlayer.setPlayWhenReady(true);
    }

    public void releasePlayer() {
        if (exoPlayer == null)
            return;

        playerView.setPlayer(null);
        exoPlayer.release();
        exoPlayer.removeListener(componentListener);
        exoPlayer = null;

    }

    public SimpleExoPlayer getPlayer() {
        return exoPlayer;
    }


    private class ComponentListener implements Player.EventListener {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            Log.d(TAG, "onPlayerStateChanged: playWhenReady: " + playWhenReady + " playbackState: " + playbackState);
            switch (playbackState) {
                case Player.STATE_IDLE:
                    playerController.showProgressBar(false);
                    playerController.showRetryBtn(true);
                    break;
                case Player.STATE_BUFFERING:
                    playerController.showProgressBar(true);
                    break;
                case Player.STATE_READY:
                    playerController.showProgressBar(false);
                    playerController.audioFocus();
                    break;
                case Player.STATE_ENDED:
                    playerController.showProgressBar(false);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            playerController.showProgressBar(false);
            playerController.showRetryBtn(true);
        }
    }
}