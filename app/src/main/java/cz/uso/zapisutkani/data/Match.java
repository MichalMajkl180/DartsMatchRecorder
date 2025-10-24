package cz.uso.zapisutkani.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "Match",
        foreignKeys = {
                @ForeignKey(
                        entity = Team.class,
                        parentColumns = "id",        // ✅ opraveno
                        childColumns = "homeTeamId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Team.class,
                        parentColumns = "id",        // ✅ opraveno
                        childColumns = "awayTeamId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("homeTeamId"),
                @Index("awayTeamId")
        }
)
public class Match {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "homeTeamId")
    private int homeTeamId;

    @ColumnInfo(name = "awayTeamId")
    private int awayTeamId;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "result")
    private String result;

    // 🔹 Konstruktor
    public Match(int homeTeamId, int awayTeamId, String date, String result) {
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.date = date;
        this.result = result;
    }

    // 🔹 Gettery a settery
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(int homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public int getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(int awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", homeTeamId=" + homeTeamId +
                ", awayTeamId=" + awayTeamId +
                ", date='" + date + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
