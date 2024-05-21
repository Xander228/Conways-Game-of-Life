import java.awt.*;
import java.awt.geom.Rectangle2D;

public class PatternPlacer {
    boolean[][] pattern;
    int x,y;

    PatternPlacer(boolean[][] pattern) {
        this.pattern = pattern;
        x = 0;
        y = 0;
    }

    public void updateCoords(Point p){
        x = (int)(p.getX() / Constants.CELL_WIDTH);
        y = (int)(p.getY() / Constants.CELL_WIDTH);

    }
    public void drawPattern(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for(int y = 0; y < pattern.length; y++) {
            for(int x = 0; x < pattern[0].length; x++) {
                g.setColor(pattern[y][x] ? Constants.LIVE_COLOR : Constants.BACKGROUND_COLOR);
                Rectangle2D rect = new Rectangle2D.Double(
                        (int)(Constants.CELL_BORDER_WIDTH / 2) + ((x + this.x) * Constants.CELL_WIDTH),
                        (int)(Constants.CELL_BORDER_WIDTH / 2) + ((y + this.y) * Constants.CELL_WIDTH),
                        Constants.CELL_WIDTH - Constants.CELL_BORDER_WIDTH,
                        Constants.CELL_WIDTH - Constants.CELL_BORDER_WIDTH);
                g2.fill(rect);
            }
        }
    }
}
