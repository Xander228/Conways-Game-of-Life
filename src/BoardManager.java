import java.util.HashSet;

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



    BoardManager(){
        gameTimer = new GameTimer(Constants.DEFAULT_GAME_DELAY);
    }


    public synchronized void nextGeneration() {
        DynamicBoard nextBoard = new DynamicBoard();
        for (Location point : board.createCheckList())
            nextBoard.setCell(point.getX(), point.getY(),
                    checkNeighbors(point.getX(), point.getY()));
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
