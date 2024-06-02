import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MazeSolverUIImpl extends JFrame implements MazeSolverUI{
    private JButton loadTextButton, loadBinaryButton, findPathButton, selectStartButton, selectEndButton, saveMazeButton, saveBinaryButton;
    private JPanel topPanel, bottomPanel, sidePanel, errorPanel;
    private JScrollPane scrollPane;
    private MazeOperations.MazePanel mazePanel;
    private List<JLabel> errorMessages;
    private MazeOperations mazeArray;

    public MazeSolverUIImpl() {
        setTitle("LabSolver2");
        setSize(960, 540);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mazeArray = new MazeOperations(); 

        // Inicjalizacja guzikow
        loadTextButton = new JButton("Wczytaj labirynt z pliku tekstowego");
        loadBinaryButton = new JButton("Wczytaj labirynt z pliku binarnego");
        findPathButton = new JButton("Znajdz najkrotsza sciezke");
        selectStartButton = new JButton("Wybierz punkt początkowy");
        selectEndButton = new JButton("Wybierz punkt końcowy");
        saveMazeButton = new JButton("Zapisz labirynt do pliku");
        saveBinaryButton = new JButton("Zapisz w formacie binarnym");

        loadTextButton.setFocusable(false);
        loadBinaryButton.setFocusable(false);
        findPathButton.setFocusable(false);
        selectStartButton.setFocusable(false);
        selectEndButton.setFocusable(false);
        saveMazeButton.setFocusable(false);
        saveBinaryButton.setFocusable(false);

        loadTextButton.addActionListener(e -> LoadTextListener());
        loadBinaryButton.addActionListener(e -> LoadBinaryListener());
        findPathButton.addActionListener(e -> FindPathListener());
        selectStartButton.addActionListener(e -> SelectStartListener());
        selectEndButton.addActionListener(e -> SelectEndListener());
        saveMazeButton.addActionListener(e -> SaveLabirynthListener());
        saveBinaryButton.addActionListener(e -> saveBinaryListener());

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
        bottomPanel.add(selectEndButton);
        bottomPanel.add(saveMazeButton);

        // poczatkowy panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        // panel labiryntu
        mazePanel = mazeArray.new MazePanel();
        mazePanel.addMouseListener(mazePanel);
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
        selectEndButton.setEnabled(false);
        saveMazeButton.setEnabled(false);

        errorMessages = new ArrayList<>();

        setVisible(true);
        displayErrorMessage("Wczytano GUI");

    }

    @Override
    public void loadTextMaze(File file) {
        try {
            mazeArray.LoadTextMaze(file);
            mazePanel.setMazeSize(mazeArray.getMazeHeight(), mazeArray.getMazeWidth());
            mazePanel.repaint();
            adjustScrollPane();
            findPathButton.setEnabled(true);
            selectStartButton.setEnabled(true);
            selectEndButton.setEnabled(true);
            saveMazeButton.setEnabled(true);
            displayErrorMessage("Wczytano labirynt");
        } catch (IOException ex) {
            displayErrorMessage("Nie udało się wczytać labiryntu z pliku: " + ex.getMessage());
        }
    }

    @Override
    public void loadBinaryMaze(File file) {
        try {
            loadMazeFromBinaryFile(file);
            mazePanel.setMazeSize(mazeArray.getMazeHeight(), mazeArray.getMazeWidth());
            mazePanel.repaint();
            adjustScrollPane();
            findPathButton.setEnabled(true);
            selectStartButton.setEnabled(true);
            selectEndButton.setEnabled(true);
            saveMazeButton.setEnabled(true);
        } catch (IOException ex) {
            displayErrorMessage("Nie udało się wczytać labiryntu z pliku binarnego: " + ex.getMessage());
        }
    }

    @Override
    public void findPath() {
        mazeArray.FindPathInMazeArray();
        mazePanel.repaint();
        displayErrorMessage("Znaleziono ścieżkę");
    }

    @Override
    public void selectStart() {
        mazeArray.choosingStart = true;
        mazeArray.choosingEnd = false;
        displayErrorMessage("Wybieranie punktu początkowego");
    }

    @Override
    public void selectEnd() {
        mazeArray.choosingStart = false;
        mazeArray.choosingEnd = true;
        displayErrorMessage("Wybieranie punktu końcowego");
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
            mazeArray.SaveMazeArrayToFile(selectedFile);
        }
    }

    private void loadMazeFromBinaryFile(File file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            mazeArray.loadBinaryMaze(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void LoadTextListener (){
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("pliki tekstowe .txt", "txt");
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            loadTextMaze(selectedFile);
        }
    }

    private void LoadBinaryListener(){
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("pliki binarne .bin", "bin");
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            loadBinaryMaze(selectedFile);
        }
    }
    

    private void FindPathListener(){
        findPath();
    }
    

    private void SelectStartListener(){
        selectStart();
    }
    

    private void SelectEndListener(){
        selectEnd();
    }
    

    private void SaveLabirynthListener(){
        saveMaze();
    }

    private void saveBinaryListener(){

    }
    
}