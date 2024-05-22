import java.awt.*;
import java.awt.geom.Rectangle2D;

public class PatternPlacer {
    private static boolean[][] pattern;
    int x,y;

    PatternPlacer(boolean[][] pattern) {
        this.pattern = pattern;
        x = 0;
        y = 0;
    }

    public static void rotatePattern(){
        boolean[][] newPattern = new boolean[pattern[0].length][pattern.length];
        for (int indexY = 0; indexY < pattern[0].length; indexY++) {
            //Index through the x-axis of the tetromino
            for (int indexX = 0; indexX < pattern.length; indexX++) {
                newPattern[pattern[0].length - indexY - 1][indexX] = pattern[indexX][indexY];
            }
        }
        pattern = newPattern;
    }

    public void writeToBoard(){
        for (int indexY = 0; indexY < pattern[0].length; indexY++) {
            for (int indexX = 0; indexX < pattern.length; indexX++) {
                if (!pattern[indexX][indexY]) continue;
                GamePanel.currentBoard[y + indexY][x + indexX] = true;
            }
        }
    }

    public void updateCoords(Point p){
        x = (int)(p.getX() / Constants.CELL_WIDTH);
        y = (int)(p.getY() / Constants.CELL_WIDTH);

    }
    public void drawPattern(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Constants.LIVE_COLOR);
        for(int y = 0; y < pattern[0].length; y++) {
            for(int x = 0; x < pattern.length; x++) {
                if(pattern[x][y]) {
                    Rectangle2D rect = new Rectangle2D.Double(
                            (int) (Constants.CELL_BORDER_WIDTH / 2) + ((x + this.x) * Constants.CELL_WIDTH),
                            (int) (Constants.CELL_BORDER_WIDTH / 2) + ((y + this.y) * Constants.CELL_WIDTH),
                            Constants.CELL_WIDTH - Constants.CELL_BORDER_WIDTH,
                            Constants.CELL_WIDTH - Constants.CELL_BORDER_WIDTH);
                    g2.fill(rect);

                }
            }
        }

        g.setColor(Constants.HIGHLIGHT_COLOR);
        Rectangle2D rect = new Rectangle2D.Double(
                (int) (Constants.CELL_BORDER_WIDTH / 2) + (this.x * Constants.CELL_WIDTH),
                (int) (Constants.CELL_BORDER_WIDTH / 2) + (this.y * Constants.CELL_WIDTH),
                pattern.length * Constants.CELL_WIDTH,
                pattern[0].length * Constants.CELL_WIDTH);
        g2.setStroke(new BasicStroke((float)Constants.CELL_BORDER_WIDTH));
        g2.draw(rect);
    }
}
