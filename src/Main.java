public class Main {
    public static void main(String[] args) {
        MazeSolverUIImpl ui = new MazeSolverUIImpl();
<<<<<<< HEAD
        
        ConsoleListener consoleInputListener = new ConsoleListener(ui);
        consoleInputListener.addObserver(ui); //obserwujemy UI
        new Thread(consoleInputListener).start();
=======
>>>>>>> a4694f2 (pushhh)
    }
}
