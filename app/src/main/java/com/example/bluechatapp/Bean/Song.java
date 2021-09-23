package com.example.bluechatapp.Bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class Song extends LitePalSupport implements Serializable{
    /**
     * 歌曲名
     */
    private String songName ;
    /**
     * 歌手
     */
    private String singer ;
    /**
     * 歌曲路径
     */
    private String path ;
    /**
     * 歌曲的长度
     */
    private int duration ;
    /**
     * 歌曲大小
     */
    public long size ;

    /**
     *  是否选中
     */
    public boolean isChecked ;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
