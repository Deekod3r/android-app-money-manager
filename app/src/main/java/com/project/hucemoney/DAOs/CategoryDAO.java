package com.project.hucemoney.DAOs;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.project.hucemoney.database.FieldData;
import com.project.hucemoney.entities.Category;

import java.util.List;

@Dao
public interface CategoryDAO {
    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    long save(Category entity);

    @Update(onConflict = OnConflictStrategy.ROLLBACK)
    int update(Category entity);

    @Delete
    int delete(Category entity);

    @Query("DELETE FROM " + FieldData.TABLE_CATEGORIES)
    int deleteAll();

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_CATEGORIES + " " +
            "WHERE " + FieldData.FIELD_UUID + " = :uuid LIMIT 1")
    Category findByUuid(String uuid);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_CATEGORIES + " " +
            "WHERE " + FieldData.FIELD_ID + " = :id LIMIT 1")
    Category findByID(String id);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_CATEGORIES + " " +
            "ORDER BY :sortedField ASC")
    List<Category> findAllSortedASC(String sortedField);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_CATEGORIES + " " +
            "ORDER BY :sortedField DESC")
    List<Category> findAllSortedDESC(String sortedField);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_CATEGORIES + " " +
            "LIMIT :limit OFFSET (:page - 1) * :limit")
    List<Category> findAllPaginated(int page, int limit);

    @Query("SELECT EXISTS (SELECT 1 " +
            "FROM " + FieldData.TABLE_CATEGORIES + " " +
            "WHERE " + FieldData.FIELD_UUID + " = :uuid)")
    boolean isExists(String uuid);

    @Query("SELECT COUNT(*) " +
            "FROM " + FieldData.TABLE_CATEGORIES)
    long count();

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_CATEGORIES + " " +
            "WHERE " + FieldData.CATEGORY_FIELD_USER + " = :user" + " " +
            "AND " + FieldData.CATEGORY_FIELD_TYPE + " = :type" + " " +
            "OR (" + FieldData.CATEGORY_FIELD_NAME + " LIKE :name" + " " +
            "OR " + FieldData.CATEGORY_FIELD_PARENT + " = :parent)")
    LiveData<List<Category>> findAll(String user, boolean type, @Nullable String name, @Nullable String parent);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_CATEGORIES + " " +
            "WHERE " + FieldData.CATEGORY_FIELD_NAME + " = :name LIMIT 1")
    Category findByName(String name);
}
