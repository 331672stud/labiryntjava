import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JPanel;
import java.awt.*;
import java.util.Queue;

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
        if (!IsMazeInit()) {
            System.out.println("Maze is not initialized.");
            return;
        }

        // Find start and end positions
        int startX = -1, startY = -1;
        int endX = -1, endY = -1;
        for (int i = 0; i < getMazeHeight(); i++) {
            for (int j = 0; j < getMazeWidth(); j++) {
                if (getMazeCell(i, j) == Start) {
                    startX = i;
                    startY = j;
                } else if (getMazeCell(i, j) == End) {
                    endX = i;
                    endY = j;
                }
            }
        }

        // If start or end positions are not found, return
        if (startX == -1 || startY == -1 || endX == -1 || endY == -1) {
            System.out.println("Start or end position not found.");
            return;
        }

        // Perform BFS to find the shortest path
        boolean[][] visited = new boolean[getMazeHeight()][getMazeWidth()];
        int[][] parentX = new int[getMazeHeight()][getMazeWidth()];
        int[][] parentY = new int[getMazeHeight()][getMazeWidth()];

        Queue<Integer> queue = new LinkedList<>();
        queue.add(startX);
        queue.add(startY);
        visited[startX][startY] = true;

        int[] dx = {-1, 0, 1, 0}; // Possible movements: Up, Right, Down, Left
        int[] dy = {0, 1, 0, -1};

        while (!queue.isEmpty()) {
            int currentX = queue.poll();
            int currentY = queue.poll();

            if (currentX == endX && currentY == endY) {
                // Shortest path found, backtrack to mark the path
                while (currentX != startX || currentY != startY) {
                    int nextX = parentX[currentX][currentY];
                    int nextY = parentY[currentX][currentY];
                    ModifyMazeArray(Solution, currentX, currentY);
                    currentX = nextX;
                    currentY = nextY;
                }
                ModifyMazeArray(Solution, startX, startY); // Mark start as part of the solution
                break;
            }

            for (int i = 0; i < 4; i++) {
                int newX = currentX + dx[i];
                int newY = currentY + dy[i];

                // Check if the new position is valid and not visited yet
                if (newX >= 0 && newX < getMazeHeight() && newY >= 0 && newY < getMazeWidth() &&
                        !visited[newX][newY] && getMazeCell(newX, newY) != Wall) {
                    visited[newX][newY] = true;
                    parentX[newX][newY] = currentX;
                    parentY[newX][newY] = currentY;
                    queue.add(newX);
                    queue.add(newY);
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