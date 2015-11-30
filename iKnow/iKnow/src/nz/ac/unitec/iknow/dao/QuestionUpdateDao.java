package nz.ac.unitec.iknow.dao;

import java.util.List;

import nz.ac.unitec.iknow.bean.QuestionBean;

/**
 * Update the database
 * @author Zhiming Zhang(1446774) Yang Zhang(1453733) Ming Kong(1449315)
 *
 */
public class QuestionUpdateDao {

	private DBHelper dbh;
	
	public QuestionUpdateDao(){
		this.dbh = DBHelper.getDBHelper();
	}
	
	public void updateQuestion(List<QuestionBean> questionList) {
		//Clear database to prevent duplicate
		this.dbh.clearTestRecord();
		//Insert questions into DB
		this.dbh.insertQuestions(questionList);
	}
	
	public void setDbh(DBHelper dbh) {
		this.dbh = dbh;
	}

	public DBHelper getDbh() {
		return dbh;
	}
}
