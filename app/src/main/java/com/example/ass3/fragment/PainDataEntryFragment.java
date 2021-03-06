package com.example.ass3.fragment;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ass3.R;
import com.example.ass3.entity.PainRecord;
import com.example.ass3.notification.AlertReceiver;
import com.example.ass3.viewmodel.PainRecordViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;


public class PainDataEntryFragment extends Fragment {

    private FirebaseAuth mAuth;
    private com.jaygoo.widget.RangeSeekBar sbPainLevel;
    private Spinner spPainLocation;
    private com.jaygoo.widget.RangeSeekBar sbMood;
    private EditText etEnterSteps;
    private EditText etEnterGoal;
    private Button bEdit;
    private Button bSave, bSetAlert;
    private float level;
    private float mood;
    private String location;

    public PainDataEntryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pain_data_entry, container, false);

        PainRecordViewModel model = new ViewModelProvider(requireActivity()).get(PainRecordViewModel.class);
        mAuth = FirebaseAuth.getInstance();

        sbPainLevel = view.findViewById(R.id.sb_pain_level);
        spPainLocation = view.findViewById(R.id.sp_pain_location);
        sbMood = view.findViewById(R.id.sb_mood);
        etEnterSteps = view.findViewById(R.id.et_step_number);
        etEnterGoal = view.findViewById(R.id.et_step_goal);
        bEdit = view.findViewById(R.id.btn_edit);
        bSave = view.findViewById(R.id.btn_save);
        bSetAlert = view.findViewById(R.id.btn_set_alert);


        sbPainLevel.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                //leftValue is left seekbar value, rightValue is right seekbar value
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
                //start tracking touch
            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
                //stop tracking touch
                level = view.getLeftSeekBar().getProgress();
            }
        });

        sbMood.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                //leftValue is left seekbar value, rightValue is right seekbar value
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
                //start tracking touch
            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
                //stop tracking touch
                mood = view.getLeftSeekBar().getProgress();
            }
        });


        spPainLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                location = getResources().getStringArray(R.array.painLocationArray)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        bSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Double temp = model.getTemperature().getValue();
                Double humidity = model.getHumidity().getValue();
                Double pressure = model.getPressure().getValue();
                String username = mAuth.getCurrentUser().getEmail();

                String recordDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

                Integer painLevel = Integer.parseInt(String.valueOf(level).substring(0, 1));
                Integer moodLevel = Integer.parseInt(String.valueOf(mood).substring(0, 1));
                if (StringUtils.isBlank(etEnterSteps.getText())) {
                    Toast.makeText(getContext(), "Not a valid step number", Toast.LENGTH_SHORT).show();
                    return;
                }
                Integer step = Integer.parseInt(etEnterSteps.getText().toString().trim());

                if (StringUtils.isBlank(etEnterGoal.getText())) {
                    etEnterGoal.setText("10000");
                }
                Integer stepGoal = Integer.parseInt(etEnterGoal.getText().toString().trim());
                SharedPreferences sharedPref = requireActivity().getSharedPreferences("step_goal", getContext().MODE_PRIVATE);
                SharedPreferences.Editor spEditor = sharedPref.edit();
                spEditor.putInt("step_goal", stepGoal);
                spEditor.apply();


                PainRecord painRecord = new PainRecord();
                painRecord.setUsername(username);
                painRecord.setTemperature(temp);
                painRecord.setPressure(pressure);
                painRecord.setHumidity(humidity);
                painRecord.setDate(recordDate);
                painRecord.setPainLevel(painLevel);
                painRecord.setMoodLevel(moodLevel);
                painRecord.setStepTaken(step);
                painRecord.setPainLocation(location);

                sbPainLevel.setEnabled(false);
                spPainLocation.setEnabled(false);
                sbMood.setEnabled(false);
                etEnterSteps.setEnabled(false);
                etEnterGoal.setEnabled(false);
                bSave.setEnabled(false);

//                Random r = new Random();
//                PainRecord pr = new PainRecord();
//                pr.setPainLocation(location);
//                pr.setUsername(username);
//                pr.setTemperature(5 + r.nextInt(1700)/100);
//                pr.setPressure(900 + r.nextInt(2000)/100);
//                pr.setHumidity(r.nextInt(10000)/100);
//                pr.setPainLevel(r.nextInt(10));
//                pr.setMoodLevel(r.nextInt(4));
//                pr.setStepTaken(r.nextInt(8000));
//                pr.setDate( "10-05-2021");
//                model.insert(pr);

                try {
                    PainRecord painRecord_old = model.findByDate(recordDate).get();
                    if (painRecord_old == null) {
                        model.insert(painRecord);
                        Toast.makeText(getContext(), "Successfully saved record", Toast.LENGTH_SHORT).show();
                    } else {
                        painRecord_old.setTemperature(temp);
                        painRecord_old.setPressure(pressure);
                        painRecord_old.setHumidity(humidity);
                        painRecord_old.setDate(recordDate);
                        painRecord_old.setPainLevel(painLevel);
                        painRecord_old.setMoodLevel(moodLevel);
                        painRecord_old.setStepTaken(step);
                        painRecord_old.setPainLocation(location);
                        model.update(painRecord_old);
                        Toast.makeText(getContext(), "Successfully update record", Toast.LENGTH_SHORT).show();
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        bEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sbPainLevel.setEnabled(true);
                spPainLocation.setEnabled(true);
                sbMood.setEnabled(true);
                etEnterSteps.setEnabled(true);
                etEnterGoal.setEnabled(true);
                bSave.setEnabled(true);
            }
        });

        Calendar c = Calendar.getInstance();
        bSetAlert.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, (view1, hourOfDay, minute) -> {
                String select_hour = String.valueOf(hourOfDay);
                String select_minute = String.valueOf(minute);
                if (hourOfDay < 10) {
                    select_hour = "0" + hourOfDay;
                }
                if (minute < 10) {
                    select_minute = "0" + minute;
                }
                Toast.makeText(getContext(), "Alert time has been set at: " + select_hour + ":" + select_minute + "", Toast.LENGTH_SHORT).show();

                AlarmManager alarmManager = (AlarmManager) getContext().getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getContext().getApplicationContext(), AlertReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext().getApplicationContext(), 1, intent, 0);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() - 120 * 1000, 86400*60000, pendingIntent);
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
            timePickerDialog.show();
        });

        return view;
    }
}