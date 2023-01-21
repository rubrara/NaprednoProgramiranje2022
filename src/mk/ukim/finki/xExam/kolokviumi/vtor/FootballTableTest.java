package mk.ukim.finki.xExam.kolokviumi.vtor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Partial exam II 2016/2017
 */
public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}

class FootballTable {
    Map<String, Team> games;

    public FootballTable() {
        this.games = new HashMap<>();
    }

    public Team checkTeam(String name) {
        if (!games.containsKey(name))
            games.put(name, new Team(name));

        return games.get(name);
    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {
        Team home = checkTeam(homeTeam);
        Team away = checkTeam(awayTeam);

        if (homeGoals > awayGoals) {
            home.takeWin(homeGoals, awayGoals);
            away.takeLoss(awayGoals, homeGoals);
        } else if (homeGoals < awayGoals) {
            away.takeWin(awayGoals, homeGoals);
            home.takeLoss(homeGoals, awayGoals);
        } else {
            home.takeDraw(homeGoals);
            away.takeDraw(awayGoals);
        }
    }

    public void printTable() {
        List<Team> table = new ArrayList<>(games.values());

        table.sort(Comparator.comparing(Team::calculatePoints).thenComparing(Team::goalDiff).reversed().thenComparing(Team::getName));

        for (int i = 0; i < table.size(); i++) {
            System.out.printf("%2d. %s", i + 1, table.get(i));
        }
    }
}

class Team {
    String name;
    int givenGoals;
    int takenGoals;
    int wins;
    int draws;
    int lost;

    public Team(String name) {
        this.name = name;
        this.givenGoals = 0;
        this.takenGoals = 0;
        this.draws = 0;
        this.wins = 0;
        this.lost = 0;
    }


    public void takeWin(int givenGoals, int takenGoals) {
        wins += 1;
        this.givenGoals += givenGoals;
        this.takenGoals += takenGoals;
    }

    public void takeLoss(int givenGoals, int takenGoals) {
        lost += 1;
        this.givenGoals += givenGoals;
        this.takenGoals += takenGoals;
    }

    public void takeDraw(int goals) {
        this.draws += 1;
        this.takenGoals += goals;
        this.givenGoals += goals;
    }

    public int totalGamesPlayed() {
        return this.wins + this.lost + this.draws;
    }

    public int calculatePoints() {
        return this.wins * 3 + this.draws;
    }

    public String getName() {
        return name;
    }

    public int goalDiff() {
        return this.givenGoals - this.takenGoals;
    }

    public int getGivenGoals() {
        return givenGoals;
    }

    public int getTakenGoals() {
        return takenGoals;
    }

    public int getWins() {
        return wins;
    }

    public int getDraws() {
        return draws;
    }

    public int getLost() {
        return lost;
    }

    @Override
    public String toString() {
        return String.format("%-15s%5d%5d%5d%5d%5d\n", getName(), totalGamesPlayed(), getWins(), getDraws(), getLost(), calculatePoints());
    }

}
