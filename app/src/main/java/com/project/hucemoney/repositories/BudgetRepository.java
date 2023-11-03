package com.project.hucemoney.repositories;

import androidx.lifecycle.LiveData;

import com.project.hucemoney.DAOs.BudgetDAO;
import com.project.hucemoney.database.AppDatabase;
import com.project.hucemoney.entities.Budget;
import com.project.hucemoney.models.requests.BudgetAddRequest;
import com.project.hucemoney.models.requests.BudgetEditRequest;

import java.util.List;

public class BudgetRepository {
    private final BudgetDAO budgetDAO;
    private AppDatabase appDatabase;

    public BudgetRepository(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
        this.budgetDAO = appDatabase.budgetDAO();
    }

    public Budget create(BudgetAddRequest budgetAddRequest) {
        try {
            Budget check = budgetDAO.GetCurrentBudgetForCategory(budgetAddRequest.getCategory());
            if (check != null) {
                throw new RuntimeException("Hạn mức cho danh mục này đang tồn tại trong thời hạn");
            }
            Budget budget = new Budget();
            budget.setUUID(java.util.UUID.randomUUID().toString());
            budget.setId("budget" + System.currentTimeMillis());
            budget.setName(budgetAddRequest.getName());
            budget.setStartDate(budgetAddRequest.getStartDate());
            budget.setEndDate(budgetAddRequest.getEndDate());
            budget.setInitialLimit(budgetAddRequest.getInitialLimit());
            budget.setCategory(budgetAddRequest.getCategory());
            budget.setNote(budgetAddRequest.getNote());
            long rowID = budgetDAO.save(budget);
            if (rowID <= 0) {
                throw new RuntimeException("Thêm hạn mức thất bại");
            }
            return budget;
        } catch (Exception e) {
            throw e;
        }
    }

    public Budget findByUUID(String uuid) {
        try {
            return budgetDAO.findByUuid(uuid);
        } catch (Exception e) {
            throw e;
        }
    }

    public LiveData<List<Budget>> getAll(String user) {
        try {
            return budgetDAO.findAll(user);
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean delete(String uuid) {
        try {
            if (!budgetDAO.isExists(uuid)) {
                throw new RuntimeException("Hạn mức không tồn tại");
            }
            Budget budget = budgetDAO.findByUuid(uuid);
            return budgetDAO.delete(budget) > 0;
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean isUserExists(String uuid) {
        try {
            return budgetDAO.isExists(uuid);
        } catch (Exception e) {
            throw e;
        }
    }

    public Budget update(BudgetEditRequest budgetEditRequest) {
        try {
            Budget budget = budgetDAO.findByUuid(budgetEditRequest.getUUID());
            if (budget == null) {
                throw new RuntimeException("hạn mức không tồn tại");
            }
            budget.setName(budgetEditRequest.getName());
            long rowID = budgetDAO.update(budget);
            if (rowID <= 0) {
                throw new RuntimeException("Cập nhật hạn mức thất bại");
            }
            return budget;
        } catch (Exception e) {
            throw e;
        }
    }
}
