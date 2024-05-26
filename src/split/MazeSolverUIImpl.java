import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MazeSolverUIImpl extends JFrame implements MazeSolverUI {
    private JButton loadTextButton, loadBinaryButton, findPathButton, selectStartButton, selectEndButton, saveLabirynthButton;
    private JPanel topPanel, bottomPanel, sidePanel, errorPanel;
    private JScrollPane scrollPane;
    private MazePanel mazePanel;
    private List<JLabel> errorMessages;
    private MazeOperations mazeArray;

    public MazeSolverUIImpl() {
        setTitle("LabSolver");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeUI();

        mazeArray = new MazeOperations(); // Initialize MazeOperations

        setVisible(true);
    }

    private void initializeUI() {
        // Initialization of buttons and panels
        loadTextButton = new JButton("Wczytaj labirynt z pliku tekstowego");
        loadBinaryButton = new JButton("Wczytaj labirynt z pliku binarnego");
        findPathButton = new JButton("Znajdz najkrotsza sciezke");
        selectStartButton = new JButton("Wybierz punkty początkowe");
        selectEndButton = new JButton("Wybierz punkty końcowe");
        saveLabirynthButton = new JButton("Zapisz labirynt do pliku");

        loadTextButton.setFocusable(false);
        loadBinaryButton.setFocusable(false);
        findPathButton.setFocusable(false);
        selectStartButton.setFocusable(false);
        selectEndButton.setFocusable(false);
        saveLabirynthButton.setFocusable(false);

        loadTextButton.addActionListener(new LoadTextListener());
        loadBinaryButton.addActionListener(new LoadBinaryListener());
        findPathButton.addActionListener(new FindPathListener());
        selectStartButton.addActionListener(new SelectStartListener());
        selectEndButton.addActionListener(new SelectEndListener());
        saveLabirynthButton.addActionListener(new SaveLabirynthListener());

        JLabel pole = new JLabel("MENU:");

        // Setting up panels and buttons
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.setBackground(Color.LIGHT_GRAY);
        topPanel.add(pole, BorderLayout.WEST);
        topPanel.add(loadTextButton);
        topPanel.add(loadBinaryButton);

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.setBackground(Color.LIGHT_GRAY);
        bottomPanel.add(findPathButton);
        bottomPanel.add(selectStartButton);
        bottomPanel.add(selectEndButton);
        bottomPanel.add(saveLabirynthButton);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        mazePanel = new MazePanel();
        scrollPane = new JScrollPane(mazePanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        contentPanel.add(scrollPane, BorderLayout.CENTER);

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

        findPathButton.setEnabled(false);
        selectStartButton.setEnabled(false);
        selectEndButton.setEnabled(false);
        saveLabirynthButton.setEnabled(false);

        errorMessages = new ArrayList<>();
    }

    @Override
    public void loadTextMaze(File file) {
        try {
            loadMazeFromFile(file);
            mazePanel.repaint();
            adjustScrollPane();
            findPathButton.setEnabled(true);
            selectStartButton.setEnabled(true);
            selectEndButton.setEnabled(true);
            saveLabirynthButton.setEnabled(true);
            displayErrorMessage("Wczytano labirynt");
        } catch (IOException ex) {
            displayErrorMessage("Nie udało się wczytać labiryntu z pliku: " + ex.getMessage());
        }
    }

    @Override
    public void loadBinaryMaze(File file) {
        try {
            loadMazeFromBinaryFile(file);
            mazePanel.repaint();
            adjustScrollPane();
            findPathButton.setEnabled(true);
            selectStartButton.setEnabled(true);
            SaveLabirynthButton.setEnabled(true);
        } catch (IOException ex) {
            displayErrorMessage("Nie udało się wczytać labiryntu z pliku binarnego: " + ex.getMessage());
        }
    }

    @Override
    public void findPath() {
        displayErrorMessage("Niezaimplementowane: ścieżka");
    }

    @Override
    public void selectStart() {
        displayErrorMessage("Niezaimplementowane: start");
    }

    @Override
    public void selectEnd() {
        displayErrorMessage("Niezaimplementowane: koniec");
    }

    @Override
    public void saveMaze() {
        writeToFile();
    }

    private void writeToFile() {
        JFileChooser zapisChooser = new JFileChooser();
        zapisChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        zapisChooser.setSelectedFile(new File("output.txt"));
        zapisChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text files (*.txt)", "txt"));

        int result = zapisChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = zapisChooser.getSelectedFile();

            try {
                FileWriter fileWriter = new FileWriter(selectedFile);
                BufferedWriter writer = new BufferedWriter(fileWriter);

                for (int i = 0; i < mazeArray.length; i++) {
                    for (int j = 0; j < mazeArray[i].length; j++) {
                        writer.write(mazeArray[i][j]);
                    }
                    writer.write(System.lineSeparator());
                }

                writer.close();

                displayErrorMessage("Zapisano");
            } catch (IOException e) {
                displayErrorMessage("Zapis się nie udał: " + e.getMessage());
            }
        }
    }

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

        mazePanel.setMazeSize(numRows, numCols);
    }

    private void loadMazeFromBinaryFile(File file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            // Parsing binary file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int readBytesAsInt(FileInputStream inputStream, int bytesToRead) throws IOException {
        byte[] buffer = new byte[bytesToRead];
        int bytesRead = inputStream.read(buffer);

        if (bytesRead != bytesToRead) {
            throw new IOException("nie udało się wczytać bajtów");
        }

        int result = 0;
        for (int i = 0; i < bytesToRead; i++) {
            // przesunięcie
            result = (result << 8) | (buffer[i] & 0xFF);
        }

        return result;
    }

    private void adjustScrollPane() {
        SwingUtilities.invokeLater(() -> {
            scrollPane.getViewport().setViewPosition(new Point(0, 0));
            scrollPane.revalidate();
            scrollPane.repaint();
        });
    }

    private void displayErrorMessage(String message) {
        JLabel errorMessageLabel = new JLabel();
        errorMessageLabel.setText("<html>" + message + "</html>");
        errorMessageLabel.setMaximumSize(new Dimension(200, Integer.MAX_VALUE));
        errorMessageLabel.setBackground(Color.WHITE);
        errorMessageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        errorMessageLabel.setOpaque(true);

        errorMessages.add(errorMessageLabel);

        if (errorMessages.size() > 50) {
            errorPanel.remove(0);
            errorMessages.remove(0);
        }

        errorPanel.add(errorMessageLabel);
        errorPanel.revalidate();
        errorPanel.repaint();
    }

    private class MazePanel extends JPanel {
        private int cellSize = 5;
        private int numRows = 0;
        private int numCols = 0;

        public void setMazeSize(int numRows, int numCols) {
            this.numRows = numRows;
            this.numCols = numCols;
            if (this.numRows <= 20 && this.numCols <= 20) {
                this.cellSize = 10;
            }
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
                        switch (mazeArray[row][col]) {
                            case 'X':
                                g.setColor(Color.BLACK);
                                break;
                            case ' ':
                                g.setColor(Color.WHITE);
                                break;
                            case 'P':
                                g.setColor(Color.GREEN);
                                break;
                            case 'K':
                                g.setColor(Color.RED);
                                break;
                            case 'S':
                                g.setColor(Color.YELLOW);
                                break;
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

    private class LoadTextListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("pliki tekstowe .txt", "txt");
            fileChooser.setFileFilter(filter);
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                loadTextMaze(selectedFile);
            }
        }
    }

    private class LoadBinaryListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("pliki binarne .bin", "bin");
            fileChooser.setFileFilter(filter);
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                loadBinaryMaze(selectedFile);
            }
        }
    }

    private class FindPathListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            findPath();
        }
    }

    private class SelectStartListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            selectStart();
        }
    }

    private class SelectEndListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            selectEnd();
        }
    }

    private class SaveLabirynthListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            saveMaze();
        }
    }
}