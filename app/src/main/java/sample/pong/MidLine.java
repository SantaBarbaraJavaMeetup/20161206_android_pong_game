package sample.pong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class MidLine {

    private static final float WIDTH = 4;

    private final Paint paint;

    private final float left;
    private final float right;

    public MidLine(float canvasWidth, float scale) {
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);

        float mid = canvasWidth / 2;
        float radius = WIDTH * scale / 2;
        left = mid - radius;
        right = mid + radius;
    }

    public void render(Canvas canvas) {
        canvas.drawRect(left, 0, right, canvas.getHeight(), paint);
    }

}
