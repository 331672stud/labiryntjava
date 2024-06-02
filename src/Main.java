public class Main {
    public static void main(String[] args) {
        MazeSolverUIImpl ui = new MazeSolverUIImpl();
        
        ConsoleListener consoleInputListener = new ConsoleListener(ui);
        consoleInputListener.addObserver(ui); //obserwujemy UI
        new Thread(consoleInputListener).start();
    }
}