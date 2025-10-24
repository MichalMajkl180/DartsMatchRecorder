package cz.uso.zapisutkani.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cz.uso.zapisutkani.data.Team;

@Dao
public interface TeamDao {

    @Query("SELECT * FROM teams")
    List<Team> getAll();

    @Query("SELECT * FROM teams WHERE id = :id")
    Team findById(int id);

    @Query("SELECT * FROM teams WHERE leagueId = :leagueId ORDER BY name")
    List<Team> getTeamsByLeague(int leagueId);

    @Query("SELECT * FROM teams WHERE name = :name LIMIT 1")
    Team findByName(String name);

    @Query("SELECT * FROM teams WHERE name LIKE '%' || :query || '%' ORDER BY name")
    List<Team> findByNameLike(String query);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTeam(Team team);

    @Update
    void updateTeam(Team team);

    @Delete
    void deleteTeam(Team team);
}
