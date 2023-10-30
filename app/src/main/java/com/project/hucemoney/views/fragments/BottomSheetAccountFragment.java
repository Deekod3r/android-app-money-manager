package com.project.hucemoney.views.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.project.hucemoney.common.Constants;
import com.project.hucemoney.common.ResponseCode;
import com.project.hucemoney.common.enums.DialogType;
import com.project.hucemoney.databinding.FragmentBottomSheetAccountBinding;
import com.project.hucemoney.entities.Account;
import com.project.hucemoney.entities.Category;
import com.project.hucemoney.models.Response;
import com.project.hucemoney.utils.FunctionUtils;
import com.project.hucemoney.viewmodels.AccountViewModel;
import com.project.hucemoney.views.activities.EditAccountActivity;

import java.util.Objects;

public class BottomSheetAccountFragment extends BottomSheetDialogFragment {

    private FragmentBottomSheetAccountBinding binding;
    private Account account;
    private int position;
    private AccountViewModel accountViewModel;
    private ActivityResultLauncher<Intent> mLauncher;

    public BottomSheetAccountFragment() {
        // Required empty public constructor
    }

    public static BottomSheetAccountFragment newInstance(Account account, int position) {
        BottomSheetAccountFragment fragment = new BottomSheetAccountFragment();
        Bundle args = new Bundle();
        args.putParcelable("account", account);
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            account = getArguments().getParcelable("account");
            position = getArguments().getInt("position");
        }
        mLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    try {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data == null) {
                                Toast.makeText(getContext(), "Có lỗi xảy ra. Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Account account = data.getParcelableExtra("accountEdited");
                            accountViewModel.editAccountLiveData(account, position);
                        }
                        dismiss();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Có lỗi xảy ra. Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                    }
                }
        );
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void init() {
        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);
        binding.setAccountViewModel(accountViewModel);
        binding.setLifecycleOwner(this);
    }

    private void controlAction() {
        binding.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditAccountActivity.class);
            intent.putExtra("account", account);
            mLauncher.launch(intent);
        });
        binding.btnDelete.setOnClickListener(v -> {
            FunctionUtils.showDialogConfirm(getContext(), "", "Bạn có chắc chắn muốn xóa tài khoản này không?", DialogType.WARNING,
                    (dialog, which) -> {
                        accountViewModel.deleteAccount(account);
                        Observer<Response<Boolean>> observer = new Observer<Response<Boolean>>() {
                            @Override
                            public void onChanged(Response<Boolean> response) {
                                if (response != null) {
                                    if (response.getStatus().equals(ResponseCode.SUCCESS)) {
                                        Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                                        accountViewModel.deleteAccountLiveData(position);
                                    }
                                    dismiss();
                                }
                                accountViewModel.getResultDeleteAccount().removeObserver(this);
                            }
                        };
                        accountViewModel.getResultDeleteAccount().observe(getViewLifecycleOwner(), observer);

                    }, (dialog, which) -> {
                        dismiss();
                    });
            Objects.requireNonNull(getDialog()).hide();
        });
    }
}