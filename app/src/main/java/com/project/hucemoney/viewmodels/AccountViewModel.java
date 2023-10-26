package com.project.hucemoney.viewmodels;

import static com.project.hucemoney.utils.ValidationUtils.isNullOrEmpty;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.project.hucemoney.R;
import com.project.hucemoney.common.ResponseCode;
import com.project.hucemoney.entities.Account;
import com.project.hucemoney.models.Response;
import com.project.hucemoney.models.requests.AccountAddRequest;
import com.project.hucemoney.models.requests.AccountEditRequest;
import com.project.hucemoney.repositories.AccountRepository;
import com.project.hucemoney.utils.SessionManager;

import java.text.NumberFormat;
import java.util.List;

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
    private String amountAsString;

    public AccountViewModel(@NonNull Application application) {
        super(application);
        this.accountRepository = new AccountRepository(application);
        sessionManager = new SessionManager(application);
    }

    public LiveData<List<Account>> getAccounts() {
        return accountsLiveData;
    }

    public void addAccount() {
        Response<Account> response = new Response<>();
        try {
            if (isNullOrEmpty(amountAsString)) {
                response.setMessage("Số tiền không được để trống");
                resultAddAccount.setValue(response);
                return;
            }
            if (isNullOrEmpty(accountAddRequest.getName())) {
                response.setMessage("Tên tài khoản không được để trống");
                resultAddAccount.setValue(response);
                return;
            }
            long amount = Long.parseLong(amountAsString);
            accountAddRequest.setAmount(amount);
            accountAddRequest.setUser(sessionManager.getUUID());
            Account account = accountRepository.create(accountAddRequest);
            if (account == null) {
                response.setMessage("Thêm tài khoản thất bại");
                resultAddAccount.setValue(response);
                return;
            }
            response.setStatus(ResponseCode.SUCCESS);
            response.setMessage("Thêm tài khoản thành công");
        } catch (Exception e) {
            response.setMessage(e.getMessage());
        }
        resultAddAccount.setValue(response);
    }
    
    public void editAccount(int position) {
        Response<Account> response = new Response<>();
        try {
            if (isNullOrEmpty(amountAsString)) {
                response.setMessage("Số tiền không được để trống");
                resultEditAccount.setValue(response);
                return;
            }
            if (isNullOrEmpty(accountEditRequest.getName())) {
                response.setMessage("Tên tài khoản không được để trống");
                resultEditAccount.setValue(response);
                return;
            }
            accountEditRequest.setAmount(Long.parseLong(amountAsString));
            Account account = accountRepository.update(accountEditRequest);
            if (account == null) {
                response.setMessage("Cập nhật tài khoản thất bại");
                resultEditAccount.setValue(response);
                return;
            }
            response.setStatus(ResponseCode.SUCCESS);
            response.setMessage("Cập nhật tài khoản thành công");
        } catch (Exception e) {
            response.setMessage(e.getMessage());
        }
        resultEditAccount.setValue(response);
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
            //List<Account> accounts = accountsLiveData.getValue();
            //assert accounts != null;
            //accounts.remove(account);
            //accountsLiveData.setValue(accounts);
            response.setStatus(ResponseCode.SUCCESS);
            response.setMessage("Xóa tài khoản thành công");
        } catch (Exception e) {
            response.setMessage(e.getMessage());
        }
        resultDeleteAccount.setValue(response);
    }

    public void loadAccounts() {
        try {
            LiveData<List<Account>> accounts = accountRepository.getAll(sessionManager.getUUID());
            accounts.observeForever(acs -> {
                accountsLiveData.setValue(acs);
                long amount = 0;
                for (Account ac : acs) {
                    amount += ac.getAmount();
                }
                NumberFormat format = NumberFormat.getInstance();
                String amountString = format.format(amount);
                amountTotal.setValue(String.format("%s %s", amountString, getApplication().getString(R.string.vi_currency)));
                //accounts.removeObservers(this);
            });
        } catch (Exception e) {
            Log.e("AccountViewModel", e.getMessage());
        }
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

    public void setAmountAsString(String amountAsString) {
        this.amountAsString = amountAsString.trim();
    }

    public void setAccountEditRequest(AccountEditRequest accountEditRequest) {
        this.accountEditRequest = accountEditRequest;
    }

    public String getAmountAsString() {
        return amountAsString;
    }

    public LiveData<String> getAmountTotal() {
        return amountTotal;
    }

}
