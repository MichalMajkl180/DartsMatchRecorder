package cz.uso.zapisutkani.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "teams")
public class Team {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "leagueId")
    private int leagueId;

    @ColumnInfo(name = "url")
    private String url; // 🔹 nově přidáno

    public Team(String name, int leagueId) {
        this.name = name;
        this.leagueId = leagueId;
    }

    // region Gettery & Settery
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getLeagueId() { return leagueId; }
    public void setLeagueId(int leagueId) { this.leagueId = leagueId; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    // endregion
}
