import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.stream.Stream;

public class PatternPicker {
    boolean cut;
    int x1,y1,x2,y2;
    int originX,originY,width,height;
    boolean mousePressed;
    boolean mouseReleased;



    PatternPicker(boolean cut) {
        this.cut = cut;
        x1 = 0;
        y1 = 0;
        x2 = 0;
        y2 = 0;
        originX = 0;
        originY = 0;
        width = 0;
        height = 0;
        mousePressed = false;
        mouseReleased = false;
    }

    private String readBoard(){
        StringBuilder pattern = new StringBuilder();
        for (int indexY = 0; indexY < height; indexY++) {
            for (int indexX = 0; indexX < width; indexX++) {
                pattern.append(GamePanel.boardManager.getCell(
                        (originX + indexX),
                        (originY + indexY)) ?
                        "o" : "b");

                if(cut) GamePanel.boardManager.setCell(
                        (originX + indexX),
                        (originY + indexY),
                        false
                );
            }
            pattern.append((indexY != height - 1) ? "$" : "!");
        }

        int lastIdenticalIndex = 0;
        StringBuilder optimizedPattern = new StringBuilder();
        for (int i = 0; i < pattern.length(); i++) {
            if (i < pattern.length() - 1){
               if(pattern.charAt(lastIdenticalIndex) == pattern.charAt(i + 1)) continue;
            }
            int symbolRepeats = i - lastIdenticalIndex + 1;
            lastIdenticalIndex = i + 1;
            if (symbolRepeats == 1) optimizedPattern.append(String.valueOf(pattern.charAt(i)));
            else optimizedPattern.append(symbolRepeats).append(String.valueOf(pattern.charAt(i)));
        }


        return optimizedPattern.toString();
    }

    public void updateCoords(Point p){
        if (mouseReleased) return;
        if (!mousePressed) updateStartCoords(p);
        else updateEndCoords(p);
    }

    public void mousePressed() {
        mousePressed = true;
    }

    public String mouseReleased() {
        if(!mousePressed) return "";
        if(width <= 0 || height <= 0) return "";
        mouseReleased = true;
        return readBoard();
    }

    public void updateStartCoords(Point p){
        double totalViewPortOffsetY = GamePanel.viewPortOffsetY + GamePanel.liveViewPortOffsetY;
        double totalViewPortOffsetX = GamePanel.viewPortOffsetX + GamePanel.liveViewPortOffsetX;

        x1 = (int)Math.floor((p.getX() / GamePanel.cellWidth - totalViewPortOffsetX));
        y1 = (int)Math.floor((p.getY() / GamePanel.cellWidth - totalViewPortOffsetY));
    }

    public void updateEndCoords(Point p){
        double totalViewPortOffsetY = GamePanel.viewPortOffsetY + GamePanel.liveViewPortOffsetY;
        double totalViewPortOffsetX = GamePanel.viewPortOffsetX + GamePanel.liveViewPortOffsetX;

        x2 = (int)Math.floor((p.getX() / GamePanel.cellWidth - totalViewPortOffsetX));
        y2 = (int)Math.floor((p.getY() / GamePanel.cellWidth - totalViewPortOffsetY));
        originX = Math.min(x1, x2);
        originY = Math.min(y1, y2);
        width = Math.abs(x2 - x1);
        height = Math.abs(y2 - y1);

    }

    public void draw(Graphics2D g2) {
        if (!mousePressed) drawStartingCell(g2);
        else drawCopyArea(g2);
    }

    private void drawStartingCell(Graphics2D g2) {
        double totalViewPortOffsetY = GamePanel.viewPortOffsetY + GamePanel.liveViewPortOffsetY;
        double totalViewPortOffsetX = GamePanel.viewPortOffsetX + GamePanel.liveViewPortOffsetX;
        double cellBoarderWidth = GamePanel.cellWidth * Constants.CELL_BORDER_RATIO;

        g2.setColor(this.cut ? Constants.CUT_OUTLINE_COLOR : Constants.COPY_OUTLINE_COLOR);
        Rectangle2D rect = new Rectangle2D.Double(
                (int) (cellBoarderWidth / 2) + ((this.x1 + totalViewPortOffsetX) * GamePanel.cellWidth),
                (int) (cellBoarderWidth / 2) + ((this.y1 + totalViewPortOffsetY) * GamePanel.cellWidth),
                GamePanel.cellWidth,
                GamePanel.cellWidth);
        g2.draw(rect);

    }


    private void drawCopyArea(Graphics2D g2) {
        double totalViewPortOffsetY = GamePanel.viewPortOffsetY + GamePanel.liveViewPortOffsetY;
        double totalViewPortOffsetX = GamePanel.viewPortOffsetX + GamePanel.liveViewPortOffsetX;
        double cellBoarderWidth = GamePanel.cellWidth * Constants.CELL_BORDER_RATIO;

        g2.setColor(this.cut ? Constants.CUT_OUTLINE_COLOR : Constants.COPY_OUTLINE_COLOR);
        g2.setStroke(new BasicStroke((float)cellBoarderWidth));

        String str = "w: " + width + " h: " + height;
        g2.setFont(new Font("Arial", Font.PLAIN,12));
        double textWidth = g2.getFontMetrics().stringWidth(str) / 12.4 + .4;

        double tagScaleValue = Math.max(3 * GamePanel.cellWidth, 15);
        tagScaleValue = Math.min(tagScaleValue, GamePanel.cellWidth * width / textWidth);

        Rectangle2D rect = new Rectangle2D.Double(
                (int) (cellBoarderWidth / 2) + (this.originX + totalViewPortOffsetX) * GamePanel.cellWidth,
                (int) (cellBoarderWidth / 2) + (this.originY + totalViewPortOffsetY) * GamePanel.cellWidth - 1 * tagScaleValue,
                textWidth * tagScaleValue,
                1 * tagScaleValue);
        g2.fill(rect);
        g2.draw(rect);

        rect = new Rectangle2D.Double(
                (int) (cellBoarderWidth / 2) + (this.originX + totalViewPortOffsetX) * GamePanel.cellWidth,
                (int) (cellBoarderWidth / 2) + (this.originY + totalViewPortOffsetY) * GamePanel.cellWidth,
                width * GamePanel.cellWidth,
                height * GamePanel.cellWidth);
        g2.draw(rect);

        g2.setColor(Constants.BACKGROUND_COLOR);
        g2.setFont(new Font("Arial", Font.PLAIN,12). deriveFont((float)tagScaleValue));
        g2.drawString(
                str,
                (float)(((this.originX + totalViewPortOffsetX) * GamePanel.cellWidth) + 0.1 * tagScaleValue),
                (float)(((this.originY + totalViewPortOffsetY) * GamePanel.cellWidth) - 0.1 * tagScaleValue));

    }
}
