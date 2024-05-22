import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

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
                GamePanel.startTimer();
            }
        });

        JButton step = new GameButton("Step");
        step.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                GamePanel.nextGeneration();
            }
        });

        JButton stop = new GameButton("Stop");
        stop.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                GamePanel.stopTimer();
            }
        });

        JButton reset = new GameButton("Reset");
        reset.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                GamePanel.stopTimer();
                GamePanel.resetBoard();
            }
        });

        JButton random = new GameButton("Random");
        random.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                GamePanel.stopTimer();
                GamePanel.randomizeBoard();
            }
        });

        JButton importString = new GameButton("Import");
        importString.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(GamePanel.patternImporter != null) GamePanel.patternImporter.dispose();
                GamePanel.patternImporter = new PatternImporter();
            }
        });

        JTextField speed = new JTextField("" + 1000 / Constants.DEFAULT_GAME_DELAY,6);
        speed.setPreferredSize(new Dimension(100, 30));
        speed.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Constants.ACCENT_COLOR));
        speed.setFont(new Font("Arial", Font.BOLD, 16));
        speed.setForeground(Constants.BACKGROUND_COLOR);
        speed.setHorizontalAlignment(SwingConstants.CENTER);
        speed.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                GamePanel.updateTimerSpeed(speed.getText());
            }
            public void removeUpdate(DocumentEvent e) {
                GamePanel.updateTimerSpeed(speed.getText());
            }
            public void insertUpdate(DocumentEvent e) {
                GamePanel.updateTimerSpeed(speed.getText());
            }
        });


        JPanel leftSubPanel = new JPanel();
        leftSubPanel.setLayout(new BorderLayout(20, 0));
        leftSubPanel.setBackground(Constants.BACKGROUND_COLOR);

        JPanel rightSubPanel = new JPanel();
        rightSubPanel.setLayout(new BorderLayout(20, 0));
        rightSubPanel.setBackground(Constants.BACKGROUND_COLOR);

        leftSubPanel.add(start, BorderLayout.WEST);
        leftSubPanel.add(step);
        leftSubPanel.add(stop, BorderLayout.EAST);

        rightSubPanel.add(reset, BorderLayout.WEST);
        rightSubPanel.add(random);
        rightSubPanel.add(speed, BorderLayout.EAST);

        add(leftSubPanel,BorderLayout.WEST);
        add(rightSubPanel,BorderLayout.EAST);
        add(importString,BorderLayout.CENTER);
    }
}
