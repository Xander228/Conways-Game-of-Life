import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonPanel extends JPanel {
    private Thread sortThread;


    ButtonPanel(MainFrame frame){
        setPreferredSize(new Dimension(Constants.BOARD_PIXEL_WIDTH, Constants.BUTTON_HEIGHT));
        setBorder(BorderFactory.createMatteBorder(25, 50, 25, 50, Constants.BACKGROUND_COLOR));
        setLayout(new BorderLayout(200, 0));
        setBackground(Constants.BACKGROUND_COLOR);

        class GameButton extends JButton {
            GameButton(String text){
                super(text);
                this.setFocusable(false);
                this.setPreferredSize(new Dimension(100, 30));
                this.setBackground(Constants.PRIMARY_COLOR);
                this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Constants.ACCENT_COLOR));
                this.setFont(new Font("Arial", Font.BOLD, 16));
                this.setForeground(Constants.BACKGROUND_COLOR);
            }
        }

        JButton start = new GameButton("Start");

        start.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                ArrayPanel.panelState = Constants.PanelStates.SORT_PHASE;
                if(sortThread != null) sortThread.interrupt();
                sortThread = new Thread(QuickSort::sort);
                sortThread.start();
            }
        });

        JButton stop = new GameButton("Stop");
        stop.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                ArrayPanel.panelState = Constants.PanelStates.IDLE_PHASE;
                if(sortThread != null) sortThread.interrupt();

            }
        });

        JButton shuffle = new GameButton("Shuffle");
        shuffle.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                ArrayPanel.resetArray();
                ArrayPanel.panelState = Constants.PanelStates.SHUFFLE_PHASE;
                if(sortThread != null) sortThread.interrupt();
                sortThread = new Thread(Shuffle::run);
                sortThread.start();
            }
        });

        add(start, BorderLayout.WEST);
        add(stop);
        add(shuffle, BorderLayout.EAST);
    }
}
