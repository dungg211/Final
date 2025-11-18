package com.example.mobilefinal.adapters;

// (Import các thư viện: LayoutInflater, List, TextView, RecyclerView, v.v.)
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilefinal.database.Observation; // (Import model Observation)
import com.example.mobilefinal.R;

import java.util.List;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.ObsViewHolder> {

    private List<Observation> observationList;
    private final OnObservationClickListener listener;

    // Interface để Sửa/Xóa (theo yêu cầu )
    public interface OnObservationClickListener {
        void onObsClick(Observation observation); // Sửa
        void onObsLongClick(Observation observation); // Xóa
    }

    public ObservationAdapter(List<Observation> list, OnObservationClickListener listener) {
        this.observationList = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ObsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.observation_list_item, parent, false);
        return new ObsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObsViewHolder holder, int position) {
        Observation obs = observationList.get(position);
        holder.tvText.setText(obs.observationText);
        holder.tvTime.setText(obs.observationTime);
        holder.tvComments.setText(obs.comments);

        // Cài đặt click để Sửa/Xóa
        holder.itemView.setOnClickListener(v -> listener.onObsClick(obs));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onObsLongClick(obs);
            return true;
        });
    }

    @Override public int getItemCount() { return observationList.size(); }

    public void setObservations(List<Observation> observations) {
        this.observationList = observations;
        notifyDataSetChanged();
    }

    // ViewHolder
    public static class ObsViewHolder extends RecyclerView.ViewHolder {
        TextView tvText, tvTime, tvComments;
        public ObsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.item_obs_text);
            tvTime = itemView.findViewById(R.id.item_obs_time);
            tvComments = itemView.findViewById(R.id.item_obs_comments);
        }
    }
}