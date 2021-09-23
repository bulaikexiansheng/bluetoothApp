package com.example.bluechatapp.MusicActivity;

import com.example.bluechatapp.R;

import java.util.Random;

public class SongPic {
    static public int []drawing = {
            R.drawable.singer_bg1 ,R.drawable.singer_bg2 ,R.drawable.singer_bg3 ,
            R.drawable.singer_bg4 ,R.drawable.singer_bg5 ,R.drawable.singer_bg6 ,
            R.drawable.singer_bg7
    } ;

    public static int getDrawing() {
        return drawing[new Random().nextInt(7)];
    }
}
