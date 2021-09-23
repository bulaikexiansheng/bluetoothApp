package com.example.bluechatapp.MusicActivity;

import com.example.bluechatapp.R;

import java.util.Random;

public class MusicBackGround {
    static public int []drawing = {
            R.drawable.music_playing_bg1 ,R.drawable.music_playing_bg2 ,R.drawable.music_playing_bg3 ,
            R.drawable.music_playing_bg4 ,R.drawable.music_playing_bg5 ,R.drawable.music_playing_bg6 ,
            R.drawable.music_playing_bg7
    } ;

    public static int getDrawing() {
        return drawing[new Random().nextInt(drawing.length)];
    }
}
