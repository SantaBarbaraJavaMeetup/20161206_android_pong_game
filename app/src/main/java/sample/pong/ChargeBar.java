package sample.pong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class ChargeBar {

    private static final float TOP = 10;
    private static final float LEFT = 89;
    private static final float WIDTH = 130;
    private static final float HEIGHT = 10;

    private static final int MAX_CHARGE = 25;

    private final float top;
    private final float left;
    private final float width;
    private final float height;

    private final Paint paint;

    private int charge;

    public ChargeBar(float scale) {
        top = TOP * scale;
        left = LEFT * scale;
        width = WIDTH * scale;
        height = HEIGHT * scale;

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
    }

    public void reset() {
        charge = 0;
    }

    public void use() {
        charge++;
    }

    public boolean available() {
        return charge < MAX_CHARGE;
    }

    public void render(Canvas canvas) {
        float fraction = (float) charge / MAX_CHARGE;
        canvas.drawRect(left, top, left + fraction * width, top + height, paint);
    }

}
