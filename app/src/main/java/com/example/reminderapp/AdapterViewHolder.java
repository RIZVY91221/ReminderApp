package com.example.reminderapp;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

public class AdapterViewHolder extends RecyclerView.Adapter<AdapterViewHolder.ViewHolder> {


    private Context context;
    private List<DataModel> dataModelList;

    public AdapterViewHolder(Context context,List<DataModel> dataModels)
    {
        this.dataModelList = dataModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.res_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataModel model = dataModelList.get(position);
        holder.setData(model);
        holder.setListener();
    }

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        private TextView namazOwakto,azanTime,namazTime;
        private Button setAlarm;
        private DataModel currentModel;

        public ViewHolder(@NonNull View view) {
            super(view);

            namazTime = view.findViewById(R.id.txtNamazTime);
            namazOwakto = view.findViewById(R.id.txtOwakto);
            azanTime = view.findViewById(R.id.txtAzanTime);
            setAlarm = view.findViewById(R.id.bSetAlarm);
        }

        private void setListener()
        {
            setAlarm.setOnClickListener(AdapterViewHolder.ViewHolder.this);
        }


        private void setData(DataModel model)
        {
            currentModel = model;

            namazTime.setText(model.getNamazTime());
            azanTime.setText(model.getAzanTime());
            namazOwakto.setText(model.getPrayerName());
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context,SetRemainder.class);
            intent.putExtra("date",currentModel.getDate());
            intent.putExtra("time",currentModel.getNamazTime());
            intent.putExtra("title",currentModel.getPrayerName());
            intent.putExtra("flag",true);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Set the URI on the data field of the inten
            context.startActivity(intent);
        }
    }
}
