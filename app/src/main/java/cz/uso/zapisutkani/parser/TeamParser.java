package cz.uso.zapisutkani.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import cz.uso.zapisutkani.utils.AppLogger;

public class TeamParser {

    public static class ParsedTeam {
        public String name;
        public String url;

        public ParsedTeam(String name, String url) {
            this.name = name;
            this.url = url;
        }
    }

    public static List<ParsedTeam> loadLeagueTeams(String leagueUrl) {
        List<ParsedTeam> teams = new ArrayList<>();

        try {
            AppLogger.i("TeamParser", "Načítám týmy z URL: " + leagueUrl);

            Document doc = Jsoup.connect(leagueUrl)
                    .timeout(15000)
                    .userAgent("Mozilla/5.0 (Android) Jsoup Parser")
                    .get();

            // najdeme všechny odkazy na týmy
            Elements links = doc.select("td.left a[href*='league_team=']");
            AppLogger.i("TeamParser", "Nalezeno odkazů: " + links.size());

            for (Element link : links) {
                String name = link.text().trim();
                String url = link.absUrl("href"); // převede na plnou URL
                teams.add(new ParsedTeam(name, url));
                AppLogger.d("TeamParser", "Tým: " + name + " -> " + url);
            }

            AppLogger.i("TeamParser", "Celkem načteno týmů: " + teams.size());
        } catch (Exception e) {
            AppLogger.e("TeamParser", "Chyba při načítání týmů: " + e.getMessage());
        }

        return teams;
    }
}
