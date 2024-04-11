import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class App {
    // Frame
    static JFrame frame = new JFrame("Flappy Bird");
    // Panel
    static FlappyBird flappyBird = new FlappyBird();
    // Button
    static JButton startButton = new JButton("Start Game!");
    static JButton restartButton = new JButton("Restart Game!");
    static JButton continueButton = new JButton("Continue!");
    static JButton quitButton = new JButton("Quit");
    // Label
    static JLabel waitingToStartLabel = new JLabel("Flappy Bird");
    static JLabel startLabel = new JLabel("Get Ready");
    static JLabel gameOverLabel = new JLabel("Game Over");
    static JLabel continueLabel = new JLabel("Game Pause");

    /**
     * Game Flappy Bird
     * */
    public static void main(String[] args) {
        // set up frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // close button
        frame.setSize(360, 640);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // set up layout
        flappyBird.setLayout(null);

        // set up button position and listener
        int lowerBoundButton = flappyBird.getHalfFrameHeight() + 66;
        int rightBoundButton = flappyBird.getHalfFrameWidth() - 100;
        int buttonWidth = 200, buttonHeight = 34;

        startButton.setBounds(rightBoundButton, lowerBoundButton, buttonWidth, buttonHeight);
        startButton.addActionListener(actionEvent -> flappyBird.startGame());

        restartButton.setBounds(rightBoundButton, lowerBoundButton, buttonWidth, buttonHeight);
        restartButton.addActionListener(actionEvent -> flappyBird.restartGame());

        continueButton.setBounds(rightBoundButton, lowerBoundButton, buttonWidth, buttonHeight);
        continueButton.addActionListener(actionEvent -> flappyBird.continueGame());

        quitButton.setBounds(rightBoundButton, lowerBoundButton + buttonHeight + 10, buttonWidth, buttonHeight);
        quitButton.addActionListener(actionEvent -> {
            int result = JOptionPane.showConfirmDialog(flappyBird, "Are you sure?", "Confirm Quit", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) System.exit(0);
        });
        quitButton.setFocusable(false);

        try {
            // set up font
            InputStream flappyBirdFontIs;
            flappyBirdFontIs = App.class.getResourceAsStream("assets/FlappyBirdRegular.ttf");
            Font flappyBirdFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(flappyBirdFontIs));
            Font jLabelFont = flappyBirdFont.deriveFont(32f);
            Font jButtonFont = flappyBirdFont.deriveFont(26f);

            // set up custom color
            Color lightGreen = new Color(128, 208, 16);
            Color green = new Color(0, 168, 0);
            Color red = new Color(208, 0, 50);
            Color darkBlue = new Color(12, 139, 154);

            // set up label
            int jLabelPosY = flappyBird.getHalfFrameHeight() - 100;
            setJLabelStyle(waitingToStartLabel, jLabelFont, Color.WHITE, jLabelPosY);
            setJLabelStyle(startLabel, jLabelFont, Color.WHITE, jLabelPosY - 200);
            setJLabelStyle(gameOverLabel, jLabelFont, Color.WHITE, jLabelPosY);
            setJLabelStyle(continueLabel, jLabelFont, Color.WHITE, jLabelPosY);

            // set up button style
            setJButtonStyle(startButton, jButtonFont, green, Color.WHITE);
            setJButtonStyle(restartButton, jButtonFont, lightGreen, Color.WHITE);
            setJButtonStyle(continueButton, jButtonFont, darkBlue, Color.WHITE);
            setJButtonStyle(quitButton, jButtonFont, red, Color.WHITE);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

        // set up flappy bird trigger
        flappyBird.setFlappyBirdListener(new FlappyBird.FlappyBirdListener() {
            @Override
            public void onStart() {
                startLabel.setText("Get Ready?");
                flappyBird.add(startLabel);
                flappyBird.remove(quitButton);
                removeStartingLayout();
            }

            @Override
            public void onWaitingToStart() {
                flappyBird.add(quitButton);
                addStartingLayout();
            }

            @Override
            public void onGame(int score) {
                startLabel.setText(String.valueOf(score));
            }

            @Override
            public void onGameOver() {
                flappyBird.add(quitButton);
                addGameOverLayout();
            }

            @Override
            public void onRestart() {
                flappyBird.remove(startLabel);
                removeGameOverLayout();
                addStartingLayout();
            }

            @Override
            public void onPause() {
                flappyBird.add(quitButton);
                addPauseLayout();
            }

            @Override
            public void onContinue() {
                flappyBird.remove(quitButton);
                removePauseLayout();
            }
        });

        // set up first layout
        flappyBird.add(quitButton);
        addStartingLayout();
        frame.add(flappyBird);

        // show frame
        frame.pack();
        frame.requestFocus();
        frame.setVisible(true);
    }

    // show and hide layout
    public static void addStartingLayout() { addLayout(waitingToStartLabel, startButton); }
    public static void removeStartingLayout() { removeLayout(waitingToStartLabel, startButton); }
    public static void addGameOverLayout() { addLayout(gameOverLabel, restartButton); }
    public static void removeGameOverLayout() { removeLayout(gameOverLabel, restartButton); }
    public static void addPauseLayout() { addLayout(continueLabel, continueButton); }
    public static void removePauseLayout() { removeLayout(continueLabel, continueButton); }

    public static void addLayout(JLabel jLabel, JButton jButton) {
        flappyBird.add(jLabel);
        flappyBird.add(jButton);
    }

    public static void removeLayout(JLabel jLabel, JButton jButton) {
        flappyBird.remove(jLabel);
        flappyBird.remove(jButton);
    }

    // styling label and button
    public static void setJLabelStyle(JLabel jLabel, Font font, Color color, int posY) {
        jLabel.setHorizontalAlignment(JLabel.CENTER);
        jLabel.setBounds(0, posY, flappyBird.getFrameWidth(), 50);
        jLabel.setFont(font);
        jLabel.setForeground(color);
    }

    public static void setJButtonStyle(JButton jButton, Font font, Color background, Color foreground) {
        jButton.setFont(font);
        jButton.setBackground(background);
        jButton.setForeground(foreground);
    }
}
