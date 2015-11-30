package nz.ac.unitec.iknow.dao;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import nz.ac.unitec.iknow.bean.KnowledgeBean;
import nz.ac.unitec.iknow.bean.QuestionBean;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DAO layer using SQLiteOpenHelper
 * 
 * @author Zhiming Zhang(1446774) Yang Zhang(1453733) Ming Kong(1449315)
 *
 */
public class DBHelper extends SQLiteOpenHelper {

	// public static final String wordListTable = "wordListTable";
	public static final String attentionWordListTable = "attentionWordListTable";
	public static final String wordID = "wordID";
	public static final String wordName = "wordName";
	public static final String wordType = "wordType";
	public static final String wordPronunciation = "wordPronunciation";
	public static final String wordMeaning = "wordMeaning";
	public static final String wordListNumber = "wordListNumber";

	public static final String knowledgeTable = "knowledgeTable";
	public static final String knowledgeID = "knowledgeID";
	public static final String knowledgeSubject = "knowledgeSubject";
	public static final String knowledgeType = "knowledgeType";
	public static final String knowledgeContent = "knowledgeContent";
	public static final String knowledgeListNumber = "knowledgeListNumber";

	public static final String attentionKnowledgeTable = "attentionKnowledgeTable";

	public static final String questionTable = "questionTable";
	public static final String questionID = "questionID";
	public static final String question = "question";
	public static final String questionType = "questionType";
	public static final String correctAnswer = "correct_answer";
	public static final String answer2 = "answer2";
	public static final String answer3 = "answer3";
	public static final String answer4 = "answer4";
	private SQLiteDatabase mDefaultWritableDatabase = null;
	private static DBHelper instance;

	private DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		this.getWritableDatabase();
	}

	/**
	 * Get DBHelper which is singleton object
	 * 
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 * @return DBHelper
	 */
	public static DBHelper getDBHelper(Context context, String name,
			CursorFactory factory, int version) {
		if (DBHelper.instance == null) {
			DBHelper.instance = new DBHelper(context, name, factory, version);
		}
		return DBHelper.instance;
	}

	/**
	 * Get DBHelper which is singleton object without parameters
	 * 
	 * @return DBHelper
	 */
	public static DBHelper getDBHelper() {
		return instance;
	}

	@Override
	public SQLiteDatabase getWritableDatabase() {
		final SQLiteDatabase db;
		if (mDefaultWritableDatabase != null) {
			db = mDefaultWritableDatabase;
		} else {
			db = super.getWritableDatabase();
		}
		return db;
	}

	/**
	 * close database
	 */
	public void Close() {
		this.getWritableDatabase().close();
	}

	/**
	 * Drop all tables
	 * 
	 * @param SQLiteDatabase
	 */
	public void dropTables(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + knowledgeTable);
		db.execSQL("DROP TABLE IF EXISTS " + attentionKnowledgeTable);
		db.execSQL("DROP TABLE IF EXISTS " + questionTable);
	}

	/**
	 * Create tables
	 * 
	 * @param SQLiteDatabase
	 */
	private void createTables(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + knowledgeTable + " ("
				+ knowledgeID + " INTEGER PRIMARY KEY," + knowledgeSubject
				+ " VARCHAR," + knowledgeType + " VARCHAR," + knowledgeContent
				+ " VARCHAR," + knowledgeListNumber + " VARCHAR)");

		db.execSQL("CREATE TABLE IF NOT EXISTS " + attentionKnowledgeTable
				+ " (" + knowledgeID + " INTEGER PRIMARY KEY,"
				+ knowledgeSubject + " VARCHAR," + knowledgeType + " VARCHAR,"
				+ knowledgeContent + " VARCHAR," + knowledgeListNumber
				+ " VARCHAR)");

		db.execSQL("CREATE TABLE IF NOT EXISTS " + questionTable + " ("
				+ questionID + " INTEGER PRIMARY KEY," + question + " VARCHAR,"
				+ questionType + " VARCHAR," + correctAnswer + " VARCHAR,"
				+ answer2 + " VARCHAR," + answer3 + " VARCHAR," + answer4
				+ " VARCHAR)");
	}

	/**
	 * Add knowledge
	 * 
	 * @param KnowledgeBean
	 */
	public void addKnowledge(KnowledgeBean bean) {
		ContentValues values = new ContentValues();

		values.put(DBHelper.knowledgeID, bean.getKnowledgeId());
		values.put(DBHelper.knowledgeSubject, bean.getSubject());
		values.put(DBHelper.knowledgeType, bean.getType());
		values.put(DBHelper.knowledgeContent, bean.getContent());
		values.put(DBHelper.knowledgeListNumber, bean.getKnowledgeId());
		this.getWritableDatabase()
				.insert(DBHelper.knowledgeTable, null, values);
	}

	public void addAttentionWord(String wordId, String wordName,
			String wordType, String wordPronunciation, String wordMeaning,
			String wordListNumber) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.wordID, wordId);
		values.put(DBHelper.wordName, wordName);
		values.put(DBHelper.wordType, wordType);
		values.put(DBHelper.wordPronunciation, wordPronunciation);
		values.put(DBHelper.wordMeaning, wordMeaning);
		values.put(DBHelper.wordListNumber, wordListNumber);
		this.getWritableDatabase().insert(DBHelper.attentionWordListTable,
				null, values);
	}

	/**
	 * Add knowledge into attention knowledge table
	 * 
	 * @param KnowledgeBean
	 * @return true: insert success false: already exists
	 */
	public boolean addAttentionKnowledge(KnowledgeBean bean) {
		boolean flag = false;
		if (attentionKnowledgeExistCheck(bean)) {
			ContentValues values = new ContentValues();
			values.put(DBHelper.knowledgeID, bean.getKnowledgeId());
			values.put(DBHelper.knowledgeSubject, bean.getSubject());
			values.put(DBHelper.knowledgeType, bean.getType());
			values.put(DBHelper.knowledgeContent, bean.getContent());
			values.put(DBHelper.knowledgeListNumber, bean.getListNum());
			this.getWritableDatabase().insert(DBHelper.attentionKnowledgeTable,
					null, values);
			flag = true;
		}
		return flag;
	}

	/**
	 * Check knowledge is already exist
	 * 
	 * @param KnowledgeBean
	 * @return true: not exist; false: already exist
	 */
	private boolean attentionKnowledgeExistCheck(KnowledgeBean bean) {
		String sql = "select * from attentionKnowledgeTable where knowledgeID = "
				+ bean.getKnowledgeId();
		SQLiteDatabase readOnlydb = this.getReadableDatabase();
		Cursor cursor = readOnlydb.rawQuery(sql, null);
		if (!cursor.moveToFirst()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Query all knowledge records
	 * 
	 * @return ArrayList<KnowledgeBean>
	 */
	public ArrayList<KnowledgeBean> SelectAllKnowledge() {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<KnowledgeBean> List = new ArrayList<KnowledgeBean>();
		Cursor cursor = db.rawQuery("select * from knowledgeTable", null);
		if (cursor.moveToFirst()) {

			do {
				KnowledgeBean word = new KnowledgeBean();
				word.setKnowledgeId(cursor.getString(0));
				word.setSubject(cursor.getString(1));
				word.setType(cursor.getString(2));
				word.setContent(cursor.getString(3));
				word.setListNum(cursor.getString(4));
				List.add(word);
			} while (cursor.moveToNext());
		}
		cursor.close();
//		db.close();
		return List;
	}

	/**
	 * Query all attention knowledge records
	 * 
	 * @return ArrayList<KnowledgeBean>
	 */
	public ArrayList<KnowledgeBean> selectAllAttentionKnowledge() {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<KnowledgeBean> lst = new ArrayList<KnowledgeBean>();

		Cursor cursor = db.rawQuery("select * from attentionKnowledgeTable",
				null);
		if (cursor.moveToFirst()) {

			do {
				KnowledgeBean bean = new KnowledgeBean();
				bean.setKnowledgeId(cursor.getString(0));
				bean.setSubject(cursor.getString(1));
				bean.setType(cursor.getString(2));
				bean.setContent(cursor.getString(3));
				bean.setListNum(cursor.getString(4));
				lst.add(bean);
			} while (cursor.moveToNext());
		}
		cursor.close();
//		db.close();

		return lst;
	}

	public void deleteAttentionWord(String knowledgeID) {
		SQLiteDatabase db = this.getReadableDatabase();

		String sql = "delete from attentionKnowledgeTable where knowledgeID="
				+ "'" + knowledgeID + "'";
		db.execSQL(sql);
//		db.close();
	}

	public void deleteAllAttentionWord() {
		this.getWritableDatabase().delete(DBHelper.attentionWordListTable,
				null, null);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		this.mDefaultWritableDatabase = db;
		createTables(db);
		initData();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		this.mDefaultWritableDatabase = db;
		dropTables(db);
		createTables(db);
	}

	/**
	 * initialize data
	 */
	private void initData() {
		insertKnowledge();
		List<QuestionBean> temp = temp();
		insertQuestions(temp);
	}

	/**
	 * insert knowledge into database
	 */
	private void insertKnowledge() {
		KnowledgeBean bean = new KnowledgeBean();
		bean.setKnowledgeId("1");
		bean.setSubject("What is OOP?");
		bean.setType("JavaBasic");
		bean.setContent("OOP means Object Oriented Programming");
		bean.setListNum("list1");
		this.addKnowledge(bean);

		bean = new KnowledgeBean();
		bean.setKnowledgeId("2");
		bean.setSubject("What is encapsulation in Java?");
		bean.setType("JavaBasic");
		bean.setContent("Encapsulation provides objects with the ability to hide their internal characteristics and behavior. Each object provides a number of methods, which can be accessed by other objects and change its internal data. ");
		bean.setListNum("list1");
		this.addKnowledge(bean);

		bean = new KnowledgeBean();
		bean.setKnowledgeId("3");
		bean.setSubject("What is Polymorphism in Java?");
		bean.setType("JavaBasic");
		bean.setContent("Polymorphism is the ability of programming languages to present the same interface for differing underlying data types.");
		bean.setListNum("list1");
		this.addKnowledge(bean);

		bean = new KnowledgeBean();
		bean.setKnowledgeId("4");
		bean.setSubject("What is Inheritance?");
		bean.setType("JavaBasic");
		bean.setContent("Inheritance provides an object with the ability to acquire the fields and methods of another class, called base class.");
		bean.setListNum("list1");
		this.addKnowledge(bean);
	}

	/**
	 * insert questions into question table
	 * 
	 * @param questions
	 */
	public void insertQuestions(List<QuestionBean> questions) {
		for (QuestionBean q : questions) {
			addQuestion(q);
		}
	}

	private List<QuestionBean> temp() {
		List<QuestionBean> questions = new LinkedList<QuestionBean>();
		QuestionBean bean = new QuestionBean();
		List<String> answers = new ArrayList<String>();
		 bean.setQuestionID("1");
		bean.setQuestion("Which statement is true for the class java.util.ArrayList?");
		bean.setType("JavaBasic");
		answers.add("The elements in the collection are ordered.");
		answers.add("The collection is guaranteed to be immutable.");
		answers.add("The elements in the collection are guaranteed to be unique.");
		answers.add("The elements in the collection are accessed using a unique key.");
		bean.setOptions(answers);
		questions.add(bean);

		bean = new QuestionBean();
		answers = new ArrayList<String>();
		 bean.setQuestionID("2");
		bean.setQuestion("What is the prototype of the default constructor?");
		bean.setType("JavaBasic");
		answers.add("public Test( )	");
		answers.add("Test( )");
		answers.add("Test(void)");
		answers.add("public Test(void)");
		bean.setOptions(answers);
		questions.add(bean);
		
		bean = new QuestionBean();
		answers = new ArrayList<String>();
		 bean.setQuestionID("3");
		bean.setQuestion("Which one of these lists contains only Java programming language keywords?");
		bean.setType("JavaBasic");
		answers.add("goto, instanceof, native, finally, default, throws	");
		answers.add("try, virtual, throw, final, volatile, transient");
		answers.add("class, if, void, long, Int, continue");
		answers.add("byte, break, assert, switch, include");
		bean.setOptions(answers);
		questions.add(bean);
		
		bean = new QuestionBean();
		answers = new ArrayList<String>();
		bean.setQuestionID("4");
		bean.setQuestion("Which is a reserved word in the Java programming language?");
		bean.setType("JavaBasic");
		answers.add("native");
		answers.add("method");
		answers.add("subclasses");
		answers.add("array");
		bean.setOptions(answers);
		questions.add(bean);
		return questions;
	}

	/**
	 * Add aquestion into database
	 * 
	 * @param QuestionBean
	 */
	private void addQuestion(QuestionBean bean) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.questionID, bean.getQuestionID());
		values.put(DBHelper.question, bean.getQuestion());
		values.put(DBHelper.questionType, bean.getType());
		values.put(DBHelper.correctAnswer, bean.getOptions().get(0));
		values.put(DBHelper.answer2, bean.getOptions().get(1));
		values.put(DBHelper.answer3, bean.getOptions().get(2));
		values.put(DBHelper.answer4, bean.getOptions().get(3));
		this.getWritableDatabase().insert(DBHelper.questionTable, null, values);
	}

	/**
	 * Get question for test
	 * 
	 * @param questionId
	 * @return Question
	 */
	public QuestionBean getQuestion(String questionId) {
		String sql = "select * from " + DBHelper.questionTable + " where "
				+ DBHelper.questionID + " = " + questionId;
		SQLiteDatabase readOnlydb = this.getReadableDatabase();
		Cursor cursor = readOnlydb.rawQuery(sql, null);
		QuestionBean q = null;
		if (cursor.moveToFirst()) {
			q = new QuestionBean();
			q.setQuestionID(questionId);
			q.setQuestion(cursor.getString(1));
			q.setType(cursor.getString(2));
			List<String> list = new LinkedList<String>();
			for (int i = 3; i < 7; i++) {
				list.add(cursor.getString(i));
			}
			q.setOptions(list);
		}
		return q;
	}

	/**
	 * get the number of questions
	 * 
	 * @return number of questions
	 */
	public int getAmountOfQuestions() {
		int amount = -1;
		String sql = "select count(*) from " + DBHelper.questionTable;
		SQLiteDatabase readOnlydb = this.getReadableDatabase();
		Cursor cursor = readOnlydb.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			amount = Integer.parseInt(cursor.getString(0));
		}
		return amount;
	}

	/**
	 * clear test record
	 */
	public void clearTestRecord() {
		String sql = "delete from questionTable";
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(sql);
		db.execSQL("VACUUM");
	}

	/**
	 * Check current question is exist or not
	 * 
	 * @param testID
	 * @return true : exist false: not exist
	 */
	public boolean questionExistCheck(int testID) {
		boolean flag = false;
		String sql = "select count(*) from questionTable where questionID = "
				+ testID;
		SQLiteDatabase readOnlydb = this.getReadableDatabase();
		Cursor cursor = readOnlydb.rawQuery(sql, null);
		int amount = -1;
		if (cursor.moveToFirst()) {
			amount = Integer.parseInt(cursor.getString(0));
		}
		if (amount == 1) {
			flag = true;
		}
		return flag;
	}
}