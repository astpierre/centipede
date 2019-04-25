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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;


public class Board extends JPanel implements Runnable, Constraints {
    private Dimension d;
    private Player player;
    private Vector<Shot> shots;
    private Vector<Centipede> centipedes;
    private Vector<Mushroom> mushrooms;
    private Spider spider;
    private int game_score = 0;
    private boolean ingame = true;
    private boolean restart_round = false;
    private String message = "Game Over";
    private Thread animator;
    private boolean updatePresent = true;
    private String updateString = "";
    public boolean spaceEntered = false;
    public int numKills = 0;
    private int mushroomSeed = 0;
    private boolean enterEntered = true;
    File shotSoundFile = new File("sounds/shotSound.wav");

    public Board(int mushroomSeed) {
        this.mushroomSeed = mushroomSeed;
        TAdapter t_a = new TAdapter();
        SpaceToContinue s_to_c = new SpaceToContinue();
        addMouseListener(t_a);
        addMouseMotionListener(t_a);
        addKeyListener(s_to_c);
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
        spider = new Spider(200,32);
        shots = new Vector<>();
        centipedes = new Vector<>();
        mushrooms = new Vector<>();
        drawInitCentipede();
        drawInitMushrooms();

        if (animator == null || !ingame){
            animator = new Thread(this);
            animator.start();
        }

        updateString = "HIT SPACEBAR TO PLAY";
    }

    public void restart() {
        if(player.lives < 0) {
            enterEntered = false;
            updateString = "GAME OVER :( (to play again, hit ENTER!)";
            player.restore();
            numKills = 0;

            spider.restore();

            for(Shot shot: shots) {
                shot.setVisible(false);
            }
            for(Centipede centipede: centipedes) {
                centipede.restore();
            }
            for(Mushroom mushroom: mushrooms) {
                if(mushroom.restore() > 0) game_score += 10;
            }
            game_score = 0;


        } else {
            player.setX(player.START_X);
            player.setY(player.START_Y);
            player.setVisible(true);
            numKills = 0;

            spider.restore();

            for(Shot shot: shots) {
                shot.setVisible(false);
            }
            for(Centipede centipede: centipedes) {
                centipede.restore();
            }
            for(Mushroom mushroom: mushrooms) {

                if(mushroom.restore() > 0) game_score += 10;
            }
            updateString = "You died, HIT SPACEBAR TO CONTINUE";
        }
    }

    public void gameWon() {
        player.setX(player.START_X);
        player.setY(player.START_Y);
        player.setVisible(true);
        numKills = 0;

        spider.restore();

        for(Shot shot: shots) {
            shot.setVisible(false);
        }
        for(Centipede centipede: centipedes) {
            centipede.restore();
        }
        for(Mushroom mushroom: mushrooms) {
            mushroom.restore();
        }
        updateString = "HIT SPACEBAR TO CONTINUE";
    }

    public void drawInitCentipede() {
        int centipedeEnd = BOARD_WIDTH-CENTIPEDE_WIDTH;
        for(int i=0; i<NUMBER_OF_CENTIPEDES_TO_KILL; i++) {
            centipedes.add(new Centipede(centipedeEnd-(i*CENTIPEDE_WIDTH), 32));
        }
    }

    public void drawInitMushrooms() {
        int mushroomStartY = 32+2*CENTIPEDE_HEIGHT;
        int mushroomEndY = GROUND-PLAYER_HEIGHT-5;
        int mushroomMinX = 1;
        int mushroomMaxX = (BOARD_WIDTH-MUSHROOM_WIDTH)/MUSHROOM_WIDTH;
        int numberMushroomsPerRow = this.mushroomSeed;

        for(int i=mushroomStartY; i<mushroomEndY; i+=3*CENTIPEDE_HEIGHT) {
            for(int j=0; j<numberMushroomsPerRow; j++) {
                double tmp = (int)(Math.random()*((mushroomMaxX-mushroomMinX)+1))+mushroomMinX;
                mushrooms.add(new Mushroom((int)(tmp)*MUSHROOM_WIDTH, i));
            }
        }
    }

    public void drawLives(Graphics g) {
        if (ingame) {
            Font small = new Font("Courier", Font.BOLD, 12);
            FontMetrics metr = this.getFontMetrics(small);
            g.setColor(Color.blue);
            g.setFont(small);
            g.drawString("Lives: "+player.lives, 175, 16);
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
        } else {
            restart_round = true;
        }
    }

    public void drawShot(Graphics g) {
        if(ingame) {
            for (Shot shot : shots) {
                if (shot.isVisible()) {
                    g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
                }
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
            drawSpider(g);
            drawMushrooms(g);
            if(updatePresent) drawUpdate(g);
            drawLives(g);
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

    public void drawSpider(Graphics g) {
        if(spider.isDying()) {
            spider.die();
        } else if(spider.isVisible()) {
            g.drawImage(spider.getImage(), spider.getX(), spider.getY(), this);
        }
    }

    public void drawMushrooms(Graphics g) {
        for(Mushroom mushroom: mushrooms) {
            if(mushroom.isDying()) {
                mushroom.die();
            } else if(mushroom.isVisible()) {
                g.drawImage(mushroom.getImage(), mushroom.getX(), mushroom.getY(), this);
            }
        }
    }

    public void animationCycle() {
        if(ingame) {
            if(spaceEntered && enterEntered) {
                if(updateString == "YOU DIED, HIT SPACEBAR TO CONTINUE") {
                    updateString = "";
                } else if(updateString == "HIT SPACEBAR TO PLAY") {
                    updateString = "";
                } else if(updateString == "GAME OVER :( (to play again, hit ENTER!)") {
                    updateString = "";
                }
                player.update();
                animateShots();
                animateCentipedes();
                animateSpider();
            }
        }
    }

    private void animateCentipedes() {
        for(Centipede centipede : centipedes) {
            if (centipede.isVisible()) {
                int c_x = centipede.getX();
                int c_y = centipede.getY();
                for(Mushroom mushroom: mushrooms) {
                    int m_x = mushroom.getX();
                    int m_y = mushroom.getY();
                    if(mushroom.isVisible() && centipede.isVisible()) {
                        if(collision(m_x, m_y, c_x, c_y, CENTIPEDE_WIDTH, CENTIPEDE_HEIGHT)) {
                            switch(centipede.direction) {
                                case "L":
                                    centipede.goRight();
                                    break;
                                case "R":
                                    centipede.goLeft();
                                    break;
                            }
                            if (c_y <= centipede.bottomBarrier) {
                                centipede.downLevel();
                            }
                        }
                    }
                }

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

                int p_x = player.getX();
                int p_y = player.getY();
                if(collision(p_x, p_y, c_x, c_y, CENTIPEDE_WIDTH, CENTIPEDE_HEIGHT)) {
                    player.loseLife();
                    spaceEntered = false;
                    restart();
                }
            }
        }
    }

    private void animateSpider() {
        if(spider.isVisible()) {
            int s_x = spider.getX();
            int s_y = spider.getY();
            for(Mushroom mushroom: mushrooms) {
                int m_x = mushroom.getX();
                int m_y = mushroom.getY();
                if(mushroom.isVisible() && spider.isVisible()) {
                    if(collision(m_x, m_y, s_x, s_y, SPIDER_WIDTH, SPIDER_HEIGHT)) {
                        switch(spider.direction) {
                            case "L":
                                spider.goRight();
                                break;
                            case "R":
                                spider.goLeft();
                                break;
                        }
                        if (s_y <= spider.bottomBarrier) {
                            spider.changeLevel();
                        }
                    }
                }
            }

            if (s_x > spider.rightBarrier) {
                if (s_y > spider.bottomBarrier) {
                    spider.goUp();
                }
                spider.goLeft();
            } else if(s_x < 0) {
                if (s_y > spider.bottomBarrier) {
                    spider.goUp();
                }
                spider.goRight();
            } else if(s_y > spider.bottomBarrier) {
                spider.goUp();
            }
            else {
                spider.update(spider.direction);
            }

            int p_x = player.getX();
            int p_y = player.getY();
            if(collision(p_x, p_y, s_x, s_y, SPIDER_WIDTH, SPIDER_HEIGHT)) {
                player.loseLife();
                spaceEntered = false;
                restart();
            } else if(collision(s_x, s_y, p_x, p_y, PLAYER_WIDTH, PLAYER_HEIGHT)) {
                player.loseLife();
                spaceEntered = false;
                restart();
            }
        }
    }

    private boolean collision(int a_x, int a_y, int b_x, int b_y, int b_width, int b_height) {
        if(a_x >= b_x && a_x <= (b_x + b_width)) {
            if(a_y >= b_y && a_y <= (b_y+b_height)) {
                return true;
            }
        }
        return false;
    }

    private void animateShots() {
        for (Shot shot : shots) {
            if (shot.isVisible()) {
                int s_x = shot.getX();
                int s_y = shot.getY();
                s_y -= SHOT_SPEED;

                for (Centipede centipede: centipedes) {
                    int c_x = centipede.getX();
                    int c_y = centipede.getY();

                    if (centipede.isVisible() && shot.isVisible()) {
                        if(collision(s_x, s_y, c_x, c_y, CENTIPEDE_WIDTH, CENTIPEDE_HEIGHT)) {
                            centipede.gotShot();

                            if (centipede.isDying()) {
                                game_score += 5;
                                updateString = "KILLED A CENTIPEDE SEGMENT!!! +5";
                                if(++numKills == NUMBER_OF_CENTIPEDES_TO_KILL) {
                                    updateString = "KILLED ENTIRE CENTIPEDE!!! +600";
                                    game_score += 600;
                                    spaceEntered = false;
                                    gameWon();
                                }
                            } else {
                                game_score += 2;
                                updateString = "HIT A CENTIPEDE SEGMENT! +2";
                            }
                            shot.die();
                        }
                    }
                }
                for (Mushroom mushroom: mushrooms) {
                    int m_x = mushroom.getX();
                    int m_y = mushroom.getY();

                    if (mushroom.isVisible() && shot.isVisible()) {
                        if(collision(s_x, s_y, m_x, m_y, MUSHROOM_WIDTH, MUSHROOM_HEIGHT)) {
                            mushroom.gotShot();
                            if (mushroom.isDying()) {
                                game_score += 5;
                                updateString = "KILLED A MUSHROOM!!! +5";
                            }
                            else {
                                game_score += 1;
                                updateString = "HIT A MUSHROOM! +1";
                            }
                            shot.die();
                        }
                    }
                }

                int sp_x = spider.getX();
                int sp_y = spider.getY();
                if (spider.isVisible() && shot.isVisible()) {
                    if(collision(s_x, s_y, sp_x, sp_y, SPIDER_WIDTH, SPIDER_HEIGHT)) {
                        spider.gotShot();
                        if(spider.isDying()) {
                            game_score += 600;
                            updateString = "KILLED THE SPIDER!!! +600";
                        }
                        else {
                            game_score += 100;
                            updateString = "HIT THE SPIDER! +100";
                        }
                        shot.die();
                    }
                }

                if (s_y < 0) {
                    shot.die();
                } else {
                    shot.setY(s_y);
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
            sleep = 20 - timeDiff;
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

    private class SpaceToContinue extends KeyAdapter implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {  }
        @Override
        public void keyReleased(KeyEvent e) {  }
        @Override
        public void keyPressed(KeyEvent e) {
            if ( e.getKeyCode() == KeyEvent.VK_SPACE ) {
                spaceEntered = true;
            } else if( e.getKeyCode() == KeyEvent.VK_ENTER ) {
                enterEntered = true;
                spaceEntered = true;
            }
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
