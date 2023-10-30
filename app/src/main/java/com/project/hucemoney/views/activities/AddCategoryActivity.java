package com.project.hucemoney.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.project.hucemoney.R;
import com.project.hucemoney.common.Constants;
import com.project.hucemoney.common.ResponseCode;
import com.project.hucemoney.common.enums.CategoryType;
import com.project.hucemoney.databinding.ActivityAddCategoryBinding;
import com.project.hucemoney.utils.FunctionUtils;
import com.project.hucemoney.viewmodels.AccountViewModel;
import com.project.hucemoney.viewmodels.CategoryViewModel;

public class AddCategoryActivity extends AppCompatActivity {

    private ActivityAddCategoryBinding binding;
    private CategoryViewModel categoryViewModel;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_category);
        init();
        controlAction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void init() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", -1);
        if (CategoryType.values()[type] == CategoryType.INCOME) {
            binding.tvTitleAddCategory.setText("Thêm danh mục chi");
        } else if (CategoryType.values()[type] == CategoryType.EXPENSE) {
            binding.tvTitleAddCategory.setText("Thêm danh mục thu");
        } else if (CategoryType.values()[type] == CategoryType.DEBT) {
            binding.tvTitleAddCategory.setText("Thêm danh mục vay/nợ");
        } else {
            Toast.makeText(this, "Lỗi", Toast.LENGTH_SHORT).show();
        }
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setCategoryViewModel(categoryViewModel);
    }

    private void controlAction() {
        binding.btnClose.setOnClickListener(v -> {
            finish();
        });

        binding.btnSave.setOnClickListener(v -> {
            if (CategoryType.values()[type] == CategoryType.DEBT) {
                Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
                return;
            }
            categoryViewModel.getCategoryAddRequest().setType(type != 0);
            categoryViewModel.getCategoryAddRequest().setParent(null);
            categoryViewModel.addCategory();
            categoryViewModel.getResultAddCategory().observe(this, response -> {
                if (response.getStatus().equals(ResponseCode.SUCCESS)) {
                    FunctionUtils.hideKeyboard(this,v);
                    Intent data = new Intent();
                    data.putExtra("categoryAdded", response.getData());
                    data.putExtra("action", Constants.ACTION_ADD);
                    setResult(Activity.RESULT_OK, data);
                    finish();
                }
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });
    }


}