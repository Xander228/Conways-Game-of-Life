import java.awt.*;
import java.awt.geom.Rectangle2D;

public class DisplayController {


    public static void drawBoard(Graphics g){
        if(GamePanel.cellWidth >= 1) renderFullRes(g);
        else renderOptimizedRes(g, 1 / GamePanel.cellWidth);
    }

    public static void renderFullRes(Graphics g){
        double cellBoarderWidth = GamePanel.cellWidth * Constants.CELL_BORDER_RATIO;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

        double totalViewPortOffsetY = GamePanel.viewPortOffsetY + GamePanel.liveViewPortOffsetY;
        double totalViewPortOffsetX = GamePanel.viewPortOffsetX + GamePanel.liveViewPortOffsetX;

        int yMin = (int)Math.floor(-totalViewPortOffsetY);
        int yMax = (int)Math.ceil((GamePanel.gamePanel.getHeight() / GamePanel.cellWidth) - totalViewPortOffsetY);
        int xMin = (int)Math.floor(-totalViewPortOffsetX);
        int xMax = (int)Math.ceil((GamePanel.gamePanel.getWidth() / GamePanel.cellWidth) - totalViewPortOffsetX);

        for(int y = yMin; y < yMax; y++) {
            for(int x = xMin; x < xMax; x++) {
                boolean cell = GamePanel.boardManager.getCell(x, y);
                if(x == 0 && y == 0) g.setColor(cell ? Constants.HOME_LIVE_COLOR : Constants.HOME_COLOR);
                else if(y == 0) g.setColor(cell ? Constants.X_LIVE_COLOR : Constants.X_COLOR);
                else if(x == 0) g.setColor(cell ? Constants.Y_LIVE_COLOR : Constants.Y_COLOR);
                else if(cell) g.setColor(Constants.LIVE_COLOR);
                else continue;

                ///*
                Rectangle2D rect = new Rectangle2D.Double(
                        (cellBoarderWidth / 2) + (x + totalViewPortOffsetX) * GamePanel.cellWidth,
                        (cellBoarderWidth / 2) + (y + totalViewPortOffsetY) * GamePanel.cellWidth,
                        GamePanel.cellWidth - cellBoarderWidth,
                        GamePanel.cellWidth - cellBoarderWidth);
                g2.fill(rect);
            }
        }
        if(cellBoarderWidth <= .2) return;
        g.setColor(Constants.ACCENT_COLOR);
        for(int y = yMin; y < yMax; y++) {
            Rectangle2D rect = new Rectangle2D.Double(
                    0,
                    (-cellBoarderWidth / 2) + (y + totalViewPortOffsetY) * GamePanel.cellWidth,
                    GamePanel.gamePanel.getWidth(),
                    cellBoarderWidth);
            g2.fill(rect);
        }
        for(int x = xMin; x < xMax; x++) {
            Rectangle2D rect = new Rectangle2D.Double(
                    (-cellBoarderWidth / 2) + (x + totalViewPortOffsetX) * GamePanel.cellWidth,
                    0,
                    cellBoarderWidth,
                    GamePanel.gamePanel.getHeight());
            g2.fill(rect);
        }
    }

    public static void renderOptimizedRes(Graphics g, double scaleDownFactor){

        double totalViewPortOffsetY = GamePanel.viewPortOffsetY + GamePanel.liveViewPortOffsetY;
        double totalViewPortOffsetX = GamePanel.viewPortOffsetX + GamePanel.liveViewPortOffsetX;

        int yMin = (int)Math.floor(-totalViewPortOffsetY / scaleDownFactor);
        int yMax = (int)Math.ceil(((GamePanel.gamePanel.getHeight() / GamePanel.cellWidth) - totalViewPortOffsetY) / scaleDownFactor);
        int xMin = (int)Math.floor(-totalViewPortOffsetX / scaleDownFactor);
        int xMax = (int)Math.ceil(((GamePanel.gamePanel.getWidth() / GamePanel.cellWidth) - totalViewPortOffsetX) / scaleDownFactor);


        for(int y = yMin; y < yMax; y++) {
            for(int x = xMin; x < xMax; x++) {

                boolean cell = GamePanel.boardManager.getCell((int)(x * scaleDownFactor), (int)(y * scaleDownFactor));

                if(x == 0 && y == 0) g.setColor(cell ? Constants.HOME_LIVE_COLOR : Constants.HOME_COLOR);
                else if(y == 0) g.setColor(cell ? Constants.X_LIVE_COLOR : Constants.X_COLOR);
                else if(x == 0) g.setColor(cell ? Constants.Y_LIVE_COLOR : Constants.Y_COLOR);
                else if(cell) g.setColor(Constants.LIVE_COLOR);
                else continue;

                g.fillRect(
                        (int)((x * scaleDownFactor + totalViewPortOffsetX) * GamePanel.cellWidth),
                        (int)((y * scaleDownFactor + totalViewPortOffsetY) * GamePanel.cellWidth),
                        (int)(GamePanel.cellWidth * scaleDownFactor + 1),
                        (int)(GamePanel.cellWidth * scaleDownFactor + 1));
            }
        }
    }


}
