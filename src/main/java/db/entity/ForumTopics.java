package db.entity;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(ForumTopics.TOPIC_DTYPE)
public class ForumTopics extends Topics implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String TOPIC_DTYPE = "forum";

	public ForumTopics() {
	}

	public ForumTopics(Integer id) {
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
		if (!(object instanceof ForumTopics)) {
			return false;
		}
		ForumTopics other = (ForumTopics) object;
		if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "db.entity.ForumTopics[ id=" + getId() + " ]";
	}
	
}
