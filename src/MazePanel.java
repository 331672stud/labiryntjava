import javax.swing.*;
import java.awt.*;
public class MazePanel extends JPanel {
    private int cellSize = 5;
    private int numRows = 0;
    private int numCols = 0;
    private MazeOperations Maze;

    public MazePanel(MazeOperations MazeOperations){
        this.Maze=MazeOperations;
    }


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
        if (Maze.IsMazeInit()) {
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int mazeWidth = numCols * cellSize;
            int mazeHeight = numRows * cellSize;

            int offsetX = (panelWidth - mazeWidth) / 2;
            int offsetY = (panelHeight - mazeHeight) / 2;

            for (int row = 0; row < numRows; row++) {
                for (int col = 0; col < numCols; col++) {
                    switch (Maze.getMazeCell(row, col)) {
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