import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;


public class SnakeView extends JPanel implements ActionListener, KeyListener {


    private static class Tile {
        int x;
        int y;
        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    int boardWidth;
    int boardHeight;
    int tileSize = 25;
    //SNAKE
    Tile snakeHead;
    ArrayList<Tile> snakeBody;
    //FOOD
    Tile food;
    Random random;
    // game logic
    public static javax.swing.Timer gameLoop;
    int velocityX;
    int velocityY;
    public static boolean gameOver = false;



    public SnakeView(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;


        gameLoop = new Timer(100, this);
        gameLoop.start();
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {

        //FOOD
        g.setColor(Color.red);
        g.fillOval(food.x * tileSize, food.y * tileSize, tileSize, tileSize);
        //SNAKE HEAD
        g.setColor(Color.green);
        g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);

        //SNAKE BODY
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
        }

        //Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        //Center the game over text
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth("RETRY? Y/N");
        int textHeight = fm.getHeight();
        int x = (600 - textWidth) / 2;
        int y = (600 - textHeight) / 2 + fm.getAscent();
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + snakeBody.size(), tileSize - 16, tileSize);
            g.drawString("RETRY? Y/N", x,y);
        }
        else {
            g.drawString("Score: " + snakeBody.size(), tileSize - 16, tileSize);
        }
    }

    public void restart() {

        // Reset the snake's position
        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        // Reset the food's position
        placeFood();

        // Reset the game state
        gameOver = false;

        // Reset the velocities
        velocityX = 0;
        velocityY = 0;

        // Restart the game loop
        gameLoop.start();

    }

    public void placeFood() {
        //Food generation in position that isn't occupied by snake
        boolean isOccupied;
        do {
            food.x = random.nextInt(boardWidth/tileSize);
            food.y = random.nextInt(boardHeight/tileSize);

            isOccupied = false;
            for (Tile tile : snakeBody) {
                if (food.x == tile.x && food.y == tile.y) {
                    isOccupied = true;
                    break;
                }
            }
        } while (isOccupied);
    }
    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }
    public void move() {
        //eat food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }
        //Snake body
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else {
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        //Snake head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //GAME OVER
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }
        if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize >= boardWidth ||
            snakeHead.y * tileSize < 0 || snakeHead.y * tileSize >= boardHeight) {
            gameOver = true;
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_W -> {
                if (velocityY != 1) {
                    velocityY = -1;
                    velocityX = 0;
                }
            }
            case KeyEvent.VK_S -> {
                if (velocityY != -1) {
                    velocityY = 1;
                    velocityX = 0;
                }
            }
            case KeyEvent.VK_A -> {
                if (velocityX != 1) {
                    velocityY = 0;
                    velocityX = -1;
                }
            }
            case KeyEvent.VK_D -> {
                if (velocityX != -1) {
                    velocityY = 0;
                    velocityX = 1;
                }
            }
            case KeyEvent.VK_Y -> {
                if (gameOver)
                    restart();
            }
            case KeyEvent.VK_N -> {
                if(gameOver)
                    System.exit(0);
            }
            case KeyEvent.VK_ESCAPE -> {
                gameLoop.stop();
                main.pauseMenu.setVisible(true);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
