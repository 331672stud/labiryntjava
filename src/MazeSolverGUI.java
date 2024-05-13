import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class MazeSolverGUI extends JFrame {
    private JButton loadTextButton, loadBinaryButton, findPathButton, selectStartButton, SaveLabirynthButton;
    private JPanel topPanel, bottomPanel, sidePanel, errorPanel;
    private JScrollPane scrollPane;
    private MazePanel mazePanel;
    private List<JPanel> errorMessages;
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
        SaveLabirynthButton = new JButton("Zapisz labirynt do pliku");

        loadTextButton.setFocusable(false);
        loadBinaryButton.setFocusable(false);
        findPathButton.setFocusable(false);
        selectStartButton.setFocusable(false);
        SaveLabirynthButton.setFocusable(false);

        // wczytywanie cos robi
        loadTextButton.addActionListener(new LoadTextListener());
        loadBinaryButton.addActionListener(new LoadBinaryListener());
        findPathButton.addActionListener(new FindPathListener());
        selectStartButton.addActionListener(new SelectStartListener());
        SaveLabirynthButton.addActionListener(new SaveLabirynthListener());

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
        bottomPanel.setBackground(Color.LIGHT_GRAY);
        bottomPanel.add(findPathButton);
        bottomPanel.add(selectStartButton);
        bottomPanel.add(SaveLabirynthButton);

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

        // Boczny Panel
        sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());
        errorPanel = new JPanel();
        errorPanel.setLayout(new BoxLayout(errorPanel, BoxLayout.Y_AXIS));
        errorPanel.setBackground(Color.GRAY);
        sidePanel.add(errorPanel, BorderLayout.CENTER);
        sidePanel.setPreferredSize(new Dimension(200, getHeight()));
        sidePanel.setBackground(Color.GRAY);

        add(contentPanel, BorderLayout.CENTER);
        add(sidePanel, BorderLayout.EAST);

        //wygaszenie guzików
        findPathButton.setEnabled(false);
        selectStartButton.setEnabled(false);
        SaveLabirynthButton.setEnabled(false);

        errorMessages = new ArrayList<>();

        setVisible(true);
    }

    // Wczytywanie tekstowe
    private class LoadTextListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("pliki tekstowe .txt", "txt");
            fileChooser.setFileFilter(filter);
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    loadMazeFromFile(selectedFile);
                    mazePanel.repaint();
                    adjustScrollPane();
                    //Włączamy guziki(beta)
                    findPathButton.setEnabled(true);
                    selectStartButton.setEnabled(true);
                    SaveLabirynthButton.setEnabled(true);
                } catch (IOException ex) {
                    displayErrorMessage("Nie udało się wczytać labiryntu z pliku: " + ex.getMessage());
                }
            }
        }
    }

    // Binarne wczytywanie
    private class LoadBinaryListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("pliki binarne .bin", "bin");
            fileChooser.setFileFilter(filter);
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    loadMazeFromBinaryFile(selectedFile);
                    mazePanel.repaint();
                    adjustScrollPane();
                    // guziki
                    findPathButton.setEnabled(true);
                    selectStartButton.setEnabled(true);
                    SaveLabirynthButton.setEnabled(true);
                } catch (IOException ex) {
                    displayErrorMessage("Nie udało się wczytać labiryntu z pliku binarnego: " + ex.getMessage());
                }
            }
        }
    }

    // Guzik na znajdowanie ścieżki
    private class FindPathListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // na razie nie ma
            displayErrorMessage("Niezaimplementowane: ścieżka");
        }
    }

    // ActionListener for selecting start point
    private class SelectStartListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // na razie nie ma
            displayErrorMessage("Niezaimplementowane: start-koniec");
        }
    }

    //Listener do Zapisu
    private class     SaveLabirynthListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //WriteToFile(maze);
            displayErrorMessage("Niezaimplementowane: start-koniec");
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

    //Biedabinary wczytywanie
    private void loadMazeFromBinaryFile(File file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            //Nagłówek
            int fileId = readInt(fileInputStream, 32);
            int escape = readInt(fileInputStream, 8);
            int columns = readInt(fileInputStream, 16);
            int lines = readInt(fileInputStream, 16);
            int entryX = readInt(fileInputStream, 16);
            int entryY = readInt(fileInputStream, 16);
            int exitX = readInt(fileInputStream, 16);
            int exitY = readInt(fileInputStream, 16);
            readInt(fileInputStream, 96); // Reserved
            int counter = readInt(fileInputStream, 32);
            int solutionOffset = readInt(fileInputStream, 32);
            int separator = readInt(fileInputStream, 8);
            int wall = readInt(fileInputStream, 8);
            int path = readInt(fileInputStream, 8);
            int numRows = 0;
            int numCols = 0;
            mazeArray = new char[lines][columns];
            // czytanko w pętelce
            while (fileInputStream.available() > 0) {
                int separatorValue = readInt(fileInputStream, 8);
                int value = readInt(fileInputStream, 8);
                int count = readInt(fileInputStream, 8);
                for(int i=0;i<=count;i++){
                    if(value==wall){
                        mazeArray[numRows][numCols]='X';
                    } else {
                        mazeArray[numRows][numCols]=' ';
                    }
                    numCols++;
                    if(numCols==columns){
                        numRows++;
                        numCols=0;
                    }
                }
            }
            mazeArray[entryY-1][entryX-1]='P';
            mazeArray[exitY-1][exitX-1]='K';
            //dynamiczny rozmiar panelu
            mazePanel.setMazeSize(lines, columns);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int readInt(InputStream inputStream, int numBits) throws IOException {
        int result = 0;
        for (int i = 0; i < numBits; i++) {
            result |= (inputStream.read() & 0x01) << i;
        }
        return result;
    }

    //przestawia scrollbary
    private void adjustScrollPane() {
        SwingUtilities.invokeLater(() -> {
            scrollPane.getViewport().setViewPosition(new Point(0, 0));
            scrollPane.revalidate();
            scrollPane.repaint();
        });
    }

    // wyświetla błędy
    private void displayErrorMessage(String message) {
        // Panel na komunikacje
        JPanel errorBox = new JPanel();
        errorBox.setBackground(Color.WHITE);
        errorBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel errorMessageLabel = new JLabel(message);
        errorBox.add(errorMessageLabel);

        // dodaje panel z wiadomością do panelu głównego
        errorMessages.add(errorBox);

        // Maks 5 wiadomości
        if (errorMessages.size() > 5) {
            errorPanel.remove(0);
            errorMessages.remove(0);
        }

        // odświeżanie panelu
        errorPanel.add(errorBox);
        errorPanel.revalidate();
        errorPanel.repaint();
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
                switch(mazeArray[row][col]) {
                    case 'X':
                        g.setColor(Color.BLACK);
                      break;
                    case ' ':
                        g.setColor(Color.WHITE);
                      break;
                    case 'P':
                        g.setColor(Color.GREEN);
                    case 'K':
                        g.setColor(Color.RED);
                    case 'S':
                        g.setColor(Color.YELLOW);
                    default:
                        displayErrorMessage("Nieznany znak w labiryncie");
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


