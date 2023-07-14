import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class MainGUI extends JFrame implements KeyListener {

    JLabel score = new JLabel("0  :  0");
    JPanel scoreBoard = new JPanel();
    JPanel playerOne = new JPanel();
    JPanel playerTwo = new JPanel();
    JPanel ball = new JPanel() {
        public void paintComponent(Graphics g) {
            super.setOpaque(false);
            super.paintComponent(g);
            g.fillOval(0, 0, 30, 30);
        }
    };
    int frameWidth = 700;
    int frameHeight = 500;
    int scoreRight;
    int scoreLeft;
    boolean restart = false;
    boolean isRunning = false;
    Timer time;
    int[] angle;
    int angleY = 4;
    Random random = new Random();
    boolean gameCheck = true;
    int playerTurn = 1;


    public MainGUI() {
        super();
        this.setSize(frameWidth, frameHeight);

        Container cp = this.getContentPane();
        cp.setLayout(null);

        scoreBoard.setBounds(frameWidth / 2 - 50, frameHeight - 100, 100, 35);
        scoreBoard.setBorder(BorderFactory.createLineBorder(Color.black));
        scoreBoard.add(score);
        cp.add(scoreBoard);

        playerOne.setBounds(10, 10, 25, 150);
        playerOne.setBackground(Color.BLACK);
        cp.add(playerOne);

        playerTwo.setBounds(frameWidth - 50, 10, 25, 150);
        playerTwo.setBackground(Color.BLACK);
        cp.add(playerTwo);

        ball.repaint();
        ball.setBounds(frameWidth / 2, 10, 30, 30);
        cp.add(ball);

        while (angleY == 0 || angleY == 1 || angleY == 2 || angleY == 3 || angleY == 9) {
            angleY = random.nextInt(10);
        }
        angle = new int[]{10, 12 / angleY};
        time = new Timer(16, e -> timerAction());
        addKeyListener(this);
        System.out.println(angle[1]);
        System.out.println(angleY);

        this.setVisible(true);
    }

    public static void main(String[] args) {
        new MainGUI();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public int yLeft() {
        return playerOne.getY();
    }

    public int xLeft() {
        return playerOne.getX();
    }

    public int yRight() {
        return playerTwo.getY();
    }

    public int xRight() {
        return playerTwo.getX();
    }

    public int yBall() {
        return ball.getY();
    }

    public int xBall() {
        return ball.getX();
    }


    @Override
    public void keyPressed(KeyEvent e) {
        if (!isRunning) {
            startGame();
            isRunning = true;
        }
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                if (yLeft() > 0) {
                    playerOne.setLocation(10, yLeft() - 30);
                }
                break;
            case KeyEvent.VK_S:
                if (yLeft() < frameHeight - 180) {
                    playerOne.setLocation(10, yLeft() + 30);
                }
                break;
            case KeyEvent.VK_UP:
                if (yRight() > 0) {
                    playerTwo.setLocation(frameWidth - 50, yRight() - 30);
                }
                break;
            case KeyEvent.VK_DOWN:
                if (yRight() < frameHeight - 180) {
                    playerTwo.setLocation(frameWidth - 50, yRight() + 30);
                }
                break;
            case KeyEvent.VK_SPACE:
                if (restart) {
                    restart = false;
                    do {
                        angleY = random.nextInt(10);
                    } while (angleY == 0 || angleY == 1 || angleY == 2 || angleY == 3 || angleY == 9);


                    angle = new int[]{10, 12 / angleY};
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void startGame() {
        time.start();
    }

    public MainGUI.direction wall() {
        if (xBall() < 40) {
            return direction.left;
        }
        if (xBall() > frameWidth - 85) {
            return direction.right;
        }
        if (yBall() < 1) {
            return direction.up;
        }
        if (yBall() > 430) {
            return direction.down;
        }
        return null;
    }

    public boolean check(int player) {
        switch (player) {
            case 1:
                if (ball.getX() <= xLeft() + 30) {
                    gameCheck = yBall() >= yLeft() - 15 && yBall() <= yLeft() + 150;
                } else {
                    gameCheck = true;
                }
                break;
            case 2:
                if (ball.getX() >= xRight() - 35) {
                    gameCheck = yBall() >= yRight() - 15 && yBall() <= yRight() + 150;
                } else {
                    gameCheck = true;
                }
                break;
        }
        return gameCheck;
    }

    public void timerAction() {
        if (isRunning && !restart) {
            int random1 = random.nextInt(3);
            if (wall() != null) {
                switch (wall()) {
                    case left -> {
                        angle[0] = -angle[0];
                        playerTurn = 1;
                        if (random1 == 1) {
                            angle[1] = -angle[1];
                        }
                    }
                    case right -> {
                        angle[0] = -angle[0];
                        playerTurn = 2;
                    }
                    case up, down -> angle[1] = -angle[1];
                }
            }

            if (!check(playerTurn)) {
                if (playerTurn == 1) {
                    scoreRight = scoreRight + 1;
                } else {
                    scoreLeft = scoreLeft + 1;
                }
                score.setText(scoreLeft + "  :  " + scoreRight);
                ball.setLocation(frameWidth / 2, 10);
                restart = true;
            } else {
                ball.setLocation(xBall() + angle[0], yBall() + angle[1]);
            }

        }
    }

    enum direction {left, right, up, down}
}
