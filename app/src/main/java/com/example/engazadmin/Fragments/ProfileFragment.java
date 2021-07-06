package com.example.engazadmin.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.engazadmin.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private View view;
    private DatabaseReference historyRef;

    HashMap<String, Integer> maps1 = new HashMap<>();
    HashMap<String, Integer> maps2 = new HashMap<>();
    HashMap<String, Integer> maps3 = new HashMap<>();


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_profile, container, false);
        historyRef = FirebaseDatabase.getInstance().getReference().child("History").child("-MaJicZemWvOSOqMFGzC").child("-MaJixbqxV0VXC8dVytS");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        historyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                maps1 = (HashMap<String, Integer>) snapshot.child("-MaUDA69DstJq8JSEj8L").getValue();
                maps2 = (HashMap<String, Integer>) snapshot.child("-MaYqTUxumaskiqmf8-u").getValue();
                maps3 = (HashMap<String, Integer>) snapshot.child("-MdZd4XB6LoxDsDJzE0E").getValue();

                System.out.println("thype hash " + maps1);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    System.out.println("thype " + dataSnapshot);
                    dataSnapshot.child("-MaUDA69DstJq8JSEj8L");
                }
                String type = snapshot.getValue().toString();
                //System.out.println("thype " + type);
                drawChart();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    private void drawChart() {
        BarChart barChart = view.findViewById(R.id.barChart);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        Description description = new Description();
        description.setText("");
        barChart.setDescription(description);
        barChart.setMaxVisibleValueCount(30);
        barChart.setVisibleXRangeMinimum(1);
        barChart.getXAxis().setAxisMaximum(30);
        barChart.getXAxis().setAxisMinimum(1);
        barChart.setPinchZoom(true);
        barChart.setDrawGridBackground(false);

        XAxis xl = barChart.getXAxis();
        xl.setGranularity(1f);
        xl.setCenterAxisLabels(true);
        barChart.setVisibleXRange(1, 10);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(30f);
        leftAxis.setAxisMinimum(1);
        barChart.getAxisRight().setEnabled(false);

        //data
        float groupSpace = 0.5f;
        float barSpace = 0.08f;
        int startDay = 1;
        int endDay = 30;

        List<BarEntry> yVals1 = new ArrayList<BarEntry>();
        List<BarEntry> yVals2 = new ArrayList<BarEntry>();
        List<BarEntry> yVals3 = new ArrayList<BarEntry>();

        for (int i = startDay; i < endDay; i++) {
            for (Map.Entry<String, Integer> entry : maps1.entrySet()) {
                String date = entry.getKey();
                String[] arrOfStr = date.split("-");
                //System.out.println("day yoo"+arrOfStr);
                int value = ((Number) entry.getValue()).intValue();
                int day = Integer.parseInt(arrOfStr[0]);

                if (i == day) {
                    System.out.println("day yoo" + day);
                    yVals1.add(new BarEntry(i, value));
                }
            }
            //yVals1.add(new BarEntry(i, 10));
        }

        for (int i = startDay; i < endDay; i++) {
            for (Map.Entry<String, Integer> entry : maps2.entrySet()) {
                String date = entry.getKey();
                String[] arrOfStr = date.split("-");
                //System.out.println("day yoo"+arrOfStr);
                int value = ((Number) entry.getValue()).intValue();
                int day = Integer.parseInt(arrOfStr[0]);
                //System.out.println("day yoo"+day);

                if (i == day) {
                    System.out.println("day yoo" + day);
                    yVals2.add(new BarEntry(i, value));
                }
            }
        }
        for (int i = startDay; i < endDay; i++) {
            for (Map.Entry<String, Integer> entry : maps3.entrySet()) {
                String date = entry.getKey();
                String[] arrOfStr = date.split("-");
                //System.out.println("day yoo"+arrOfStr);
                int value = ((Number) entry.getValue()).intValue();
                int day = Integer.parseInt(arrOfStr[0]);
                //System.out.println("day yoo"+day);

                if (i == day) {
                    System.out.println("day yoo" + day);
                    yVals3.add(new BarEntry(i, value));
                }
            }
        }
        BarDataSet set1, set2, set3;

        if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) barChart.getData().getDataSetByIndex(1);
            set3 = (BarDataSet) barChart.getData().getDataSetByIndex(2);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            set3.setValues(yVals3);

            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "بدل فاقد");
            set1.setColor(Color.rgb(104, 241, 175));
            set2 = new BarDataSet(yVals2, "تجديد رخصه");
            set2.setColor(Color.rgb(164, 228, 251));
            set3 = new BarDataSet(yVals3, "اصدار ملصق");
            set3.setColor(Color.rgb(220, 120, 120));


            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            dataSets.add(set2);
            dataSets.add(set3);

            BarData data = new BarData(dataSets);
            data.setBarWidth(0.9f);
            barChart.setData(data);
            YAxis yAxisLeft = barChart.getAxisLeft();
            YAxis yAxisRight = barChart.getAxisRight();
            yAxisRight.setEnabled(false);
            barChart.setVisibleYRange(1, 30, YAxis.AxisDependency.LEFT);

            barChart.getDescription().setEnabled(false);
            barChart.animateY(1500);
            barChart.animateX(1500);
            yAxisLeft.setAxisMaximum(30);

        }

        barChart.getBarData();
        barChart.groupBars(startDay, groupSpace, barSpace);
        barChart.invalidate();

    }
}
