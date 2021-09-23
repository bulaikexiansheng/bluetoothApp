package com.example.bluechatapp.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bluechatapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LineChartFragment extends Fragment implements View.OnClickListener {
    private LineChart lineChart ;
    // 当前fragment是否被展示
    private static boolean state ;
    // 展示的是什么指标
    private int flag = 0 ;
    public static final int G = 0 ;
    public static final int B = 1 ;
    public static final int A = 2 ;
    // x,y,z是否展示
    private Button displayXBtn ;
    private Button displayYBtn ;
    private Button displayZBtn ;
    private Boolean X_DISPLAY = true ;
    private Boolean Y_DISPLAY = true ;
    private Boolean Z_DISPLAY = true ;
    // x,y,z三个数据的list
    private static List<Entry> xEntries ;
    private static List<Entry> yEntries ;
    private static List<Entry> zEntries ;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        state = true ;
        View view = inflater.inflate(R.layout.fragment_linechart,container,false) ;
        lineChart = view.findViewById(R.id.lineChart) ;
        createLineChart() ;
        displayXBtn = view.findViewById(R.id.chart_xdisplay_btn) ;
        displayYBtn = view.findViewById(R.id.chart_ydisplay_btn) ;
        displayZBtn = view.findViewById(R.id.chart_zdisplay_btn) ;
        registerClickEvent() ;
        return view;
    }
    // 设置fragment的展出状态
    public static boolean getState(){
        return state ;
    }

    public static void setState(boolean state) {
        LineChartFragment.state = state;
    }

    private void registerClickEvent() {
        displayXBtn.setOnClickListener(this);
        displayYBtn.setOnClickListener(this);
        displayZBtn.setOnClickListener(this);
    }

    public static LineChartFragment newInstance(int flag) {

        Bundle args = new Bundle();

        LineChartFragment fragment = new LineChartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void createLineChart() {
        // 数据初始化
        xEntries = new ArrayList<>() ;
        yEntries = new ArrayList<>() ;
        zEntries = new ArrayList<>() ;
//        xEntries.add(new Entry((float) 3.0,(float)2.0));
//        xEntries.add(new Entry((float) 1.0,(float)7.0));
//        xEntries.add(new Entry((float) 2.0,(float)9.0));
//        yEntries.add(new Entry((float) 3.0,(float)2.0));
//        yEntries.add(new Entry((float) 1.0,(float)7.0));
//        yEntries.add(new Entry((float) 2.0,(float)9.0));
//        zEntries.add(new Entry((float) 3.0,(float)2.0));
//        zEntries.add(new Entry((float) 1.0,(float)7.0));
//        zEntries.add(new Entry((float) 2.0,(float)9.0));
        Legend mLegend = lineChart.getLegend() ;
        mLegend.setEnabled(true);
        mLegend.setForm(Legend.LegendForm.CIRCLE);
        xEntries.add(new Entry(0, (float) 1.0)) ;
        yEntries.add(new Entry(0, (float) 2.0)) ;
        zEntries.add(new Entry(0, (float) 2.0)) ;
        LineDataSet xDataSet = new LineDataSet(xEntries,"GX");
        LineDataSet yDataSet = new LineDataSet(yEntries,"GY");
        LineDataSet zDataSet = new LineDataSet(zEntries,"GZ");
        xDataSet.setLineWidth(3.0f);
        xDataSet.setColor(Color.RED);
        xDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        yDataSet.setLineWidth(3.0f);
        yDataSet.setColor(Color.BLUE);
        yDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        zDataSet.setLineWidth(3.0f);
        zDataSet.setColor(Color.GREEN);
        zDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        List<ILineDataSet> dataSets = new ArrayList<>() ;
        dataSets.add(xDataSet) ;
        dataSets.add(yDataSet) ;
        dataSets.add(zDataSet) ;
        LineData lineData = new LineData(dataSets) ;
        YAxis yAxis = lineChart.getAxisLeft() ;
        yAxis.setStartAtZero(false);
        yAxis.setAxisMinValue(-300);
        yAxis.setAxisMaxValue(300);
        lineChart.setData(lineData);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
//        lineChart.clear();
        lineChart.invalidate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chart_xdisplay_btn:{
                break ;
            }
            case R.id.chart_ydisplay_btn:{
                break ;
            }
            case R.id.chart_zdisplay_btn:{
                break ;
            }
            default:
                break ;
        }
    }
}
