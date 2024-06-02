import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;


public class MazeSolverUIImpl extends JFrame implements MazeSolverUI, Observer{
    private JButton loadTextButton, loadBinaryButton, findPathButton, selectStartButton, selectEndButton, saveMazeButton, saveBinaryButton;
    private JPanel topPanel, bottomPanel;
    private JScrollPane scrollPane;
    private MazeOperations.MazePanel mazePanel;
    private ErrorPanel errorPanel;
    private MazeOperations mazeArray;
    private volatile boolean running = true; // Czy UI działa

    public MazeSolverUIImpl() {
        setTitle("LabSolver");
        setSize(960, 540);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mazeArray = new MazeOperations();
        mazeArray.addObserver(this); 

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
        add(contentPanel, BorderLayout.CENTER);

        //boczny panel
        errorPanel = new ErrorPanel();
        add(errorPanel, BorderLayout.EAST);


        //wygaszenie guzików
        findPathButton.setEnabled(false);
        selectStartButton.setEnabled(false);
        selectEndButton.setEnabled(false);
        saveMazeButton.setEnabled(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        errorPanel.addErrorMessage(message); // Using custom ErrorPanel's method
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

    private void closeUI() {
        running = false;
        dispose(); // zamykanie
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void update(String input) {
        switch (input) {
            case "loadtext":
                LoadTextListener();
                break;
            case "loadbinary":
                LoadBinaryListener();
                break;
            case "findpath":
                FindPathListener();
                break;
            case "selectstart":
                SelectStartListener();
                break;
            case "selectend":
                SelectEndListener();
                break;
            case "savelabirynth":
                SaveLabirynthListener();
                break;
            case "close":
                closeUI();
                break;
            case "dispSUCC":
            displayErrorMessage("Udało się zapisać w pliku");
                break;
            case "dispF":
            displayErrorMessage("Nie udało się zapisać w pliku");
                break;
            case "dispUn":
            displayErrorMessage("nieznany znak w labiryncie");
                break;
            default:
                displayErrorMessage("Nieznana komenda, użyj: loadtext, loadbinary, findpath, selectstart, selectend, savelabirynth, close");
        }
    }
    
}