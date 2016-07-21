package web.mbeans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;

@ManagedBean(name = "test")
@ApplicationScoped
public class TestMBean {

	private String str;
	
	public TestMBean() {
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public void update() {
		if (str == null) {
			str = "Initialized";
		} else {
			str += " -- updated -- ";
		}
	}

}
