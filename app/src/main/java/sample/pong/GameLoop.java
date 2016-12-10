package sample.pong;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameLoop implements Runnable {

    /**
     * Target frames per second.
     */
    private final static int FPS = 60;

    /**
     * Holds a display surface and provides access to {@link Canvas} for drawing.
     */
    private final SurfaceHolder surfaceHolder;

    /**
     * Worker thread.
     */
    private final Thread thread;

    /**
     * Game state.
     */
    private final Game game;

    /**
     * Effective frames per second.
     */
    private final int fps;

    /**
     * Target frame period in milliseconds.
     */
    private final int framePeriod;

    /**
     * Loop paused.
     */
    private boolean paused;

    public GameLoop(SurfaceHolder surfaceHolder, Game game, float fpsScale) {
        this.surfaceHolder = surfaceHolder;
        this.thread = new Thread(this);
        this.paused = true;
        this.game = game;

        fps = Math.round(fpsScale * FPS);
        framePeriod = 1000 / fps;
    }

    public synchronized void start() {
        thread.start();
    }

    public synchronized void resume() {
        paused = false;
        notify();
    }

    public synchronized void pause() {
        paused = true;
    }

    private synchronized void await() {
        while(paused) {
            try {
                wait();
            } catch (InterruptedException ignore) {}
        }
    }

    private void drawFrame() {
        Canvas canvas = surfaceHolder.lockCanvas();

        // surface not ready
        if (canvas == null) {
            return;
        }

        try {
            // update game state
            game.update(canvas.getWidth(), canvas.getHeight());

            // render game
            game.render(canvas);
        } finally {
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public void run() {
        int frame = 0;
        long time = System.currentTimeMillis();
        long iterationTimeSum = 0;

        while (true) {
            // wait for pause to clear
            await();

            long beginTime = System.currentTimeMillis();

            drawFrame();

            // calculate how long did the cycle take
            long timeDiff = System.currentTimeMillis() - beginTime;
            iterationTimeSum += timeDiff;

            // calculate sleep time
            long sleepTime = framePeriod - timeDiff;

            // sleep for remainder of frame period
            if (sleepTime > 0) {
                try {Thread.sleep(sleepTime); } catch (InterruptedException ignore) {}
            }

            frame++;

            if (frame % FPS == 0) {
                long now = System.currentTimeMillis();
                long elapsed = now - time;
                float fps = (FPS / (float)elapsed) * 1000.0f;
                float iterationTime = iterationTimeSum / FPS;
                Log.i("GameLoop", "fps-target: " + FPS + ", fps-observed: " + fps + ", iteration-time: " + iterationTime + ", frame-period: " + framePeriod);
                frame = 0;
                iterationTimeSum = 0;
                time = now;
            }
        }
    }
}
