package web.mbeans;

import db.entity.Courses;
import db.entity.Users;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import web.mbeans.DbMBean;

@ManagedBean(name = "dbAdm")
@RequestScoped
public class DbAdminMBean {

	@ManagedProperty("#{db}")
	DbMBean db;
	@ManagedProperty("#{ctx}")
	ContextMBean ctx;
	
	private String query;
	private List<Object> result;
	private int qty;
	private boolean update;
	
	private List<Courses> delCourses;
	
	
	public DbAdminMBean() {
	}

	public void setDb(DbMBean db) {
		this.db = db;
	}

	public void setCtx(ContextMBean ctx) {
		this.ctx = ctx;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public List<Object> getResult() {
		return result;
	}

	public void setResult(List<Object> result) {
		this.result = result;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public List<Courses> getDelCourses() {
		if (delCourses == null)
			delCourses = db.select(Courses.class, "select c from Courses c where c.isDelete = true");
		return delCourses;
	}
	
	public void exec() {
		FacesMessage msg = null;

		if (query == null || query.isEmpty()) {
			msg = new FacesMessage("No query");
		} else {
			EntityManager em = db.getEntityManager();

			try {
				Query q = em.createQuery(query);
				if (query.toLowerCase().startsWith("select")) {
					update = false;
					result = q.getResultList();
				} else {
					update = true;
					qty = q.executeUpdate();
				}
				msg = new FacesMessage("The request's completed.");
			} catch (Exception ex) {
				msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error on the request", ex.getMessage());
				Logger.getLogger(DbAdminMBean.class.getName()).throwing("DbAdminMBean", "exec", ex);
			} finally {
				if (em != null)
					em.close();
			}
		}
		if (msg != null)
			FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void restoreCourse(Courses c) {
		c.setIsDelete(false);
		c.setUpdatedAt(new Date());
		c = db.merge(c);
		delCourses.remove(c);
		
// WORKAROUND for cache syncro...
db.clearCache();
if (ctx.getUser().equals(c.getAuthorId()))
	ctx.reload();

		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage("Курс восстановлен -- " + c.getTitle()));

	}
}
