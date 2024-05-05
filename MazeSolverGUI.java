import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MazeSolverGUI extends JFrame {
    private JButton loadTextButton, loadBinaryButton, findPathButton, selectStartButton;
    private JPanel topPanel, bottomPanel, mazePanel;

    public MazeSolverGUI() {
        setTitle("Maze Solver");
        setSize(960, 540);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //inicjalizacja guzikow
        loadTextButton = new JButton("Wczytaj plik tekstowy");
        loadBinaryButton = new JButton("Wczytaj plik binarny");
        findPathButton = new JButton("Znajdź najkrótszą ścieżkę");
        selectStartButton = new JButton("Wybierz punkty Start-Koniec");

        //sprawdza czy wcisniete
        loadTextButton.addActionListener(new LoadTextListener());
        loadBinaryButton.addActionListener(new LoadBinaryListener());

        //wczytywanie
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.add(loadTextButton);
        topPanel.add(loadBinaryButton);

        //znajdz/wybierz start
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.add(findPathButton);
        bottomPanel.add(selectStartButton);

        //srodek
        mazePanel = new JPanel();
        mazePanel.setPreferredSize(new Dimension(400, 300));
        mazePanel.setBackground(Color.WHITE);

        //scrollbary
        JScrollPane scrollPane = new JScrollPane(mazePanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        //glowny panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    // wczytywanie tektu
    private class LoadTextListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                //czy dziala
                System.out.println("otwarto plik tekstowy: " + selectedFile.getName());
            }
        }
    }

    // wczytywanie binary
    private class LoadBinaryListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                // czy dziala
                System.out.println("Otwarto plik: " + selectedFile.getName());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MazeSolverGUI::new);
    }
}