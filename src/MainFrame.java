import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        //Set up the frame properties
        setTitle("Conway's Game of Life"); //Title of the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Behavior on close, exits the program when the frame is closed
        setResizable(false); //sets the frame to a fixed size, not resizeable by a user
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createMatteBorder(10,10,10,10,Constants.ACCENT_COLOR)); //Add a border around the frame
        mainPanel.setBackground(Constants.ACCENT_COLOR); //Set the background color of the panel
        mainPanel.setLayout(new BorderLayout(10,10)); //Sets the edge offset of member panels to properly space them

        ButtonPanel buttonPanel = new ButtonPanel(this);
        GamePanel gamePanel = new GamePanel();

        mainPanel.add(gamePanel);
        mainPanel.add(buttonPanel,BorderLayout.SOUTH);

        add(mainPanel);
        pack();
        setFocusable(true);

        //Set the frame visible
        setVisible(true);
    }

    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable(){
            public void run(){
                new MainFrame();
                new PatternImporter();
            }
        });
    }



}