/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Половников ЕА
 */
@Entity
@Table(name = "users")
@NamedQueries({
	@NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u")})
public class Users implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
	private Integer id;
	@Basic(optional = false)
    @Column(name = "role")
	private String role;
	@Column(name = "ggle_uid")
	private String ggleUid;
	@Column(name = "email")
	private String email;
	@Basic(optional = false)
    @Column(name = "status")
	private String status;
	@Column(name = "avatar_url")
	private String avatarUrl;
/*
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
*/
	@Column(name = "name")
	private String name;
	@Lob
    @Column(name = "personal_info")
	private String personalInfo;
	@Basic(optional = false)
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	@Basic(optional = false)
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;
	@Column(name = "old_id")
	private Integer oldId;
	@Column(name = "php_id")
	private Integer phpId;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "studentId")
	private Collection<CoursesStudents> coursesStudentsCollection;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "authorId")
	private Collection<Courses> coursesCollection;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "authorId")
	private Collection<Posts> postsCollection;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "authorId")
	private Collection<Comments> commentsCollection;

	public Users() {
	}

	public Users(Integer id) {
		this.id = id;
	}

	public Users(Integer id, String role, String status, Date createdAt, Date updatedAt) {
		this.id = id;
		this.role = role;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getGgleUid() {
		return ggleUid;
	}

	public void setGgleUid(String ggleUid) {
		this.ggleUid = ggleUid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPersonalInfo() {
		return personalInfo;
	}

	public void setPersonalInfo(String personalInfo) {
		this.personalInfo = personalInfo;
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

	public Integer getOldId() {
		return oldId;
	}

	public void setOldId(Integer oldId) {
		this.oldId = oldId;
	}

	public Integer getPhpId() {
		return phpId;
	}

	public void setPhpId(Integer phpId) {
		this.phpId = phpId;
	}

	public Collection<CoursesStudents> getCoursesStudentsCollection() {
		return coursesStudentsCollection;
	}

	public void setCoursesStudentsCollection(Collection<CoursesStudents> coursesStudentsCollection) {
		this.coursesStudentsCollection = coursesStudentsCollection;
	}

	public Collection<Courses> getCoursesCollection() {
		return coursesCollection;
	}

	public void setCoursesCollection(Collection<Courses> coursesCollection) {
		this.coursesCollection = coursesCollection;
	}

	public Collection<Posts> getPostsCollection() {
		return postsCollection;
	}

	public void setPostsCollection(Collection<Posts> postsCollection) {
		this.postsCollection = postsCollection;
	}

	public Collection<Comments> getCommentsCollection() {
		return commentsCollection;
	}

	public void setCommentsCollection(Collection<Comments> commentsCollection) {
		this.commentsCollection = commentsCollection;
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
		if (!(object instanceof Users)) {
			return false;
		}
		Users other = (Users) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "db.entity.Users[ id=" + id + " ]";
	}
	
}
