import java.util.Random;

public class MultiplyQuestion implements Question {
    @Override
    public int ask() {
        Random r = new Random();
        int a = r.nextInt(10);
        int b = r.nextInt(10);
        System.out.println(a + " * " + b + " = ?");
        return a * b;
    }
}