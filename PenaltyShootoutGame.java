import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class PenaltyShootoutGame extends JFrame {
    private JLabel ballLabel, goalkeeperLabel, scoreLabel, messageLabel;
    private int score = 0;
    private int shots = 5;  // Number of shots the player gets
    private Timer goalkeeperTimer;
    private Random random = new Random();
    private Image backgroundImage;


    public PenaltyShootoutGame() {
        //background image
        backgroundImage = new ImageIcon("Assets/goalpost3.png").getImage();

        // game window
        setTitle("Penalty Shootout Game");
        setSize(1000, 750);  // Set size to match your game panel
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Custom JPanel with a background image
        GamePanel gamePanel = new GamePanel();
        gamePanel.setBounds(0, 0, 1000, 800);
        gamePanel.setLayout(null);

        // Ball
        ballLabel = new JLabel(new ImageIcon("Assets/football.png"));
        ballLabel.setBounds(500, 650, 100, 100);
        gamePanel.add(ballLabel);

        // Goalkeeper
        goalkeeperLabel = new JLabel(new ImageIcon("Assets/goalkeeper.png"));
        goalkeeperLabel.setBounds(285, 220, 400, 400);
        gamePanel.add(goalkeeperLabel);

        // Score label
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 30));
        scoreLabel.setBounds(450, 80, 150, 30);
        gamePanel.add(scoreLabel);

        // Message label
        messageLabel = new JLabel("Take a shot!");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        messageLabel.setBounds(400, 30, 400, 30);
        gamePanel.add(messageLabel);

        //mouse listener to take a shot
        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                //goal will be counted only if mouse clicked within the following axes
                if(e.getY() <= 600 && e.getY() >= 100 && e.getX() >= 0 && e.getX() <= 1000) {
                    if (shots > 0) {
                        moveGoalkeeper();
                        moveBall(e.getX(), e.getY());
                        takeShot(e.getX(), e.getY());
                        goalkeeperTimer = new Timer(3000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                ballLabel.setBounds(500, 650, 100, 100);
                                goalkeeperLabel.setBounds(285, 220, 400, 400);
                            }
                        });
                        goalkeeperTimer.start();
                    } else {
                        messageLabel.setText("Game Over!");
                    }
                }else {
                    messageLabel.setText("click within the goalpost to score!!!");
                }
            }

        });


        // Timer to move the goalkeeper randomly
//        goalkeeperTimer = new Timer(1000, new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                moveGoalkeeper();
//            }
//        });
//        goalkeeperTimer.start();

        // Add the game panel with background image
        add(gamePanel);
    }


    // Method to simulate taking a shot
    private void takeShot(int x, int y) {
        shots--;

        // Check if the shot is blocked by the goalkeeper
        if (x >= goalkeeperLabel.getX() && x <= (goalkeeperLabel.getX() + goalkeeperLabel.getWidth()) &&
                y >= goalkeeperLabel.getY() && y <= (goalkeeperLabel.getY() + goalkeeperLabel.getHeight())) {
            messageLabel.setText("Shot Blocked! Shots left: " + shots);
        } else {
            score++;
            scoreLabel.setText("Score: " + score);
            messageLabel.setText("Goal! Shots left: " + shots);
        }

        // End the game after all shots are taken
        if (shots == 0) {
            messageLabel.setText("Game Over!");
            goalkeeperTimer.stop();
        }
    }

    private void moveBall(int x, int y) {
        // Adjust the ball's position, center it at the click
        int ballSize = ballLabel.getWidth();
        int newX = x - ballSize / 2;  // Center the ball horizontally at click
        int newY = y - ballSize / 2;  // Center the ball vertically at click

        // Keep the ball within the game panel boundaries
        newX = Math.max(0, Math.min(newX, getWidth() - ballSize));  // Horizontal bounds
        newY = Math.max(0, Math.min(newY, getHeight() - ballSize));  // Vertical bounds

        // Set the new position for the ball
        ballLabel.setLocation(newX, newY);
        ballLabel.repaint();  // Repaint to reflect the new position
    }


    // Method to move the goalkeeper randomly across the goal
    private void moveGoalkeeper() {
        int x = random.nextInt(400);  // Adjust the width of the goal
        goalkeeperLabel.setLocation(x + 100, goalkeeperLabel.getY());  // Adjust based on goal size
    }



    // Custom JPanel to paint the background image
    class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Draw the background image
            g.drawImage(backgroundImage, 0, 120, 1000, 500, this);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PenaltyShootoutGame game = new PenaltyShootoutGame();
            game.setVisible(true);
        });
    }
}
