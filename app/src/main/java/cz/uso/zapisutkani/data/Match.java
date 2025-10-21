package cz.uso.zapisutkani.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

@Entity(foreignKeys = {
        @ForeignKey(entity = League.class, parentColumns = "leagueId", childColumns = "leagueId", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Team.class, parentColumns = "teamId", childColumns = "homeTeamId", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Team.class, parentColumns = "teamId", childColumns = "awayTeamId", onDelete = ForeignKey.CASCADE)
})
public class Match {

    @PrimaryKey(autoGenerate = true)
    public int matchId;

    public int leagueId;
    public int homeTeamId;
    public int awayTeamId;
    public String date;
    public String location;
    public String note; // např. "Bylo občerstvení", "Zápas proběhl v klidu"
}
