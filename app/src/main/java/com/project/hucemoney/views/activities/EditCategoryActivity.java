package com.project.hucemoney.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.project.hucemoney.R;
import com.project.hucemoney.adapters.entities.CategoryAdapter;
import com.project.hucemoney.common.Constants;
import com.project.hucemoney.common.ResponseCode;
import com.project.hucemoney.common.enums.CategoryType;
import com.project.hucemoney.common.enums.DialogType;
import com.project.hucemoney.databinding.ActivityAddCategoryBinding;
import com.project.hucemoney.databinding.ActivityEditCategoryBinding;
import com.project.hucemoney.entities.Category;
import com.project.hucemoney.utils.FunctionUtils;
import com.project.hucemoney.viewmodels.CategoryViewModel;

public class EditCategoryActivity extends AppCompatActivity {

    private ActivityEditCategoryBinding binding;
    private CategoryViewModel categoryViewModel;
    private Category category;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_category);
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
            binding.tvTitleAddCategory.setText("Sửa danh mục chi");
        } else if (CategoryType.values()[type] == CategoryType.EXPENSE) {
            binding.tvTitleAddCategory.setText("Sửa danh mục thu");
        } else if (CategoryType.values()[type] == CategoryType.DEBT) {
            binding.tvTitleAddCategory.setText("Sửa danh mục vay/nợ");
        } else {
            Toast.makeText(this, "Lỗi", Toast.LENGTH_SHORT).show();
            finish();
        }
        position = intent.getIntExtra("position", -1);
        if (position == -1) {
            Toast.makeText(this, "Lỗi", Toast.LENGTH_SHORT).show();
            finish();
        }
        category = intent.getParcelableExtra("category");
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.getCategoryEditRequest().setUUID(category.getUUID());
        categoryViewModel.getCategoryEditRequest().setName(category.getName());
        categoryViewModel.getCategoryEditRequest().setNote(category.getNote());
        binding.setCategoryViewModel(categoryViewModel);
        binding.setLifecycleOwner(this);
    }

    private void controlAction() {
        binding.btnClose.setOnClickListener(v -> {
            finish();
        });
        binding.btnSave.setOnClickListener(v -> {
            categoryViewModel.editCategory();
        });
        binding.btnDelete.setOnClickListener(v -> {
            FunctionUtils.showDialogConfirm(this, "", "Bạn có chắc chắn muốn xóa danh mục này không?", DialogType.WARNING,
                    (dialog, which) -> {
                        categoryViewModel.deleteCategory();
                        dialog.dismiss();
                    }, (dialog, which) -> {
                        dialog.dismiss();
                    });
        });
    }

    private void observe() {
        categoryViewModel.getResultEditCategory().observe(this, response -> {
            Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
            if (response.getStatus().equals(ResponseCode.SUCCESS)) {
                Intent data = new Intent();
                data.putExtra("categoryEdited", response.getData());
                data.putExtra("position", position);
                data.putExtra("action", Constants.ACTION_EDIT);
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });
        categoryViewModel.getResultDeleteCategory().observe(this, response -> {
            Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
            if (response.getStatus().equals(ResponseCode.SUCCESS)) {
                Intent data = new Intent();
                data.putExtra("position", position);
                data.putExtra("action", Constants.ACTION_DELETE);
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });
    }
}