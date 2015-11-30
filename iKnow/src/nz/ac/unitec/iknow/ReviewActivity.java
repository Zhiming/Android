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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Activity for the function of review
 * @author Zhiming Zhang(1446774) Yang Zhang(1453733) Ming Kong(1449315)
 *
 */
@SuppressLint("ShowToast")
public class ReviewActivity extends Activity implements OnClickListener{

	private int currentNum;
	private int maxNum;
	//	private ArrayList<Word> list = new ArrayList<Word>();
	private ArrayList<KnowledgeBean> list = new ArrayList<KnowledgeBean>();
	private Button delete;
	private Button nextone;
	private Button beforeone;
	private TextView subject;
	private TextView content;
	private DBHelper mDBHelper = null;

	/**
	 * Called when review activity starts
	 */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.review);
		currentNum=0;

		mDBHelper = DBHelper.getDBHelper(ReviewActivity.this, "wordDataBase", null, 1);
		assert(mDBHelper != null);
		list = mDBHelper.selectAllAttentionKnowledge();
		initWidgets();
		maxNum = list.size();
		if (0 == maxNum) {
			subject.setText("Message");
			content.setText(
					"There is no knowledge to review." + "\n");

		} else {
			subject.setText(list.get(currentNum).getKnowledgeId() + ". "
					+ list.get(currentNum).getSubject());
			content.setText(
					list.get(currentNum).getContent() + "\n");
		}
		UpdateView();
	}

	/**
	 * Initialize widgets
	 */
	private void initWidgets() {
		this.delete=(Button) this.findViewById(R.id.review_delete);
		delete.setOnClickListener(this);
		this.content=(TextView) this.findViewById(R.id.review_info);
		this.beforeone=(Button) this.findViewById(R.id.review_beforeone);
		beforeone.setOnClickListener(this);
		this.nextone=(Button) this.findViewById(R.id.review_nextone);
		nextone.setOnClickListener(this);
		this.subject=(TextView) this.findViewById(R.id.review_spelling);
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/SEGOEUI.TTF");
		content.setTypeface(tf);
		DisplayMetrics dm = new DisplayMetrics(); 
		dm = getApplicationContext().getResources().getDisplayMetrics(); 
		int screenWidth = dm.widthPixels; 
		delete.setWidth(screenWidth/3);
		beforeone.setWidth(screenWidth/3);
		nextone.setWidth(screenWidth/3);
	}

	/**
	 * On click listener, handler user's actions
	 */
	public void onClick(View v) {
		this.UpdateView();
		if (v==nextone){
			if(currentNum + 1 < maxNum){
				currentNum++;
				this.UpdateView();
			}

		}else if (v==delete){
			if(maxNum != 0){
				Dialog dialog = new AlertDialog.Builder(this)
						.setTitle("Review")
						.setMessage("Are you sure?")
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								/* User clicked OK so do some stuff */
								mDBHelper.deleteAttentionWord(list.get(currentNum).getKnowledgeId());
								list = mDBHelper.selectAllAttentionKnowledge();
								maxNum = list.size();
								UpdateView();
							}
						})
						.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								/* User clicked OK so do some stuff */
							}
						}).create();
				dialog.show();
			}


		}else if(v==beforeone){
			if (currentNum !=0) {
				currentNum--;
				this.UpdateView();
			}
		}
	}

	/**
	 * Back button listener
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Dialog dialog = new AlertDialog.Builder(this)
					.setTitle("Back")
					.setMessage("Back to main page?")
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							/* User clicked OK so do some stuff */

							finish();
							Intent intent = new Intent();
							intent.setClass(ReviewActivity.this, MainActivity.class);
							startActivity(intent);
						}
					})
					.setNegativeButton("No", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							/* User clicked OK so do some stuff */
						}
					}).create();
			dialog.show();
		}

		return true;
	}

	/**
	 * Update the view when user add or delete knowledge from review list
	 */
	private void UpdateView() {
		if(maxNum == 0) {
			subject.setText(null);
			content.setText(
					"There is no knowledge to review." + "\n");
			return;
		}

		if(currentNum==0){
			beforeone.setEnabled(false);
		}
		else if(currentNum>0){
			beforeone.setEnabled(true);
		}

		if (currentNum<maxNum){
			subject.setText(list.get(currentNum).getKnowledgeId() + ". "
					+ list.get(currentNum).getSubject());
			content.setText(
					list.get(currentNum).getContent() + "\n");    
		}else if(currentNum == maxNum){
			subject.setText(list.get(maxNum-1).getKnowledgeId() + ". "
					+ list.get(maxNum-1).getSubject());
			content.setText(
					list.get(maxNum-1).getContent() + "\n"); 
			currentNum--;
		}else{

		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
