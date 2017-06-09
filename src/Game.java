
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
    //create a crosshair
    Rectangle crosshairOut = new Rectangle(WIDTH / 2, HEIGHT / 2, 40, 40);
    Rectangle crosshairIn = new Rectangle(WIDTH / 2 + 19, HEIGHT / 2 + 19, 4, 4);
    //create a target
    Rectangle target = new Rectangle(WIDTH / 2, HEIGHT / 2, 80, 80);
    //font
    Font myFont = new Font("Arial", Font.BOLD, 75);
    Font restart = new Font("Arial", Font.BOLD, 30);
    //crosshair movement
    boolean upPressed;
    boolean downPressed;
    boolean leftPressed;
    boolean rightPressed;
    //target shooting
    boolean hit;
    //play again
    boolean playAgain;
    boolean end;
    // YOUR GAME VARIABLES WOULD GO HERE
    int score = 0;
    long flashDelay = 200;
    long nextShot = 0;
    int shotDelay = 1000;
    long nextTargetMove = 0;
    long targetDelay = 300;
    int targetMove = 0;
    int targetSpeed = 1;
    long playAgainTimer = 5000;
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
        g.setColor(Color.BLACK);

        //background
//        g.fillRect(0, 0, WIDTH, HEIGHT);

        //target
        g.setColor(Color.BLUE);
        g.fillRect(target.x, target.y, target.width, target.height);

        //crosshair
        g.setColor(Color.RED);

        g.drawRect(crosshairOut.x, crosshairOut.y, crosshairOut.width, crosshairOut.height);
        g.fillRect(crosshairIn.x, crosshairIn.y, crosshairIn.width, crosshairIn.height);

        //score #
        g.setFont(myFont);

        g.setColor(Color.RED);

        g.drawString("" + score, 700, 100);

        //play again message
        if (end == true) {
            System.out.println("test");
            g.setFont(restart);
            g.setColor(Color.RED);
            g.drawString("Press Y to play again.", 50, 50);
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

            //collision
            collision();

            if (upPressed) {
                crosshairOut.y = crosshairOut.y - 5;
                crosshairIn.y = crosshairIn.y - 5;
            }

            if (downPressed) {
                crosshairOut.y = crosshairOut.y + 5;
                crosshairIn.y = crosshairIn.y + 5;
            }

            if (leftPressed) {
                crosshairOut.x = crosshairOut.x - 5;
                crosshairIn.x = crosshairIn.x - 5;
            }

            if (rightPressed) {
                crosshairOut.x = crosshairOut.x + 5;
                crosshairIn.x = crosshairIn.x + 5;
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


            //point scoring condition
            if (startTime >= nextShot && crosshairIn.intersects(target) && hit == true) {
                score++;
                targetSpeed = targetSpeed + 1;
                nextShot = startTime + shotDelay;
                nextTargetMove = nextShot;
            } else if (!crosshairIn.intersects(target) && startTime >= nextShot && hit == true) {
                done = true;
                end = true;
               


            }


            while(done) {
                if (playAgain == true) {
//            end = false;
            
            score = 0;
            flashDelay = 200;
            nextShot = 0;
            shotDelay = 1000;

            nextTargetMove = 0;
            targetDelay = 300;
            targetMove = 0;
            targetSpeed = 1;

            crosshairOut.x = WIDTH / 2;
            crosshairOut.y = HEIGHT / 2;
            crosshairIn.x = WIDTH / 2 + 19;
            crosshairIn.y = HEIGHT / 2 + 19;

            target.x = WIDTH / 2;
            target.y = HEIGHT / 2;

            done = false;

        }
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
            //moving he crosshair
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

            //restart game
            if (e.getKeyCode() == KeyEvent.VK_Y) {
                playAgain = true;
                 
            }


            //shooting with spacebar
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                hit = true;
            }
        }

        // if a key has been released
        @Override
        public void keyReleased(KeyEvent e) {

            //moving the crosshair
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

            //shooting with spacebar
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                hit = false;
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
        if (crosshairOut.y <= 0) {
            crosshairOut.y = 0;
            crosshairIn.y = crosshairOut.height / 2;
        }

        if (crosshairOut.y + crosshairOut.height >= HEIGHT) {
            crosshairOut.y = HEIGHT - crosshairOut.height;
            crosshairIn.y = HEIGHT - crosshairOut.height / 2;
        }

        if (crosshairOut.x <= 0) {
            crosshairOut.x = 0;
            crosshairIn.x = crosshairOut.width / 2;
        }

        if (crosshairOut.x + crosshairOut.width >= WIDTH) {
            crosshairOut.x = WIDTH - crosshairOut.width;
            crosshairIn.x = WIDTH - crosshairOut.width / 2;
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

    
        
    
}
