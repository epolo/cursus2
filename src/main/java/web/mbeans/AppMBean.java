package web.mbeans;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import db.entity.Courses;
import db.entity.Users;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(name = "app")
@ApplicationScoped
public class AppMBean {
	static final String PROPS_FILE_PARAM = "server.properties";
	static final String DEFAULT_PROPS_FILE = "etc/server.properties";
	static final String[] dirs = { "courses", "professors", "forum" };
	
	private final HashMap<String, Integer> extUsersMap = new HashMap<>();

	@ManagedProperty("#{msg}")
	private ResourceBundle msg;
	private Properties props;
	private AWSCredentials awsCredentials;
	
	String DEFAULT_USER_AVATAR;
	String DEFAULT_COURSE_AVATAR;
	
	public AppMBean() {
	}
	
	@PostConstruct
	void init() {
		DEFAULT_USER_AVATAR = msg.getString("default.user.avatar");
		DEFAULT_COURSE_AVATAR = msg.getString("default.course.avatar");
	}

	public void setMsg(ResourceBundle msg) {
		this.msg = msg;
	}
	
	
	public String[] getDirs() {
		return dirs;
	}

	public Properties getProps() {
		if (props == null) {
			props = new Properties();
			try {
				reloadProps();
			} catch (IOException ex) {
				Logger.getLogger(AppMBean.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return props;
	}

	public AWSCredentials getAwsCredentials() {
		if (awsCredentials == null) {
			getProps().getProperty("aws_access_key_id");
			awsCredentials = new BasicAWSCredentials(getProps().getProperty("aws_access_key_id"), getProps().getProperty("aws_secret_access_key"));
		}
		return awsCredentials;
	}

	String getS3Bucket() {
		return getProps().getProperty("aws_s3_bucket");
	}
	
	String getS3Dir() {
		return getProps().getProperty("aws_s3_dir");
	}

	public void reloadProps() throws IOException {
		String propFile = FacesContext.getCurrentInstance().getExternalContext()
				.getInitParameter(PROPS_FILE_PARAM);
		if (propFile == null) {
			propFile = DEFAULT_PROPS_FILE;
			System.err.println("WARN -- no web.xml parameter: " + PROPS_FILE_PARAM
				+ " -- set default properties file: " + propFile);
		}

		File f = new File(propFile);
		FileReader r = new FileReader(f);
		props.load(r);
		r.close();
		awsCredentials = null;
	}

	public String getUserAvatar(Users u) {
		String a = u.getAvatarUrl();
		return (a == null || a.isEmpty()? DEFAULT_USER_AVATAR: a);
	}

	public String getCourseAvatar(Courses c) {
		String a = c.getCoverUrl();
		return (a == null || a.isEmpty()? DEFAULT_COURSE_AVATAR: a);
	}

	public HashMap<String, Integer> getExtUsersMap() {
		return extUsersMap;
	}
}
