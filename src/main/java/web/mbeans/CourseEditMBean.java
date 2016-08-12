package web.mbeans;

import db.entity.Courses;
import db.entity.Disciplines;
import db.entity.Users;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@ManagedBean(name = "courseEdit")
@ViewScoped
public class CourseEditMBean implements Serializable {

	static final Logger logger = Logger.getLogger(CourseEditMBean.class.getName());
	
	@ManagedProperty(value = "#{app}")
	AppMBean app;
	@ManagedProperty(value = "#{db}")
	private DbMBean db;
	@ManagedProperty(value = "#{ctx}")
	private ContextMBean ctx;

	private Courses course;
	private Part part;
	private List<CourseQuestion> quest;
	
	public CourseEditMBean() {
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
	
	
//	@PostConstruct
	private void init() {
//		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		do {
			if (!ctx.inRole("admin,teacher"))
				break;

//			String s = ec.getRequestParameterMap().get("courseId");
//			if (s != null) try {
//				int id = Integer.parseInt(s);
//				course = db.find(Courses.class, id);
//				if (course == null)
//					break;
//				
//				if (!(ctx.inRole("admin") || 
//						course.getAuthorId().getId() == ctx.getUser().getId()))
//						// ctx.isCourseOfUser(course)))
//					break;
//				
//				quest = CourseQuestion.decodeList(course.getQuestions());
//			} catch (Exception e) {
//				e.printStackTrace();
//				break;
//			} else {
				course = new Courses();
				course.setAuthorId(ctx.getUser());
				course.setStudentsLimit(1);
				course.setUuid(UUID.randomUUID().toString());
//			}

//			if (course.getCoverUrl() == null || course.getCoverUrl().trim().isEmpty()) {
//				course.setCoverUrl("/images/avatar-course.svg");
//			}
			return;
		} while(false);
		
		try {
//			ec
			FacesContext.getCurrentInstance().getExternalContext()
					.responseSendError(HttpServletResponse.SC_NOT_FOUND, "Requested resource not found!");
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "init failed", ex);
			throw new IllegalStateException(ex);
		}
	}

	public String getCourseUuid() {
		return course == null? null: course.getUuid();
	}
	
	public void setCourseUuid(String uuid) throws IOException {
		if (course != null && course.getUuid().equals(uuid))
			return;
		if ("new".equals(uuid)) {
			init();
		} else {
			course = db.findByUuid(Courses.class, uuid);
			if (course == null) {
				logger.warning("Cannot find course by id = " + uuid);
			}
			ctx.checkAccess(ctx.canEditCourse(course));
		}
		quest = CourseQuestion.decodeList(course.getQuestions());
	}

	public Courses getCourse() {
		return course;
	}
//
//	public void setCourse(Courses course) {
//		this.course = course;
//	}

	public Part getPart() {
		return part;
	}

	public void setPart(Part part) {
		this.part = part;
	}

	public List<CourseQuestion> getQuest() {
		return quest;
	}
	
	public boolean isNewCourse() {
		return course.getId() == null || course.getId() == 0;
	}
	
	public void update() throws Exception {
		boolean create = isNewCourse();
		Date now = new Date();
		course.setUpdatedAt(now);
		course.setQuestions(CourseQuestion.encodeList(quest));
		Disciplines disc = course.getDisciplineId();
		if (disc != null) {
			disc = db.getEntityManager().getReference(Disciplines.class, disc.getId());
			course.setDisciplineId(disc);
		}

		if (create) {
			course.setCreatedAt(now);
			course.setAuthorId(ctx.getUser());
			course = db.persist(course);
/*			
			CourseTopics t = new CourseTopics();
			t.setCreatedAt(now);
			t.setUpdatedAt(now);
			t.setTitle("");
			t.setCourse(course);
			t = db.persist(t);
			course.getTopicsList().add(t);
			ctx.getUser().getCoursesCollection().add(course);
*/			
		}
		if (part != null && part.getSize() > 0) {
			String fn = app.getAvatar().uploadCourseImg(course, part);
			course.setCoverUrl(fn);
		}
		course = db.merge(course);
/*
		Disciplines d = course.getDisciplineId();
		if (!d.getCoursesCollection().contains(course)) {
			d.getCoursesCollection().add(course);
		}
*/		
	

// WORKAROUND for cache syncro...
db.clearCache();

		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage((create? "Курс создан.": "Курс изменён."), course.getTitle()));
		if (create)
			ctx.getUser().getCoursesCollection().add(course);
	}
	
	public String deactivate() {
		course.setIsDelete(true);
		course.setUpdatedAt(new Date());
		course = db.merge(course);

// WORKAROUND for cache syncro...
db.clearCache();
if (ctx.getUser().equals(course.getAuthorId()))
	ctx.reload();

		return "/courses/index";
	}

	public void addQuest() {
		quest.add(new CourseQuestion());
	}

	public void delQuest(CourseQuestion q) {
		if (quest.contains(q)) {
			quest.remove(q);
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Question not found: " + q));
		}
				
	}
	
	public void delQuest(int idx) {
		if (idx < 0 || idx >= quest.size()) {
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage("Question index is out of range: " + idx + " of " + quest.size()));
		} else {
			quest.remove(idx);
		}
	}
}
