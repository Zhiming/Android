package nz.ac.unitec.iknow;

import java.util.HashSet;

import nz.ac.unitec.iknow.bean.QuestionBean;
import nz.ac.unitec.iknow.dao.DBHelper;
import nz.ac.unitec.iknow.service.TestService;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity for the function of test
 * @author Zhiming Zhang(1446774) Yang Zhang(1453733) Ming Kong(1449315)
 *
 */
public class TestActivity extends Activity {
	private TextView question;
	private RadioGroup options;
	private RadioButton option1, option2, option3, option4;
	private int currentNum;
	private int currentQuestionId = 1;
	private int grade;
	private int correctAnswerIndex = -1;
	//Button can only be clicked when one answer is selected
	//	boolean optionStatusCheck = false;
	private DBHelper md;
	private Button nextBtn, overBtn;
	//	private Button addAttentionBtn;
	private TestService ts;
	//Total number of questions
	private int amountOfQuestions = -1;
	//Questions have been examed
	private HashSet<Integer> examedQuestionID;

	/**
	 * Called when test activity starts
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.test);

		md = DBHelper.getDBHelper(TestActivity.this, "wordDataBase", null, 1);
		currentNum = 1;
		ts = new TestService();
		examedQuestionID =  new HashSet<Integer>();
		question = (TextView) findViewById(R.id.WordName);

		options = (RadioGroup) findViewById(R.id.WordMeanning);
		option1 = (RadioButton) findViewById(R.id.WordMeaning1);
		option2 = (RadioButton) findViewById(R.id.WordMeaning2);
		option3 = (RadioButton) findViewById(R.id.WordMeaning3);
		option4 = (RadioButton) findViewById(R.id.WordMeaning4);

		nextBtn = (Button) findViewById(R.id.NextButton);	//next
		overBtn = (Button) findViewById(R.id.OverButton);	//end

		//Get random questions
		amountOfQuestions = md.getAmountOfQuestions();

		getAmountQuestions(amountOfQuestions);

		//If there is no question, message user to update the db
		fetchQuestion(currentQuestionId);

		Toast.makeText(getApplicationContext(), "There are total of " + amountOfQuestions + " questions, each question is worth 5 mark",
				Toast.LENGTH_LONG).show();
		grade = 0;

		// Not allowed to submit unless user select an answer
		nextBtn.setEnabled(false);

		options.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// Last question, not allowed to click next
				if (currentNum < amountOfQuestions) {
					nextBtn.setEnabled(true);
				}
			}
		});

		nextBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkQuestion();
				currentNum++;
				nextBtn.setEnabled(false);
				if (currentNum <= amountOfQuestions) {
					option1.setChecked(false);
					option2.setChecked(false);
					option3.setChecked(false);
					option4.setChecked(false);
					fetchQuestion(currentNum);
				} 
			}
		});

		overBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				checkQuestion();
				Dialog dialog = new AlertDialog.Builder(TestActivity.this)
						.setTitle("Finish exam?")
						.setMessage("Current corrected " + grade / 5 + "/"
								+ currentNum + " questions£¬score is " + grade)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								/* User clicked OK so do some stuff */

								finish();
								Intent intent = new Intent();
								intent.setClass(TestActivity.this,
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
		});

	}

	/**
	 * Get questions from DB, if there are more than five questions in DB, set number to five
	 * @param amountOfQuestions
	 */
	private void getAmountQuestions(int amountOfQuestions) {
		if(amountOfQuestions < 0){
			Dialog dialog = new AlertDialog.Builder(TestActivity.this)
					.setTitle("Study")
					.setMessage("No question, please update the database")
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							Intent intent = new Intent();
							intent.setClass(TestActivity.this,
									MainActivity.class);
							TestActivity.this.startActivity(intent);

						}
					}).create();
			dialog.show();
		}

		if(amountOfQuestions > 5){
			amountOfQuestions = 5;
		}
	}

	/**
	 * Fetch questions from DB
	 * @param currentWordId
	 */
	private void fetchQuestion(int currentWordId) {
		QuestionBean q = md.getQuestion(currentWordId + "");

		question.setText(currentNum + ". " + q.getQuestion());

		int trueAnswer = (int) (1 + Math.random() * 4);
		correctAnswerIndex = trueAnswer - 1;

		if (trueAnswer == 1) {
			option1.setText((CharSequence) (q.getOptions().get(0)));
			option2.setText((CharSequence) (q.getOptions().get(1)));
			option3.setText((CharSequence) (q.getOptions().get(2)));
			option4.setText((CharSequence) (q.getOptions().get(3)));

		} else if (trueAnswer == 2) {
			option1.setText((CharSequence) (q.getOptions().get(1)));
			option2.setText((CharSequence) (q.getOptions().get(0)));
			option3.setText((CharSequence) (q.getOptions().get(2)));
			option4.setText((CharSequence) (q.getOptions().get(3)));
		} else if (trueAnswer == 3) {
			option1.setText((CharSequence) (q.getOptions().get(1)));
			option2.setText((CharSequence) (q.getOptions().get(2)));
			option3.setText((CharSequence) (q.getOptions().get(0)));
			option4.setText((CharSequence) (q.getOptions().get(3)));
		} else if (trueAnswer == 4) {
			option1.setText((CharSequence) (q.getOptions().get(1)));
			option2.setText((CharSequence) (q.getOptions().get(3)));
			option3.setText((CharSequence) (q.getOptions().get(2)));
			option4.setText((CharSequence) (q.getOptions().get(0)));
		}
	}

	/**
	 * Check if the answer is correct
	 */
	private void checkQuestion() {
		View radioButton = options.findViewById(options
				.getCheckedRadioButtonId());

		if (options.indexOfChild(radioButton) == correctAnswerIndex) {

			grade = grade + 5;
			Toast.makeText(getApplicationContext(), "Congratulation, your answer is correct.",
					Toast.LENGTH_SHORT).show();
		} else {
			grade = grade + 0;
			Toast.makeText(getApplicationContext(), "Wrong answer!",
					Toast.LENGTH_SHORT).show();
		}
	}
}
