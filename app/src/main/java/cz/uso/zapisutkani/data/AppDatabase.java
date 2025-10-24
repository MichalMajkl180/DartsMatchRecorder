package cz.uso.zapisutkani.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import cz.uso.zapisutkani.dao.LeagueDao;
import cz.uso.zapisutkani.dao.TeamDao;
import cz.uso.zapisutkani.dao.PlayerDao;
import cz.uso.zapisutkani.dao.MatchDao;
import cz.uso.zapisutkani.dao.MatchPlayerDao;

@Database(
        entities = {
                League.class,
                Team.class,
                Player.class,
                Match.class,
                MatchPlayer.class
        },
        version = 4, // 🔹 zvyšte o +1 při změně struktury tabulek
        exportSchema = true
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract LeagueDao leagueDao();
    public abstract TeamDao teamDao();
    public abstract PlayerDao playerDao();
    public abstract MatchDao matchDao();
    public abstract MatchPlayerDao matchPlayerDao();

    // 🧱 Singleton instance
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "uso_db" // 👈 název databáze
                            )
                            // 🟢 Pro vývoj: smaže starou DB při změně verze (zabraňuje chybám s migrací)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
