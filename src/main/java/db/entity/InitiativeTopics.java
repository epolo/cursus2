package db.entity;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(InitiativeTopics.TOPIC_DTYPE)
public class InitiativeTopics extends Topics implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TOPIC_DTYPE = "initiative";

	public InitiativeTopics() {
	}

	public InitiativeTopics(Integer id) {
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
		if (!(object instanceof InitiativeTopics)) {
			return false;
		}
		InitiativeTopics other = (InitiativeTopics) object;
		if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "db.entity.InitiativeTopics[ id=" + getId() + " ]";
	}
	
}
