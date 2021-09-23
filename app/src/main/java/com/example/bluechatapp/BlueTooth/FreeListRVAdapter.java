package com.example.bluechatapp.BlueTooth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluechatapp.R;
import com.example.bluechatapp.Static.Utils.ToastUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FreeListRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    // 上下文
    private Context mContext ;
    private List<BlueToothDevice> freeDevicesList ;
    private OnItemClickListener onItemClickListener ;
    public FreeListRVAdapter(Context mContext,OnItemClickListener listener) {
        this.mContext = mContext;
        this.onItemClickListener = listener ;
    }

    public void setPairedDevicesList(List<BlueToothDevice> pairedDevicesList) {
        this.freeDevicesList = pairedDevicesList;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new FreeViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_devicelist_item,parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        // 子项点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClick(position);

            }
        });
    }
    @Override
    public int getItemCount() {
        return 10;
    }
    class FreeViewHolder extends RecyclerView.ViewHolder{
        private TextView deviceName ;
        private TextView bluetoothId ;
        public FreeViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.device_name_tv) ;
            bluetoothId = itemView.findViewById(R.id.blueTooth_id_tv) ;
        }
    }
    public interface OnItemClickListener{
        void onClick(int pos) ;
    }
}
