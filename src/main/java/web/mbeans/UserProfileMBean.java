package web.mbeans;

import db.entity.Users;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

@ManagedBean(name = "profile")
@ViewScoped
public class UserProfileMBean implements Serializable {

	static final Logger logger = Logger.getLogger(UserProfileMBean.class.getName());

	@ManagedProperty("#{ctx}")
	private ContextMBean ctx;
	@ManagedProperty("#{db}")
	private DbMBean db;
	@ManagedProperty("#{app}")
	AppMBean app;
	
	private Users user;
	private Part part;
	
	
	public UserProfileMBean() {
	}

	public void setCtx(ContextMBean ctx) {
		this.ctx = ctx;
	}

	public void setDb(DbMBean db) {
		this.db = db;
	}

	public void setApp(AppMBean app) {
		this.app = app;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Users getUser() {
//		if (user == null) {
//			user = ctx.getUser();
//		}
		return user;
	}

	public Part getPart() {
		return part;
	}

	public void setPart(Part part) {
		this.part = part;
	}

	public String getUserUuid() {
		return user == null? null: user.getUuid();
	}
	
	public void setUserUuid(String uuid) {
		if (uuid == null || uuid.isEmpty())
			return;
		if (user != null && uuid.equalsIgnoreCase(user.getUuid()))
			return;
		user = db.findByUuid(Users.class, uuid);
	}
	
	public void saveUser() {
		if (part != null && part.getSize() > 0) {
			try {
				String fn = app.getAvatar().uploadUserImg(user, part);
				user.setAvatarFile(fn);
			} catch (IOException ex) {
				logger.log(Level.SEVERE, "Image upload error", ex);
			}
		}
		user = db.merge(user);
		if (user.equals(ctx.getUser()))
			ctx.setUser(user);

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Изменения сохранены"));
	}
}
