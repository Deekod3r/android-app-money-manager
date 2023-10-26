package com.project.hucemoney.viewmodels;

import static com.project.hucemoney.utils.ValidationUtils.isNullOrEmpty;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.project.hucemoney.R;
import com.project.hucemoney.common.ResponseCode;
import com.project.hucemoney.entities.Account;
import com.project.hucemoney.entities.Category;
import com.project.hucemoney.models.Response;
import com.project.hucemoney.models.requests.CategoryAddRequest;
import com.project.hucemoney.models.requests.CategoryEditRequest;
import com.project.hucemoney.repositories.CategoryRepository;
import com.project.hucemoney.utils.SessionManager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    private MutableLiveData<List<Category>> categoriesLiveData = new MutableLiveData<>();
    private MutableLiveData<Response<Category>> resultAddCategory = new MutableLiveData<>();
    private MutableLiveData<Response<Category>> resultEditCategory = new MutableLiveData<>();
    private MutableLiveData<Response<Boolean>> resultDeleteCategory = new MutableLiveData<>();
    private CategoryAddRequest categoryAddRequest = new CategoryAddRequest();
    private CategoryEditRequest categoryEditRequest = new CategoryEditRequest();
    private CategoryRepository categoryRepository;
    private SessionManager sessionManager;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        this.categoryRepository = new CategoryRepository(application);
        sessionManager = new SessionManager(application);
    }

    public void addCategory() {
        Response<Category> response = new Response<>();
        try {
            if (isNullOrEmpty(categoryAddRequest.getName())) {
                response.setMessage("Tên danh mục không được để trống");
                resultAddCategory.setValue(response);
                return;
            }
            categoryAddRequest.setUser(sessionManager.getUUID());
            Category category = categoryRepository.create(categoryAddRequest);
            if (category != null) {
                response.setStatus(ResponseCode.SUCCESS);
                response.setMessage("Thêm danh mục thành công");
                response.setData(category);
            } else {
                response.setMessage("Thêm danh mục thất bại");
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
        }
        resultAddCategory.setValue(response);
    }

    public void editCategory() {
        Response<Category> response = new Response<>();
        try {
            if (isNullOrEmpty(categoryEditRequest.getName())) {
                response.setMessage("Tên danh mục không được để trống");
                resultEditCategory.setValue(response);
                return;
            }
            Category category = categoryRepository.update(categoryEditRequest);
            if (category != null) {
                response.setStatus(ResponseCode.SUCCESS);
                response.setMessage("Sửa danh mục thành công");
                response.setData(category);
            } else {
                response.setMessage("Sửa danh mục thất bại");
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
        }
        resultEditCategory.setValue(response);
    }

    public void deleteCategory() {
        Response<Boolean> response = new Response<>();
        try {
            boolean result = categoryRepository.delete(categoryEditRequest.getUUID());
            if (result) {
                response.setStatus(ResponseCode.SUCCESS);
                response.setMessage("Xóa danh mục thành công");
                response.setData(true);
            } else {
                response.setMessage("Xóa danh mục thất bại");
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
        }
        resultDeleteCategory.setValue(response);
    }

    public void loadCategories(boolean type, @Nullable String name, @Nullable String parent) {
        try {
            LiveData<List<Category>> categories = categoryRepository.getAll(sessionManager.getUUID(), type, null, null);
            categories.observeForever(ctg -> {
                categoriesLiveData.setValue(ctg);
                //accounts.removeObservers(this);
            });
        } catch (Exception e) {
            Log.e("CategoryViewModel", e.getMessage());
        }
    }

    public void clearCategories() {
        categoriesLiveData.setValue(new ArrayList<>());
    }

    public LiveData<List<Category>> getCategories() {
        return categoriesLiveData;
    }
    public LiveData<Response<Category>> getResultAddCategory() {
        return resultAddCategory;
    }
    public LiveData<Response<Category>> getResultEditCategory() {
        return resultEditCategory;
    }
    public LiveData<Response<Boolean>> getResultDeleteCategory() {
        return resultDeleteCategory;
    }
    public CategoryAddRequest getCategoryAddRequest() {
        return categoryAddRequest;
    }

    public CategoryEditRequest getCategoryEditRequest() {
        return categoryEditRequest;
    }

}
