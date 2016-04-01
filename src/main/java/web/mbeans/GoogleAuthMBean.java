package web.mbeans;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "auth")
@RequestScoped
public class GoogleAuthMBean {

	@ManagedProperty("#{app}")
	private AppMBean app;

	GoogleAuthMBean() {
//		cliId = props.getProperty("oauth2.cli.id");
//		cliSecret = props.getProperty("oauth2.cli.secret");
//		redirUrl = props.getProperty("oauth2.redir.url");
	}

	public void setApp(AppMBean app) {
		this.app = app;
	}
	
	private String getProp(String name) {
		return app.getGgleProps().getProperty(name);
	}
	
	public String getReqPath() {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath();
	}
	
/*
	void redirToGoogle() {
		String redir = "https://accounts.google.com/o/oauth2/auth?client_id=" + cliId
						+ "&response_type=code&scope=profile+email&redirect_uri=" + redirUrl
						+ "&state=retrieve-google-profile&access_type=online";
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(redir);
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "Redirection to google error", ex);
		}
	}
*/	
	
}
