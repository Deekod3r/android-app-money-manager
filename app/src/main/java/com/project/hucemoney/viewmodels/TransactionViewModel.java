package com.project.hucemoney.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.project.hucemoney.common.ResponseCode;
import com.project.hucemoney.database.AppDatabase;
import com.project.hucemoney.entities.Transaction;
import com.project.hucemoney.entities.pojo.CategoryStatistic;
import com.project.hucemoney.entities.pojo.TimeSummary;
import com.project.hucemoney.entities.pojo.TransactionWithCategoryAndAccount;
import com.project.hucemoney.models.Response;
import com.project.hucemoney.models.requests.TransactionAddRequest;
import com.project.hucemoney.models.requests.TransactionEditRequest;
import com.project.hucemoney.repositories.TransactionRepository;
import com.project.hucemoney.utils.SessionManager;

import java.time.LocalDate;
import java.util.List;

public class TransactionViewModel extends AndroidViewModel {
    private MutableLiveData<List<TransactionWithCategoryAndAccount>> transactionsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<CategoryStatistic>> transactionsWithCategoryLiveData = new MutableLiveData<>();
    private MutableLiveData<Response<Transaction>> resultAddTransaction = new MutableLiveData<>();
    private MutableLiveData<Response<Transaction>> resultEditTransaction = new MutableLiveData<>();
    private MutableLiveData<Response<Boolean>> resultDeleteTransaction = new MutableLiveData<>();
    private MutableLiveData<Response<TimeSummary>> summaryDate = new MutableLiveData<>();
    private MutableLiveData<Response<TimeSummary>> summaryMonth = new MutableLiveData<>();
    private MutableLiveData<Response<TimeSummary>> summaryYear = new MutableLiveData<>();
    private MutableLiveData<List<TimeSummary>> summaryMonths = new MutableLiveData<>();
    private MutableLiveData<List<TimeSummary>> summaryYears = new MutableLiveData<>();
    private TransactionAddRequest transactionAddRequest = new TransactionAddRequest();
    private TransactionEditRequest transactionEditRequest = new TransactionEditRequest();
    private TransactionRepository transactionRepository;
    private SessionManager sessionManager;
    private LocalDate date;

    public TransactionViewModel(Application application) {
        super(application);
        this.transactionRepository = new TransactionRepository(AppDatabase.getDatabase(application));
        this.sessionManager = new SessionManager(application);
    }

    public void loadTransactions(String key) {
        try {
            LiveData<List<TransactionWithCategoryAndAccount>> transactions = transactionRepository.getAll(sessionManager.getUUID(), key);
            Observer<List<TransactionWithCategoryAndAccount>> observer = new Observer<List<TransactionWithCategoryAndAccount>>() {
                @Override
                public void onChanged(List<TransactionWithCategoryAndAccount> tst) {
                    transactionsLiveData.setValue(tst);
                    transactions.removeObserver(this);
                }
            };
            transactions.observeForever(observer);
        } catch (Exception e) {
            Log.e("TransactionViewModel", "loadTransactions: " + e.getMessage());
        }
    }

    public void addTransaction() {
        Response<Transaction> response = new Response<>();
        try {
            if (transactionAddRequest.getAmount() <= 0) {
                response.setMessage("Số tiền phải lớn hơn 0");
                resultAddTransaction.setValue(response);
                return;
            }
            if (transactionAddRequest.getDate() == null) {
                response.setMessage("Ngày giao dịch không được để trống");
                resultAddTransaction.setValue(response);
                return;
            }
            if (transactionAddRequest.getCategory() == null) {
                response.setMessage("Danh mục không được để trống");
                resultAddTransaction.setValue(response);
                return;
            }
            if (transactionAddRequest.getAccount() == null) {
                response.setMessage("Tài khoản không được để trống");
                resultAddTransaction.setValue(response);
                return;
            }
            if (transactionAddRequest.getName() == null) {
                response.setMessage("Tên giao dịch không được để trống");
                resultAddTransaction.setValue(response);
                return;
            }
            Transaction transaction = transactionRepository.create(transactionAddRequest);
            response.setStatus(ResponseCode.SUCCESS);
            response.setMessage("Thêm giao dịch thành công");
            response.setData(transaction);
            resultAddTransaction.setValue(response);
        } catch (Exception e) {
            Log.e("TransactionViewModel", "addTransaction: " + e.getMessage());
            response.setMessage("Except: Thêm giao dịch thất bại");
            resultAddTransaction.setValue(response);
        }
    }

    public void editTransaction() {
        Response<Transaction> response = new Response<>();
        try {
            if (transactionEditRequest.getAmount() <= 0) {
                response.setMessage("Số tiền phải lớn hơn 0");
                resultEditTransaction.setValue(response);
                return;
            }
            if (transactionEditRequest.getDate() == null) {
                response.setMessage("Ngày giao dịch không được để trống");
                resultEditTransaction.setValue(response);
                return;
            }
            if (transactionEditRequest.getCategory() == null) {
                response.setMessage("Danh mục không được để trống");
                resultEditTransaction.setValue(response);
                return;
            }
            if (transactionEditRequest.getAccount() == null) {
                response.setMessage("Tài khoản không được để trống");
                resultEditTransaction.setValue(response);
                return;
            }
            if (transactionEditRequest.getName() == null) {
                response.setMessage("Tên giao dịch không được để trống");
                resultEditTransaction.setValue(response);
                return;
            }
            Transaction transaction = transactionRepository.update(transactionEditRequest);
            response.setStatus(ResponseCode.SUCCESS);
            response.setMessage("Cập nhật giao dịch thành công");
            response.setData(transaction);
            resultEditTransaction.setValue(response);
        } catch (Exception e) {
            Log.e("TransactionViewModel", "editTransaction: " + e.getMessage());
            response.setMessage("Except: Cập nhật giao dịch thất bại");
            resultEditTransaction.setValue(response);
        }
    }

    public void deleteTransaction() {
        Response<Boolean> response = new Response<>();
        try {
            boolean result = transactionRepository.delete(transactionEditRequest.getUUID());
            if(result){
                response.setStatus(ResponseCode.SUCCESS);
                response.setMessage("Xóa giao dịch thành công");
            } else {
                response.setMessage("Xóa giao dịch thất bại");
            }
            resultDeleteTransaction.setValue(response);
        } catch (Exception e) {
            Log.e("TransactionViewModel", "deleteTransaction: " + e.getMessage());
            response.setMessage("Except: Xóa giao dịch thất bại");
            resultDeleteTransaction.setValue(response);
        }
    }

    public void transactionsWithCategory(String time, int type){
        try {
            LiveData<List<CategoryStatistic>> transactions;
            if (type == 1) {
                transactions = transactionRepository.getAllByYear(time, sessionManager.getUUID());
            } else if (type == 2) {
                transactions = transactionRepository.getAllByMonth(time, sessionManager.getUUID());
            } else {
                transactions = transactionRepository.getAllByDay(time, sessionManager.getUUID());
            }
            Observer<List<CategoryStatistic>> observer = new Observer<List<CategoryStatistic>>() {
                @Override
                public void onChanged(List<CategoryStatistic> tst) {
                    transactionsWithCategoryLiveData.setValue(tst);
                    transactions.removeObserver(this);
                }
            };
            transactions.observeForever(observer);
        } catch (Exception e) {
            Log.e("TransactionViewModel", "transactionsWithCategory: " + e.getMessage());
        }
    }

    public void summaryDate() {
        Response<TimeSummary> response = new Response<>();
        try {
            TimeSummary timeSummary = transactionRepository.getDateSummary(sessionManager.getUUID(),date);
            if (timeSummary != null && timeSummary.getTime() != null) {
                response.setStatus(ResponseCode.SUCCESS);
                response.setMessage("Thống kê thành công");
            }
            response.setData(timeSummary);
            summaryDate.setValue(response);
        } catch (Exception e) {
            Log.e("TransactionViewModel", "summaryDate: " + e.getMessage());
            response.setMessage("Except: Thống kê thất bại");
            summaryDate.setValue(response);
        }
    }

    public void summaryMonth() {
        Response<TimeSummary> response = new Response<>();
        try {
            TimeSummary timeSummary = transactionRepository.getMonthSummary(sessionManager.getUUID(),date);
            if (timeSummary != null && timeSummary.getTime() != null) {
                response.setStatus(ResponseCode.SUCCESS);
                response.setMessage("Thống kê thành công");
            }
            response.setData(timeSummary);
            summaryMonth.setValue(response);
        } catch (Exception e) {
            Log.e("TransactionViewModel", "summaryMonth: " + e.getMessage());
            response.setMessage("Except: Thống kê thất bại");
            summaryMonth.setValue(response);
        }
    }

    public void summaryYear() {
        Response<TimeSummary> response = new Response<>();
        try {
            TimeSummary timeSummary = transactionRepository.getYearSummary(sessionManager.getUUID(),date);
            if (timeSummary != null && timeSummary.getTime() != null) {
                response.setStatus(ResponseCode.SUCCESS);
                response.setMessage("Thống kê thành công");
            }
            response.setData(timeSummary);
            summaryYear.setValue(response);
        } catch (Exception e) {
            Log.e("TransactionViewModel", "summaryYear: " + e.getMessage());
            response.setMessage("Except: Thống kê thất bại");
            summaryYear.setValue(response);
        }
    }

    public void summaryMonths() {
        try {
            List<TimeSummary> timeSummaries = transactionRepository.getMonthsSummary(sessionManager.getUUID(), date);
            summaryMonths.setValue(timeSummaries);
        } catch (Exception e) {
            Log.e("TransactionViewModel", "setSummaryMonths: " + e.getMessage());
        }
    }

    public void summaryYears() {
        try {
            List<TimeSummary> timeSummaries = transactionRepository.getYearsSummary(sessionManager.getUUID());
            summaryYears.setValue(timeSummaries);
        } catch (Exception e) {
            Log.e("TransactionViewModel", "setSummaryYears: " + e.getMessage());
        }
    }

    public LiveData<Response<Transaction>> getResultAddTransaction() {
        return resultAddTransaction;
    }

    public LiveData<Response<Transaction>> getResultEditTransaction() {
        return resultEditTransaction;
    }

    public LiveData<Response<Boolean>> getResultDeleteTransaction() {
        return resultDeleteTransaction;
    }

    public LiveData<List<TransactionWithCategoryAndAccount>> getTransactions() {
        return transactionsLiveData;
    }

    public LiveData<List<CategoryStatistic>> getTransactionsWithCategory() {
        return transactionsWithCategoryLiveData;
    }

    public LiveData<Response<TimeSummary>> getSummaryDate() {
        return summaryDate;
    }

    public LiveData<Response<TimeSummary>> getSummaryMonth() {
        return summaryMonth;
    }

    public LiveData<Response<TimeSummary>> getSummaryYear() {
        return summaryYear;
    }

    public LiveData<List<TimeSummary>> getSummaryMonths() {
        return summaryMonths;
    }

    public LiveData<List<TimeSummary>> getSummaryYears() {
        return summaryYears;
    }

    public TransactionAddRequest getTransactionAddRequest() {
        return transactionAddRequest;
    }

    public TransactionEditRequest getTransactionEditRequest() {
        return transactionEditRequest;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


}
