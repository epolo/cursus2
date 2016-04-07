package web.mbeans;

import java.io.IOException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "req")
@RequestScoped
public class RequestMBean {

	@ManagedProperty(value = "#{ctx}")
	ContextMBean ctx;
	
	private String curDir;

	public RequestMBean() {
	}

	public void setCtx(ContextMBean ctx) {
		this.ctx = ctx;
	}

	public String getCurDir() {
		if (curDir == null) {
			curDir = "/";
			String s = FacesContext.getCurrentInstance().getExternalContext()
							.getRequestServletPath();
			if (s != null && s.length() > 5 && s.startsWith("/")) {
				s = s.substring(1);
				for (String d : AppMBean.dirs) {
					if (s.startsWith(d))
						if (s.charAt(d.length()) == '/') {
							curDir += d;
							break;
						}
				}
			}
		}
		return curDir;
	}
	
	public boolean isCurDir(String  d) {
		if (d == null)
			return false;
		String s = getCurDir();
		if (!d.startsWith("/"))
			s = s.substring(1);
		return  d.equals(s);
	}

	public String logout() {
		String ret = getCurDir();
		if (!ret.endsWith("/"))
			ret += '/';

		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return ret + "index";
	}

	public void checkUserRole(String r) throws IOException {
		if (!ctx.inRole(r)) {
			String dir = getCurDir();
			if (!dir.endsWith("/"))
				dir += '/';
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect(dir + "index");
		}
	}
	
	public void redirect(String url) throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect(url);
	}
}
