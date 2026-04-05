import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                GameManager game = new GameManager();
                game.startGame();

                int choice;

                OUTER:
                while (true) {
                    try {
                        System.out.println("1. Play again");
                        System.out.println("2. Exit");
                        System.out.print("Choose: ");
                        choice = sc.nextInt();
                        sc.nextLine();
                        switch (choice) {
                            case 1 -> {
                                break OUTER; // เล่นต่อ
                            }
                            case 2 -> {
                                System.out.println("Goodbye!");
                                return; // ออกจากโปรแกรมเลย
                            }
                            default -> System.out.println("Please enter only 1 or 2");
                        }
                    }catch (Exception e) {
                        System.out.println("Numbers only!");
                        sc.next(); // ล้างค่าที่พิมพ์ผิด
                    }           }
            }
        }
    }
}