import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;


public class GamePanel extends JPanel {

    public static int generation;

    public static GameHistory gameHistory;
    public static PatternPlacer patternPlacer;
    public static JDialog patternImporter;

    public static GamePanel gamePanel;
    public static BoardManager boardManager;

    public static double cellWidth;
    private static double dragStartX, dragStartY;
    private boolean isDragging;
    public static double viewPortOffsetX;
    public static double viewPortOffsetY;
    public static double liveViewPortOffsetX;
    public static double liveViewPortOffsetY;

    private long lastTime;
    public static int fps;

    GamePanel thisPanel = this;

    GamePanel() {
        super();
        boardManager = new BoardManager();
        gamePanel = this;
        boardManager.setBoard(new DynamicBoard());
        setPreferredSize(new Dimension(Constants.DESIRED_VIEWPORT_WIDTH, Constants.DESIRED_VIEWPORT_HEIGHT));
        setBackground(Constants.ACCENT_COLOR);
        setViewPortHome(Constants.DESIRED_VIEWPORT_WIDTH, Constants.DESIRED_VIEWPORT_HEIGHT);

        generation = 0;
        gameHistory = new GameHistory();
        gameHistory.addToHistory(new GameState(generation, boardManager.getBoard()));

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
                            (int)Math.floor((e.getX() / cellWidth - viewPortOffsetX)) ,
                            (int)Math.floor((e.getY() / cellWidth - viewPortOffsetY))
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
                long time = System.nanoTime();
                long delta = time - lastTime;
                fps = (int)(1000000000.0 / delta);
                lastTime = time;

                Point p = MouseInfo.getPointerInfo().getLocation();
                SwingUtilities.convertPointFromScreen(p, thisPanel);
                if(isDragging){
                    liveViewPortOffsetX = (p.getX() - dragStartX) / cellWidth;
                    liveViewPortOffsetY = (p.getY() - dragStartY) / cellWidth;
                }
                if (patternPlacer != null) patternPlacer.updateCoords(p);
                TopButtonPanel.updateLabels();
                repaint();
            }
        });

        displayTimer.start();
    }

    public void invertCell(int x, int y){
        boardManager.invertCell(x, y);
        generation = 0;
        gameHistory.addToHistory(new GameState(generation, boardManager.getBoard()));
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

    public void setViewPortHome(){
        cellWidth = Constants.DEFAULT_CELL_WIDTH;
        viewPortOffsetX = this.getWidth() / cellWidth / 2;
        viewPortOffsetY = this.getHeight() / cellWidth / 2;
    }

    public void setViewPortHome(double x, double y){
        cellWidth = Constants.DEFAULT_CELL_WIDTH;
        viewPortOffsetX = x / cellWidth / 2;
        viewPortOffsetY = y / cellWidth / 2;
    }

    public void drawBoard(Graphics g){
        double cellBoarderWidth = GamePanel.cellWidth * Constants.CELL_BORDER_RATIO;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double totalViewPortOffsetY = viewPortOffsetY + liveViewPortOffsetY;
        double totalViewPortOffsetX = viewPortOffsetX + liveViewPortOffsetX;

        int yMin = (int)Math.floor(-totalViewPortOffsetY);
        int yMax = (int)Math.ceil((this.getHeight() / cellWidth) - totalViewPortOffsetY);
        int xMin = (int)Math.floor(-totalViewPortOffsetX);
        int xMax = (int)Math.ceil((this.getWidth() / cellWidth) - totalViewPortOffsetX);

        for(int y = yMin; y < yMax; y++) {
            for(int x = xMin; x < xMax; x++) {
                boolean cell = boardManager.getCell(x, y);
                if(x == 0 && y == 0) g.setColor(cell ? Constants.HOME_LIVE_COLOR : Constants.HOME_COLOR);
                else if(y == 0) g.setColor(cell ? Constants.X_LIVE_COLOR : Constants.X_COLOR);
                else if(x == 0) g.setColor(cell ? Constants.Y_LIVE_COLOR : Constants.Y_COLOR);
                else g.setColor(cell ? Constants.LIVE_COLOR : Constants.BACKGROUND_COLOR);

                Rectangle2D rect = new Rectangle2D.Double(
                        (cellBoarderWidth / 2) + (x + totalViewPortOffsetX) * cellWidth,
                        (cellBoarderWidth / 2) + (y + totalViewPortOffsetY) * cellWidth,
                        cellWidth - cellBoarderWidth,
                        cellWidth - cellBoarderWidth);
                g2.fill(rect);
            }
        }
    }

    public static void resetBoard(){
            boardManager.setBoard(new DynamicBoard());
            generation = 0;
            gameHistory.addToHistory(new GameState(generation, boardManager.getBoard()));
    }

    public static void randomizeBoard(){
        boardManager.setBoard(new DynamicBoard());
        for(int y = -100; y <= 100; y++) {
            for (int x = -100; x <= 100; x++) {
                boardManager.setCell(x, y, (int)(2 * Math.random()) == 0);
            }
        }
        generation = 0;
        gameHistory.addToHistory(new GameState(generation, boardManager.getBoard()));
    }


    public static boolean updateTimerSpeed(String freq){
        boolean inputNotOk;
        try{inputNotOk = Integer.parseInt(freq) <= 0;}
        catch (Exception e) {inputNotOk = true;}
        if (inputNotOk) boardManager.setTimerDelay(Constants.DEFAULT_GAME_DELAY);
        else boardManager.setTimerDelay(1000.0 / Integer.parseInt(freq));
        return inputNotOk;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        if (patternPlacer != null) patternPlacer.drawPattern(g);

    }
}
