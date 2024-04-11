import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Objects;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    // frame size
    private int frameWidth = 360, frameHeight = 640;
    private int halfFrameWidth = frameWidth / 2, halfFrameHeight = frameHeight / 2;
    // image assets
    private Image backgroundImage, birdImage, lowerPipeImage, upperPipeImage;
    // player: size and position
    private int playerWidth = 34, playerHeight = 24;
    private int playerStartPosX = halfFrameWidth - (playerWidth / 2), playerStartPosY = halfFrameHeight - (playerHeight / 2);
    private Player player;
    // pipe: size and postion
    private int pipeWidth = 64, pipeHeight = 512;
    private int pipeStartPosX = frameWidth, pipeStartPosY = 0;
    private ArrayList<Pipe> pipes;
    // timer
    private Timer gameLoop, pipesCooldown;
    // gravity
    private int gravity = 1;
    // game condition
    public enum GameCond {waitingToStart, onStart, onGame, gameOver, onPause}
    private GameCond gameCond;
    // safe zone boundaries
    private int upperBoundSafeZone = 0, lowerBoundSafeZone = frameHeight - playerHeight;
    public enum HitCond {unhit, hitTop, hitBottom}
    // score
    private int score;
    // trigger
    public interface FlappyBirdListener {
        void onStart();
        void onWaitingToStart();
        void onGame(int score);
        void onGameOver();
        void onRestart();
        void onPause();
        void onContinue();
    }
    private FlappyBirdListener flappyBirdListener;

    // constructor
    public FlappyBird() {
        // set up panel and listener
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        setFocusable(true);
        addKeyListener(this);

        // load image
        backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("assets/background.png"))).getImage();
        birdImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("assets/bintang.png"))).getImage();
        lowerPipeImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("assets/lowerPipe.png"))).getImage();
        upperPipeImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("assets/upperPipe.png"))).getImage();

        // set up player
        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);

        // set up pipes
        pipes = new ArrayList<>();
        pipesCooldown = new Timer(3000, actionEvent -> {
            System.out.println("Pipe");
            if(gameCond == GameCond.onStart) {
                flappyBirdListener.onGame(score);
                gameCond = GameCond.onGame;
            }
            placesPipe();
        });

        // set up game loop
        gameLoop = new Timer(1000/60, this);
        gameCond = GameCond.waitingToStart;
    }

    // getter and setter
    public int getFrameWidth() { return frameWidth; }
    public void setFrameWidth(int frameWidth) { this.frameWidth = frameWidth; }
    public int getFrameHeight() { return frameHeight;}
    public void setFrameHeight(int frameHeight) { this.frameHeight = frameHeight; }
    public int getHalfFrameWidth() { return halfFrameWidth; }
    public void setHalfFrameWidth(int halfFrameWidth) { this.halfFrameWidth = halfFrameWidth; }
    public int getHalfFrameHeight() { return halfFrameHeight; }
    public void setHalfFrameHeight(int halfFrameHeight) { this.halfFrameHeight = halfFrameHeight; }
    public Image getBackgroundImage() { return backgroundImage; }
    public void setBackgroundImage(Image backgroundImage) { this.backgroundImage = backgroundImage; }
    public Image getBirdImage() { return birdImage; }
    public void setBirdImage(Image birdImage) { this.birdImage = birdImage; }
    public Image getLowerPipeImage() { return lowerPipeImage; }
    public void setLowerPipeImage(Image lowerPipeImage) { this.lowerPipeImage = lowerPipeImage; }
    public Image getUpperPipeImage() { return upperPipeImage; }
    public void setUpperPipeImage(Image upperPipeImage) { this.upperPipeImage = upperPipeImage; }
    public int getPlayerWidth() { return playerWidth; }
    public void setPlayerWidth(int playerWidth) { this.playerWidth = playerWidth; }
    public int getPlayerHeight() { return playerHeight; }
    public void setPlayerHeight(int playerHeight) { this.playerHeight = playerHeight; }
    public int getPlayerStartPosX() { return playerStartPosX; }
    public void setPlayerStartPosX(int playerStartPosX) { this.playerStartPosX = playerStartPosX; }
    public int getPlayerStartPosY() { return playerStartPosY; }
    public void setPlayerStartPosY(int playerStartPosY) { this.playerStartPosY = playerStartPosY; }
    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }
    public int getPipeWidth() { return pipeWidth; }
    public void setPipeWidth(int pipeWidth) { this.pipeWidth = pipeWidth; }
    public int getPipeHeight() { return pipeHeight; }
    public void setPipeHeight(int pipeHeight) { this.pipeHeight = pipeHeight; }
    public int getPipeStartPosX() { return pipeStartPosX; }
    public void setPipeStartPosX(int pipeStartPosX) { this.pipeStartPosX = pipeStartPosX; }
    public int getPipeStartPosY() { return pipeStartPosY; }
    public void setPipeStartPosY(int pipeStartPosY) { this.pipeStartPosY = pipeStartPosY; }
    public Timer getGameLoop() { return gameLoop; }
    public void setGameLoop(Timer gameLoop) { this.gameLoop = gameLoop; }
    public Timer getPipesCooldown() { return pipesCooldown; }
    public void setPipesCooldown(Timer pipesCooldown) { this.pipesCooldown = pipesCooldown; }
    public int getGravity() { return gravity; }
    public void setGravity(int gravity) { this.gravity = gravity; }
    public GameCond getGameCond() { return gameCond; }
    public void setGameCond(GameCond gameCond) { this.gameCond = gameCond; }
    public int getUpperBoundSafeZone() { return upperBoundSafeZone; }
    public void setUpperBoundSafeZone(int upperBoundSafeZone) { this.upperBoundSafeZone = upperBoundSafeZone; }
    public int getLowerBoundSafeZone() { return lowerBoundSafeZone; }
    public void setLowerBoundSafeZone(int lowerBoundSafeZone) { this.lowerBoundSafeZone = lowerBoundSafeZone; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public FlappyBirdListener getFlappyBirdListener() { return flappyBirdListener; }
    public void setFlappyBirdListener(FlappyBirdListener flappyBirdListener) { this.flappyBirdListener = flappyBirdListener; }

    // set up pipes
    public void placesPipe() {
        int randomPosY = (int) (pipeStartPosY - pipeHeight/4 - Math.random() * (pipeHeight/2));
        int openingSpace = frameHeight/4;

        Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, (randomPosY + openingSpace + pipeHeight), pipeWidth, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);
    }

    // reset starting point
    public void resetStartingPoint() {
        player.setVelocityY(0);
        player.setPosY(playerStartPosY);
        upperBoundSafeZone = 0;
        lowerBoundSafeZone = frameHeight - playerHeight;
        score = 0;
    }

    /**
     * Gameplay
     * */
    public void startGame() {
        pipesCooldown.start();
        gameLoop.start();
        gameCond = GameCond.onStart;
        flappyBirdListener.onStart();
    }

    public void stopGame() {
        System.out.println("Game Over");
        pipesCooldown.stop();
        gameLoop.stop();
        gameCond = GameCond.gameOver;
        flappyBirdListener.onGameOver();
    }

    public void restartGame() {
        resetStartingPoint();
        pipes.clear();
        repaint();
        gameCond = GameCond.waitingToStart;
        flappyBirdListener.onRestart();
    }

    public void pauseGame() {
        gameCond = GameCond.onPause;
        flappyBirdListener.onPause();
    }

    public void continueGame() {
        pipesCooldown.start();
        gameLoop.start();
        gameCond = GameCond.onGame;
        flappyBirdListener.onGame(score);
        flappyBirdListener.onContinue();
    }

    /**
     * draw assets
     * */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // layer 1: background
        g.drawImage(backgroundImage, 0, 0, frameWidth, frameHeight, null);
        // layer 2: pipe
        for (Pipe pipe : pipes) {
            g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), null);
        }
        // layer 3: player
        g.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), null);
    }

    /**
     * Check game condition: lose(hit) or win(unhit)
     * */
    public HitCond checkGame() {
        HitCond hitCond = HitCond.unhit;
        int curPlayerPosY = player.getPosY();

        if (curPlayerPosY < upperBoundSafeZone) hitCond = HitCond.hitTop;
        else if (curPlayerPosY > lowerBoundSafeZone) hitCond = HitCond.hitBottom;

        return hitCond;
    }

    // gameplay: move player and pipes
    public void move() {
        // set velocity up and down
        player.setVelocityY(player.getVelocityY() + gravity);
        // move player position by y axis
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.max(player.getPosY(), 0));

        // get total pipes and set pipe passed condition
        int totalPipes = pipes.size();
        boolean pipePassed = false;

        // pipe loop: upper pipe is pivot, so iterate with 2
        for (int i = 0; i < totalPipes; i+=2) {
            // get upper and lower pipe
            Pipe upperPipe = pipes.get(i), lowerPipe = pipes.get(i + 1);
            //  get current x position of pipe: left and right point
            int leftBoundPipe = upperPipe.getPosX();
            int rightBoundPipe = upperPipe.getPosX() + pipeWidth;

            // move the pipe that appear on screen
            if(rightBoundPipe >= 0) {
                // move pipe potition by x axis
                upperPipe.setPosX(upperPipe.getPosX() - upperPipe.getVelocityX());
                lowerPipe.setPosX(lowerPipe.getPosX() - lowerPipe.getVelocityX());

                // set top and bottom safezone
                if (leftBoundPipe == playerStartPosX + playerWidth) {
                    upperBoundSafeZone = upperPipe.getPosY() + pipeHeight;
                    lowerBoundSafeZone = lowerPipe.getPosY() - playerHeight;
                } else if (rightBoundPipe == playerStartPosX) {
                    upperBoundSafeZone = 0;
                    lowerBoundSafeZone = frameHeight - playerHeight;
                    pipePassed = true;
                }
            }
        }

        // check game condition: hit bondaries or not
        HitCond hitCond = checkGame();
        if(hitCond != HitCond.unhit) {  // stop game if hit
            stopGame();
        } else {                        // increase game score if not
            if(pipePassed) {
                score++;
                flappyBirdListener.onGame(score);
            }
        }
    }

    // gameplay: play or stop game
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (gameCond == GameCond.onGame) move();
        else if (gameCond == GameCond.onStart) move();
        else pipesCooldown.stop();
        repaint();
    }

    // gameplay: keyboard trigger
    @Override
    public void keyTyped(KeyEvent keyEvent) {}

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        // get key pressed
        int keyCodePressed = keyEvent.getKeyCode();
        // trigger something by key pressed by game condition
        switch (gameCond) {
            case onGame, onStart -> {
                if (keyCodePressed == KeyEvent.VK_SPACE) player.setVelocityY(-10);
                else if (keyCodePressed == KeyEvent.VK_P) pauseGame();
            }
            case waitingToStart -> {
                if (keyCodePressed == KeyEvent.VK_SPACE) startGame();
            }
            case gameOver -> {
                if (keyCodePressed == KeyEvent.VK_R) restartGame();
            }
            case onPause -> {
                if (keyCodePressed == KeyEvent.VK_P) continueGame();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {}
}
