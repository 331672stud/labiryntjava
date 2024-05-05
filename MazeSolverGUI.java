import javax.swing.*;
import java.awt.*;

public class MazeSolverGUI extends JFrame {
    private JButton loadTextButton, loadBinaryButton, findPathButton, selectStartButton;
    private JPanel topPanel, bottomPanel, mazePanel;

    public MazeSolverGUI() {
        setTitle("Maze Solver");
        setSize(960, 540);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //guziki
        loadTextButton = new JButton("Wczytaj plik tekstowy");
        loadBinaryButton = new JButton("Wczytaj plik binarny");
        findPathButton = new JButton("Znajdź najkrótszą ścieżkę");
        selectStartButton = new JButton("Wybierz punkty Start-Koniec");
        //menu wczytywania
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.add(loadTextButton);
        topPanel.add(loadBinaryButton);
        //wyszukiwanie/wybieranie
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.add(findPathButton);
        bottomPanel.add(selectStartButton);
        //panel srodkowy
        mazePanel = new JPanel();
        mazePanel.setPreferredSize(new Dimension(400, 300));
        mazePanel.setBackground(Color.WHITE);
        //scrollbary
        JScrollPane scrollPane = new JScrollPane(mazePanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MazeSolverGUI::new);
    }
}

