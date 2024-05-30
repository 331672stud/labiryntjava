public class Maze {
    private char[][] MazeArray;
    protected static char Wall='X';
    protected static char Path=' ';
    protected static char Start='P';
    protected static char End='K';
    protected void InitMazeArray(int height, int width){
        MazeArray = new char[height][width];
    }

    public boolean IsMazeInit() {
        // Iterate through the array and check if any element is not the default value ('\u0000')
        for (int i = 0; i < MazeArray.length; i++) {
            for (int j = 0; j < MazeArray[i].length; j++) {
                if (MazeArray[i][j] != '\u0000') {
                    return true; // Array is initialized
                }
            }
        }
        return false; // Array is not initialized
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
