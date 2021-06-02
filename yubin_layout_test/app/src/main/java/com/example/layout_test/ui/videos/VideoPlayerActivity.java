package com.example.layout_test.ui.videos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.layout_test.R;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.layout_test.ui.videos.VideoFileAdapter.videoFiles;

public class VideoPlayerActivity extends AppCompatActivity implements View.OnClickListener, PlayerController {
    private String TAG = "201521037";

    private PlayerView playerView;
    private VideoPlayer player;
    private ImageButton subtitle, retry, back;
    private ProgressBar progressBar;
    private AudioManager mAudioManager;
    private int position = -1;
    ArrayList<VideoFile> mFiles = new ArrayList<>();
    private boolean disableBackPress = false;
    private boolean toggleSubtitle = true;

    private final AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    switch (focusChange) {
                        case AudioManager.AUDIOFOCUS_GAIN:
                            if (player != null)
//                                  player.getPlayer().setPlayWhenReady(true);
                                break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                            // Audio focus was lost, but it's possible to duck (i.e.: play quietly)
                            if (player != null)
                                player.getPlayer().setPlayWhenReady(false);
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        case AudioManager.AUDIOFOCUS_LOSS:
                            // Lost audio focus, probably "permanently"
                            // Lost audio focus, but will gain it back (shortly), so note whether
                            // playback should resume
                            if (player != null)
                                player.getPlayer().setPlayWhenReady(false);
                            break;
                    }
                }
            };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.screen_video_player);
        Objects.requireNonNull(getSupportActionBar()).hide();

        getDataFromIntent();
        setupLayout();
        initSource();
        showSubtitle(true);
    }


    private void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void getDataFromIntent() {
        position = getIntent().getIntExtra("position", -1);
    }

    private void setupLayout() {
        playerView = findViewById(R.id.exoplayer_movie);
        progressBar = findViewById(R.id.progress_bar);

        subtitle = findViewById(R.id.btn_subtitle);
        retry = findViewById(R.id.retry_btn);
        back = findViewById(R.id.btn_back);

        subtitle.setOnClickListener(this);
        retry.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    private void initSource() {
        mFiles = videoFiles;
        String path = mFiles.get(position).getPath();

        player = new VideoPlayer(playerView, getApplicationContext(), path, this);

        mAudioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        playerView.getSubtitleView().setVisibility(View.GONE);
        player.seekToOnDoubleTap();
    }

    @Override
    public void showProgressBar(boolean visible) {
        progressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showRetryBtn(boolean visible) {
        retry.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showSubtitle(boolean show) {
        toggleSubtitle = !toggleSubtitle;
        if (player == null || playerView.getSubtitleView() == null || !toggleSubtitle || !show) {
            subtitle.setImageResource(R.drawable.exo_no_subtitle_btn);
            playerView.getSubtitleView().setVisibility(View.GONE);
            return;
        }

        subtitle.setImageResource(R.drawable.exo_subtitle_btn);
        playerView.getSubtitleView().setVisibility(View.VISIBLE);
    }

    @Override
    public void audioFocus() {
        mAudioManager.requestAudioFocus(
                mOnAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (player != null)
            player.resumePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if (player != null)
            player.resumePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null)
            player.releasePlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (mAudioManager != null) {
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
            mAudioManager = null;
        }
        if (player != null) {
            player.releasePlayer();
            player = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (disableBackPress)
            return;

        super.onBackPressed();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        hideSystemUi();
    }

    @Override
    public void onClick(View v) {
        int controllerId = v.getId();

        switch (controllerId) {
            case R.id.btn_subtitle:
                prepareSubtitles();
                break;
            case R.id.btn_back:
                onBackPressed();
                break;

            default:
                break;
        }
    }

    private void prepareSubtitles() {
        String subtitleUrl = mFiles.get(position).getSubtitlePath();
        if (player == null || playerView.getSubtitleView() == null || subtitleUrl == null) {
            subtitle.setImageResource(R.drawable.exo_no_subtitle_btn);
            return;
        }

        player.pausePlayer();
        player.setSelectedSubtitle(subtitleUrl);
    }


}
