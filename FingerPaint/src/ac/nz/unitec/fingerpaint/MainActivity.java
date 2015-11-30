package ac.nz.unitec.fingerpaint;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ac.nz.unitec.constants.DrawingConstants;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity implements OnClickListener {

	private DrawingView drawView;
	private ImageButton currPaint;
	private ImageView currShape;
	private Button btnReset, btnExit, btnSave;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		drawView = (DrawingView) findViewById(R.id.drawing_view);

		GridLayout paintLayout = (GridLayout) findViewById(R.id.color_grid);
		// Default color is red
		currPaint = (ImageButton) paintLayout.getChildAt(0);
		currPaint.setImageDrawable(getResources().getDrawable(
				R.drawable.paint_pressed));
		LinearLayout shapeLayout = (LinearLayout) findViewById(R.id.shape_area);

		// Default shape is circle
		currShape = (ImageView) shapeLayout.getChildAt(2);
		currShape.setBackground(getResources().getDrawable(
				R.drawable.shape_border));

		btnReset = (Button) findViewById(R.id.reset_btn);
		btnReset.setOnClickListener(this);
		btnSave = (Button) findViewById(R.id.save_btn);
		btnSave.setOnClickListener(this);
		btnExit = (Button) findViewById(R.id.exit_btn);
		btnExit.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void paintClicked(View view) {
		if (view != currPaint) {
			ImageButton imgView = (ImageButton) view;
			String color = view.getTag().toString();
			drawView.setColor(color);
			imgView.setImageDrawable(getResources().getDrawable(
					R.drawable.paint_pressed));
			currPaint.setImageDrawable(getResources().getDrawable(
					R.drawable.paint_original));

			currPaint = (ImageButton) view;
		}
	}

	public void shapeClicked(View view) {
		if (view != currShape) {
			ImageView ib = (ImageView) view;
			ib.setBackground(getResources()
					.getDrawable(R.drawable.shape_border));
			currShape.setBackground(null);
			drawView.setShape(ib.getTag().toString());
			currShape = ib;
		}
	}

	@Override
	public void onClick(View view) {
		// Reset
		if (view.getId() == R.id.reset_btn) {
			AlertDialog.Builder resetDialog = new AlertDialog.Builder(this);
			resetDialog.setTitle("Reset");
			resetDialog
					.setMessage("Reset drawing?");
			resetDialog.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							drawView.startNew();
							dialog.dismiss();
						}
					});
			resetDialog.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			resetDialog.show();
		} else if (view.getId() == R.id.save_btn) {
			// Save images
			AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
			saveDialog.setTitle("Save drawing");
			saveDialog.setMessage("Save drawing to device Gallery?");
			saveDialog.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// save images
							String imgSaved = savePicture(drawView);
							if (imgSaved != null) {
								Toast savedToast = Toast.makeText(
										getApplicationContext(),
										"Drawing saved to Gallery!",
										Toast.LENGTH_SHORT);
								savedToast.show();
							} else {
								Toast unsavedToast = Toast.makeText(
										getApplicationContext(),
										"Oops! Image could not be saved.",
										Toast.LENGTH_SHORT);
								unsavedToast.show();
							}
							drawView.destroyDrawingCache();
						}
					});
			saveDialog.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			saveDialog.show();
		} else if (view.getId() == R.id.exit_btn) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder.setTitle("Exit Application?");
			alertDialogBuilder
					.setMessage("Click yes to exit!")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									MainActivity.this.finish();
									System.exit(0);
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});

			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}
	}

	private String savePicture(View view) {
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		File myappDir = new File(Environment.getExternalStorageDirectory(),
				"zzm");
		if (myappDir.exists() && myappDir.isFile()) {
			myappDir.delete();
		}
		if (!myappDir.exists()) {
			myappDir.mkdir();
		}
		String fileName = System.currentTimeMillis() + ".png";
		File file = new File(myappDir, fileName);
		if (file.exists()) {
			file.delete();
		}
		try {
			FileOutputStream fos = new FileOutputStream(file);
			if (bitmap != null) {
				bitmap.compress(CompressFormat.PNG, 100, fos);
			}
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			return MediaStore.Images.Media.insertImage(getContentResolver(),
					file.getAbsolutePath(), fileName, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

}
