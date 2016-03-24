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
* 	[{"question":{"body":"Умеете ли Вы играть в какие-нибудь карточные игры со взятками?","answer1":"Нет","answer2":"Да, в преферанс","answer3":"Да, в покер","answer4":"Да"},"answer":"Нет"}]
*/
public class StudentAnswer {
	
	static final Logger logger = Logger.getLogger(StudentAnswer.class.getName());

	static List<StudentAnswer> decodeList(String json) {
		List<StudentAnswer> lst = new ArrayList<>();
		if (!(json == null || json.trim().isEmpty()))
			try {
				ObjectMapper om = new ObjectMapper();
				StudentAnswer[] ar = om.readValue(json, StudentAnswer[].class);
				Arrays.stream(ar).forEach(cq -> lst.add(cq));
			} catch (IOException ex) {
				logger.log(Level.SEVERE, "Decoding error", ex);
			}
		return lst;
	}

	static List<StudentAnswer> fromQuestions(String json) {
		List<StudentAnswer> lst = new ArrayList<>();
		if (!(json == null || json.trim().isEmpty()))
		if (!(json == null || json.trim().isEmpty()))
			try {
				ObjectMapper om = new ObjectMapper();
				CourseQuestion[] ar = om.readValue(json, CourseQuestion[].class);
				Arrays.stream(ar).forEach(cq -> lst.add(new StudentAnswer(cq)));
			} catch (IOException ex) {
				logger.log(Level.SEVERE, "Decoding error", ex);
			}
		return lst;
	}

	static String encodeList(List<StudentAnswer> lst) {
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

	private CourseQuestion question;
	private String answer;

	public StudentAnswer() {
	}

	public StudentAnswer(CourseQuestion question) {
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

}
