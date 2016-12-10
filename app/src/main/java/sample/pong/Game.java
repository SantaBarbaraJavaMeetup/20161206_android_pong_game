package sample.pong;

import android.graphics.Canvas;

public class Game {

    private static final float WIDTH = 620f;

    private final float fpsScale;

    private SplashScreen splashScreen;
    private PlayerPaddle playerPaddle;
    private GamePaddle gamePaddle;
    private ChargeBar chargeBar;
    private Ball ball;
    private Score score;
    private MidLine midLine;

    private volatile boolean down;
    private volatile float yTouch;
    private volatile boolean intro;

    public Game(float fpsScale) {
        this.fpsScale = fpsScale;
        intro = true;
    }

    public void onTouchDown(float x, float y) {
        yTouch = y;
        down = true;
    }

    public void onTouchUp(float x, float y) {
        down = false;
        intro = false;
    }

    private void init(int width, int height) {
        float canvasScale = width / WIDTH;

        splashScreen = new SplashScreen(canvasScale, fpsScale);
        playerPaddle = new PlayerPaddle(height, canvasScale, fpsScale);
        gamePaddle = new GamePaddle(width, height, canvasScale, fpsScale);
        ball = new Ball(width, height, canvasScale, fpsScale);
        chargeBar = new ChargeBar(canvasScale);
        score = new Score(width, canvasScale);
        midLine = new MidLine(width, canvasScale);
    }

    public void update(int width, int height) {
        if (playerPaddle == null) {
            init(width, height);
        }

        if (!intro && !splashScreen.closing()) {
            splashScreen.close();
        }

        if (intro || (!intro && !splashScreen.closed())) {
            splashScreen.update();
            return;
        }

        gamePaddle.ai(ball);

        if (down && chargeBar.available()) {
            playerPaddle.accelerate(yTouch);
            chargeBar.use();
        }

        playerPaddle.update();
        gamePaddle.update();
        ball.update();

        if (ball.outLeft()) {
            score.gameScore();
            ball.reset();
            chargeBar.reset();
        }

        if (ball.outRight()) {
            score.playerScore();
            ball.reset();
            chargeBar.reset();
        }

        Float yRelative = playerPaddle.testCollision(ball);
        if (yRelative != null) {
            ball.collision(yRelative, false);
            chargeBar.reset();
        }

        yRelative = gamePaddle.testCollision(ball);
        if (yRelative != null) {
            ball.collision(yRelative, true);
        }
    }

    public void render(Canvas canvas) {
        canvas.drawARGB(255, 0, 0, 0);

        if (intro || (!intro && !splashScreen.closed())) {
            splashScreen.render(canvas);
            return;
        }

        midLine.render(canvas);
        score.render(canvas);
        playerPaddle.render(canvas);
        gamePaddle.render(canvas);
        ball.render(canvas);
        chargeBar.render(canvas);
    }

}
