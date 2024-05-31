import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JPanel;
import java.awt.*;

public class MazeOperations extends Maze {

    protected void LoadTextMaze (File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        int numRows = 0;
        int numCols = 0;
        while ((line = reader.readLine()) != null) {
            numCols = Math.max(numCols, line.length());
            numRows++;
        }
        reader.close();

        InitMazeArray(numRows, numCols);
        reader = new BufferedReader(new FileReader(file));
        int row = 0;
        while ((line = reader.readLine()) != null) {
            for (int col = 0; col < line.length(); col++) {
                ModifyMazeArray(line.charAt(col), row, col);
            }
            row++;
        }
        reader.close();
    }

    //Biedabinary wczytywanie
    protected void loadBinaryMaze(File file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            //Nagłówek
            int fileId = readBytesAsInt(fileInputStream, 32/8);
            int escape = readBytesAsInt(fileInputStream, 8/8);
            int columns = readBytesAsInt(fileInputStream, 16/8);
            int lines = readBytesAsInt(fileInputStream, 16/8);
            int entryX = readBytesAsInt(fileInputStream, 16/8);
            int entryY = readBytesAsInt(fileInputStream, 16/8);
            int exitX = readBytesAsInt(fileInputStream, 16/8);
            int exitY = readBytesAsInt(fileInputStream, 16/8);
            // Reserved
            readBytesAsInt(fileInputStream, 32/8);
            readBytesAsInt(fileInputStream, 32/8);
            readBytesAsInt(fileInputStream, 32/8);
            int counter = readBytesAsInt(fileInputStream, 32/8);
            int solutionOffset = readBytesAsInt(fileInputStream, 32/8);
            int separator = readBytesAsInt(fileInputStream, 8/8);
            int wall = readBytesAsInt(fileInputStream, 8/8);
            int path = readBytesAsInt(fileInputStream, 8/8);
            int numRows = 0;
            int numCols = 0;
            InitMazeArray(numRows, numCols);
            // czytanko w pętelce
            while (fileInputStream.available() > 0) {
                int separatorValue = readBytesAsInt(fileInputStream, 8/8);
                int value = readBytesAsInt(fileInputStream, 8/8);
                int count = readBytesAsInt(fileInputStream, 8/8);
                for(int i=0;i<=count;i++){
                    if(value==wall){
                        ModifyMazeArray(Maze.Wall, numRows, numCols);
                    } else {
                        ModifyMazeArray(Maze.Path, numRows, numCols);
                    }
                    numCols++;
                    if(numCols==columns){
                        numRows++;
                        numCols=0;
                    }
                }
            }
            ModifyMazeArray(Maze.Start, entryY-1, entryX-1);    
            ModifyMazeArray(Maze.End, exitY-1, exitX-1);
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

    public void FindPathInMazeArray(){
        // Check if the maze is initialized
        if (!IsMazeInit()) {
            System.out.println("Maze is not initialized.");
            return;
        }

        // Initialize a visited array to keep track of visited cells
        boolean[][] visited = new boolean[getMazeHeight()][getMazeWidth()];

        // Find the starting position
        int startRow = -1, startCol = -1;
        for (int i = 0; i < getMazeHeight(); i++) {
            for (int j = 0; j < getMazeWidth(); j++) {
                if (getMazeCell(i, j) == Start) {
                    startRow = i;
                    startCol = j;
                    break;
                }
            }
            if (startRow != -1) break;
        }

        // Perform DFS
        dfs(startRow, startCol, visited);

        // Mark the shortest path
        markShortestPath(visited);
    }
    
    // Depth-First Search (DFS) method
    private void dfs(int row, int col, boolean[][] visited) {
        // Mark the current cell as visited
        visited[row][col] = true;

        // Check neighbors
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            // Check if the neighbor is within bounds and not visited
            if (newRow >= 0 && newRow < getMazeHeight() && newCol >= 0 && newCol < getMazeWidth()
                    && !visited[newRow][newCol]) {
                char cell = getMazeCell(newRow, newCol);

                // If the neighbor is a path, continue DFS
                if (cell == Path || cell == End) {
                    dfs(newRow, newCol, visited);
                }
            }
        }
    }

    // Helper method to mark the shortest path
    private void markShortestPath(boolean[][] visited) {
        for (int i = 0; i < getMazeHeight(); i++) {
            for (int j = 0; j < getMazeWidth(); j++) {
                if (visited[i][j]) {
                    if (getMazeCell(i, j) != Start && getMazeCell(i, j) != End) {
                        ModifyMazeArray(Solution, i, j);
                    }
                } else {
                    if (getMazeCell(i, j) == Solution) {
                        ModifyMazeArray(Path, i, j);
                    }
                }
            }
        }
    }

    public void SaveMazeArrayToFile(File selectedFile){ 
        //to jest do tekstowych, możemy dodać switch na binarne
        try {
            FileWriter fileWriter = new FileWriter(selectedFile);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            for (int i = 0; i < getMazeHeight(); i++) {
                for (int j = 0; j < getMazeWidth(); j++) {
                    writer.write(getMazeCell(i, j));
                }
                writer.write(System.lineSeparator()); 
            }

            writer.close();
            
           //notify tu dac displayErrorMessage("Zapisano");
        } catch (IOException e) {
          //dacme notify  displayErrorMessage("Zapis się nie udał: " + e.getMessage());
        }
        
    }
    public class MazePanel extends JPanel {
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
        if (IsMazeInit()) {
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int mazeWidth = numCols * cellSize;
            int mazeHeight = numRows * cellSize;

            int offsetX = (panelWidth - mazeWidth) / 2;
            int offsetY = (panelHeight - mazeHeight) / 2;

            for (int row = 0; row < numRows; row++) {
                for (int col = 0; col < numCols; col++) {
                    switch (getMazeCell(row, col)) {
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
                            // notify displayErrorMessage("Nieznany znak w labiryncie");
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