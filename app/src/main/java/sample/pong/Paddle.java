package sample.pong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public abstract class Paddle {

    protected static final float DRAG = 1.09f;

    protected static final float COLLISION_GRACE = 5.0f;

    protected static final float ACCELERATION_GRACE = 10.0f;

    protected static final float MAX_VELOCITY = 9.0f;
    protected static final float ACCELERATION = 1.2f;

    protected static final float MARGIN = 10.0f;
    protected static final float WIDTH = 12.0f;
    protected static final float HEIGHT = 60.0f;

    protected final float drag;
    protected final float maxVelocity;
    protected final float acceleration;
    protected final float collisionGrace;
    protected final float accelerationGrace;

    protected final float width;
    protected final float height;
    protected final float canvasHeight;

    protected final Paint paint;

    protected float left;
    protected float top;

    protected float velocity;

    public Paddle(float canvasHeight, float canvasScale, float fpsScale) {
        this.canvasHeight = canvasHeight;

        left = MARGIN * canvasScale;
        width = WIDTH * canvasScale;
        height = HEIGHT * canvasScale;
        top = canvasHeight / 2 - height / 2;

        drag = DRAG * fpsScale;
        maxVelocity = MAX_VELOCITY * canvasScale / fpsScale;
        acceleration = ACCELERATION * canvasScale / fpsScale;
        collisionGrace = COLLISION_GRACE * canvasScale / fpsScale;
        accelerationGrace = ACCELERATION_GRACE * canvasScale;
        velocity = 0;

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
    }

    public boolean containsPoint(float x, float y) {
        RectF rect = new RectF(
                left - collisionGrace,
                top - collisionGrace,
                left + width + collisionGrace,
                top + height + collisionGrace);

        return rect.contains(x, y);
    }

    public Float testCollision(Ball ball) {
        Float y = doTestCollision(ball);
        return y == null ? null : normalize(y);
    }

    private float normalize(float y) {
        float midpoint = top + height / 2;
        float yRelative = y - midpoint;
        return yRelative / (height / 2);
    }

    protected abstract Float doTestCollision(Ball ball);

    public void accelerate(float y) {
        float diff = y - (top + height / 2);
        if (diff < -accelerationGrace) {
            up();
        } else if (diff > accelerationGrace) {
            down();
        }
    }

    public void up() {
        velocity = Math.max(-maxVelocity, velocity - acceleration);
    }

    public void down() {
        velocity = Math.min(maxVelocity, velocity + acceleration);
    }

    public void update() {
        velocity /= DRAG;

        if (velocity > 0 && top + height >= canvasHeight || velocity < 0 && top <= 0) {
            velocity = -velocity;
        }

        top += velocity;
    }

    public void render(Canvas canvas) {
        canvas.drawRect(left, top, left + width, top + height, paint);
    }
}
