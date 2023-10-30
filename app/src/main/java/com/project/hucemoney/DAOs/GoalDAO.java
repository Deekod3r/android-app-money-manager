package com.project.hucemoney.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.project.hucemoney.database.FieldData;
import com.project.hucemoney.entities.Goal;

import java.util.List;

@Dao
public interface GoalDAO {
    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    long save(Goal entity);

    @Update(onConflict = OnConflictStrategy.ROLLBACK)
    int update(Goal entity);

    @Delete
    int delete(Goal entity);

    @Query("DELETE FROM " + FieldData.TABLE_GOALS)
    int deleteAll();

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_GOALS + " " +
            "WHERE " + FieldData.FIELD_UUID + " = :uuid LIMIT 1")
    Goal findByUuid(String uuid);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_GOALS + " " +
            "WHERE " + FieldData.FIELD_ID + " = :id LIMIT 1")
    Goal findByID(String id);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_GOALS + " " +
            "ORDER BY :sortedField ASC")
    List<Goal> findAllSortedASC(String sortedField);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_GOALS + " " +
            "ORDER BY :sortedField DESC")
    List<Goal> findAllSortedDESC(String sortedField);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_GOALS + " " +
            "LIMIT :limit OFFSET (:page - 1) * :limit")
    List<Goal> findAllPaginated(int page, int limit);

    @Query("SELECT EXISTS (SELECT 1 " +
            "FROM " + FieldData.TABLE_GOALS + " " +
            "WHERE " + FieldData.FIELD_UUID + " = :uuid)")
    boolean isExists(String uuid);

    @Query("SELECT COUNT(*) " +
            "FROM " + FieldData.TABLE_GOALS)
    long count();

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_GOALS + " " +
            "WHERE " + FieldData.GOAL_FIELD_USER + " = :user")
    LiveData<List<Goal>> findAll(String user);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_GOALS + " " +
            "WHERE " + FieldData.GOAL_FIELD_NAME + " = :name LIMIT 1")
    Goal findByName(String name);
}
