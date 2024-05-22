import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class ButtonPanel extends JPanel {
    private Thread sortThread;

    ButtonPanel(MainFrame frame){
        setPreferredSize(new Dimension(0, Constants.BUTTON_HEIGHT));
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

        JButton stepFwd = new GameButton("Step Forward");
        stepFwd.setPreferredSize(new Dimension(130, 30));
        stepFwd.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                GamePanel.nextGeneration();
            }
        });

        JButton stepRev = new GameButton("Step Reverse");
        stepRev.setPreferredSize(new Dimension(130, 30));
        stepRev.addActionListener(new ActionListener(){
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


        JPanel buttonSubPanel = new JPanel(new GridLayout(1,8,20,0));
        buttonSubPanel.setBackground(Constants.BACKGROUND_COLOR);

        buttonSubPanel.add(stop);
        buttonSubPanel.add(stepRev);
        buttonSubPanel.add(stepFwd);
        buttonSubPanel.add(start);
        buttonSubPanel.add(importString);
        buttonSubPanel.add(reset);
        buttonSubPanel.add(random);
        buttonSubPanel.add(speed);

        add(buttonSubPanel);


    }
}
