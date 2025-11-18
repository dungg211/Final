package com.example.mobilefinal;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilefinal.database.AppDatabase;
import com.example.mobilefinal.database.Trip;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class AddTripActivity extends AppCompatActivity {

    // --- UI Components ---
    private TextInputEditText etName, etLocation, etDate, etLength, etParticipants;
    private CheckBox cbParking, cbPermit;
    private Spinner spinnerDifficulty;
    private Button btnSave;

    // --- Database ---
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        // Get database instance
        database = AppDatabase.getDatabase(this);

        // Find all views
        etName = findViewById(R.id.et_trip_name);
        etLocation = findViewById(R.id.et_trip_location);
        etDate = findViewById(R.id.et_trip_date);
        etLength = findViewById(R.id.et_trip_length);
        etParticipants = findViewById(R.id.et_trip_participants);
        cbParking = findViewById(R.id.cb_parking);
        cbPermit = findViewById(R.id.cb_permit);
        spinnerDifficulty = findViewById(R.id.spinner_trip_difficulty);
        btnSave = findViewById(R.id.btn_save_trip);

        // --- Set Listeners ---

        // Date Picker
        etDate.setOnClickListener(v -> showDatePickerDialog());

        // Save Button
        btnSave.setOnClickListener(v -> {
            if (validateInput()) {
                saveTrip();
            }
        });
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
        // ... add other validation ...
        return true;
    }

    private void saveTrip() {
        // Get all data from the form
        String name = etName.getText().toString();
        String location = etLocation.getText().toString();
        String date = etDate.getText().toString();
        boolean parking = cbParking.isChecked();
        boolean permit = cbPermit.isChecked();
        double length = Double.parseDouble(etLength.getText().toString());
        int participants = Integer.parseInt(etParticipants.getText().toString());
        String difficulty = spinnerDifficulty.getSelectedItem().toString();

        // Create the new Trip object
        Trip newTrip = new Trip(name, location, date, parking, length, difficulty, permit, participants);

        // --- Save to Database (using a background thread) ---
        // Room does not allow database operations on the main thread
        new Thread(() -> {
            database.tripDao().insert(newTrip);

            // Show toast on the main thread after saving
            runOnUiThread(() -> {
                Toast.makeText(this, "Trip saved!", Toast.LENGTH_SHORT).show();
                finish(); // Close this activity and go back to TripActivity
            });
        }).start();
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