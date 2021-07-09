package com.example.NbaQuickStats.model;

public class Player {
    private int id;
    private double pts;
    private double ast;
    private double reb;
    private double stl;
    private double blk;
    private double fgPct;
    private double threePtPct;
    private double ftPct;
    private String name;
    private String teamName;

    public Player(int id, double pts, double ast, double reb, double stl, double blk, double fgPct, double threePtPct, double ftPct, String name, String teamName) {
        this.id = id;
        this.pts = pts;
        this.ast = ast;
        this.reb = reb;
        this.stl = stl;
        this.blk = blk;
        this.fgPct = fgPct;
        this.threePtPct = threePtPct;
        this.ftPct = ftPct;
        this.name = name;
        this.teamName = teamName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPts() {
        return pts;
    }

    public void setPts(double pts) {
        this.pts = pts;
    }

    public double getAst() {
        return ast;
    }

    public void setAst(double ast) {
        this.ast = ast;
    }

    public double getReb() {
        return reb;
    }

    public void setReb(double reb) {
        this.reb = reb;
    }

    public double getStl() {
        return stl;
    }

    public void setStl(double stl) {
        this.stl = stl;
    }

    public double getBlk() {
        return blk;
    }

    public void setBlk(double blk) {
        this.blk = blk;
    }

    public double getFgPct() {
        return fgPct;
    }

    public void setFgPct(double fgPct) {
        this.fgPct = fgPct;
    }

    public double getThreePtPct() {
        return threePtPct;
    }

    public void setThreePtPct(double threePtPct) {
        this.threePtPct = threePtPct;
    }

    public double getFtPct() {
        return ftPct;
    }

    public void setFtPct(double ftPct) {
        this.ftPct = ftPct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
