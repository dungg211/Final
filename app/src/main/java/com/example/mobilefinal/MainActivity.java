package com.example.mobilefinal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

<<<<<<< HEAD
import com.example.mobilefinal.Trip.SearchActivity;
=======
>>>>>>> 3f969490bbc306db4d04de7c3260f80c4db4e8fa
import com.example.mobilefinal.Trip.TripActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;

    // (Bạn nên xóa @SuppressLint này đi sau khi sửa, nhưng tôi sẽ giữ nó)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setSelectedItemId(R.id.nav_hike_plan);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

<<<<<<< HEAD
            if (itemId == R.id.nav_hike_plan) {
                return true; // Đang ở đây rồi, không làm gì
            } else if (itemId == R.id.nav_trip) {
                startActivity(new Intent(getApplicationContext(), TripActivity.class));
                overridePendingTransition(0, 0); // Tắt hiệu ứng chuyển cảnh để mượt hơn
                return true;
            } else if (itemId == R.id.nav_search) {
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                overridePendingTransition(0, 0);
                return true;
=======
                if (itemId == R.id.nav_hike_plan) {
                    Intent tripIntent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(tripIntent);
                    return true;
                } else if (itemId == R.id.nav_trip) {
                    Intent tripIntent = new Intent(MainActivity.this, TripActivity.class);
                    startActivity(tripIntent);
                    return true;
                }
                return false;
>>>>>>> 3f969490bbc306db4d04de7c3260f80c4db4e8fa
            }
            return false;
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}