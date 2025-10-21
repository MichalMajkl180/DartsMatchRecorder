package cz.uso.zapisutkani.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import cz.uso.zapisutkani.data.Match;

@Dao
public interface MatchDao {
    @Insert
    long insert(Match match);

    @Query("SELECT * FROM `Match` WHERE leagueId = :leagueId")
    List<Match> getByLeague(int leagueId);

    @Query("DELETE FROM `Match` WHERE leagueId = :leagueId")
    void deleteByLeague(int leagueId);
}
