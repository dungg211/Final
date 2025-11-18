package com.example.mobilefinal.Observation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilefinal.R;
import com.example.mobilefinal.database.AppDatabase;
import com.example.mobilefinal.database.Observation;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddObservationActivity extends AppCompatActivity {

    private TextInputEditText etObservation, etTime, etComments;
    private Button btnSave;
    private AppDatabase database;

    private int tripId = -1;

    // === BIẾN CHO CHẾ ĐỘ SỬA ===
    private Observation existingObs = null;
    private int obsId = -1;
    // ===========================

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_observation);

        database = AppDatabase.getDatabase(this);
        calendar = Calendar.getInstance();

        // Ánh xạ UI
        etObservation = findViewById(R.id.et_observation_text);
        etTime = findViewById(R.id.et_observation_time);
        etComments = findViewById(R.id.et_observation_comments);
        btnSave = findViewById(R.id.btn_save_observation);

        // Lấy Trip ID (luôn cần thiết)
        tripId = getIntent().getIntExtra("TRIP_ID", -1);

        // === KIỂM TRA CHẾ ĐỘ SỬA ===
        if (getIntent().hasExtra("OBS_ID")) {
            obsId = getIntent().getIntExtra("OBS_ID", -1);
            if (obsId != -1) {
                btnSave.setText("Update Observation"); // Đổi tên nút
                loadObservationData(obsId); // Tải dữ liệu cũ lên
            }
        } else {
            // Chế độ thêm mới: Đặt thời gian mặc định là hiện tại
            updateTimeLabel();
        }
        // ===========================

        etTime.setOnClickListener(v -> showDateTimePicker());
        btnSave.setOnClickListener(v -> saveOrUpdateObservation());
    }

    /**
     * Tải dữ liệu quan sát để sửa (Chạy ngầm)
     */
    private void loadObservationData(int id) {
        new Thread(() -> {
            existingObs = database.observationDao().getObservationById(id);
            if (existingObs != null) {
                runOnUiThread(() -> {
                    etObservation.setText(existingObs.observationText);
                    etTime.setText(existingObs.observationTime);
                    etComments.setText(existingObs.comments);
                });
            }
        }).start();
    }

    /**
     * Lưu hoặc Cập nhật
     */
    private void saveOrUpdateObservation() {
        String obsText = etObservation.getText().toString().trim();
        String obsTime = etTime.getText().toString().trim();
        String obsComments = etComments.getText().toString().trim();

        if (TextUtils.isEmpty(obsText)) {
            etObservation.setError("Observation text is required");
            return;
        }

        // Tạo object mới
        // Lưu ý: Nếu đang sửa, ta vẫn dùng tripId cũ của nó
        Observation observation = new Observation(tripId, obsText, obsTime, obsComments);

        new Thread(() -> {
            if (existingObs != null) {
                // === UPDATE (SỬA) ===
                observation.id = existingObs.id; // Quan trọng: Gán ID cũ để update đúng dòng
                observation.trip_id = existingObs.trip_id; // Giữ nguyên trip_id cũ
                database.observationDao().update(observation);
                runOnUiThread(() -> Toast.makeText(this, "Observation Updated!", Toast.LENGTH_SHORT).show());
            } else {
                // === INSERT (THÊM MỚI) ===
                if (tripId == -1) {
                    runOnUiThread(() -> Toast.makeText(this, "Error: No Trip ID", Toast.LENGTH_SHORT).show());
                    return;
                }
                database.observationDao().insert(observation);
                runOnUiThread(() -> Toast.makeText(this, "Observation Saved!", Toast.LENGTH_SHORT).show());
            }
            finish(); // Đóng màn hình
        }).start();
    }

    // (Các hàm DatePicker/TimePicker giữ nguyên)
    private void updateTimeLabel() {
        String format = "HH:mm dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        etTime.setText(sdf.format(calendar.getTime()));
    }

    private void showDateTimePicker() {
        // (Giữ nguyên code showDateTimePicker của bạn)
        new DatePickerDialog(this, (view, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            new TimePickerDialog(this, (v, hour, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                updateTimeLabel();
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}