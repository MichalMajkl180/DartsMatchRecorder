package cz.uso.zapisutkani.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class League {
    @PrimaryKey(autoGenerate = true)
    public int leagueId;

    public String name;      // např. "2. liga B JIH"
    public String season;    // např. "2024/25"
    public String region;    // volitelné
    public String url;       // volitelné, adresa na sipky.org

    @Override
    public String toString() {
        // co se zobrazí ve Spinneru — můžeš upravit formát podle potřeby
        if (season != null && !season.isEmpty()) {
            return season + " — " + (name != null ? name : "");
        } else {
            return (name != null ? name : "");
        }
    }
}
