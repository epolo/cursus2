package web.mbeans;

import db.entity.CourseTopics;
import db.entity.Courses;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@ManagedBean(name = "courseEdit")
@ViewScoped
public class CourseEditMBean {

	@ManagedProperty(value = "#{db}")
	private DbMBean db;

	@ManagedProperty(value = "#{ctx}")
	private ContextMBean ctx;

	private Courses course;
	private List<CourseQuestion> quest;
	
	public CourseEditMBean() {
	}

	public void setDb(DbMBean db) {
		this.db = db;
	}

	public void setCtx(ContextMBean ctx) {
		this.ctx = ctx;
	}
	
	
	@PostConstruct
	public void init() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		do {
			if (!ctx.inRole("admin,teacher"))
				break;

			String s = ec.getRequestParameterMap().get("courseId");
			if (s != null) try {
				int id = Integer.parseInt(s);
				course = db.find(Courses.class, id);
				if (course == null)
					break;
				
				if (!(ctx.inRole("admin") || 
						course.getAuthorId().getId() == ctx.getUser().getId()))
						// ctx.isCourseOfUser(course)))
					break;
				
				quest = CourseQuestion.decodeList(course.getQuestions());
			} catch (Exception e) {
				e.printStackTrace();
				break;
			} else {
				course = new Courses();
				course.setAuthorId(ctx.getUser());
				course.setStudentsLimit(1);
			}

			if (course.getCoverUrl() == null || course.getCoverUrl().trim().isEmpty()) {
				course.setCoverUrl("/images/avatar-course.svg");
			}
			return;
		} while(false);
		
		try {
			ec.responseSendError(HttpServletResponse.SC_NOT_FOUND, "Requested resource not found!");
		} catch (IOException ex) {
			Logger.getLogger(CourseEditMBean.class.getName()).log(Level.SEVERE, "init failed", ex);
			throw new IllegalStateException(ex);
		}
	}

	public Courses getCourse() {
		return course;
	}

	public void setCourse(Courses course) {
		this.course = course;
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
		if (create) {
			course.setCreatedAt(now);
			course = db.persist(course);
			CourseTopics t = new CourseTopics();
			t.setCreatedAt(now);
			t.setTitle("");
			t.setCourse(course);
			t = db.persist(t);
			// course.setTopicsCollection(Collections.singleton(t));
			ctx.getUser().getCoursesCollection().add(course);
		} else {
			course = db.merge(course);
		}

		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage((create? "Курс создан.": "Курс изменён.") 
										+ " ID = " + course.getId()));
	}
	
	public void uploadListener(FileUploadEvent evt) throws IOException {
		UploadedFile uf = evt.getFile();
		if (uf != null) {
			String fn = uf.getFileName();
			String url = saveFile(fn, uf.getInputstream());
			course.setCoverUrl(url);
		}
	}
	
	private String saveFile(String fileName, InputStream is) throws IOException {
		String url = "/upload/courses/";
		String fn = FacesContext.getCurrentInstance().getExternalContext().getRealPath(url);
		File dir = new File(fn);
		dir.mkdirs();
		Files.copy(is, new File(dir, fileName).toPath());
		is.close();
		return url + fileName;
	}

	public void addQuest() {
		quest.add(new CourseQuestion());
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
