package cz.uso.zapisutkani.db;

import android.content.Context;
import android.database.sqlite.*;
import cz.uso.zapisutkani.utils.AppLogger;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "darts.db";
    private static final int VERSION = 1;

    public MyDatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE League (id INTEGER PRIMARY KEY, name TEXT NOT NULL)");
        db.execSQL("CREATE TABLE Team (id INTEGER PRIMARY KEY, name TEXT NOT NULL, league_id INTEGER NOT NULL, FOREIGN KEY(league_id) REFERENCES League(id))");

        db.execSQL("INSERT INTO League (id, name) VALUES (1, 'Test Liga')");
        db.execSQL("INSERT INTO Team (id, name, league_id) VALUES (1, 'Tým A', 1)");
        db.execSQL("INSERT INTO Team (id, name, league_id) VALUES (2, 'Tým B', 1)");
        db.execSQL("INSERT INTO Team (id, name, league_id) VALUES (3, 'Tým C', 1)");

        AppLogger.d("MyDatabaseHelper", "Databáze vytvořena a naplněna testovacími daty");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Team");
        db.execSQL("DROP TABLE IF EXISTS League");
        onCreate(db);
    }
}
