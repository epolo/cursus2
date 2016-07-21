package web.mbeans;

import db.entity.Users;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

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
		if (user == null) {
			user = ctx.getUser();
		}
		return user;
	}

	public Part getPart() {
		return part;
	}

	public void setPart(Part part) {
		this.part = part;
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
		ctx.setUser(db.merge(user));
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Changes are successfully saved."));
	}
}
