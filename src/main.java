import javax.swing.*;
import java.awt.*;

public class main {
    public static JWindow pauseMenu;

    public static void main(String[] args) {


        int boardWidth = 600;
        int boardHeight = boardWidth;

        //MAIN WINDOW
        JFrame window = new JFrame("Snake");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setSize(600,600);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        SnakeView snake = new SnakeView(boardWidth, boardHeight);
        window.add(snake);
        window.pack();
        snake.requestFocus();

        //PAUSE MENU
        pauseMenu = new JWindow(window);
        pauseMenu.setSize(300, 300);
        pauseMenu.setLocationRelativeTo(window);

        JLabel label = new JLabel("Game Paused", SwingConstants.CENTER);
        pauseMenu.add(label);

        JButton resumeButton = new JButton("Resume");
        resumeButton.addActionListener(e -> {
            pauseMenu.setVisible(false);
            if (!SnakeView.gameOver) {
                SnakeView.gameLoop.start();
            }
        });
        pauseMenu.add(resumeButton, BorderLayout.SOUTH);

    }
}
