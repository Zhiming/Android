package nz.ac.unitec.iknow.bean;

/**
 * Knowledge bean
 * 
 * @author Zhiming Zhang(1446774) Yang Zhang(1453733) Ming Kong(1449315)
 *
 */
public class KnowledgeBean {
	// knowledgeId
	private String knowledgeId;
	// subject
	private String subject;
	// knowledge type
	private String type;
	// content
	private String Content;
	// list number
	private String ListNum;

	public String getKnowledgeId() {
		return knowledgeId;
	}

	public void setKnowledgeId(String knowledgeId) {
		this.knowledgeId = knowledgeId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getListNum() {
		return ListNum;
	}

	public void setListNum(String listNum) {
		ListNum = listNum;
	}

}
