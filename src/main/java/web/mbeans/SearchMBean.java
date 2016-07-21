package web.mbeans;

import db.entity.Courses;
import db.entity.Users;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.persistence.EntityManager;

@ManagedBean(name = "search")
@ViewScoped
public class SearchMBean implements Serializable {

	static Logger logger = Logger.getLogger(SearchMBean.class.getName());
	
	public enum RESULT_TYPE {
		PROF, COURSE, TOPIC;
	}
	
	public static class Result {
		private final RESULT_TYPE type;
		private final String title;
		private final String url;

		public Result(RESULT_TYPE type, String title, String url) {
			this.type = type;
			this.title = title;
			this.url = url;
		}

		public RESULT_TYPE getType() {
			return type;
		}

		public String getTitle() {
			return title;
		}

		public String getUrl() {
			return url;
		}

		@Override
		public String toString() {
			return "Result{" + "type=" + type + ", title=" + title + ", url=" + url + '}';
		}
	}
	
	@ManagedProperty("#{db}")
	private DbMBean db;
	
	private String value;
	private List<Result> resList;
	
	public SearchMBean() {
	}

	public void setDb(DbMBean db) {
		this.db = db;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<Result> getResList() {
		return resList;
	}

	private String getSql(String entity, String[] fields, String and) {
		StringBuilder sb = new StringBuilder();
		sb.append("select o from ").append(entity).append(" o where ( ");
		boolean first = true;
		for (String s : fields) {
			if (first)
				first = false;
			else
				sb.append("or ");
			sb.append("0 < LOCATE (:searchStr, o.").append(s).append(") ");
		}
		sb.append(" )");
		if (and != null)
			sb.append(" and ").append(and);
		
		return sb.toString();
	}
	
	static final String[] COURSES_FIELDS = {"title", "subTitle", "tags", "description"};
	static final String COURSES_URL = "/courses/detail?uid=";

		static final String[] PROF_FIELDS = {"name", "personalInfo"};
	static final String PROF_URL = "/professors/detail?uid=";
	
	public void search() {
		if (value == null || value.isEmpty())
			return;

		// final String ctx = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
		resList = new ArrayList<>();
		EntityManager em = db.getEntityManager();
		try {
			
			em.createQuery(getSql("Courses", COURSES_FIELDS, "not o.isDelete"), Courses.class)
				.setParameter("searchStr", value).getResultList().stream().forEach(c -> 
					resList.add(new Result(RESULT_TYPE.COURSE, c.getTitle(), /*ctx +*/ COURSES_URL + c.getUuid())));
			
			em.createQuery(getSql("Users", PROF_FIELDS, "o.status='active' and not o.coursesCollection is empty"), Users.class)
				.setParameter("searchStr", value).getResultList().stream().forEach(u -> 
					resList.add(new Result(RESULT_TYPE.PROF, u.getName(), /*ctx +*/ PROF_URL + u.getUuid())));
			
		} finally {
			em.close();
		}
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Search content: " + value));
	}

}
