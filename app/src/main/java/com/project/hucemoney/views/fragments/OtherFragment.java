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


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OtherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OtherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OtherFragment newInstance(String param1, String param2) {
        OtherFragment fragment = new OtherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
    }

    private void fillData() {
        SessionManager sessionManager = new SessionManager(getContext());
        String username = sessionManager.getUsername();
        binding.tvUsername.setText(username);
        String email = sessionManager.getEmail();
        binding.tvEmail.setText(email);
//        String dateSync = prefs.getString("dateSync", null);
//        if (dateSync != null) {
//            binding.tvDateSync.setText(String.format("Đồng bộ lần cuối lúc: %s", dateSync));
//        }
        String versionName = FunctionUtils.getAppVersionName(getContext());
        binding.tvVersionName.setText(String.format("Phiên bản: %s", versionName));
    }
}