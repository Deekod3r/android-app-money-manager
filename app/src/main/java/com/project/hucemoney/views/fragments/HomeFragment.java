package com.project.hucemoney.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.hucemoney.R;
import com.project.hucemoney.common.enums.DialogType;
import com.project.hucemoney.database.AppDatabase;
import com.project.hucemoney.databinding.FragmentHomeBinding;
import com.project.hucemoney.entities.Exrate;
import com.project.hucemoney.repositories.AccountRepository;
import com.project.hucemoney.repositories.CategoryRepository;
import com.project.hucemoney.utils.FunctionUtils;
import com.project.hucemoney.utils.SessionManager;
import com.project.hucemoney.viewmodels.AccountViewModel;
import com.project.hucemoney.viewmodels.ExrateViewModel;
import com.project.hucemoney.views.activities.AppActivity;
import com.project.hucemoney.views.activities.BudgetActivity;
import com.project.hucemoney.views.activities.CategoryActivity;
import com.project.hucemoney.views.activities.ExrateActivity;
import com.project.hucemoney.views.activities.GoalActivity;
import com.project.hucemoney.views.activities.LauncherActivity;
import com.project.hucemoney.views.activities.TaxActivity;
import com.project.hucemoney.views.activities.TransactionActivity;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private boolean previewSumMoney = true;
    private AccountViewModel accountViewModel;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding  = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        controlAction();
        fillData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.unbind();
    }

    private void init() {
        SessionManager sessionManager = new SessionManager(requireContext());
        boolean isFirstLogin = sessionManager.isFirstLogin(sessionManager.getUUID());
        if (isFirstLogin) {
            sessionManager.setFirstLogin(sessionManager.getUUID(), false);
            CategoryRepository categoryRepository = new CategoryRepository(AppDatabase.getDatabase(requireContext()));
            categoryRepository.insertDefaultCategory(sessionManager.getUUID());
            AccountRepository accountRepository = new AccountRepository(AppDatabase.getDatabase(requireContext()));
            accountRepository.insertDefaultAccount(sessionManager.getUUID());
        }
        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);
        accountViewModel.loadAccounts();
        binding.setAccountViewModel(accountViewModel);
        binding.setLifecycleOwner(this);
    }

    private void controlAction() {
        binding.btnPreview.setOnClickListener(v -> {
            if (previewSumMoney) {
                binding.sumMoney.setText(String.format("%s %s", "******", getString(R.string.vi_currency)));
                binding.btnPreview.setImageResource(R.drawable.baseline_hide_black_24);
                previewSumMoney = false;
            } else {
                fillData();
                binding.btnPreview.setImageResource(R.drawable.baseline_show_black_24);
                previewSumMoney = true;
            }
        });

        binding.btnSync.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Đồng bộ thành công!", Toast.LENGTH_SHORT).show();
            v.animate().rotation(360).setDuration(2000).start();
        });

        binding.btnNotify.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Bạn không có thông báo nào!", Toast.LENGTH_SHORT).show();
        });

        binding.btnAddTransaction.setOnClickListener(v -> {
            AppActivity appActivity = (AppActivity) getActivity();
            if (appActivity != null) {
                appActivity.setSelectedNavItem(R.id.nav_transaction);
            }
        });

        binding.btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), TransactionActivity.class);
            startActivity(intent);
        });

        binding.btnAccount.setOnClickListener(v -> {
            AppActivity appActivity = (AppActivity) getActivity();
            if (appActivity != null) {
                appActivity.setSelectedNavItem(R.id.nav_account);
            }
        });

        binding.btnBudget.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), BudgetActivity.class);
            startActivity(intent);
        });

        binding.btnGoal.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), GoalActivity.class);
            startActivity(intent);
        });

        binding.btnCategory.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CategoryActivity.class);
            startActivity(intent);
        });

        binding.btnExchangeRate.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ExrateActivity.class);
            startActivity(intent);
        });

        binding.btnTax.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), TaxActivity.class);
            startActivity(intent);
        });

    }

    private void fillData() {
        accountViewModel.getAmountTotal().observe(getViewLifecycleOwner(), s -> {
            binding.sumMoney.setText(s);
        });

        SessionManager sessionManager = new SessionManager(requireContext());
        String username = sessionManager.getUsername();
        binding.hello.setText(String.format("Xin chào 👋 \n%s!", username.toUpperCase()));
    }
}