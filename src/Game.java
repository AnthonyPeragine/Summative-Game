
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author ${user}
 */
public class Game extends JComponent {

    // Height and Width of our game
    static final int WIDTH = 800;
    static final int HEIGHT = 600;
    //Title of the window
    String title = "My Game";
    // sets the framerate and delay for our game
    // you just need to select an approproate framerate
    long desiredFPS = 60;
    long desiredTime = (1000) / desiredFPS;
    //create 2 crosshairs
    Rectangle crosshairOut = new Rectangle(WIDTH / 2, HEIGHT / 2, 40, 40);
    Rectangle crosshair2Out = new Rectangle(WIDTH / 2 - 40, HEIGHT / 2, 40, 40);
    Rectangle crosshairIn = new Rectangle(WIDTH / 2 + 19, HEIGHT / 2 + 19, 4, 4);
    //create a target
    Rectangle target = new Rectangle(WIDTH / 2, HEIGHT / 2, 130, 130);
    //font
    Font myFont = new Font("Arial", Font.BOLD, 75);
    Font restart = new Font("Arial", Font.BOLD, 30);
    //crosshair 1 movement
    boolean upPressed;
    boolean downPressed;
    boolean leftPressed;
    boolean rightPressed;
    //crosshair 2 movement
    boolean wPressed;
    boolean sPressed;
    boolean aPressed;
    boolean dPressed;
    //target shooting
    boolean hit1;
    boolean hit2;
    //play again
    boolean playAgain;
    boolean end;
    // YOUR GAME VARIABLES WOULD GO HERE
    int score1 = 0;
    int score2 = 0;
    long flashDelay = 200;
    long nextShot1 = 0;
    long nextShot2 = 0;
    int shotDelay = 1000;
    long nextTargetMove = 0;
    long targetDelay = 300;
    int targetMove = 0;
    int targetSpeed = 1;
    long playAgainTimer = 5000;
    int hitBoxCorrection = crosshairOut.x - 10;
    //images
    BufferedImage targetImage = loadImage("target.png");
    BufferedImage crosshairImage = loadImage("Crosshair.png");
    BufferedImage crosshair2Image = loadImage("Crosshair.png");
    BufferedImage shootingRange = loadImage("range.jpg");

    // GAME VARIABLES END HERE   
    // Constructor to create the Frame and place the panel in
    // You will learn more about this in Grade 12 :)
    public Game() {
        // creates a windows to show my game
        JFrame frame = new JFrame(title);

        // sets the size of my game
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // adds the game to the window
        frame.add(this);

        // sets some options and size of the window automatically
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        // shows the window to the user
        frame.setVisible(true);

        // add listeners for keyboard and mouse
        frame.addKeyListener(new Keyboard());
        Mouse m = new Mouse();

        this.addMouseMotionListener(m);
        this.addMouseWheelListener(m);
        this.addMouseListener(m);
    }

    // drawing of the game happens in here
    // we use the Graphics object, g, to perform the drawing
    // NOTE: This is already double buffered!(helps with framerate/speed)
    @Override
    public void paintComponent(Graphics g) {
        // always clear the screen first!
        g.clearRect(0, 0, WIDTH, HEIGHT);

        // GAME DRAWING GOES HERE
        //g.setColor(Color.GRAY);


        //background
        g.drawImage(shootingRange, 0, 0, WIDTH, HEIGHT, null);
        //g.fillRect(0, 0, WIDTH, HEIGHT);

        //target
        //g.setColor(Color.BLUE);
        //g.fillRect(target.x - 1, target.y - 1, target.width + 1, target.height + 1);
        g.drawImage(targetImage, target.x, target.y, target.width, target.height, null);

        //crosshairs


        g.setColor(Color.RED);
        g.fillOval(crosshairOut.x, crosshairOut.y, crosshairOut.width, crosshairOut.height);
        g.drawImage(crosshairImage, crosshairOut.x - 20, crosshairOut.y - 20, 80, 80, null);

        g.setColor(Color.BLUE);
        g.fillOval(crosshair2Out.x, crosshair2Out.y, crosshair2Out.width, crosshair2Out.height);
        g.drawImage(crosshair2Image, crosshair2Out.x - 20, crosshair2Out.y - 20, 80, 80, null);

        //g.drawRect(crosshairOut.x, crosshairOut.y, crosshairOut.width, crosshairOut.height);
        //g.fillRect(crosshairIn.x, crosshairIn.y, crosshairIn.width, crosshairIn.height);

        //score1 #
        g.setFont(myFont);

        g.setColor(Color.RED);

        g.drawString("" + score1, 700, 100);

        //score2 #
        g.setFont(myFont);

        g.setColor(Color.BLUE);

        g.drawString("" + score2, 100, 100);


        //winner message
        if (score1 == 10) {
            g.setColor(Color.RED);
            g.drawString("Red Player Wins", 130, 500);
        }
        if (score2 == 10) {
            g.setColor(Color.BLUE);
            g.drawString("Blue Player Wins", 130, 500);
        }
        //play again message

        if (end == true) {
            g.setFont(restart);
            g.setColor(Color.BLACK);
            g.drawString("Press Y to play again.", 250, 50);
        }




        // GAME DRAWING ENDS HERE
    }

    // This method is used to do any pre-setup you might need to do
    // This is run before the game loop begins!
    public void preSetup() {
        // Any of your pre setup before the loop starts should go here
    }

    // The main game loop
    // In here is where all the logic for my game will go
    public void run() {
        // Used to keep track of time used to draw and update the game
        // This is used to limit the framerate later on
        long startTime;
        long deltaTime;

        preSetup();

        // the main game loop section
        // game will end if you set done = false;
        boolean done = false;
        while (!done) {
            // determines when we started so we can keep a framerate
            startTime = System.currentTimeMillis();

            // all your game rules and move is done in here
            // GAME LOGIC STARTS HERE 

            //game end condition
            if (score1 == 10 || score2 == 10) {
                done = true;
            }

            //after game ends, chance to restart
            while (done) {
                if (playAgain == true) {
                    end = false;

                    score1 = 0;
                    score2 = 0;
                    flashDelay = 200;
                    nextShot1 = 0;
                    nextShot2 = 0;
                    shotDelay = 1000;

                    nextTargetMove = 0;
                    targetDelay = 300;
                    targetMove = 0;
                    targetSpeed = 1;

                    crosshairOut.x = WIDTH / 2;
                    crosshairOut.y = HEIGHT / 2;
                    crosshair2Out.x = WIDTH / 2 - 40;
                    crosshair2Out.y = HEIGHT / 2;

                    target.x = WIDTH / 2;
                    target.y = HEIGHT / 2;

                    done = false;

                }
            }

            //collision
            collision();

            if (upPressed) {
                crosshairOut.y = crosshairOut.y - 8;
            }

            if (downPressed) {
                crosshairOut.y = crosshairOut.y + 8;
            }

            if (leftPressed) {
                crosshairOut.x = crosshairOut.x - 8;
            }

            if (rightPressed) {
                crosshairOut.x = crosshairOut.x + 8;
            }

            if (wPressed) {
                crosshair2Out.y = crosshair2Out.y - 8;
            }

            if (sPressed) {
                crosshair2Out.y = crosshair2Out.y + 8;
            }

            if (aPressed) {
                crosshair2Out.x = crosshair2Out.x - 8;
            }

            if (dPressed) {
                crosshair2Out.x = crosshair2Out.x + 8;
            }

            //target random movement
            if (startTime >= nextTargetMove) {
                targetMove = (int) (Math.random() * (8 - 1 + 1)) + 1;
                nextTargetMove = startTime + targetDelay;
            }
            if (targetMove == 1) {

                target.x = target.x + targetSpeed;

            }

            if (targetMove == 2) {

                target.x = target.x - targetSpeed;

            }

            if (targetMove == 3) {

                target.y = target.y + targetSpeed;

            }

            if (targetMove == 4) {

                target.y = target.y - targetSpeed;

            }

            if (targetMove == 5) {

                target.x = target.x + targetSpeed;
                target.y = target.y + targetSpeed;

            }

            if (targetMove == 6) {

                target.x = target.x - targetSpeed;
                target.y = target.y - targetSpeed;

            }

            if (targetMove == 7) {

                target.x = target.x + targetSpeed;
                target.y = target.y - targetSpeed;

            }

            if (targetMove == 8) {

                target.x = target.x - targetSpeed;
                target.y = target.y + targetSpeed;

            }


            //point scoring condition for player 1

            if (startTime >= nextShot1 && crosshairOut.intersects(target) && hit1 == true) {
                score1++;
                targetSpeed = targetSpeed + 1;
                nextShot1 = startTime + shotDelay;
            } else if (!crosshairOut.intersects(target) && hit1 == true) {
                nextShot1 = startTime + shotDelay;
            }

            //point scoring condition for player 2
            if (startTime >= nextShot2 && crosshair2Out.intersects(target) && hit2 == true) {
                score2++;
                targetSpeed = targetSpeed + 1;
                nextShot2 = startTime + shotDelay;
            } else if (!crosshair2Out.intersects(target) && hit2 == true) {
                nextShot2 = startTime + shotDelay;
            }



            if (score1 == 10 || score2 == 10) {
                end = true;
            }

            // GAME LOGIC ENDS HERE 
            // update the drawing (calls paintComponent)
            {
                repaint();
            }

            // SLOWS DOWN THE GAME BASED ON THE FRAMERATE ABOVE
            // USING SOME SIMPLE MATH
            deltaTime = System.currentTimeMillis() - startTime;
            try {
                if (deltaTime > desiredTime) {
                    //took too much time, don't wait
                    Thread.sleep(1);
                } else {
                    // sleep to make up the extra time
                    Thread.sleep(desiredTime - deltaTime);
                }
            } catch (Exception e) {
            };
        }
    }

    // Used to implement any of the Mouse Actions
    private class Mouse extends MouseAdapter {
        // if a mouse button has been pressed down

        @Override
        public void mousePressed(MouseEvent e) {
        }

        // if a mouse button has been released
        @Override
        public void mouseReleased(MouseEvent e) {
        }

        // if the scroll wheel has been moved
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
        }

        // if the mouse has moved positions
        @Override
        public void mouseMoved(MouseEvent e) {
        }
    }

    // Used to implements any of the Keyboard Actions
    private class Keyboard extends KeyAdapter {
        // if a key has been pressed down

        @Override
        public void keyPressed(KeyEvent e) {
            //moving crosshair 1
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                upPressed = true;
            }

            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                downPressed = true;
            }

            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftPressed = true;
            }

            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightPressed = true;
            }

            //moving crosshair 2
            if (e.getKeyCode() == KeyEvent.VK_W) {
                wPressed = true;
            }

            if (e.getKeyCode() == KeyEvent.VK_S) {
                sPressed = true;
            }

            if (e.getKeyCode() == KeyEvent.VK_A) {
                aPressed = true;
            }

            if (e.getKeyCode() == KeyEvent.VK_D) {
                dPressed = true;
            }

            //restart game
            if (e.getKeyCode() == KeyEvent.VK_Y) {
                playAgain = true;

            }


            //shooting for both players
            if (e.getKeyCode() == KeyEvent.VK_COMMA) {
                hit1 = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                hit2 = true;
            }
        }

        // if a key has been released
        @Override
        public void keyReleased(KeyEvent e) {

            //moving crosshair 1
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                upPressed = false;
            }

            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                downPressed = false;
            }

            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftPressed = false;
            }

            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightPressed = false;
            }

            //moving crosshair 2
            if (e.getKeyCode() == KeyEvent.VK_W) {
                wPressed = false;
            }

            if (e.getKeyCode() == KeyEvent.VK_S) {
                sPressed = false;
            }

            if (e.getKeyCode() == KeyEvent.VK_A) {
                aPressed = false;
            }

            if (e.getKeyCode() == KeyEvent.VK_D) {
                dPressed = false;
            }

            //shooting for both players
            if (e.getKeyCode() == KeyEvent.VK_COMMA) {
                hit1 = false;
            }

            if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                hit2 = false;
            }

            //restart game
            if (e.getKeyCode() == KeyEvent.VK_Y) {
                playAgain = false;
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // creates an instance of my game
        Game game = new Game();

        // starts the game loop
        game.run();
    }

    public void collision() {
        //crosshair 1 movement collision at border of screen
        if (crosshairOut.y <= 0) {
            crosshairOut.y = 0;
        }

        if (crosshairOut.y + crosshairOut.height >= HEIGHT) {
            crosshairOut.y = HEIGHT - crosshairOut.height;
        }

        if (crosshairOut.x <= 0) {
            crosshairOut.x = 0;
        }

        if (crosshairOut.x + crosshairOut.width >= WIDTH) {
            crosshairOut.x = WIDTH - crosshairOut.width;
        }

        //crosshair 2 movement collision at border of screen
        if (crosshair2Out.y <= 0) {
            crosshair2Out.y = 0;
        }

        if (crosshair2Out.y + crosshair2Out.height >= HEIGHT) {
            crosshair2Out.y = HEIGHT - crosshair2Out.height;
        }

        if (crosshair2Out.x <= 0) {
            crosshair2Out.x = 0;
        }

        if (crosshair2Out.x + crosshair2Out.width >= WIDTH) {
            crosshair2Out.x = WIDTH - crosshair2Out.width;
        }

        if (target.x <= 0) {
            target.x = 0;
        }

        if (target.x + target.width >= WIDTH) {
            target.x = WIDTH - target.width;
        }

        if (target.y <= 0) {
            target.y = 0;
        }

        if (target.y + target.height >= HEIGHT) {
            target.y = HEIGHT - target.height;
        }

    }

    // A method used to load in an image
    // The filname is used to pass in the EXACT full name of the image from the src folder
    // i.e.  images/picture.png
    public BufferedImage loadImage(String filename) {

        BufferedImage img = null;

        try {
            // use ImageIO to load in an Image
            // ClassLoader is used to go into a folder in the directory and grab the file
            img = ImageIO.read(ClassLoader.getSystemResourceAsStream(filename));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        return img;
    }
}
