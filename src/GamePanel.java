import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;


public class GamePanel extends JPanel {

    private Timer timer;
    public static volatile int[][] currentBoard;
    private int[] writtenIndexes;
    private int[] readIndexes;
    public static volatile Constants.PanelStates panelState;

    Synthesizer syn;
    MidiChannel[] midChannel;
    Instrument[] instrument;



    GamePanel() {
        setPreferredSize(new Dimension(Constants.BOARD_PIXEL_WIDTH, Constants.BOARD_PIXEL_HEIGHT));
        setBackground(Constants.BACKGROUND_COLOR);

        panelState = Constants.PanelStates.IDLE_PHASE;
        currentBoard = new int[Constants.BOARD_HEIGHT][Constants.BOARD_WIDTH];

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x=e.getX();
                int y=e.getY();
                System.out.println(x+","+y);//these co-ords are relative to the component
            }
        });

        timer = new Timer(Constants.DISPLAY_LOOP_TIME, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });

        timer.start();
    }

    public void drawArray(Graphics g){
        boolean hasChanged = false;
        boolean[] wasWritten = array.getWasWritten();
        boolean[] wasRead = array.getWasRead();

        for(int x = 0; x < Constants.BOARD_WIDTH; x++) if(wasWritten[x]) {
            writtenIndexes[x] = Constants.HIGHLIGHT_LOOP_TIME;
            hasChanged = true;
        }
        for(int x = 0; x < Constants.BOARD_WIDTH; x++) if(wasRead[x]) {
            readIndexes[x] = Constants.HIGHLIGHT_LOOP_TIME;
            hasChanged = true;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int maxValue = 0;
        for(int x = 0; x < array.length; x++) maxValue = Math.max(maxValue, array.getSilently(x));

        double spaceWidth = (Constants.BOARD_PIXEL_WIDTH - (2.0 * Constants.BOARD_BORDER_WIDTH)) / (Constants.BAR_SPACE_RATIO * array.length + array.length - 1);
        double heightRatio = (Constants.BOARD_PIXEL_HEIGHT - (2.0 * Constants.BOARD_BORDER_WIDTH)) / maxValue;
        g.setColor(Constants.PRIMARY_COLOR);
        for(int x = 0; x < array.length; x++) {
            g.setColor(wasWritten[x] ? Constants.WRITTEN_COLOR : (wasRead[x] ? Constants.READ_COLOR : Constants.PRIMARY_COLOR));
            if(writtenIndexes[x] >= 0 && hasChanged) writtenIndexes[x]--;
            double barHeight = array.getSilently(x) * heightRatio;
            Rectangle2D rect = new Rectangle2D.Double(
                    (Constants.BOARD_BORDER_WIDTH + (x * spaceWidth * (1 + Constants.BAR_SPACE_RATIO))),
                    Constants.BOARD_PIXEL_HEIGHT - barHeight - Constants.BOARD_BORDER_WIDTH,
                    (spaceWidth * Constants.BAR_SPACE_RATIO),
                    barHeight);
            g2.fill(rect);

        }
    }

    public static void resetArray(){
        array = new MonitoredArray(Constants.BOARD_WIDTH);
        for (int x = 0; x < Constants.BOARD_WIDTH; x++) array.set(x, Constants.BOARD_WIDTH - x);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawArray(g);
    }
}
