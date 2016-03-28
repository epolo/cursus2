package db.entity;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "mediatek")
public class MediatekTopics extends Topics implements Serializable {

	private static final long serialVersionUID = 1L;

	public MediatekTopics() {
	}

	public MediatekTopics(Integer id) {
		super(id);
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (getId() != null ? getId().hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof MediatekTopics)) {
			return false;
		}
		MediatekTopics other = (MediatekTopics) object;
		if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "db.entity.MediatekTopics[ id=" + getId() + " ]";
	}
	
}
