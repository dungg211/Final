package com.example.mobilefinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer; // <-- THÊM IMPORT NÀY
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// Import các tệp mới
import com.example.mobilefinal.adapters.TripAdapter;
import com.example.mobilefinal.database.AppDatabase; // (Import AppDatabase)
import com.example.mobilefinal.database.Trip;

import java.util.ArrayList;
import java.util.List;

// === BƯỚC 1: TRIỂN KHAI INTERFACE ===
public class TripActivity extends AppCompatActivity implements TripAdapter.OnTripClickListener {

    // --- Khai báo UI ---
    private Button btnCreateTrip, btnDeleteAll;
    private RecyclerView recyclerViewTrips;

    // --- Khai báo Logic ---
    private AppDatabase database; // (Dùng AppDatabase thay vì DatabaseHelper)
    private TripAdapter tripAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        // --- Ánh xạ UI ---
        btnCreateTrip = findViewById(R.id.btn_create_trip);
        btnDeleteAll = findViewById(R.id.btn_delete_all_trips);
        recyclerViewTrips = findViewById(R.id.recyclerView_trips);

        // --- Khởi tạo Logic ---
        database = AppDatabase.getDatabase(this); // Lấy instance của Room DB
        setupRecyclerView();
        loadTripsFromDatabase(); // Bắt đầu tải dữ liệu

        // === Xử lý sự kiện "Create Trip" ===
        btnCreateTrip.setOnClickListener(v -> {
            Intent intent = new Intent(TripActivity.this, AddTripActivity.class);
            startActivity(intent);
        });

        // === Xử lý sự kiện "Delete All" ===
        btnDeleteAll.setOnClickListener(v -> {
            new AlertDialog.Builder(TripActivity.this)
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete ALL trips?")
                    .setPositiveButton("Yes, Delete All", (dialog, which) -> {
                        // Phải chạy trên background thread
                        new Thread(() -> {
                            database.tripDao().deleteAllTrips();
                            runOnUiThread(() -> Toast.makeText(TripActivity.this, "All trips deleted.", Toast.LENGTH_SHORT).show());
                        }).start();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    private void setupRecyclerView() {
        // "this" là OnTripClickListener
        tripAdapter = new TripAdapter(new ArrayList<>(), this);
        recyclerViewTrips.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTrips.setAdapter(tripAdapter);
    }

    /**
     * Tải dữ liệu bằng LiveData (tự động cập nhật)
     */
    private void loadTripsFromDatabase() {
        // .observe() sẽ tự động chạy khi dữ liệu thay đổi (Thêm/Xóa)
        database.tripDao().getAllTrips().observe(this, new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> trips) {
                // Cập nhật dữ liệu cho Adapter
                tripAdapter.setTrips(trips);
            }
        });
    }

    /**
     * === BƯỚC 2: XỬ LÝ CLICK (ĐỂ SỬA) ===
     * Hàm này được gọi từ Adapter khi bạn bấm vào một item
     */
    @Override
    public void onTripClick(Trip trip) {
        // Mở AddTripActivity, nhưng gửi ID theo
        Intent intent = new Intent(TripActivity.this, EditTrip.class);
        intent.putExtra("TRIP_ID", trip.id); // Gửi ID để "Sửa"
        startActivity(intent);
    }
}