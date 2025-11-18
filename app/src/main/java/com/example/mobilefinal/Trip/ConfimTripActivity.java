package com.example.mobilefinal.Trip;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilefinal.R;
import com.example.mobilefinal.database.AppDatabase;
import com.example.mobilefinal.database.Trip;

public class ConfimTripActivity extends AppCompatActivity {

    // 1. Khai báo thêm các TextView mới
    private TextView tvName, tvLocation, tvDate, tvParking, tvPermit, tvLength, tvDifficulty, tvParticipants;
    private Button btnConfirm, btnEdit;
    private Trip tripData;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_trip);

        database = AppDatabase.getDatabase(this);

        // 2. Ánh xạ UI (Tìm các TextView mới theo ID trong XML)
        tvName = findViewById(R.id.tv_confirm_name);
        tvLocation = findViewById(R.id.tv_confirm_location);
        tvDate = findViewById(R.id.tv_confirm_date);
        tvParking = findViewById(R.id.tv_confirm_parking);
        tvPermit = findViewById(R.id.tv_confirm_permit);
        tvLength = findViewById(R.id.tv_confirm_length);
        tvDifficulty = findViewById(R.id.tv_confirm_difficulty);
        tvParticipants = findViewById(R.id.tv_confirm_participants);

        btnConfirm = findViewById(R.id.btn_confirm_save);
        btnEdit = findViewById(R.id.btn_back_edit);

        // 3. Nhận và Hiển thị dữ liệu
        if (getIntent().hasExtra("TRIP_DATA")) {
            tripData = (Trip) getIntent().getSerializableExtra("TRIP_DATA");

            if (tripData != null) {
                // Chuỗi (String) thì set trực tiếp
                tvName.setText(tripData.name);
                tvLocation.setText(tripData.location);
                tvDate.setText(tripData.date);
                tvDifficulty.setText(tripData.difficultyLevel);

                // Boolean (Đúng/Sai) -> Chuyển thành "Yes"/"No"
                tvParking.setText(tripData.parkingAvailable ? "Yes" : "No");
                tvPermit.setText(tripData.movementPermit ? "Yes" : "No");

                // Số (Double/Int) -> Phải chuyển thành String bằng String.valueOf()
                // Nếu không sẽ bị lỗi crash app
                tvLength.setText(String.valueOf(tripData.lengthKm));
                tvParticipants.setText(String.valueOf(tripData.participantCount));
            }
        }

        btnConfirm.setOnClickListener(v -> saveTripToDatabase());

        btnEdit.setOnClickListener(v -> finish());
    }

    private void saveTripToDatabase() {
        new Thread(() -> {
            database.tripDao().insert(tripData);

            runOnUiThread(() -> {
                Toast.makeText(this, "Trip created successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ConfimTripActivity.this, TripActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        }).start();
    }
}