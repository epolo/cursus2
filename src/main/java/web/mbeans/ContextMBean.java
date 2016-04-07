package web.mbeans;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import db.entity.Courses;
import db.entity.Topics;
import db.entity.Users;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ManagedBean(name = "ctx")
@SessionScoped
public class ContextMBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManagedProperty("#{app}")
	private AppMBean app;
	@ManagedProperty("#{db}")
	DbMBean db;
	
	private String userName;
	private Users user;
	private List<Courses> myCourses;
	private String extCookie;
	private String invCode;
	private String codeRequest;
	private boolean forumAdmin;
	static final String EXTSESSION_COOKIE = "EXTSESSION";

	public ContextMBean() {
	}

	@PreDestroy
	void destroy() {
		setUser(null);
	}
	
	public void setApp(AppMBean app) {
		this.app = app;
	}

	public void setDb(DbMBean db) {
		this.db = db;
	}

	public String getUserName() {
		return userName;
	}

	public Users getUser() {
		return user;
	}

	private void updateExtCookie() {
		if (extCookie == null) {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			extCookie = "EXT-" + ec.getSessionId(true);
			HashMap<String, Object> props = new HashMap<>();
			props.put("path", "/");
			ec.addResponseCookie(EXTSESSION_COOKIE, extCookie, props);
		}
	}

	/**
	 * Utility to call from servlet context
	 */
	public void updateExtCookie(HttpServletRequest req, HttpServletResponse resp) {
		if (extCookie == null) {
			extCookie = "EXT-" + req.getSession().getId();
			Cookie coo = new Cookie(EXTSESSION_COOKIE, extCookie);
			coo.setPath("/");
			resp.addCookie(coo);
		}
	}
	
	public void setUser(Users user, String sessId) {
		this.user = user;
		if (user == null) {
			userName = null;
			if (extCookie != null) {
				app.getExtUsersMap().remove(extCookie);
				extCookie = null;
			}
		} else {
			userName = user.getName();
			if (user.getPhpId() != null) {
				updateExtCookie();
				app.getExtUsersMap().put(extCookie, user.getPhpId());
			}
		}
	}
	
	public void setUser(Users user) {
		this.user = user;
		if (user == null) {
			userName = null;
			if (extCookie != null) {
				app.getExtUsersMap().remove(extCookie);
				extCookie = null;
			}
		} else {
			userName = user.getName();
			if (user.getPhpId() != null) {
				if (extCookie == null) {
					ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
					extCookie = "EXT-" + ec.getSessionId(true);
					HashMap<String, Object> props = new HashMap<>();
					props.put("path", "/");
					ec.addResponseCookie(EXTSESSION_COOKIE, extCookie, props);
				}
				app.getExtUsersMap().put(extCookie, user.getPhpId());
			}
		}
	}

	public String getInvCode() {
		return invCode;
	}

	public void setInvCode(String invCode) {
		this.invCode = invCode;
	}

	public String getCodeRequest() {
		return codeRequest;
	}

	public void setCodeRequest(String codeRequest) {
		this.codeRequest = codeRequest;
	}
	
	public List<Courses> getMyCourses() {
		if (user != null && myCourses == null) {
			myCourses = db.select(Courses.class, "select c.courseId from CoursesStudents c where c.studentId.id = ?1", 
					user.getId());
		}
		return myCourses;
	}

	public boolean isAuth() {
		return userName != null;
	}

	public boolean isForumAdmin() {
		return forumAdmin;
	}

	public void setForumAdmin(boolean forumAdmin) {
		this.forumAdmin = forumAdmin;
	}

	/**
	 * @param roles - comma separated roles
	 */
	public boolean inRole(String roles) {
		if (user == null || roles == null || roles.isEmpty())
			return false;
		String role = user.getRole();
		if (role == null)
			return false;
		if ("*".equals(roles))
			return true;
		for (String r : roles.split(","))
			if (role.equals(r))
				return true;
		return false;
	}
	
	public void checkUserRole(String r) throws IOException {
		if (!inRole(r)) {
			FacesContext.getCurrentInstance().getExternalContext()
					.responseSendError(HttpServletResponse.SC_UNAUTHORIZED, "Access forbidden!");
		}
	}

	public void checkNotNull(Object o) throws IOException {
		if (o == null) {
			FacesContext.getCurrentInstance().getExternalContext()
					.responseSendError(HttpServletResponse.SC_NOT_FOUND, "Requested resource not found!");
		}
	}

	public boolean isCourseOfUser(Courses c) {
		return user != null && getMyCourses().contains(c);
	}

	private String getUserDir() {
		return user == null? null: app.getS3Dir() + 'u' + user.getId() + '/';
	}

	/* returns URL */
	public String saveFile(String fn, InputStream is, String contentType, long contentSize) {
		String dir = getUserDir();
		if (dir == null)
			return null;
		String path = dir + fn;
		String bucket = app.getS3Bucket();
		AmazonS3Client s3 = new AmazonS3Client(app.getAwsCredentials());
		ObjectMetadata om = new ObjectMetadata();
		om.setContentType(contentType);
		om.setContentLength(contentSize);
		PutObjectRequest req = new PutObjectRequest(bucket, path, is, om);
		req.setCannedAcl(CannedAccessControlList.PublicRead);
		PutObjectResult res = s3.putObject(req);
		return s3.getUrl(bucket, path).toString();
	}
	
	public List<String> listFiles(String prefix) {
		String dir = getUserDir();
		if (dir == null)
			return null;
		AmazonS3Client s3 = new AmazonS3Client(app.getAwsCredentials());
		String bucket = app.getS3Bucket();
		String path = prefix == null? dir: dir + prefix;
		ObjectListing lst = s3.listObjects(bucket, path);
		List<String> res = new ArrayList<>();
		if (lst != null)
			for (S3ObjectSummary ob : lst.getObjectSummaries()) {
				String key = ob.getKey();
				res.add(s3.getUrl(bucket, key).toString());
			}
		return res;
	}

	public void changeForumAdmin() {
		if (inRole("admin")) {
			app.getExtUsersMap().put(extCookie, forumAdmin? 1: user.getPhpId());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Forum admin status: " + forumAdmin));
		}
	}
	
	/**
	 * @returns true if user is registered
	 */
	public boolean authorize(Users u) {
		if (u == null)
			return false;
		if (u.getEmail() != null) try {
			Users u1 = (Users) db.selectSingle("select u from Users u where u.email=?1", u.getEmail());
			if (u1 != null) {
				setUser(u1);
				return true;
			}
		} catch (Exception ex) {
			Logger.getLogger(ContextMBean.class.getName()).log(Level.SEVERE, "User search error", ex);
		}
		setUser(u);
		return false;
	}
	
	public String verifyInvCode() {
		FacesContext.getCurrentInstance().addMessage("inv_code", new FacesMessage("Access code verification -- not yet implemented..."));
		return null;
	}
	
	public String codeReq() {
		FacesContext.getCurrentInstance().addMessage("code_req", new FacesMessage("Invitation request processing -- not yet implemented..."));
		return null;
	}
}
