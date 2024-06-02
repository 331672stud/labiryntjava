public class Maze {
    private char[][] MazeArray;
    protected static char Wall='X';
    protected static char Path=' ';
    protected static char Solution='S';
    protected static char Start='P';
    protected static char End='K';
    
    protected void InitMazeArray(int height, int width){
        MazeArray = new char[height][width];
    }

    public boolean IsMazeInit() {
        if (MazeArray!= null) {
            return true;
        }
        return false;
    }
    
    protected void ModifyMazeArray(char Symbol, int CurrentHeight, int CurrentWidth){
        if (isValidPosition(CurrentHeight, CurrentWidth)) {
            MazeArray[CurrentHeight][CurrentWidth] = Symbol;
        } else {
            System.out.println("Error: Out of Bounds");
        }
    }
    
    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < MazeArray.length && col >= 0 && col < MazeArray[0].length;
    }
    
    public int getMazeHeight(){
        return MazeArray.length;
    }

    public int getMazeWidth(){
        return MazeArray[0].length;
    }

    protected char getMazeCell(int height, int width){
        return MazeArray[height][width];
    }
}
