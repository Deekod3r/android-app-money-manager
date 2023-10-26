package com.project.hucemoney.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.project.hucemoney.R;
import com.project.hucemoney.databinding.ActivityListCategoryBinding;

public class ListCategoryActivity extends AppCompatActivity {

    private ActivityListCategoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_category);
    }
}