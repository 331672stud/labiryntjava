import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MazeSolverUIImpl extends JFrame implements MazeSolverUI {
    private JButton loadTextButton, loadBinaryButton, findPathButton, selectStartButton, selectEndButton, SaveLabirynthButton;
    private JPanel topPanel, bottomPanel, sidePanel, errorPanel;
    private JScrollPane scrollPane;
    private MazePanel mazePanel;
    private List<JLabel> errorMessages;
    public MazeSolverUIImpl() {
        setTitle("LabSolver");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loadTextButton = new JButton("Wczytaj labirynt z pliku tekstowego");
        loadBinaryButton = new JButton("Wczytaj labirynt z pliku binarnego");
        findPathButton = new JButton("Znajdz najkrotsza sciezke");
        selectStartButton = new JButton("Wybierz punkty początkowe");
        selectEndButton = new JButton("Wybierz punkty końcowe");
        SaveLabirynthButton = new JButton("Zapisz labirynt do pliku");

        loadTextButton.setFocusable(false);
        loadBinaryButton.setFocusable(false);
        findPathButton.setFocusable(false);
        selectStartButton.setFocusable(false);
        selectEndButton.setFocusable(false);
        SaveLabirynthButton.setFocusable(false);

        loadTextButton.addActionListener(new LoadTextListener());
        loadBinaryButton.addActionListener(new LoadBinaryListener());
        findPathButton.addActionListener(new FindPathListener());
        selectStartButton.addActionListener(new SelectStartListener());
        selectEndButton.addActionListener(new SelectEndListener());
        SaveLabirynthButton.addActionListener(new SaveLabirynthListener());

        JLabel pole = new JLabel("MENU:");

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
        bottomPanel.add(SaveLabirynthButton);

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
        SaveLabirynthButton.setEnabled(false);

        errorMessages = new ArrayList<>();

        setVisible(true);
    }

    private class LoadTextListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("pliki tekstowe .txt", "txt");
            fileChooser.setFileFilter(filter);
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    MazeOperations maze = new MazeOperations();
                    maze.ReadTextMaze(selectedFile);
                    mazePanel.setMazeSize(maze.getMazeHeight(),maze.getMazeWidth());
                    mazePanel.repaint();
                    adjustScrollPane();
                    findPathButton.setEnabled(true);
                    selectStartButton.setEnabled(true);
                    selectEndButton.setEnabled(true);
                    SaveLabirynthButton.setEnabled(true);
                    displayErrorMessage("Wczytano labirynt");
                } catch (IOException ex) {
                    displayErrorMessage("Nie udało się wczytać labiryntu z pliku: " + ex.getMessage());
                }
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
                try {
                    MazeOperations maze = new MazeOperations();
                    maze.loadMazeFromBinaryFile(selectedFile);
                    mazePanel.setMazeSize(maze.getMazeHeight(),maze.getMazeWidth());
                    mazePanel.repaint();
                    adjustScrollPane();
                    findPathButton.setEnabled(true);
                    selectStartButton.setEnabled(true);
                    SaveLabirynthButton.setEnabled(true);
                } catch (IOException ex) {
                    displayErrorMessage("Nie udało się wczytać labiryntu z pliku binarnego: " + ex.getMessage());
                }
            }
        }
    }

    private class FindPathListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            displayErrorMessage("Niezaimplementowane: ścieżka");
        }
    }

    private class SelectStartListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            displayErrorMessage("Niezaimplementowane: start");
        }
    }

    private class SelectEndListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            displayErrorMessage("Niezaimplementowane: koniec");
        }
    }

    private class SaveLabirynthListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser zapisChooser = new JFileChooser();
            zapisChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            zapisChooser.setSelectedFile(new File("output.txt"));
            zapisChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text files (*.txt)", "txt")); 
            
            int result = zapisChooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                maze.SaveMazeArrayToFile(zapisChooser.getSelectedFile());            
            }
        }
    }

    private void adjustScrollPane() {
        SwingUtilities.invokeLater(() -> {
            scrollPane.getViewport().setViewPosition(new Point(0, 0));
            scrollPane.revalidate();
            scrollPane.repaint();
        });
    }

    @Override
    public void displayErrorMessage(String message) {
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

    @Override
    public JFrame getFrame() {
        return this;
    }

    private class MazePanel extends JPanel {
        private int cellSize = 5; // Cell size
        private int numRows = 0;
        private int numCols = 0;

        public void setMazeSize(int numRows, int numCols) {
            this.numRows = numRows;
            this.numCols = numCols;
            if (this.numRows <= 20 && this.numCols <= 20)
                this.cellSize = 10;
            else cellSize=5;
            revalidate();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (maze != null) {
                int panelWidth = getWidth();
                int panelHeight = getHeight();
                int mazeWidth = numCols * cellSize;
                int mazeHeight = numRows * cellSize;

                int offsetX = (panelWidth - mazeWidth) / 2;
                int offsetY = (panelHeight - mazeHeight) / 2;

                for (int row = 0; row < numRows; row++) {
                    for (int col = 0; col < numCols; col++) {
                        switch (maze[row][col]) {
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
}