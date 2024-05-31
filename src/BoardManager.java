import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.*;

public class BoardManager {


    private class GameTimer implements Runnable {

        private long lastTime;
        private double delay;
        private int tps;
        private volatile boolean running;
        GameTimer(double delay){
            this.delay = delay;
            this.running = false;
        }

        public synchronized void start(){
            running = true;
            new Thread(this).start();
        }
        public synchronized void stop(){
            running = false;
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
                    tps = (int)(1000000000.0 / delta);
                }
            }
        }

        public void setDelay(double delay) {
            this.delay = delay;
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
         executor = Executors.newFixedThreadPool(10);
    }


    public synchronized void nextGeneration() {
        DynamicBoard nextBoard = new DynamicBoard();
        List<Callable<String>> checkCoords = new ArrayList<Callable<String>>();
        for (Location point : board.createCheckList()) {
            checkCoords.add(new Callable<String>(){
                @Override
                public String call () {
                    nextBoard.setCell(point.getX(), point.getY(), checkNeighbors(point.getX(), point.getY()));
                    return "executed";
                }
            });
        }
        List<Future<String>> executorStates;
        try {
            executorStates = executor.invokeAll(checkCoords);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        boolean executed = false;
        while(!executed){
            executed = true;
            for (Future<String> executorState : executorStates) {
                if (!executorState.isDone()) executed = false;
            }
        }
        board = nextBoard;
        GamePanel.generation++;
        GamePanel.gameHistory.addToHistory(new GameState(GamePanel.generation, board));
    }

    private boolean checkNeighbors(int x, int y){
        int neighborSum = 0;
        for (int i = -1; i < 2; i++) if (board.getCell(x + i, y + 1)) neighborSum++;
        for (int i = -1; i < 2; i++) if (board.getCell(x + i, y - 1)) neighborSum++;
        if (board.getCell(x + 1, y)) neighborSum++;
        if (board.getCell(x - 1, y)) neighborSum++;

        if(board.getCell(x, y) && neighborSum < 2) return false;
        if(board.getCell(x, y) && neighborSum > 3) return false;
        if(neighborSum == 3) return true;
        return board.getCell(x,y);
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
