package cz.uso.zapisutkani.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cz.uso.zapisutkani.data.MatchPlayer;

@Dao
public interface MatchPlayerDao {

    @Query("SELECT * FROM MatchPlayer")
    List<MatchPlayer> getAll();

    @Query("SELECT * FROM MatchPlayer WHERE id = :id")
    MatchPlayer findById(int id);

    @Query("SELECT * FROM MatchPlayer WHERE matchId = :matchId")
    List<MatchPlayer> getByMatch(int matchId);

    @Query("SELECT * FROM MatchPlayer WHERE playerId = :playerId")
    List<MatchPlayer> getByPlayer(int playerId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMatchPlayer(MatchPlayer matchPlayer);

    @Update
    void updateMatchPlayer(MatchPlayer matchPlayer);

    @Delete
    void deleteMatchPlayer(MatchPlayer matchPlayer);
}
