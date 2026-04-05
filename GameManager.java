import java.io.*;
import java.util.*;

public class GameManager {

    private final Queue<Question> questions = new LinkedList<>();
    private final Stack<String> history = new Stack<>();
    private final Leaderboard leaderboard = new Leaderboard();

    private int damage = 10;
    private int playerHP = 100;
    private int enemyHP = 100;

    public void startGame() {

        // 🔥 แก้แล้ว (ไม่ใช้ try-with-resources)
        Scanner sc = new Scanner(System.in);

        displayData("Enter name: ");
        String name = sc.nextLine();

        // 🎯 Difficulty (กันพิมพ์มั่ว)
        int choice = 0;

        while (true) {
            try {
                displayData("Select Difficulty:");
                displayData("1. Easy");
                displayData("2. Medium");
                displayData("3. Hard");
                displayData("Choice: ");

                choice = sc.nextInt();
                sc.nextLine();

                if (choice < 1 || choice > 3) {
                    throw new InvalidChoiceException("Please enter only 1-3");
                }

                break;

            } catch (InputMismatchException e) {
                displayData("Please enter numbers only!");
                sc.next();
            } catch (InvalidChoiceException e) {
                displayData(e.getMessage());
            }
        }

        switch (choice) {
            case 1 -> {
                damage = 5;
                playerHP = 120;
                enemyHP = 80;
            }
            case 2 -> {
                damage = 10;
                playerHP = 100;
                enemyHP = 100;
            }
            case 3 -> {
                damage = 15;
                playerHP = 80;
                enemyHP = 120;
            }
        }

        Player player = new Player(name, playerHP);
        Enemy enemy = new Enemy(enemyHP);

        // โหลดคำถาม
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0)
                questions.add(new AddQuestion());
            else
                questions.add(new MultiplyQuestion());
        }

        while (player.getHp() > 0 && enemy.getHp() > 0) {

            if (questions.isEmpty()) {
                for (int i = 0; i < 5; i++) {
                    if (i % 2 == 0)
                        questions.add(new AddQuestion());
                    else
                        questions.add(new MultiplyQuestion());
                }
            }

            Question q = questions.poll();
            int correct = q.ask();

            try {
                displayData("Answer: ");
                int ans = sc.nextInt();

                if (ans < 0) {
                    throw new InvalidChoiceException("Answer must be positive");
                }

                history.push("Correct: " + correct + " Your: " + ans);

                if (ans == correct) {
                    displayData("Correct! You hit enemy!");
                    enemy.takeDamage(damage);
                    player.addScore(10);
                } else {
                    displayData("Wrong! Enemy hits you!");
                    player.takeDamage(damage);
                }

            } catch (InvalidChoiceException e) {
                displayData(e.getMessage());
            } catch (Exception e) {
                displayData("number only ):<");
                sc.next();
            }

            displayFormatted("Player HP", player.getHp());
            displayFormatted("Enemy HP", enemy.getHp());
        }

        // 🔥 ไม่มี draw แล้ว
        if (player.getHp() > 0) {
            displayData("YOU WIN!");
        } else {
            displayData("ENEMY WINS!");
        }

        displayFormatted("Final Score", player.getScore());

        leaderboard.add(player);
        leaderboard.display();

        saveScore(player);

        showHistory(); // 🔥 ย้ายมาอยู่ใน method เลย
    }

    private void saveScore(Player p) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("score.txt", true))) {
            bw.write(p.getName() + " : " + p.getScore());
            bw.newLine();
        } catch (IOException e) {
            displayData("File error");
        }
    }

    private void showHistory() {
        displayData("\nHistory:");
        while (!history.isEmpty()) {
            displayData(history.pop());
        }
    }

    public <T> void displayData(T data) {
        System.out.println(data);
    }

    public <T> void displayFormatted(String label, T data) {
        System.out.println(label + ": " + data);
    }
}