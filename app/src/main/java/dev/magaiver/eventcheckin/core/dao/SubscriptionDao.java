package dev.magaiver.eventcheckin.core.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import dev.magaiver.eventcheckin.domain.model.Subscription;

@Dao
public interface SubscriptionDao {

    @Query("select * from  subscription")
    List<Subscription> findAll();

    @Query("select * from  subscription")
    LiveData<List<Subscription>> findAllLiveData();

    @Query("select * from subscription where eventId = :eventId and (status = 'ENABLED' or status = 'PRESENT') ")
    Subscription findById(String eventId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Subscription subscription);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Subscription> subscriptions);

    @Update
    void update(Subscription subscription);

    @Delete
    void delete(Subscription subscription);

    @Query("delete from subscription")
    void deleteAll();
}