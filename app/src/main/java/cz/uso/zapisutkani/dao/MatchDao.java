package cz.uso.zapisutkani.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cz.uso.zapisutkani.data.Match;

@Dao
public interface MatchDao {

    @Query("SELECT * FROM Match")
    List<Match> getAll();

    @Query("SELECT * FROM Match WHERE id = :id")
    Match findById(int id);

    @Query("SELECT * FROM Match WHERE homeTeamId = :teamId OR awayTeamId = :teamId")
    List<Match> getMatchesByTeam(int teamId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMatch(Match match);

    @Update
    void updateMatch(Match match);

    @Delete
    void deleteMatch(Match match);
}
