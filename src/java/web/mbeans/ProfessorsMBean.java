package web.mbeans;

import db.entity.Users;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "prof")
@ViewScoped
public class ProfessorsMBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{db}")
	private DbMBean db;
	
	private List<Users> profList;
	private Users selected;
	
	public ProfessorsMBean() {
	}

	@PostConstruct
	void init() {
		String s = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("profId");
		if (s != null) try {
			setSelectedId(Integer.parseInt(s));
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public void setDb(DbMBean db) {
		this.db = db;
	}

	public List<Users> getProfList() {
		if (profList == null) {
			profList = db.select(
				"SELECT o FROM Users o WHERE o.id in (select c.authorId.id from Courses c)");
		}
		return profList;
	}

	public Users getSelected() {
		return selected;
	}

	public void setSelected(Users selected) {
		this.selected = selected;
	}
	
	public void setSelectedId(int id) {
		if (id > 0)
			selected = (Users) db.selectSingle(
					"select o from Users o where o.id=?1 and o.id in (select c.authorId.id from Courses c)", 
					id);
	}
	
	public void update() {
		profList = null;
	}
	
}
