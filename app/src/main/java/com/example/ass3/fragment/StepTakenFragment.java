package com.example.ass3.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ass3.R;
import com.example.ass3.entity.PainRecord;
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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

public class StepTakenFragment extends Fragment {

    private PieChart pcStepTaken;

    public StepTakenFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_taken, container, false);
        pcStepTaken = view.findViewById(R.id.pc_step_taken);

        pcStepTaken.setUsePercentValues(false);
        pcStepTaken.getDescription().setEnabled(false);
        pcStepTaken.setExtraOffsets(5, 10, 5, 5);

        pcStepTaken.setDragDecelerationFrictionCoef(0.95f);

//        pcStepTaken.setCenterTextTypeface(tfLight);
        pcStepTaken.setCenterText(generateCenterSpannableText());

        pcStepTaken.setDrawHoleEnabled(true);
        pcStepTaken.setHoleColor(Color.WHITE);

        pcStepTaken.setTransparentCircleColor(Color.WHITE);
        pcStepTaken.setTransparentCircleAlpha(110);

        pcStepTaken.setHoleRadius(58f);
        pcStepTaken.setTransparentCircleRadius(61f);

        pcStepTaken.setDrawCenterText(true);

        pcStepTaken.setRotationAngle(0);
        // enable rotation of the pcStepTaken by touch
        pcStepTaken.setRotationEnabled(true);
        pcStepTaken.setHighlightPerTapEnabled(true);

        // add a selection listener
//        pcStepTaken.setOnpcStepTakenValueSelectedListener(this);

        pcStepTaken.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        Legend l = pcStepTaken.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        ArrayList<PieEntry> entries = new ArrayList<>();
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("step_goal", Context.MODE_PRIVATE);
        Integer currentGoal = sharedPref.getInt("step_goal", 10000);
        Integer currentStep = 0;
        Integer stepRemaining = currentGoal;
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        PainRecordViewModel model = new ViewModelProvider(requireActivity()).get(PainRecordViewModel.class);
        try {
            PainRecord curPainRecord = model.findByDate(currentDate).get();
            if (curPainRecord != null) {
                currentStep = curPainRecord.stepTaken;
                stepRemaining = currentGoal - currentStep;
            }
        } catch (
                ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        entries.add(new PieEntry(currentStep, "Current Steps"));
        entries.add(new PieEntry(stepRemaining, "Remaining Steps"));


        PieDataSet dataSet = new PieDataSet(entries, "Current step taken");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

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
        pcStepTaken.setDrawEntryLabels(true);
        pcStepTaken.setEntryLabelColor(Color.DKGRAY);
        pcStepTaken.setData(data);

        // undo all highlights
        pcStepTaken.highlightValues(null);

        pcStepTaken.invalidate();

        return view;
    }


    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Step Taken\nThe current date and the remaining steps");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 10, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 10, s.length(), 0);
        return s;
    }
}