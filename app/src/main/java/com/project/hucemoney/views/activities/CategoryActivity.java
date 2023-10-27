package com.project.hucemoney.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.project.hucemoney.R;
import com.project.hucemoney.adapters.entities.AccountAdapter;
import com.project.hucemoney.adapters.entities.CategoryAdapter;
import com.project.hucemoney.common.Constants;
import com.project.hucemoney.databinding.ActivityCategoryBinding;
import com.project.hucemoney.entities.Account;
import com.project.hucemoney.entities.Category;
import com.project.hucemoney.viewmodels.AccountViewModel;
import com.project.hucemoney.viewmodels.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private ActivityCategoryBinding binding;
    private List<Category> categories = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private CategoryViewModel categoryViewModel;
    private RecyclerView recyclerView;
    private ActivityResultLauncher<Intent> mLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category);
        init();
        initTabLayout();
        initRecyclerView();
        controlAction();
        observe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void init() {
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryAdapter = new CategoryAdapter(this, categories);
        categoryViewModel.loadCategories(binding.tabLayout.getSelectedTabPosition() != 0,null);
        binding.setCategoryViewModel(categoryViewModel);
        binding.setLifecycleOwner(this);
        mLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    try {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data == null) {
                                Toast.makeText(this, "Có lỗi xảy ra. Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String action = data.getStringExtra("action");
                            if (action == null) {
                                Toast.makeText(this, "Có lỗi xảy ra. Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            switch (action) {
                                case Constants.ACTION_ADD: {
                                    Category category = data.getParcelableExtra("categoryAdded");
                                    categoryViewModel.addCategoryLiveData(category);
                                    break;
                                }
                                case Constants.ACTION_EDIT: {
                                    Category category = data.getParcelableExtra("categoryEdited");
                                    int position = data.getIntExtra("position", -1);
                                    categoryViewModel.editCategoryLiveData(category, position);
                                    break;
                                }
                                case Constants.ACTION_DELETE: {
                                    int position = data.getIntExtra("position", -1);
                                    categories.remove(position);
                                    categoryViewModel.deleteCategoryLiveData(position);
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void initTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = binding.tabLayout.getSelectedTabPosition();
                switch (position) {
                    case 0:
                        binding.searchView.setQuery("", false);
                        categoryViewModel.loadCategories(false, null);
                        break;
                    case 1:
                        binding.searchView.setQuery("", false);
                        categoryViewModel.loadCategories(true, null);
                        break;
                    case 2:
                        binding.searchView.setQuery("", false);
                        //categoryViewModel.clearCategories();
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

    private void initRecyclerView() {
        RecyclerView recyclerView = binding.rvCategory;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(categoryAdapter);
    }

    private void controlAction() {

        binding.btnClose.setOnClickListener(v -> {
            finish();
        });

        binding.searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = "%" + newText + "%";
                categoryViewModel.loadCategories(binding.tabLayout.getSelectedTabPosition() != 0, newText);
                return false;
            }
        });

        categoryAdapter.setOnItemClickListener((category, position) -> {
            Intent intent = new Intent(CategoryActivity.this, EditCategoryActivity.class);
            intent.putExtra("category", category);
            intent.putExtra("position", position);
            intent.putExtra("type", binding.tabLayout.getSelectedTabPosition());
            mLauncher.launch(intent);
        });

        binding.btnAddCategory.setOnClickListener(v -> {
            Intent intent = new Intent(CategoryActivity.this, AddCategoryActivity.class);
            intent.putExtra("type", binding.tabLayout.getSelectedTabPosition());
            mLauncher.launch(intent);
        });
    }

    private void observe() {
        categoryViewModel.getCategories().observe(this, categories -> {
            categoryAdapter.setData(categories);
            if (categories == null || categories.size() == 0) {
                binding.tvNotifyNoData.setVisibility(View.VISIBLE);
            } else {
                binding.tvNotifyNoData.setVisibility(View.GONE);
            }
        });
    }
}