package com.example.bluechatapp.MusicActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bluechatapp.Bean.Song;

import java.io.IOException;
import java.lang.ref.WeakReference;


public class MusicPlayerHelper implements MediaPlayer.OnBufferingUpdateListener,MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {

    public static String TAG = MusicPlayerHelper.class.getSimpleName();
    private static int MSG_CODE = 0x01;
    private static long MSG_TIME = 1_000L;
    private OnCompletionListener onCompletionListener ;
    private MusicPlayerHelperHandler mHandler;
    /**
     * 进度条
     */
    private SeekBar seekBar ;
    /**
     * 显示播放信息
     */
    private TextView text;
    /**
     * 播放器
     */
    private MediaPlayer player ;

    /**
     * 当前播放的歌曲信息
     */
    private Song currentSong ;

    public MusicPlayerHelper(SeekBar seekBar, TextView text) {
        mHandler = new MusicPlayerHelperHandler(this);
        player = new MediaPlayer();
        // 设置媒体流类型
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnBufferingUpdateListener(this);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        this.seekBar = seekBar;
        this.text = text;
    }

    /**
     *
     * 媒体资源的缓冲状态
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBar.setSecondaryProgress(percent);
        int currentProgress = seekBar.getMax() * player.getCurrentPosition() / player.getDuration() ;
        Log.e(TAG, currentProgress + "% play --> " + percent + "% buffer");
    }

    /**
     * 当前Song播放完毕
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        onCompletionListener.onCompletion(player);
    }

    /**
     * 当前Song已经准备好了
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.e(TAG, "onPrepared");
        mp.start();
    }

    /**
     *
     * @param currentSong  播放源
     * @param isRestPlayer true切换歌曲，false不切换
     */
    public void playBySong(Song currentSong,Boolean isRestPlayer ) throws IOException {
        this.currentSong = currentSong ;
        Log.e(TAG, "playBySongModel Url: " + currentSong.getPath());
        if (isRestPlayer){
            // 重置多媒体
            player.reset() ;
            if (!TextUtils.isEmpty(currentSong.getPath())){
                player.setDataSource(currentSong.getPath());
            }
            player.prepareAsync();
        }else{
            player.start();
        }
        //发送更新命令
        mHandler.sendEmptyMessage(MSG_CODE) ;
    }
    /**
     * 暂停
     */
    public void pause() {
        Log.e(TAG, "pause");
        if (player.isPlaying()) {
            player.pause();
        }
    }

    /**
     * 停止
     */
    public void stop() {
        Log.e(TAG, "stop");
        player.stop();
        seekBar.setProgress(0);
        text.setText("停止播放");
        //移除更新命令
        mHandler.removeMessages(MSG_CODE);
    }
    /**
     * 是否正在播放
     */
    public Boolean isPlaying() {
        return player.isPlaying();
    }

    /**
     * 消亡 必须在 Activity 或者 Frament onDestroy() 调用 以防止内存泄露
     */
    public void destroy() {
        // 释放掉播放器
        player.release();
        mHandler.removeCallbacksAndMessages(null);
    }

    private String getCurrentPlayingInfo(int currentTime, int maxTime) {
        String info = String.format("正在播放:  %s\t\t", currentSong.getSongName());
//        return String.format("%s %s / %s", info, ScanMusicUtils.formatTime(currentTime), ScanMusicUtils.formatTime(maxTime));
        return null ;
    }

    /**
     * 用于监听SeekBar进度值的改变
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }
    /**
     * 用于监听SeekBar开始拖动
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeMessages(MSG_CODE);
    }
    /**
     * 用于监听SeekBar停止拖动  SeekBar停止拖动后的事件
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        Log.i(TAG, "onStopTrackingTouch " + progress);
        // 得到该首歌曲最长秒数
        int musicMax = player.getDuration();
        // SeekBar最大值
        int seekBarMax = seekBar.getMax();
        //计算相对当前播放器歌曲的应播放时间
        float msec = progress / (seekBarMax * 1.0F) * musicMax;
        // 跳到该曲该秒
        player.seekTo((int) msec);
        mHandler.sendEmptyMessageDelayed(MSG_CODE, MSG_TIME);
    }
    public void setOnCompletionListener(OnCompletionListener onCompletionListener) {
        this.onCompletionListener = onCompletionListener;
    }
    interface OnCompletionListener {
        void onCompletion(MediaPlayer mediaPlayer) ;
    }

    static class MusicPlayerHelperHandler extends Handler {
        WeakReference<MusicPlayerHelper> weakReference ;
        public MusicPlayerHelperHandler(MusicPlayerHelper helper){
            super(Looper.getMainLooper()) ;
            this.weakReference = new WeakReference<>(helper) ;
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_CODE) {
                int pos = 0;
                //如果播放且进度条未被按压
                if (weakReference.get().player.isPlaying() && !weakReference.get().seekBar.isPressed()) {
                    int position = weakReference.get().player.getCurrentPosition();
                    int duration = weakReference.get().player.getDuration();
                    if (duration > 0) {
                        // 计算进度（获取进度条最大刻度*当前音乐播放位置 / 当前音乐时长）
                        pos = (int) (weakReference.get().seekBar.getMax() * position / (duration * 1.0f));
                    }
                    weakReference.get().text.setText(weakReference.get().getCurrentPlayingInfo(position, duration));
                }
                weakReference.get().seekBar.setProgress(pos);
                sendEmptyMessageDelayed(MSG_CODE, MSG_TIME);
            }
        }
    }
}
