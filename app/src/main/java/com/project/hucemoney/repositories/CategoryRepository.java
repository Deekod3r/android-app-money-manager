package com.project.hucemoney.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.project.hucemoney.DAOs.CategoryDAO;
import com.project.hucemoney.database.AppDatabase;
import com.project.hucemoney.entities.Category;
import com.project.hucemoney.models.requests.CategoryAddRequest;
import com.project.hucemoney.models.requests.CategoryEditRequest;

import java.util.List;

public class CategoryRepository {
    private CategoryDAO categoryDAO;
    public CategoryRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getDatabase(application);
        this.categoryDAO = appDatabase.categoryDAO();
    }

    public Category create(CategoryAddRequest CategoryAddRequest) {
        try {
            Category category = new Category();
            category.setUUID(java.util.UUID.randomUUID().toString());
            category.setId("category" + System.currentTimeMillis());
            category.setName(CategoryAddRequest.getName());
            category.setNote(CategoryAddRequest.getNote());
            category.setUser(CategoryAddRequest.getUser());
            category.setParent(CategoryAddRequest.getParent());
            category.setType(CategoryAddRequest.isType());
            long rowID = categoryDAO.save(category);
            if (rowID <= 0) {
                throw new RuntimeException("Thêm danh mục thất bại");
            }
            return category;
        } catch (Exception e) {
            throw e;
        }
    }

    public Category findByUUID(String uuid) {
        try {
            return categoryDAO.findByUuid(uuid);
        } catch (Exception e) {
            throw e;
        }
    }

    public LiveData<List<Category>> getAll(String user) {
        try {
            if (user == null) {
                return categoryDAO.findAll();
            }
            return categoryDAO.findAllByUser(user);
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean delete(String uuid) {
        try {
            if (!categoryDAO.isExists(uuid)) {
                throw new RuntimeException("Danh mục không tồn tại");
            }
            Category Category = categoryDAO.findByUuid(uuid);
            return categoryDAO.delete(Category) > 0;
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean isUserExists(String uuid) {
        try {
            return categoryDAO.isExists(uuid);
        } catch (Exception e) {
            throw e;
        }
    }

    public Category update(CategoryEditRequest CategoryEditRequest) {
        try {
            Category category = categoryDAO.findByUuid(CategoryEditRequest.getUUID());
            if (category == null) {
                throw new RuntimeException("Tài khoản không tồn tại");
            }
            category.setName(CategoryEditRequest.getName());
            category.setParent(CategoryEditRequest.getParent());
            category.setNote(CategoryEditRequest.getNote());
            long rowID = categoryDAO.update(category);
            if (rowID <= 0) {
                throw new RuntimeException("Cập nhật tài khoản thất bại");
            }
            return category;
        } catch (Exception e) {
            throw e;
        }
    }
}
