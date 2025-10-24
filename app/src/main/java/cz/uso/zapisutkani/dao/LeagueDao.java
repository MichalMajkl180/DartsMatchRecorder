package cz.uso.zapisutkani.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import cz.uso.zapisutkani.data.League;
import cz.uso.zapisutkani.data.Team;

@Dao
public interface LeagueDao {

    @Query("SELECT * FROM League WHERE id = :id")
    League getLeagueById(int id);

    @Query("SELECT * FROM Team WHERE leagueId = :leagueId")
    List<Team> getTeamsByLeague(int leagueId);

    @Query("SELECT COUNT(*) FROM League")
    int getLeagueCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLeague(League league);
}
