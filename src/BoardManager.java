import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BoardManager {

    public static volatile Timer gameTimer;
    public static volatile DynamicBoard board;

    private static volatile long lastTime;
    public static volatile int tps;

    BoardManager(){
        gameTimer = new Timer(Constants.DEFAULT_GAME_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextGeneration();
            }
        });
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

    public static void startThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    long time = System.nanoTime();
                    long delta = time - lastTime;
                    tps = (int)(1000000000.0 / delta);
                    lastTime = time;
                    nextGeneration();
                }
            }
        }).start();
    }

    public static void startTimer(){
        gameTimer.start();
    }



    public static void stopTimer(){
        gameTimer.stop();
    }
}
