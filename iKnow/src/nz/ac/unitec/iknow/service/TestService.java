package nz.ac.unitec.iknow.service;

import nz.ac.unitec.iknow.dao.DBHelper;

/**
 * Test services, create new test service
 * @author Zhiming Zhang(1446774) Yang Zhang(1453733) Ming Kong(1449315)
 *
 */
public class TestService {

	private DBHelper dbh;
	
	public TestService(){
		this.dbh = DBHelper.getDBHelper();
	}
	
	public boolean questionExistCheck(int testID) {
		return this.dbh.questionExistCheck(testID);
	}

}
