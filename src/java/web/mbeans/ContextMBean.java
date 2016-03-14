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
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
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

	public ContextMBean() {
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

	public void setUserName(String userName) {
		this.userName = userName;
		user = null;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
		userName = user == null? null: user.getName();
	}

	public List<Courses> getMyCourses() {
		if (user != null && myCourses == null) {
			myCourses = db.select("select c.courseId from CoursesStudents c where c.studentId.id = ?1", 
					user.getId());
		}
		return myCourses;
	}

	public boolean isAuth() {
		return userName != null;
	}
	
	/**
	 * @param role - comma separated roles
	 */
	public boolean inRole(String role) {
		if (user == null || role == null || role.isEmpty())
			return false;
		if ("*".equals(role))
			return true;
		String r = user.getRole();
		for (String s : role.split(","))
			if (r.equals(s))
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

}
