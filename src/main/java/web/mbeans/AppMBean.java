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
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(name = "app")
@ApplicationScoped
public class AppMBean {
	static final String PROPS_FILE_PARAM = "server.properties";
	static final String GGLE_PROPS_FILE_PARAM = "ggle.properties";
	static final String AWS_PROPS_FILE_PARAM = "aws.properties";

	static final String[] dirs = { "courses", "professors", "forum" };

	private final HashMap<String, Integer> extUsersMap = new HashMap<>();

	@ManagedProperty("#{msg}")
	private ResourceBundle msg;
	private Properties props;
	private Properties ggleProps;
	private Properties awsProps;
	private AWSCredentials awsCredentials;
	
	public AppMBean() {
	}
	
	@PostConstruct
	void init() {
		reloadProps();
	}
	
	public void setMsg(ResourceBundle msg) {
		this.msg = msg;
	}
	
	public String[] getDirs() {
		return dirs;
	}

	public Properties getProps() {
		return props;
	}

	public Properties getGgleProps() {
		return ggleProps;
	}

	public AWSCredentials getAwsCredentials() {
		return awsCredentials;
	}

	public String getS3Bucket() {
		return awsProps.getProperty("aws_s3_bucket");
	}
	
	public String getS3Dir() {
		return awsProps.getProperty("aws_s3_dir");
	}

	
	static class PropertiesLoader {
		private static final String DEFAULT_PROPS_DIR = "/projects/cursus/etc/";
		private ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();

		Properties loadProp(String initParam) {
			String fn = FacesContext.getCurrentInstance().getExternalContext().getInitParameter(initParam);
			if (fn == null)
				fn = DEFAULT_PROPS_DIR + initParam;
			File f = new File(fn);
			Properties p = new Properties();
			try {
				p.load(new FileReader(f));
			} catch (IOException ex) {
				Logger.getLogger(AppMBean.class.getName())
						.log(Level.SEVERE, "Property load error from file: " + f.getAbsolutePath(), ex);
			}
			return p;
		}
	}
	
	public void reloadProps() {
		PropertiesLoader loader = new PropertiesLoader();
		props = loader.loadProp(PROPS_FILE_PARAM);
		ggleProps = loader.loadProp(GGLE_PROPS_FILE_PARAM);
		awsProps = loader.loadProp(AWS_PROPS_FILE_PARAM);
		awsCredentials = null == awsProps.getProperty("aws_access_key_id")? null:
				new BasicAWSCredentials(awsProps.getProperty("aws_access_key_id"), 
												awsProps.getProperty("aws_secret_access_key"));
	}

	public String getUserAvatar(Users u) {
		String a = u.getAvatarUrl();
		return (a == null || a.isEmpty()? msg.getString("default.user.avatar"): a);
	}

	public String getCourseAvatar(Courses c) {
		String a = c.getCoverUrl();
		return (a == null || a.isEmpty()? msg.getString("default.course.avatar"): a);
	}

	public HashMap<String, Integer> getExtUsersMap() {
		return extUsersMap;
	}
}
