/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Половников ЕА
 */
@Entity
@Table(name = "courses_students")
@NamedQueries({
	@NamedQuery(name = "CoursesStudents.findAll", query = "SELECT c FROM CoursesStudents c")})
public class CoursesStudents implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
	private Integer id;
	@Lob
    @Column(name = "answers")
	private String answers;
	@Basic(optional = false)
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	@Basic(optional = false)
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;
	@JoinColumn(name = "student_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
	private Users studentId;
	@JoinColumn(name = "course_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
	private Courses courseId;

	public CoursesStudents() {
	}

	public CoursesStudents(Integer id) {
		this.id = id;
	}

	public CoursesStudents(Integer id, Date createdAt, Date updatedAt) {
		this.id = id;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public CoursesStudents(String answers, Users studentId, Courses courseId) {
		this.answers = answers;
		this.studentId = studentId;
		this.courseId = courseId;
		Date d = new Date();
		this.createdAt = d;
		this.updatedAt = d;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAnswers() {
		return answers;
	}

	public void setAnswers(String answers) {
		this.answers = answers;
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

	public Users getStudentId() {
		return studentId;
	}

	public void setStudentId(Users studentId) {
		this.studentId = studentId;
	}

	public Courses getCourseId() {
		return courseId;
	}

	public void setCourseId(Courses courseId) {
		this.courseId = courseId;
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
		if (!(object instanceof CoursesStudents)) {
			return false;
		}
		CoursesStudents other = (CoursesStudents) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "db.entity.CoursesStudents[ id=" + id + " ]";
	}
	
}
