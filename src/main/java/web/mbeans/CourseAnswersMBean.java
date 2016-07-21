package web.mbeans;

import db.entity.Courses;
import db.entity.CoursesStudents;
import db.entity.Users;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;


@ManagedBean(name = "cAns")
@ViewScoped
public class CourseAnswersMBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(CourseAnswersMBean.class.getName());
	
	@ManagedProperty(value = "ctx")
	ContextMBean ctx;
	@ManagedProperty(value = "db")
	DbMBean db;
	
	private Courses course;
	private List<CourseQuestion> questList;
	private List<StudentAnswer> ansList;
	int currQuest;
	
	public CourseAnswersMBean() {
	}

	public void setCtx(ContextMBean ctx) {
		this.ctx = ctx;
	}

	public void setDb(DbMBean db) {
		this.db = db;
	}

	public Courses getCourse() {
		return course;
	}

	public void setCourse(Courses course) {
		this.course = course;
		questList = CourseQuestion.decodeList(course.getQuestions());
		int n = questList.size();
		ansList = new ArrayList<>();
		for (int i = 0; i < n; i++)
			ansList.add(new StudentAnswer(questList.get(i)));
		currQuest = 0;
	}

	public List<StudentAnswer> getAnsList() {
		return ansList;
	}

	public List<CourseQuestion> getQuestList() {
		return questList;
	}

	public int getCurrQuest() {
		return currQuest;
	}

	public void setCurrQuest(int currQuest) {
		this.currQuest = currQuest;
	}
	
	public boolean canMove(int delta) {
		if (delta < 0)
			return currQuest > 0;
		if (delta > 0)
			return currQuest < (ansList.size() - 1);
		return currQuest >= 0 && currQuest < ansList.size();
	}
	
	public void move(int delta) {
		int i = currQuest + delta;
		if (i >= 0 || i < ansList.size())
			currQuest = i;
		else
			logger.warning(" --- attempt to move out of questions index: " 
					+ currQuest + " + " + delta + " = " + i 
					+ " --- for Course: " + course);
	}
	
	void save() {
		Users u = ctx.getUser();
		Object o = null;
		try {
			o = db.selectSingle("select o from CoursesStudents o where o.courseId.id=" + course.getId() 
				+ " and o.studentId.id=" + u.getId()); 
		} catch (NoResultException nre) {
			o = null;
		}
		CoursesStudents cs;
		if (o != null) {
			cs = (CoursesStudents) o;
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "You have already registered for this course",
							"Course '" + cs.getCourseId().getTitle() +"' was registered for you " + cs.getCreatedAt()));
			return;
		}
		cs = new CoursesStudents();
		cs.setStudentId(u);
		cs.setCourseId(course);
		cs.setCreatedAt(new Date());
		cs.setAnswers(StudentAnswer.encodeList(ansList));
		
		db.persist(cs);
		u.getCoursesStudentsCollection().add(cs);
		course.getCoursesStudentsCollection().add(cs);
	}
	
}
