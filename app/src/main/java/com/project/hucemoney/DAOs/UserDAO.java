package com.project.hucemoney.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.project.hucemoney.database.FieldData;
import com.project.hucemoney.entities.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    long save(User entity);

    @Update(onConflict = OnConflictStrategy.ROLLBACK)
    int update(User entity);

    @Delete
    int delete(User entity);

    @Query("DELETE FROM " + FieldData.TABLE_USERS)
    int deleteAll();

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_USERS + " " +
            "WHERE " + FieldData.FIELD_UUID + " = :uuid AND isDeleted = 0 LIMIT 1")
    User findByUUID(String uuid);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_USERS + " " +
            "WHERE " + FieldData.FIELD_UUID + " = :uuid LIMIT 1")
    User findForVerifyRegister(String uuid);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_USERS + " " +
            "WHERE " + FieldData.FIELD_ID + " = :id AND isDeleted = 0 LIMIT 1")
    User findById(String id);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_USERS + " " +
            "WHERE " + FieldData.USER_FIELD_EMAIL + " = :email AND isDeleted = 0 LIMIT 1")
    User findByEmail(String email);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_USERS + " " +
            "WHERE " + FieldData.USER_FIELD_USERNAME + " = :username AND isDeleted = 0 LIMIT 1")
    User findByUsername(String username);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_USERS + " " +
            "ORDER BY :sortedField ASC")
    List<User> findAllSortedASC(String sortedField);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_USERS + " " +
            "ORDER BY :sortedField DESC")
    List<User> findAllSortedDESC(String sortedField);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_USERS + " " +
            "LIMIT :limit " +
            "OFFSET (:page - 1) * :limit")
    List<User> findAllPaginated(int page, int limit);

    @Query("SELECT * " +
            "FROM " + FieldData.TABLE_USERS)
    List<User> findAll();

    @Query("SELECT COUNT(*) " +
            "FROM " + FieldData.TABLE_USERS + " " +
            "WHERE " + FieldData.FIELD_UUID + " = :uuid AND isDeleted = 0 LIMIT 1")
    boolean isExist(String uuid);

    @Query("SELECT COUNT(*) " +
            "FROM " + FieldData.TABLE_USERS)
    long count();
}
