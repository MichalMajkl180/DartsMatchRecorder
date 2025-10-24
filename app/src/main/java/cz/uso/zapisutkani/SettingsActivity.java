package cz.uso.zapisutkani;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;

import cz.uso.zapisutkani.dao.LeagueDao;
import cz.uso.zapisutkani.data.AppDatabase;
import cz.uso.zapisutkani.data.League;
import cz.uso.zapisutkani.utils.AppLogger;

public class SettingsActivity extends AppCompatActivity {

    private EditText editLeagueID;
    private Button buttonSave;
    private Button buttonExportLogs;
    private TextView logTextView;

    private SharedPreferences prefs;
    private AppDatabase db;
    private LeagueDao leagueDao;

    private static final String PREFS_NAME = "DartsPrefs";
    private static final String KEY_LEAGUE_ID = "LeagueID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editLeagueID = findViewById(R.id.editLeagueID);
        buttonSave = findViewById(R.id.buttonSaveSettings);
        buttonExportLogs = findViewById(R.id.buttonExportLogs);
        logTextView = findViewById(R.id.logTextView);

        AppLogger.setLogView(logTextView);
        AppLogger.initFileLogging(getApplicationContext());
        AppLogger.d("SettingsActivity", "Spouštím SettingsActivity...");

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        db = AppDatabase.getInstance(getApplicationContext());
        leagueDao = db.leagueDao();

        int currentLeagueId = prefs.getInt(KEY_LEAGUE_ID, -1);
        if (currentLeagueId != -1) {
            editLeagueID.setText(String.valueOf(currentLeagueId));
            AppLogger.d("SettingsActivity", "Načteno uložené LeagueID: " + currentLeagueId);
        }

        buttonSave.setOnClickListener(v -> saveLeagueId());
        buttonExportLogs.setOnClickListener(v -> exportLogs());
    }

    private void saveLeagueId() {
        try {
            String input = editLeagueID.getText().toString().trim();
            if (input.isEmpty()) {
                AppLogger.w("SettingsActivity", "Pole LeagueID je prázdné!");
                runOnUiThread(() ->
                        Toast.makeText(this, "Zadej číslo ligy!", Toast.LENGTH_SHORT).show()
                );
                return;
            }

            int leagueId = Integer.parseInt(input);

            // 🔹 Uložení do SharedPreferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(KEY_LEAGUE_ID, leagueId);
            editor.apply();

            AppLogger.i("SettingsActivity", "LeagueID " + leagueId + " uložen do SharedPreferences.");
            runOnUiThread(() ->
                    Toast.makeText(this, "LeagueID " + leagueId + " uložen", Toast.LENGTH_SHORT).show()
            );

            // 🔹 Uložení nebo aktualizace ligy v DB
            new Thread(() -> {
                try {
                    League existing = leagueDao.findById(leagueId);
                    if (existing == null) {
                        League league = new League();
                        league.leagueId = leagueId;
                        league.name = "Liga #" + leagueId;
                        league.region = "ULK";
                        league.url = "https://www.sipky.org/?region=ulk&page=ligova-skupina&league=" + leagueId;
                        leagueDao.insertLeague(league);
                        AppLogger.i("SettingsActivity", "Do DB vložena nová liga: " + league.name);
                    } else {
                        AppLogger.d("SettingsActivity", "Liga s ID " + leagueId + " již v DB existuje.");
                    }

                    // 🔹 Přesměrování zpět do MainActivity
                    runOnUiThread(() -> {
                        AppLogger.i("SettingsActivity", "LeagueID " + leagueId + " uložen. Otevírám MainActivity...");
                        Toast.makeText(this, "Uloženo! Otevírám hlavní obrazovku...", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    });
                } catch (Exception e) {
                    AppLogger.e("SettingsActivity", "Chyba při ukládání do DB: " + e.getMessage());
                    runOnUiThread(() ->
                            Toast.makeText(this, "Chyba při ukládání do DB!", Toast.LENGTH_LONG).show()
                    );
                }
            }).start();

        } catch (NumberFormatException e) {
            AppLogger.e("SettingsActivity", "Neplatné číslo LeagueID!");
            runOnUiThread(() ->
                    Toast.makeText(this, "Neplatné číslo!", Toast.LENGTH_SHORT).show()
            );
        }
    }

    private void exportLogs() {
        try {
            File logFile = new File(getFilesDir(), "DartsLogs.txt");
            if (!logFile.exists()) {
                AppLogger.w("SettingsActivity", "Soubor logu neexistuje!");
                return;
            }

            // Sdílení přes FileProvider (bezpečné URI)
            Uri fileUri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".fileprovider",
                    logFile
            );

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Darts Recorder Log");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Zasílám export logů z aplikace DartsMatchRecorder.");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(Intent.createChooser(shareIntent, "Sdílet logy pomocí..."));

            AppLogger.i("SettingsActivity", "Log odeslán: " + logFile.getAbsolutePath());
        } catch (Exception e) {
            AppLogger.e("SettingsActivity", "Chyba při exportu logu: " + e.getMessage());
        }
    }
}
