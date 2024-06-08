import java.util.Objects;

public class Location {
    public int x;
    public int y;
    public int hash;

    public Location(int x,int y)
    {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        try {
            Location point = (Location) o;
            return x == point.x && y == point.y;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        if (hash == 0) hash = Objects.hash(x, y);
        return hash;
    }
}