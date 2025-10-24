package cz.uso.zapisutkani.parser;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

import cz.uso.zapisutkani.dao.LeagueDao;
import cz.uso.zapisutkani.data.AppDatabase;
import cz.uso.zapisutkani.data.Team;
import cz.uso.zapisutkani.utils.AppLogger;

public class TeamParser {

    public static List<Team> loadTeams(int leagueID, Context context) {
        AppLogger.d("TeamParser", "Načítám týmy pro LeagueID: " + leagueID);

        LeagueDao leagueDao = AppDatabase.getInstance(context).leagueDao();
        List<Team> teams = leagueDao.getTeamsByLeague(leagueID);

        if (teams == null) teams = new ArrayList<>();
        AppLogger.d("TeamParser", "Počet načtených týmů: " + teams.size());
        for (Team t : teams) {
            AppLogger.d("TeamParser", "Tým: " + t.getName() + " (id=" + t.getId() + ")");
        }
        return teams;
    }
}
