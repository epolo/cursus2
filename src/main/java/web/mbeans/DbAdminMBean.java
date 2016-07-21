package web.mbeans;

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
	
	private String query;
	List<Object> result;
	int qty;
	boolean update;
	
	public DbAdminMBean() {
	}

	public void setDb(DbMBean db) {
		this.db = db;
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

}
