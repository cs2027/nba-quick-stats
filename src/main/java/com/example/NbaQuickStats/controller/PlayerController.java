package com.example.NbaQuickStats.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerController {
    @RequestMapping("/")
    public String home() {
        String welcomeMessage = "Welcome to NBA Quick Stats! ";
        welcomeMessage += "To view season leaders (top 20 in a category), visit the following URLs --> ";
        welcomeMessage += "Points Leaders: /pts-leaders, Assist Leaders: /ast-leaders, ";
        welcomeMessage += "Rebound Leaders: /reb-leaders, Block Leaders: /blk-leaders, ";
        welcomeMessage += "FG % Leaders: /fg-pct-leaders, 3PT % Leaders: /3pt-pct-leaders, FT % Leaders: /ft-pct-leaders. ";
        welcomeMessage += "To view players who averaged a certain range in a statistical category, you can visit: ";
        welcomeMessage += "/minPts=(MIN_PTS)&maxPts=(MAX_PTS), /minAst=(MIN_AST)&maxAst=(MAX_AST), /minReb=(MIN_REB)&maxReb=(MAX_REB). ";
        welcomeMessage += "This will give players who averaged within a specific range of points, assists and rebounds, respectively. ";
        welcomeMessage += "And finally, to view a player with a specific ID number, you can visit /(PLAYER_ID).";
        return welcomeMessage;
    }
}
