import java.util.*;

public class Leaderboard {
    private final List<Player> players = new ArrayList<>();

    public void add(Player p) {
        players.add(p);
    }

    public void display() {
        System.out.println("\nLeaderboard:");
        for (Player p : players) {
            System.out.println(p.getName() + " : " + p.getScore());
        }
    }
}