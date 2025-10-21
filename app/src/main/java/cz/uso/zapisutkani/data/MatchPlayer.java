package cz.uso.zapisutkani.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

@Entity(foreignKeys = {
        @ForeignKey(entity = Match.class, parentColumns = "matchId", childColumns = "matchId", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Player.class, parentColumns = "playerId", childColumns = "playerId", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Team.class, parentColumns = "teamId", childColumns = "teamId", onDelete = ForeignKey.CASCADE)
})
public class MatchPlayer {

    @PrimaryKey(autoGenerate = true)
    public int matchPlayerId;

    public int matchId;
    public int teamId;
    public int playerId;
    public boolean isSubstitute;
    public int position; // 1–4 základ, 5–8 náhradník
}
