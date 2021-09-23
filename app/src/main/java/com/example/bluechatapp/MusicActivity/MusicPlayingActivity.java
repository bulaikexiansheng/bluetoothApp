package com.example.bluechatapp.MusicActivity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.bluechatapp.Bean.Song;
import com.example.bluechatapp.R;
import com.example.bluechatapp.Static.Utils.ToastUtil;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallbackWithBeforeParam;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.ForwardScope;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayingActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MusicPlayingActivity.class.getSimpleName();
    // 组件声明
    private ImageButton backToMusicListBtn ;
    private ImageButton musicPlayButton ;
    private ImageButton previousSongButton ;
    private ImageButton nextSongButton ;
    private SeekBar seekBar ;
    private TextView songName ;
    private TextView singer ;
    private LinearLayout musicBg ;
    private ImageView songPic ;
    // 音乐播放器组件
    private MusicPlayerHelper helper ;
    // 歌曲列表
    private List<Song> songList ;
    // 当前播放歌曲游标位置
    private int mPosition ;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        findViewForAllComponent();
        initializeComponent();
        getDataFromListActivity() ;
        registerClickEvent();
        permissionsRequest();

        try {
            play(songList.get(mPosition),true ) ;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从list活动获得列表音乐，以及点击的歌曲信息
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getDataFromListActivity() {
        Intent intent = getIntent() ;
        songList = (List<Song>) intent.getSerializableExtra("SongList") ;
        mPosition = intent.getIntExtra("Selected",0) ;
        changeSongMessage(songList.get(mPosition));
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    /**
     *  每切换一首歌就更新消息
     */
    private void changeSongMessage(Song song){
        songName.setText(song.getSongName()+"");
        singer.setText(song.getSinger()+"");
        songPic.setImageDrawable(getDrawable(SongPic.getDrawing()));
        musicBg.setBackground(getDrawable(MusicBackGround.getDrawing()));
    }

    // 为所有组件寻找布局
    private void findViewForAllComponent() {
        backToMusicListBtn= findViewById(R.id.music_back) ;
        musicPlayButton = findViewById(R.id.music_control_btn) ;
        previousSongButton = findViewById(R.id.music_previous_btn) ;
        nextSongButton = findViewById(R.id.music_next_btn) ;
        seekBar = findViewById(R.id.music_seekbar) ;
        songName = findViewById(R.id.music_name_tv) ;
        singer = findViewById(R.id.music_singer) ;
        musicBg = findViewById(R.id.music_activity_bg);
        songPic = findViewById(R.id.song_pic_iv);
    }
    // 对所有组件进行初始化
    private void initializeComponent() {
        createMediaPlayer() ;
    }

    private void createMediaPlayer() {
        helper = new MusicPlayerHelper(seekBar,new TextView(MusicPlayingActivity.this)) ;
        helper.setOnCompletionListener(new MusicPlayerHelper.OnCompletionListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.e(TAG, "next()");
                //下一曲
                try {
                    next();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // 注册点击事件
    private void registerClickEvent() {
        backToMusicListBtn.setOnClickListener(this) ;
        musicPlayButton.setOnClickListener(this);
        previousSongButton.setOnClickListener(this);
        nextSongButton.setOnClickListener(this);
    }
    // 点击事件
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.music_back:{
                helper.destroy();
                Intent toMusicListActivity = new Intent(MusicPlayingActivity.this, MusicListActivity.class) ;
                startActivity(toMusicListActivity);
                break ;
            }
            case R.id.music_control_btn:{
                try {
                    play(songList.get(mPosition),false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break ;
            }
            case R.id.music_previous_btn:{
                try {
                    last() ;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break ;
            }
            case R.id.music_next_btn:{
                try {
                    next();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break ;
            }
            default:
                break ;
        }
    }
    /**
     * 播放歌曲
     *
     * @param songModel    播放源
     * @param isRestPlayer true 切换歌曲 false 不切换
     */
    private void play(Song songModel, Boolean isRestPlayer) throws IOException {
        if (!TextUtils.isEmpty(songModel.getPath())) {
            Log.e(TAG, String.format("当前状态：%s  是否切换歌曲：%s", helper.isPlaying(), isRestPlayer));
            // 当前若是播放，则进行暂停
            if (!isRestPlayer && helper.isPlaying()) {
                pause();
                musicPlayButton.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
            } else {
                //进行切换歌曲播放
                helper.playBySong(songModel, isRestPlayer);
                // 正在播放的列表进行更新哪一首歌曲正在播放 主要是为了更新列表里面的显示
                for (int i = 0; i < songList.size(); i++) {
                    songList.get(i).setChecked(mPosition == i);
                }
                musicPlayButton.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
            }
        } else {
           ToastUtil.showShortMessage(MusicPlayingActivity.this,"当前的播放地址无效");
        }
    }
    /**
     * 上一首
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void last() throws IOException {
        mPosition--;
        //如果上一曲小于0则取最后一首
        if (mPosition < 0) {
            mPosition = songList.size() - 1;
        }
        play(songList.get(mPosition), true);
        changeSongMessage(songList.get(mPosition));
    }

    /**
     * 下一首
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void next() throws IOException {
        mPosition++;
        //如果下一曲大于歌曲数量则取第一首
        if (mPosition >= songList.size()) {
            mPosition = 0;
        }
        play(songList.get(mPosition), true);
        changeSongMessage(songList.get(mPosition));
    }
    /**
     * 暂停播放
     */
    private void pause() {
        helper.pause();
        musicPlayButton.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
    }

    /**
     * 停止播放
     */
    private void stop() {
        helper.stop();
    }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.destroy();
    }
}