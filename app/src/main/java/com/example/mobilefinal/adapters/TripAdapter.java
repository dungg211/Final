package com.example.mobilefinal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilefinal.R;
import com.example.mobilefinal.database.Trip;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    private List<Trip> tripList;
    private final OnTripClickListener listener;

    public interface OnTripClickListener {
        void onTripClick(Trip trip);
    }

    // Constructor
    public TripAdapter(List<Trip> tripList, OnTripClickListener listener) {
        this.tripList = tripList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_list_item, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip currentTrip = tripList.get(position);
        holder.tvName.setText(currentTrip.name);
        holder.tvLocation.setText(currentTrip.location);
        holder.tvDate.setText(currentTrip.date);
        String details = currentTrip.lengthKm + " km - " + currentTrip.difficultyLevel;
        holder.tvDetails.setText(details);
        holder.itemView.setOnClickListener(v -> listener.onTripClick(currentTrip));
    }
    @Override
    public int getItemCount() {
        return tripList.size();
    }
    public void setTrips(List<Trip> trips) {
        this.tripList = trips;
        notifyDataSetChanged();
    }
    public static class TripViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvLocation, tvDate, tvDetails;
        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.item_trip_name);
            tvLocation = itemView.findViewById(R.id.item_trip_location);
            tvDate = itemView.findViewById(R.id.item_trip_date);
            tvDetails = itemView.findViewById(R.id.item_trip_details);
        }
    }
}