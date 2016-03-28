package web.mbeans;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedProperty;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

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
