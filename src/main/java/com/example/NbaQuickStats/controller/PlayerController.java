package com.example.NbaQuickStats.controller;
import com.example.NbaQuickStats.model.Player;
import com.example.NbaQuickStats.model.PlayerAdv;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@RestController
public class PlayerController {
    @Autowired
    private WebClient.Builder webClientBuilder;

    final int numLeaders = 10;
    private List<Integer> allPlayerIDs = new ArrayList<>();
    private List<Player> allPlayers = new ArrayList<>();
    private List<PlayerAdv> allPlayersAdv = new ArrayList<>();

    @RequestMapping("/")
    public String home() {
        getAllPlayers();
        getAllPlayersAdv();

        String welcomeMessage = "Welcome to NBA Quick Stats! ";
        welcomeMessage += "To view season leaders (top 10 in a category), visit the following URLs --> ";
        welcomeMessage += "Points Leaders: /pts-leaders, Assist Leaders: /ast-leaders, ";
        welcomeMessage += "Rebound Leaders: /reb-leaders, Block Leaders: /blk-leaders, ";
        welcomeMessage += "FG % Leaders: /fg-pct-leaders, 3PT % Leaders: /3pt-pct-leaders, FT % Leaders: /ft-pct-leaders. ";
        welcomeMessage += "To view players who averaged a certain range in a statistical category, you can visit: ";
        welcomeMessage += "/minPts=(MIN_PTS)&maxPts=(MAX_PTS), /minAst=(MIN_AST)&maxAst=(MAX_AST), /minReb=(MIN_REB)&maxReb=(MAX_REB). ";
        welcomeMessage += "This will give players who averaged within a specific range of points, assists and rebounds, respectively. ";
        welcomeMessage += "And finally, to view a player with a specific ID number, you can visit /(PLAYER_ID).";
        return welcomeMessage;
    }

    // View scoring leaders from 2020-21 NBA season
    @RequestMapping("/pts-leaders")
    public List<PlayerAdv> ptsLeaders() {
        getAllPlayers();
        getAllPlayersAdv();

        List<PlayerAdv> playersAdvCopy = allPlayersAdv;
        Collections.sort(playersAdvCopy,
                Comparator.comparingDouble(PlayerAdv::getPts).reversed());
        return playersAdvCopy.subList(0, numLeaders);
    }

    // Loop over 5 pages of NBA game logs, see which players appear, & take down their info
    private void getAllPlayers()  {
        if (allPlayerIDs.size() != 0) {
            return;
        }

        Random rand = new Random();
        int startPage = rand.nextInt(300) + 1;

        for (int i = startPage; i < startPage + 5; i++) {
            String url = "https://www.balldontlie.io/api/v1/stats?seasons[]=2020&per_page=100&page=" + i;
            String dataString = webClientBuilder.build()
                    .get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class).
                    block();

            JsonObject allData = JsonParser.parseString(dataString).getAsJsonObject();
            JsonArray gameData = allData.getAsJsonArray("data");

            for (int j = 0; j < gameData.size(); j++) {
                JsonObject gameInstance = gameData.get(j).getAsJsonObject();
                JsonObject playerData = gameInstance.getAsJsonObject("player");
                JsonObject teamData = gameInstance.getAsJsonObject("team");
                int playerId = playerData.get("id").getAsInt();

                if (! (allPlayerIDs.contains(playerId))) {
                    int id = playerId;
                    String name = "";
                    String teamName = "";

                    if (playerData.get("first_name") != null && playerData.get("last_name") != null) {
                        name += playerData.get("first_name").getAsString();
                        name += " ";
                        name += playerData.get("last_name").getAsString();
                    } else {
                        continue;
                    }

                    if (teamData.get("full_name") != null) {
                        teamName += teamData.get("full_name").getAsString();
                    } else {
                        continue;
                    }

                    allPlayerIDs.add(playerId);
                    allPlayers.add(new Player(id, name, teamName));
                }
            }
        }
    }

    // Take all the players we found before & query for their season statistics
    private void getAllPlayersAdv() {
        if (allPlayerIDs.size() == 0) {
            getAllPlayers();
        }

        if (allPlayersAdv.size() != 0) {
            return;
        }

        String playerStatsUrl = "https://www.balldontlie.io/api/v1/season_averages?season=2020";
        for (int id : allPlayerIDs) {
            playerStatsUrl += "&player_ids[]=" + id;
        }

        String playerStatsString = webClientBuilder.build()
                .get()
                .uri(playerStatsUrl)
                .retrieve()
                .bodyToMono(String.class).
                block();
        JsonObject playerStatsObj = JsonParser.parseString(playerStatsString).getAsJsonObject();
        JsonArray playerStatsArr = playerStatsObj.getAsJsonArray("data");

        for (int i = 0; i < playerStatsArr.size(); i++) {
            JsonObject playerStats = playerStatsArr.get(i).getAsJsonObject();
            int id = playerStats.get("player_id").getAsInt();
            Player player = findPlayerByID(id);
            double pts;
            double ast;
            double reb;
            double stl;
            double blk;
            double fgPct;
            double threePtPct;
            double ftPct;
            String name;
            String teamName;

            if (player != null) {
                name = player.getName();
                teamName = player.getTeamName();
            } else {
                continue;
            }

            if (playerStats.get("pts") != null) {
                pts = playerStats.get("pts").getAsDouble();
            } else {
                continue;
            }

            if (playerStats.get("ast") != null) {
                ast = playerStats.get("ast").getAsDouble();
            } else {
                continue;
            }

            if (playerStats.get("reb") != null) {
                reb = playerStats.get("reb").getAsDouble();
            } else {
                continue;
            }

            if (playerStats.get("stl") != null) {
                stl = playerStats.get("stl").getAsDouble();
            } else {
                continue;
            }

            if (playerStats.get("blk") != null) {
                blk = playerStats.get("blk").getAsDouble();
            } else {
                continue;
            }

            if (playerStats.get("fg_pct") != null) {
                fgPct = playerStats.get("fg_pct").getAsDouble();
            } else {
                continue;
            }

            if (playerStats.get("fg3_pct") != null) {
                threePtPct = playerStats.get("fg3_pct").getAsDouble();
            } else {
                continue;
            }

            if (playerStats.get("ft_pct") != null) {
                ftPct = playerStats.get("ft_pct").getAsDouble();
            } else {
                continue;
            }

            allPlayersAdv.add(new PlayerAdv(id, pts, ast, reb, stl, blk, fgPct, threePtPct, ftPct, name, teamName));
        }
    }

    // Obtains a 'Player' object by his ID number
    public Player findPlayerByID(int id) {
        Player playerByID = null;

        for (Player player : allPlayers) {
            if (player.getId() == id) {
                playerByID = player;
            }
        }

        return playerByID;
    }
}
