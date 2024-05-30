import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainFrame extends JFrame {

    //To do:
    //Multithreading board check
    //Export
    //Import catalog



    public static MainFrame frame;

    public MainFrame() {
        super();
        setTitle("Conway's Game of Life"); //Title of the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createMatteBorder(10,10,10,10,Constants.ACCENT_COLOR)); //Add a border around the frame
        mainPanel.setBackground(Constants.ACCENT_COLOR); //Set the background color of the panel
        mainPanel.setLayout(new BorderLayout(10,10)); //Sets the edge offset of member panels to properly space them

        ButtonPanel buttonPanel = new ButtonPanel(this);
        TopButtonPanel topButtonPanel = new TopButtonPanel(this);
        GamePanel gamePanel = new GamePanel();
        mainPanel.add(topButtonPanel,BorderLayout.NORTH);
        mainPanel.add(gamePanel);
        mainPanel.add(buttonPanel,BorderLayout.SOUTH);

        addKeyListener(
                new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {

                    }

                    //Overrides the keyPressed method to add the state of the current pressed key to the map
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if(e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP)
                            GamePanel.viewPortOffsetY += Constants.PAN_SPEED_FACTOR / GamePanel.cellWidth;
                        if(e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN)
                            GamePanel.viewPortOffsetY -= Constants.PAN_SPEED_FACTOR / GamePanel.cellWidth;
                        if(e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT)
                            GamePanel.viewPortOffsetX += Constants.PAN_SPEED_FACTOR / GamePanel.cellWidth;
                        if(e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT)
                            GamePanel.viewPortOffsetX -= Constants.PAN_SPEED_FACTOR / GamePanel.cellWidth;

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