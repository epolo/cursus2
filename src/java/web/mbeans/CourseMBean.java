package web.mbeans;

import db.entity.CourseTopics;
import db.entity.Courses;
import db.entity.CoursesStudents;
import db.entity.Users;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "course")
@ViewScoped
public class CourseMBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CourseMBean.class.getName());
	
	@ManagedProperty(value = "#{db}")
	private DbMBean db;

	@ManagedProperty(value = "#{ctx}")
	private ContextMBean ctx;
	
	private List<Courses> coursesLst;
	private boolean showDeleted;
	private boolean showPast;
	private Courses selected;
	private List<StudentAnswer> ansList;
	private int curAnswer = 0;
	private String otherAnswer;

	private TopicsProcessor topProc;
	
	public CourseMBean() {
	}

	@PostConstruct
	void init() {
		String s = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("courseId");
		if (s != null) try {
			int id = Integer.parseInt(s);
			selected = db.find(Courses.class, id);
		} catch (Exception e) { e.printStackTrace(); }
	}

	public void setDb(DbMBean db) {
		this.db = db;
	}

	public void setCtx(ContextMBean ctx) {
		this.ctx = ctx;
	}

	public List<Courses> getCoursesLst() {
		if (coursesLst == null) {
			coursesLst = db.getCoursesList(showDeleted, showPast);
		}
		return coursesLst;
	}

	public boolean isShowDeleted() {
		return showDeleted;
	}

	public void setShowDeleted(boolean showDeleted) {
		this.showDeleted = showDeleted;
	}

	public boolean isShowPast() {
		return showPast;
	}

	public void setShowPast(boolean showPast) {
		this.showPast = showPast;
	}

	public Courses getSelected() {
		return selected;
	}

	public void setSelected(Courses selected) {
		this.selected = selected;
	}

	public List<StudentAnswer> getAnsList() {
		if (ansList == null && selected != null) {
			ansList = StudentAnswer.fromQuestions(selected.getQuestions());
		}
		return ansList;
	}

	public void update() {
		coursesLst = null;
	}

	public boolean canEdit() {
		// is selected course active
		if (selected == null)
			return false;
		if (ctx.inRole("teacher"))
			return selected.getAuthorId().getId() == ctx.getUser().getId();
		return ctx.inRole("admin");
	}
	
	public boolean canEnroll() {
		// is selected course active
		if (selected == null || selected.getStartsAt() == null || new Date().after(selected.getStartsAt()))
			return false;
		// check if course students limit is not reached
		int n = selected.getCoursesStudentsCollection() == null? 0: selected.getCoursesStudentsCollection().size();
		if ((selected.getStudentsLimit() - n) <= 0)
			return false;
		// is user authorized and not already enrolled
		if (!ctx.inRole("student") || ctx.isCourseOfUser(selected))
			return false;
		return true;
	}
	
	public void enroll() {
		if (!canEnroll()) {
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Вы не можете записаться на этот курс", 
							selected.getTitle()));
			return;
		}

		Users u = ctx.getUser();
		String a = StudentAnswer.encodeList(ansList);
		CoursesStudents cs = new CoursesStudents(a, u, selected);
		db.persist(cs);
		ctx.getMyCourses().add(selected);
		FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage("Вы записаны на курс " + selected.getTitle()));
	}

	public String getQuestTitle() {
		int i = curAnswer + 1;
		return "Вопрос " + i + " из " + getAnsList().size();
	}
	
	public boolean isFirstQuest() {
		return curAnswer == 0;
	}
	
	public boolean isLastQuest() {
		return (getAnsList().size() - curAnswer) < 2;
	}

	public void nextQuest() {
		if (curAnswer < getAnsList().size())
			curAnswer++;
	}
	
	public void prevQuest() {
		if (curAnswer > 0)
			curAnswer--;
	}
	
	public StudentAnswer getAnswer() {
		return getAnsList().get(curAnswer);
	}

	public String getOtherAnswer() {
		return otherAnswer;
	}

	public void setOtherAnswer(String otherAnswer) {
		this.otherAnswer = otherAnswer;
	}

	public TopicsProcessor getTopProc() {
		if (topProc == null) {
			topProc = new TopicsProcessor(selected.getTopicsList(), ctx);
		}
		return topProc;
	}

	
}
