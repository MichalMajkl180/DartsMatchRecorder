package cz.uso.zapisutkani.utils;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 🧩 Univerzální logger pro vývoj a ladění.
 * - vypisuje do Logcatu
 * - vypisuje do TextView
 * - ukládá do souboru (DartsLogs.txt)
 */
public class AppLogger {

    private static TextView logView;
    private static final Handler uiHandler = new Handler(Looper.getMainLooper());
    private static File logFile;
    private static boolean fileLoggingEnabled = false;

    // 🔹 Inicializace logování do souboru
    public static void initFileLogging(Context context) {
        File dir = context.getFilesDir(); // interní úložiště
        logFile = new File(dir, "DartsLogs.txt");
        fileLoggingEnabled = true;
        d("AppLogger", "Soubor logu: " + logFile.getAbsolutePath());
    }

    // 🔹 Nastavení TextView, kam se vypisují logy
    public static void setLogView(TextView view) {
        logView = view;
    }

    public static void d(String tag, String message) {
        Log.d(tag, message);
        appendToView("🐞 " + tag + ": " + message);
        appendToFile("DEBUG", tag, message);
    }

    public static void e(String tag, String message) {
        Log.e(tag, message);
        appendToView("❌ " + tag + ": " + message);
        appendToFile("ERROR", tag, message);
    }

    public static void i(String tag, String message) {
        Log.i(tag, message);
        appendToView("ℹ️ " + tag + ": " + message);
        appendToFile("INFO", tag, message);
    }

    public static void w(String tag, String message) {
        Log.w(tag, message);
        appendToView("⚠️ " + tag + ": " + message);
        appendToFile("WARN", tag, message);
    }

    // 🔹 Přidá text do UI logu
    private static void appendToView(String msg) {
        if (logView == null) return;
        uiHandler.post(() -> {
            logView.append(msg + "\n");
            int scrollAmount = logView.getLayout() == null ? 0 :
                    logView.getLayout().getLineTop(logView.getLineCount()) - logView.getHeight();
            logView.scrollTo(0, Math.max(scrollAmount, 0));
        });
    }

    // 🔹 Uloží log do souboru
    private static void appendToFile(String level, String tag, String message) {
        if (!fileLoggingEnabled || logFile == null) return;

        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
                .format(new Date());
        String logLine = String.format("%s [%s] %s: %s\n", timestamp, level, tag, message);

        try (FileWriter writer = new FileWriter(logFile, true)) {
            writer.append(logLine);
        } catch (IOException e) {
            Log.e("AppLogger", "Chyba při zápisu logu do souboru: " + e.getMessage());
        }
    }

    // 🔹 Smaže staré logy
    public static void clearLogFile() {
        if (logFile != null && logFile.exists()) {
            boolean deleted = logFile.delete();
            Log.i("AppLogger", deleted ? "Soubor logu vymazán." : "Nepodařilo se smazat log.");
        }
    }

    // 🔹 Vrátí cestu k souboru s logem
    public static String getLogFilePath() {
        return logFile != null ? logFile.getAbsolutePath() : "(soubor neexistuje)";
    }
}
