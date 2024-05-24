import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;


public class GamePanel extends JPanel {

    private static Timer gameTimer;
    public static volatile boolean[][] currentBoard;

    public static PatternPlacer patternPlacer;
    public static JDialog patternImporter;

    public static double cellWidth;
    private static double dragStartX, dragStartY;
    private boolean isDragging;
    public static double viewPortOffsetX;
    public static double viewPortOffsetY;
    public static double liveViewPortOffsetX;
    public static double liveViewPortOffsetY;

    GamePanel thisPanel = this;

    GamePanel() {
        setPreferredSize(new Dimension(Constants.DESIRED_VIEWPORT_WIDTH, Constants.DESIRED_VIEWPORT_HEIGHT));
        setBackground(Constants.ACCENT_COLOR);
        currentBoard = new boolean[Constants.BOARD_HEIGHT][Constants.BOARD_WIDTH];
        cellWidth = Constants.DEFAULT_CELL_WIDTH;

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1) {
                    if (patternPlacer != null) {
                        patternPlacer.writeToBoard();
                        patternPlacer = null;
                        return;
                    }
                    invertCell(
                            (int)(e.getX() / cellWidth - viewPortOffsetX) ,
                            (int)(e.getY() / cellWidth - viewPortOffsetY)
                    );
                }
                if(e.getButton() == MouseEvent.BUTTON2 || e.getButton() == MouseEvent.BUTTON3) {
                    isDragging = true;
                    dragStartX = e.getX();
                    dragStartY = e.getY();
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON2 || e.getButton() == MouseEvent.BUTTON3) {
                    isDragging = false;
                    viewPortOffsetX += liveViewPortOffsetX;
                    viewPortOffsetY += liveViewPortOffsetY;
                    liveViewPortOffsetX = 0;
                    liveViewPortOffsetY = 0;
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                centeredZoom(e.getPreciseWheelRotation());
            }
        });

        Timer displayTimer = new Timer(Constants.DISPLAY_LOOP_TIME, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Point p = MouseInfo.getPointerInfo().getLocation();
                SwingUtilities.convertPointFromScreen(p, thisPanel);
                if(isDragging){
                    liveViewPortOffsetX = (p.getX() - dragStartX) / cellWidth;
                    liveViewPortOffsetY = (p.getY() - dragStartY) / cellWidth;
                }
                if (patternPlacer != null) patternPlacer.updateCoords(p);
                repaint();
            }
        });

        displayTimer.start();

        gameTimer = new Timer(Constants.DEFAULT_GAME_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextGeneration();
            }
        });

        displayTimer.start();
    }

    public void invertCell(int x, int y){
        currentBoard[y][x] = !currentBoard[y][x];
    }

    public void centeredZoom(double zoomFactor){
        Point p = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(p, thisPanel);
        double oldCellWidth = cellWidth;
        cellWidth -= (Constants.ZOOM_SCALE_FACTOR * cellWidth * zoomFactor);
        cellWidth = Math.clamp(cellWidth, Constants.MIN_CELL_WIDTH, Constants.MAX_CELL_WIDTH);
        viewPortOffsetX -= (p.getX() * (cellWidth / oldCellWidth - 1)) / cellWidth;
        viewPortOffsetY -= (p.getY() * (cellWidth / oldCellWidth - 1)) / cellWidth;
    }
    public void drawArray(Graphics g){
        double cellBoarderWidth = GamePanel.cellWidth * Constants.CELL_BORDER_RATIO;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for(int y = 0; y < currentBoard.length; y++) {
            for(int x = 0; x < currentBoard[0].length; x++) {
                g.setColor(currentBoard[y][x] ? Constants.LIVE_COLOR : Constants.BACKGROUND_COLOR);
                Rectangle2D rect = new Rectangle2D.Double(
                        (cellBoarderWidth / 2) + (x + viewPortOffsetX + liveViewPortOffsetX) * cellWidth,
                        (cellBoarderWidth / 2) + (y + viewPortOffsetY + liveViewPortOffsetY) * cellWidth,
                        cellWidth - cellBoarderWidth,
                        cellWidth - cellBoarderWidth);
                g2.fill(rect);
            }
        }
    }

    public static void resetBoard(){
            currentBoard = new boolean[Constants.BOARD_HEIGHT][Constants.BOARD_WIDTH];
    }

    public static void randomizeBoard(){
        currentBoard = new boolean[Constants.BOARD_HEIGHT][Constants.BOARD_WIDTH];
        for(int y = 0; y < currentBoard.length; y++) {
            for (int x = 0; x < currentBoard[0].length; x++) {
                currentBoard[y][x] = (int)(2 * Math.random()) == 0;
            }
        }
    }

    public static void nextGeneration() {
        boolean[][] nextBoard = new boolean[Constants.BOARD_HEIGHT][Constants.BOARD_WIDTH];
        for(int y = 0; y < currentBoard.length; y++) {
            for (int x = 0; x < currentBoard[0].length; x++) {
                boolean newCell = false;
                try {newCell = checkNeighbors(x,y);}
                catch (Exception e) {}
                nextBoard[y][x] = newCell;
            }
        }
        currentBoard = nextBoard;
    }
    public static boolean checkNeighbors(int x, int y){
        int neighborSum = 0;
        for (int i = -1; i < 2; i++) if (currentBoard[y + 1][x + i]) neighborSum++;
        for (int i = -1; i < 2; i++) if (currentBoard[y - 1][x + i]) neighborSum++;
        if (currentBoard[y][x + 1]) neighborSum++;
        if (currentBoard[y][x - 1]) neighborSum++;

        if(currentBoard[y][x] && neighborSum < 2) return false;
        if(currentBoard[y][x] && neighborSum > 3) return false;
        if(neighborSum == 3) return true;
        return currentBoard[y][x];
    }

    public static void startTimer(){
        gameTimer.start();
    }

    public static void stopTimer(){
        gameTimer.stop();
    }

    public static void updateTimerSpeed(String freq){
        boolean inputNotOk;
        try{inputNotOk = Integer.parseInt(freq) <= 0;}
        catch (Exception e) {inputNotOk = true;}
        if (inputNotOk) gameTimer.setDelay(Constants.DEFAULT_GAME_DELAY);
        else gameTimer.setDelay(1000 / Integer.parseInt(freq));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawArray(g);
        if (patternPlacer != null) patternPlacer.drawPattern(g);

    }
}
