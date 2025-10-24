package cz.uso.zapisutkani;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
        // ... (tvoje existující metoda)
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
