package com.example.atapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.atapp.fragments.MainFragment;
import com.example.atapp.model.Worker;
import com.example.atapp.sqliteAccess.GetDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FragmentTransaction ft;

    ArrayList<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting the workers list from SQLite
        GetDatabase getDatabase = new GetDatabase(MainActivity.this);
        names = getDatabase.getWorkersNames();
        getDatabase.close();

        Log.d("names:", names.toString());
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("names", names);


        Fragment mainFragment = new MainFragment();
        mainFragment.setArguments(bundle);
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragments_container, mainFragment);
        ft.commit();
    }
}