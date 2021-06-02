package com.example.layout_test.ui.videos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.example.layout_test.R;
import com.example.layout_test.ui.videos.srt.SRTTools;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;


import java.util.ArrayList;
import java.util.Objects;

import static com.example.layout_test.ui.videos.VideoFileAdapter.videoFiles;

public class VideoPlayer {
    private String TAG = "201521037";
    private Context context;
    private PlayerController playerController;

    SRTTools srtTools;
    private PlayerView playerView;
    private SimpleExoPlayer exoPlayer;
    private MediaSource mediaSource;
    private int widthOfScreen;
    private ComponentListener componentListener;
    private CacheDataSourceFactory cacheDataSourceFactory;
    private String path;
    private boolean isLock = false;

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
        srtTools = new SRTTools();
        srtTools.parseSRT(path.replace(".mp4", ".srt"));
        playerView.requestFocus();
        componentListener = new ComponentListener();
        cacheDataSourceFactory = new CacheDataSourceFactory(
                context,
                100 * 1024 * 1024,
                5 * 1024 * 1024);

        if (path != null) {
            exoPlayer = new SimpleExoPlayer.Builder(context).build();

            playerView.setPlayer(exoPlayer);
            playerView.setKeepScreenOn(true);
            exoPlayer.setPlayWhenReady(true);
            exoPlayer.addListener(componentListener);

            mediaSource = buildMediaSource(Uri.parse(path), cacheDataSourceFactory);
            exoPlayer.prepare(mediaSource);
        }
    }

    private MediaSource buildMediaSource(Uri uri, CacheDataSourceFactory cacheDataSourceFactory) {
        @C.ContentType int type = Util.inferContentType(uri);
        switch (type) {
            case C.TYPE_SS:
                return new SsMediaSource.Factory(cacheDataSourceFactory).createMediaSource(uri);
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(cacheDataSourceFactory).createMediaSource(uri);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(cacheDataSourceFactory).createMediaSource(uri);
            case C.TYPE_OTHER:
                return new ProgressiveMediaSource.Factory(cacheDataSourceFactory).createMediaSource(uri);
            default: {
                throw new IllegalStateException("Unsupported type: " + uri);
            }
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

    @SuppressLint("ClickableViewAccessibility")
    public void seekToOnDoubleTap() {
        getWidthOfScreen();
        final GestureDetector gestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        float positionOfDoubleTapX = e.getX();
                        Log.d(TAG, "onDoubleTap: size of srts: " + srtTools.getSrts().size());
                        long current = exoPlayer.getCurrentPosition();
                        if (positionOfDoubleTapX < (int) (widthOfScreen / 2)) {
                            Log.d(TAG, "onDoubleTap: getPrev: (current:" + current + "), position: " + srtTools.getPrev(current));
                            exoPlayer.seekTo(srtTools.getPrev(exoPlayer.getCurrentPosition()));
                        }
                        else {
                            Log.d(TAG, "onDoubleTap: getNext: " + srtTools.getNext(current));
                            exoPlayer.seekTo(srtTools.getNext(exoPlayer.getCurrentPosition()));
                        }

                        Log.d(TAG, "onDoubleTap(): widthOfScreen >> " + widthOfScreen +
                                " positionOfDoubleTapX >>" + positionOfDoubleTapX);
                        return true;
                    }
                });

        playerView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
    }

    private void getWidthOfScreen() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        widthOfScreen = metrics.widthPixels;
    }

    public void setSelectedSubtitle(String path) {
        Format subtitleFormat = Format.createTextSampleFormat(
                null,
                MimeTypes.APPLICATION_SUBRIP,
                Format.NO_VALUE,
                null);

        MediaSource subtitleSource = new SingleSampleMediaSource
                .Factory(cacheDataSourceFactory)
                .createMediaSource(Uri.parse(path), subtitleFormat, C.TIME_UNSET);

        exoPlayer.prepare(new MergingMediaSource(mediaSource, subtitleSource), false, false);
        playerController.showSubtitle(true);
        resumePlayer();
    }

    public void lockScreen(boolean isLock) {
        this.isLock = isLock;
    }

    public boolean isLock() {
        return isLock;
    }

    private class ComponentListener implements Player.EventListener {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
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