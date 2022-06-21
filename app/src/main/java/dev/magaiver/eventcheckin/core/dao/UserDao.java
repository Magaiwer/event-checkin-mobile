package dev.magaiver.eventcheckin.core.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import dev.magaiver.eventcheckin.domain.model.User;


@Dao
public interface UserDao {

    @Query("select * from  user")
    List<User> findAll();

    @Query("select * from  user")
    LiveData<List<User>> findAllLiveData();

    @Query("select * from user where uuid = :uuid ")
    User findById(String uuid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<User> users);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("delete from user")
    void deleteAll();
}
