package cz.uso.zapisutkani;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import cz.uso.zapisutkani.data.AppDatabase;
import cz.uso.zapisutkani.data.League;

public class SettingsActivity extends AppCompatActivity {

    private Spinner spinnerLeague;
    private EditText editSeason;
    private Button btnSave, btnClearPrefs, btnClearDB;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        spinnerLeague = findViewById(R.id.spinnerLeague);
        editSeason = findViewById(R.id.editSeason);
        btnSave = findViewById(R.id.btnSave);
        btnClearPrefs = findViewById(R.id.btnClearPrefs);
        btnClearDB = findViewById(R.id.btnClearDB);

        db = AppDatabase.getInstance(this);

        // 🔹 Naplníme seznam lig z databáze
        new Thread(() -> {
            List<League> leagues = db.leagueDao().getAll();
            runOnUiThread(() -> {
                System.out.println("Loaded leagues: " + leagues.size()); //test, pak smazat
                if (leagues.isEmpty()) {
                    Toast.makeText(this, "⚠️ V databázi zatím nejsou žádné ligy.", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayAdapter<League> adapter = new ArrayAdapter<>(
                            this, android.R.layout.simple_spinner_dropdown_item, leagues);
                    spinnerLeague.setAdapter(adapter);
                }
            });
        }).start();

        // 🔹 Načteme uložené nastavení
        SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
        int savedLeagueId = prefs.getInt("selectedLeagueId", -1);
        String savedSeason = prefs.getString("selectedSeason", "");

        editSeason.setText(savedSeason);

        // 🔹 Uložení nastavení
        btnSave.setOnClickListener(v -> {
            League selectedLeague = (League) spinnerLeague.getSelectedItem();
            String season = editSeason.getText().toString();

            if (selectedLeague == null) {
                Toast.makeText(this, "Vyberte ligu.", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("selectedLeagueId", selectedLeague.leagueId);
            editor.putString("selectedSeason", season);
            editor.apply();

            Toast.makeText(this, "✅ Nastavení uloženo: " + selectedLeague.name + " (" + season + ")", Toast.LENGTH_SHORT).show();
            finish();
        });

        // 🔹 Smazat uložené nastavení
        btnClearPrefs.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Vymazat nastavení?")
                    .setMessage("Opravdu chcete smazat uložené nastavení ligy a sezóny?")
                    .setPositiveButton("Ano", (dialog, which) -> {
                        prefs.edit().clear().apply();
                        Toast.makeText(this, "Nastavení vymazáno.", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Ne", null)
                    .show();
        });

        // 🔹 Smazat celou databázi
        btnClearDB.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Smazat databázi?")
                    .setMessage("Opravdu chcete vymazat všechny uložené ligy, týmy a data?")
                    .setPositiveButton("Ano", (dialog, which) -> {
                        new Thread(() -> {
                            db.clearAllTables();
                            runOnUiThread(() ->
                                    Toast.makeText(this, "Databáze byla vymazána.", Toast.LENGTH_SHORT).show()
                            );
                        }).start();
                    })
                    .setNegativeButton("Ne", null)
                    .show();
        });
    }
}
