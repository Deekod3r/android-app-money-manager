package com.project.hucemoney.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.project.hucemoney.DAOs.AccountDAO;
import com.project.hucemoney.database.AppDatabase;
import com.project.hucemoney.entities.Account;
import com.project.hucemoney.models.requests.AccountAddRequest;
import com.project.hucemoney.models.requests.AccountEditRequest;

import java.util.List;

public class AccountRepository {
    private final AccountDAO accountDAO;

    public AccountRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getDatabase(application);
        this.accountDAO = appDatabase.accountDAO();
    }

    public Account create(AccountAddRequest accountAddRequest) {
        try {
            Account account = new Account();
            account.setUUID(java.util.UUID.randomUUID().toString());
            account.setId("account" + System.currentTimeMillis());
            account.setName(accountAddRequest.getName());
            long money = accountAddRequest.getAmount();
            account.setAmount(accountAddRequest.getAmount());
            account.setNote(accountAddRequest.getNote());
            account.setUser(accountAddRequest.getUser());
            long rowID = accountDAO.save(account);
            if (rowID <= 0) {
                throw new RuntimeException("Thêm tài khoản thất bại");
            }
            return account;
        } catch (Exception e) {
            throw e;
        }
    }

    public Account findByUUID(String uuid) {
        try {
            return accountDAO.findByUuid(uuid);
        } catch (Exception e) {
            throw e;
        }
    }

    public LiveData<List<Account>> getAll(String user) {
        try {
            if (user == null) {
                return accountDAO.findAll();
            }
            return accountDAO.findAllByUser(user);
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean delete(String uuid) {
        try {
            if (!accountDAO.isExists(uuid)) {
                throw new RuntimeException("Tài khoản không tồn tại");
            }
            Account Account = accountDAO.findByUuid(uuid);
            return accountDAO.delete(Account) > 0;
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean isUserExists(String uuid) {
        try {
            return accountDAO.isExists(uuid);
        } catch (Exception e) {
            throw e;
        }
    }

    public Account update(AccountEditRequest accountEditRequest) {
        try {
            Account account = accountDAO.findByUuid(accountEditRequest.getUUID());
            if (account == null) {
                throw new RuntimeException("Tài khoản không tồn tại");
            }
            account.setName(accountEditRequest.getName());
            account.setAmount(accountEditRequest.getAmount());
            account.setNote(accountEditRequest.getNote());
            long rowID = accountDAO.update(account);
            if (rowID <= 0) {
                throw new RuntimeException("Cập nhật tài khoản thất bại");
            }
            return account;
        } catch (Exception e) {
            throw e;
        }
    }

    public Account findByName(String name) {
        try {
            return accountDAO.findByName(name);
        } catch (Exception e) {
            throw e;
        }
    }

    public Long getSumMoney(String user) {
        try {
            LiveData<List<Account>> accounts = accountDAO.findAllByUser(user);
            List<Account> acs = accounts.getValue();
            if (acs == null) {
                return 0L;
            }
            long sum = 0;
            for (Account account : acs) {
                sum += account.getAmount();
            }
            return sum;
        } catch (Exception e) {
            throw e;
        }
    }
}
