package com.example.mobilefinal.Trip;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mobilefinal.R;
import com.example.mobilefinal.database.AppDatabase;
import com.example.mobilefinal.database.Trip;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;

public class AddTripActivity extends AppCompatActivity {
    private TextInputEditText etName, etLocation, etDate, etLength, etParticipants;
    private CheckBox cbParking, cbPermit;
    private Spinner spinnerDifficulty;
    private Button btnSave;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        database = AppDatabase.getDatabase(this);
        etName = findViewById(R.id.et_trip_name);
        etLocation = findViewById(R.id.et_trip_location);
        etDate = findViewById(R.id.et_trip_date);
        etLength = findViewById(R.id.et_trip_length);
        etParticipants = findViewById(R.id.et_trip_participants);
        cbParking = findViewById(R.id.cb_parking);
        cbPermit = findViewById(R.id.cb_permit);
        spinnerDifficulty = findViewById(R.id.spinner_trip_difficulty);
        btnSave = findViewById(R.id.btn_save_trip);
        etDate.setOnClickListener(v -> showDatePickerDialog());

        btnSave.setOnClickListener(v -> {
            if (validateInput()) {
                goToConfirmPage();
            }
        });
    }
    private void goToConfirmPage() {
            String name = etName.getText().toString();
            String location = etLocation.getText().toString();
            String date = etDate.getText().toString();
            boolean parking = cbParking.isChecked();
            boolean permit = cbPermit.isChecked();
            double length = 0.0;
            int participants = 0;
            try {
                length = Double.parseDouble(etLength.getText().toString());
                participants = Integer.parseInt(etParticipants.getText().toString());
            } catch (NumberFormatException e) {
            }
            String difficulty = spinnerDifficulty.getSelectedItem().toString();
            Trip tempTrip = new Trip(name, location, date, parking, length, difficulty, permit, participants);
            Intent intent = new Intent(AddTripActivity.this, ConfimTripActivity.class);
            intent.putExtra("TRIP_DATA", tempTrip);
            startActivity(intent);
        }
    private boolean validateInput() {
        // You must add validation here as required by the coursework
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