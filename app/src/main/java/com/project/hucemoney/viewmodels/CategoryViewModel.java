package com.project.hucemoney.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.project.hucemoney.repositories.CategoryRepository;

public class CategoryViewModel extends AndroidViewModel {

    private CategoryRepository categoryRepository;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        this.categoryRepository = new CategoryRepository(application);
    }
}
