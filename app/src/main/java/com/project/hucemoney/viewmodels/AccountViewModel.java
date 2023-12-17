package com.project.hucemoney.viewmodels;

import static com.project.hucemoney.utils.ValidationUtils.isNullOrEmpty;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.project.hucemoney.R;
import com.project.hucemoney.common.ResponseCode;
import com.project.hucemoney.database.AppDatabase;
import com.project.hucemoney.entities.Account;
import com.project.hucemoney.models.Response;
import com.project.hucemoney.models.requests.AccountAddRequest;
import com.project.hucemoney.models.requests.AccountEditRequest;
import com.project.hucemoney.repositories.AccountRepository;
import com.project.hucemoney.utils.SessionManager;

import java.text.NumberFormat;
import java.util.List;

import lombok.Getter;

public class AccountViewModel extends AndroidViewModel {
    private MutableLiveData<List<Account>> accountsLiveData = new MutableLiveData<>();
    private MutableLiveData<Response<Account>> resultAddAccount = new MutableLiveData<>();
    private MutableLiveData<Response<Account>> resultEditAccount = new MutableLiveData<>();
    private MutableLiveData<Response<Boolean>> resultDeleteAccount = new MutableLiveData<>();
    private MutableLiveData<String> amountTotal = new MutableLiveData<>();
    private AccountAddRequest accountAddRequest = new AccountAddRequest();
    private AccountEditRequest accountEditRequest = new AccountEditRequest();
    private AccountRepository accountRepository;
    private SessionManager sessionManager;

    public AccountViewModel(@NonNull Application application) {
        super(application);
        this.accountRepository = new AccountRepository(AppDatabase.getDatabase(application));
        sessionManager = new SessionManager(application);
    }

    public LiveData<List<Account>> getAccounts() {
        return accountsLiveData;
    }

    public void addAccount() {
        Response<Account> response = new Response<>();
        try {
            if (isNullOrEmpty(accountAddRequest.getName())) {
                response.setMessage("Tên tài khoản không được để trống");
                resultAddAccount.setValue(response);
                return;
            }
            accountAddRequest.setUser(sessionManager.getUUID());
            Account account = accountRepository.create(accountAddRequest);
            if (account == null) {
                response.setMessage("Thêm tài khoản thất bại");
                resultAddAccount.setValue(response);
                return;
            }
            response.setStatus(ResponseCode.SUCCESS);
            response.setMessage("Thêm tài khoản thành công");
            response.setData(account);
            resultAddAccount.setValue(response);
        } catch (Exception e) {
            Log.e("AccountViewModel", "addAccount: " + e.getMessage());
            //response.setMessage("Except: Thêm tài khoản thất bại");
            response.setMessage( e.getMessage());
            resultAddAccount.setValue(response);
        }
    }

    public void addAccountLiveData(Account account) {
        List<Account> accounts = accountsLiveData.getValue();
        assert accounts != null;
        accounts.add(account);
        accountsLiveData.setValue(accounts);
        loadAmountTotal();
    }

    public void editAccount() {
        Response<Account> response = new Response<>();
        try {
            if (isNullOrEmpty(accountEditRequest.getName())) {
                response.setMessage("Tên tài khoản không được để trống");
                resultEditAccount.setValue(response);
                return;
            }
            Account account = accountRepository.update(accountEditRequest);
            if (account == null) {
                response.setMessage("Cập nhật tài khoản thất bại");
                resultEditAccount.setValue(response);
                return;
            }
            response.setStatus(ResponseCode.SUCCESS);
            response.setMessage("Cập nhật tài khoản thành công");
            response.setData(account);
            resultEditAccount.setValue(response);
        } catch (Exception e) {
            Log.e("AccountViewModel", "editAccount: " + e.getMessage());
            //response.setMessage("Except: Cập nhật tài khoản thất bại");
            response.setMessage( e.getMessage());
            resultEditAccount.setValue(response);
        }
    }

    public void editAccountLiveData(Account account, int position) {
        List<Account> accounts = accountsLiveData.getValue();
        assert accounts != null;
        accounts.set(position, account);
        accountsLiveData.setValue(accounts);
        loadAmountTotal();
    }

    public void deleteAccount(Account account) {
        Response<Boolean> response = new Response<>();
        try {
            boolean isDelete = accountRepository.delete(account.getUUID());
            if (!isDelete) {
                response.setMessage("Xóa tài khoản thất bại");
                resultDeleteAccount.setValue(response);
                return;
            }
            response.setStatus(ResponseCode.SUCCESS);
            response.setMessage("Xóa tài khoản thành công");
            resultDeleteAccount.setValue(response);
        } catch (Exception e) {
            Log.e("AccountViewModel", "deleteAccount: " + e.getMessage());
            //response.setMessage("Except: Xóa tài khoản thất bại");
            response.setMessage( e.getMessage());
            resultDeleteAccount.setValue(response);
        }
    }

    public void deleteAccountLiveData(int position) {
        List<Account> accounts = accountsLiveData.getValue();
        assert accounts != null;
        accounts.remove(position);
        accountsLiveData.setValue(accounts);
        loadAmountTotal();
    }

    public void loadAccounts() {
        try {
            LiveData<List<Account>> accounts = accountRepository.getAll(sessionManager.getUUID());
            Observer<List<Account>> observer = new Observer<List<Account>>() {
                @Override
                public void onChanged(List<Account> acs) {
                    accountsLiveData.setValue(acs);
                    loadAmountTotal();
                    accounts.removeObserver(this);;
                }
            };
            accounts.observeForever(observer);
        } catch (Exception e) {
            Log.e("AccountViewModel", "loadAccounts: " + e.getMessage());
        }
    }

    private void loadAmountTotal() {
        List<Account> acs = accountsLiveData.getValue();
        long amount = 0;
        assert acs != null;
        for (Account ac : acs) {
            amount += ac.getAmount();
        }
        NumberFormat format = NumberFormat.getInstance();
        String amountString = format.format(amount);
        amountTotal.setValue(String.format("%s %s", amountString, getApplication().getString(R.string.vi_currency)));
    }

    public LiveData<Response<Account>> getResultAddAccount() {
        return resultAddAccount;
    }

    public LiveData<Response<Account>> getResultEditAccount() {
        return resultEditAccount;
    }

    public LiveData<Response<Boolean>> getResultDeleteAccount() {
        return resultDeleteAccount;
    }

    public AccountAddRequest getAccountAddRequest() {
        return accountAddRequest;
    }

    public AccountEditRequest getAccountEditRequest() {
        return accountEditRequest;
    }


    public void setAccountEditRequest(AccountEditRequest accountEditRequest) {
        this.accountEditRequest = accountEditRequest;
    }

    public LiveData<String> getAmountTotal() {
        return amountTotal;
    }

}
