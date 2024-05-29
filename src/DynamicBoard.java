import java.util.*;
public class DynamicBoard {


    private HashSet<Location> board;
    private int xMin, xMax;
    private int yMin, yMax;
    private boolean changed;

    DynamicBoard(){
        this.board = new HashSet<Location>();
        changed = false;
        calculateBounds();
    }

    DynamicBoard(HashSet<Location> board){
        this.board = new HashSet<Location>(board);
        changed = false;
        calculateBounds();
    }

    public void setCell(int x, int y, boolean live){
        if(live) board.add(new Location(x,y));
        else board.remove(new Location(x,y));
        changed = true;
    }

    public boolean getCell(int x, int y){
        return board.contains(new Location(x,y));
    }

    private void calculateBounds(){
        xMin = Integer.MAX_VALUE;
        xMax = Integer.MIN_VALUE;
        yMin = Integer.MAX_VALUE;
        yMax = Integer.MIN_VALUE;
        if(board.isEmpty()) {
            xMin = 0;
            xMax = -1;
            yMin = 0;
            yMax = -1;
            return;
        }
        for(Location point : board){
            xMin = Math.min(xMin, point.getX());
            xMax = Math.max(xMax, point.getX());
            yMin = Math.min(yMin, point.getY());
            yMax = Math.max(yMax, point.getY());
        }
        changed = false;
    }
    public int getXMin(){
        if (!changed) return xMin;
        calculateBounds();
        return xMin;
    }

    public int getXMax(){
        if (!changed) return xMax;
        calculateBounds();
        return xMax;
    }

    public int getYMin(){
        if (!changed) return yMin;
        calculateBounds();
        return yMin;
    }

    public int getYMax(){
        if (!changed) return yMax;
        calculateBounds();
        return yMax;
    }

    public int getSize(){
        return board.size();
    }

    public HashSet<Location> createCheckList(){
        HashSet<Location> checkList = new HashSet<Location>();
        for(Location point : board) {
            for (int i = -1; i < 2; i++)  neighborSum++;
            for (int i = -1; i < 2; i++) neighborSum++;
            if (board.getCell(x + 1, y)) neighborSum++;
            if (board.getCell(x - 1, y)) neighborSum++;
        }
    }

    public DynamicBoard copy(){
        return new DynamicBoard(this.board);
    }
}
