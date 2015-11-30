package nz.ac.unitec.iknow.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Communicator for connecting server
 * 
 * @author Zhiming Zhang(1446774) Yang Zhang(1453733) Ming Kong(1449315)
 *
 */
public class Communicator {
	//http://10.0.2.2:8080/questions-json/questions.json
	public InputStream getQuestions(String path) throws MalformedURLException, IOException {
		//Server URL, Using localhost (127.0.0.1) to open
		InputStream inStream = null;
		URL url = new URL(path);
		// Construct an URL
		HttpURLConnection con = (HttpURLConnection) url.openConnection();// open connection
		con.setConnectTimeout(5000);// Set timeout to 5 seconds
		con.setRequestMethod("GET");
		if (con.getResponseCode() == 200) { // Check if connection successful
			inStream = con.getInputStream();
		}
		return inStream;
	}

}
