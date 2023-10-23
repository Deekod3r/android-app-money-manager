package com.project.hucemoney.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.project.hucemoney.common.enums.DialogType;
import com.project.hucemoney.databinding.FragmentBottomSheetAccountBinding;
import com.project.hucemoney.entities.Account;
import com.project.hucemoney.utils.FunctionUtils;
import com.project.hucemoney.viewmodels.AccountViewModel;
import com.project.hucemoney.views.activities.EditAccountActivity;

public class BottomSheetAccountFragment extends BottomSheetDialogFragment {

    private FragmentBottomSheetAccountBinding binding;
    private Account account;
    private int position;
    private AccountViewModel accountViewModel;

    public BottomSheetAccountFragment() {
        // Required empty public constructor
    }

    public BottomSheetAccountFragment(Account account, int position) {
        this.account = account;
        this.position = position;
    }

    public static BottomSheetAccountFragment newInstance(String param1, String param2) {
        BottomSheetAccountFragment fragment = new BottomSheetAccountFragment();
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
        binding = FragmentBottomSheetAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        controlAction();
        observe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void init() {
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        binding.setAccountViewModel(accountViewModel);
        binding.setLifecycleOwner(this);
    }

    private void controlAction() {
        binding.btnEditAccount.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditAccountActivity.class);
            intent.putExtra("account", account);
            intent.putExtra("position", position);
            startActivity(intent);
            this.dismiss();
        });
        binding.btnDeleteAccount.setOnClickListener(v -> {
            FunctionUtils.showDialogConfirm(getContext(), "", "Bạn có chắc chắn muốn xóa tài khoản này không?", DialogType.WARNING,
                    (dialog, which) -> {
                        accountViewModel.deleteAccount(account);
                        dialog.dismiss();
                        this.dismiss();
                    }, (dialog, which) -> {
                        dialog.dismiss();
                        this.dismiss();
                    });
            getDialog().hide();
        });
    }

    private void observe() {
        accountViewModel.getResultDeleteAccount().observe(getViewLifecycleOwner(), response -> {
            Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
            dismiss();
        });
    }
}