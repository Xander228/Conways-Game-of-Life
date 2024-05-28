import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class ButtonPanel extends JPanel {

    ButtonPanel(MainFrame frame){
        super();
        setLayout(new BorderLayout(20, 15));
        setBackground(Constants.BACKGROUND_COLOR);

        class GameButton extends JButton {
            GameButton(String text){
                super(text);
                this.setFocusable(false);
                this.setPreferredSize(new Dimension(80, 40));
                this.setBackground(Constants.PRIMARY_COLOR);
                this.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Constants.ACCENT_COLOR));
                this.setFont(new Font("Arial", Font.BOLD, 16));
                this.setForeground(Constants.BACKGROUND_COLOR);
            }
        }

        JButton start = new GameButton("Start");
        start.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                BoardManager.startTimer();
            }
        });

        JButton step = new GameButton("Step");
        step.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                BoardManager.stopTimer();
                BoardManager.nextGeneration();
            }
        });

        JButton stop = new GameButton("Stop");
        stop.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                BoardManager.stopTimer();
            }
        });

        JButton reset = new GameButton("Reset");
        reset.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                BoardManager.stopTimer();
                GamePanel.resetBoard();
            }
        });

        JButton random = new GameButton("Random");
        random.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                BoardManager.stopTimer();
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
        speed.setPreferredSize(new Dimension(80, 40));
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


        JPanel buttonSubPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20,5));
        buttonSubPanel.setBackground(Constants.BACKGROUND_COLOR);


        buttonSubPanel.add(stop);
        buttonSubPanel.add(step);
        buttonSubPanel.add(start);
        buttonSubPanel.add(importString);
        buttonSubPanel.add(reset);
        buttonSubPanel.add(random);
        buttonSubPanel.add(speed);

        add(buttonSubPanel);


    }
}
