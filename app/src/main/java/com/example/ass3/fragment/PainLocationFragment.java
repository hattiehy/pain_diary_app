package com.example.ass3.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ass3.R;
import com.example.ass3.entity.PainRecord;
import com.example.ass3.recyclerView.RecyclerAdapter;
import com.example.ass3.viewmodel.PainRecordViewModel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class PainLocationFragment extends Fragment {

    private PieChart pcPainLocation;

    public PainLocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pain_location, container, false);
        pcPainLocation = view.findViewById(R.id.pc_pain_location);

        pcPainLocation.setUsePercentValues(true);
        pcPainLocation.getDescription().setEnabled(false);
        pcPainLocation.setExtraOffsets(5, 10, 5, 5);

        pcPainLocation.setDragDecelerationFrictionCoef(0.95f);

//        pcPainLocation.setCenterTextTypeface(tfLight);
        pcPainLocation.setCenterText(generateCenterSpannableText());

        pcPainLocation.setDrawHoleEnabled(false);
        pcPainLocation.setHoleColor(Color.WHITE);

        pcPainLocation.setTransparentCircleColor(Color.WHITE);
        pcPainLocation.setTransparentCircleAlpha(110);

        pcPainLocation.setHoleRadius(58f);
        pcPainLocation.setTransparentCircleRadius(61f);

        pcPainLocation.setDrawCenterText(true);

        pcPainLocation.setRotationAngle(0);
        // enable rotation of the pcPainLocation by touch
        pcPainLocation.setRotationEnabled(true);
        pcPainLocation.setHighlightPerTapEnabled(true);

        // add a selection listener
//        pcPainLocation.setOnpcPainLocationValueSelectedListener(this);

        pcPainLocation.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        Legend l = pcPainLocation.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        ArrayList<PieEntry> entries = new ArrayList<>();
        Map<String, Integer> location_freq = new HashMap<String, Integer>();

        PainRecordViewModel model = new ViewModelProvider(requireActivity()).get(PainRecordViewModel.class);
        try {
            List<String> allLocations = model.getAllLocations().get();
            for (String temp : allLocations) {
                Integer counts = location_freq.getOrDefault(temp, 1);
                location_freq.put(temp, counts + 1);
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (Map.Entry<String, Integer> entry : location_freq.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }
        PieDataSet dataSet = new PieDataSet(entries, "Pain Location");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        dataSet.setColor(Color.DKGRAY);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.DKGRAY);
        pcPainLocation.setDrawEntryLabels(true);
        pcPainLocation.setEntryLabelColor(Color.DKGRAY);
        pcPainLocation.setData(data);

        // undo all highlights
        pcPainLocation.highlightValues(null);

        pcPainLocation.invalidate();

        return view;
    }


    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Pain Location\npie chart for the pain location data");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 13, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 13, s.length(), 0);
        return s;
    }
}