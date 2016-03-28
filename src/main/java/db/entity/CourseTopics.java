package db.entity;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue(CourseTopics.TOPIC_DTYPE)
public class CourseTopics extends Topics implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TOPIC_DTYPE = "course";

	@JoinColumn(name = "course_id", referencedColumnName = "id")
    @ManyToOne
	private Courses course;

	public CourseTopics() {
	}

	public CourseTopics(Integer id) {
		super(id);
	}

	public Courses getCourse() {
		return course;
	}

	public void setCourse(Courses course) {
		this.course = course;
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
		if (!(object instanceof CourseTopics)) {
			return false;
		}
		CourseTopics other = (CourseTopics) object;
		if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "db.entity.CourseTopics[ id=" + getId() + " ]";
	}
	
}
