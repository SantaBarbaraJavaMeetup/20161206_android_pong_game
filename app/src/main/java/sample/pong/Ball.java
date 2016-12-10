package sample.pong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Ball {

    private static final float COLLISION_GRACE = 5;

    private final float RADIUS = 7.0f;
    private final float SPEED = 8.0f;
    private final float MAX_ANGLE = (float)Math.toRadians(65.0);

    private final float canvasWidth;
    private final float canvasHeight;
    private final float speed;
    private final float collisionGrace;
    private final float radius;
    private final float diameter;

    private final Paint paint;

    private float top;
    private float left;

    private float xVelocity;
    private float yVelocity;

    public Ball(float canvasWidth, float canvasHeight, float canvasScale, float fpsScale) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;

        speed = SPEED * canvasScale / fpsScale;
        radius = RADIUS * canvasScale;
        diameter = radius * 2;
        collisionGrace = COLLISION_GRACE * canvasScale / fpsScale;

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);

        reset();
    }

    public float left() {
        return left;
    }

    public float top() {
        return top;
    }

    public float diameter() {
        return radius * 2;
    }

    public float radius() {
        return radius;
    }

    public float xVelocity() {
        return xVelocity;
    }

    public float yVelocity() {
        return yVelocity;
    }

    public boolean movingLeft() {
        return xVelocity < 0;
    }

    public boolean movingRight() {
        return xVelocity > 0;
    }

    public boolean movingDown() {
        return yVelocity > 0;
    }

    public boolean movingUp() {
        return yVelocity < 0;
    }

    public boolean outLeft() {
        return left + diameter <= 0;
    }

    public boolean outRight() {
        return left >= canvasWidth;
    }

    public boolean containsPoint(float x, float y) {
        float xCenter = left + radius;
        float yCenter = top + radius;
        return Math.sqrt(Math.pow(xCenter - x, 2) + Math.pow(yCenter - y, 2)) <= radius + collisionGrace;
    }

    public void collision(float yRelative, boolean negate) {
        apply(yRelative * MAX_ANGLE, negate);
    }

    public void reset() {
        top = canvasHeight / 2;
        left = canvasWidth / 4;
        apply(0, false);
    }

    private void apply(double angle, boolean negate) {
        xVelocity = (float)(speed * Math.cos(angle)) * (negate ? -1 : 1);
        yVelocity = (float)(speed * Math.sin(angle));
    }

    public void update() {
        if (yVelocity > 0 && top + diameter >= canvasHeight || yVelocity < 0 && top <= 0) {
            yVelocity = -yVelocity;
        }

        top += yVelocity;
        left += xVelocity;
    }

    public void render(Canvas canvas) {
        canvas.drawOval(left, top, left + diameter, top + diameter, paint);
    }

}
