package sample.pong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Score {

    private static final float SCORE_TEXT_SIZE = 64;

    private static final float SCORE_MARGIN = 50;
    private static final float PLAYER_SCORE_Y = 80;

    private final Paint paint;

    private final float yScore;
    private float scoreMargin;
    private float canvasWidth;

    private int playerScore;
    private int gameScore;

    private float playerScoreWidth;

    public Score(float canvasWidth, float scale) {
        this.canvasWidth = canvasWidth;

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setTextSize(SCORE_TEXT_SIZE * scale);

        yScore = PLAYER_SCORE_Y * scale;
        scoreMargin = SCORE_MARGIN * scale;

        playerScoreWidth = paint.measureText(playerScore+"");
    }

    public void playerScore() {
        playerScore++;
        playerScoreWidth = paint.measureText(playerScore+"");
    }

    public void gameScore() {
        gameScore++;
    }

    public void render(Canvas canvas) {
        canvas.drawText(playerScore+"", canvasWidth / 2 - scoreMargin - playerScoreWidth, yScore, paint);
        canvas.drawText(gameScore+"", canvasWidth / 2 + scoreMargin, yScore, paint);
    }

}
