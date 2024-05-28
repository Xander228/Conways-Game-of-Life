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
                BoardManager.board.setCell(
                        x + indexX - (int)GamePanel.viewPortOffsetX,
                        y + indexY - (int)GamePanel.viewPortOffsetY,
                        true
                        );
            }
        }
        GamePanel.generation = 0;
    }

    public void updateCoords(Point p){
        x = (int)(p.getX() /  GamePanel.cellWidth);
        y = (int)(p.getY() / GamePanel.cellWidth);

    }
    public void drawPattern(Graphics g){
        double cellBoarderWidth = GamePanel.cellWidth * Constants.CELL_BORDER_RATIO;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Constants.LIVE_COLOR);
        for(int y = 0; y < pattern[0].length; y++) {
            for(int x = 0; x < pattern.length; x++) {
                if(pattern[x][y]) {
                    Rectangle2D rect = new Rectangle2D.Double(
                            (cellBoarderWidth / 2) + ((x + this.x + ((GamePanel.viewPortOffsetX + GamePanel.liveViewPortOffsetX) % 1)) * GamePanel.cellWidth),
                            (cellBoarderWidth / 2) + ((y + this.y + ((GamePanel.viewPortOffsetY + GamePanel.liveViewPortOffsetY) % 1)) * GamePanel.cellWidth),
                            GamePanel.cellWidth - cellBoarderWidth,
                            GamePanel.cellWidth - cellBoarderWidth);
                    g2.fill(rect);

                }
            }
        }

        g.setColor(Constants.OUTLINE_COLOR);
        Rectangle2D rect = new Rectangle2D.Double(
                (int) (cellBoarderWidth / 2) + ((this.x + ((GamePanel.viewPortOffsetX + GamePanel.liveViewPortOffsetX) % 1)) * GamePanel.cellWidth),
                (int) (cellBoarderWidth / 2) + ((this.y + ((GamePanel.viewPortOffsetY + GamePanel.liveViewPortOffsetY) % 1)) * GamePanel.cellWidth),
                pattern.length * GamePanel.cellWidth,
                pattern[0].length * GamePanel.cellWidth);
        g2.setStroke(new BasicStroke((float)cellBoarderWidth));
        g2.draw(rect);
    }
}
