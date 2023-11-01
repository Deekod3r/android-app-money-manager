package com.project.hucemoney.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.project.hucemoney.database.FieldData;
import com.project.hucemoney.entities.crossref.TransactionGoal;
import com.project.hucemoney.entities.pojo.GoalWithTransactions;
import com.project.hucemoney.entities.pojo.TransactionWithGoals;

import java.util.List;

@Dao
public interface TransactionGoalDAO {

    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    long insert(TransactionGoal transactionGoal);

    @Update(onConflict = OnConflictStrategy.ROLLBACK)
    int update(TransactionGoal transactionGoal);

    @Delete
    int delete(TransactionGoal transactionGoal);

    @Transaction
    @Query("SELECT * FROM " + FieldData.TABLE_TRANSACTIONS + " WHERE " + FieldData.FIELD_UUID + " = :transactionUUID")
    public TransactionWithGoals getTransactionsWithGoals(String transactionUUID);

    @Transaction
    @Query("SELECT * FROM " + FieldData.TABLE_GOALS + " WHERE " + FieldData.FIELD_UUID + " = :goalUUID")
    public GoalWithTransactions getGoalWithTransactions(String goalUUID);

    @Transaction
    @Query("SELECT * FROM " + FieldData.TABLE_TRANSACTIONS)
    public List<TransactionWithGoals> getTransactionsWithGoals();

    @Transaction
    @Query("SELECT * FROM " + FieldData.TABLE_GOALS)
    public List<GoalWithTransactions> getGoalWithTransactions();
}

