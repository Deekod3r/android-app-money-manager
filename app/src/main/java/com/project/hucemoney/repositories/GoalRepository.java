package com.project.hucemoney.repositories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.Transaction;

import com.project.hucemoney.DAOs.GoalDAO;
import com.project.hucemoney.database.AppDatabase;
import com.project.hucemoney.entities.Goal;
import com.project.hucemoney.models.requests.GoalAddRequest;
import com.project.hucemoney.models.requests.GoalEditRequest;

import java.util.List;

public class GoalRepository {
    private GoalDAO goalDAO;
    private AppDatabase appDatabase;

    public GoalRepository(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
        this.goalDAO = appDatabase.goalDAO();
    }

    @Transaction
    public Goal create(GoalAddRequest goalAddRequest) {
        try {
            Goal goal = new Goal();
            goal.setUUID(java.util.UUID.randomUUID().toString());
            goal.setId("goal" + System.currentTimeMillis());
            goal.setName(goalAddRequest.getName());
            goal.setNote(goalAddRequest.getNote());
            goal.setUser(goalAddRequest.getUser());
            goal.setTargetAmount(goalAddRequest.getTargetAmount());
            goal.setCurrentAmount(goalAddRequest.getCurrentAmount());
            goal.setStartDate(goalAddRequest.getStartDate());
            goal.setEndDate(goalAddRequest.getEndDate());
            long rowID = goalDAO.save(goal);
            if (rowID <= 0) {
                throw new RuntimeException("Thêm mục tiêu thất bại");
            }
            return goal;
        } catch (Exception e) {
            throw e;
        }
    }

    public Goal getByUUID(String uuid) {
        try {
            return goalDAO.findByUuid(uuid);
        } catch (Exception e) {
            throw e;
        }
    }

    public LiveData<List<Goal>> getAll(@NonNull String user) {
        try {
            return goalDAO.findAll(user);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transaction
    public boolean delete(String uuid) {
        try {
            Goal goal = goalDAO.findByUuid(uuid);
            if (goal == null) {
                throw new RuntimeException("Mục tiêu không tồn tại");
            }
            return goalDAO.delete(goal) > 0;
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean isUserExists(String uuid) {
        try {
            return goalDAO.isExists(uuid);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transaction
    public Goal update(GoalEditRequest goalEditRequest) {
        try {
            Goal goal = goalDAO.findByUuid(goalEditRequest.getUUID());
            if (goal == null) {
                throw new RuntimeException("Mục tiêu không tồn tại");
            }
            goal.setName(goalEditRequest.getName());
            goal.setNote(goalEditRequest.getNote());
            goal.setTargetAmount(goalEditRequest.getTargetAmount());
            goal.setCurrentAmount(goalEditRequest.getCurrentAmount());
            goal.setStartDate(goalEditRequest.getStartDate());
            goal.setEndDate(goalEditRequest.getEndDate());
            long rowID = goalDAO.update(goal);
            if (rowID <= 0) {
                throw new RuntimeException("Cập nhật mục tiêu thất bại");
            }
            return goal;
        } catch (Exception e) {
            throw e;
        }
    }
}
