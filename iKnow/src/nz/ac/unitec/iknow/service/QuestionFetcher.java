package nz.ac.unitec.iknow.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;

import org.json.JSONException;

import nz.ac.unitec.iknow.bean.QuestionBean;
import nz.ac.unitec.iknow.dao.QuestionUpdateDao;
import nz.ac.unitec.iknow.model.Communicator;
import nz.ac.unitec.iknow.model.JsonParser;
import nz.ac.unitec.iknow.model.Parser;
import nz.ac.unitec.iknow.model.QuestionUpdateFailException;

/**
 * Get latest questions from server
 * @author Zhiming Zhang(1446774) Yang Zhang(1453733) Ming Kong(1449315)
 *
 */
public class QuestionFetcher {

	private Communicator communicator;

	public QuestionFetcher() {
		this.communicator = new Communicator();
	}

	public boolean getLatestQuestions(String url) throws QuestionUpdateFailException, JSONException {
		boolean flag = false;
		// connect to server and download question in json
		try {
			InputStream is = communicator.getQuestions(url);
			if (is != null) {
				// parse json
				Parser parser = new JsonParser();
				List<QuestionBean> questionList = parser.parse(is);
				// write into db
				QuestionUpdateDao qud = new QuestionUpdateDao();
				qud.updateQuestion(questionList);
				flag = true;
			}
		} catch (MalformedURLException mfe) {
			throw new QuestionUpdateFailException("Fail to connect to server");
		} catch (IOException e) {
			throw new QuestionUpdateFailException("Fail to connect to server");
		}catch (JSONException e) {
			throw new JSONException("");
		}
		return flag;
	}
	
	

}
