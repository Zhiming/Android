package ac.nz.unitec.mars_rover;

import java.util.LinkedList;

import ac.nz.untiec.model.LogicModel;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.view.View.OnTouchListener;

public class MainActivity extends Activity {

	private LogicModel logicModel;
	private LinkedList<Button> btnList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		logicModel = (LogicModel) findViewById(R.id.logicModel);

		btnList = new LinkedList<Button>();

		Button btnUp = (Button) findViewById(R.id.btnUp);
		Button btnLeft = (Button) findViewById(R.id.btnLeft);
		Button btnRight = (Button) findViewById(R.id.btnRight);

		btnList.add(btnUp);
		btnList.add(btnLeft);
		btnList.add(btnRight);

		regBtnLongEvet();
	}

	private void regBtnLongEvet() {
		for (Button btn : btnList) {
			btn.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View view, MotionEvent event) {
					directControl(view, event);
					return true;
				}
			});
		}
	}

	private void directControl(View view, MotionEvent event) {
		logicModel.directControl(view, event);
	}

	public void reset(View view) {
		logicModel.reset();
	}

}
