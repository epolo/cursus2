package web.mbeans;

import db.entity.Disciplines;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;


@FacesConverter("discipline")
public class DisciplineConverter implements Converter {
	
	@Override
	public Object getAsObject(FacesContext fc, UIComponent uic, String value) throws ConverterException {
		if (value == null || value.trim().isEmpty())
			return null;
		try {
			int id = Integer.parseInt(value);
			DbMBean db = (DbMBean) fc.getExternalContext().getApplicationMap().get("db");
			for (Disciplines d : db.getDiscList())
				if (id == d.getId())
					return d;
			throw new ConverterException("Discipline not found, ID = " + value);
		} catch(NumberFormatException nfe) {
			throw new ConverterException("Wrong discipline id format: " + value, nfe);
		}
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent uic, Object disc) throws ConverterException {
		return disc==null? null: Integer.toString(((Disciplines)disc).getId());
	}
	
}
