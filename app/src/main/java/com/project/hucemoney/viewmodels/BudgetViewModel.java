package com.project.hucemoney.viewmodels;

import static com.project.hucemoney.utils.ValidationUtils.isNullOrEmpty;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.project.hucemoney.common.ResponseCode;
import com.project.hucemoney.database.AppDatabase;
import com.project.hucemoney.entities.Account;
import com.project.hucemoney.entities.Budget;
import com.project.hucemoney.models.Response;
import com.project.hucemoney.models.requests.BudgetAddRequest;
import com.project.hucemoney.models.requests.BudgetEditRequest;
import com.project.hucemoney.repositories.AccountRepository;
import com.project.hucemoney.repositories.BudgetRepository;
import com.project.hucemoney.utils.SessionManager;

import java.util.List;

public class BudgetViewModel extends AndroidViewModel {

    private MutableLiveData<List<Budget>> budgetsLiveData = new MutableLiveData<>();
    private MutableLiveData<Response<Budget>> resultAddBudget = new MutableLiveData<>();
    private MutableLiveData<Response<Budget>> resultEditBudget = new MutableLiveData<>();
    private MutableLiveData<Response<Boolean>> resultDeleteBudget = new MutableLiveData<>();
    private BudgetAddRequest budgetAddRequest = new BudgetAddRequest();
    private BudgetEditRequest budgetEditRequest = new BudgetEditRequest();
    private BudgetRepository budgetRepository;
    private SessionManager sessionManager;

    public BudgetViewModel(@NonNull Application application) {
        super(application);
        budgetRepository = new BudgetRepository(AppDatabase.getDatabase(application));
        sessionManager = new SessionManager(application);
    }

    public void addBudget() {
        Response<Budget> response = new Response<>();
        try {
            if (budgetAddRequest.getInitialLimit() <= 0) {
                response.setMessage("Hạn mức phải lớn hơn 0");
                resultAddBudget.setValue(response);
                return;
            }
            if (isNullOrEmpty(budgetAddRequest.getName())) {
                response.setMessage("Tên hạn mức không được để trống");
                resultAddBudget.setValue(response);
                return;
            }
            if (isNullOrEmpty(budgetAddRequest.getCategory())) {
                response.setMessage("Danh mục không được để trống");
                resultAddBudget.setValue(response);
                return;
            }
            if (budgetAddRequest.getStartDate() == null) {
                response.setMessage("Ngày bắt đầu không được để trống");
                resultAddBudget.setValue(response);
                return;
            }
            if (budgetAddRequest.getEndDate() == null) {
                response.setMessage("Ngày kết thúc không được để trống");
                resultAddBudget.setValue(response);
                return;
            }
            if (budgetAddRequest.getStartDate().isAfter(budgetAddRequest.getEndDate())) {
                response.setMessage("Ngày bắt đầu phải trước ngày kết thúc");
                resultAddBudget.setValue(response);
                return;
            }
            Budget budget = budgetRepository.create(budgetAddRequest);
            if (budget == null) {
                response.setMessage("Thêm hạn mức thất bại");
                resultAddBudget.setValue(response);
                return;
            }
            response.setStatus(ResponseCode.SUCCESS);
            response.setMessage("Thêm hạn mức thành công");
            response.setData(budget);
            resultAddBudget.setValue(response);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            resultAddBudget.setValue(response);
        }
    }

    public void addBudgetLiveData(Budget budget) {
        List<Budget> budgets = budgetsLiveData.getValue();
        assert budgets != null;
        budgets.add(budget);
        budgetsLiveData.setValue(budgets);
    }

    public LiveData<List<Budget>> getBudgets() {
        return budgetsLiveData;
    }

    public LiveData<Response<Budget>> getResultAddBudget() {
        return resultAddBudget;
    }

    public LiveData<Response<Budget>> getResultEditBudget() {
        return resultEditBudget;
    }

    public LiveData<Response<Boolean>> getResultDeleteBudget() {
        return resultDeleteBudget;
    }

    public void loadBudgets() {
        try {
            LiveData<List<Budget>> budgets = budgetRepository.getAll(sessionManager.getUUID());
            Observer<List<Budget>> observer = new Observer<List<Budget>>() {
                @Override
                public void onChanged(List<Budget> bg) {
                    budgetsLiveData.setValue(bg);
                    budgets.removeObserver(this);;
                }
            };
            budgets.observeForever(observer);
        } catch (Exception e) {
            Log.e("BudgetViewModel", e.getMessage());
        }
    }

    public BudgetAddRequest getBudgetAddRequest() {
        return budgetAddRequest;
    }

    public BudgetEditRequest getBudgetEditRequest() {
        return budgetEditRequest;
    }
}
