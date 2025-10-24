package cz.uso.zapisutkani.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "MatchPlayer",
        foreignKeys = {
                @ForeignKey(
                        entity = Match.class,
                        parentColumns = "id",       // 🔗 odkaz na Match.id
                        childColumns = "matchId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Player.class,
                        parentColumns = "id",       // 🔗 odkaz na Player.id
                        childColumns = "playerId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("matchId"),
                @Index("playerId")
        }
)
public class MatchPlayer {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "matchId")
    private int matchId;

    @ColumnInfo(name = "playerId")
    private int playerId;

    @ColumnInfo(name = "score")
    private int score;

    // 🔹 Konstruktor
    public MatchPlayer(int matchId, int playerId, int score) {
        this.matchId = matchId;
        this.playerId = playerId;
        this.score = score;
    }

    // 🔹 Gettery a settery
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "MatchPlayer{" +
                "id=" + id +
                ", matchId=" + matchId +
                ", playerId=" + playerId +
                ", score=" + score +
                '}';
    }
}
