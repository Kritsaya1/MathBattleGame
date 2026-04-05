public class Player extends Character {
    private int score;

    public Player(String name, int hp) {
        super(name, hp);
        this.score = 0;
    }

    public Player(String name) {
        super(name, 100);
        this.score = 0;
    }

    public String getName() {
        return name;
    }

    public void addScore(int s) {
        score += s;
    }

    public int getScore() {
        return score;
    }
}