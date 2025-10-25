package cz.uso.zapisutkani;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import cz.uso.zapisutkani.data.AppDatabase;
import cz.uso.zapisutkani.data.Team;
import cz.uso.zapisutkani.repository.TeamRepository;
import cz.uso.zapisutkani.utils.AppLogger;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "DartsPrefs";
    private static final String KEY_LEAGUE_ID = "LeagueID";

    private Button buttonUpdateTeams;
    private Button buttonOpenSettings;
    private ListView listTeams;

    private TeamRepository repository;
    private AppDatabase db;
    private SharedPreferences prefs;

    private ArrayAdapter<String> teamAdapter;
    private List<String> teamNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 🔹 Inicializace UI
        buttonUpdateTeams = findViewById(R.id.buttonUpdateTeams);
        buttonOpenSettings = findViewById(R.id.buttonOpenSettings);
        listTeams = findViewById(R.id.listTeams);

        // 🔹 Logger (jen soubor + Logcat)
        AppLogger.initFileLogging(getApplicationContext());
        AppLogger.i("MainActivity", "Aplikace spuštěna");

        // 🔹 Inicializace DB a repository
        db = AppDatabase.getInstance(this);
        repository = new TeamRepository(this);
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // 🔹 Nastavení adaptérů
        teamAdapter = new ArrayAdapter<>(this, R.layout.item_team, R.id.textTeamName, teamNames);
        listTeams.setAdapter(teamAdapter);

        // 🔹 Ověření LeagueID
        int leagueId = prefs.getInt(KEY_LEAGUE_ID, -1);
        if (leagueId == -1) {
            AppLogger.w("MainActivity", "LeagueID zatím není nastaveno — přesměrovávám do Nastavení");
            startActivity(new Intent(this, SettingsActivity.class));
            finish();
            return;
        }

        AppLogger.i("MainActivity", "Načteno LeagueID: " + leagueId);

        String leagueUrl = "https://www.sipky.org/?region=ulk&page=ligova-skupina&league=" + leagueId;

        // 🔹 Načíst aktuální týmy z DB při startu
        loadTeamsFromDatabase(leagueId);

        // 🔹 Aktualizovat týmy z webu
        buttonUpdateTeams.setOnClickListener(v -> {
            Toast.makeText(this, "🔄 Načítám týmy z webu...", Toast.LENGTH_SHORT).show();
            AppLogger.i("MainActivity", "Spouštím aktualizaci týmů...");

            repository.updateTeamsFromWeb(leagueUrl, count -> runOnUiThread(() -> {
                Toast.makeText(this, "✅ Uloženo " + count + " týmů", Toast.LENGTH_SHORT).show();
                AppLogger.i("MainActivity", "Načteno a uloženo " + count + " týmů.");
                loadTeamsFromDatabase(leagueId); // obnov seznam
            }));
        });

        // 🔹 Otevřít nastavení
        buttonOpenSettings.setOnClickListener(v -> {
            Toast.makeText(this, "⚙️ Otevírám nastavení...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        });

        // 🔹 Kliknutí na tým – otevře jeho profil
        listTeams.setOnItemClickListener((parent, view, position, id) -> {
            new Thread(() -> {
                try {
                    List<Team> allTeams = db.teamDao().getTeamsByLeague(prefs.getInt(KEY_LEAGUE_ID, -1));
                    if (position >= 0 && position < allTeams.size()) {
                        Team team = allTeams.get(position);
                        String url = team.getUrl();
                        if (url != null && !url.isEmpty()) {
                            runOnUiThread(() -> {
                                Toast.makeText(this, "🌐 Otevírám: " + team.getName(), Toast.LENGTH_SHORT).show();
                                AppLogger.i("MainActivity", "Otevírám profil týmu: " + team.getName());

                                Intent intent = new Intent(MainActivity.this, TeamDetailActivity.class);
                                intent.putExtra("TEAM_URL", url);
                                intent.putExtra("TEAM_NAME", team.getName());
                                startActivity(intent);
                            });
                        } else {
                            runOnUiThread(() ->
                                    Toast.makeText(this, "⚠️ Tým nemá URL", Toast.LENGTH_SHORT).show());
                            AppLogger.w("MainActivity", "Tým " + team.getName() + " nemá uloženou URL!");
                        }
                    }
                } catch (Exception e) {
                    AppLogger.e("MainActivity", "Chyba při otevírání profilu: " + e.getMessage());
                    runOnUiThread(() ->
                            Toast.makeText(this, "❌ Chyba při otevírání profilu", Toast.LENGTH_SHORT).show());
                }
            }).start();
        });
    }

    /**
     * 🔹 Načte všechny týmy z DB a zobrazí je v ListView
     */
    private void loadTeamsFromDatabase(int leagueId) {
        new Thread(() -> {
            try {
                List<Team> teams = db.teamDao().getTeamsByLeague(leagueId);
                teamNames.clear();
                for (Team t : teams) {
                    teamNames.add(t.getName());
                }

                runOnUiThread(() -> {
                    teamAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "📋 Načteno " + teamNames.size() + " týmů", Toast.LENGTH_SHORT).show();
                    AppLogger.i("MainActivity", "Z databáze načteno " + teamNames.size() + " týmů.");
                });
            } catch (Exception e) {
                AppLogger.e("MainActivity", "Chyba při načítání týmů z DB: " + e.getMessage());
                runOnUiThread(() ->
                        Toast.makeText(this, "❌ Chyba při načítání týmů", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
