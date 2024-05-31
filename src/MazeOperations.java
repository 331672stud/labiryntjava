import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
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
        // Check if the maze is initialized
        if (!IsMazeInit()) {
            System.out.println("Maze is not initialized.");
            return;
        }

        // Initialize a visited array to keep track of visited cells
        boolean[][] visited = new boolean[getMazeHeight()][getMazeWidth()];

        // Initialize a queue for BFS
        Queue<int[]> queue = new LinkedList<>();

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

        // Add the starting position to the queue
        queue.offer(new int[]{startRow, startCol});

        // Perform BFS
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];

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

                    // If the neighbor is a path, mark it as part of the solution
                    if (cell == Path) {
                        ModifyMazeArray(Solution, newRow, newCol);
                        queue.offer(new int[]{newRow, newCol});
                    }

                    // If the neighbor is the end point, stop BFS
                    if (cell == End) {
                        markShortestPath(visited);
                        return;
                    }
                }
            }
        }

        // If the end point is not reached, there is no solution
        System.out.println("No solution found.");
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
}