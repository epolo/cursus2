package web.mbeans;

import java.util.ArrayList;
import java.util.Properties;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean(name = "nav")
@RequestScoped
public class NavMBean {

	private String curPage;
	private String[] parents;
	private Properties props;
	
	public NavMBean() {
	}
	
	public String getCurPage() {
		if (curPage == null) {
			initCrumbs();
		}
		return curPage;
	}

	public String[] getParents() {
		if (parents == null)
			initCrumbs();
		return parents;
	}

	private void initCrumbs() {
		// RequestServletPath=/qqq/index.jsf
		String rsp = FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath();
		if (rsp.endsWith("index.jsf"))
			rsp = rsp.substring(0, rsp.length() - "index.jsf".length());
		if (rsp.endsWith(".jsf"))
			rsp = rsp.substring(0, rsp.length() - ".jsf".length());
		ArrayList<String> pp = new ArrayList<>();
		for (String p : rsp.split("/"))
			if (!(p == null || p.isEmpty()))
				pp.add(p);
		curPage = pp.size() > 0? pp.remove(pp.size() - 1): "";
		parents = new String[pp.size()];
		if (pp.size() > 0)
			pp.toArray(parents);
	}

	public Properties getProps() {
		if (props == null) {
			props = new Properties();
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();

			props.setProperty("ApplicationContextPath", ec.getApplicationContextPath());
			props.setProperty("RequestContextPath", ec.getRequestContextPath());
			props.setProperty("RequestServletPath", ec.getRequestServletPath());
		}
		return props;
	}
	
	
	
	
	
}
