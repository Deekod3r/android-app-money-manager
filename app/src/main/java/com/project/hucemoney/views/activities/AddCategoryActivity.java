package com.project.hucemoney.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.project.hucemoney.R;
import com.project.hucemoney.common.enums.CategoryType;
import com.project.hucemoney.databinding.ActivityAddCategoryBinding;

public class AddCategoryActivity extends AppCompatActivity {

    private ActivityAddCategoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_category);
        init();
        controlAction();
        observe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void init() {
        Intent intent = getIntent();
        int type = intent.getIntExtra("type",-1);
        if (CategoryType.values()[type] == CategoryType.INCOME) {
            binding.tvTitleAddCategory.setText("Thêm danh mục chi");
        } else if (CategoryType.values()[type] == CategoryType.EXPENSE) {
            binding.tvTitleAddCategory.setText("Thêm danh mục thu");
        } else if (CategoryType.values()[type] == CategoryType.DEBT) {
            binding.tvTitleAddCategory.setText("Thêm danh mục vay/nợ");
        } else {
            Toast.makeText(this, "Lỗi", Toast.LENGTH_SHORT).show();
        }
    }

    private void controlAction() {
        binding.btnClose.setOnClickListener(v -> {
            finish();
        });
    }

    private void observe() {

    }

}