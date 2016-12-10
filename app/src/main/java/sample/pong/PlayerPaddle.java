package sample.pong;

public class PlayerPaddle extends Paddle {

    public PlayerPaddle(float canvasHeight, float canvasScale, float fpsScale) {
        super(canvasHeight, canvasScale, fpsScale);
    }

    protected Float doTestCollision(Ball ball) {
        if (!ball.movingLeft()) {
            return null;
        }

        // mid-left ball point inside of paddle
        if (containsPoint(ball.left(), ball.top() + ball.radius())) {
            return Math.max(top, Math.min(top + height, ball.top() + ball.radius()));
        }

        // mid-bottom ball point inside of paddle
        if (ball.movingDown() && containsPoint(ball.left() + ball.radius(), ball.top() + ball.diameter())) {
            return top;
        }

        // ball contains upper right corner point of paddle
        if (ball.movingDown() && ball.containsPoint(left + width, top)) {
            return top;
        }

        // mid-top ball point inside of paddle
        if (ball.movingUp() && containsPoint(ball.left() + ball.radius(), ball.top())) {
            return top + height;
        }

        // ball contains bottom right corner point of paddle
        if (ball.movingUp() && ball.containsPoint(left + width, top + height)) {
            return top + height;
        }

        return null;
    }

}
