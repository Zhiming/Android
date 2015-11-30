package ac.nz.unitec.fingerpaint;

import ac.nz.unitec.constants.DrawingConstants;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.PorterDuff;

public class DrawingView extends View {

	private Path drawPath;
	private Paint drawPaint, canvasPaint;
	private int paintColor = 0xFFFF0000;
	private String drawingShape = DrawingConstants.CIRCLE;
	private Canvas drawCanvas;
	private Bitmap canvasBitmap;

	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupDrawing();
	}

	private void setupDrawing() {
		drawPath = new Path();
		drawPaint = new Paint();
		drawPaint.setColor(paintColor);

		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(20);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		canvasPaint = new Paint(Paint.DITHER_FLAG);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		drawCanvas = new Canvas(canvasBitmap);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		canvas.drawPath(drawPath, drawPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float touchX = event.getX();
		float touchY = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			drawPath.moveTo(touchX, touchY);
			break;
		case MotionEvent.ACTION_MOVE:

			break;
		case MotionEvent.ACTION_UP:
			if (drawingShape.equalsIgnoreCase(DrawingConstants.CIRCLE)) {
				drawPath.addCircle(touchX, touchY,
						DrawingConstants.DEFAULT_CIRCLE_RADIUS,
						Path.Direction.CW);
			} else if (drawingShape.equalsIgnoreCase(DrawingConstants.TRANGLE)) {
				addTrangle(touchX, touchY, drawPath);
			} else if (drawingShape.equalsIgnoreCase(DrawingConstants.SQUARE)) {
				drawPath.addRect(touchX, touchY, touchX
						+ DrawingConstants.DEFAULT_SQUARE_EDGE_LENGTH, touchY
						+ DrawingConstants.DEFAULT_SQUARE_EDGE_LENGTH,
						Path.Direction.CW);
			}
			drawCanvas.drawPath(drawPath, drawPaint);
			drawPath.reset();
			break;
		default:
			return false;
		}
		// invalidate() calls onDraw()
		invalidate();
		return true;
	}

	public void setShape(String newShape) {
		invalidate();
		this.drawingShape = newShape;
	}

	public void setColor(String newColor) {
		invalidate();
		paintColor = Color.parseColor(newColor);
		drawPaint.setColor(paintColor);
	}

	// Clear canvas and update display
	public void startNew() {
		drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
		invalidate();
	}
	
	
	private void addTrangle(float x, float y, Path path){
		path.lineTo(x, y + DrawingConstants.DEFAULT_TRANGLE_EDGE);
		path.lineTo(x + DrawingConstants.DEFAULT_TRANGLE_EDGE, y);
		path.close();
	}
	
}
