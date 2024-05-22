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

    public void writeToBoard(){
        //Index through the y-axis of the tetromino
        for (int indexY = 0; indexY < pattern.length; indexY++) {
            //Index through the x-axis of the tetromino
            for (int indexX = 0; indexX < pattern[0].length; indexX++) {
                if (!pattern[indexX][indexY]) continue; //Skips the cell if it's empty
                GamePanel.currentBoard[x + indexX][y + indexY] = true; //Write the cell to the board
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
        for(int y = 0; y < pattern.length; y++) {
            for(int x = 0; x < pattern[0].length; x++) {
                if(pattern[y][x]) {
                    Rectangle2D rect = new Rectangle2D.Double(
                            (int) (Constants.CELL_BORDER_WIDTH / 2) + ((x + this.x) * Constants.CELL_WIDTH),
                            (int) (Constants.CELL_BORDER_WIDTH / 2) + ((y + this.y) * Constants.CELL_WIDTH),
                            Constants.CELL_WIDTH - Constants.CELL_BORDER_WIDTH,
                            Constants.CELL_WIDTH - Constants.CELL_BORDER_WIDTH);
                    g2.fill(rect);
                }
            }
        }
    }
}
