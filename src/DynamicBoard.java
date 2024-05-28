import java.util.*;
public class DynamicBoard {
    private class Location {
        public int x;
        public int y;

        public Location(int x,int y)
        {
            this.x=x;
            this.y=y;
        }

        public int getX() {
            return x;
        }
        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            Location point = (Location) o;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

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

    public DynamicBoard copy(){
        return new DynamicBoard(this.board);
    }
}
