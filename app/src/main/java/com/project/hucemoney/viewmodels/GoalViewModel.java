package com.project.hucemoney.viewmodels;

import static com.project.hucemoney.utils.ValidationUtils.isNullOrEmpty;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.project.hucemoney.common.ResponseCode;
import com.project.hucemoney.database.AppDatabase;
import com.project.hucemoney.entities.Category;
import com.project.hucemoney.entities.Goal;
import com.project.hucemoney.models.Response;
import com.project.hucemoney.models.requests.GoalAddRequest;
import com.project.hucemoney.models.requests.GoalEditRequest;
import com.project.hucemoney.repositories.AccountRepository;
import com.project.hucemoney.repositories.GoalRepository;
import com.project.hucemoney.utils.SessionManager;

import java.util.List;

public class GoalViewModel extends AndroidViewModel {
    private MutableLiveData<List<Goal>> goalsLiveData = new MutableLiveData<>();
    private MutableLiveData<Response<Goal>> resultAddGoal = new MutableLiveData<>();
    private MutableLiveData<Response<Goal>> resultEditGoal = new MutableLiveData<>();
    private MutableLiveData<Response<Boolean>> resultDeleteGoal = new MutableLiveData<>();
    private GoalAddRequest goalAddRequest = new GoalAddRequest();
    private GoalEditRequest goalEditRequest = new GoalEditRequest();
    private GoalRepository goalRepository;
    private SessionManager sessionManager;

    public GoalViewModel(@NonNull Application application) {
        super(application);
        this.goalRepository = new GoalRepository(AppDatabase.getDatabase(application));
        sessionManager = new SessionManager(application);
    }

    public void loadGoals() {
        try {
            LiveData<List<Goal>> categories = goalRepository.getAll(sessionManager.getUUID());
            Observer<List<Goal>> observer = new Observer<List<Goal>>() {
                @Override
                public void onChanged(List<Goal> g) {
                    goalsLiveData.setValue(g);
                    categories.removeObserver(this);
                }
            };
            categories.observeForever(observer);
        } catch (Exception e) {
            Log.e("GoalViewModel", e.getMessage());
        }
    }

    public void addGoalLiveData(Goal goal) {
        List<Goal> g = goalsLiveData.getValue();
        assert g != null;
        g.add(goal);
        goalsLiveData.setValue(g);
    }

    public void editGoalLiveData(Goal goal, int position) {
        List<Goal> g = goalsLiveData.getValue();
        assert g != null;
        g.set(position, goal);
        goalsLiveData.setValue(g);
    }

    public void deleteGoalLiveData(int position) {
        List<Goal> g = goalsLiveData.getValue();
        assert g != null;
        g.remove(position);
        goalsLiveData.setValue(g);
    }

    public LiveData<List<Goal>> getGoals() {
        return goalsLiveData;
    }

    public LiveData<Response<Goal>> getResultAddGoal() {
        return resultAddGoal;
    }

    public LiveData<Response<Goal>> getResultEditGoal() {
        return resultEditGoal;
    }

    public LiveData<Response<Boolean>> getResultDeleteGoal() {
        return resultDeleteGoal;
    }
    public GoalAddRequest getGoalAddRequest() {
        return goalAddRequest;
    }

    public GoalEditRequest getGoalEditRequest() {
        return goalEditRequest;
    }

    public void addGoal() {
        Response<Goal> response = new Response<>();
        try {
            if (goalAddRequest.getTargetAmount() <= 0) {
                response.setMessage("Số tiền mục tiêu phải lớn hơn 0");
                resultAddGoal.setValue(response);
                return;
            }
            if (goalAddRequest.getCurrentAmount() < 0) {
                response.setMessage("Số tiền hiện tại không được âm");
                resultAddGoal.setValue(response);
                return;
            }
            if (isNullOrEmpty(goalAddRequest.getName())) {
                response.setMessage("Tên mục tiêu không được để trống");
                resultAddGoal.setValue(response);
                return;
            }
            if (goalAddRequest.getStartDate() == null) {
                response.setMessage("Ngày bắt đầu không được để trống");
                resultAddGoal.setValue(response);
                return;
            }
            if (goalAddRequest.getEndDate() == null) {
                response.setMessage("Ngày kết thúc không được để trống");
                resultAddGoal.setValue(response);
                return;
            }
            if (goalAddRequest.getStartDate().isAfter(goalAddRequest.getEndDate())) {
                response.setMessage("Ngày bắt đầu phải trước ngày kết thúc");
                resultAddGoal.setValue(response);
                return;
            }
            goalAddRequest.setUser(sessionManager.getUUID());
            Goal goal = goalRepository.create(goalAddRequest);
            if (goal == null) {
                response.setMessage("Thêm mục tiêu thất bại");
                resultAddGoal.setValue(response);
                return;
            }
            response.setStatus(ResponseCode.SUCCESS);
            response.setMessage("Thêm mục tiêu thành công");
            response.setData(goal);
            resultAddGoal.setValue(response);
        } catch (Exception e) {
            Log.e("GoalViewModel", "addGoal: ", e);
            response.setMessage("Except: Thêm mục tiêu thất bại");
            resultAddGoal.setValue(response);
        }
    }

    public void editGoal() {
        Response<Goal> response = new Response<>();
        try {
            if (goalEditRequest.getTargetAmount() <= 0) {
                response.setMessage("Số tiền mục tiêu phải lớn hơn 0");
                resultEditGoal.setValue(response);
                return;
            }
            if (goalEditRequest.getCurrentAmount() < 0) {
                response.setMessage("Số tiền hiện tại không được âm");
                resultEditGoal.setValue(response);
                return;
            }
            if (isNullOrEmpty(goalEditRequest.getName())) {
                response.setMessage("Tên mục tiêu không được để trống");
                resultEditGoal.setValue(response);
                return;
            }
            if (goalEditRequest.getStartDate() == null) {
                response.setMessage("Ngày bắt đầu không được để trống");
                resultEditGoal.setValue(response);
                return;
            }
            if (goalEditRequest.getEndDate() == null) {
                response.setMessage("Ngày kết thúc không được để trống");
                resultEditGoal.setValue(response);
                return;
            }
            if (goalEditRequest.getStartDate().isAfter(goalEditRequest.getEndDate())) {
                response.setMessage("Ngày bắt đầu phải trước ngày kết thúc");
                resultEditGoal.setValue(response);
                return;
            }
            Goal goal = goalRepository.update(goalEditRequest);
            if (goal == null) {
                response.setMessage("Sửa mục tiêu thất bại");
                resultEditGoal.setValue(response);
                return;
            }
            response.setStatus(ResponseCode.SUCCESS);
            response.setMessage("Sửa mục tiêu thành công");
            response.setData(goal);
            resultEditGoal.setValue(response);
        } catch (Exception e) {
            Log.e("GoalViewModel", "editGoal: ", e);
            response.setMessage("Except: Sửa mục tiêu thất bại");
            resultEditGoal.setValue(response);
        }
    }

    public void deleteGoal(){
        Response<Boolean> response = new Response<>();
        try {
            boolean result = goalRepository.delete(goalEditRequest.getUUID());
            if (!result) {
                response.setMessage("Xóa mục tiêu thất bại");
                resultDeleteGoal.setValue(response);
                return;
            }
            response.setStatus(ResponseCode.SUCCESS);
            response.setMessage("Xóa mục tiêu thành công");
            response.setData(true);
            resultDeleteGoal.setValue(response);
        } catch (Exception e) {
            Log.e("GoalViewModel", "deleteGoal: ", e);
            response.setMessage("Except: Xóa mục tiêu thất bại");
            resultDeleteGoal.setValue(response);
        }
    }

}
