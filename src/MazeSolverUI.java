import java.io.File;

public interface MazeSolverUI {
    void loadTextMaze(File file);
    void loadBinaryMaze(File file);
    void findPath();
    void selectStart();
    void selectEnd();
    void saveMaze();
}