package com.example.bluechatapp.MusicActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluechatapp.Bean.Song;
import com.example.bluechatapp.MainActivity.MainActivity;
import com.example.bluechatapp.R;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallbackWithBeforeParam;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.ForwardScope;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;
import org.litepal.parser.LitePalParser;

import java.util.ArrayList;
import java.util.List;

public class MusicListActivity extends AppCompatActivity implements View.OnClickListener {
    // 设置toolbar
    private Toolbar toolbar ;
    // 设置music的recycleview
    private RecyclerView musicRV ;
    private MusicRVAdapter rvAdapter ;
    private List<Song> songList ;
    // 设置扫描音乐button
    private ImageButton scanButton ;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musiclist);
        findViewForAllComponent();
        loadMusicDataFromDataBase() ;
        initializeComponent();
        registerOnClickEvent() ;
        permissionsRequest();
    }

    private void loadMusicDataFromDataBase() {
        songList = LitePal.findAll(Song.class) ;
        if(songList==null){
            songList = new ArrayList<>() ;
        }
    }

    private void registerOnClickEvent() {
        scanButton.setOnClickListener(this);
    }

    // 为所有组件寻找布局
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void findViewForAllComponent() {
        toolbar = findViewById(R.id.music_toolbar) ;
        musicRV = findViewById(R.id.music_list_rv) ;
        scanButton = findViewById(R.id.scan_songs_btn) ;
    }
    // 对所有组件进行初始化
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initializeComponent() {
        createToolbar();
        createMusicRV() ;
    }

    private void createMusicRV() {
        musicRV.setLayoutManager(new LinearLayoutManager(MusicListActivity.this));
        musicRV.addItemDecoration(new MyDecoraton());
        rvAdapter = new MusicRVAdapter(MusicListActivity.this,songList) ;
        musicRV.setAdapter(rvAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createToolbar(){
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMainActivity = new Intent(MusicListActivity.this, MainActivity.class) ;
                startActivity(toMainActivity);
            }
        });
        // 设置状态栏和toolbar同背景
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            WindowManager.LayoutParams barLayoutParams=getWindow().getAttributes();
            barLayoutParams.flags=(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS|barLayoutParams.flags);

        }

    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.scan_songs_btn:{
                // 扫描系统歌曲
                List<Song> musicList = MusicUtils.scanSysMusic(MusicListActivity.this) ;
                rvAdapter.setMusicList(musicList);
                rvAdapter.notifyDataSetChanged();
                System.out.println("扫描出歌曲数："+musicList.size());
                break ;
            }
        }
    }
    class MyDecoraton extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0,0,0,3);
        }
    }
    /**
     * 动态权限请求
     */
    private void permissionsRequest() {

        PermissionX.init(this).permissions(
                //写入文件
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onExplainRequestReason(new ExplainReasonCallbackWithBeforeParam() {
                    @Override
                    public void onExplainReason(ExplainScope scope, List<String> deniedList, boolean beforeRequest) {
                        scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "我已明白");
                    }
                })
                .onForwardToSettings(new ForwardToSettingsCallback() {
                    @Override
                    public void onForwardToSettings(ForwardScope scope, List<String> deniedList) {
                        scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "我已明白");
                    }
                })
                .setDialogTintColor(R.color.white, R.color.permissionx_tint_color)
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {
                            //通过后的业务逻辑
                        } else {
                        }
                    }
                });
    }

}
