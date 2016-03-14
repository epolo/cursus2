package web.mbeans;

import db.entity.Users;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@ManagedBean(name = "profile")
@ViewScoped
public class UserProfileMBean {

	@ManagedProperty("#{ctx}")
	private ContextMBean ctx;

	@ManagedProperty("#{db}")
	private DbMBean db;
	
	private Users user;
	private Part part;
	
	
	public UserProfileMBean() {
	}
	
	@PostConstruct
	private void init() {
		user = db.find(Users.class, ctx.getUser().getId());
	}

	public void setCtx(ContextMBean ctx) {
		this.ctx = ctx;
	}

	public void setDb(DbMBean db) {
		this.db = db;
	}

	public Users getUser() {
		return user;
	}

	public Part getPart() {
		return part;
	}

	public void setPart(Part part) {
		this.part = part;
	}

	public void saveUser() {
		ctx.setUser(db.merge(user));
	}

	public void uploadListener(FileUploadEvent evt) {
		UploadedFile uf = evt.getFile();
		if (uf != null)
			try {
				String fn = uf.getFileName();
				String url = saveFile(fn, uf.getInputstream());
				user.setAvatarUrl(url);
			} catch (IOException ex) {
				Logger.getLogger(UserProfileMBean.class.getName()).log(Level.SEVERE, 
						"Avatar file upload error", ex);
			}
	}
	
	private String saveFile(String fileName, InputStream is) throws IOException {
		String url = "/upload/user_" + user.getId() + '/';
		String fn = FacesContext.getCurrentInstance().getExternalContext().getRealPath(url);
		new File(fn).mkdirs();
		FileOutputStream fos = new FileOutputStream(fn + fileName);
		byte[] buf = new byte[16 * 1024];
		int sz;
		while ((sz = is.read(buf)) > 0)
			fos.write(buf, 0, sz);
		fos.flush();
		fos.close();
		is.close();
		return url + fileName;
	}

	public void upload(){
		String fn = part.getSubmittedFileName();
		Logger log = Logger.getLogger(UserProfileMBean.class.getName());
        log.info("call upload...");      
        log.log(Level.INFO, "content-type:{0}", part.getContentType());
        log.log(Level.INFO, "filename:{0}", part.getName());
        log.log(Level.INFO, "submitted filename:{0}", part.getSubmittedFileName());
        log.log(Level.INFO, "size:{0}", part.getSize());
		
        try {
/*            
            byte[] results=new byte[(int)part.getSize()];
            InputStream in=part.getInputStream();
            in.read(results);         
*/
			String url = saveFile(fn, part.getInputStream());
			user.setAvatarUrl(url);
			
        } catch (IOException ex) {
           log.log(Level.SEVERE, " ex @{0}", ex);
        }
		
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Uploaded!"));
    }
}
