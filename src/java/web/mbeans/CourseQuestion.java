package web.mbeans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
{"body":"Вопрос №1","answer1":"А","answer2":"Б","answer3":"В","answer4":"Г"}
*/
public class CourseQuestion {
	
	static final Logger logger = Logger.getLogger(CourseQuestion.class.getName());

	static List<CourseQuestion> decodeList(String json) {
		List<CourseQuestion> lst = new ArrayList<>();
		if (!(json == null || json.trim().isEmpty()))
			try {
				ObjectMapper om = new ObjectMapper();
				CourseQuestion[] ar = om.readValue(json, CourseQuestion[].class);
				Arrays.stream(ar).forEach(cq -> lst.add(cq));
			} catch (IOException ex) {
				logger.log(Level.SEVERE, "Decoding error", ex);
			}
		return lst;
	}
	
	static String encodeList(List<CourseQuestion> lst) {
		String json = "";
		if (!(lst == null || lst.isEmpty()))
			try {
				ObjectMapper om = new ObjectMapper();
				json = om.writeValueAsString(lst);
			} catch (JsonProcessingException ex) {
				logger.log(Level.SEVERE, "Encoding error", ex);
			}
		return json;
	}
	
	private String body;
	private String answer1;
	private String answer2;
	private String answer3;
	private String answer4;

	public CourseQuestion() {
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getAnswer1() {
		return answer1;
	}

	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}

	public String getAnswer2() {
		return answer2;
	}

	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}

	public String getAnswer3() {
		return answer3;
	}

	public void setAnswer3(String answer3) {
		this.answer3 = answer3;
	}

	public String getAnswer4() {
		return answer4;
	}

	public void setAnswer4(String answer4) {
		this.answer4 = answer4;
	}
	
	
}
