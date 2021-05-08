package com.example.ass3.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ass3.entity.PainRecord;
import com.example.ass3.repository.PainRecordRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PainRecordViewModel extends AndroidViewModel {
    private PainRecordRepository pRepository;
    private MutableLiveData<String> username;
    private MutableLiveData<Double> temperature;
    private MutableLiveData<Double> humidity;
    private MutableLiveData<Double> pressure;
    private LiveData<List<PainRecord>> allRecords;

    public PainRecordViewModel(Application application) {
        super(application);
        pRepository = new PainRecordRepository(application);
        allRecords = pRepository.getAllRecords();
        username = new MutableLiveData();
        temperature = new MutableLiveData();
        humidity = new MutableLiveData();
        pressure = new MutableLiveData();
    }

    public LiveData<String> getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username.setValue(username);
    }

    public LiveData<Double> getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature.setValue(temperature);
    }

    public LiveData<Double> getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity.setValue(humidity);
    }

    public LiveData<Double> getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure.setValue(pressure);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<PainRecord> findByID(final int recordId) {
        return pRepository.findByID(recordId);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<PainRecord> findByDate(final String recordDate) {
        return pRepository.findByDate(recordDate);
    }

    public LiveData<List<PainRecord>> getAllRecords() {
        return allRecords;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<List<PainRecord>> getAllRecs() {
        return pRepository.getAllRec();
    }

    public void insert(PainRecord painRecord) {
        pRepository.insert(painRecord);
    }

    public void deleteAll() {
        pRepository.deleteAll();
    }

    public void update(PainRecord painRecord) {
        pRepository.updateRecord(painRecord);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<List<String>> getAllLocations() {
        return pRepository.getAllLoc();
    }
}
