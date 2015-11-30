package nz.ac.unitec.iknow;

import java.util.ArrayList;

import nz.ac.unitec.iknow.bean.KnowledgeBean;
import nz.ac.unitec.iknow.dao.DBHelper;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Activity for the function of Study
 * @author Zhiming Zhang(1446774) Yang Zhang(1453733) Ming Kong(1449315)
 *
 */
@SuppressLint("ShowToast")
public class StudyActivity extends Activity implements OnClickListener {
	private int currentNum;
	private int maxNum;
	private boolean finishedFlg;
	private ArrayList<KnowledgeBean> list = new ArrayList<KnowledgeBean>();
	private Button add;
	private Button nextone;
	private Button beforeone;
	private TextView subject;
	private TextView content;
	private DBHelper mDBHelper = null;

	/**
	 * Called when study activity starts
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.studyword);
		currentNum = 0;
		finishedFlg = false;
		mDBHelper = DBHelper.getDBHelper(StudyActivity.this, "wordDataBase", null, 1);
		assert (mDBHelper != null);
		//		list = mDBHelper.QueryAllWord();
		list = mDBHelper.SelectAllKnowledge();
		initWidgets();
		maxNum = list.size();
		subject.setText(list.get(currentNum).getKnowledgeId() + ". "
				+ list.get(currentNum).getSubject());
		content.setText(
				//				"[" + list.get(currentnum).getWordPronunciation() + "]"
				//				+ "\n" + list.get(currentnum).getWordType() + "  " +
				list.get(currentNum).getContent() + "\n");

		UpdateView();
	}

	/**
	 * Initialize the widgets
	 */
	private void initWidgets() {
		this.add = (Button) this.findViewById(R.id.add);
		add.setOnClickListener(this);
		this.content = (TextView) this.findViewById(R.id.info);
		this.beforeone = (Button) this.findViewById(R.id.beforeone);
		beforeone.setOnClickListener(this);
		this.nextone = (Button) this.findViewById(R.id.nextone);
		nextone.setOnClickListener(this);
		this.subject = (TextView) this.findViewById(R.id.spelling);
		Typeface tf = Typeface
				.createFromAsset(getAssets(), "fonts/SEGOEUI.TTF");
		content.setTypeface(tf);
		DisplayMetrics dm = new DisplayMetrics();
		dm = getApplicationContext().getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		add.setWidth(screenWidth / 3);
		beforeone.setWidth(screenWidth / 3);
		nextone.setWidth(screenWidth / 3);

	}

	/**
	 * On click listener, handler user's actions
	 */
	public void onClick(View v) {
		this.UpdateView();
		if (v == nextone) {
			if (currentNum < maxNum - 1) {
				currentNum++;
				Log.i("aa", " " + currentNum);
				if((currentNum) == maxNum - 1) {
					finishedFlg = true;
				}
				this.UpdateView();
			}

		} else if (v == add) {
			final StudyActivity _this = this;
			Dialog dialog = new AlertDialog.Builder(this)
					.setTitle("Add to review list")
					.setMessage("Do you want to add this to review list?")
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							KnowledgeBean bean = new KnowledgeBean();
							bean.setKnowledgeId(list.get(currentNum).getKnowledgeId());
							bean.setSubject(list.get(currentNum).getSubject());
							bean.setType(list.get(currentNum).getType());
							bean.setContent(list.get(currentNum).getContent());
							bean.setListNum(list.get(currentNum).getListNum());
							if(mDBHelper.addAttentionKnowledge(bean)){
								new  AlertDialog.Builder(_this).setTitle("Notice" ).setMessage("Added")  
								.setPositiveButton("Confirm" ,  null ).show();  
							}else{
								new  AlertDialog.Builder(_this).setTitle("Notice" ).setMessage("The entry has existed")  
								.setPositiveButton("Confirm" ,  null ).show();  
							}
						}
					})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							/* User clicked OK so do some stuff */
						}
					}).create();
			dialog.show();

		} else if (v == beforeone) {
			currentNum--;
			this.UpdateView();
		}
	}
	
	/**
	 * Back button listener
	 */

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (finishedFlg) {
				Dialog dialog = new AlertDialog.Builder(this)
						.setTitle("Finished")
						.setMessage("Are you sure to leave?")
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								/* User clicked OK so do some stuff */

								finish();
								Intent intent = new Intent();
								intent.setClass(StudyActivity.this,
										MainActivity.class);
								startActivity(intent);
							}
						})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								/* User clicked OK so do some stuff */
							}
						}).create();
				dialog.show();
			} else {

				Dialog dialog = new AlertDialog.Builder(this)
						.setTitle("Not finished")
						.setMessage("Study is not finished, are you sure to leave?")
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								/* User clicked OK so do some stuff */

								finish();
								Intent intent = new Intent();
								intent.setClass(StudyActivity.this,
										MainActivity.class);
								startActivity(intent);
							}
						})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								/* User clicked OK so do some stuff */
							}
						}).create();
				dialog.show();
			}
		}

		return true;
	}

	/**
	 * Update the view when user studies
	 */
	private void UpdateView() {
		if (currentNum == 0) {
			beforeone.setEnabled(false);
		} else if (currentNum > 0) {
			beforeone.setEnabled(true);
		}

		if (currentNum < maxNum) {
			//			spelling.setText(list.get(currentnum).getWordID() + "."
			//					+ list.get(currentnum).getWordName());
			//			info.setText("[" + list.get(currentnum).getWordPronunciation()
			//					+ "]" + "\n" + list.get(currentnum).getWordType() + "  "
			//					+ list.get(currentnum).getWordMeaning() + "\n");
			subject.setText(list.get(currentNum).getKnowledgeId() + ". "
					+ list.get(currentNum).getSubject());
			content.setText(list.get(currentNum).getContent() + "\n");
		}
		if (currentNum > maxNum) {

		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
