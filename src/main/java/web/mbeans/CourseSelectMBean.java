package web.mbeans;

import db.entity.Courses;
import db.entity.CoursesStudents;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "courseSel")
@ViewScoped
public class CourseSelectMBean implements Serializable {

	static final Logger logger = Logger.getLogger(CourseSelectMBean.class.getName());
	
	@ManagedProperty(value = "#{app}")
	AppMBean app;
	@ManagedProperty(value = "#{db}")
	private DbMBean db;
	@ManagedProperty(value = "#{ctx}")
	private ContextMBean ctx;

	private Courses course;
	private CourseAnswer[] answers;
	
	public CourseSelectMBean() {
	}

	public void setApp(AppMBean app) {
		this.app = app;
	}

	public void setDb(DbMBean db) {
		this.db = db;
	}

	public void setCtx(ContextMBean ctx) {
		this.ctx = ctx;
	}

	public String getCourseUuid() {
		return course == null? null: course.getUuid();
	}
	
	public void setCourseUuid(String uuid) throws IOException {
		if (course != null && course.getUuid().equals(uuid))
			return;

		course = db.findByUuid(Courses.class, uuid);
		if (course == null) {
			logger.warning("Cannot find course by id = " + uuid);
		}
		
		List<CourseQuestion> lst = CourseQuestion.decodeList(course.getQuestions());
		answers = new CourseAnswer[lst.size()];
		for (int i = 0; i < answers.length; i++)
			answers[i] = new CourseAnswer(lst.get(i));
	}

	public Courses getCourse() {
		return course;
	}

	public CourseAnswer[] getAnswers() {
		return answers;
	}
	
	public void save() {
		FacesMessage msg = null;
		
		if (ctx.getMyCourses().contains(course)) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Вы уже записаны на курс", course.getTitle());
		} else {
			Date now = new Date();
			CoursesStudents cs = new CoursesStudents();
			cs.setCourseId(course);
			cs.setStudentId(ctx.getUser());
			cs.setAnswers(CourseAnswer.encodeList(answers));
			cs.setCreatedAt(now);
			cs.setUpdatedAt(now);

			cs = db.persist(cs);
			ctx.getMyCourses().add(course);
			course.getCoursesStudentsCollection().add(cs);
			db.merge(course);
		}

		if (msg != null)
			FacesContext.getCurrentInstance().addMessage(null, msg);
	}
}
