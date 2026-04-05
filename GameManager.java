import java.io.*;
import java.util.*;

public class GameManager {

    // Collection with Generics (Requirement 2.5)
    private final Queue<Question> questions = new LinkedList<>();
    private final Stack<String> history = new Stack<>();
    private final Leaderboard leaderboard = new Leaderboard();

    public void startGame() {
        try (Scanner sc = new Scanner(System.in)) {

            displayData("Enter name: ");
            String name = sc.nextLine();

            Player player = new Player(name);
            Enemy enemy = new Enemy();

            // โหลดคำถามเริ่มต้น
            for (int i = 0; i < 10; i++) {
                if (i % 2 == 0)
                    questions.add(new AddQuestion());
                else
                    questions.add(new MultiplyQuestion());
            }

            // 🔥 Game Loop
            while (player.getHp() > 0 && enemy.getHp() > 0) {

                // ถ้าคำถามหมด → generate ใหม่
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

                    // Custom Exception (Requirement 2.3)
                    if (ans < 0) {
                        throw new InvalidChoiceException("Answer must be positive");
                    }

                    history.push("Correct: " + correct + " Your: " + ans);

                    int damage = 10;

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

            // Result
            if (player.getHp() > 0) {
                displayData("YOU WIN!");
            } else {
                displayData("ENEMY WINS!");
            }

            displayFormatted("Final Score", player.getScore());

            leaderboard.add(player);
            leaderboard.display();

            saveScore(player);
        }

        showHistory();
    }

    // ===============================
    // 📁 File I/O (Requirement 2.1)
    // ===============================
    // This method writes player score to a file using append mode.
    // Try-with-resources ensures the file is closed automatically.
    private void saveScore(Player p) {
        if (p == null) return;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("score.txt", true))) {
            bw.write(p.getName() + " : " + p.getScore());
            bw.newLine();
        } catch (IOException e) {
            displayData("File error: " + e.getMessage());
        }
    }

    // ===============================
    // 📚 Stack (History Feature)
    // ===============================
    private void showHistory() {
        displayData("\nHistory:");
        while (!history.isEmpty()) {
            displayData(history.pop());
        }
    }

    public Queue<Question> getQuestions() {
        return questions;
    }

    // ============================================
    // 🔥 Parametric Polymorphism (Requirement 2.6)
    // ============================================
    // Generic method allows handling multiple data types with one method
    // improving reusability and flexibility
    public <T> void displayData(T data) {
        System.out.println(data);
    }

    // Generic method with label formatting
    public <T> void displayFormatted(String label, T data) {
        System.out.println(label + ": " + data);
    }
}