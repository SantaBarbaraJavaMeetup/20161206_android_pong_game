package sample.pong;

public class GamePaddle extends Paddle {

    private static final float DIFFICULT = 0.13f;

    private float difficulty;

    public GamePaddle(float canvasWidth, float canvasHeight, float canvasScale, float fpsScale) {
        super(canvasHeight, canvasScale, fpsScale);
        left = canvasWidth - width - MARGIN * canvasScale;
        difficulty = DIFFICULT / fpsScale;
    }

    public void ai(Ball ball) {
        if (Math.random() < difficulty) {
            if (ball.movingRight()) {
                float xVelocity = ball.xVelocity();
                float yVelocity = ball.yVelocity();
                float ballLeft = ball.left();
                float ballTop = ball.top();

                float range = left - ballLeft - ball.diameter();
                float frames = range / xVelocity;

                float yDest = ballTop + yVelocity * frames;

                if (yDest > 0 && yDest < canvasHeight) {
                    accelerate(yDest);
                } else if (yDest < 0 && yDest > -canvasHeight) {
                    accelerate(-yDest);
                } else if (yDest > canvasHeight && yDest < canvasHeight * 2) {
                    accelerate(canvasHeight - (yDest - canvasHeight));
                } else if (yDest < -height && yDest > -canvasHeight * 2) {
                    accelerate(canvasHeight - (-yDest - canvasHeight));
                } else {
                    accelerate(canvasHeight / 2);
                }
            } else {
                accelerate(canvasHeight / 2);
            }
        }
    }

    protected Float doTestCollision(Ball ball) {
        if (!ball.movingRight()) {
            return null;
        }

        // mid-right ball point inside of paddle
        if (containsPoint(ball.left() + ball.diameter(), ball.top() + ball.radius())) {
            return Math.max(top, Math.min(top + height, ball.top() + ball.radius()));
        }

        // mid-bottom ball point inside of paddle
        if (ball.movingDown() && containsPoint(ball.left() + ball.radius(), ball.top() + ball.diameter())) {
            return top;
        }

        // ball contains upper left corner point of paddle
        if (ball.movingDown() && ball.containsPoint(left, top)) {
            return top;
        }

        // mid-top ball point inside of paddle
        if (ball.movingUp() && containsPoint(ball.left() + ball.radius(), ball.top())) {
            return top + height;
        }

        // ball contains bottom left corner point of paddle
        if (ball.movingUp() && ball.containsPoint(left, top + height)) {
            return top + height;
        }

        return null;
    }

}
