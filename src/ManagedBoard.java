import java.util.*;
public class ManagedBoard {
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

        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            Location point = (Location) o;
            return x == point.x && y == point.y;
        }

        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    HashSet<Location> board;
    int xMin, xMax;
    int yMin, yMax;
    boolean changed;

    ManagedBoard(){
        changed = false;
        xMin = 0;
        xMax = 0;
        yMin = 0;
        yMax = 0;
    }

    ManagedBoard(HashSet<Location> board){
        this.board = new HashSet<Location>(board);
        calculateBounds();
        xMin = 0;
        xMax = 0;
        yMin = 0;
        yMax = 0;
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
        xMin = 0;
        xMax = 0;
        yMin = 0;
        yMax = 0;
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

    public ManagedBoard copy(){
        return new ManagedBoard(this.board);
    }
}
