package cz.uso.zapisutkani;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import cz.uso.zapisutkani.dao.LeagueDao;
import cz.uso.zapisutkani.data.AppDatabase;
import cz.uso.zapisutkani.data.League;
import cz.uso.zapisutkani.data.Team;
import cz.uso.zapisutkani.utils.AppLogger; //doplnil jsem si tohle sám

public class MainActivity extends AppCompatActivity {

    private TextView logTextView;
    private SharedPreferences prefs;
    private AppDatabase db;
    private LeagueDao leagueDao;

    private static final String PREFS_NAME = "DartsPrefs";
    private static final String KEY_LEAGUE_ID = "LeagueID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logTextView = findViewById(R.id.logTextView);
        AppLogger.setLogView(logTextView);

        AppLogger.d("MainActivity", "Spouštím MainActivity...");

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        db = AppDatabase.getInstance(getApplicationContext());
        leagueDao = db.leagueDao();

        checkAndLoadLeague();
    }

    private void checkAndLoadLeague() {
        int savedLeagueId = prefs.getInt(KEY_LEAGUE_ID, -1);
        AppLogger.d("MainActivity", "Načtená hodnota LeagueID ze SharedPreferences: " + savedLeagueId);

        int leagueCount = leagueDao.getLeagueCount();
        AppLogger.d("MainActivity", "Počet lig v databázi: " + leagueCount);

        if (savedLeagueId == -1 || leagueCount == 0) {
            AppLogger.d("MainActivity", "Neuložené ID ligy nebo prázdná databáze → přesměrovávám do SettingsActivity...");
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        League league = leagueDao.getLeagueById(savedLeagueId);
        if (league == null) {
            AppLogger.d("MainActivity", "Liga s ID " + savedLeagueId + " nebyla nalezena. Spouštím nastavení.");
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        AppLogger.d("MainActivity", "Načtena liga: " + league.getName() + " (ID: " + league.getId() + ")");
        loadTeamsForLeague(savedLeagueId);
    }

    private void loadTeamsForLeague(int leagueId) {
        List<Team> teams = leagueDao.getTeamsByLeague(leagueId);

        if (teams == null || teams.isEmpty()) {
            AppLogger.d("MainActivity", "Žádné týmy v lize ID " + leagueId);
            return;
        }

        AppLogger.d("MainActivity", "Načítám " + teams.size() + " týmů pro ligu ID " + leagueId);

        StringBuilder sb = new StringBuilder();
        sb.append("🏆 Liga ID ").append(leagueId).append("\n");
        sb.append("=========================\n");
        for (Team team : teams) {
            sb.append("• ").append(team.getName()).append(" (ID: ").append(team.getId()).append(")\n");
        }

        AppLogger.d("MainActivity", "Týmy úspěšně načteny.");
        AppLogger.d("MainActivity", sb.toString());
    }
}
