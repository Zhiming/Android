package nz.ac.unitec.iknow;

import nz.ac.unitec.iknow.dao.DBHelper;
import nz.ac.unitec.iknow.model.QuestionUpdateFailException;
import nz.ac.unitec.iknow.service.QuestionFetcher;

import org.json.JSONException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * Main Activity 
 * @author Zhiming Zhang(1446774) Yang Zhang(1453733) Ming Kong(1449315)
 *
 */
public class MainActivity extends Activity implements OnClickListener{
	private DBHelper mDBHelper = null;

	private Button learnBu;							
	private Button reviewBu;		
	private Button testBu;			
	private Button btnUpdateQuestion;		

	/**
	 * Called when main activity starts
	 */
	View myView;
	@SuppressLint("InflateParams")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.setTitle("iKonw");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//This piece of code is used for handling network communication when starting it from main UI thread
		//The code is in the block when registering btnUpdateQuestion
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		LayoutInflater mInflater = LayoutInflater.from(this);
		myView = mInflater.inflate(R.layout.main, null);
		String DataBaseName = "wordDataBase";

		initWidgets();
		createDB(DataBaseName);
		setContentView(myView);
	}

	/**
	 * Create Database
	 * @param DB_NAME
	 */
	private void createDB(String DB_NAME) {
		mDBHelper = DBHelper.getDBHelper(MainActivity.this, DB_NAME, null, 1);
		assert(mDBHelper != null);
	}

	/**
	 * Initialize the widgets
	 */
	private void initWidgets() {
		this.learnBu=(Button) myView.findViewById(R.id.learn);
		learnBu.setOnClickListener(this);
		this.reviewBu=(Button) myView.findViewById(R.id.review);
		reviewBu.setOnClickListener(this);
		this.testBu=(Button) myView.findViewById(R.id.test);
		testBu.setOnClickListener(this);
		this.btnUpdateQuestion=(Button) myView.findViewById(R.id.update);
		btnUpdateQuestion.setOnClickListener(this);
		//initSpinner();
	}

	/**
	 * On click listener, handler user's actions
	 */
	public void onClick(View v) {
		if (v==reviewBu){
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, ReviewActivity.class);
			this.startActivity(intent);
		}
		if (v==testBu){
			Dialog dialog = new AlertDialog.Builder(MainActivity.this)
					.setTitle("Test")
					.setMessage("Are you ready to take the test?")
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							/* User clicked OK so do some stuff */
							Intent intent = new Intent();
							intent.setClass(MainActivity.this, TestActivity.class);
							MainActivity.this.startActivity(intent);

						}
					})
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							/* User clicked OK so do some stuff */
						}
					}).create();
			dialog.show();
		}

		if (v==this.btnUpdateQuestion){
			String resourcePath = this.getString(R.string.server_url) + this.getString(R.string.lastes_questions);
			QuestionFetcher updater = new QuestionFetcher();
			try {
				if(updater.getLatestQuestions(resourcePath)){
					// successfully updated
					Toast.makeText(getApplicationContext(), "Questions have been updated", Toast.LENGTH_SHORT).show();
				}else{
					// no update found
					Toast.makeText(getApplicationContext(), "Current questions are latest", Toast.LENGTH_SHORT).show();
				}
			} catch (QuestionUpdateFailException e) {
				// fail to connect to server
				Toast.makeText(getApplicationContext(), "Fail to connect to server", Toast.LENGTH_SHORT).show();
			} catch (JSONException e) {
				Toast.makeText(getApplicationContext(), "Fail to parse question set", Toast.LENGTH_SHORT).show();
			}
		}


		if (v==learnBu){
			Dialog dialog = new AlertDialog.Builder(MainActivity.this)
					.setTitle("Study")
					.setMessage("Are you sure to study?")
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							/* User clicked OK so do some stuff */
							Intent intent = new Intent();
							intent.setClass(MainActivity.this, StudyActivity.class);
							MainActivity.this.startActivity(intent);

						}
					})
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							/* User clicked OK so do some stuff */
						}
					}).create();
			dialog.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
