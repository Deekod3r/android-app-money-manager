package com.project.hucemoney.repositories;

import androidx.lifecycle.LiveData;

import com.project.hucemoney.DAOs.TransactionDAO;
import com.project.hucemoney.DAOs.TransactionGoalDAO;
import com.project.hucemoney.database.AppDatabase;
import com.project.hucemoney.entities.Account;
import com.project.hucemoney.entities.Budget;
import com.project.hucemoney.entities.Transaction;
import com.project.hucemoney.entities.pojo.TransactionWithCategoryAndAccount;
import com.project.hucemoney.models.requests.AccountEditRequest;
import com.project.hucemoney.models.requests.BudgetEditRequest;
import com.project.hucemoney.models.requests.TransactionAddRequest;
import com.project.hucemoney.models.requests.TransactionEditRequest;

import java.util.List;

public class TransactionRepository {
    private AccountRepository accountRepository;
    private BudgetRepository budgetRepository;
    private CategoryRepository categoryRepository;
    private final TransactionDAO transactionDAO;
    private final TransactionGoalDAO transactionGoalDAO;
    private AppDatabase appDatabase;

    public TransactionRepository(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
        this.accountRepository = new AccountRepository(appDatabase);
        this.categoryRepository = new CategoryRepository(appDatabase);
        this.budgetRepository = new BudgetRepository(appDatabase);
        this.transactionDAO = appDatabase.transactionDAO();
        this.transactionGoalDAO = appDatabase.transactionGoalDAO();
    }

    public Transaction create(TransactionAddRequest transactionAddRequest) {
        try {
            Account account = accountRepository.findByUUID(transactionAddRequest.getAccount());
            if(account == null) {
                throw new RuntimeException("Tài khoản không tồn tại");
            }
            if(categoryRepository.findByUUID(transactionAddRequest.getCategory()) == null) {
                throw new RuntimeException("Danh mục không tồn tại");
            }
            Transaction transaction = new Transaction();
            transaction.setUUID(java.util.UUID.randomUUID().toString());
            transaction.setId("transaction" + System.currentTimeMillis());
            transaction.setName(transactionAddRequest.getName());
            transaction.setAmount(transactionAddRequest.getAmount());
            transaction.setCategory(transactionAddRequest.getCategory());
            transaction.setAccount(transactionAddRequest.getAccount());
            transaction.setDate(transactionAddRequest.getDate());
            transaction.setType(transactionAddRequest.getType());
            transaction.setNote(transactionAddRequest.getNote());
            long rowID = transactionDAO.save(transaction);
            if (rowID <= 0) {
                throw new RuntimeException("Thêm giao dịch thất bại");
            }
            if(transactionAddRequest.getType()) {
                account.setAmount(account.getAmount() + transactionAddRequest.getAmount());
            } else {
                account.setAmount(account.getAmount() - transactionAddRequest.getAmount());
            }
            accountRepository.update(AccountEditRequest.of(account.getUUID(), account.getName(), account.getAmount(), account.getNote()));
            Budget budget = budgetRepository.getCurrentBudgetForCategory(transactionAddRequest.getCategory());
            if (budget != null) {
                budget.setCurrentBalance(budget.getCurrentBalance() + transactionAddRequest.getAmount());
                budgetRepository.update(BudgetEditRequest.of(budget.getUUID(), budget.getName(), budget.getStartDate(), budget.getEndDate(), budget.getInitialLimit(), budget.getCurrentBalance(), budget.getCategory(), budget.getNote()));
            }
            return transaction;
        } catch (Exception e) {
            throw e;
        }
    }

    public Transaction findByUUID(String uuid) {
        try {
            return transactionDAO.findByUuid(uuid);
        } catch (Exception e) {
            throw e;
        }
    }

    public LiveData<List<TransactionWithCategoryAndAccount>> getAll(String user) {
        try {
            return transactionDAO.findAll(user);
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean delete(String uuid) {
        try {
            if (!transactionDAO.isExists(uuid)) {
                throw new RuntimeException("Giao dịch không tồn tại");
            }
            Transaction transaction = transactionDAO.findByUuid(uuid);
            return transactionDAO.delete(transaction) > 0;
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean isUserExists(String uuid) {
        try {
            return transactionDAO.isExists(uuid);
        } catch (Exception e) {
            throw e;
        }
    }

    public Transaction update(TransactionEditRequest transactionEditRequest) {
        try {
            Transaction transaction = transactionDAO.findByUuid(transactionEditRequest.getUUID());
            if (transaction == null) {
                throw new RuntimeException("Giao dịch không tồn tại");
            }
            long rowID = transactionDAO.update(transaction);
            if (rowID <= 0) {
                throw new RuntimeException("Cập nhật giao dịch thất bại");
            }
            return transaction;
        } catch (Exception e) {
            throw e;
        }
    }
}
