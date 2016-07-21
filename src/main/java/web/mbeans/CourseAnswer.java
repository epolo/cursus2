package web.mbeans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CourseAnswer {

	private CourseQuestion question;
	private String answer;
	private int selected = 0;

	public CourseAnswer() {
	}

	public CourseAnswer(CourseQuestion question) {
		this.question = question;
	}

	public CourseQuestion getQuestion() {
		return question;
	}

	public void setQuestion(CourseQuestion question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getSelected() {
		return selected;
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}
	
	
	
	static final Logger logger = Logger.getLogger(CourseAnswer.class.getName());

	static CourseAnswer[] decodeList(String json) {
		if (!(json == null || json.trim().isEmpty()))
			try {
				ObjectMapper om = new ObjectMapper();
				return om.readValue(json, CourseAnswer[].class);
			} catch (IOException ex) {
				logger.log(Level.SEVERE, "Decoding error", ex);
			}
		return null;
	}
	
	static String encodeList(CourseAnswer[] arr) {
		String json = "";
		if (arr != null && arr.length > 0)
			try {
				ObjectMapper om = new ObjectMapper();
				json = om.writeValueAsString(arr);
			} catch (JsonProcessingException ex) {
				logger.log(Level.SEVERE, "Encoding error", ex);
			}
		return json;
	}

}
