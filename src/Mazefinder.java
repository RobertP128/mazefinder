import java.util.ArrayList;
import java.util.List;

public class Mazefinder {

    private MazeItem[][] maze;
    private MazeItem[][] mazeStart;
    private List<Pos> backtrack;

    private void initMaze(MazeItem[][] maze) {

        // Loop through all slots
        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[y].length; x++) {
                maze[y][x] = MazeItem.EMPTY;
            }
        }

        maze[0][2]=MazeItem.OBSTICLE;
        maze[0][maze[0].length-1]=MazeItem.END;
    }

    private MazeItem[][] cloneMaze(MazeItem[][] maze) {
        MazeItem[][] m2=new MazeItem[maze.length][maze[0].length];
        // Loop through all slots
        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[y].length; x++) {
                m2[y][x]=maze[y][x];
            }
        }
        return m2;
    }

    public void run() {
        backtrack=new ArrayList<>();
        maze=new MazeItem[3][6];

        initMaze(maze);


        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[y].length; x++) {
                if (maze[y][x]==MazeItem.EMPTY) {
                    maze[y][x] = MazeItem.START;
                    backtrack=new ArrayList<>();
                    System.out.println("Start...");
                    mazeStart=cloneMaze(maze);
                    printMaze(maze);
                    findTheEnd(x, y,true);
                    maze[y][x] = MazeItem.EMPTY;
                }
            }
        }





    }

    // return false if we hit an obsticle
    private void findTheEnd(int x,int y,boolean ignoreStart){

        //System.out.println(x+":"+y);
        if (maze[y][x]==MazeItem.END && !ignoreStart) {
            //System.out.println("found End");
            //printMaze();
            if (allOccupied()){
                System.out.println("Found Solution");
                printMaze(maze);
                printBacktraceMaze();
            }
            return;
        }

        if (maze[y][x]!=MazeItem.EMPTY && !ignoreStart ) {
            //System.out.println("dead end detour");
            //printMaze();
            return;
        }
        // if Empty the go ahead searching
        // Mark as PATH
        if (maze[y][x]!=MazeItem.START) {
            backtrack.add(new Pos(x,y));
            maze[y][x] = MazeItem.PATH;
        }

        // find top
        if (y>0){
            findTheEnd(x,y-1,false);
        }
        // find right
        if (x<maze[y].length-1){
            findTheEnd(x+1,y,false);
        }
        // find bottom
        if (y<maze.length-1){
            findTheEnd(x,y+1,false);
        }
        // find left
        if (x>0){
            findTheEnd(x-1,y,false);
        }

        if (maze[y][x]!=MazeItem.START) {
            backtrack.remove(backtrack.size()-1);
            maze[y][x] = MazeItem.EMPTY;
        }

    }

    private boolean allOccupied(){
        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[y].length; x++) {
                if (maze[y][x] == MazeItem.EMPTY || maze[y][x] == MazeItem.VISITED) return false;
            }
        }
        return true;
    }



    private void printMaze(MazeItem[][] maze) {

        for (int y = 0; y < maze.length; y++) {
            if (y == 0) {
                System.out.print("-");
                for (int x = 0; x < maze[y].length; x++) {
                    System.out.print("--");
                }
                System.out.println();
            }
            for (int x = 0; x < maze[y].length; x++) {
                if (x == 0) {
                    System.out.print("|");
                }
                System.out.print(MazeItemToString(maze[y][x]) + "|");
            }
            System.out.println();
            System.out.print(" ");
            for (int x = 0; x < maze[y].length; x++) {
                System.out.print("--");
            }
            System.out.println();
        }
    }

    private void printBacktraceMaze(){
        MazeItem[][] maze=cloneMaze(this.mazeStart);

        for (var pos: backtrack) {
            maze[pos.y][pos.x]=MazeItem.PATH;
            printMaze(maze);
            try {
                Thread.sleep(400);
            }
            catch (InterruptedException e){

            }

        }
    }

    private String MazeItemToString(MazeItem item) {
        return switch (item) {
            case EMPTY -> " ";
            case START -> "S";
            case END -> "E";
            case OBSTICLE -> "O";
            case PATH -> "*";
            case VISITED -> ".";
        };

    }


}
