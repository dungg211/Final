package com.example.mobilefinal.Trip;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilefinal.Observation.AddObservationActivity;
import com.example.mobilefinal.R;
import com.example.mobilefinal.adapters.ObservationAdapter;
import com.example.mobilefinal.database.AppDatabase;
import com.example.mobilefinal.database.Observation;
import com.example.mobilefinal.database.Trip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

// Triển khai listener của Adapter
public class TripDetailActivity extends AppCompatActivity implements ObservationAdapter.OnObservationClickListener {

    // --- UI Components ---
    private TextView tvName, tvLocation, tvDate, tvDetails;
    private Button btnEditTrip;
    private  Button btnBack;
    private Button btn_delete_all_Observations;


    private FloatingActionButton fabAddObservation;
    private RecyclerView rvObservations;

    // --- Logic ---
    private AppDatabase database;
    private ObservationAdapter observationAdapter;
    private int tripId = -1;
    private Trip currentTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            finish();
        });

        btn_delete_all_Observations = findViewById(R.id.btn_delete_all_Observations);
        btn_delete_all_Observations.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete All Observations") // (Sửa tiêu đề)
                    .setMessage("Are you sure you want to delete ALL observations for this trip?") // (Sửa tin nhắn)
                    .setPositiveButton("Delete All", (dialog, which) -> { // (Sửa nút)
                        new Thread(() -> {
                            database.observationDao().deleteAllObservationsForTrip(tripId);
                            runOnUiThread(() -> Toast.makeText(this, "All observations deleted.", Toast.LENGTH_SHORT).show());
                        }).start();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        database = AppDatabase.getDatabase(this);
        tripId = getIntent().getIntExtra("TRIP_ID", -1);
        if (tripId == -1) {
            Toast.makeText(this, "Error: Invalid Trip ID.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvName = findViewById(R.id.detail_trip_name);
        tvLocation = findViewById(R.id.detail_trip_location);
        tvDate = findViewById(R.id.detail_trip_date);
        tvDetails = findViewById(R.id.detail_trip_details);
        btnEditTrip = findViewById(R.id.btn_edit_trip_details);
        fabAddObservation = findViewById(R.id.fab_add_observation);
        rvObservations = findViewById(R.id.rv_observations);

        // --- Cài đặt Listeners ---
        // 1. Nút Sửa Chuyến đi
        btnEditTrip.setOnClickListener(v -> {
            Intent intent = new Intent(TripDetailActivity.this, EditTrip.class);
            intent.putExtra("TRIP_ID", tripId);
            startActivity(intent);
        });

        // 2. Nút Thêm Quan sát
        fabAddObservation.setOnClickListener(v -> {
            Intent intent = new Intent(TripDetailActivity.this, AddObservationActivity.class);
            intent.putExtra("TRIP_ID", tripId); // Gửi ID của chuyến đi này
            startActivity(intent);
        });

        // --- Tải dữ liệu ---
        setupRecyclerView();

        loadObservations();
    }

    private void setupRecyclerView() {
        observationAdapter = new ObservationAdapter(new ArrayList<>(), this);
        rvObservations.setLayoutManager(new LinearLayoutManager(this));
        rvObservations.setAdapter(observationAdapter);
    }

    /**
     * Tải thông tin chi tiết của chuyến đi (Trip)
     */
    private void loadTripDetails() {
        // (Tải trên background thread)
        new Thread(() -> {
            currentTrip = database.tripDao().getTripById(tripId);
            if (currentTrip != null) {
                // Cập nhật UI trên main thread
                runOnUiThread(() -> populateTripDetails(currentTrip));
            }
        }).start();
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Tải lại chi tiết chuyến đi mỗi khi bạn quay lại màn hình này
        // Điều này đảm bảo dữ liệu luôn được cập nhật sau khi Edit
        loadTripDetails();
    }
    private void populateTripDetails(Trip trip) {
        tvName.setText(trip.name);
        tvLocation.setText(trip.location);
        tvDate.setText(trip.date);
        String details = trip.lengthKm + " km - " + trip.difficultyLevel
                + " - Parking: " + (trip.parkingAvailable ? "Yes" : "No");
        tvDetails.setText(details);
    }

    /**
     * Tải danh sách quan sát (dùng LiveData để tự động cập nhật)
     */
    private void loadObservations() {
        LiveData<List<Observation>> observationLiveData = database.observationDao().getObservationsForTrip(tripId);
        observationLiveData.observe(this, observations -> {
            observationAdapter.setObservations(observations);
        });
    }

    // === XỬ LÝ SỬA/XÓA QUAN SÁT (THEO YÊU CẦU ĐỀ BÀI) ===

    @Override
    public void onObsClick(Observation observation) {
        Intent intent = new Intent(TripDetailActivity.this, AddObservationActivity.class);

        intent.putExtra("TRIP_ID", tripId); // Vẫn gửi Trip ID để phòng hờ
        intent.putExtra("OBS_ID", observation.id); // Gửi ID quan sát để biết là đang Sửa

        startActivity(intent);
    }

    @Override
    public void onObsLongClick(Observation observation) {
        // === ĐÂY LÀ NƠI ĐỂ XÓA 1 CÁI ===
        new AlertDialog.Builder(this)
                .setTitle("Delete Observation")
                .setMessage("Are you sure you want to delete this observation?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    new Thread(() -> {
                        database.observationDao().delete(observation); // Xóa 1 object
                        runOnUiThread(() -> Toast.makeText(this, "Observation deleted.", Toast.LENGTH_SHORT).show());
                    }).start();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}