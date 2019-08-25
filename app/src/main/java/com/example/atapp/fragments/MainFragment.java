package com.example.atapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.atapp.MainActivity;
import com.example.atapp.R;
import com.example.atapp.model.Worker;
import com.example.atapp.sqliteAccess.GetDatabase;
import com.example.atapp.sqliteAccess.SQLiteHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class MainFragment extends Fragment {

    String selectedName;
    int sqLiteId, savedPosition;

    ArrayList<String> workersNames;
    ArrayAdapter<String> adapter;

    Fragment recordFragment;
    FragmentTransaction ft;

    Context context;

    Button enterBtn, exitBtn, recordBtn, addBtn, removeBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        context = getActivity();

        enterBtn = rootView.findViewById(R.id.enterBtnId);
        exitBtn = rootView.findViewById(R.id.exitBtnId);
        recordBtn = rootView.findViewById(R.id.recordBtnId);
        addBtn = rootView.findViewById(R.id.addBtnId);
        removeBtn = rootView.findViewById(R.id.removeBtn);

        final Spinner spinner = rootView.findViewById(R.id.spinnerId);

        //create a list of items for the spinner.

        getSpinner(spinner);

        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if the selected spinner item is not the first one (which is "choose a worker")
                if (savedPosition > 0) {
                    // if the enter button is clicked for the first time
                    new SQLiteHelper(context, selectedName, "enter");
                } else {
                    Toast.makeText(context,"No worker was chosen", Toast.LENGTH_SHORT).show();
                }
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (savedPosition > 0) {
                    new SQLiteHelper(context, selectedName, "exit");
                } else {
                    Toast.makeText(context, "No worker was chosen", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(savedPosition!=0) {

                    workersNames.remove(0);
                    goToRecord();
                }
                else{
                    Toast.makeText(context,"No worker was chosen", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAWorker();
            }
        });
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (savedPosition > 0) {
                    //if no worker was selected
                    removeWorker();
                } else {
                    Toast.makeText(context,"No worker was chosen", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    private void getSpinner(final Spinner spinner) {

        String[] listFirstItem = new String[1];
        listFirstItem[0] = "Choose a worker ";

        if (getArguments() != null) {

            workersNames = getArguments().getStringArrayList("names");

            Collections.sort(workersNames, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    return s1.compareToIgnoreCase(s2);
                }
            });

            Log.d("workersNames2", workersNames.toString());
            String[] namesStringArray;
//            if (workersNames.isEmpty()) {
                    workersNames.add(0, Arrays.toString(listFirstItem).replace("[", "").replace("]", ""));
//            }
            Log.d("workersNames", workersNames.toString());

            namesStringArray = workersNames.toArray(new String[0]);
            adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, namesStringArray);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    savedPosition = position;

                    if(position != 0) {
                        sqLiteId = position - 1;
                        selectedName = spinner.getSelectedItem().toString();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {

                    savedPosition = 0;
                }
            });
        }
    }

    private void goToRecord() {

        if(recordFragment == null){
            recordFragment = new RecordFragment();
        }

        Bundle recordBundle = new Bundle();
        recordBundle.putString("name", selectedName);
        Log.d("Monitoring", "Clicked id: "+ selectedName);
        recordFragment.setArguments(recordBundle);

        recordFragment.setArguments(recordBundle);
        if (getFragmentManager() != null) {
            ft = getFragmentManager().beginTransaction();
        }

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.fragments_container, recordFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void addAWorker() {

        View inflatedView = LayoutInflater.from(context).inflate(R.layout.dialog_worker_add, null, false);

        Button add = inflatedView.findViewById(R.id.addWorkerBtnId);
        final EditText newName = inflatedView.findViewById(R.id.addWorkerNameId);
        final EditText newId = inflatedView.findViewById(R.id.addWorkerIdId);

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(inflatedView);

        final AlertDialog newWorkerAlert = alert.create();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String typedName = newName.getText().toString();
                final int idLength = newId.getText().length();
//                final int typedId = Integer.parseInt(newId.getText().toString());

                if (typedName.isEmpty()) {
                    Toast.makeText(context, "Please fill in all the text fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (idLength != 9) {
                    Toast.makeText(context, "An ID number must include 9 digits", Toast.LENGTH_SHORT).show();
                    return;
                }

                new GetDatabase(getActivity(), typedName, Integer.parseInt(newId.getText().toString()));

                reload();
                newWorkerAlert.dismiss();
            }
        });

        newWorkerAlert.show();
    }

    private void removeWorker() {

        // Set the alert's layout
        View inflatedView = LayoutInflater.from(context).inflate(R.layout.dialod_worker_remove,null,false);

        Button approve = inflatedView.findViewById(R.id.approveRemovalId);
        Button cancel = inflatedView.findViewById(R.id.cancelRemovalId);
        TextView name = inflatedView.findViewById(R.id.removedId);

        String removeString = selectedName + "?";
        name.setText(removeString);

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(inflatedView);

        final AlertDialog removeWorkerAlert = alert.create();

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetDatabase(context).removeWorker(selectedName);
                removeWorkerAlert.dismiss();
                reload();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeWorkerAlert.dismiss();
            }
        });

        removeWorkerAlert.show();
    }

    private void reload() {

        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }
}