package cz.uso.zapisutkani.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import cz.uso.zapisutkani.data.MatchPlayer;

@Dao
public interface MatchPlayerDao {
    @Insert
    long insert(MatchPlayer mp);

    @Query("SELECT * FROM MatchPlayer WHERE matchId = :matchId")
    List<MatchPlayer> getPlayersForMatch(int matchId);
}
