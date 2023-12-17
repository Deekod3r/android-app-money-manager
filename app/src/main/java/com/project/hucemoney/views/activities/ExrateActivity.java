package com.project.hucemoney.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.project.hucemoney.R;
import com.project.hucemoney.adapters.entities.CategoryAdapter;
import com.project.hucemoney.adapters.entities.ExrateAdapter;
import com.project.hucemoney.common.enums.DialogType;
import com.project.hucemoney.databinding.ActivityExrateBinding;
import com.project.hucemoney.entities.Category;
import com.project.hucemoney.entities.Exrate;
import com.project.hucemoney.utils.FunctionUtils;
import com.project.hucemoney.utils.NetworkUtils;
import com.project.hucemoney.viewmodels.CategoryViewModel;
import com.project.hucemoney.viewmodels.ExrateViewModel;

import java.util.ArrayList;
import java.util.List;

public class ExrateActivity extends AppCompatActivity {

    private ActivityExrateBinding binding;
    private ExrateViewModel exrateViewModel;
    private List<Exrate> exrates = new ArrayList<>();
    private List<Exrate> exratesFilter = new ArrayList<>();
    private ExrateAdapter exrateAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exrate);
        init();
        initRecyclerView();
        controlAction();
        observer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.unbind();
    }

    private void init() {
        exrateViewModel = new ViewModelProvider(this).get(ExrateViewModel.class);
        exrateAdapter = new ExrateAdapter(this, exrates, true);
        if (NetworkUtils.isNetworkAvailable(this)) {
            exrateViewModel.fetchData();
        } else {
            FunctionUtils.showDialogNotify(this, "", "Vui lòng kết nối mạng để xem thông tin về tỷ giá", DialogType.INFO);
        }
        binding.setExrateViewModel(exrateViewModel);
        binding.setLifecycleOwner(this);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = binding.rvExrate;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(exrateAdapter);
    }

    private void controlAction() {
        binding.btnClose.setOnClickListener(v -> finish());
        binding.edtMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    binding.edtMoney.setText("0");
                } else {
                    if (s.length() > 1 && s.charAt(0) == '0') {
                        binding.edtMoney.setText(s.subSequence(1, s.length()));
                    }
                    if (!s.toString().isEmpty()) {
                        //
                    }
                }
                binding.edtMoney.setSelection(binding.edtMoney.getText().length());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!exrates.isEmpty()) {
                    exratesFilter.clear();
                    for (Exrate exrate : exrates) {
                        if (exrate.getCurrencyCode().toLowerCase().contains(newText.trim().toLowerCase()) || exrate.getCurrencyName().toLowerCase().contains(newText.trim().toLowerCase())) {
                            exratesFilter.add(exrate);
                        }
                    }
                    exrateAdapter.setData(exratesFilter);
                }
                return false;
            }
        });

        binding.btnChangeType.setOnClickListener(v -> {
            exrateAdapter.changeType(!exrateAdapter.getType());
        });
    }

    private void observer() {
        exrateViewModel.getDateTimeLiveData().observe(this, dateTime -> {
            if (dateTime != null) {
                binding.tvNotifyData.setText(dateTime);
            }
        });
        exrateViewModel.getExrateListLiveData().observe(this, exrateList -> {
            if (exrateList != null) {
                exrates = exrateList;
                exrateAdapter.setData(exrateList);
            }
        });
    }
}