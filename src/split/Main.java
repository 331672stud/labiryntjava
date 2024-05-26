public class Main {
    public static void main(String[] args) {
        MazeSolverUIImpl ui = new MazeSolverUIImpl();

        // Create a ConsoleInput object and add the UI as an observer
        ConsoleInput consoleInput = new ConsoleInput();
        consoleInput.addObserver(ui);

        // Start a separate thread to listen for console input
        Thread inputThread = new Thread(consoleInput);
        inputThread.start();

        // Keep the main thread running
        while (true) {
            // Add any other logic if needed
        }
    }
}
