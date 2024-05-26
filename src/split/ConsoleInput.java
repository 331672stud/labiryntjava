import java.util.Observable;
import java.util.Scanner;

public class ConsoleInput extends Observable implements Runnable {
    private Scanner scanner;

    public ConsoleInput() {
        scanner = new Scanner(System.in);
    }

    @Override
    public void run() {
        // Continuously read input from the console and notify observers
        while (true) {
            String input = scanner.nextLine();
            setChanged();
            notifyObservers(input);
        }
    }
}