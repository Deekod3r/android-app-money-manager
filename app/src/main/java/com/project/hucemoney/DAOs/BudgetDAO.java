package com.project.hucemoney.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.project.hucemoney.database.FieldData;
import com.project.hucemoney.entities.Budget;
import com.project.hucemoney.entities.pojo.BudgetWithCategory;

import java.time.LocalDate;
import java.util.List;

@Dao
public interface BudgetDAO {
    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    long save(Budget entity);

    @Update(onConflict = OnConflictStrategy.ROLLBACK)
    int update(Budget entity);

    @Delete
    int delete(Budget entity);

    @Query("DELETE FROM " + FieldData.TABLE_BUDGETS)
    int deleteAll();

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_BUDGETS + " " +
            "WHERE " + FieldData.FIELD_UUID + " = :uuid LIMIT 1")
    Budget findByUuid(String uuid);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_BUDGETS + " " +
            "WHERE " + FieldData.FIELD_ID + " = :id LIMIT 1")
    Budget findByID(String id);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_BUDGETS + " " +
            "ORDER BY :sortedField ASC")
    List<Budget> findAllSortedASC(String sortedField);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_BUDGETS + " " +
            "ORDER BY :sortedField DESC")
    List<Budget> findAllSortedDESC(String sortedField);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_BUDGETS + " " +
            "LIMIT :limit OFFSET (:page - 1) * :limit")
    List<Budget> findAllPaginated(int page, int limit);

    @Query("SELECT EXISTS (SELECT 1 " +
            "FROM " + FieldData.TABLE_BUDGETS + " " +
            "WHERE " + FieldData.FIELD_UUID + " = :uuid)")
    boolean isExists(String uuid);

    @Query("SELECT COUNT(*) " +
            "FROM " + FieldData.TABLE_BUDGETS)
    long count();

    @Transaction
    @Query("SELECT " + FieldData.TABLE_BUDGETS +".*" +
            "FROM " + FieldData.TABLE_BUDGETS + " " +
            "INNER JOIN " + FieldData.TABLE_CATEGORIES + " " +
            "ON " + FieldData.TABLE_BUDGETS + "." + FieldData.BUDGET_FIELD_CATEGORY + " = " + FieldData.TABLE_CATEGORIES + "." + FieldData.FIELD_UUID + " " +
            "WHERE " + FieldData.TABLE_CATEGORIES + "." + FieldData.CATEGORY_FIELD_USER + " = :user ")
    LiveData<List<BudgetWithCategory>> findAll(String user);


    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_BUDGETS + " " +
            "WHERE " + FieldData.BUDGET_FIELD_NAME + " = :name LIMIT 1")
    Budget findByName(String name);

    @Query( "SELECT * FROM budgets WHERE category = :categoryUUID AND endDate >= date('now') AND startDate <= date('now') LIMIT 1")
    Budget findCurrentBudgetForCategory(String categoryUUID);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_BUDGETS + " " +
            "WHERE " + FieldData.BUDGET_FIELD_CATEGORY + " = :categoryUUID AND " +
            "startDate <= :date AND " +
            "endDate >= :date LIMIT 1")
    Budget findBudgetForCategory(String categoryUUID, LocalDate date);
}
