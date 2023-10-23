package com.project.hucemoney.views.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.tabs.TabLayout;
import com.project.hucemoney.R;
import com.project.hucemoney.databinding.ActivityCategoryBinding;

public class CategoryActivity extends AppCompatActivity {

    private ActivityCategoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category);
        init();
        initTabLayout();
        controlAction();
        observe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void init() {

    }

    private void initTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = binding.tabLayout.getSelectedTabPosition();
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Xử lý khi tab không còn được chọn
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Xử lý khi tab đã được chọn và lại được chọn lần nữa
            }
        });
    }

    private void controlAction() {
        binding.btnClose.setOnClickListener(v -> {
            finish();
        });
        binding.btnAddCategory.setOnClickListener(v -> {
            Intent intent = new Intent(CategoryActivity.this, AddCategoryActivity.class);
            intent.putExtra("type", binding.tabLayout.getSelectedTabPosition());
            startActivity(intent);
        });
    }

    private void observe() {

    }
}