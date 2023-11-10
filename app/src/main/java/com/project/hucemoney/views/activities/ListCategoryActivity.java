package com.project.hucemoney.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.project.hucemoney.R;
import com.project.hucemoney.adapters.entities.CategoryAdapter;
import com.project.hucemoney.databinding.ActivityListCategoryBinding;
import com.project.hucemoney.entities.Account;
import com.project.hucemoney.entities.Category;
import com.project.hucemoney.viewmodels.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListCategoryActivity extends AppCompatActivity {

    private ActivityListCategoryBinding binding;
    private CategoryViewModel categoryViewModel;
    private CategoryAdapter categoryAdapter;
    private RecyclerView recyclerView;
    private List<Category> categories = new ArrayList<>();
    private Category categorySelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_category);
        init();
        initRecyclerView();
        controlAction();
        observer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.unbind();
        categoryViewModel.getCategories().removeObservers(this);
    }

    private void init() {
        Intent intent = getIntent();
        boolean type = intent.getBooleanExtra("type", false);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryAdapter = new CategoryAdapter(this, categories);
        categoryViewModel.loadCategories(type,null);
        binding.setCategoryViewModel(categoryViewModel);
        binding.setLifecycleOwner(this);
    }

    private void initRecyclerView() {
        recyclerView = binding.rvListCategory;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(categoryAdapter);
    }

    private void controlAction() {
        binding.btnClose.setOnClickListener(v -> finish());
        categoryAdapter.setOnItemClickListener((category, position) -> {
            Intent intent = new Intent();
            intent.putExtra("categorySelected", category);
            intent.putExtra("position", position);
            intent.putExtra("isCategory", true);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    private void observer() {
        categoryViewModel.getCategories().observe(this, categories -> {
            categoryAdapter.setData(categories);
            if (categories == null || categories.size() == 0) {
                binding.tvNotifyNoData.setVisibility(View.VISIBLE);
            } else {
                Intent intent = getIntent();
                if (intent != null) {
                    categorySelected = intent.getParcelableExtra("categorySelected");
                    if (categorySelected != null) {
                        for (Category category : categories) {
                            if (category.getUUID().equals(categorySelected.getUUID())) {
                                categoryAdapter.setPositionSelected(categories.indexOf(category));
                                break;
                            }
                        }
                    }
                }
                binding.tvNotifyNoData.setVisibility(View.GONE);
            }
        });
    }
}