import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class TopButtonPanel extends JPanel {



        TopButtonPanel(MainFrame frame){
            super();
            setLayout(new BorderLayout(20, 5));
            setBackground(Constants.BACKGROUND_COLOR);

            class GameButton extends JButton {
                Image image;

                GameButton(String imageFilename) {
                    try {
                        image = ImageIO.read(this.getClass().getResource(imageFilename));
                    } catch (Exception e) {
                        this.setIcon(UIManager.getIcon("OptionPane.questionIcon"));
                    }

                    this.setFocusable(false);
                    this.setPreferredSize(new Dimension(32, 32));
                    this.setBackground(Constants.PRIMARY_COLOR);
                    this.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Constants.ACCENT_COLOR));
                }

                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    if (image != null) g2.drawImage(image, 1, 0, 30, 30, this);
                }
            }

            JButton home = new GameButton("home.png");
            home.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    GamePanel.gamePanel.setViewPortHome();


                }
            });


            JButton undo = new GameButton("undo.png");
            undo.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    GamePanel.stopTimer();
                    GameState newState = GamePanel.gameHistory.undo();
                    if (newState == null) return;
                    GamePanel.generation = newState.getGeneration();
                    GamePanel.currentBoard = newState.getBoardState();

                }
            });

            JButton redo = new GameButton("redo.png");
            redo.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    GamePanel.stopTimer();
                    GameState newState = GamePanel.gameHistory.redo();
                    if (newState == null) return;
                    GamePanel.generation = newState.getGeneration();
                    GamePanel.currentBoard = newState.getBoardState();
                }
            });

            class GameLabel extends JLabel {
                GameLabel(String text){
                    super(text);
                    this.setFocusable(false);
                    this.setBackground(Constants.BACKGROUND_COLOR);
                    this.setFont(new Font("Arial", Font.BOLD, 12));
                    this.setForeground(Constants.PRIMARY_COLOR);
                }
            }

            JPanel centerSubPanel = new JPanel(new FlowLayout());
            centerSubPanel.setBackground(Constants.BACKGROUND_COLOR);
            centerSubPanel.add(new GameLabel("Generations: 00000"));
            centerSubPanel.add(new GameLabel("Active Board: w:000 h:000"));
            centerSubPanel.add(new GameLabel("Viewport Dimensions: w:000 h:000"));
            centerSubPanel.add(new GameLabel("Cursor position: x:000 y:000"));
            centerSubPanel.add(new GameLabel("Live cells: 0000"));
            centerSubPanel.add(new GameLabel("FPS: 00"));
            centerSubPanel.add(new GameLabel("FPS: 000"));
            //labels:
            //Generations
            //Active Board Dimensions
            //Viewport Dimensions
            //Cursor position
            //Live cells
            //FPS
            //TPS









            JPanel eastSubPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            eastSubPanel.setBackground(Constants.BACKGROUND_COLOR);

            eastSubPanel.add(undo);
            eastSubPanel.add(redo);


            JPanel westSubPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            westSubPanel.setBackground(Constants.BACKGROUND_COLOR);

            westSubPanel.add(home);




            add(westSubPanel,BorderLayout.WEST);
            add(centerSubPanel,BorderLayout.CENTER);
            add(eastSubPanel,BorderLayout.EAST);


        }
    }


