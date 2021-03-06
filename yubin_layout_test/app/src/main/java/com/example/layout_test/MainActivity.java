package com.example.layout_test;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.example.layout_test.ui.videos.VideoUtils;

import com.example.layout_test.ui.videos.VideoFile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

import static androidx.navigation.ui.NavigationUI.setupActionBarWithNavController;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION = 123;
    public static ArrayList<VideoFile> videoFiles = new ArrayList<>();
    public static ArrayList<String> folderList = new ArrayList<>();
    public static FirebaseDatabase fireDB = null;
    public static FirebaseAuth fireAuth = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Context 설정
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 하단 Navigation 바
        BottomNavigationView navView = findViewById(R.id.nav_view);

        permission();  // 창헌 추가 (외부 파일 권한 요청)

        // 파이어베이스 DB 사용하기 앞서 핸들 가져오기
        fireDB = FirebaseDatabase.getInstance(); // 데이터베이스 접근용
        fireAuth = FirebaseAuth.getInstance(); // 로그인/회원가입용

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_video, R.id.navigation_calendar, R.id.navigation_community, R.id.navigation_mypage)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // 창헌 추가 (이걸 해야 불필요한 뒤로가기 버튼이 없어진다.)
        setupActionBarWithNavController(this, navController,
                new AppBarConfiguration.Builder(R.id.navigation_video_folder, R.id.navigation_video_file,
                        R.id.navigation_video, R.id.navigation_home, R.id.navigation_community,
                        R.id.navigation_calendar, R.id.navigation_mypage).build());
    }

    // 유빈 추가 시작 (파이어베이스 관련)
    public static FirebaseDatabase getFireDB() {
        return fireDB;
    }

    public static FirebaseAuth getFireAuth() {
        return fireAuth;
    }
    // 유빈 추가 끝

    // 창헌 추가 시작 (외부 파일 권한 관련)
    private void permission() {
        String TAG = "201521037";
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        } else {
            videoFiles = VideoUtils.getAllVideos(this, null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                videoFiles = VideoUtils.getAllVideos(this, null);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
            }
        }
    }
    // 창헌 추가 끝
}