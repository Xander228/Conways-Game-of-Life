import java.awt.*;
import java.awt.geom.Rectangle2D;

public class PatternPlacer {
    private boolean[][] pattern;
    int x,y;

    PatternPlacer(boolean[][] pattern) {
        this.pattern = pattern;
        x = 0;
        y = 0;
    }

    public void rotatePattern(){
        if(pattern == null) return;
        boolean[][] newPattern = new boolean[pattern[0].length][pattern.length];
        for (int indexY = 0; indexY < pattern[0].length; indexY++) {
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
                GamePanel.boardManager.setCell(
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
    public void draw(Graphics2D g2){
        double cellBoarderWidth = GamePanel.cellWidth * Constants.CELL_BORDER_RATIO;

        g2.setColor(Constants.LIVE_COLOR);
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

        g2.setColor(Constants.PASTE_OUTLINE_COLOR);
        g2.setStroke(new BasicStroke((float)cellBoarderWidth));

        String str = "w: " + pattern.length + " h: " + pattern[0].length;
        g2.setFont(new Font("Arial", Font.PLAIN,12));
        double textWidth = g2.getFontMetrics().stringWidth(str) / 12.4 + .4;

        double tagScaleValue = Math.max(3 * GamePanel.cellWidth, 15);
        tagScaleValue = Math.min(tagScaleValue, GamePanel.cellWidth * pattern.length / textWidth);

        Rectangle2D rect = new Rectangle2D.Double(
                (int) (cellBoarderWidth / 2) + ((this.x + ((GamePanel.viewPortOffsetX + GamePanel.liveViewPortOffsetX) % 1)) * GamePanel.cellWidth),
                (int) (cellBoarderWidth / 2) + ((this.y + ((GamePanel.viewPortOffsetY + GamePanel.liveViewPortOffsetY) % 1)) * GamePanel.cellWidth) - 1 * tagScaleValue,
                textWidth * tagScaleValue,
                1 * tagScaleValue);
        g2.fill(rect);
        g2.draw(rect);

        rect = new Rectangle2D.Double(
                (int) (cellBoarderWidth / 2) + ((this.x + ((GamePanel.viewPortOffsetX + GamePanel.liveViewPortOffsetX) % 1)) * GamePanel.cellWidth),
                (int) (cellBoarderWidth / 2) + ((this.y + ((GamePanel.viewPortOffsetY + GamePanel.liveViewPortOffsetY) % 1)) * GamePanel.cellWidth),
                pattern.length * GamePanel.cellWidth,
                pattern[0].length * GamePanel.cellWidth);
        g2.draw(rect);

        g2.setColor(Constants.BACKGROUND_COLOR);
        g2.setFont(new Font("Arial", Font.PLAIN,12). deriveFont((float)tagScaleValue));
        g2.drawString(
                str,
                (float)(((this.x + ((GamePanel.viewPortOffsetX + GamePanel.liveViewPortOffsetX) % 1)) * GamePanel.cellWidth) + 0.1 * tagScaleValue),
                (float)(((this.y + ((GamePanel.viewPortOffsetY + GamePanel.liveViewPortOffsetY) % 1)) * GamePanel.cellWidth) - 0.1 * tagScaleValue));

    }
}
