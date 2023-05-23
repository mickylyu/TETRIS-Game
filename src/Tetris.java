import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener; //to get the input from the keyboard to do something (output action within the game)
import java.util.ArrayList;
import java.util.Collections; //for insert, sort, delete, etc...

// java.swing.JFrame and java.swing.JPanel (for user interface)
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Tetris extends JPanel {
    //we need to create a 3D ArrayList so ==
    // [creates the shape][four rotations for player controls][store the coordinates of a certain shape]
    // structure: Color(color of the import)[] Colors (name of the series)
    // coordinates of each shape implemented within the game
    private final Point[][][] Shapes = {
// I-Piece
            {
                    { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
                    { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) },
                    { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
                    { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) }
            },
// J-Piece
            {
                    { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0) },
                    { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
                    { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2) },
                    { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 0) }
            },
// L-Piece
            {
                    { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
                    { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2) },
                    { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 0) },
                    { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0) }
            },
// O-Piece
            {
                    { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
                    { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
                    { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
                    { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) }
            },
// S-Piece
            {
                    { new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
                    { new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
                    { new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
                    { new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) }
            },
// T-Piece
            {
                    { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
                    { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
                    { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
                    { new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) }
            },
// Z-Piece
            {
                    { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
                    { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) },
                    { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
                    { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) }
            }
    };
    private final Color[] Colors = {
            Color.green, Color.blue, Color.orange, Color.yellow, Color.magenta, Color.pink, Color.red};
    private Point origin; // the point of the shape we determine when player changes movements
    private int current; // store the current puzzle that is going to fall
    private int rotation; // storing the rotations
    private ArrayList<Integer> nextPuzzle = new ArrayList<Integer>();
    // creating an arraylist to store the current pieces that touches the floor of the map

    private int score;
    // storing the score of the player, creating a scoreboard to keep track of the score
    private Color[][] background;

    // this void method initialises the background and spawning the first block/puzzle
    private void init() {
        background = new Color[12][24];
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 23; j++) {
                if (i == 0 || i == 11 || j == 22) {
                    background[i][j] = Color.lightGray;
                } else {
                    background[i][j] = Color.black;
                }
            }
        }
        spawn(); // after background is created the blocks start spawning
    }

    // this method is deliberately to spawn continuous pieces from the game, will be infinite until game is over
    public void spawn() {
        // 5 cuz it's the middle part of 12 block
        origin = new Point(5, -1);
        rotation = 0;
        if (nextPuzzle.isEmpty()) {
            Collections.addAll(nextPuzzle, 0, 1, 2, 3, 4, 5, 6);
            // collection helps determine the types and the number of blocks we want to store
            Collections.shuffle(nextPuzzle);
        }
        current = nextPuzzle.get(0);
        nextPuzzle.remove(0);
    }

    //this method checks if the puzzle has collided with another puzzle which already existed on the ground
    private boolean isCollide(int x, int y, int rotation) {
        for (Point p : Shapes[current][rotation]) { // to check the shape of the puzzles
            if (background[p.x + x][p.y + y] != Color.BLACK) {
                // to make sure that the blocks do not collide with the color of the side walls
                return true;
            }
        }
        return false;
    }

    // this method allows the users to rotate the puzzle
    public void rotate(int r) {
        int newRotation = (rotation + r) % 4;
        // default value of rotation is 0, click up change 0 to 1, click down change 1 to 0
        if (newRotation < 0) {
            newRotation = 3;
            // result divided by 4 then take remainder so its equivalent to 0,1,2,3 index of rotations
        }
        if (!isCollide(origin.x, origin.y, newRotation)) {
            rotation = newRotation;
            // if it does not collide with the wall, then allow rotate, if not then rotate is disabled
        }
        repaint(); //refill the color after background has been revealed
    }

    // this method allows the user to move the puzzle left and right as it falls from spawn spot
    public void move(int m) {
        if (!isCollide(origin.x + m, origin.y, rotation)) {
            //it's just checking the x coordinates cuz only left and right
            origin.x += m;
        }
        repaint(); //make it black again, since default background color is BLACK
    }
    // Drops the piece one line or fixes it to the well if it can't drop
    public void drop() {
        if (!isCollide(origin.x, origin.y + 1, rotation)) {
            origin.y += 1;
        } else {
            toWall();
        }
        repaint();
    }

    //this method would enable puzzle to drop down automatically
    public void toWall() {
        for (Point p : Shapes[current][rotation]) {
            // don't need a parameter cuz there's only one direction not entirely y up needed
            background[origin.x + p.x][origin.y + p.y] = Colors[current];
        }
        clearEntireRow();
        spawn();
    }

    // to check if the entire row is filled, if yes call clearEntireRow method to increase scoreboard
    public void clearRow(int row) {
        for (int i = row-1; i > 0; i--) {
            for (int j = 1; j < 11; j++) {
                // only 1-11 cuz that's where th  blocks are allowed to be allocated,
                // 0 and 12 is the side walls
                background[j][i+1] = background[j][i]; // when bottom row is eliminated, upper row falls down
            }
        }
    }

    //this is when player reaches a maximum row of blocks, then it clears the space automatically
    public void clearEntireRow() {
        boolean gap;
        int numClears = 0;
        for (int i = 21; i > 0; i--) { // the height is 24-3 cuz first 3 is the space for block spawn
            gap = false;
            for (int j = 1; j < 11; j++) {
                if (background[j][i] == Color.BLACK) { // if white then skip and no clear
                    gap = true;
                    break;
                }
            }
            if (!gap) {
                clearRow(i);
                i += 1; // i = y coordinates, check one line then move on to the next one
                numClears += 1; // this is for the scoreboard
            }
        }
        switch (numClears) { // enhance switch case has been suggested by intellj making the lines of code shorter
            case 1 -> score += 50; // clearing one line of tetris would get 50 points as score
            case 2 -> score += 150;
            case 3 -> score += 350;
            case 4 -> score += 650; // most rewarded possibility is 4 lines in a row earning 650 points
        }
    }

    // drawing the puzzle out by the corresponding coordinates provided
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Colors[current]);
        for (Point p : Shapes[current][rotation]) {
            g.fillRect((p.x + origin.x) * 26,
                    (p.y + origin.y) * 26,
                    25, 25);
        }

    }

    public void gameOver(){

    }
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        // this is for filling in the color for the background
        g.fillRect(0, 0, 26*12, 26*23);
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 23; j++) {
                g.setColor(background[i][j]);
                g.fillRect(26*i, 26*j, 25, 25);
            }
        }
        // this is for the color and size of the scoreboard display
        g.setColor(Color.WHITE);
        g.drawString("" + score, 19*12, 25);
        // this is for creating the puzzle that's dropping at the moment
    }

    // the main function here would keep looping the game infinitely until game over
    public static void main(String[] args) {
        JFrame f = new JFrame("Tetris");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // this ensures that the window is closed completely
        f.setPreferredSize(new Dimension(12*26+10, 26*23+25));
        // this ensures that the window created pops out for the visuals to be displayed for the players
        Tetris game = new Tetris(); // like any other programs in the main they have to call the class
        game.init();
        f.add(game);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);


        // Keyboard controls
        f.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        game.rotate(-1);
                        break;
                    case KeyEvent.VK_DOWN:
                        game.rotate(+1);
                        break;
                    case KeyEvent.VK_LEFT:
                        game.move(-1);
                        break;
                    case KeyEvent.VK_RIGHT:
                        game.move(+1);
                        break;
                    case KeyEvent.VK_SPACE:
                        game.drop();
                        game.score += 1;
                        break;
                }
            }
            public void keyReleased(KeyEvent e) {
            } // connecting controls on keyboard to rotation commands
        });
        // make the falling piece drop every second
        new Thread() {
            @Override public void run() {
                while (true) {
                    try {

                        Thread.sleep(1000);
                        game.drop();

                    } catch ( InterruptedException e ) {}
                }
            }
        }.start();

    }
}