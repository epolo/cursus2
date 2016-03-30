package web.mbeans;

import db.entity.Users;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "prof")
@ViewScoped
public class ProfessorsMBean implements Serializable {
	private static final long serialVersionUID = 1L;

	static final String PROFF_CONDITION = "o.id in (select c.authorId.id from Courses c)";
	
	@ManagedProperty(value = "#{db}")
	private DbMBean db;
	
	private List<Users> profList;
	private Users selected;
	
	public ProfessorsMBean() {
	}

	public void setDb(DbMBean db) {
		this.db = db;
	}

	public List<Users> getProfList() {
		if (profList == null) {
			profList = db.select(Users.class, "SELECT o FROM Users o WHERE " + PROFF_CONDITION);
		}
		return profList;
	}
	
	public Users getSelected() {
		return selected;
	}

	public void setSelected(Users selected) {
		this.selected = selected;
	}
	
	public void setSelectedId(Integer id) {
		if (id > 0) try {
			selected = (Users) db.selectSingle("select o from Users o where o.id=?1 and " + PROFF_CONDITION, id);
		} catch (Exception ex) {
			Logger.getLogger(ProfessorsMBean.class.getName())
					.log(Level.SEVERE, "Cannot find professor by id = " + id, ex);
		}
	}

	public Integer getSelectedId() {
		return selected == null? null: selected.getId();
	}
	
	public void update() {
		profList = null;
	}
	
}
