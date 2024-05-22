import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;


public class GamePanel extends JPanel {

    private static Timer gameTimer;
    public static volatile boolean[][] currentBoard;

    public static PatternPlacer patternPlacer;
    public static volatile Constants.PanelStates panelState;

    GamePanel thisPanel = this;

    GamePanel() {
        setPreferredSize(new Dimension(Constants.BOARD_PIXEL_WIDTH, Constants.BOARD_PIXEL_HEIGHT));
        setBackground(Constants.ACCENT_COLOR);

        panelState = Constants.PanelStates.IDLE_PHASE;
        currentBoard = new boolean[Constants.BOARD_HEIGHT][Constants.BOARD_WIDTH];

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {
                if (patternPlacer != null){
                    patternPlacer.writeToBoard();
                    patternPlacer = null;
                    return;
                }
                invertCell(
                        (int)(e.getX() / Constants.CELL_WIDTH),
                        (int)(e.getY() / Constants.CELL_WIDTH));
            }
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        Timer displayTimer = new Timer(Constants.DISPLAY_LOOP_TIME, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Point p = MouseInfo.getPointerInfo().getLocation();
                SwingUtilities.convertPointFromScreen(p, thisPanel);
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

    public void drawArray(Graphics g){

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for(int y = 0; y < currentBoard.length; y++) {
            for(int x = 0; x < currentBoard[0].length; x++) {
                g.setColor(currentBoard[y][x] ? Constants.LIVE_COLOR : Constants.BACKGROUND_COLOR);
                Rectangle2D rect = new Rectangle2D.Double(
                        (int)(Constants.CELL_BORDER_WIDTH / 2) + (x * Constants.CELL_WIDTH),
                        (int)(Constants.CELL_BORDER_WIDTH / 2) + (y * Constants.CELL_WIDTH),
                        Constants.CELL_WIDTH - Constants.CELL_BORDER_WIDTH,
                        Constants.CELL_WIDTH - Constants.CELL_BORDER_WIDTH);
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
