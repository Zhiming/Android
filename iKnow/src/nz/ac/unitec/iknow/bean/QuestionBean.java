package nz.ac.unitec.iknow.bean;

import java.util.List;

/**
 * DAO layer using SQLiteOpenHelper
 * 
 * @author Zhiming Zhang(1446774) Yang Zhang(1453733) Ming Kong(1449315)
 *
 */
public class QuestionBean {
	// Question ID
	private String questionID;
	// Question
	private String question;
	// Question type
	private String type;
	// Options for question
	private List<String> options;

	public String getQuestionID() {
		return questionID;
	}

	public void setQuestionID(String questionID) {
		this.questionID = questionID;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

	@Override
	public String toString() {
		return "Question [questionID=" + questionID + ", question=" + question
				+ ", type=" + type + ", options=" + options + "]";
	}
}
