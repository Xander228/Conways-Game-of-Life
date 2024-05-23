import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainFrame extends JFrame {

    //To do:
    //Undo redo
    //Cell cord readout
    //Generation number
    //move to non-swing game timer
    //Render check
    //Infinite board
    //Multithreading board check
    //Export



    public static MainFrame frame;

    public MainFrame() {
        //Set up the frame properties
        setTitle("Conway's Game of Life"); //Title of the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createMatteBorder(10,10,10,10,Constants.ACCENT_COLOR)); //Add a border around the frame
        mainPanel.setBackground(Constants.ACCENT_COLOR); //Set the background color of the panel
        mainPanel.setLayout(new BorderLayout(10,10)); //Sets the edge offset of member panels to properly space them

        ButtonPanel buttonPanel = new ButtonPanel(this);
        GamePanel gamePanel = new GamePanel();

        mainPanel.add(gamePanel);
        mainPanel.add(buttonPanel,BorderLayout.SOUTH);

        addKeyListener(
                //creates a new KeyListener object with the following methods overriden
                new KeyListener() {
                    //Must be overriden to create object due to KeyListener being an interface
                    @Override
                    public void keyTyped(KeyEvent e) {
                    }

                    //Overrides the keyPressed method to add the state of the current pressed key to the map
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if(e.getKeyCode() == KeyEvent.VK_R) PatternPlacer.rotatePattern();
                        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                            if(GamePanel.patternImporter != null) GamePanel.patternImporter.dispose();
                            GamePanel.patternPlacer = null;
                        }
                    }

                    //Overrides the keyReleased method to add the state of the current released key to the map
                    @Override
                    public void keyReleased(KeyEvent e) {
                    }
                }
        );



        add(mainPanel);
        pack();
        setFocusable(true);

        //Set the frame visible
        setVisible(true);
    }

    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable(){
            public void run(){
                frame = new MainFrame();
            }
        });
    }



}