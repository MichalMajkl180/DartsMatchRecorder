package cz.uso.zapisutkani.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class League {
    @PrimaryKey(autoGenerate = true)
    public int leagueId;

    public String name;      // např. "2. liga B JIH"
    public String season;    // např. "2024/25"
    public String region;    // např. "ULK"
    public String url;       // odkaz na stránku ligy (sipky.org)
}
