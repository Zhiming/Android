package nz.ac.unitec.iknow.model;

import java.io.InputStream;
import java.util.List;

import nz.ac.unitec.iknow.bean.QuestionBean;

import org.json.JSONException;

/**
 * Parser interface
 * @author Zhiming Zhang(1446774) Yang Zhang(1453733) Ming Kong(1449315)
 *
 */
public interface Parser {
	List<QuestionBean> parse(InputStream inStream) throws JSONException;
}
