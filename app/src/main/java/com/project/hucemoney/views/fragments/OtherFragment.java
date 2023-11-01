package com.project.hucemoney.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.project.hucemoney.databinding.FragmentOtherBinding;
import com.project.hucemoney.utils.FunctionUtils;
import com.project.hucemoney.utils.SessionManager;
import com.project.hucemoney.views.activities.LauncherActivity;

public class OtherFragment extends Fragment {

    private FragmentOtherBinding binding;
    SessionManager sessionManager;

    public OtherFragment() {
        // Required empty public constructor
    }

    public static OtherFragment newInstance() {
        OtherFragment fragment = new OtherFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOtherBinding.inflate(inflater, container, false);
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
        binding = null;
    }

    private void init(){
        sessionManager = new SessionManager(getContext());
    }

    private void controlAction() {
        binding.btnLogout.setOnClickListener(v -> {
            SessionManager sessionManager = new SessionManager(getContext());
            sessionManager.logoutUser();
            Intent intent = new Intent(getActivity(), LauncherActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        binding.radioLgEnglish.setOnClickListener(v -> {
            if (binding.radioLgEnglish.isChecked()) {
                binding.radioLgVietnamese.setChecked(false);
            }
        });

        binding.radioLgVietnamese.setOnClickListener(v -> {
            if (binding.radioLgVietnamese.isChecked()) {
                binding.radioLgEnglish.setChecked(false);
            }
        });

        binding.btnSync.setOnClickListener(v -> {
            sessionManager.setSyncDate();
            fillData();
        });
    }

    private void fillData() {
        String username = sessionManager.getUsername();
        binding.tvUsername.setText(username);
        String email = sessionManager.getEmail();
        binding.tvEmail.setText(email);
        String dateSync = sessionManager.getSyncDate();
        if (dateSync != null) {
            binding.tvDateSync.setText(String.format("Đồng bộ lần cuối lúc: %s", dateSync));
        }
        String versionName = FunctionUtils.getAppVersionName(getContext());
        binding.tvVersionName.setText(String.format("Phiên bản: %s", versionName));
    }
}