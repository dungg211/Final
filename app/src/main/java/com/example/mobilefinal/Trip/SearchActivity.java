package com.example.mobilefinal.Trip;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilefinal.MainActivity;
import com.example.mobilefinal.R;
import com.example.mobilefinal.Trip.TripDetailActivity;
import com.example.mobilefinal.adapters.TripAdapter;
import com.example.mobilefinal.database.AppDatabase;
import com.example.mobilefinal.database.Trip;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements TripAdapter.OnTripClickListener {

    private RecyclerView recyclerView;
    private TripAdapter adapter;
    private AppDatabase database;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        database = AppDatabase.getDatabase(this);

        // 1. Cài đặt RecyclerView
        recyclerView = findViewById(R.id.rv_search_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Tái sử dụng TripAdapter (truyền 'this' để xử lý click vào kết quả)
        adapter = new TripAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        // 2. Cài đặt SearchView
        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                performSearch(newText); // Tìm kiếm ngay khi gõ
                return true;
            }
        });

        // Tải tất cả dữ liệu ban đầu (khi chưa gõ gì)
        performSearch("");
        // Trong SearchActivity.java

// ... (trong onCreate) ...
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);

// 1. Đánh dấu icon hiện tại là Search
        bottomNavigation.setSelectedItemId(R.id.nav_search);

// 2. Xử lý chuyển trang
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_hike_plan) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_trip) {
                startActivity(new Intent(getApplicationContext(), TripActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_search) {
                return true; // Đang ở đây rồi
            }
            return false;
        });
    }

    private void performSearch(String query) {
        // Gọi hàm searchTrips trong DAO
        database.tripDao().searchTrips(query).observe(this, new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> trips) {
                // Cập nhật Adapter với danh sách tìm được
                adapter.setTrips(trips);
            }
        });
    }

    // === XỬ LÝ KHI BẤM VÀO KẾT QUẢ TÌM KIẾM ===
    @Override
    public void onTripClick(Trip trip) {
        // Mở trang chi tiết (giống như ở trang chủ)
        Intent intent = new Intent(SearchActivity.this, TripDetailActivity.class);
        intent.putExtra("TRIP_ID", trip.id);
        startActivity(intent);
    }
}