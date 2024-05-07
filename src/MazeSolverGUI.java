import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MazeSolverGUI extends JFrame {
    private JButton loadTextButton, loadBinaryButton, findPathButton, selectStartButton;
    private JPanel topPanel, bottomPanel;
    private JScrollPane scrollPane;
    private MazePanel mazePanel;
    private char[][] mazeArray;

    public MazeSolverGUI() {
        setTitle("LabSolver");
        setSize(960, 540);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Inicjalizacja guzikow
        loadTextButton = new JButton("Wczytaj labirynt z pliku tekstowego");
        loadBinaryButton = new JButton("Wczytaj labirynt z pliku binarnego");
        findPathButton = new JButton("Znajdz najkrotsza sciezke");
        selectStartButton = new JButton("Wybierz punkt poczatkowy i koncowy");

        loadTextButton.setFocusable(false);
        loadBinaryButton.setFocusable(false);
        findPathButton.setFocusable(false);
        selectStartButton.setFocusable(false);

        // wczytywanie cos robi
        loadTextButton.addActionListener(new LoadTextListener());
        loadBinaryButton.addActionListener(new LoadBinaryListener());

        JLabel pole = new JLabel("MENU:");

        // guziki wczytywania
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.setBackground(Color.LIGHT_GRAY);
        topPanel.add(pole, BorderLayout.WEST);
        topPanel.add(loadTextButton);
        topPanel.add(loadBinaryButton);

        //guziki do pracy z labiryntem
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.add(findPathButton);
        bottomPanel.add(selectStartButton);
        bottomPanel.setBackground(Color.LIGHT_GRAY);

        // poczatkowy panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        // panel labiryntu
        mazePanel = new MazePanel();
        scrollPane = new JScrollPane(mazePanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        contentPanel.add(scrollPane, BorderLayout.CENTER);
        add(contentPanel);

        setVisible(true);
    }

    // Wczytywanie tekstowe
    private class LoadTextListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    loadMazeFromFile(selectedFile);
                    mazePanel.repaint();
                    adjustScrollPane();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // Binarne wczytywanie
    private class LoadBinaryListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    loadMazeFromFile(selectedFile);
                    mazePanel.repaint();
                    adjustScrollPane();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // Biedawczytywanie
    private void loadMazeFromFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        int numRows = 0;
        int numCols = 0;
        while ((line = reader.readLine()) != null) {
            numCols = Math.max(numCols, line.length());
            numRows++;
        }
        reader.close();

        mazeArray = new char[numRows][numCols];
        reader = new BufferedReader(new FileReader(file));
        int row = 0;
        while ((line = reader.readLine()) != null) {
            for (int col = 0; col < line.length(); col++) {
                mazeArray[row][col] = line.charAt(col);
            }
            row++;
        }
        reader.close();

        //dynamiczny rozmiar panelu
        mazePanel.setMazeSize(numRows, numCols);
    }

    //przestawia scrollbary
    private void adjustScrollPane() {
        SwingUtilities.invokeLater(() -> {
            scrollPane.getViewport().setViewPosition(new Point(0, 0));
            scrollPane.revalidate();
            scrollPane.repaint();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MazeSolverGUI::new);
    }

    // wewnetrzna klasa na labirynt
    private class MazePanel extends JPanel {
        private int cellSize = 5; //rozmiar komorki
        private int numRows = 0;
        private int numCols = 0;


        //rozmiar labiryntu
        public void setMazeSize(int numRows, int numCols) {
            this.numRows = numRows;
            this.numCols = numCols;
            revalidate();
            repaint();
        }


        @Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (mazeArray != null) {
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int mazeWidth = numCols * cellSize;
        int mazeHeight = numRows * cellSize;

        int offsetX = (panelWidth - mazeWidth) / 2;
        int offsetY = (panelHeight - mazeHeight) / 2;

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                if (mazeArray[row][col] == 'X') {
                    g.setColor(Color.BLACK);
                } else {
                    g.setColor(Color.WHITE);
                }
                int x = offsetX + col * cellSize;
                int y = offsetY + row * cellSize;
                g.fillRect(x, y, cellSize, cellSize);
            }
        }
    }
}

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(numCols * cellSize, numRows * cellSize);
        }
    }
}


