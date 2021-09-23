package com.example.bluechatapp.MusicActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluechatapp.Bean.Song;
import com.example.bluechatapp.R;
import com.example.bluechatapp.Static.Utils.ToastUtil;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.List;

public class MusicRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Song> musicList ;
    private Context mContext ;

    public void setMusicList(List<Song> musicList) {
        this.musicList = musicList;
    }

    public MusicRVAdapter(Context mContext, List<Song> musicList) {
        this.musicList = musicList;
        this.mContext = mContext;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new MusicViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_musicrv_item,parent,false)) ;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        if(musicList!=null&&musicList.size()!=0){
            ((MusicViewHolder)holder).songOrder.setText(""+(position+1)); // 设置序号
            ((MusicViewHolder)holder).songName.setText(musicList.get(position).getSongName()); // 设置歌曲名
            ((MusicViewHolder)holder).singerName.setText(musicList.get(position).getSinger()); // 设置歌手
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMusicActivity = new Intent(mContext,MusicPlayingActivity.class) ;
                toMusicActivity.putExtra("SongList",(Serializable) musicList) ;
                toMusicActivity.putExtra("Selected",position) ;
                mContext.startActivity(toMusicActivity);
            }
        });
    }
    @Override
    public int getItemCount() {
        return musicList.size();
    }
    class MusicViewHolder extends RecyclerView.ViewHolder{
        private TextView songName ;
        private TextView songOrder ;
        private TextView singerName ;
        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.song_name_tv) ;
            songOrder = itemView.findViewById(R.id.music_order_tv) ;
            singerName = itemView.findViewById(R.id.singer_name_tv) ;
        }
    }
}
