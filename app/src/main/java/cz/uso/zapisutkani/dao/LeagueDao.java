package cz.uso.zapisutkani.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import cz.uso.zapisutkani.data.League;
import cz.uso.zapisutkani.data.Team;

@Dao
public interface LeagueDao {

    @Insert
    void insertLeague(League league);

    // 🔹 změna "id" → "leagueId"
    @Query("SELECT * FROM League WHERE leagueId = :leagueId")
    League findById(int leagueId);

    @Query("SELECT * FROM League")
    List<League> getAllLeagues();

    // 🔹 napojení na týmy podle leagueId
    @Query("SELECT * FROM teams WHERE leagueId = :leagueId")
    List<Team> getTeamsByLeague(int leagueId);
}
