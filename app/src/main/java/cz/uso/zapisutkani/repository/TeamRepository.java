package cz.uso.zapisutkani.repository;

import android.content.Context;

import java.util.List;

import cz.uso.zapisutkani.data.AppDatabase;
import cz.uso.zapisutkani.data.Team;
import cz.uso.zapisutkani.network.TeamParser;

public class TeamRepository {
    private final AppDatabase db;

    public interface OnTeamsLoadedListener {
        void onLoaded(int count);
    }

    public TeamRepository(Context context) {
        db = AppDatabase.getInstance(context);
    }

    // 🔹 Načte týmy z webu a uloží je do databáze
    public void updateTeamsFromWeb(String leagueUrl, OnTeamsLoadedListener listener) {
        new Thread(() -> {
            int count = 0;
            try {
                List<TeamParser.ParsedTeam> parsedTeams = TeamParser.loadLeagueTeams(leagueUrl);

                for (TeamParser.ParsedTeam p : parsedTeams) {
                    Team existing = db.teamDao().findByName(p.name);
                    if (existing == null) {
                        Team team = new Team(p.name, 244485); // ✅ použij konstruktor
                        db.teamDao().insertTeam(team);
                        count++;
                    }
                }

                System.out.println("✅ Načteno " + count + " nových týmů (" + parsedTeams.size() + " celkem)");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (listener != null) {
                listener.onLoaded(count);
            }
        }).start();
    }
}
