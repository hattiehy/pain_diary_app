package com.example.ass3.repository;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.example.ass3.PainRecordDAO;
import com.example.ass3.database.PainRecordDatabase;
import com.example.ass3.entity.PainRecord;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class PainRecordRepository {
    private PainRecordDAO painRecordDAO;
    private LiveData<List<PainRecord>> allRecords;

    public PainRecordRepository(Application application) {
        PainRecordDatabase db = PainRecordDatabase.getInstance(application);
        painRecordDAO = db.PainRecordDAO();
        allRecords = painRecordDAO.getAll();
    }

    // Room executes this query on a separate thread
    public LiveData<List<PainRecord>> getAllRecords() {
        return allRecords;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<List<PainRecord>> getAllRec() {
        return CompletableFuture.supplyAsync(new Supplier<List<PainRecord>>() {

            @Override
            public List<PainRecord> get() {
                return painRecordDAO.getAllR();
            }
        }, PainRecordDatabase.databaseWriteExecutor);
    }

    public void insert(final PainRecord painRecord) {
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                painRecordDAO.insert(painRecord);
            }
        });
    }

    public void deleteAll() {
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                painRecordDAO.deleteAll();
            }
        });
    }

    public void delete(final PainRecord painRecord) {
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                painRecordDAO.delete(painRecord);
            }
        });
    }

    public void updateRecord(final PainRecord painRecord) {
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                painRecordDAO.updateRecord(painRecord);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<PainRecord> findByID(final int recordId) {
        return CompletableFuture.supplyAsync(new Supplier<PainRecord>() {
            @Override
            public PainRecord get() {
                return painRecordDAO.findByID(recordId);
            }
        }, PainRecordDatabase.databaseWriteExecutor);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<PainRecord> findByDate(final String RecordDate) {
        return CompletableFuture.supplyAsync(new Supplier<PainRecord>() {
            @Override
            public PainRecord get() {
                return painRecordDAO.findByDate(RecordDate);
            }
        }, PainRecordDatabase.databaseWriteExecutor);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<List<String>> getAllLoc() {
        return CompletableFuture.supplyAsync(new Supplier<List<String>>() {

            @Override
            public List<String> get() {
                return painRecordDAO.getPainLoc();
            }
        }, PainRecordDatabase.databaseWriteExecutor);
    }
}
