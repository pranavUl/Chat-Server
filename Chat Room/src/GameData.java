public class GameData
{
    private char[][] grid = new char[6][7];

    public char[][] getGrid()
    {
        return grid;
    }

    public GameData() {
        reset();
    }

    public void reset()
    {

        grid = new char[6][7];
        for(int r=0;r<grid.length; r++)
            for(int c=0; c<grid[0].length; c++)
                grid[r][c]=' ';
    }


    public boolean isCat()
    {
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[0].length; j++) {
                if(grid[i][j]==' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isWinner(char letter) {

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j + 3 < grid[0].length; j++) {
                if (grid[i][j] == letter && grid[i][j + 1] == letter &&
                        grid[i][j + 2] == letter && grid[i][j + 3] == letter) {
                    return true;
                }
            }
        }
        for (int i = 0; i + 3 < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == letter && grid[i + 1][j] == letter &&
                        grid[i + 2][j] == letter && grid[i + 3][j] == letter) {
                    return true;
                }
            }
        }
        for (int i = 0; i + 3 < grid.length; i++) {
            for (int j = 0; j + 3 < grid[0].length; j++) {
                if (grid[i][j] == letter && grid[i + 1][j + 1] == letter &&
                        grid[i + 2][j + 2] == letter && grid[i + 3][j + 3] == letter) {
                    return true;
                }
            }
        }
        for (int i = 3; i < grid.length; i++) {
            for (int j = 0; j + 3 < grid[0].length; j++) {
                if (grid[i][j] == letter && grid[i - 1][j + 1] == letter &&
                        grid[i - 2][j + 2] == letter && grid[i - 3][j + 3] == letter) {
                    return true;
                }
            }
        }
        return false;
    }

    public int addToLowest(int r, char letter) {
        int col = r-1;
        for(int i = 0; i < grid.length; i++) {
            if(grid[i][col]==' ') {
                grid[i][col]=letter;
                return i;
            }
        }
        System.out.println("Invalid move - that column is full");
        return -1;
    }
}
