package com.example.mobilefinal.Trip; // (Hoặc package của bạn)

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import android.app.DatePickerDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilefinal.R;
import com.example.mobilefinal.database.AppDatabase;
import com.example.mobilefinal.database.Trip;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
public class EditTrip extends AppCompatActivity { // (Tên class là EditTrip)

    private TextInputEditText etName, etLocation, etDate, etLength, etParticipants;
    private CheckBox cbParking, cbPermit;
    private Spinner spinnerDifficulty;
    private Button btnUpdate;
    private Button btnDelete;   private AppDatabase database;
    private Trip existingTrip = null; // Chuyến đi đang sửa
    private int tripId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);
        btnDelete = findViewById(R.id.btn_delete_trip);
        database = AppDatabase.getDatabase(this);
        etName = findViewById(R.id.et_trip_name);
        etLocation = findViewById(R.id.et_trip_location);
        etDate = findViewById(R.id.et_trip_date);
        etLength = findViewById(R.id.et_trip_length);
        etParticipants = findViewById(R.id.et_trip_participants);
        cbParking = findViewById(R.id.cb_parking);
        cbPermit = findViewById(R.id.cb_permit);
        spinnerDifficulty = findViewById(R.id.spinner_trip_difficulty);
        btnUpdate = findViewById(R.id.btn_update_trip);
        if (getIntent().hasExtra("TRIP_ID")) {
            tripId = getIntent().getIntExtra("TRIP_ID", -1);
            if (tripId != -1) {
                loadTripData(tripId);
            }
        } else {
            Toast.makeText(this, "Error: No Trip ID found.", Toast.LENGTH_SHORT).show();
            finish();
        }
        etDate.setOnClickListener(v -> showDatePickerDialog());
        btnUpdate.setOnClickListener(v -> {
            if (validateInput()) {
                updateTrip();
            }
        });
        btnDelete.setOnClickListener(v -> {
            // Hiển thị hộp thoại xác nhận (giống code của bạn)
            showDeleteConfirmationDialog();
        });    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditTrip.this);
        alertDialogBuilder.setMessage("Bạn có muốn xóa chuyến đi này!"); // (Giống code của bạn)

        alertDialogBuilder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Xóa chuyến đi này
                deleteTrip();
            }
        });

        alertDialogBuilder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Không làm gì
            }
        });

        alertDialogBuilder.show();
    }
    private void deleteTrip() {
        if (existingTrip != null) {
            // Chạy trên background thread
            new Thread(() -> {
                database.tripDao().delete(existingTrip);

                // Sau khi xóa, quay lại màn hình danh sách
                runOnUiThread(() -> {
                    Toast.makeText(EditTrip.this, "Trip deleted.", Toast.LENGTH_SHORT).show();
                    finish(); // Đóng màn hình Edit
                });
            }).start();
        }
    }

    private void loadTripData(int id) {
        new Thread(() -> {
            existingTrip = database.tripDao().getTripById(id);
            if (existingTrip != null) {
                // Hiển thị dữ liệu lên UI (phải chạy trên main thread)
                runOnUiThread(() -> populateForm(existingTrip));
            }
        }).start();
    }
    private void populateForm(Trip trip) {
        etName.setText(trip.name);
        etLocation.setText(trip.location);
        etDate.setText(trip.date);
        cbParking.setChecked(trip.parkingAvailable);
        cbPermit.setChecked(trip.movementPermit);
        etLength.setText(String.valueOf(trip.lengthKm));
        etParticipants.setText(String.valueOf(trip.participantCount));
        SpinnerAdapter adapter = spinnerDifficulty.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).toString().equalsIgnoreCase(trip.difficultyLevel)) {
                spinnerDifficulty.setSelection(i);
                break;
            }
        }
    }
    private void updateTrip() {
        // 1. Lấy tất cả dữ liệu từ form
        String name = etName.getText().toString();
        String location = etLocation.getText().toString();
        String date = etDate.getText().toString();
        boolean parking = cbParking.isChecked();
        boolean permit = cbPermit.isChecked();
        double length = Double.parseDouble(etLength.getText().toString());
        int participants = Integer.parseInt(etParticipants.getText().toString());
        String difficulty = spinnerDifficulty.getSelectedItem().toString();
        Trip tripToUpdate = new Trip(name, location, date, parking, length, difficulty, permit, participants);
        tripToUpdate.id = existingTrip.id;
        new Thread(() -> {
            database.tripDao().update(tripToUpdate);
            runOnUiThread(() -> Toast.makeText(this, "Trip Updated!", Toast.LENGTH_SHORT).show());
            finish();
        }).start();
    }
    private boolean validateInput() {
        if (TextUtils.isEmpty(etName.getText().toString())) {
            etName.setError("Name is required");
            return false;
        }
        if (TextUtils.isEmpty(etLocation.getText().toString())) {
            etLocation.setError("Location is required");
            return false;
        }
        return true;
    }
    private void showDatePickerDialog() {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    etDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        );
        dialog.show();
    }
   }