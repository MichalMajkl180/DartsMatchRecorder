package cz.uso.zapisutkani.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cz.uso.zapisutkani.data.Player;

@Dao
public interface PlayerDao {

    @Query("SELECT * FROM Player")
    List<Player> getAll();

    @Query("SELECT * FROM Player WHERE id = :id")
    Player findById(int id);

    @Query("SELECT * FROM Player WHERE teamId = :teamId")
    List<Player> getPlayersByTeam(int teamId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPlayer(Player player);

    @Update
    void updatePlayer(Player player);

    @Delete
    void deletePlayer(Player player);
}
