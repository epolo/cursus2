package web.mbeans;

import db.entity.Courses;
import db.entity.Disciplines;
import db.entity.Users;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * Nav tree:
 * /
 *	index
 *	about
 *	contact
 *	invitation
 *	admin/
 *		index -- dashboard
 *	personal/
 *		index -- profile
 *		my-courses -- profile
 *	courses/ ? dis
 *		index -- list
 *		detail ? uuid
 *	professors/ ? dis
 *		index -- list
 *		detail ? uuid
 *	forum/
 *		index -- redir to /smf
 *	media/ ? res
 *		index -- list
 *		detail ? uuid
 */


@ManagedBean(name = "nav")
@ViewScoped
public class NavMBean {

	private static Logger logger = Logger.getLogger(NavMBean.class.getName());

	@ManagedProperty("#{db}")
	private DbMBean db;

	private List<Item> crumbs;
	
	public static class Item {
		private final String name;
		private final String outcome;

		public Item(String name, String outcome) {
			this.name = name;
			this.outcome = outcome;
		}

		public String getName() {
			return name;
		}

		public String getOutcome() {
			return outcome;
		}

		@Override
		public String toString() {
			return "Item{" + "name=" + name + ", outcome=" + outcome + '}';
		}
		
		
	}
	
	public NavMBean() {
	}


	public void setDb(DbMBean db) {
		this.db = db;
	}

	@PostConstruct
	public void init() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		String rsp = ec.getRequestServletPath();

		crumbs = new ArrayList<>();
		crumbs.add(new Item("home", "/index"));

		int i = rsp.indexOf('/', 1);
		if (i > 0) {
			String part = rsp.substring(1, i);
			String page = rsp.substring(i + 1);

			//				0		 1			 2			3			  4		   5
			String[] ss = { "admin", "personal", "courses", "professors", "forum", "media" };
			for (i = 0; i < ss.length && !ss[i].equals(part); i++);
			if (i < ss.length) {
				Map<String, String> params = ec.getRequestParameterMap();
				crumbs.add(new Item(part, "/" + part + "/index"));
				if (i == 1) {
					if (page.startsWith("my-courses"))
						crumbs.add(new Item("my-courses", "/personal/my-courses"));
				} else if (i == 2 || i == 3) {
					String disUid = params.get("dis");
					if (disUid != null) {
						Disciplines dis = db.findByUuid(Disciplines.class, disUid);
						if (dis == null) {
							logger.warning("Unknown discipline dis=" + disUid + " for request: " + rsp);
							disUid = null;
						} else
							crumbs.add(new Item(dis.getName(), "/" + part + "/index?dis=" + disUid));
					}
					if (page.startsWith("detail")) {
						String uid = params.get("uuid");
						if (uid != null) {
							String name = null;
							if (i == 2) {
								Courses c = db.findByUuid(Courses.class, uid);
								if (c != null)
									name = c.getTitle();
							} else {
								Users u = db.findByUuid(Users.class, uid);
								if (u != null)
									name = u.getName();
							}
							if (name != null) {
								page = "/" + part + "/detail?uuid=" + uid;
								if (disUid != null)
									page += "&dis=" + disUid;
								crumbs.add(new Item(name, page));
							} else {
								logger.warning("Unknown uuid=" + uid + " for request: " + rsp);
							}
						} else {
							logger.warning("No uid for detail request: " + rsp + " ? " + params);
						}
					}
				}
			} else {
				logger.warning("Unknown partition in request: " + rsp);
			}
		} else if (rsp.length() > 3) {
/*
 *	about
 *	contact
 *	invitation
*/
			String s = rsp.substring(1);
			String[] ss = { "about", "contact", "invitation" };
			for (i = 0; i < ss.length && !s.startsWith(ss[i]); i++);
			if (i < ss.length)
				crumbs.add(new Item(ss[i], "/" + ss[i]));
				// crumbs.add(new Item(ss[i], "/" + ss[i]));
			else
				logger.warning("Unknown page in request: " + rsp);
		}
	}

	public List<Item> getCrumbs() {
//		if (crumbs == null)
//			init();
		return crumbs;
	}
	
	
}
