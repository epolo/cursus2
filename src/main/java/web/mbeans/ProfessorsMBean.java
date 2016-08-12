package web.mbeans;

import db.entity.Disciplines;
import db.entity.Users;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.myfaces.config.element.FacesConfig;

@ManagedBean(name = "prof")
@ViewScoped
public class ProfessorsMBean implements Serializable {
	private static final long serialVersionUID = 1L;

	static final String PROFF_CONDITION = "o.status='active' and o.id in (select c.authorId.id from Courses c where c.isDelete=false)";
	static final String PROFF_CONDITION_1 = "o.status='active' and o.id in (select c.authorId.id from Courses c where c.isDelete=false and c.disciplineId.id=%d)";
	
	@ManagedProperty(value = "#{db}")
	private DbMBean db;
	
	private Disciplines disc;
	private List<Users> profList;
	private Users selected;
	
	public ProfessorsMBean() {
	}

	public void setDb(DbMBean db) {
		this.db = db;
	}

	private String getProfCond() {
		return disc == null? PROFF_CONDITION:
				String.format(PROFF_CONDITION_1, disc.getId());
	}
	
	public List<Users> getProfList() {
		if (profList == null)
			profList = db.select(Users.class, "SELECT o FROM Users o WHERE " + 
					getProfCond());

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
	
	public void setSelectedUuid(String uuid) throws IOException {
		if (!(uuid == null || uuid.isEmpty())) try {
			selected = db.findByUuid(Users.class, uuid);
					//.selectSingle("select o from Users o where o.uuid=?1 and " + PROFF_CONDITION, uuid);
		} catch (Exception ex) {
			Logger.getLogger(ProfessorsMBean.class.getName())
					.log(Level.SEVERE, "Cannot find professor by uuid = " + uuid, ex);
		}
		if (selected == null)
			FacesContext.getCurrentInstance().getExternalContext().responseSendError(404, "Requested resource not found");
	}

	public String getSelectedUuid() {
		return selected == null? null: selected.getUuid();
	}

	public Disciplines getDisc() {
		return disc;
	}

	public void setDisc(Disciplines disc) {
		this.disc = disc;
	}

	public String getDiscUid() {
		return disc == null? null: disc.getUuid();
	}

	public void setDiscUid(String uid) {
		if (uid == null || uid.isEmpty()) {
			disc = null;
			return;
		}
		try {
			disc = db.findByUuid(Disciplines.class, uid);
		} catch (Exception ex) {
			Logger.getLogger(ProfessorsMBean.class.getName())
					.log(Level.SEVERE, "Cannot find discipline  by uuid = " + uid, ex);
		}
	}

	public void update() {
		profList = null;
	}
	
}
