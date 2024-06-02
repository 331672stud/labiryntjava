import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class ConsoleListener implements Runnable, Observable {
    private MazeSolverUIImpl ui;
    private List<Observer> observers = new ArrayList<>();

    public ConsoleListener(MazeSolverUIImpl ui) {
        this.ui = ui;
    }

    //metody Observable
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String input) {
        for (Observer observer : observers) {
            observer.update(input);
        }
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (ui.isRunning()) {
            String input = scanner.nextLine().trim().toLowerCase();
            notifyObservers(input);
        }
        scanner.close();
    }


}