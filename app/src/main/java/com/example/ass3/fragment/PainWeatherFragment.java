package com.example.ass3.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ass3.R;
import com.example.ass3.entity.PainRecord;
import com.example.ass3.viewmodel.PainRecordViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


public class PainWeatherFragment extends Fragment {

    private LineChart lcPainWeather;
    private EditText etStartDate;
    private EditText etEndDate;
    private Spinner spWeather;
    private Button bCreateChart, bAnalyse;
    private TextView tvResult;

    String startDate;
    String endDate;
    String weather;
    ArrayList<Double> weathers = new ArrayList<>();
    ArrayList<Double> painNums = new ArrayList<>();

    public PainWeatherFragment() {
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
        View view = inflater.inflate(R.layout.fragment_pain_weather, container, false);
        lcPainWeather = view.findViewById(R.id.lc_pain_weather);
        etStartDate = view.findViewById(R.id.et_start_date);
        etEndDate = view.findViewById(R.id.et_end_date);
        spWeather = view.findViewById(R.id.sp_weather_choose);
        bCreateChart = view.findViewById(R.id.btn_create_chart);
        bAnalyse = view.findViewById(R.id.btn_analyse);
        tvResult = view.findViewById(R.id.tv_analyse_result);



        Calendar c = Calendar.getInstance();
        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePacket = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String select_month = String.valueOf(month + 1);
                                String select_day = String.valueOf(dayOfMonth);
                                if ((month + 1) < 10) {
                                    select_month = "0" + (month + 1);
                                }
                                if (dayOfMonth < 10) {
                                    select_day = "0" + dayOfMonth;
                                }
                                etStartDate.setText(select_day + "-" + select_month + "-" + year);
                                startDate = select_day + "-" + select_month + "-" + year;
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                datePacket.show();
            }
        });

        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePacket = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String select_month = String.valueOf(month + 1);
                                String select_day = String.valueOf(dayOfMonth);
                                if ((month + 1) < 10) {
                                    select_month = "0" + (month + 1);
                                }
                                if (dayOfMonth < 10) {
                                    select_day = "0" + dayOfMonth;
                                }
                                etEndDate.setText(select_day + "-" + select_month + "-" + year);
                                endDate = select_day + "-" + select_month + "-" + year;
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                datePacket.show();
            }
        });

        spWeather.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                weather = getResources().getStringArray(R.array.weatherArray)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        bCreateChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isEmpty(startDate, endDate)) {
                    Toast.makeText(getContext(), "Start date and End date can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd-MM-yyyy")).isAfter(LocalDate.parse(endDate, DateTimeFormatter.ofPattern("dd-MM-yyyy")))) {
                    Toast.makeText(getContext(), "End date should after start date ", Toast.LENGTH_SHORT).show();
                    return;
                }

                setChart();
                bAnalyse.setEnabled(true);
            }

        });

        bAnalyse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (painNums.size() <= 2 || weathers.size() <= 2){
                    Toast.makeText(getContext(), "Insufficient sample data", Toast.LENGTH_SHORT).show();
                    return;
                }
                String testResult = testCorrelation(painNums, weathers);
                tvResult.setText(testResult);
            }
        });

        return view;
    }

    public boolean isEmpty(String startDate, String endDate) {
        if (startDate != null && endDate != null) {
            return false;
        } else
            return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setChart() {
        lcPainWeather.setDrawBorders(true);

        Legend legend = lcPainWeather.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setFormSize(8f);
        legend.setTextColor(getContext().getApplicationContext().getResources().getColor(R.color.mapbox_blue));
        legend.setTextSize(12f);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

        lcPainWeather.animateX(2500);
        lcPainWeather.setTouchEnabled(true);
        lcPainWeather.setDragEnabled(true);
        lcPainWeather.setScaleEnabled(true);
        lcPainWeather.invalidate();

        ArrayList<Entry> painValues = new ArrayList<>();
        ArrayList<Entry> weatherValues = new ArrayList<>();
        //X-Axis
        final ArrayList<String> xLabel = new ArrayList<>();

        PainRecordViewModel model = new ViewModelProvider(requireActivity()).get(PainRecordViewModel.class);
        try {
            List<PainRecord> allRecords = model.getAllRecs().get();
            allRecords.sort(Comparator.comparing(record -> LocalDate.parse(record.recordDate, DateTimeFormatter.ofPattern("dd-MM-yyyy")), Comparator.naturalOrder()));
            LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd-MM-yyyy")).minusDays(1);
            LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("dd-MM-yyyy")).plusDays(1);

            for (PainRecord record : allRecords) {
                LocalDate date = LocalDate.parse(record.recordDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                if (date.isAfter(start) && date.isBefore(end)) {
                    float x = date.toEpochDay() - start.toEpochDay();
                    painValues.add(new Entry(x, record.painLevel));
                    painNums.add(Integer.valueOf(record.painLevel).doubleValue());
                    xLabel.add(record.recordDate);
                }
            }

            for (PainRecord record : allRecords) {
                LocalDate date = LocalDate.parse(record.recordDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                if (date.isAfter(start) && date.isBefore(end)) {
                    float x = date.toEpochDay() - start.toEpochDay();
                    if (weather.equals("temperature")) {
                        weatherValues.add(new Entry(x, (float) record.temperature));
                        weathers.add(record.temperature);
                    } else if (weather.equals("humidity")) {
                        weatherValues.add(new Entry(x, (float) record.humidity));
                        weathers.add(record.humidity);
                    } else {
                        weatherValues.add(new Entry(x, (float) record.pressure));
                        weathers.add(record.pressure);
                    }
                }
            }


        } catch (
                ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LineDataSet set1, set2;
        set1 = new LineDataSet(painValues, "Pain Level");
        set2 = new LineDataSet(weatherValues, "Weather");

        // create a data object with the data sets
        LineData data = new LineData(set1, set2);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setCircleColor(Color.DKGRAY);
        set1.setLineWidth(2f);
        set1.setCircleRadius(3f);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);

        set2.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set2.setColor(Color.RED);
        set2.setCircleColor(Color.DKGRAY);
        set2.setLineWidth(2f);
        set2.setCircleRadius(3f);
        set2.setFillAlpha(65);
        set2.setFillColor(Color.RED);
        set2.setDrawCircleHole(false);
        set2.setHighLightColor(Color.rgb(244, 117, 117));

        data.setValueTextColor(Color.DKGRAY);
        data.setValueTextSize(9f);

        XAxis xAxis = lcPainWeather.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabel));
        xAxis.setTextColor(Color.parseColor("#333333"));
        xAxis.setTextSize(11f);
        xAxis.setAxisMinimum(0f);
        xAxis.setLabelRotationAngle(-60);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        // set data
        lcPainWeather.setData(data);
        lcPainWeather.invalidate();
    }

    public String testCorrelation(ArrayList<Double> firstArray, ArrayList<Double> secondArray) {
        // two column array: 1st column=first array, 1st column=second array
        double data[][] = new double[firstArray.size()][2];
        for (int i = 0; i < firstArray.size(); i++) {
            data[i][0] = firstArray.get(i);
            data[i][1] = secondArray.get(i);
        }

        // create a realmatrix
        RealMatrix m = MatrixUtils.createRealMatrix(data);
        // measure all correlation test: x-x, x-y, y-x, y-x
        for (int i = 0; i < m.getColumnDimension(); i++)
            for (int j = 0; j < m.getColumnDimension(); j++) {
                PearsonsCorrelation pc = new PearsonsCorrelation();
                double cor = pc.correlation(m.getColumn(i), m.getColumn(j));
                System.out.println(i + "," + j + "=[" + String.format(".%2f", cor) + "," + "]");
            }
        // correlation test (another method): x-y
        PearsonsCorrelation pc = new PearsonsCorrelation(m);
        RealMatrix corM = pc.getCorrelationMatrix();
        // significant test of the correlation coefficient (p-value)
        RealMatrix pM = pc.getCorrelationPValues();
        return ("p value:" + pM.getEntry(0, 1) + "\n" + "correlation: " + corM.getEntry(0, 1));
    }

}
