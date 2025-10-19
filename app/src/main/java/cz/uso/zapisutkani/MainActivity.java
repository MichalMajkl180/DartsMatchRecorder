package cz.uso.zapisutkani;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import cz.uso.zapisutkani.data.AppDatabase;
import cz.uso.zapisutkani.data.Team;
import cz.uso.zapisutkani.repository.TeamRepository;

public class MainActivity extends AppCompatActivity {

    private static final String LEAGUE_URL = "https://www.sipky.org/?region=ulk&page=ligova-skupina&league=244485";
    private AppDatabase db;
    private TextView textTeams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonLoad = findViewById(R.id.buttonLoad);
        textTeams = findViewById(R.id.textTeams);
        db = AppDatabase.getInstance(getApplicationContext());

        // 🟢 Po spuštění zobrazíme, co je aktuálně v databázi
        refreshTeams();

        // 🔘 Po kliknutí načti data z webu
        buttonLoad.setOnClickListener(v -> {
            textTeams.setText("⏳ Načítám týmy z webu...");
            System.out.println("➡️ Spouštím načítání týmů z: " + LEAGUE_URL);

            TeamRepository repo = new TeamRepository(this);
            repo.updateTeamsFromWeb(LEAGUE_URL, count -> {
                System.out.println("✅ Dokončeno, počet nově vložených týmů: " + count);
                runOnUiThread(() -> {
                    textTeams.setText("✅ Načteno " + count + " nových týmů.\n\nAktualizuji seznam...");
                    refreshTeams();
                });
            });
        });
    }

    // 🧩 Pomocná metoda pro výpis týmů z DB
    private void refreshTeams() {
        new Thread(() -> {
            StringBuilder sb = new StringBuilder();
            for (Team t : db.teamDao().getAllTeams()) {
                sb.append("• ").append(t.teamName);
                if (t.league != null && !t.league.isEmpty()) {
                    sb.append(" (").append(t.league).append(")");
                }
                sb.append("\n");
            }

            String text = (sb.length() == 0)
                    ? "📭 Žádné týmy v databázi.\nKlikni na tlačítko pro načtení."
                    : sb.toString();

            System.out.println("📋 V DB je aktuálně " + db.teamDao().getAllTeams().size() + " týmů.");

            runOnUiThread(() -> textTeams.setText(text));
        }).start();
    }
}
