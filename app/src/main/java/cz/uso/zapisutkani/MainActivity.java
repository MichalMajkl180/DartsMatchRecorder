package cz.uso.zapisutkani;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.content.SharedPreferences;
import cz.uso.zapisutkani.repository.TeamRepository;

import java.io.IOException;
import java.util.List;

import cz.uso.zapisutkani.data.AppDatabase;
import cz.uso.zapisutkani.data.Team;
import cz.uso.zapisutkani.data.League;
import cz.uso.zapisutkani.parser.TeamParser; // DŮLEŽITÉ: parser musí být ve složce cz.uso.zapisutkani.parser

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;
    private TextView textTeams;
    private Button buttonLoadTeams, buttonRefresh;
    private int selectedLeagueId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textTeams = findViewById(R.id.textTeams);
        buttonLoadTeams = findViewById(R.id.buttonLoadTeams);
        buttonRefresh = findViewById(R.id.buttonRefresh);

        db = AppDatabase.getInstance(getApplicationContext());

        // Načti uloženou ligu
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        selectedLeagueId = prefs.getInt("selectedLeagueId", -1);

        if (selectedLeagueId == -1) {
            Toast.makeText(this, "⚙️ Není vybraná žádná liga – otevři Nastavení.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, SettingsActivity.class));
            return;
        }

        // Zkus načíst objekt ligy z DB (synchronně je to ok, jednorázově)
        League selectedLeague = db.leagueDao().findById(selectedLeagueId);
        if (selectedLeague == null) {
            Toast.makeText(this, "⚠️ Vybraná liga neexistuje. Otevři Nastavení.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, SettingsActivity.class));
            return;
        }

        setTitle("Liga: " + selectedLeague.name + " (" + selectedLeague.season + ")");

        // obnovíme aktuální seznam z DB
        refreshTeams();

        // tlačítko: načíst týmy z webu a uložit do DB (v novém vlákně)
        buttonLoadTeams.setOnClickListener(v -> {
            textTeams.setText("⏳ Načítám týmy z webu...");
            new Thread(() -> {
                try {
                    String leagueUrl = selectedLeague.url;
                    // voláme parser, který vloží týmy do DB a vrátí seznam nově vložených
                    List<Team> newTeams = TeamParser.loadTeamsFromLeague(leagueUrl, selectedLeagueId, db);

                    final int added = newTeams == null ? 0 : newTeams.size();
                    runOnUiThread(() -> {
                        Toast.makeText(this, "✅ Načteno " + added + " nových týmů.", Toast.LENGTH_SHORT).show();
                        refreshTeams();
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(this, "Chyba při načítání: " + e.getMessage(), Toast.LENGTH_LONG).show());
                }
            }).start();
        });

        // tlačítko: obnovit z databáze
        buttonRefresh.setOnClickListener(v -> refreshTeams());
    }

    private void refreshTeams() {
        new Thread(() -> {
            List<Team> teams = db.teamDao().getTeamsByLeague(selectedLeagueId);

            // připrav text pro UI
            StringBuilder sb = new StringBuilder();
            if (teams == null || teams.isEmpty()) {
                sb.append("📭 Žádné týmy pro vybranou ligu.\nKlikni na 'Načti týmy z webu'.");
            } else {
                for (Team t : teams) {
                    sb.append("• ").append(t.teamName);
                    if (t.city != null && !t.city.isEmpty()) {
                        sb.append(" (").append(t.city).append(")");
                    }
                    sb.append("\n");
                }
            }
            final String display = sb.toString();

            runOnUiThread(() -> textTeams.setText(display));
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
