package nz.ac.unitec.iknow.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import nz.ac.unitec.iknow.bean.QuestionBean;

/**
 * Read questions using Json
 * @author Zhiming Zhang(1446774) Yang Zhang(1453733) Ming Kong(1449315)
 *
 */
public class JsonParser implements Parser {

	@Override
	public List<QuestionBean> parse(InputStream inStream) throws JSONException {
		List<QuestionBean> questionList = null;
		byte[] data = read(inStream);
		if(data.length > 0){
			questionList = new ArrayList<QuestionBean>();
			String json = new String(data);
			Log.d(null, "1");
			JSONArray array = new JSONArray(json);
			Log.d(null, "2");
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				QuestionBean q = new QuestionBean();
				q.setQuestion(jsonObject.getString("question"));
				q.setType(jsonObject.getString("type"));
				List<String> options = new ArrayList<String>();
				for(int j = 1; j < 5; j++){
					String index = "option" + j;
					options.add(jsonObject.getString(index));
				}
				q.setOptions(options);
				questionList.add(q);
			}
			Log.d(null, "3");
		}
		return questionList;
	}

	private byte[] read(InputStream inputStream) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int len = 0;
		try {
			while ((len = inputStream.read(b)) != -1) {
				outputStream.write(b);
			}
		} catch (IOException e) {
			Log.d(null, e.getMessage());
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				Log.d(null, e.getMessage());
			}
		}
		return outputStream.toByteArray();
	}

}
