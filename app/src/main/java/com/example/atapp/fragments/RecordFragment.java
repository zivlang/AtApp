package com.example.atapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.atapp.R;
import com.example.atapp.RecordAdapter;
import com.example.atapp.model.Worker;
import com.example.atapp.sqliteAccess.GetDatabase;

import java.util.ArrayList;

public class RecordFragment extends Fragment {

    TextView name;

    Context context;

    RecordAdapter recordAdapter;
    RecyclerView recordRV;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_record, container, false);

        context = getActivity();

        name = rootView.findViewById(R.id.workerNameId);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recordAdapter = new RecordAdapter(context);

        recordRV = rootView.findViewById(R.id.workersRVId);
        recordRV.setLayoutManager(linearLayoutManager);
        recordRV.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(context, linearLayoutManager.getOrientation());

        recordRV.addItemDecoration(dividerItemDecoration);
        recordRV.setAdapter(recordAdapter);

        String selectedName = null;
        if (getArguments() != null) {
            selectedName = getArguments().getString("name");
            name.setText(selectedName);
        }

        ArrayList<Worker> recordList = new GetDatabase(context).getWorkerRecord(selectedName);
        recordAdapter.attachRecordList(recordList);
        Log.d("workerRecord", recordList.toString());

        return rootView;
    }
}
