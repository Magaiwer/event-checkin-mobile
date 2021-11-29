package dev.magaiver.eventcheckin.core.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import dev.magaiver.eventcheckin.domain.model.Event;

@Dao
public interface EventDao {

    @Query("select * from  event")
    List<Event> findAll();

    @Query("select * from event where id = :id ")
    Event findById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Event event);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Event> events);

    @Update
    void update(Event event);

    @Delete
    void delete(Event event);

    @Query("delete from event")
    void deleteAll();
}
