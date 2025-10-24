package cz.uso.zapisutkani.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.Index;

@Entity(
        tableName = "Team",
        foreignKeys = @ForeignKey(
                entity = League.class,
                parentColumns = "id",
                childColumns = "leagueId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = @Index("leagueId")
)
public class Team {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "leagueId")
    private int leagueId; // 🔗 vazba na League.id

    // 🔹 Konstruktor
    public Team(String name, int leagueId) {
        this.name = name;
        this.leagueId = leagueId;
    }

    // 🔹 Gettery / Settery
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(int leagueId) {
        this.leagueId = leagueId;
    }

    // 🔹 Pro ladění
    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", leagueId=" + leagueId +
                '}';
    }
}
