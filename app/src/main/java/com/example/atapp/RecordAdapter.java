package com.example.atapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.atapp.model.Worker;

import java.util.ArrayList;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RowViewHolder>{

    private Context context;
    private List<Worker> recordList;

    public RecordAdapter(Context context) {

        this.context = context;
        recordList = new ArrayList<>();
    }

    @NonNull
    @Override //of the row
    public RowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(context)
                //row's layout
                .inflate(R.layout.row_record, viewGroup,false);

        return new RowViewHolder(itemView);
    }

    class RowViewHolder extends RecyclerView.ViewHolder{

        TextView viewEnterExit;
        TextView viewTime;

        RowViewHolder(@NonNull View itemView) {
            super(itemView);
            viewEnterExit = itemView.findViewById(R.id.enterExitId);
            viewTime = itemView.findViewById(R.id.timeId);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RowViewHolder rowViewHolder, int i) {

        rowViewHolder.viewEnterExit.setText(recordList.get(i).getEnterExit());
        rowViewHolder.viewTime.setText(recordList.get(i).getTime());
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public void attachRecordList(List<Worker> recordList) {
        // getting the list from the Fragment.
        this.recordList = recordList;
    }
}
