package com.project.hucemoney.repositories;

import static com.project.hucemoney.utils.ValidationUtils.isNullOrEmpty;

import androidx.lifecycle.LiveData;

import com.project.hucemoney.DAOs.TransactionDAO;
import com.project.hucemoney.DAOs.TransactionGoalDAO;
import com.project.hucemoney.database.AppDatabase;
import com.project.hucemoney.entities.Account;
import com.project.hucemoney.entities.Budget;
import com.project.hucemoney.entities.Transaction;
import com.project.hucemoney.entities.pojo.CategoryStatistic;
import com.project.hucemoney.entities.pojo.TimeSummary;
import com.project.hucemoney.entities.pojo.TransactionWithCategory;
import com.project.hucemoney.entities.pojo.TransactionWithCategoryAndAccount;
import com.project.hucemoney.models.requests.AccountEditRequest;
import com.project.hucemoney.models.requests.BudgetEditRequest;
import com.project.hucemoney.models.requests.TransactionAddRequest;
import com.project.hucemoney.models.requests.TransactionEditRequest;

import java.time.LocalDate;
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

    @androidx.room.Transaction
    public Transaction create(TransactionAddRequest transactionAddRequest) {
        try {
            Account account = accountRepository.findByUUID(transactionAddRequest.getAccount());
            if (account == null) {
                throw new RuntimeException("Tài khoản không tồn tại");
            }
            if (categoryRepository.getByUUID(transactionAddRequest.getCategory()) == null) {
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
            if (transactionAddRequest.getType()) {
                account.setAmount(account.getAmount() + transactionAddRequest.getAmount());
            } else {
                account.setAmount(account.getAmount() - transactionAddRequest.getAmount());
                Budget budget = budgetRepository.getCurrentBudgetForCategory(transactionAddRequest.getCategory());
                if (budget != null) {
                    if (transaction.getDate().isAfter(budget.getStartDate()) && transaction.getDate().isBefore(budget.getEndDate())) {
                        transaction.setBudget(budget.getUUID());
                        budget.setCurrentBalance(budget.getCurrentBalance() + transactionAddRequest.getAmount());
                        budgetRepository.update(BudgetEditRequest.of(budget.getUUID(), budget.getName(), budget.getStartDate(), budget.getEndDate(), budget.getInitialLimit(), budget.getCurrentBalance(), budget.getCategory(), budget.getNote()));
                    }
                }
            }
            accountRepository.update(AccountEditRequest.of(account.getUUID(), account.getName(), account.getAmount(), account.getNote()));
            long rowID = transactionDAO.save(transaction);
            if (rowID <= 0) {
                throw new RuntimeException("Thêm giao dịch thất bại");
            }
            return transaction;
        } catch (Exception e) {
            throw e;
        }
    }

    public Transaction getByUUID(String uuid) {
        try {
            return transactionDAO.findByUuid(uuid);
        } catch (Exception e) {
            throw e;
        }
    }

    public LiveData<List<TransactionWithCategoryAndAccount>> getAll(String user, String key) {
        try {
            if (isNullOrEmpty(key)) {
                key = "%%";
            }
            return transactionDAO.findAll(user, key);
        } catch (Exception e) {
            throw e;
        }
    }

    public LiveData<List<CategoryStatistic>> getAllByYear(String year, String user) {
        try {
            return transactionDAO.findTransactionWithCategoryByYear(year, user);
        } catch (Exception e) {
            throw e;
        }
    }

    public LiveData<List<CategoryStatistic>> getAllByMonth(String month, String user) {
        try {
            return transactionDAO.findTransactionWithCategoryByMonth(month, user);
        } catch (Exception e) {
            throw e;
        }
    }

    public LiveData<List<CategoryStatistic>> getAllByDay(String day, String user) {
        try {
            return transactionDAO.findTransactionWithCategoryByDay(day, user);
        } catch (Exception e) {
            throw e;
        }
    }

    @androidx.room.Transaction
    public boolean delete(String uuid) {
        try {
            if (!transactionDAO.isExists(uuid)) {
                throw new RuntimeException("Giao dịch không tồn tại");
            }
            Transaction transaction = transactionDAO.findByUuid(uuid);
            Account account = accountRepository.findByUUID(transaction.getAccount());
            if (account == null) {
                throw new RuntimeException("Tài khoản không tồn tại");
            }
            if (transaction.getType()) {
                account.setAmount(account.getAmount() - transaction.getAmount());
            } else {
                account.setAmount(account.getAmount() + transaction.getAmount());
            }
            accountRepository.update(AccountEditRequest.of(account.getUUID(), account.getName(), account.getAmount(), account.getNote()));
            Budget budget = budgetRepository.getByUUID(transaction.getBudget());
            if (budget != null) {
                budget.setCurrentBalance(budget.getCurrentBalance() - transaction.getAmount());
                budgetRepository.update(BudgetEditRequest.of(budget.getUUID(), budget.getName(), budget.getStartDate(), budget.getEndDate(), budget.getInitialLimit(), budget.getCurrentBalance(), budget.getCategory(), budget.getNote()));
            }
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

    @androidx.room.Transaction
    public Transaction update(TransactionEditRequest transactionEditRequest) {
        try {
            Transaction transaction = transactionDAO.findByUuid(transactionEditRequest.getUUID());
            if (transaction == null) {
                throw new RuntimeException("Giao dịch không tồn tại");
            }
            if (!transaction.getAccount().equals(transactionEditRequest.getAccount())) {
                Account account = accountRepository.findByUUID(transaction.getAccount());
                if (account == null) {
                    throw new RuntimeException("Tài khoản không tồn tại");
                }
                if (transactionEditRequest.getType()) {
                    account.setAmount(account.getAmount() - transaction.getAmount());
                } else {
                    account.setAmount(account.getAmount() + transaction.getAmount());
                }
                accountRepository.update(AccountEditRequest.of(account.getUUID(), account.getName(), account.getAmount(), account.getNote()));
            } else {
                Account account = accountRepository.findByUUID(transaction.getAccount());
                if (account == null) {
                    throw new RuntimeException("Tài khoản không tồn tại");
                }
                if (transactionEditRequest.getType()) {
                    account.setAmount(account.getAmount() - transaction.getAmount() + transactionEditRequest.getAmount());
                } else {
                    account.setAmount(account.getAmount() + transaction.getAmount() - transactionEditRequest.getAmount());
                }
                accountRepository.update(AccountEditRequest.of(account.getUUID(), account.getName(), account.getAmount(), account.getNote()));
            }
            if (!transaction.getType()) {
                if (!transaction.getCategory().equals(transactionEditRequest.getCategory()) || !transaction.getDate().isEqual(transactionEditRequest.getDate())) {
                    Budget oldBudget = budgetRepository.getByUUID(transaction.getBudget());
                    if (oldBudget != null) {
                        oldBudget.setCurrentBalance(oldBudget.getCurrentBalance() - transaction.getAmount());
                        budgetRepository.update(BudgetEditRequest.of(oldBudget.getUUID(), oldBudget.getName(), oldBudget.getStartDate(), oldBudget.getEndDate(), oldBudget.getInitialLimit(), oldBudget.getCurrentBalance(), oldBudget.getCategory(), oldBudget.getNote()));
                        transaction.setBudget(null);
                    }
                    Budget newBudget = budgetRepository.getCurrentBudgetForCategory(transactionEditRequest.getCategory());
                    if (newBudget != null) {
                        newBudget.setCurrentBalance(newBudget.getCurrentBalance() + transactionEditRequest.getAmount());
                        budgetRepository.update(BudgetEditRequest.of(newBudget.getUUID(), newBudget.getName(), newBudget.getStartDate(), newBudget.getEndDate(), newBudget.getInitialLimit(), newBudget.getCurrentBalance(), newBudget.getCategory(), newBudget.getNote()));
                        transaction.setBudget(newBudget.getUUID());
                    }
                } else {
                    Budget budget = budgetRepository.getByUUID(transaction.getBudget());
                    if (budget != null) {
                        budget.setCurrentBalance(budget.getCurrentBalance() - transaction.getAmount() + transactionEditRequest.getAmount());
                        budgetRepository.update(BudgetEditRequest.of(budget.getUUID(), budget.getName(), budget.getStartDate(), budget.getEndDate(), budget.getInitialLimit(), budget.getCurrentBalance(), budget.getCategory(), budget.getNote()));
                    } else {
                        Budget newBudget = budgetRepository.getCurrentBudgetForCategory(transactionEditRequest.getCategory());
                        if (newBudget != null) {
                            newBudget.setCurrentBalance(newBudget.getCurrentBalance() + transactionEditRequest.getAmount());
                            budgetRepository.update(BudgetEditRequest.of(newBudget.getUUID(), newBudget.getName(), newBudget.getStartDate(), newBudget.getEndDate(), newBudget.getInitialLimit(), newBudget.getCurrentBalance(), newBudget.getCategory(), newBudget.getNote()));
                            transaction.setBudget(newBudget.getUUID());
                        }
                    }
                }
            }
            transaction.setName(transactionEditRequest.getName());
            transaction.setAmount(transactionEditRequest.getAmount());
            transaction.setCategory(transactionEditRequest.getCategory());
            transaction.setAccount(transactionEditRequest.getAccount());
            transaction.setDate(transactionEditRequest.getDate());
            transaction.setNote(transactionEditRequest.getNote());
            long rowID = transactionDAO.update(transaction);
            if (rowID <= 0) {
                throw new RuntimeException("Cập nhật giao dịch thất bại");
            }
            return transaction;
        } catch (Exception e) {
            throw e;
        }
    }

    public TimeSummary getDateSummary(String user, LocalDate date) {
        try {
            return transactionDAO.findDateSummary(user, date);
        } catch (Exception e) {
            throw e;
        }
    }

    public TimeSummary getMonthSummary(String user, LocalDate date) {
        try {
            return transactionDAO.findMonthSummary(user, date);
        } catch (Exception e) {
            throw e;
        }
    }

    public TimeSummary getYearSummary(String user, LocalDate date) {
        try {
            return transactionDAO.findYearSummary(user, date);
        } catch (Exception e) {
            throw e;
        }
    }

    public List<TimeSummary> getMonthsSummary(String user, LocalDate date) {
        try {
            List<TimeSummary> timeSummaries = transactionDAO.findMonthsSummary(user, date);
            return transactionDAO.findMonthsSummary(user, date);
        } catch (Exception e) {
            throw e;
        }
    }

    public List<TimeSummary> getYearsSummary(String user) {
        try {
            return transactionDAO.findYearsSummary(user);
        } catch (Exception e) {
            throw e;
        }
    }
}
