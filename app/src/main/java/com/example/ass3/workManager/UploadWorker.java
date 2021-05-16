package com.example.ass3.workManager;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.ass3.PainRecordDAO;
import com.example.ass3.activity.MainActivity;
import com.example.ass3.database.PainRecordDatabase;
import com.example.ass3.entity.PainRecord;
import com.example.ass3.repository.PainRecordRepository;
import com.example.ass3.viewmodel.PainRecordViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UploadWorker extends Worker {


    public UploadWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        Context context = getApplicationContext();
        PainRecordDatabase db = PainRecordDatabase.getInstance(context);
        PainRecordDAO dao = db.PainRecordDAO();
        PainRecord painRecord = dao.findByDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        FirebaseFirestore fireDB = FirebaseFirestore.getInstance();

        if (painRecord == null) {
            Log.i("WORK_MANAGER", "There is no new record");
            return Result.success();
        }

        fireDB.collection("PainRecords").document(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .set(painRecord)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("WORK_MANAGER", "DocumentSnapshot successfully written!");
                    }
                });
        return Result.success();
    }
}
