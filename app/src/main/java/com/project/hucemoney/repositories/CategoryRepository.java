package com.project.hucemoney.repositories;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.Transaction;

import com.project.hucemoney.DAOs.CategoryDAO;
import com.project.hucemoney.common.Constants;
import com.project.hucemoney.database.AppDatabase;
import com.project.hucemoney.entities.Category;
import com.project.hucemoney.models.requests.CategoryAddRequest;
import com.project.hucemoney.models.requests.CategoryEditRequest;

import java.util.List;

public class CategoryRepository {
    private CategoryDAO categoryDAO;
    private AppDatabase appDatabase;

    public CategoryRepository(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
        this.categoryDAO = appDatabase.categoryDAO();
    }

    @Transaction
    public void insertDefaultCategory(String user) {
        try {
            Category category = new Category();
            category.setUUID(java.util.UUID.randomUUID().toString());
            category.setId("category" + System.currentTimeMillis());
            category.setName("Ăn uống");
            category.setNote("Danh mục mặc định");
            category.setUser(user);
            category.setParent("0");
            category.setType(Constants.TYPE_EXPENSE);
            categoryDAO.save(category);

            category = new Category();
            category.setUUID(java.util.UUID.randomUUID().toString());
            category.setId("category" + System.currentTimeMillis());
            category.setName("Di chuyển");
            category.setNote("Danh mục mặc định");
            category.setUser(user);
            category.setParent("0");
            category.setType(Constants.TYPE_EXPENSE);
            categoryDAO.save(category);

            category = new Category();
            category.setUUID(java.util.UUID.randomUUID().toString());
            category.setId("category" + System.currentTimeMillis());
            category.setName("Mua sắm");
            category.setNote("Danh mục mặc định");
            category.setUser(user);
            category.setParent("0");
            category.setType(Constants.TYPE_EXPENSE);
            categoryDAO.save(category);

            category = new Category();
            category.setUUID(java.util.UUID.randomUUID().toString());
            category.setId("category" + System.currentTimeMillis());
            category.setName("Giải trí");
            category.setNote("Danh mục mặc định");
            category.setUser(user);
            category.setParent("0");
            category.setType(Constants.TYPE_EXPENSE);
            categoryDAO.save(category);

            category = new Category();
            category.setUUID(java.util.UUID.randomUUID().toString());
            category.setId("category" + System.currentTimeMillis());
            category.setName("Tiền lương");
            category.setNote("Danh mục mặc định");
            category.setUser(user);
            category.setParent("0");
            category.setType(Constants.TYPE_INCOME);
            categoryDAO.save(category);

        } catch (Exception e) {
            throw e;
        }
    }

    @Transaction
    public Category create(CategoryAddRequest categoryAddRequest) {
        try {
            Category check = categoryDAO.findByName(categoryAddRequest.getName());
            if (check != null && check.getType() == categoryAddRequest.getType()) {
                throw new RuntimeException("Tên danh mục đã tồn tại");
            }
            Category category = new Category();
            category.setUUID(java.util.UUID.randomUUID().toString());
            category.setId("category" + System.currentTimeMillis());
            category.setName(categoryAddRequest.getName());
            category.setNote(categoryAddRequest.getNote());
            category.setUser(categoryAddRequest.getUser());
            category.setParent(categoryAddRequest.getParent() == null ? "0" : categoryAddRequest.getParent());
            category.setType(categoryAddRequest.getType());
            long rowID = categoryDAO.save(category);
            if (rowID <= 0) {
                throw new RuntimeException("Thêm danh mục thất bại");
            }
            return category;
        } catch (Exception e) {
            throw e;
        }
    }

    public Category getByUUID(String uuid) {
        try {
            return categoryDAO.findByUuid(uuid);
        } catch (Exception e) {
            throw e;
        }
    }

    public LiveData<List<Category>> getAll(String user, boolean type, String name) {
        try {
            return categoryDAO.findAll(user, type, name);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transaction
    public boolean delete(String uuid) {
        try {
            Category category = categoryDAO.findByUuid(uuid);
            if (category == null) {
                throw new RuntimeException("Danh mục không tồn tại");
            }
            return categoryDAO.delete(category) > 0;
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

    @Transaction
    public Category update(CategoryEditRequest categoryEditRequest) {
        try {
            Category category = categoryDAO.findByUuid(categoryEditRequest.getUUID());
            if (category == null) {
                throw new RuntimeException("Danh mục không tồn tại");
            }
            category.setName(categoryEditRequest.getName());
            category.setNote(categoryEditRequest.getNote());
            long rowID = categoryDAO.update(category);
            if (rowID <= 0) {
                throw new RuntimeException("Cập nhật danh mục thất bại");
            }
            return category;
        } catch (Exception e) {
            throw e;
        }
    }
}
