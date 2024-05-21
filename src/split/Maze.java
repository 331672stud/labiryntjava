public class Maze {
    private char[][] MazeArray;
    protected static char Wall='X';
    protected static char Path=' ';
    protected static char Start='P';
    protected static char End='K';
    protected void InitMazeArray(int height, int width){
        MazeArray = new char[height][width];
    }
    
    protected void ModifyMazeArray(char Symbol, int CurrentHeight, int CurrentWidth){
        MazeArray[CurrentHeight][CurrentWidth]=Symbol;
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
