package sample.pong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;

public class SplashScreen {

    private static final float[][] LETTER_P_OUTER = {
            {138.41315f, 187.98037f},
            {115.62909f, 82.604101f},
            {160.6276f, 36.466385f},
            {208.47413f, 56.402435f},
            {208.47413f, 96.844137f},
            {185.12047f, 129.31142f},
            {149.23558f, 120.1978f},
            {154.93159f, 186.84116f},
    };

    private static final float[][] LETTER_P_INNER = {
            {152.65318f, 78.04729f},
            {151.51398f, 94.565731f},
            {173.15884f, 100.83135f},
            {186.82927f, 94.565731f},
            {187.96847f, 68.364065f},
            {170.31083f, 57.541638f},
    };

    private static final float[][] LETTER_O_OUTER = {
            {237.5238f, 111.65377f},
            {211.32213f, 147.53866f},
            {232.39738f, 178.29714f},
            {265.43427f, 186.27156f},
            {293.91434f, 159.50029f},
            {284.80072f, 117.34979f},
    };

    private static final float[][] LETTER_O_INNER = {
            {244.92862f, 146.39946f},
            {250.62463f, 154.94348f},
            {256.89025f, 158.36109f},
            {269.42148f, 150.38667f},
            {263.72546f, 138.42504f},
            {252.33344f, 135.00743f},
    };

    private static final float[][] LETTER_N = {
            {315.5592f, 187.41077f},
            {313.28079f, 121.337f},
            {363.40572f, 123.61541f},
            {373.08894f, 186.84117f},
            {356.0009f, 186.84117f},
            {345.74808f, 144.12106f},
            {325.81203f, 144.69066f},
            {330.36884f, 186.84117f},
    };

    private static final float[][] LETTER_G_OUTER = {
            {442.58032f, 120.1978f},
            {407.26503f, 116.21059f},
            {389.03778f, 134.43783f},
            {398.72101f, 149.24747f},
            {424.92267f, 148.10827f},
            {429.47949f, 166.90511f},
            {406.69543f, 169.18352f},
            {403.84742f, 161.7787f},
            {393.5946f, 161.7787f},
            {402.13862f, 188.54997f},
            {443.71952f, 187.41077f},
            {457.38996f, 172.60113f},
            {453.97235f, 135.00743f},
    };

    private static final float[][] LETTER_G_INNER = {
            {431.75789f, 130.45061f},
            {417.51785f, 128.17221f},
            {410.68263f, 132.72902f},
            {414.66984f, 138.99464f},
            {426.63147f, 138.99464f},
    };

    private static final float[][] BASELINE = {
            {90f, 230f},
            {100f, 230f},
            {470f, 230f},
            {463.08597f, 241.5229f},
            {99.11065f, 246.07971f},
    };

//    private static final float DROP_HEIGHT = 200;

    private static final int OPEN_FRAMES = 150;
    private static final int CLOSE_FRAMES = 100;

    private final Interpolator bounceInterpolator;
    private final Interpolator accelerateInterpolator;

    private final Paint paint;

    private final float canvasScale;
    private final int openFrames;
    private final int closeFrames;

    private int frame;
    private boolean closing;

    public SplashScreen(float canvasScale, float fpsScale) {
        this.canvasScale = canvasScale;
        openFrames = Math.round(OPEN_FRAMES * fpsScale);
        closeFrames = Math.round(CLOSE_FRAMES * fpsScale);

        bounceInterpolator = new BounceInterpolator();
        accelerateInterpolator = new AccelerateInterpolator();

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
    }

    public void update() {
        if (frame < (closing ? closeFrames : openFrames)) {
            frame++;
        }
    }

    public void close() {
        closing = true;
        frame = 0;
    }

    public boolean closing() {
        return closing;
    }

    public boolean closed() {
        return closing && frame == closeFrames;
    }

    public void render(Canvas canvas) {
        if (!closing) {
            float fraction = bounceInterpolator.getInterpolation((float) frame / openFrames);
            float yTrans = (fraction * canvas.getHeight() / 2) - (canvas.getHeight() / 2);

            canvas.drawPath(toPath(LETTER_P_OUTER, yTrans), paint);
            canvas.drawPath(toPath(LETTER_P_INNER, yTrans), paint);
            canvas.drawPath(toPath(LETTER_O_OUTER, yTrans), paint);
            canvas.drawPath(toPath(LETTER_O_INNER, yTrans), paint);
            canvas.drawPath(toPath(LETTER_N, yTrans), paint);
            canvas.drawPath(toPath(LETTER_G_OUTER, yTrans), paint);
            canvas.drawPath(toPath(LETTER_G_INNER, yTrans), paint);
            canvas.drawPath(toPath(BASELINE, 0), paint);
        } else {
            float fraction = accelerateInterpolator.getInterpolation((float) frame / closeFrames);
            float yTrans = fraction * canvas.getHeight();

            canvas.drawPath(toPath(LETTER_P_OUTER, yTrans), paint);
            canvas.drawPath(toPath(LETTER_P_INNER, yTrans), paint);
            canvas.drawPath(toPath(LETTER_O_OUTER, yTrans), paint);
            canvas.drawPath(toPath(LETTER_O_INNER, yTrans), paint);
            canvas.drawPath(toPath(LETTER_N, yTrans), paint);
            canvas.drawPath(toPath(LETTER_G_OUTER, yTrans), paint);
            canvas.drawPath(toPath(LETTER_G_INNER, yTrans), paint);
            canvas.drawPath(toPath(BASELINE, yTrans), paint);
        }
    }

    private Path toPath(float[][] coordinates, float yTrans) {
        Path path = new Path();
        for (int i=0; i<coordinates.length; i++) {
            float x = coordinates[i][0] * canvasScale;
            float y = coordinates[i][1] * canvasScale + yTrans;
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        path.close();
        return path;
    }

}
