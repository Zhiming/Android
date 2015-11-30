package ac.nz.untiec.model;

import ac.nz.unitec.constants.Direction;
import ac.nz.unitec.constants.PhysicalConstants;
import ac.nz.unitec.mars_rover.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class LogicModel extends SurfaceView implements Runnable,
		SurfaceHolder.Callback {

	private float xcor[] = { 0, 200, 190, 218, 260, 275, 298, 309, 327, 336,
			368, 382, 448, 462, 476, 498, 527, 600, 600, 0, 0 };

	private float ycor[] = { 616, 540, 550, 605, 605, 594, 530, 520, 520, 527,
			626, 636, 636, 623, 535, 504, 481, 481, 750, 750, 616 };

	private boolean gameover = false;
	private Paint paint = new Paint();
	private Path path;
	private double t = PhysicalConstants.INITIAL_TIME;
	private float x, y;
	private Thread main;
	private int width = 0;
	private Bitmap rover;
	private int roverWidth = 0;
	private int roverHeight = 0;
	private Bitmap explosion;
	private float widthRatio = 0;
	private float heightRatio = 0;
	private Bitmap mars;
	private int screenWidth;
	private Bitmap thrustImg;
	private Bitmap mainFlameImg;
	// counterclockwise coresponding left, main and right thrusters
	private boolean[] directions = { false, false, false };
	private int explosionBcm = 0;
	private int successBcm = 0;
	private int thrusterBcm = 0;
	private SoundPool sp;
	private Context contx;

	public LogicModel(Context context) {
		super(context);
		this.contx = context;
		init();
	}

	public LogicModel(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.contx = context;
		init();
	}

	public LogicModel(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.contx = context;
		init();
	}

	@SuppressWarnings("deprecation")
	private void loadSounds(){
		sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		explosionBcm = sp.load(contx, R.raw.explosion_bmc, 1);
		successBcm = sp.load(contx, R.raw.success_bmc, 1);
		thrusterBcm = sp.load(contx, R.raw.thruster_bmc, 1);
	}
	
	private void playSound(int soundNum){
		if(soundNum != 0){
			sp.play(soundNum, 1, 1, 0, 0, 1);
		}
	}
	
	/**
	 * Draw terrain
	 */
	private void init() {
		screenAdjust();
		path = new Path();
		for (int i = 0; i < xcor.length; i++) {
			path.lineTo(xcor[i], ycor[i]);
		}
		getHolder().addCallback(this);
		rover = BitmapFactory.decodeResource(getResources(), R.drawable.rover);
		mars = BitmapFactory.decodeResource(getResources(), R.drawable.mars);
		thrustImg = BitmapFactory.decodeResource(getResources(),
				R.drawable.thruster);
		mainFlameImg = BitmapFactory.decodeResource(getResources(),
				R.drawable.main_flame);
		roverWidth = rover.getWidth();
		roverHeight = rover.getHeight();
		explosion = BitmapFactory.decodeResource(getResources(),
				R.drawable.explosion);
		loadSounds();
	}

	/**
	 * calculate the ratio between the current device screen size and the
	 * initial array
	 */
	private void screenAdjust() {
		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();

		screenWidth = dm.widthPixels;
		widthRatio = (float) screenWidth / (float) 600;

		int screenHeight = dm.heightPixels;
		heightRatio = (float) screenHeight / (float) 750;

		for (int i = 0; i < xcor.length; i++) {
			xcor[i] = xcor[i] * widthRatio;
			ycor[i] = ycor[i] * heightRatio;
		}

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		main = new Thread(this);
		if (main != null)
			main.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		while (retry) {
			try {
				main.join();
				retry = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		width = w;
		x = width / 2;
	}

	private void drawTerr(Paint paint) {
		BitmapShader bs = new BitmapShader(mars, Shader.TileMode.REPEAT,
				Shader.TileMode.REPEAT);
		paint.setShader(bs);
	}

	@Override
	public void run() {
		while (true) {
			while (!gameover) {
				Canvas canvas = null;
				SurfaceHolder holder = getHolder();
				synchronized (holder) {
					canvas = holder.lockCanvas();
					canvas.drawColor(Color.BLACK);
					drawTerr(paint);
					canvas.drawPath(path, paint);
					changeYDirection();
					changeXDirection();
					drawFlame(canvas);
					playSounds();
					statusCheck(canvas);
				}
				try {
					Thread.sleep(PhysicalConstants.REFRESH_RATE);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (canvas != null) {
						holder.unlockCanvasAndPost(canvas);
					}
				}
			}
		}
	}
	
	private void playSounds(){
		//play thruster
		for(boolean flag : directions){
			if(flag){
				playSound(thrusterBcm);
			}
		}
	}

	private void changeYDirection() {
		if (!directions[Direction.MAIN.direction()]) {
			y = (int) y + (int) ((0.5 * (PhysicalConstants.GRAVITY * t * t)));
			t = t + 0.01;
		} else {
			y = y - roverHeight / 5;
			if (y - 46 <= 0) {
				y = 46;
			}
		}
	}

	private void changeXDirection() {
		if (directions[Direction.LEFT.direction()]) {
			x = x + roverWidth / 10;
			if (x + roverWidth >= screenWidth) {
				x = 0;
			}
		} else if (directions[Direction.RIGHT.direction()]) {
			x = x - roverWidth / 10;
			if (x <= 0) {
				x = screenWidth - roverWidth;
			}
		}
	}

	/**
	 * based on thruster position to return flame
	 * @param direct
	 * @return
	 */
	private Bitmap getFlame(Direction direct){
		if(direct == Direction.MAIN){
			return mainFlameImg;
		}else{
			return thrustImg;
		}
	}
	
	private float[] getFlamePosition(Direction direct){
		if(direct == Direction.LEFT){
			return new float[]{x, y + roverHeight};
		}else if(direct == Direction.RIGHT){
			return new float[]{x + roverWidth - (roverWidth / 6), y + roverHeight};
		}else{
			return new float[]{x + roverWidth / 2 - (roverWidth / 6), y + roverHeight};
		}
	}
	
	private void drawFlame(Canvas canvas){
		for(int i = 0; i < directions.length; i++){
			if(directions[i]){
				drawSingleFlame(canvas, Direction.values()[i]);
			}
		}
	}
	
	private void drawSingleFlame(Canvas canvas, Direction direct){
		Bitmap flame = getFlame(direct);
		float[] cor = getFlamePosition(direct);
		canvas.drawBitmap(flame, cor[0],  cor[1], null);
	}
	
	/**
	 * Check whether it's still flying or landing
	 * 
	 * @param canvas
	 */
	private void statusCheck(Canvas canvas) {
		boolean bottomLeft = contains(xcor, ycor, x, y + roverHeight);
		boolean bottomRight = contains(xcor, ycor, x + roverWidth, y
				+ roverHeight);

		if (!bottomLeft && !bottomRight) {
			canvas.drawBitmap(rover, x, y, paint);
		} else {
			if (bottomLeft && bottomRight) {
				canvas.drawBitmap(rover, x, y, paint);
				playSound(successBcm);
			} else {
				paint.setColor(Color.BLACK);
				canvas.drawBitmap(explosion, x, y, null);
				playSound(explosionBcm);
				t = PhysicalConstants.INITIAL_TIME;
			}
			gameover = true;
		}
	}

	/**
	 * 
	 * @param xcor
	 * @param ycor
	 * @param x0
	 * @param y0
	 * @return odd - true even - false
	 */
	private boolean contains(float[] xcor, float[] ycor, double x0, double y0) {
		int crossings = 0;

		for (int i = 0; i < xcor.length - 1; i++) {
			float x1 = xcor[i];
			float x2 = xcor[i + 1];

			float y1 = ycor[i];
			float y2 = ycor[i + 1];

			float dy = y2 - y1;
			float dx = x2 - x1;

			double slope = 0;
			if (dx != 0) {
				slope = (double) dy / dx;
			}

			boolean cond1 = (x1 <= x0) && (x0 < x2);
			boolean cond2 = (x2 <= x0) && (x0 < x1);
			boolean above = (y0 < slope * (x0 - x1) + y1);

			if ((cond1 || cond2) && above) {
				crossings++;
			}
		}
		return (crossings % 2 != 0); // even or odd
	}

	public void reset() {
		gameover = false;
		x = width / 2;
		y = 0;
		t = PhysicalConstants.INITIAL_TIME;
	}

	public void directControl(View view, MotionEvent event) {
		steerDirection(view, event);
	}

	/**
	 * Register action for buttons
	 * 
	 * @param view
	 * @param event
	 */
	private void steerDirection(View view, MotionEvent event) {
		String tag = view.getTag().toString();

		if (tag.equalsIgnoreCase(getResources().getString(R.string.left))) {
			flyDirectionCheck(Direction.LEFT, event);

		} else if (tag.equalsIgnoreCase(getResources()
				.getString(R.string.right))) {
			flyDirectionCheck(Direction.RIGHT, event);

		} else {
			flyDirectionCheck(Direction.MAIN, event);
			if (event.getAction() == MotionEvent.ACTION_UP) {
				t = PhysicalConstants.INITIAL_TIME;
			}
		}
	}

	/**
	 * Make sure when a button is pressed and held, the spaceship still has the
	 * right response
	 * 
	 * @param flag
	 * @param event
	 */
	private void flyDirectionCheck(Direction direct, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			directions[direct.direction()] = true;
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			directions[direct.direction()] = false;
		}
	}

}
