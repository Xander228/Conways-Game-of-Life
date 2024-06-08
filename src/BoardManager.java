import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.*;

public class BoardManager {


    private class GameTimer implements Runnable {

        private long lastTime;
        private double delay;
        private int tps;
        private volatile boolean running;
        private Thread gameThread;
        private AverageFinder averageFinder;
        GameTimer(double delay){
            this.delay = delay;
            this.running = false;
            this.averageFinder = new AverageFinder(200);
        }

        public synchronized void start(){
            running = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
        public synchronized void stop(){
            running = false;
            if(gameThread != null) gameThread.interrupt();
            tps = 0;
        }

        @Override
        public void run(){
            while (running){
                long time = System.nanoTime();
                long delta = time - lastTime;
                if(delta >= delay * 1000000L) {
                    nextGeneration();
                    lastTime = time;
                    tps = averageFinder.averageOf((int)(1000000000.0 / delta));
                }
            }
        }

        public void setDelay(double delay) {
            this.delay = delay;
        }

        public boolean isRunning() {
            return running;
        }

        public int getTps(){
            return tps;
        }

    }


    private volatile GameTimer gameTimer;
    private volatile DynamicBoard board;
    ExecutorService executor;



    BoardManager(){
        gameTimer = new GameTimer(Constants.DEFAULT_GAME_DELAY);
         executor = Executors.newFixedThreadPool(16);
    }

    public synchronized void nextGeneration() {
        DynamicBoard nextBoard = new DynamicBoard();
        List<Callable<String>> callList = new ArrayList<Callable<String>>();
        for(Location point : board.getSet()) {
            callList.add(new Callable<String>() {
                @Override
                public String call() {
                    List<Location> locations = new ArrayList<Location>();
                    int xCenter = point.getX();
                    int yCenter = point.getY();
                    boolean[][] boardCache = new boolean[5][5];
                    for (int i = -2; i <= 2; i++) {
                        for (int j = -2; j <= 2; j++) {
                            boardCache[i + 2][j + 2] =
                                    board.getCell(i + xCenter, j + yCenter);
                        }
                    }

                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            if(checkLocalNeighbors(i + 2, j + 2, boardCache))
                                locations.add(new Location(xCenter + i, yCenter + j));
                        }
                    }
                    nextBoard.setCell(locations);
                    return "executed";
                }
            });
        }

        try {
            executor.invokeAll(callList);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = nextBoard;
        GamePanel.generation++;
        GamePanel.gameHistory.addToHistory(new GameState(GamePanel.generation, board));
    }


    private boolean checkLocalNeighbors(int x, int y, boolean[][] boardCache){
        int neighborSum = 0;
        for (int i = -1; i <= 1; i++) if (boardCache[x + i][y + 1]) neighborSum++;
        for (int i = -1; i <= 1; i++) if (boardCache[x + i][y - 1]) neighborSum++;
        if (boardCache[x + 1][y]) neighborSum++;
        if (boardCache[x - 1][y]) neighborSum++;

        boolean currentCell = boardCache[x][y];
        if(currentCell && neighborSum < 2) return false;
        if(currentCell && neighborSum > 3) return false;
        if(neighborSum == 3) return true;
        return currentCell;
    }

    public void startTimer(){
        gameTimer.start();
    }

    public void stopTimer(){
        gameTimer.stop();
    }

    public void setTimerDelay(double delay){
        gameTimer.setDelay(delay);
    }

    public synchronized void invertCell(int x, int y){
        board.setCell(x, y, !board.getCell(x,y));
    }

    public void setBoard(DynamicBoard board){
        gameTimer.stop();
        this.board = board;
    }

    public void setCell(int x, int y, boolean cell){
        gameTimer.stop();
        board.setCell(x, y, cell);
    }

    public DynamicBoard getBoard(){
        return board;
    }
    public boolean getCell(int x, int y){
        return board.getCell(x,y);
    }

    public int getTps(){
        return gameTimer.getTps();
    }

    public int getSize(){
        return board.getSize();
    }

    public int getXMax(){
        return board.getXMax();
    }

    public int getXMin(){
        return board.getXMin();
    }

    public int getYMax(){
        return board.getYMax();
    }

    public int getYMin(){
        return board.getYMin();
    }



}
