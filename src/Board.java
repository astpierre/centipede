import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.Vector;
import java.util.Iterator;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.*;
import java.net.URL;

public class Board extends JPanel implements Runnable, Constraints {
    private Dimension d;
    private Player player;
    private Vector<Shot> shots;
    private Vector<Centipede> centipedes;
    //private vector<Mushroom> mushrooms;
    private int game_score = 0;
    private boolean ingame = true;
    private boolean restart_round = false;
    private String message = "Game Over";
    private Thread animator;
    private boolean updatePresent = true;
    private String updateString = "";
    File shotSoundFile = new File("sounds/shotSound.wav");

    public Board(int m_c){
        TAdapter t_a = new TAdapter();
        addMouseListener(t_a);
        addMouseMotionListener(t_a);
        setFocusable(true);
        d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
        setBackground(Color.BLACK);
        gameInit();
        setDoubleBuffered(true);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        gameInit();
    }

    public void gameInit() {
        player = new Player();
        shots = new Vector<>();
        centipedes = new Vector<>();
        drawInitCentipede();

        if (animator == null || !ingame){
            animator = new Thread(this);
            animator.start();
        }

    }

    public void drawInitCentipede() {
        for (int i = (BOARD_WIDTH - CENTIPEDE_WIDTH); i > (BOARD_WIDTH - CENTIPEDE_WIDTH) - (CENTIPEDE_WIDTH * NUMBER_OF_CENTIPEDES_TO_DESTROY); i -= CENTIPEDE_WIDTH) {
            centipedes.add(new Centipede(i, 32));
            try {
                Thread.sleep(25);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void playSound(File sound) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(sound));
            clip.start();
            Thread.sleep(clip.getMicrosecondLength()/1000);
        } catch(Exception e) {   }
    }

    public void drawPlayer(Graphics g) {
        if (player.isVisible()) {
            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }
    }

    public void drawShot(Graphics g) {
        for (Shot shot : shots) {
            if (shot.isVisible()) {
                g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
            }
        }
    }

    public void drawScore(Graphics g) {
        if (ingame) {
            Font small = new Font("Courier", Font.BOLD, 14);
            FontMetrics metr = this.getFontMetrics(small);
            g.setColor(Color.white);
            g.setFont(small);
            g.drawString("Score: " + game_score, 2, 16);
        }
    }

    public void drawUpdate(Graphics g) {
        // updatePresent = false;
        if (ingame) {
            Font small = new Font("Courier", Font.BOLD, 14);
            FontMetrics metr = this.getFontMetrics(small);
            g.setColor(Color.pink);
            g.setFont(small);
            g.drawString(updateString, 275, 16);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);

        if (ingame) {
            drawPlayer(g);
            drawScore(g);
            drawShot(g);
            drawCentipedes(g);
            if(updatePresent) drawUpdate(g);
        }
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    public void drawCentipedes(Graphics g) {
        for(Centipede centipede: centipedes) {
            if(centipede.isDying()) {
                centipede.die();
            } else if(centipede.isVisible()) {
                g.drawImage(centipede.getImage(), centipede.getX(), centipede.getY(), this);
            }
        }
    }

    public void animationCycle() {
        player.update();
        animateShots();
        animateCentipedes();
    }

    private void animateCentipedes() {
        for(Centipede centipede : centipedes) {
            if (centipede.isVisible()) {
                int c_x = centipede.getX();
                int c_y = centipede.getY();

                if (c_x > centipede.rightBarrier) {
                    if (c_y <= centipede.bottomBarrier) {
                        centipede.downLevel();
                    }
                    centipede.goLeft();
                } else if(c_x < 0) {
                    if (c_y <= centipede.bottomBarrier) {
                        centipede.downLevel();
                    }
                    centipede.goRight();
                }
                else {
                    centipede.update(centipede.direction);
                }
            }
        }
    }

    private void animateShots() {
        for (Shot shot : shots) {
            if (shot.isVisible()) {
                int shotX = shot.getX();
                int shotY = shot.getY();
                shotY -= SHOT_SPEED;

                for (Centipede centipede: centipedes) {
                    int centipedeX = centipede.getX();
                    int centipedeY = centipede.getY();

                    if (centipede.isVisible() && shot.isVisible()) {
                        if (shotX >= centipedeX && shotX <= (centipedeX + CENTIPEDE_WIDTH)
                                && shotY >= centipedeY && shotY <= (centipedeY + CENTIPEDE_HEIGHT)) {
                            centipede.gotShot();
                            if (centipede.isDying()) {
                                game_score += 5;
                                updateString = "KILLED A CENTIPEDE SEGMENT!!! +5";
                            }
                            else {
                                game_score += 2;
                                updateString = "HIT A CENTIPEDE SEGMENT! +2";
                            }
                            shot.die();
                        }
                    }
                }

                if (shotY < 0) {
                    shot.die();
                } else {
                    shot.setY(shotY);
                }
            }
        }
    }

    @Override
    public void run() {
        long beforeTime, timeDiff, sleep;
        beforeTime = System.currentTimeMillis();
        while (ingame) {
            repaint();
            animationCycle();
            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;
            if (sleep < 0) {
                sleep = 2;
            }
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }
            beforeTime = System.currentTimeMillis();
        }
    }

    private class TAdapter extends MouseAdapter implements MouseMotionListener {
        @Override
        public void mouseMoved(MouseEvent e) {
            player.mouseMoved(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            int x = player.getX();
            int y = player.getY();
            shots.add(new Shot(x, y));
            //playSound(shotSoundFile);
        }
    }
}
