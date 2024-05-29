import javax.xml.stream.Location;
import java.util.HashSet;

public class BoardManager {

    public class GameTimer implements Runnable {

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


    public static volatile GameTimer gameTimer;
    public static volatile DynamicBoard board;



    BoardManager(){
        gameTimer = new GameTimer(Constants.DEFAULT_GAME_DELAY);
    }


    public static void nextGeneration() {
        DynamicBoard nextBoard = new DynamicBoard();
        for(int y = board.getYMin() - 1; y <= board.getYMax() + 1; y++) {
            for (int x = board.getXMin() - 1; x <= board.getXMax() + 1; x++) {
                nextBoard.setCell(x, y, checkNeighbors(x, y));
            }
        }
        board = nextBoard;
        GamePanel.generation++;
        GamePanel.gameHistory.addToHistory(new GameState(GamePanel.generation, board));
    }


    public static boolean checkNeighbors(int x, int y){
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



    public static void startTimer(){
        gameTimer.start();
    }



    public static void stopTimer(){
        gameTimer.stop();
    }
}
