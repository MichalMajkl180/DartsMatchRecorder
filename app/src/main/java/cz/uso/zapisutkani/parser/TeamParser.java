package cz.uso.zapisutkani.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.uso.zapisutkani.data.AppDatabase;
import cz.uso.zapisutkani.data.Team;

public class TeamParser {

    /**
     * Načte všechny týmy z webové stránky dané ligy (sipky.org)
     * a uloží je do databáze s odkazem na daný leagueId.
     */
    public static List<Team> loadTeamsFromLeague(String leagueUrl, int leagueId, AppDatabase db) throws IOException {
        List<Team> result = new ArrayList<>();

        // 1️⃣ Načteme HTML stránky
        Document doc = Jsoup.connect(leagueUrl).get();

        // 2️⃣ Najdeme všechny týmy ve skupině (typicky <a href="?region=ulk&page=profil-druzstva&league_team=XXXXX">Název týmu</a>)
        Elements teamLinks = doc.select("a[href*='page=profil-druzstva']");

        for (Element link : teamLinks) {
            String teamName = link.text().trim();
            String teamUrl = "https://www.sipky.org/" + link.attr("href");
            String city = ""; // zatím prázdné, později můžeme doplnit z detailu týmu

            // 3️⃣ Zkontroluj, jestli už tým v databázi není
            Team existing = db.teamDao().findByName(teamName);
            if (existing == null) {
                Team team = new Team();
                team.teamName = teamName;
                team.city = city;
                team.leagueId = leagueId;
                db.teamDao().insert(team);
                result.add(team);
            }
        }

        return result;
    }
}
