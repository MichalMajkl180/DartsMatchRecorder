package cz.uso.zapisutkani.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import cz.uso.zapisutkani.data.League;

@Dao
public interface LeagueDao {
    @Insert
    long insert(League league);

    @Query("SELECT * FROM League ORDER BY season DESC, name ASC")
    List<League> getAll();

    @Query("SELECT * FROM League WHERE leagueId = :id LIMIT 1")
    League findById(int id);

    @Query("SELECT * FROM League WHERE name = :name AND season = :season LIMIT 1")
    League findByNameAndSeason(String name, String season);

    @Query("DELETE FROM League WHERE leagueId = :id")
    void deleteById(int id);

}
