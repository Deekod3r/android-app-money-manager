package com.project.hucemoney.repositories;

import androidx.lifecycle.LiveData;
import androidx.room.Transaction;

import com.project.hucemoney.DAOs.BudgetDAO;
import com.project.hucemoney.DAOs.TransactionDAO;
import com.project.hucemoney.database.AppDatabase;
import com.project.hucemoney.entities.Budget;
import com.project.hucemoney.entities.pojo.BudgetWithCategory;
import com.project.hucemoney.models.requests.BudgetAddRequest;
import com.project.hucemoney.models.requests.BudgetEditRequest;

import java.util.List;
import java.util.Objects;

public class BudgetRepository {
    private final BudgetDAO budgetDAO;
    private final TransactionDAO transactionDAO;
    private AppDatabase appDatabase;

    public BudgetRepository(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
        this.budgetDAO = appDatabase.budgetDAO();
        this.transactionDAO = appDatabase.transactionDAO();
    }

    @Transaction
    public Budget create(BudgetAddRequest budgetAddRequest) {
        try {
            Budget check = budgetDAO.findCurrentBudgetForCategory(budgetAddRequest.getCategory());
            if (check != null && !(budgetAddRequest.getStartDate().isAfter(check.getEndDate()) || budgetAddRequest.getEndDate().isBefore(check.getStartDate()))) {
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
            List<com.project.hucemoney.entities.Transaction> transactions = transactionDAO.findTransactionsByCategoryAndCurrentBudget(budget.getCategory(), budget.getStartDate(), budget.getEndDate());
            long currentBalance = 0L;
            if (transactions != null && transactions.size() > 0) {
                for (com.project.hucemoney.entities.Transaction transaction : transactions) {
                    currentBalance += transaction.getAmount();
                    transaction.setBudget(budget.getUUID());
                    transactionDAO.update(transaction);
                }
            }
            budget.setCurrentBalance(currentBalance);
            long rowID = budgetDAO.save(budget);
            if (rowID <= 0) {
                throw new RuntimeException("Thêm hạn mức thất bại");
            }
            return budget;
        } catch (Exception e) {
            throw e;
        }
    }

    public Budget getByUUID(String uuid) {
        try {
            return budgetDAO.findByUuid(uuid);
        } catch (Exception e) {
            throw e;
        }
    }

    public LiveData<List<BudgetWithCategory>> getAll(String user) {
        try {
            return budgetDAO.findAll(user);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transaction
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

    @Transaction
    public Budget update(BudgetEditRequest budgetEditRequest) {
        try {
            Budget budget = budgetDAO.findByUuid(budgetEditRequest.getUUID());
            if (budget == null) {
                throw new RuntimeException("Hạn mức không tồn tại");
            }
            Budget check = budgetDAO.findCurrentBudgetForCategory(budgetEditRequest.getCategory());
            if (check != null && !Objects.equals(check.getCategory(), budget.getCategory())) {
                throw new RuntimeException("Hạn mức cho danh mục này đang tồn tại trong thời hạn");
            }
            budget.setName(budgetEditRequest.getName());
            budget.setStartDate(budgetEditRequest.getStartDate());
            budget.setEndDate(budgetEditRequest.getEndDate());
            budget.setInitialLimit(budgetEditRequest.getInitialLimit());
            budget.setCurrentBalance(budgetEditRequest.getCurrentBalance());
            budget.setCategory(budgetEditRequest.getCategory());
            budget.setNote(budgetEditRequest.getNote());
            List<com.project.hucemoney.entities.Transaction> transactions = transactionDAO.findTransactionsByBudget(budget.getUUID());
            if (transactions != null && transactions.size() > 0) {
                for (com.project.hucemoney.entities.Transaction transaction : transactions) {
                    if (transaction.getDate().isBefore(budget.getStartDate()) || transaction.getDate().isAfter(budget.getEndDate())) {
                        transaction.setBudget(null);
                        transactionDAO.update(transaction);
                        budget.setCurrentBalance(budget.getCurrentBalance() - transaction.getAmount());
                    }
                }
            }
            transactions = transactionDAO.findTransactionsByCategoryAndCurrentBudget(budget.getCategory(), budget.getStartDate(), budget.getEndDate());
            if (transactions != null && transactions.size() > 0) {
                for (com.project.hucemoney.entities.Transaction transaction : transactions) {
                    if (transaction.getBudget() == null) {
                        transaction.setBudget(budget.getUUID());
                        transactionDAO.update(transaction);
                        budget.setCurrentBalance(budget.getCurrentBalance() + transaction.getAmount());
                    }
                }
            }
            long rowID = budgetDAO.update(budget);
            if (rowID <= 0) {
                throw new RuntimeException("Cập nhật hạn mức thất bại");
            }
            return budget;
        } catch (Exception e) {
            throw e;
        }
    }

    public Budget getCurrentBudgetForCategory(String category) {
        try {
            return budgetDAO.findCurrentBudgetForCategory(category);
        } catch (Exception e) {
            throw e;
        }
    }
}
