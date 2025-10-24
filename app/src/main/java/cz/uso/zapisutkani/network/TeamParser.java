package cz.uso.zapisutkani.network;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;

public class TeamParser {

    public static class ParsedTeam {
        public String name;
        public String url;
    }

    // Stažení seznamu týmů ze stránky ligové skupiny
    public static List<ParsedTeam> loadLeagueTeams(String leagueUrl) throws Exception {
        Document doc = Jsoup.connect(leagueUrl).get();
        List<ParsedTeam> teams = new ArrayList<>();

        Elements links = doc.select("a[href*='page=profil-druzstva']");
        for (Element link : links) {
            ParsedTeam team = new ParsedTeam();
            team.name = link.text().trim();
            String href = link.attr("href");
            if (!href.startsWith("https")) {
                team.url = "https://www.sipky.org/" + href;
            } else {
                team.url = href;
            }
            teams.add(team);
        }

        return teams;
    }
}
