package com.example.bluechatapp.MusicActivity;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.bluechatapp.Bean.Song;

import java.util.ArrayList;
import java.util.List;

public class MusicUtils {
    private static List<Song> musicList ;
    /**
     * 扫描系统里面的音频
     */
    public static List<Song> scanSysMusic(Context context){
        musicList = new ArrayList<>() ;
        // 媒体库查询语句
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,MediaStore.Audio.Media.IS_MUSIC) ;
        if (cursor!=null){
            while (cursor.moveToNext()){
                Song song = new Song() ;
                song.setSongName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))) ;
                song.setSinger(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                //歌曲路径
                song.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                //歌曲时长
                song.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                //歌曲大小
                song.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                if (song.size > 1000 * 800) {
                    // 注释部分是切割标题，分离出歌曲名和歌手 （本地媒体库读取的歌曲信息不规范）
                    if (song.getSongName().contains("-")) {
                        String[] str = song.getSongName().split("-") ;
                        song.setSinger(str[0]) ;
                        song.setSongName(str[1]) ;
                    }
                    musicList.add(song);
                }
            }

            cursor.close();
        }
        saveMusicListInLitePal();
        return musicList ;
    }
    // 存数据到LitePal数据库中
    private static void saveMusicListInLitePal(){
        for (Song i:musicList){
            i.save() ;
        }
    }
}
