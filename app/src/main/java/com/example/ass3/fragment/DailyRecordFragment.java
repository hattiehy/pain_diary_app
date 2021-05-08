package com.example.ass3.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ass3.R;
import com.example.ass3.entity.PainRecord;
import com.example.ass3.recyclerView.RecyclerAdapter;
import com.example.ass3.viewmodel.PainRecordViewModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DailyRecordFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_record, container, false);

        PainRecordViewModel model = new ViewModelProvider(requireActivity()).get(PainRecordViewModel.class);
        try {
            List<PainRecord> allRecord = model.getAllRecs().get();
            recyclerView = view.findViewById(R.id.rev_record);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerAdapter = new RecyclerAdapter(getContext(), allRecord);
            recyclerView.setAdapter(recyclerAdapter);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return view;
    }
}