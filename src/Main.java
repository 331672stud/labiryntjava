public class Main {
    public static void main(String[] args) {
        // Start UI
        MazeSolverUIImpl ui = new MazeSolverUIImpl();
        
        // Start console input listener
        ConsoleListener consoleInputListener = new ConsoleListener(ui);
        consoleInputListener.addObserver(ui); // Register UI as an observer
        new Thread(consoleInputListener).start();
    }
}