import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;


public class GamePanel extends JPanel {

    public static int generation;

    public static GameHistory gameHistory;
    public static PatternPlacer patternPlacer;
    public static PatternPicker patternPicker;
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

        InputMap inputMap= this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK),"copy");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK),"cut");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK),"paste");

        inputMap.put(KeyStroke.getKeyStroke("UP"),"up");
        inputMap.put(KeyStroke.getKeyStroke('w'),"up");

        inputMap.put(KeyStroke.getKeyStroke("DOWN"),"down");
        inputMap.put(KeyStroke.getKeyStroke('s'),"down");

        inputMap.put(KeyStroke.getKeyStroke("LEFT"),"left");
        inputMap.put(KeyStroke.getKeyStroke('a'),"left");

        inputMap.put(KeyStroke.getKeyStroke("RIGHT"),"right");
        inputMap.put(KeyStroke.getKeyStroke('d'),"right");

        inputMap.put(KeyStroke.getKeyStroke('r'),"rotate");
        inputMap.put(KeyStroke.getKeyStroke("ESCAPE"),"escape");

        ActionMap actionMap = this.getActionMap();
        actionMap.put("copy", new AbstractAction(){
            public void actionPerformed(ActionEvent e) {
                if(patternImporter != null) patternImporter.dispose();
                patternPlacer = null;
                patternPicker = null;

                patternPicker = new PatternPicker(false);
            }
        });
        actionMap.put("cut", new AbstractAction(){
            public void actionPerformed(ActionEvent e) {
                if(patternImporter != null) patternImporter.dispose();
                patternPlacer = null;
                patternPicker = null;

                patternPicker = new PatternPicker(true);
            }
        });
        actionMap.put("paste", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if(patternImporter != null) patternImporter.dispose();
                patternPlacer = null;
                patternPicker = null;
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                try {
                    patternPlacer = new PatternPlacer(PatternImporter.convertToArray((String) clipboard.getData(DataFlavor.stringFlavor)));
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (UnsupportedFlavorException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        actionMap.put("up", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                viewPortOffsetY += Constants.PAN_SPEED_FACTOR / cellWidth;
            }
        });
        actionMap.put("down", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                viewPortOffsetY -= Constants.PAN_SPEED_FACTOR / cellWidth;
            }
        });
        actionMap.put("left", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                viewPortOffsetX += Constants.PAN_SPEED_FACTOR / cellWidth;
            }
        });
        actionMap.put("right", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                viewPortOffsetX -= Constants.PAN_SPEED_FACTOR / cellWidth;
            }
        });
        actionMap.put("rotate", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                patternPlacer.rotatePattern();
            }
        });
        actionMap.put("escape", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                    if(patternImporter != null) patternImporter.dispose();
                    patternPlacer = null;
                    patternPicker = null;
            }
        });

        boardManager = new BoardManager();
        gamePanel = this;
        boardManager.setBoard(new DynamicBoard());
        setPreferredSize(new Dimension(Constants.DESIRED_VIEWPORT_WIDTH, Constants.DESIRED_VIEWPORT_HEIGHT));
        setBackground(Constants.BACKGROUND_COLOR);
        setViewPortHome(Constants.DESIRED_VIEWPORT_WIDTH, Constants.DESIRED_VIEWPORT_HEIGHT);

        generation = 0;
        gameHistory = new GameHistory();
        gameHistory.addToHistory(new GameState(generation, boardManager.getBoard()));

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                MainFrame.frame.mainPanel.grabFocus();
                double totalViewPortOffsetY = GamePanel.viewPortOffsetY + GamePanel.liveViewPortOffsetY;
                double totalViewPortOffsetX = GamePanel.viewPortOffsetX + GamePanel.liveViewPortOffsetX;
                if(e.getButton() == MouseEvent.BUTTON1) {
                    if (patternPlacer != null) {
                        patternPlacer.writeToBoard();
                        patternPlacer = null;
                    }
                    else if(patternPicker != null) {
                        patternPicker.mousePressed();
                    }
                    else invertCell(
                            (int)Math.floor((e.getX() / cellWidth - totalViewPortOffsetX)) ,
                            (int)Math.floor((e.getY() / cellWidth - totalViewPortOffsetY))
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
                if(e.getButton() == MouseEvent.BUTTON1) {
                    if(patternPicker != null) {
                        String myString = patternPicker.mouseReleased();
                        patternPicker = null;

                        StringSelection stringSelection = new StringSelection(myString);
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(stringSelection, null);
                    }
                }
                if(e.getButton() == MouseEvent.BUTTON2 || e.getButton() == MouseEvent.BUTTON3) {
                    isDragging = false;
                    viewPortOffsetX += liveViewPortOffsetX;
                    viewPortOffsetY += liveViewPortOffsetY;
                    liveViewPortOffsetX = 0;
                    liveViewPortOffsetY = 0;
                }
            }
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
                if (patternPicker != null) patternPicker.updateCoords(p);
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


    public static boolean updateTimerSpeed(int freq){
        boolean inputNotOk = freq <= 0;
        if (inputNotOk) boardManager.setTimerDelay(Constants.DEFAULT_GAME_DELAY);
        else boardManager.setTimerDelay(1000.0 / freq);
        return inputNotOk;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        DisplayController.drawBoard(g2);
        if (patternPlacer != null) patternPlacer.draw(g2);
        if (patternPicker != null) patternPicker.draw(g2);
    }
}
