package cz.uso.zapisutkani.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "Player",
        foreignKeys = @ForeignKey(
                entity = Team.class,
                parentColumns = "id",        // 🔗 vazba na Team.id
                childColumns = "teamId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = @Index("teamId")
)
public class Player {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "teamId")
    private int teamId;

    @ColumnInfo(name = "nickname")
    private String nickname;

    @ColumnInfo(name = "averageScore")
    private float averageScore;

    // 🔹 Konstruktor
    public Player(String name, String nickname, int teamId, float averageScore) {
        this.name = name;
        this.nickname = nickname;
        this.teamId = teamId;
        this.averageScore = averageScore;
    }

    // 🔹 Gettery a settery
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public float getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(float averageScore) {
        this.averageScore = averageScore;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", teamId=" + teamId +
                ", averageScore=" + averageScore +
                '}';
    }
}
