package cz.uso.zapisutkani.repository;

import android.content.Context;

import java.util.List;

import cz.uso.zapisutkani.data.AppDatabase;
import cz.uso.zapisutkani.data.Team;
import cz.uso.zapisutkani.dao.TeamDao;
import cz.uso.zapisutkani.parser.TeamParser;
import cz.uso.zapisutkani.utils.AppLogger;

public class TeamRepository {
    private final AppDatabase db;
    private final TeamDao teamDao;

    public interface OnTeamsLoadedListener {
        void onLoaded(int newTeamsCount);
    }

    public TeamRepository(Context context) {
        db = AppDatabase.getInstance(context);
        teamDao = db.teamDao();
    }

    /**
     * 🔹 Načte týmy z webu (sipky.org) a uloží je do databáze
     * @param leagueUrl např. "https://www.sipky.org/?region=ulk&page=ligova-skupina&league=244485"
     * @param listener callback, který oznámí počet nově uložených týmů
     */
    public void updateTeamsFromWeb(String leagueUrl, OnTeamsLoadedListener listener) {
        new Thread(() -> {
            int insertedCount = 0;

            try {
                AppLogger.i("TeamRepository", "Stahuji data z: " + leagueUrl);

                List<TeamParser.ParsedTeam> parsedTeams = TeamParser.loadLeagueTeams(leagueUrl);
                AppLogger.i("TeamRepository", "Z webu načteno týmů: " + parsedTeams.size());

                for (TeamParser.ParsedTeam parsed : parsedTeams) {
                    Team existing = teamDao.findByName(parsed.name);

                    if (existing == null) {
                        // ⚙️ Vytvoří nový tým
                        Team team = new Team(parsed.name, extractLeagueIdFromUrl(leagueUrl));
                        team.setUrl(parsed.url); // 🔹 nově přidáno
                        teamDao.insertTeam(team);

                        insertedCount++;
                        AppLogger.d("TeamRepository", "Uložen nový tým: " + parsed.name);
                    } else {
                        AppLogger.d("TeamRepository", "Tým již existuje: " + parsed.name);
                    }
                }

                AppLogger.i("TeamRepository", "Uloženo nových týmů: " + insertedCount);
            } catch (Exception e) {
                AppLogger.e("TeamRepository", "Chyba při načítání týmů: " + e.getMessage());
            }

            if (listener != null) {
                listener.onLoaded(insertedCount);
            }
        }).start();
    }

    /**
     * 🔸 Pomocná metoda — extrahuje číslo ligy z URL (např. league=244485)
     */
    private int extractLeagueIdFromUrl(String url) {
        try {
            int index = url.indexOf("league=");
            if (index != -1) {
                String part = url.substring(index + 7);
                if (part.contains("&")) part = part.substring(0, part.indexOf("&"));
                return Integer.parseInt(part);
            }
        } catch (Exception e) {
            AppLogger.w("TeamRepository", "Nepodařilo se zjistit leagueId z URL: " + url);
        }
        return -1;
    }
}
