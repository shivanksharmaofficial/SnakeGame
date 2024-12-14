 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
  
public class Board extends JPanel implements ActionListener {

    // Images for game assets (apple, snake body, and snake head)
    private Image apple;
    private Image dot;
    private Image head;

    // Constants for game configuration
    private final int ALL_DOTS = 900; // Maximum possible number of dots (snake length)
    private final int DOT_SIZE = 10; // Size of each snake dot
    private final int RANDOM_POSITION = 29; // Random position range for placing the apple

    // Position of the apple
    private int apple_x;
    private int apple_y;

    // Arrays to store the x and y coordinates of the snake segments
    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    // Booleans to track the direction of the snake's movement
    private boolean leftDirection = false;
    private boolean rightDirection = true; // Snake starts by moving right
    private boolean upDirection = false;
    private boolean downDirection = false;

    // Game status flag
    private boolean inGame = true;

    // Current length of the snake
    private int dots;
    private Timer timer;

    // Constructor to set up the game board
    Board() {
        addKeyListener(new TAdapter()); // Key listener for handling player input
        
        setBackground(Color.BLACK); // Set the game background to black
        setPreferredSize(new Dimension(300, 300)); // Set size of the game board
        setFocusable(true); // Enable focus for key events
        
        loadImages(); // Load images for game assets
        initGame(); // Initialize the game
    }

    // Load images for apple, snake body, and head
    public void loadImages() {
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("apple.png"));
        apple = i1.getImage();

        ImageIcon i2 = new ImageIcon(ClassLoader.getSystemResource("dot.png"));
        dot = i2.getImage();

        ImageIcon i3 = new ImageIcon(ClassLoader.getSystemResource("head.png"));
        head = i3.getImage();
    }

    // Initialize the game state, snake starting position and timer
    public void initGame() {
        dots = 3; // Initial length of the snake

        // Position the snake starting segments
        for (int i = 0; i < dots; i++) {
            y[i] = 50;
            x[i] = 50 - i * DOT_SIZE;
        }

        locateApple(); // Place the first apple

        timer = new Timer(140, this); // Create timer for game updates
        timer.start(); // Start the game timer
    }

    // Randomly place the apple on the board
    public void locateApple() {
        int r = (int)(Math.random() * RANDOM_POSITION);
        apple_x = r * DOT_SIZE;

        r = (int)(Math.random() * RANDOM_POSITION);
        apple_y = r * DOT_SIZE;
    }

    // Override paintComponent to draw the game on the board
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        draw(g); // Call the draw method to render the game
    }

    // Draw the apple, snake, and game-over message if game has ended
    public void draw(Graphics g) {
        if (inGame) {
            g.drawImage(apple, apple_x, apple_y, this); // Draw the apple

            // Draw each segment of the snake
            for (int i = 0; i < dots; i++) {
                if (i == 0) {
                    g.drawImage(head, x[i], y[i], this); // Draw the head
                } else {
                    g.drawImage(dot, x[i], y[i], this); // Draw body segments
                }
            }

            Toolkit.getDefaultToolkit().sync(); // Sync for smooth rendering
        } else {
            gameOver(g); // Display game-over screen
        }
    }

    // Display game-over message
    public void gameOver(Graphics g) {
        String msg = "Game Over!";
        Font font = new Font("SAN_SERIF", Font.BOLD, 14);
        FontMetrics metrices = getFontMetrics(font);

        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(msg, (300 - metrices.stringWidth(msg)) / 2, 300 / 2);
    }

    // Move the snake by updating the position of each segment
    public void move() {
        // Shift each part of the snake body to the position of the previous part
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        // Move the head in the current direction
        if (leftDirection) {
            x[0] = x[0] - DOT_SIZE;
        }
        if (rightDirection) {
            x[0] = x[0] + DOT_SIZE;
        }
        if (upDirection) {
            y[0] = y[0] - DOT_SIZE;
        }
        if (downDirection) {
            y[0] = y[0] + DOT_SIZE;
        }
    }

    // Check if the snake's head has reached the apple
    public void checkApple() {
        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            dots++;     
            locateApple();  
        }
    }

    // Check if the snake collides with itself or the board edges
    public void checkCollision() {
        // Check if the head collides with the body
        for (int i = dots; i > 0; i--) {
            if ((i > 4) && (x[0] == x[i]) && (y[0] == y[i])) {
                inGame = false; // End game if collision detected
            }
        }

        // Check for collisions with the board edges
        if (y[0] >= 300) {
            inGame = false;
        }
        if (x[0] >= 300) {
            inGame = false;
        }
        if (y[0] < 0) {
            inGame = false;
        }
        if (x[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop(); // Stop the game timer if the game is over
        }
    }

    // Main game loop, called by the timer to update the game state
    public void actionPerformed(ActionEvent ae) {
        if (inGame) {
            checkApple(); // Check if apple is eaten
            checkCollision(); // Check for collisions
            move(); // Move the snake
        }

        repaint(); // Repaint the board with updated game state
    }

    // KeyAdapter to handle player input for controlling the snake
    public class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            // Set direction booleans based on arrow keys pressed
            if (key == KeyEvent.VK_LEFT && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if (key == KeyEvent.VK_RIGHT && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if (key == KeyEvent.VK_UP && (!downDirection)) {
                upDirection = true;
                leftDirection = false;
                rightDirection = false;
            }

            if (key == KeyEvent.VK_DOWN && (!upDirection)) {
                downDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
        }
    }
}
