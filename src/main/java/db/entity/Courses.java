
package db.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "courses")
@NamedQueries({
	@NamedQuery(name = "Courses.findAll", query = "SELECT c FROM Courses c")})
public class Courses implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
	private Integer id;
	@Column(name = "title")
	private String title;
	@Column(name = "sub_title")
	private String subTitle;
	@Basic(optional = false)
    @Column(name = "students_limit")
	private int studentsLimit;
	@Column(name = "cover_url")
	private String coverUrl;
	@Basic(optional = false)
    @Column(name = "starts_at")
    @Temporal(TemporalType.TIMESTAMP)
	private Date startsAt;
	@Basic(optional = false)
    @Column(name = "ends_at")
    @Temporal(TemporalType.TIMESTAMP)
	private Date endsAt;
	@Lob
    @Column(name = "tags")
	private String tags;
	@Lob
    @Column(name = "description")
	private String description;
	@Lob
    @Column(name = "questions")
	private String questions;
	@Basic(optional = false)
    @Column(name = "is_delete")
	private boolean isDelete;
	@Basic(optional = false)
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	@Basic(optional = false)
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;
	@Basic(optional = false)
    @Column(name = "price")
	private long price;
	@Column(name = "old_id")
	private Integer oldId;
	@Column(name = "uuid")
	private String uuid;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "courseId")
	private Collection<CoursesStudents> coursesStudentsCollection;
	@OneToMany(mappedBy = "course")
	@OrderBy("createdAt ASC")
	private List<CourseTopics> topicsList;
	@JoinColumn(name = "discipline_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
	private Disciplines disciplineId;
	@JoinColumn(name = "author_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
	private Users authorId;

	public Courses() {
	}

	public Courses(Integer id) {
		this.id = id;
	}

	public Courses(Integer id, int studentsLimit, Date startsAt, Date endsAt, boolean isDelete, Date createdAt, Date updatedAt, long price) {
		this.id = id;
		this.studentsLimit = studentsLimit;
		this.startsAt = startsAt;
		this.endsAt = endsAt;
		this.isDelete = isDelete;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.price = price;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public int getStudentsLimit() {
		return studentsLimit;
	}

	public void setStudentsLimit(int studentsLimit) {
		this.studentsLimit = studentsLimit;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public Date getStartsAt() {
		return startsAt;
	}

	public void setStartsAt(Date startsAt) {
		this.startsAt = startsAt;
	}

	public Date getEndsAt() {
		return endsAt;
	}

	public void setEndsAt(Date endsAt) {
		this.endsAt = endsAt;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getQuestions() {
		return questions;
	}

	public void setQuestions(String questions) {
		this.questions = questions;
	}

	public boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public Integer getOldId() {
		return oldId;
	}

	public void setOldId(Integer oldId) {
		this.oldId = oldId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Collection<CoursesStudents> getCoursesStudentsCollection() {
		return coursesStudentsCollection;
	}

	public void setCoursesStudentsCollection(Collection<CoursesStudents> coursesStudentsCollection) {
		this.coursesStudentsCollection = coursesStudentsCollection;
	}

	public List<CourseTopics> getTopicsList() {
		return topicsList;
	}

	public void setTopicsList(List<CourseTopics> topicsList) {
		this.topicsList = topicsList;
	}

	public Disciplines getDisciplineId() {
		return disciplineId;
	}

	public void setDisciplineId(Disciplines disciplineId) {
		this.disciplineId = disciplineId;
	}

	public Users getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Users authorId) {
		this.authorId = authorId;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Courses)) {
			return false;
		}
		Courses other = (Courses) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "db.entity.Courses[ id=" + id + " ]";
	}

	
	public boolean isOpen() {
		Date d = new Date();
		return startsAt.after(d);
	}
	
	public boolean isStarted() {
		Date d = new Date();
		return startsAt.before(d) && endsAt.after(d);
	}
	
	public boolean isFinished() {
		Date d = new Date();
		return endsAt.before(d);
	}

}
