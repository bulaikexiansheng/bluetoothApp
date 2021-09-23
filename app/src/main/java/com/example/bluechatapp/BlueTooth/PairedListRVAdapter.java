package com.example.bluechatapp.BlueTooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluechatapp.MainActivity.MainActivity;
import com.example.bluechatapp.R;
import com.example.bluechatapp.Static.Utils.ToastUtil;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PairedListRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    /**
     * Return Intent extra
     */
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    // 上下文
    private Context mContext ;
    private List<BluetoothDevice> pairedDevicesList ;
    private OnItemClickListener clickListener ;
    private BuildConnectionListener listener;

    public PairedListRVAdapter(Context mContext, OnItemClickListener onItemClickListener, BuildConnectionListener listener, Set<BluetoothDevice> pairedSet) {
        this.mContext = mContext;
        this.clickListener = onItemClickListener ;
        this.listener = listener ;
        this.pairedDevicesList = new ArrayList<>(pairedSet) ;
    }

    public void setPairedDevicesList(Set<BluetoothDevice> pairedDevicesList) {
        this.pairedDevicesList = new ArrayList<>(pairedDevicesList) ;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        return new PairedViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_devicelist_item,parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull RecyclerView.ViewHolder holder, int position) {
        if (pairedDevicesList.size()>=0){
            ((PairedViewHolder)holder).bluetoothAddress.setText(pairedDevicesList.get(position).getAddress());
            ((PairedViewHolder)holder).deviceName.setText(pairedDevicesList.get(position).getName());
        }
        // 子项点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获得蓝牙设备的信息，以及地址信息
                String info = ((PairedViewHolder)holder).deviceName.getText().toString();
                String address = ((PairedViewHolder)holder).bluetoothAddress.getText().toString();
                clickListener.onClick(position);
                listener.build(pairedDevicesList.get(position));
            }
        });
    }
    @Override
    public int getItemCount() {
        return pairedDevicesList.size();
    }
    class PairedViewHolder extends RecyclerView.ViewHolder{
        private TextView deviceName ;
        private TextView bluetoothAddress ;
        public PairedViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.device_name_tv) ;
            bluetoothAddress = itemView.findViewById(R.id.blueTooth_id_tv) ;
        }
    }
    public interface OnItemClickListener {
        void onClick(int pos) ;
    }
    public interface BuildConnectionListener{
        public void build(BluetoothDevice device) ;
    }

}
